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

import com.neowit.apexscanner.symbols.SymbolKind
import io.circe.Json
/**
  * Created by Andrey Gavrikov 
  */
case class CompletionItem(
    /**
    * The label of this completion item. By default
    * also the text that is inserted when selecting
    * this completion.
    */
    label: String,
    /**
      * The kind of this completion item. Based of the kind
      * an icon is chosen by the editor.
      */
    kind: SymbolKind,
    /**
      * A human-readable string with additional information
      * about this item, like type or symbol information.
      */
    detail: Option[String] = None,
    /**
      * A human-readable string that represents a doc-comment.
      */
    documentation: Option[String] = None,
    /**
      * A string that shoud be used when comparing this item
      * with other items. When `falsy` the label is used.
      */
    sortText: Option[String] = None,
    /**
      * A string that should be used when filtering a set of
      * completion items. When `falsy` the label is used.
      */
    filterText: Option[String] = None,
    /**
      * A string that should be inserted a document when selecting
      * this completion. When `falsy` the label is used.
      */
    insertText: Option[String] = None,
    /**
      * The format of the insert text. The format applies to both the `insertText` property
      * and the `newText` property of a provided `textEdit`.
      */
    //insertTextFormat: Option[InsertTextFormat] = None
    /**
      * An edit which is applied to a document when selecting this completion. When an edit is provided the value of
      * `insertText` is ignored.
      *
      * *Note:* The range of the edit must be a single line range and it must contain the position at which completion
      * has been requested.
      */
    //textEdit?: TextEdit,
    /**
      * An optional array of additional text edits that are applied when
      * selecting this completion. Edits must not overlap with the main edit
      * nor with themselves.
      */
    //additionalTextEdits?: TextEdit[],
    /**
      * An optional command that is executed *after* inserting this completion. *Note* that
      * additional modifications to the current document should be described with the
      * additionalTextEdits-property.
      */
    //command?: Command,
    /**
      * An data entry field that is preserved on a completion item between
      * a completion and a completion resolve request.
      */
    data: Option[Json] = None
)


object CompletionItem {
    def apply(symbol: com.neowit.apexscanner.symbols.Symbol): CompletionItem = {
        new CompletionItem(
            label = symbol.symbolName,
            kind = symbol.symbolKind,
            documentation = symbol.documentation
        )

    }
}
