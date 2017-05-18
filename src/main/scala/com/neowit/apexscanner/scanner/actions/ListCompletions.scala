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

package com.neowit.apexscanner.scanner.actions

import java.nio.file.{Path, Paths}

import com.neowit.apexscanner.Project
import com.neowit.apexscanner.completion._
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.{ExecutionContext, Future}
/**
  * Created by Andrey Gavrikov 
  */
object ListCompletions {
    def main(args: Array[String]): Unit = {
        import scala.concurrent.ExecutionContext.Implicits.global

        val projectPath = Paths.get("/Users/andrey/eclipse.workspace/Sforce - SFDC Experiments/SForce (vim-force.com)")
        val path: Path = Paths.get("/Users/andrey/eclipse.workspace/Sforce - SFDC Experiments/SForce (vim-force.com)/src/classes/CompletionTester.cls")
        val completions = new ListCompletions(Project(projectPath))
        val res = completions.list(path, 38, 20)
        println(res)
    }

}

class ListCompletions(project: Project)(implicit ex: ExecutionContext) extends LazyLogging {

    def list(file: Path, line: Int, column: Int): Future[ListCompletionsResult] = {
        val finder = new CompletionFinder(project)
        finder.listCompletions(file, line, column).map{ignore =>
            ???
        }
    }
}
