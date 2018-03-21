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

import java.nio.file.{Path, Paths}

import com.neowit.apexscanner.Project
import com.neowit.apexscanner.nodes.{AstNode, Location, Position, Range}
import com.neowit.apexscanner.server.protocol.LanguageServer
import com.neowit.apexscanner.server.protocol.messages.{ErrorCodes, RequestMessage, ResponseError, ResponseMessage}
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Andrey Gavrikov 
  */
trait MessageHandler extends LazyLogging {
    protected def handleImpl(server: LanguageServer, messageIn: RequestMessage)(implicit ex: ExecutionContext): Future[ResponseMessage]

    def handle(server: LanguageServer, messageIn: RequestMessage)(implicit ex: ExecutionContext): Future[ResponseMessage] = {
        handleImpl(server, messageIn).recover{
            case e: Throwable =>
                logger.error(e.toString)
                ResponseMessage(messageIn.id, result = None, error = Option(ResponseError(ErrorCodes.InternalError, e.getMessage)))
        }
    }

    def fromLspToAntlr4Position(p: Position): Position = {
        // Line and Column in LSP are zero based
        // while ANTLR uses: Line starting with 1, column starting with 0
        p.copy(line = p.line +1)
    }
    def fromAntlr4ToLspPosition(p: Position): Position = {
        // Line and Column in LSP are zero based
        // while ANTLR uses: Line starting with 1, column starting with 0
        p.copy(line = p.line -1)
    }

    def toLspLocation(proj: Project, astNode: AstNode): Location = {
        new Location {
            override def project: Project = proj

            // Line and Column in LSP are zero based
            // while ANTLR uses: Line starting with 1, column starting with 0
            override def range: Range = {
                val start = astNode.range.start
                val end = astNode.range.end
                Range(fromAntlr4ToLspPosition(start), fromAntlr4ToLspPosition(end), Position(0, 0))
            }

            override def path: Path = {
                astNode.getFileNode.map(_.file) match {
                    case Some(_path) => _path
                    case None => Paths.get("")
                }
            }
        }
    }

}
