/*
 * Copyright (c) 2018 Andrey Gavrikov.
 * this file is part of tooling-force.com application
 * https://github.com/neowit/tooling-force.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.neowit.apexscanner.server.protocol.messages

/**
  * Created by Andrey Gavrikov 
  */
sealed trait MessageType {
    def code: Int
}
object MessageType {
    case object Error extends MessageType {
        override def code: Int = 1
    }
    case object Warning extends MessageType {
        override def code: Int = 2
    }
    case object Info extends MessageType {
        override def code: Int = 3
    }

    case object Log extends MessageType {
        override def code: Int = 4
    }
}
