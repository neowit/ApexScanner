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

package com.neowit.apex.resolvers

import java.nio.file.{FileSystems, Path}

import com.neowit.apex.{Project, TestConfigProvider}
import com.neowit.apex.ast.AstBuilder
import com.neowit.apex.nodes.AstNode

/**
  * Created by Andrey Gavrikov 
  */
object ResolverTestUtils extends TestConfigProvider{

    def withPathProperty(pathKey: String)(codeBlock: (String, Path, AstNode) => Any): Unit = {
        val filePath = getProperty("AscendingDefinitionFinderTest.testFindDefinition.path")
        val path = FileSystems.getDefault.getPath(filePath)

        val astBuilder = new AstBuilder(Project(path))
        astBuilder.build(path)

        astBuilder.getAst(path) match {
            case None =>
            // do nothing
            case Some(result) if result.fileScanResult.errors.nonEmpty =>
                println("ERRORS ENCOUNTERED")
                result.fileScanResult.errors.foreach(println(_))

            case Some(result) =>
                val rootNode = result.rootNode
                codeBlock(filePath, path, rootNode)
        }
    }
}
