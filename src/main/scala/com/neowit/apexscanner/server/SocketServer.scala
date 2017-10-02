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

import java.net.ServerSocket
import java.util.concurrent.{ExecutorService, Executors}

import scala.concurrent.ExecutionContext


/**
  * Created by Andrey Gavrikov
  */
object SocketServer {
    implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
    def main(args: Array[String]): Unit = {
        val server = new SocketServer(65001, 2)
        server.start()
    }

}
// see also: https://twitter.github.io/scala_school/concurrency.html#executor for socket server example
class SocketServer(port: Int, poolSize: Int)(implicit val ex: ExecutionContext) {thisServer =>
    val serverSocket = new ServerSocket(port)
    private val pool: ExecutorService = Executors.newFixedThreadPool(poolSize)

    def start(): Unit = {
        println(s"READY to accept connection on localhost:$port") // this line is important, otherwise client does not know that server has started
        try {
            while (true) {
                // This will block until a connection comes in.
                val socket = serverSocket.accept()
                //pool.execute(new SocketLanguageServer(socket))
                val langServer = new LanguageServerDefault(socket.getInputStream, socket.getOutputStream) with Runnable {
                    override implicit val ex: ExecutionContext = thisServer.ex

                    override protected def isConnectionOpen: Boolean = super.isConnectionOpen && !socket.isClosed

                    override def shutdown(): Unit = {
                        socket.close()
                        super.shutdown()
                    }

                    override def run(): Unit = start()

                    override def start(): Unit = {
                        logger.debug("Starting SocketServer")
                        super.start()
                    }
                }
                pool.execute(langServer)
            }
        } finally {
            shutdown()
        }
    }

    def shutdown(): Unit = {
        if (!pool.isShutdown)
            pool.shutdown()
    }
}


