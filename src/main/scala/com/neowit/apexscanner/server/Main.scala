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

import com.typesafe.scalalogging.LazyLogging

//import ch.qos.logback.classic.{Level, LoggerContext}
//import org.slf4j.LoggerFactory

/**
  * Created by Andrey Gavrikov 
  */
object Main extends LazyLogging {
    case class Config (
                          communicationMethod:String = "socket",
                          host:String = "localhost",
                          port:Int = 65001
                      )

    def main(args: Array[String]): Unit = {
        logger.debug("command line: " + args.mkString(" "))
        val parser = new scopt.OptionParser[Config]("ApexScanner") {
            head("ApexScanner", "0.0.1")

            opt[String]('c', "communicationMethod").action( (x, c) =>
                c.copy(communicationMethod = x) ).text("Connection Type - socket | stdio")
            opt[String]('h', "host").action( (x, c) =>
                c.copy(host = x) ).text("Host address. Ignored if Connection Type is not socket")
            opt[Int]('p', "port").action( (x, c) =>
                c.copy(port = x) ).text("Socket Port. Ignored if Connection Type is not socket")

        }
        parser.parse(args, Config()) match {
          case Some(config) =>
              config.communicationMethod match {
                  case "socket" =>
                      val port = config.port
                      val server = new SocketServer(port, 2)
                      server.start()
                  case "stdio" =>
                      // when using STDIO we have to disable STDOUT log otherwise LSP Client gets confused
                      //val loggerContext: LoggerContext = LoggerFactory.getILoggerFactory.asInstanceOf[LoggerContext]
                      //val rootLogger = loggerContext.getLogger("root")
                      //rootLogger.setLevel(Level.DEBUG)
                      System.setProperty("STDOUT_LEVEL", "debug")

                      val server = new StdInOutServer(System.in, System.out)
                      server.start()
                  case _ =>
              }
          case None =>
                // bad config
        }
    }

}
