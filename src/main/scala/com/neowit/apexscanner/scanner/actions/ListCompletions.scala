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

import com.neowit.apexscanner.{FileBasedDocument, Project, VirtualDocument}
import com.neowit.apexscanner.completion._
import com.neowit.apexscanner.nodes.Position
import com.typesafe.scalalogging.LazyLogging

/**
  * Created by Andrey Gavrikov 
  */
object ListCompletions extends LazyLogging {
    def main(args: Array[String]): Unit = {

        val projectPath = Paths.get("/Users/andrey/eclipse.workspace/Sforce - SFDC Experiments/SForce (vim-force.com)")
        val path: Path = Paths.get("/Users/andrey/eclipse.workspace/Sforce - SFDC Experiments/SForce (vim-force.com)/src/classes/CompletionTester.cls")
        val completions = new ListCompletions(Project(projectPath))
        val line = 12
        //val res = completions.list(FileBasedDocument(path), line, 12) //con.;// FallThoughNode
        //val res = completions.list(FileBasedDocument(path), line + 1, 20) //con.acc() + ; // ExpressionStatementNode
        //val res = completions.list(FileBasedDocument(path), line +2, 15) //con.acc; // ExpressionDotExpression
        //val res = completions.list(FileBasedDocument(path), line +3, 27) //((Opportunity)obj). // Expression -> ExpressionDotExpression
        //val res = completions.list(FileBasedDocument(path), line +4, 22) //new Object__c(
        //val res = completions.list(FileBasedDocument(path), line +5, 38) //new CompletionTester( Field =
        //val res = completions.list(FileBasedDocument(path), line +6, 44) //new List<Map<String, Set<Integer>>>(
        val res = completions.list(FileBasedDocument(path), line +7, 57) //String str = new List<Map<String, Set<Integer>>>(

        logger.debug( res.toString )
        ()
    }

}

class ListCompletions(project: Project) extends LazyLogging {

    def list(document: VirtualDocument, position: Position): ListCompletionsResult = {
        list(document, position.line, position.col)
    }
    def list(document: VirtualDocument, line: Int, column: Int): ListCompletionsResult = {
        document.fileOpt match {
            case Some(file) =>
                val finder = new CompletionFinder(project)
                val symbols  = finder.listCompletions(document, line, column)
                ListCompletionsResult(file, symbols)
            case None =>
                throw new IllegalArgumentException("Document.file must be provided")
        }
    }

}
