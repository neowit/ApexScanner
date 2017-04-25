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

package com.neowit.apexscanner.server


import java.io.{InputStream, OutputStream}

import com.neowit.apexscanner.server.protocol.LanguageServer

/**
  * Created by Andrey Gavrikov 
  */
object StdInOutServer {
    def main(args: Array[String]): Unit = {
        val server = new StdInOutServer(System.in, System.out)
        server.start()
    }

}
// see also: https://twitter.github.io/scala_school/concurrency.html#executor for socket server example
class StdInOutServer(inStream: InputStream, outStream: OutputStream) extends LanguageServer {
    def start(): Unit = {
        val reader = new MessageReader(inStream)
        val data = reader.read()
        println("Received:")
        println(data.mkString)
    }

    override def shutdown(): Unit = {
        inStream.close()
        outStream.close()
    }
}
