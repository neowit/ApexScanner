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

import com.neowit.apexscanner.FileBasedDocument
import com.neowit.apexscanner.scanner.actions.{ActionContext, ListCompletions, ListCompletionsActionType}
import com.neowit.apexscanner.server.protocol.LanguageServer
import com.neowit.apexscanner.server.protocol.messages.MessageParams.TextDocumentPositionParams
import com.neowit.apexscanner.server.protocol.messages._
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{ExecutionContext, Future}
import io.circe.syntax._

import scala.util.Random

/**
  * Created by Andrey Gavrikov 
  */
class CompletionHandler() extends MessageHandler with MessageJsonSupport with LazyLogging {
    override protected def handleImpl(server: LanguageServer, messageIn: RequestMessage)(implicit ex: ExecutionContext): Future[Either[ResponseError, ResponseMessage]] = {
        messageIn.params match {
          case Some(json) =>
              json.as[TextDocumentPositionParams]  match {
                  case Right(params) =>
                      logger.debug(params.toString)
                      params.textDocument.uri.path match {
                        case Some(file) =>
                            server.getProject(file) match {
                              case Some(project) =>
                                  // cancel all currently running Completion Actions
                                  ActionContext.cancelAll(ListCompletionsActionType)
                                  // LSP does not support action Id, so have to use random value
                                  val context = ActionContext("LSP-" + Random.nextString(5), ListCompletionsActionType)

                                  val completions = new ListCompletions(project)
                                  // Line and Column in LSP are zero based
                                  // while ANTLR uses: Line starting with 1, column starting with 0
                                  val position = fromLspToAntlr4Position(params.position)
                                  val document = project.getFileContent(file).getOrElse(FileBasedDocument(file))
                                  val res = completions.list(document, position.line, position.col, context)
                                  val completionItems = res.options.map(CompletionItem(_))
                                  Future.successful(Right(ResponseMessage(messageIn.id, Option(completionItems.asJson), error = None)))


                              case None =>
                                  Future.successful(Left(ResponseError(ErrorCodes.InvalidParams, "Project not found by path: " + file.toString)))
                            }
                        case None =>
                              Future.successful(Left(ResponseError(ErrorCodes.InvalidParams, "Document path not specified")))
                      }
                  case Left(err) =>
                      Future.successful(Left(ResponseError(ErrorCodes.InvalidParams, s"Failed to parse message: $messageIn. Error: $err")))
              }
          case None =>
              Future.successful(Left(ResponseError(ErrorCodes.InvalidParams, s"Failed to parse message: $messageIn. Missing params.")))
        }
    }
}
