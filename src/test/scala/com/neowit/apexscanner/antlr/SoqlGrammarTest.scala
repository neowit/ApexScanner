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

    test("IN :productIds") {
        val text =
            """
              |[Select Id,UnitPrice,Product2Id
              |                                From PricebookEntry
              |                                Where Product2Id IN:productIds]
            """.stripMargin
        val doc = TextBasedDocument(text, dummyFile)
        val scanResult = soqlScanner.scan(doc, predictionMode)
        val errors = scanResult.errors
        errors.foreach(e =>  fail(s"\n=> (${e.line}, ${e.charPositionInLine}): " + e.msg))
    }

    test("IN: myMap.keySet() ") {
        val text =
            """
              |[Select Field1__c, Some_Date__c, Field2__c From Some_Object__c
              |Where Field1__c IN: myMap.keySet() And Id NOT IN: dsbIds And Some_Date__c != null]
            """.stripMargin
        val doc = TextBasedDocument(text, dummyFile)
        val scanResult = soqlScanner.scan(doc, predictionMode)
        val errors = scanResult.errors
        errors.foreach(e =>  fail(s"\n=> (${e.line}, ${e.charPositionInLine}): " + e.msg))
    }

    test("Some_Date__c <> null") {
        val text =
            """
              |[Select Field1__c, Some_Date__c, Field2__c From Some_Object__c
              |Where Some_Date__c <> null]
            """.stripMargin
        val doc = TextBasedDocument(text, dummyFile)
        val scanResult = soqlScanner.scan(doc, predictionMode)
        val errors = scanResult.errors
        errors.foreach(e =>  fail(s"\n=> (${e.line}, ${e.charPositionInLine}): " + e.msg))
    }

    test("id=:ApexPages.currentPage().getParameters().get('Id')") {
        val text =
            """
              |[Select Field1__c, Some_Date__c, Field2__c From Some_Object__c
              |Where id=:ApexPages.currentPage().getParameters().get('Id')]
            """.stripMargin
        val doc = TextBasedDocument(text, dummyFile)
        val scanResult = soqlScanner.scan(doc, predictionMode)
        val errors = scanResult.errors
        errors.foreach(e =>  fail(s"\n=> (${e.line}, ${e.charPositionInLine}): " + e.msg))
    }
}
