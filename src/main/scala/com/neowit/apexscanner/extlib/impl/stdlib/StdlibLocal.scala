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

package com.neowit.apexscanner.extlib.impl.stdlib

import java.io.{File, FileInputStream, InputStream}
import java.nio.ByteBuffer

import com.neowit.apexscanner.Project
import com.neowit.apexscanner.ast.QualifiedName
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
            val source = toByteBuffer(getSourceStream)
            decodeByteBuffer[ApexApiJson](source) match {
                case Left(failure) => throw new IllegalArgumentException("StdLib: Failed to parse source file; " + failure.getMessage)
                case Right(apexAPI) =>
                    val visitor = new StdlibJsonVisitor(this)
                    visitor.visit(apexAPI)
            }
            _isLoaded = true
        }
        this
    }

    override protected def getDefaultNamespaces: Seq[QualifiedName] = {
        Seq(
            QualifiedName("System"), //e.g. Test.isRunningTest() instead of Test.isRunningTest()
        )
    }

    private def getSourceStream: InputStream = {
        fileOpt match {
            case Some(providedFile) if providedFile.canRead => new FileInputStream(providedFile)
            case None => getDefaultStream
        }
    }

    private def getDefaultStream: InputStream = {
        // when reading resource from inside .jar have use getResourceAsStream() instead of getResource()
        val is = getClass.getClassLoader.getResourceAsStream("apex-api-v40.json")
        if (null != is) {
            is
        } else {
            throw new IllegalStateException("Standard Apex Library resource is not available")
        }

    }

    private def toByteBuffer(is: InputStream): ByteBuffer = {
        val bytesArray = Iterator.continually(is.read).takeWhile(_ != -1).map(_.toByte).toArray
        ByteBuffer.wrap(bytesArray)
    }

}



