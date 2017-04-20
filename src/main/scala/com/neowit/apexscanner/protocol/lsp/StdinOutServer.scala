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

package com.neowit.apexscanner.protocol.lsp

import java.util.concurrent.{ExecutorService, Executors}

/**
  * Created by Andrey Gavrikov 
  */
object StdinOutServer {
    def main(args: Array[String]): Unit = {
        val server = new StdinOutServer(2)
        server.run()
    }

}
// see also: https://twitter.github.io/scala_school/concurrency.html#executor for socket server example
class StdinOutServer(poolSize: Int) extends LanguageServer {
    private val pool: ExecutorService = Executors.newFixedThreadPool(poolSize)

    def run(): Unit = {
        try {
            while (true) {
                // This will block until a connection comes in.
                val msg = scala.io.StdIn.readLine()
                pool.execute(new SingleMessageProcessor(msg, getHandler(msg)))
            }
        } finally {
            shutdown()
        }
    }

    override def shutdown(): Unit = {
        if (!pool.isShutdown)
            pool.shutdown()
    }
}

class SingleMessageProcessor(val messageIn: String, handler: MessageHandler) extends Runnable {

    def run(): Unit = {
        println("Received: " + messageIn)

        println("Result: " + handler.handle(messageIn))
    }
}
