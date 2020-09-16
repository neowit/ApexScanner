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

package com.neowit.apexscanner

import java.nio.file.{Files, Path, Paths}
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

    /**
      * test resource paths are all relative to TEST_RESOURCE_ROOT
      * @param propName e.g. QualifiedNameDefinitionFinderTest.projectPath
      * @return path to local file which is concatenation of TEST_RESOURCE_ROOT + getProperty(propName)
      */
    def getTestResourcePath(propName: String): Path = {
        val testResourceRoot = expandPath(paths.getProperty("TEST_RESOURCE_ROOT"))
        Paths.get(testResourceRoot, paths.getProperty(propName))
    }

    /**
      * convert "~" to full reference to home folder
      * @param path file path string which may contain "~"
      * @return
      */
    def expandPath(path: String): String = {
        path.replaceFirst("^~", System.getProperty("user.home").replace("\\","\\\\"))
    }

    def getLineNoByTag(path: Path, lineTag: String): Seq[Int] = {
        val lines = Files.lines(path)
        val lst = lines.collect(Collectors.toList())
        val iter = lst.iterator()
        val lineNumBuilder = Seq.newBuilder[Int]
        var lineNumber = 1
        while (iter.hasNext) {
            val str = iter.next()
            if (str.indexOf(lineTag) >=0) {
                lineNumBuilder += lineNumber
            }
            lineNumber += 1
        }
        lineNumBuilder.result()
    }
}
