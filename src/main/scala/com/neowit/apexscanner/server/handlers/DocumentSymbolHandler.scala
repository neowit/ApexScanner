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

import com.neowit.apexscanner.nodes.{AstNode, ClassLike}
import com.neowit.apexscanner.{FileBasedDocument, Project}
import com.neowit.apexscanner.scanner.actions.{ActionContext, DocumentSymbolActionType}
import com.neowit.apexscanner.server.protocol.LanguageServer
import com.neowit.apexscanner.server.protocol.messages.MessageParams.DocumentSymbolParams
import com.neowit.apexscanner.server.protocol.messages._
import com.neowit.apexscanner.symbols._
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random
import io.circe.syntax._

/**
  * Created by Andrey Gavrikov 
  */
class DocumentSymbolHandler() extends MessageHandler with MessageJsonSupport with LazyLogging {
        override protected def handleImpl(server: LanguageServer, messageIn: RequestMessage)(implicit ex: ExecutionContext): Future[Either[ResponseError, ResponseMessage]] = {
            messageIn.params match {
                case Some(json) =>
                    json.as[DocumentSymbolParams] match {
                        case Right(params) =>
                            logger.debug(params.toString)
                            params.textDocument.uri.path match {
                                case Some(file) =>
                                    server.getProject(file) match {
                                        case Some(project) =>
                                            // cancel all currently running Completion Actions
                                            ActionContext.cancelAll(DocumentSymbolActionType)
                                            // LSP does not support action Id, so have to use random value
                                            val context = ActionContext("LSP-" + Random.nextString(5), DocumentSymbolActionType)

                                            val document = project.getFileContent(file).getOrElse(FileBasedDocument(file))
                                            val symbols: Seq[AstNode] =
                                                project.getAst(document) match {
                                                    case Some(result) =>
                                                        if (context.isCancelled) {
                                                            Seq.empty
                                                        } else {
                                                            result.rootNode.getFileNode.flatMap(_.findChildInAst(_.isInstanceOf[ClassLike])) match {
                                                                case Some(classNode) =>
                                                                    classNode.findChildrenInAst(_.isSymbol)
                                                                case None =>
                                                                    Seq.empty[AstNode]
                                                            }
                                                        }
                                                    case _ => Seq.empty
                                                }
                                            val symbolInfos = symbols.map(n => toSymbolInformation(n, project))
                                            Future.successful(Right(ResponseMessage(messageIn.id, Option(symbolInfos.asJson), error = None)))
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

    private def toSymbolInformation(node: AstNode, project: Project): SymbolInformation = {
        val symbol = node.asInstanceOf[Symbol]
        SymbolInformation(name = symbol.symbolName, kind = symbol.symbolKind, location = toLspLocation(project, node), containerName = None)
    }
}
