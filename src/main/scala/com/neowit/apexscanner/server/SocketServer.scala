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
import java.net.{ServerSocket, Socket}
import java.util.concurrent.{ExecutorService, Executors}

import com.neowit.apexscanner.server.protocol.LanguageServer
import com.neowit.apexscanner.server.protocol.messages.NotificationMessage
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.ExecutionContext


/**
  * Created by Andrey Gavrikov
  */
object SocketServer {
    def main(args: Array[String]): Unit = {
        val server = new SocketServer(65001, 2)
        server.start()
    }

}
// see also: https://twitter.github.io/scala_school/concurrency.html#executor for socket server example
class SocketServer(port: Int, poolSize: Int) {
    val serverSocket = new ServerSocket(port)
    private val pool: ExecutorService = Executors.newFixedThreadPool(poolSize)

    def start(): Unit = {
        println(s"READY to accept connection on localhost:$port") // this line is important, otherwise client does not know that server has started
        try {
            while (true) {
                // This will block until a connection comes in.
                val socket = serverSocket.accept()
                pool.execute(new SocketLanguageServer(socket))
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

class SocketLanguageServer(socket: Socket) extends Runnable with LanguageServer with LazyLogging {
    //TODO review method of obtaining ExecutionContext
    implicit val ex: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

    private val inStream: InputStream = socket.getInputStream
    private val outStream: OutputStream = socket.getOutputStream
    private val reader = new MessageReader(inStream)
    private val writer = new MessageWriter(outStream)
    //def message = (Thread.currentThread.getName() + "\n").getBytes
    def run(): Unit = {
        while (!reader.isStreamClosed && !socket.isClosed) {
            reader.read().foreach{message =>
                //logger.debug("Received:")
                //logger.debug(message.toString)

                process(message).map {
                    case Right(response) =>
                        writer.write(response)
                    case Left(_) =>
                }

            }
        }
        shutdown()
    }


    override def sendNotification(notification: NotificationMessage): Unit = {
        writer.write(notification)
    }

    override def shutdown(): Unit = {
        socket.close()
        sys.exit(0)
    }
}
