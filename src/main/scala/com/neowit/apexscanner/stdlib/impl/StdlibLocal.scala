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
import com.neowit.apexscanner.extlib.CodeLibrary
import io.circe.jawn._


/**
  * Created by Andrey Gavrikov 
  */
object StdlibLocal {
    def apply(project: Project, fileOpt: Option[File] = None): CodeLibrary = {
        val lib = new StdlibLocal(project, fileOpt)
        //lib.load(project)
        lib
    }
}

private class StdlibLocal(project: Project, fileOpt: Option[File] = None) extends CodeLibrary with StdlibLocalJsonSupport {
    private var _isLoaded = false


    override def getName: String = "StdLib"

    override def isLoaded: Boolean = _isLoaded

    def load(project: Project): CodeLibrary = {
        if (!isLoaded) {
            val file = getSourceFile
            decodeFile[ApexApiJson](file) match {
                case Left(failure) => throw new IllegalArgumentException("Failed to parse file: '" + file.getPath + "'; " + failure.getMessage)
                case Right(apexAPI) =>
                    val visitor = new StdlibJsonVisitor(this)
                    visitor.visit(apexAPI)
            }
            _isLoaded = true
        }
        this
    }

    private def getSourceFile: File = {
        fileOpt match {
            case Some(providedFile) if providedFile.canRead => providedFile
            case None => getDefaultFile
        }
    }

    private def getDefaultFile: File = {
        val url = getClass.getClassLoader.getResource("apex-api-v40.json")
        if (null != url) {
            new File(url.toURI)
        } else {
            throw new IllegalStateException("Standard Apex Library resource is not available")
        }
    }
}



