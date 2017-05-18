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

import java.nio.file.{FileSystems, Path}

import com.neowit.apexscanner.ast.{AstBuilder, AstBuilderResult, QualifiedName}
import com.neowit.apexscanner.nodes.{AstNode, HasQualifiedName}
import com.neowit.apexscanner.stdlib.StandardLibrary
import com.neowit.apexscanner.stdlib.impl.StdLibLocal

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Andrey Gavrikov 
  */
case class Project(path: Path)(implicit ex: ExecutionContext) {
    private val astBuilder: AstBuilder = new AstBuilder(this)
    private var _stdLib: Option[StandardLibrary] = None

    private val _containerByQName = new mutable.HashMap[QualifiedName, AstNode with HasQualifiedName]()

    def getStandardLibrary: StandardLibrary = {
        _stdLib match {
          case Some(lib) => lib
          case None =>
              val lib = loadStandardLibrary()
              _stdLib = Option(lib)
             lib
        }
    }

    private def loadStandardLibrary(): StandardLibrary = {

        // TODO implement proper resolution of path to STD Library
        val stdLibDir = "TODO"
        val path = FileSystems.getDefault.getPath(stdLibDir)
        new StdLibLocal(path)
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

    def getAst(path: Path): Future[Option[AstBuilderResult]] = {
        astBuilder.getAst(path) match {
          case Some(_ast) => Future.successful(Option(_ast))
          case None =>
              // looks like AST for given file has not been built yet, let's fix it
              astBuilder.build(path).map { _ =>
                  astBuilder.getAst(path)
              }
        }
    }
}
