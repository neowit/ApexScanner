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

package com.neowit.apexscanner.antlr

import java.nio.file.Paths

import com.neowit.apexscanner.TextBasedDocument
import com.neowit.apexscanner.scanner.{Scanner, SoqlScanner}
import com.neowit.apexscanner.scanner.actions.SyntaxChecker
import org.antlr.v4.runtime.atn.PredictionMode
import org.scalatest.FunSuite

/**
  * Created by Andrey Gavrikov 
  */
class SoqlGrammarTest extends FunSuite {
    private val soqlScanner = new SoqlScanner(
        p => true,
        onEachResult = Scanner.emptyOnEachResult,
        errorListenerFactory = SyntaxChecker.errorListenerCreator
    )
    private val dummyFile = Paths.get("")
    private val predictionMode: PredictionMode = PredictionMode.SLL

    test("IN:ProductIds") {
        val text =
            """
              |[Select Id,UnitPrice,Product2Id
              |                                From PricebookEntry
              |                                Where Product2Id IN:ProductIds]
            """.stripMargin
        val doc = new TextBasedDocument(text, dummyFile)
        val scanResult = soqlScanner.scan(doc, predictionMode)
        val errors = scanResult.errors
        errors.foreach(e =>  assert(false, s"\n=> (${e.line}, ${e.charPositionInLine}): " + e.msg))
    }

    test("IN:ProductIds") {
        val text =
            """
              |[Select Current__c, DBS_Certificate_Date__c, Member__c From DSB_Check__c
              |Where Member__c IN: mMostRecent.keySet() And Id NOT IN: dsbIds And DBS_Certificate_Date__c <> null]
            """.stripMargin
        val doc = new TextBasedDocument(text, dummyFile)
        val scanResult = soqlScanner.scan(doc, predictionMode)
        val errors = scanResult.errors
        errors.foreach(e =>  assert(false, s"\n=> (${e.line}, ${e.charPositionInLine}): " + e.msg))
    }
}
