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

import com.neowit.apexscanner.TextBasedDocument
import com.neowit.apexscanner.server.protocol.LanguageServer
import com.neowit.apexscanner.server.protocol.messages.{MessageJsonSupport, NotificationMessage}
import com.neowit.apexscanner.server.protocol.messages.MessageParams.DidChangeTextDocumentParams
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Andrey Gavrikov 
  */
class DidChangeHandler extends NotificationHandler with MessageJsonSupport with LazyLogging {
    override protected def handleImpl(server: LanguageServer, messageIn: NotificationMessage)(implicit ex: ExecutionContext): Future[Unit] = {
        messageIn.params match {
            case Some(params) =>
                params.as[DidChangeTextDocumentParams] match {
                    case Right(didSaveParams) =>
                        didSaveParams.textDocument.uri.path match {
                            case Some(filePath) =>
                                server.getProject(filePath) match {
                                    case Some(project) =>
                                        // save current document content in temp storage for later use
                                        didSaveParams.contentChanges.lastOption match {
                                          case Some(lastVersion) =>
                                              project.saveFileContent(TextBasedDocument(lastVersion.text, filePath))
                                          case None =>
                                              Future.successful(logger.error("Missing file content in message: " + messageIn))
                                        }
                                    case None =>
                                        Future.successful(logger.error("Failed to determine project for message: " + messageIn))
                                }
                            case None =>
                                Future.successful(logger.error("Failed to determine file path for message: " + messageIn))
                        }
                    case Left(err) =>
                        Future.successful(logger.error("Failed to parse message: " + messageIn + " Error: " + err))
                }
            case None =>
                Future.successful(logger.error("Missing message params in message: " + messageIn))
        }
    }
}
