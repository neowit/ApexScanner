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
import com.neowit.apexscanner.nodes._
import com.neowit.apexscanner.resolvers.AscendingDefinitionFinder
import com.neowit.apexscanner.scanner.actions.{ActionContext, FindSymbolActionType}
import com.neowit.apexscanner.server.protocol.LanguageServer
import com.neowit.apexscanner.server.protocol.messages.MessageParams.TextDocumentPositionParams
import com.neowit.apexscanner.server.protocol.messages._
import com.typesafe.scalalogging.LazyLogging
import io.circe.syntax._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

/**
  * Created by Andrey Gavrikov 
  */
class DefinitionHandler() extends MessageHandler with MessageJsonSupport with LazyLogging {
    override protected def handleImpl(server: LanguageServer, messageIn: RequestMessage)(implicit ex: ExecutionContext): Future[ResponseMessage] = {
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
                                  ActionContext.cancelAll(FindSymbolActionType)
                                  // LSP does not support action Id, so have to use random value
                                  val context = ActionContext("LSP-" + Random.nextString(5), FindSymbolActionType)

                                  // Line and Column in LSP are zero based
                                  // while ANTLR uses: Line starting with 1, column starting with 0
                                  val position = fromLspToAntlr4Position(params.position)
                                  val document = project.getFileContent(file).getOrElse(FileBasedDocument(file))
                                  val locations: Seq[Location] =
                                      project.getAst(document) match {
                                          case Some(result) =>
                                              if (context.isCancelled) {
                                                  Seq.empty
                                              } else {
                                                  val finder = new AscendingDefinitionFinder(context)
                                                  val allDefNodes = finder.findUltimateDefinition(result.rootNode, position, project)
                                                  val _locations = allDefNodes.filter {
                                                      case defNode: AstNode if Range.INVALID_LOCATION != defNode.range => true
                                                      case _ => false
                                                  }.map { defNode => toLspLocation(project, defNode) }
                                                  _locations
                                              }

                                          case _ => Seq.empty
                                      }
                                  Future.successful(ResponseMessage(messageIn.id, Option(locations.asJson), error = None))
                              case None =>

                                  val error = ResponseError(ErrorCodes.InvalidParams, "Project not found by path: " + file.toString)
                                  Future.successful(ResponseMessage(messageIn.id, result = None, Option(error)))
                            }
                        case None =>
                            val error = ResponseError(ErrorCodes.InvalidParams, "Document path not specified")
                            Future.successful(ResponseMessage(messageIn.id, result = None, Option(error)))
                      }
                  case Left(err) =>
                      val error = ResponseError(ErrorCodes.InvalidParams, s"Failed to parse message: $messageIn. Error: $err")
                      Future.successful(ResponseMessage(messageIn.id, result = None, Option(error)))
              }
          case None =>
              val error = ResponseError(ErrorCodes.InvalidParams, s"Failed to parse message: $messageIn. Missing params.")
              Future.successful(ResponseMessage(messageIn.id, result = None, Option(error)))
        }
    }


}
