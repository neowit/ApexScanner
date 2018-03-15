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

/**
  * Created by Andrey Gavrikov 
  */
case class ServerCapabilities(
     /**
       * Defines how text documents are synced.
       */
     textDocumentSync: Int = TextDocumentSyncKind.Full,
     //textDocumentSync: TextDocumentSyncOptions = TextDocumentSyncOptions(),
     /**
       * The server provides hover support.
       */
     hoverProvider: Boolean = false,
     /**
       * The server provides completion support.
       */
     completionProvider: Option[CompletionOptions] = Option(CompletionOptions(resolveProvider = false, triggerCharacters = Seq("."))),
     //completionProvider: Option[CompletionOptions] = None,
     /**
       * The server provides signature help support.
       */
     signatureHelpProvider: Option[SignatureHelpOptions] = None,
     /**
       * The server provides goto definition support.
       */
     definitionProvider: Boolean = true,
     /**
       * The server provides find references support.
       */
     referencesProvider: Boolean = false,
     /**
       * The server provides document highlight support.
       */
     documentHighlightProvider: Boolean = false,
     /**
       * The server provides document symbol support.
       */
     documentSymbolProvider: Boolean = true,
     /**
       * The server provides workspace symbol support.
       */
     workspaceSymbolProvider: Boolean = false,
     /**
       * The server provides code actions.
       */
     codeActionProvider: Boolean = false,
     /**
       * The server provides code lens.
       */
     codeLensProvider: Option[CodeLensOptions] = None,
     /**
       * The server provides document formatting.
       */
     documentFormattingProvider: Boolean = false,
     /**
       * The server provides document range formatting.
       */
     documentRangeFormattingProvider: Boolean = false,
     /**
       * The server provides document formatting on typing.
       */
     documentOnTypeFormattingProvider: Option[DocumentOnTypeFormattingOptions] = None,
     /**
       * The server provides rename support.
       */
     renameProvider: Boolean = false,
     /**
       * The server provides document link support.
       */
     //documentLinkProvider: Option[DocumentLinkOptions]
     /**
       * The server provides execute command support.
       */
     executeCommandProvider: ExecuteCommandOptions = ExecuteCommandOptions(Nil)

)



case class CompletionOptions(resolveProvider: Boolean, triggerCharacters: Seq[String])

case class SignatureHelpOptions(triggerCharacters: Seq[String])

case class CodeLensOptions(resolveProvider: Boolean = false)

case class DocumentOnTypeFormattingOptions(firstTriggerCharacter: String, moreTriggerCharacters: Seq[String])

case class ExecuteCommandOptions(commands: Seq[String])