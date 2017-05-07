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


import java.nio.file.{Files, Path}

import com.neowit.apexscanner.Project
import com.neowit.apexscanner.scanner.FileScanResult
import com.neowit.apexscanner.scanner.actions.{SyntaxChecker, SyntaxError}
import com.neowit.apexscanner.server.protocol.{Diagnostic, LanguageServer, PublishDiagnosticsParams}
import com.neowit.apexscanner.server.protocol.messages.MessageParams.DidSaveParams
import com.neowit.apexscanner.server.protocol.messages._
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{ExecutionContext, Future}
import io.circe.syntax._


/**
  * Created by Andrey Gavrikov 
  */
class DidSaveHandler extends NotificationHandler with MessageJsonSupport with LazyLogging {

    protected override def handleImpl(server: LanguageServer, messageIn: NotificationMessage)(implicit ex: ExecutionContext): Future[Unit] = {
        messageIn.params match {
            case Some(params) =>
                params.as[DidSaveParams] match {
                    case Right(didSaveParams) =>
                        didSaveParams.textDocument.getPath match {
                            case Some(filePath) =>
                                server.getProject(filePath) match {
                                    case Some(project) =>
                                        /*
                                        checkSyntax(project, filePath).onComplete {
                                            case Success(res) =>
                                                logger.debug("success" + res)
                                            case Failure(e) =>
                                                logger.debug("failure: " + e)
                                        }
                                        */
                                        checkSyntax(project, filePath).map{errorsByFile =>
                                            val notifications = generateNotifications(errorsByFile)
                                            notifications.foreach{notificationParams =>
                                                val diagnosticNotification =
                                                    NotificationMessage("textDocument/publishDiagnostics", Option(notificationParams.asJson))
                                                server.sendNotification(diagnosticNotification)
                                            }
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

    private def checkSyntax(project: Project, file: Path)(implicit ex: ExecutionContext): Future[Map[Path, Seq[SyntaxError]]] = {
        val errorBuilder = Map.newBuilder[Path, Seq[SyntaxError]]

        def onFileCheckResult(scanResult: FileScanResult):Unit = {
            val file: Path = scanResult.sourceFile
            val errors = scanResult.errors
            if (errors.nonEmpty) {
                errorBuilder += file -> errors
            }
            //val fileName = file.getName(file.getNameCount-1).toString
            //println("Checking " + fileName)
            //errors.foreach(e =>  assert(false, "\n" + file.toString + s"\n=> (${e.line}, ${e.charPositionInLine}): " + e.msg))
        }
        val checker = new SyntaxChecker()
        Future{
            checker.check(file, file => !DidSaveHandler.isSupportedPath(file), onFileCheckResult)
            errorBuilder.result()
        }
    }

    private def generateNotifications(errorsByFile: Map[Path, Seq[SyntaxError]]): Iterable[NotificationMessage] = {
        errorsByFile.map{case (file, errors) => generateNotification(file, errors)}
    }

    private def generateNotification(file: Path, errors: Seq[SyntaxError]): NotificationMessage = {
        val url = file.toUri.getRawPath
        val diagnostics = errors.map(syntaxError => Diagnostic( syntaxError ) )

        val params = PublishDiagnosticsParams(url, diagnostics.toArray)
        NotificationMessage("textDocument/publishDiagnostics", Option(params.asJson))
    }
}

object DidSaveHandler {

    def isSupportedPath(path: Path): Boolean = {
        val isDirectory = Files.isDirectory(path)
        val fileName = path.getName(path.getNameCount-1).toString
        if (isDirectory) {
            false // directories not supported
        } else {
            //regular file
            if (fileName.endsWith(".cls") || fileName.endsWith(".trigger")) {
                if (fileName.contains("__")) { // exclude classes with namespace <Namespace>__classname.cls, they do not have apex code
                    false // ignored file
                } else {
                    true
                }

            } else {
                false // ignored file due to wrong extension
            }
        }
    }

}

