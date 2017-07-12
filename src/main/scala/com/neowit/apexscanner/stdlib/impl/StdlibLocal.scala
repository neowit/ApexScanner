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

package com.neowit.apexscanner.stdlib.impl


import java.io.File

import com.neowit.apexscanner.Project
import com.neowit.apexscanner.ast.QualifiedName
import com.neowit.apexscanner.nodes.AstNode
import com.neowit.apexscanner.stdlib.StandardLibrary
import io.circe.jawn._

/**
  * Created by Andrey Gavrikov 
  */
object StdlibLocal {
    def apply(file: File, project: Project): StandardLibrary = {
        val lib = new StdlibLocal(file, project)
        lib.load()
        lib
    }
}
private class StdlibLocal(file: File, project: Project) extends StandardLibrary with StdlibLocalJsonSupport {
    var _apexAPI: Option[ApexApiJson] = None
    override def findChild(name: QualifiedName): Option[AstNode] = {
        println("Checking StdLib type: " + name)
        //TODO - implement StdLib nodes
        _apexAPI match {
            case Some(api) =>
                //TODO - resolve name step by step
                //api.publicDeclarations.get()
            case None => throw new IllegalStateException("Standard Apex library is not loaded. Call StdlibLocal.load() first")
        }
        None
    }
    def load(): StdlibLocal = {
        decodeFile[ApexApiJson](file) match {
            case Left(failure) => throw new IllegalArgumentException("Failed to parse file: '" + file.getPath + "'; " + failure.getMessage)
            case Right(apexAPI) =>
                val visitor = new StdlibJsonVisitor(project)
                visitor.visit(apexAPI)
                _apexAPI = Option(apexAPI)
                //println(apexAPI.publicDeclarations.keys)
        }
        //TODO visit resulting JSON Nodes and build proper AST
        this
    }
}



