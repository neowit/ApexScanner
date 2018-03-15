/*
 * Copyright (c) 2017 Andrey Gavrikov.
 * this file is part of tooling-force.com application
 * https://github.com/neowit/tooling-force.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.neowit.apexscanner.server.handlers

import com.neowit.apexscanner.scanner.actions.{ActionContext, ExecuteCommandActionType}
import com.neowit.apexscanner.server.protocol.{DocumentUri, LanguageServer}
import com.neowit.apexscanner.server.protocol.messages.MessageParams.ExecuteCommandParams
import com.neowit.apexscanner.server.protocol.messages.{MessageJsonSupport, RequestMessage, ResponseError, ResponseMessage}
import com.typesafe.scalalogging.LazyLogging
import io.circe.Json
//import io.circe.syntax._

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Andrey Gavrikov
  * https://microsoft.github.io/language-server-protocol/specification#workspace_executeCommand
  */
class ExecuteCommandHandler extends MessageHandler with MessageJsonSupport with LazyLogging {

    //TODO - continue here
    private def getDocumentUri(arguments: Seq[Json]): Option[DocumentUri] = {

        arguments.find{json =>
            json.findAllByKey("documentUri").nonEmpty
        }.flatMap{json =>
            json.asObject.flatMap {obj =>
                obj.toMap.get("documentUri").map {f =>
                    toDocumentUri(f)
                }
            }
        }
    }
    protected def handleImpl(server: LanguageServer, messageIn: RequestMessage)(implicit ex: ExecutionContext): Future[Either[ResponseError, ResponseMessage]] = {
        messageIn.params match {
            case Some(params) =>
                params.as[ExecuteCommandParams] match {
                    case Right(executeCommandParams) =>
                        executeCommandParams.arguments match {
                            case Some(arguments) =>
                                getDocumentUri(arguments) match {
                                    case Some(documentUri)=>
                                        documentUri.path match {
                                            case Some(filePath) =>
                                                server.getProject(filePath) match {
                                                    case Some(project) =>
                                                        // cancel all currently running Completion Actions
                                                        ActionContext.cancelAll(ExecuteCommandActionType(command = executeCommandParams.command))
                                                        // LSP does not support action Id, so have to use random value
                                                        //val context = ActionContext("LSP-" + Random.nextString(5), ExecuteCommandActionType)
                                                        // run command

                                                        server.executeCommand(messageIn.id, executeCommandParams, Option(project))
                                                    case None =>
                                                        logger.error("Failed to determine project for message: " + messageIn)
                                                        Future.successful(Right(ResponseMessage(messageIn.id, result = None, error = Option(ResponseError(code = 0, message = "Failed to determine project for message: " + messageIn)))))
                                                }
                                            case None =>
                                                logger.error("Failed to determine file path for message: " + messageIn)
                                                Future.successful(Right(ResponseMessage(messageIn.id, result = None, error = Option(ResponseError(code = 0, message = "Failed to determine file path for message: " + messageIn)))))
                                        }
                                    case None =>
                                        server.executeCommand(messageIn.id, executeCommandParams.command)
                                        //logger.info("TODO: execute command: " + executeCommandParams.command + " without arguments ")
                                        //Future.successful(Right(ResponseMessage(messageIn.id, result = None, error = None)))
                                }
                            case None =>
                                //logger.info("TODO: execute command: " + executeCommandParams.command + " without arguments ")
                                //Future.successful(Right(ResponseMessage(messageIn.id, result = None, error = None)))
                                server.executeCommand(messageIn.id, executeCommandParams.command)
                        }
                    case Left(err) =>
                        logger.error("Failed to parse message: " + messageIn + " Error: " + err)
                        Future.successful(Right(ResponseMessage(messageIn.id, result = None, error = Option(ResponseError(code = 0, message = "Failed to parse message: " + messageIn + " Error: " + err)))))
                }
            case None =>
                logger.error("Missing message params in message: " + messageIn)
                Future.successful(Right(ResponseMessage(messageIn.id, result = None, error = Option(ResponseError(code = 0, message = "Missing message params in message: " + messageIn)))))
        }
    }

}
