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

import com.neowit.apexscanner.nodes.Location
import com.neowit.apexscanner.symbols.SymbolKind

/**
  * Created by Andrey Gavrikov 
  */
case class SymbolInformation (
    /**
      * The name of this symbol.
      */
    name: String,

    /**
      * The kind of this symbol.
      */
    kind: SymbolKind,

    /**
      * The location of this symbol. The location's range is used by a tool
      * to reveal the location in the editor. If the symbol is selected in the
      * tool the range's start information is used to position the cursor. So
      * the range usually spwans more then the actual symbol's name and does
      * normally include thinks like visibility modifiers.
      *
      * The range doesn't have to denote a node range in the sense of a abstract
      * syntax tree. It can therefore not be used to re-construct a hierarchy of
      * the symbols.
      */
    location: Location,

    /**
      * The name of the symbol containing this symbol. This information is for
      * user interface purposes (e.g. to render a qaulifier in the user interface
      * if necessary). It can't be used to re-infer a hierarchy for the document
      * symbols.
      */
    containerName: Option[String]
)
