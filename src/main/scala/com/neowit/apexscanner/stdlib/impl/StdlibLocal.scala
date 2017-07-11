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

import com.neowit.apexscanner.ast.QualifiedName
import com.neowit.apexscanner.stdlib.{ApexAPI, StandardLibrary, StdlibNode}
import io.circe.jawn._

/**
  * Created by Andrey Gavrikov 
  */
object StdlibLocal {
    def apply(file: File): StandardLibrary = {
        val lib = new StdlibLocal(file)
        lib.load()
        lib
    }
    def main(args: Array[String]): Unit = {
        val lib = StdlibLocal(new File("/Users/andrey/development/scala/projects/ApexScanner/src/main/resources/apex-api-v40.json"))
        lib.findChild(QualifiedName(Array("System", "String"))) match {
            case Some(node) =>
                println("node")
            case None =>
                println("not found")
        }
    }
}
private class StdlibLocal(file: File) extends StandardLibrary with StdlibLocalJsonSupport{
    var _apexAPI: Option[ApexAPI] = None
    override def findChild(name: QualifiedName): Option[StdlibNode] = {
        println("Checking StdLib type: " + name)
        //TODO - implement StdLib nodes
        _apexAPI match {
            case Some(api) =>
                //TODO - resolve name step by step
                api.publicDeclarations.get()
            case None => throw new IllegalStateException("Standard Apex library is not loaded. Call StdlibLocal.load() first")
        }
        None
    }
    def load(): StdlibLocal = {
        decodeFile[ApexAPI](file) match {
            case Left(failure) => throw new IllegalArgumentException("Failed to parse file: '" + file.getPath + "'; " + failure.getMessage)
            case Right(apexAPI) =>
                _apexAPI = Option(apexAPI)
                println(apexAPI.publicDeclarations.keys)
        }
        this
    }
}



