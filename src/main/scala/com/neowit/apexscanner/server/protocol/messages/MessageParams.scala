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

package com.neowit.apexscanner.server.protocol.messages

import java.nio.file.Path

import com.neowit.apexscanner.nodes.Position
import com.neowit.apexscanner.server.protocol.DocumentUri
import io.circe.Json


/**
  * Created by Andrey Gavrikov 
  */
object MessageParams {
    //type DocumentUri = String
    type LanguageId = String

    /**
      * part of cancelRequest notification
      * @param id The request id to cancel.
      */
    case class CancelParams(id: Int)

    /**
      * part of initialize request
      */
    case class ClientCapabilities(workspace: Option[WorkspaceClientCapabilities], textDocument: Option[TextDocumentClientCapabilities])

    case class TextDocument(uri: DocumentUri, languageId: Option[LanguageId], version: Option[Int], text: Option[String]) {
        def getPath: Option[Path] = {
            //Try {
            //    Paths.get(URI.create(uri))
            //}.toOption
            uri.path
        }
    }

    case class TextDocumentIdentifier(uri: DocumentUri)


    sealed trait MessageParams

    case class InitializeParams(processId: Int, rootUri: DocumentUri, trace: String,
                                capabilities: ClientCapabilities, initializationOptions: Option[Json]) extends MessageParams

    case class DidSaveParams(textDocument: TextDocument) extends MessageParams

    case class CompletionParams(position: Position, textDocument: TextDocument) extends MessageParams
}
