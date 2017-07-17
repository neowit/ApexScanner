/*
 * Copyright (c) 2017 Andrey Gavrikov.
 *
 * This file is part of https://github.com/neowit/apexscanner
 *
 *  This file is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This file is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 */

package com.neowit.apexscanner

import java.io.File
import java.nio.file._

import com.neowit.apexscanner.ast.{AstBuilder, AstBuilderResult, QualifiedName}
import com.neowit.apexscanner.nodes.{AstNode, HasQualifiedName, NamespaceNode}
import com.neowit.apexscanner.scanner.Scanner
import com.neowit.apexscanner.stdlib.StandardLibrary
import com.neowit.apexscanner.stdlib.impl.StdlibLocal

import scala.annotation.tailrec
import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Andrey Gavrikov 
  */
object Project {

    val defaultIsIgnoredPath: Path => Boolean = Scanner.defaultIsIgnoredPath
    def getClassOrTriggerMatcher(name: String): PathMatcher = {
        FileSystems.getDefault.getPathMatcher("""regex:(?i).*\b""" + name + """\.(cls|trigger)$""")
    }

    /**
      * using provided path try to find src (root) project folder
      * project root is usually same folder that contains package.xml file
      * @param path starting from provided path
      * @return
      */
    def findApexProjectRoot(path: Path): Option[Path] = {
        findApexProjectRoot(path, maxDepth = 10)
    }

    /**
      *
      * @param path start path
      * @param maxDepth if current path is not project root then maxDepth (if >=0) tells how many levels up we are
      *                 allowed to go up the file hierarchy
      * @return
      */
    @tailrec
    private def findApexProjectRoot(path: Path, maxDepth: Int): Option[Path] = {
        if (null == path || maxDepth < 0) {
            None
        } else if ( isProjectRoot(path) ) {
            Option(path)
        } else {
            // go up
            val parent = path.getParent
            findApexProjectRoot(parent, maxDepth - 1)
        }
    }

    private def isProjectRoot(path: Path): Boolean = {
        // check if path points to a folder that either has name "src" or contains file package.xml
        null != path && path.toFile.isDirectory && (path.endsWith("src") || new File(path.toString, "package.xml").canRead)
    }
}

/**
  * @param path must point to Apex project root, @see also Project.findApexProjectRoot(path)
  *             no check is made to ensure that provided path points to correct folder
  */
case class Project(path: Path)(implicit ex: ExecutionContext) {
    private val astBuilder: AstBuilder = new AstBuilder(this)
    private var _stdLibPath: Option[Path] = None
    private var _stdLib: Option[StandardLibrary] = None

    private val _containerByQName = new mutable.HashMap[QualifiedName, AstNode with HasQualifiedName]()
    private val _namespaceByQName = new mutable.HashMap[QualifiedName, NamespaceNode]()
    private val _fileContentByPath = new mutable.HashMap[Path, VirtualDocument]()

    def getStandardLibrary: StandardLibrary = {
        _stdLib match {
          case Some(lib) => lib
          case None =>
              val lib = loadStandardLibrary()
              _stdLib = Option(lib)
             lib
        }
    }

    /**
      * @param path custom path to STD Lib JSON file
      * val path = FileSystems.getDefault.getPath(stdLibDir)
      */
    def setStdLibPath(path: Path): Unit = {
        if (!path.toFile.canRead) {
            throw new IllegalArgumentException(s"Provided Standard Library path: '${path.toString}' is not readable. Default library will be used.")
        }
        _stdLibPath = Option(path)
    }

    def getStdLibPath: Option[Path] = _stdLibPath

    private def loadStandardLibrary(): StandardLibrary = {

        val file =
            _stdLibPath match {
                case Some(providedPath) => providedPath.toFile
                case None =>
                    val url = getClass.getClassLoader.getResource("apex-api-v40.json")
                    if (null != url) {
                        new File(url.toURI)
                    } else {
                        throw new IllegalStateException("Standard Apex Library resource is not available")
                    }
                //val doc = scala.io.Source.fromInputStream(is.openStream())("UTF-8").getLines().mkString

            }
        StdlibLocal(file, this)
    }

    def saveFileContent(document: VirtualDocument): Future[Unit] = {
        _fileContentByPath += document.file -> document
        Future.successful(())
    }
    def getFileContent(file: Path): Option[VirtualDocument] = {
        _fileContentByPath.get(file)
    }
    def clearFileContent(file: Path): Future[Unit] = {
        _fileContentByPath -= file
        Future.successful(())
    }

    /**
      * add given class/interface/trigger to global map of top level containers
      * @param node usually class/interface/trigger
      */
    def addByQualifiedName(node: AstNode with HasQualifiedName): Unit = {
        node.qualifiedName match {
            case Some(qName) => _containerByQName += qName -> node
            case None =>
        }
        // check if this node is a Namespace
        node match {
            case n @ NamespaceNode(Some(name)) =>
                n.qualifiedName.foreach(qName => _namespaceByQName += qName -> n)
            case _ => // do nothing
        }
    }

    /**
      * NOTE: this method only finds nodes physically added via addByQualifiedName()
      * It will not find class methods/variables or Enum constants,
      * when target node type is not already known to be Namespace or Class - use QualifiedNameDefinitionFinder
      */
    def getByQualifiedName(qualifiedName: QualifiedName): Option[AstNode] = {
        _containerByQName.get(qualifiedName).orElse{
            //check if this name is in one of available namespaces
            // first try to find namespace which contains class with name == qualifiedName
            val namespaceQNameOpt =
                _namespaceByQName.keySet.find{ qName =>
                        val fullName = QualifiedName(qName, qualifiedName)
                        _containerByQName.contains(fullName)
                }
            // retrieve class node fro namespace (if namespace found)
            namespaceQNameOpt match {
                case Some(namespaceQName) =>
                    val fullName = QualifiedName(namespaceQName, qualifiedName)
                    _containerByQName.get(fullName)
                case None => None
            }
        }
    }

    def getAst(document: VirtualDocument): Future[Option[AstBuilderResult]] = {
        astBuilder.getAst(document) match {
          case Some(_ast) => Future.successful(Option(_ast))
          case None =>
              // looks like AST for given file has not been built yet, let's fix it
              astBuilder.build(document).map { _ =>
                  astBuilder.getAst(document)
              }
        }
    }

    /**
      * check if given name corresponds to a valid apex file (.cls, .trigger) inside current project
      * if it does then return document instance
      * @param name potential document/file name (without extension), e.g. MyClass
      * @return
      */
    def getDocumentByName(name: String): Option[VirtualDocument] = {
        import scala.collection.JavaConverters._
        val rootPath = this.path
        val matcher = Project.getClassOrTriggerMatcher(name)
        val isIgnoredPath: Path => Boolean = Project.defaultIsIgnoredPath
        val paths = Files.walk(rootPath).iterator().asScala.filter(file => matcher.matches(file) && !isIgnoredPath(file))
        paths.toList.headOption match {
            case Some(foundPath) => Option(FileBasedDocument(foundPath))
            case None => None
        }
    }

}
