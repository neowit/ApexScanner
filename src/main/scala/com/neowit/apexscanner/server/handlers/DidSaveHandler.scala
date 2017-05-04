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
import com.neowit.apexscanner.server.protocol.messages.MessageParams.DidSaveParams
import com.neowit.apexscanner.server.protocol.messages._


/**
  * Created by Andrey Gavrikov 
  */
class DidSaveHandler extends NotificationHandler with MessageJsonSupport {
    override def handle(server: LanguageServer, messageIn: NotificationMessage): Unit = {
        messageIn.params match {
          case Some(params) =>
              params.as[DidSaveParams] match {
                case Right(didSaveParams) =>
                    didSaveParams.textDocument.getPath match {
                      case Some(filePath) =>
                      case None =>
                            // failed to determine file path
                    }
                case Left(err) =>
              }
          case None =>
        }
    }
}

