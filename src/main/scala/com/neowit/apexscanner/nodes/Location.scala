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

package com.neowit.apexscanner.nodes

import java.nio.file.Path

import com.neowit.apexscanner.Project

/**
  * Created by Andrey Gavrikov 
  */
trait Location {
    def project: Project

    def path: Path

    def range: Range

    def getDebugInfo: String = path.getName(path.getNameCount -1).toString + " => " + range.getDebugInfo
}

case object LocationUndefined extends Location {
    override def project: Project = null

    override def path: Path = null

    override def range: Range = null

    override def getDebugInfo: String = "Undefined"
}

