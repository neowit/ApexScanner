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

package com.neowit.apexscanner.server.handlers

import com.neowit.apexscanner.server.protocol.LanguageServer
import com.neowit.apexscanner.server.protocol.messages.NotificationMessage
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Andrey Gavrikov 
  */
trait NotificationHandler extends LazyLogging {
    protected def handleImpl(server: LanguageServer, messageIn: NotificationMessage)(implicit ex: ExecutionContext): Future[Unit]

    def handle(server: LanguageServer, messageIn: NotificationMessage)(implicit ex: ExecutionContext): Future[Unit] = {
        handleImpl(server, messageIn).recover{
            case e: Throwable =>
                logger.error(e.toString)
        }
    }
}
