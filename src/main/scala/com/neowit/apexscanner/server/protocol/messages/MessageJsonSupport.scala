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

import com.neowit.apexscanner.server.protocol.messages.MessageParams._
import io.circe._
import io.circe.generic.semiauto._
/**
  * Created by Andrey Gavrikov 
  */
trait MessageJsonSupport {
    implicit val RequestMessageDecoder: Decoder[RequestMessage] = deriveDecoder
    implicit val NotificationMessageDecoder: Decoder[NotificationMessage] = deriveDecoder

    implicit val CompletionOptionsEncoder: Encoder[CompletionOptions] = deriveEncoder
    implicit val SignatureHelpOptionsEncoder: Encoder[SignatureHelpOptions] = deriveEncoder
    implicit val CodeLensOptionsEncoder: Encoder[CodeLensOptions] = deriveEncoder
    implicit val DocumentOnTypeFormattingOptionsEncoder: Encoder[DocumentOnTypeFormattingOptions] = deriveEncoder
    implicit val SaveOptionsEncoder: Encoder[SaveOptions] = deriveEncoder
    implicit val TextDocumentSyncOptionsEncoder: Encoder[TextDocumentSyncOptions] = deriveEncoder
    implicit val ServerCapabilitiesEncoder: Encoder[ServerCapabilities] = deriveEncoder

    implicit val ResponseErrorEncoder: Encoder[ResponseError] = deriveEncoder
    implicit val ResponseMessageEncoder: Encoder[ResponseMessage] = deriveEncoder


    implicit val WorkspaceClientCapabilitiesDecoder: Decoder[WorkspaceClientCapabilities] = deriveDecoder
    implicit val TextDocumentClientCapabilitiesDecoder: Decoder[TextDocumentClientCapabilities] = deriveDecoder
    implicit val ClientCapabilitiesDecoder: Decoder[ClientCapabilities] = deriveDecoder
    implicit val TextDocumentDecoder: Decoder[TextDocument] = deriveDecoder
    implicit val TextDocumentIdentifierDecoder: Decoder[TextDocumentIdentifier] = deriveDecoder
    implicit val InitializeParamsDecoder: Decoder[InitializeParams] = deriveDecoder
    implicit val DidSaveParamsDecoder: Decoder[DidSaveParams] = deriveDecoder
}
