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

package com.neowit.apex

import java.nio.file.{Files, Path}
import java.util.Properties
import java.util.stream.Collectors

/**
  * Created by Andrey Gavrikov 
  */
trait TestConfigProvider {
    private val is = getClass.getClassLoader.getResource("paths.properties").openStream()
    val paths = new Properties()
    paths.load(is)

    def getProperty(propName: String): String = paths.getProperty(propName)

    def getLineNoByTag(path: Path, lineTag: String): Option[Int] = {
        val lines = Files.lines(path)
        val lst = lines.collect(Collectors.toList())
        val iter = lst.iterator()
        var lineNumber = 1
        while (iter.hasNext) {
            val str = iter.next()
            if (str.indexOf(lineTag) >=0) {
                return Option(lineNumber)
            }
            lineNumber += 1
        }
        None
    }
}
