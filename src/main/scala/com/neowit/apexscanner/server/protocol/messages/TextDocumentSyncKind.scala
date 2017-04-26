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
object TextDocumentSyncKind {
    /**
      * Documents should not be synced at all.
      */
    final val None = 0

    /**
      * Documents are synced by always sending the full content
      * of the document.
      */
    final val Full = 1

    /**
      * Documents are synced by sending the full content on open.
      * After that only incremental updates to the document are
      * send.
      */
    final val Incremental = 2
}

