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

package com.neowit.apexscanner.server.protocol

sealed trait DiagnosticSeverity {
    val code: Int
}
/**
  * Created by Andrey Gavrikov 
  */
object DiagnosticSeverity {
    case object Error extends DiagnosticSeverity {
        override val code: Int = 1
    }
    case object Warning extends DiagnosticSeverity {
        override val code: Int = 2
    }
    case object Information extends DiagnosticSeverity {
        override val code: Int = 3
    }
    case object Hint extends DiagnosticSeverity {
        override val code: Int = 4
    }
}
