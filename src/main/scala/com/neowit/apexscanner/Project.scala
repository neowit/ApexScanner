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
import java.nio.file.Path

import com.neowit.apexscanner.ast.{AstBuilder, AstBuilderResult, QualifiedName}
import com.neowit.apexscanner.nodes.{AstNode, HasQualifiedName}
import com.neowit.apexscanner.stdlib.StandardLibrary
import com.neowit.apexscanner.stdlib.impl.StdlibLocal

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Andrey Gavrikov 
  */
case class Project(path: Path)(implicit ex: ExecutionContext) {
    private val astBuilder: AstBuilder = new AstBuilder(this)
    private var _stdLibPath: Option[Path] = None
    private var _stdLib: Option[StandardLibrary] = None

    private val _containerByQName = new mutable.HashMap[QualifiedName, AstNode with HasQualifiedName]()
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
    }
    def getByQualifiedName(qualifiedName: QualifiedName): Option[AstNode] = {
        _containerByQName.get(qualifiedName)
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
}
