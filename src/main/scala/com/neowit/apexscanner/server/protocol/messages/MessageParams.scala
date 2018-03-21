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

    case class TextDocument(uri: DocumentUri, languageId: Option[LanguageId], version: Option[Int], text: Option[String])


    case class TextDocumentIdentifier(uri: DocumentUri)
    case class VersionedTextDocumentIdentifier(uri: DocumentUri, version: Int)


    sealed trait MessageParams

    case class InitializeParams(processId: Int, rootUri: DocumentUri, trace: String,
                                capabilities: ClientCapabilities, initializationOptions: Option[Json]) extends MessageParams

    case class DidSaveParams(textDocument: TextDocument) extends MessageParams

    case class CompletionParams(position: Position, textDocument: TextDocument) extends MessageParams
    case class TextDocumentPositionParams(position: Position, textDocument: TextDocument) extends MessageParams
    case class DocumentSymbolParams(textDocument: TextDocumentIdentifier) extends MessageParams


    case class DidChangeTextDocumentParams (
         /**
           * The document that did change. The version number points
           * to the version after all provided content changes have
           * been applied.
           */
         textDocument: VersionedTextDocumentIdentifier,

         /**
           * The actual content changes.
           */
         contentChanges: Seq[TextDocumentContentChangeEvent]
                                           ) extends MessageParams

    case class TextDocumentContentChangeEvent (
          /**
            * The range of the document that changed.
            */
          //range: Option[Range],

          /**
            * The length of the range that got replaced.
            */
          rangeLength: Option[Int],

          /**
            * The new text of the range/document.
            */
          text: String
                                              )

    case class ExecuteCommandParams (
        /**
          * The identifier of the actual command handler.
          */
        command: String,
        /**
          * Arguments that the command should be invoked with.
          * - in the current implementation Array[ExecuteCommandArguments] always contains single argument
          * that single argument contains JSON object with all relevant {name: value} parameters
          */
        arguments: Option[Seq[Json]]

    ) extends MessageParams


    sealed trait NotificationMessageParams {
        /**
          * The message type. See {@link MessageType}
          */
        def `type`: MessageType

        /**
          * The actual message
          */
        def message: String
    }

    case class ShowMessageParams (
        `type`: MessageType,
        message: String
    ) extends NotificationMessageParams

    case class LogMessageParams (
        `type`: MessageType,
        message: String
    ) extends NotificationMessageParams
}
