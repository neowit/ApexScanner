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
case class TextDocumentSyncOptions(
/**
* Open and close notifications are sent to the server.
*/
openClose: Option[Boolean] = Option(true),
/**
  * Change notifications are sent to the server. See TextDocumentSyncKind.None, TextDocumentSyncKind.Full
  * and TextDocumentSyncKindIncremental.
  */
change: Int = TextDocumentSyncKind.Full,
/**
  * Will save notifications are sent to the server.
  */
willSave: Option[Boolean] = Option(false),
/**
  * Will save wait until requests are sent to the server.
  */
willSaveWaitUntil: Option[Boolean] = Option(false),
/**
  * Save notifications are sent to the server.
  */
save: Option[SaveOptions] = Option(SaveOptions(includeText = true))
)

case class SaveOptions(includeText: Boolean)
