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

import java.nio.file.Path

import com.neowit.apexscanner.{TextBasedDocument, VirtualDocument}
import com.neowit.apexscanner.scanner.{ApexErrorListener, DocumentScanResult, Scanner, SoqlScanner}
import com.neowit.apexscanner.scanner.actions.SyntaxChecker
import org.antlr.v4.runtime.atn.PredictionMode
import org.scalatest.FunSuite

/**
  * Created by Andrey Gavrikov 
  */
class SoqlGrammarTest extends FunSuite {
    private val soqlScanner = new SoqlScanner(){
        override def isIgnoredPath(path: Path) = true
        override def onEachResult(result: DocumentScanResult): DocumentScanResult = Scanner.defaultOnEachResult(result)
        override def errorListenerFactory(document: VirtualDocument): ApexErrorListener = SyntaxChecker.errorListenerCreator(document)
    }
    //private val dummyFile = Paths.get("")
    private val predictionMode: PredictionMode = PredictionMode.SLL

    test("IN :productIds") {
        val text =
            """
              |[Select Id,UnitPrice,Product2Id From PricebookEntry Where Product2Id IN:productIds]
            """.stripMargin
        val doc = TextBasedDocument(text, fileOpt = None)// dummyFile
        val scanResult = soqlScanner.scan(doc, predictionMode, None)
        val errors = scanResult.errors
        errors.foreach(e =>  fail(s"\n=> (${e.line}, ${e.charPositionInLine}): " + e.msg))
    }

    test("IN: myMap.keySet() ") {
        val text =
            """
              |[Select Field1__c, Some_Date__c, Field2__c From Some_Object__c
              |Where Field1__c IN: myMap.keySet() And Id NOT IN: dsbIds And Some_Date__c != null]
            """.stripMargin
        val doc = TextBasedDocument(text, fileOpt = None)
        val scanResult = soqlScanner.scan(doc, predictionMode, None)
        val errors = scanResult.errors
        errors.foreach(e =>  fail(s"\n=> (${e.line}, ${e.charPositionInLine}): " + e.msg))
    }

    test("Some_Date__c <> null") {
        val text =
            """
              |[Select Field1__c, Some_Date__c, Field2__c From Some_Object__c
              |Where Some_Date__c <> null]
            """.stripMargin
        val doc = TextBasedDocument(text, fileOpt = None)
        val scanResult = soqlScanner.scan(doc, predictionMode, None)
        val errors = scanResult.errors
        errors.foreach(e =>  fail(s"\n=> (${e.line}, ${e.charPositionInLine}): " + e.msg))
    }

    test("id=:ApexPages.currentPage().getParameters().get('Id')") {
        val text =
            """
              |[Select Field1__c, Some_Date__c, Field2__c From Some_Object__c
              |Where id=:ApexPages.currentPage().getParameters().get('Id')]
            """.stripMargin
        val doc = TextBasedDocument(text, fileOpt = None)
        val scanResult = soqlScanner.scan(doc, predictionMode, None)
        val errors = scanResult.errors
        errors.foreach(e =>  fail(s"\n=> (${e.line}, ${e.charPositionInLine}): " + e.msg))
    }

    test("where lon__c > account.Lon__c + 2") {
        val text =
            """
              | SELECT Id, Name, OwnerId
              | from Account
              | WHERE
              | lon__c < :account.Lon__c + 2
              | and lon__c > :account.Lon__c / 2
              | and lat__c < :account.Lat__c + 2
              | and lat__c > :account.Lat__c * 2
            """.stripMargin
        val doc = TextBasedDocument(text, fileOpt = None)
        val scanResult = soqlScanner.scan(doc, predictionMode, None)
        val errors = scanResult.errors
        errors.foreach(e =>  fail(s"\n=> (${e.line}, ${e.charPositionInLine}): " + e.msg))
    }

    test("select count(Id) sum from Account") {
        val text =
            """
              | select count(Id) sum from Account
            """.stripMargin
        val doc = TextBasedDocument(text, fileOpt = None)
        val scanResult = soqlScanner.scan(doc, predictionMode, None)
        val errors = scanResult.errors
        errors.foreach(e =>  fail(s"\n=> (${e.line}, ${e.charPositionInLine}): " + e.msg))
    }

    test("select id,(select id from OpenActivities order by ActivityDate DESC, LastModifiedDate DESC limit 500) from Opportunity ") {
        val text =
            """
              | select id,(select id from OpenActivities order by ActivityDate DESC, LastModifiedDate DESC limit 500) from Opportunity
            """.stripMargin
        val doc = TextBasedDocument(text, fileOpt = None)
        val scanResult = soqlScanner.scan(doc, predictionMode, None)
        val errors = scanResult.errors
        errors.foreach(e =>  fail(s"\n=> (${e.line}, ${e.charPositionInLine}): " + e.msg))
    }

    test("where ( Id in :config.productIds or ParentId in :config.productIds )") {
        val text =
            """
              | select Id from Object
              |where ( Id in :config.productIds or ParentId in :config.productIds )
              |and Active__c = true
            """.stripMargin
        val doc = TextBasedDocument(text, fileOpt = None)
        val scanResult = soqlScanner.scan(doc, predictionMode, None)
        val errors = scanResult.errors
        errors.foreach(e =>  fail(s"\n=> (${e.line}, ${e.charPositionInLine}): " + e.msg))
    }

    test("WHERE ID IN (:someId, :originId, :destinationId)") {
        val text =
            """
              | SELECT Id, Name FROM Account WHERE ID IN (:someId, :originId, :destinationId)
            """.stripMargin
        val doc = TextBasedDocument(text, fileOpt = None)
        val scanResult = soqlScanner.scan(doc, predictionMode, None)
        val errors = scanResult.errors
        errors.foreach(e =>  fail(s"\n=> (${e.line}, ${e.charPositionInLine}): " + e.msg))
    }

    test("where Id in: new Id[] {item1.Id, item2.Id, item2_1.Id} ") {
        val text =
            """
              | [select Id from Object where Id in: new Id[] {item1.Id, item2.Id, item2_1.Id} ]
            """.stripMargin
        val doc = TextBasedDocument(text, fileOpt = None)
        val scanResult = soqlScanner.scan(doc, predictionMode, None)
        val errors = scanResult.errors
        errors.foreach(e =>  fail(s"\n=> (${e.line}, ${e.charPositionInLine}): " + e.msg))
    }

    test("keyword used as object name: From Group  ") {
        val text =
            """
              | [Select Id From Group]
            """.stripMargin
        val doc = TextBasedDocument(text, fileOpt = None)
        val scanResult = soqlScanner.scan(doc, predictionMode, None)
        val errors = scanResult.errors
        errors.foreach(e =>  fail(s"\n=> (${e.line}, ${e.charPositionInLine}): " + e.msg))
    }

    test(" NOT in front of field name ") {
        val text =
            """
              | select Id from RecordType where Name like 'Some%' and (NOT Name like '%Other%')
            """.stripMargin
        val doc = TextBasedDocument(text, fileOpt = None)
        val scanResult = soqlScanner.scan(doc, predictionMode, None)
        val errors = scanResult.errors
        errors.foreach(e =>  fail(s"\n=> (${e.line}, ${e.charPositionInLine}): " + e.msg))
    }

    test(" NOT in front of (expression) ") {
        val text =
            """
              | select Id from RecordType where Name like 'Some%' and NOT (Name like '%Other%' or Name = 'Some')
            """.stripMargin
        val doc = TextBasedDocument(text, fileOpt = None)
        val scanResult = soqlScanner.scan(doc, predictionMode, None)
        val errors = scanResult.errors
        errors.foreach(e =>  fail(s"\n=> (${e.line}, ${e.charPositionInLine}): " + e.msg))
    }

    test(" SOQL keywords in :apexExpression ") {
        val text =
            """
              |SELECT Id FROM Object
              |WHERE SomeDate < :today
              |or SomeDate >= :Date.today() or SomeAt = : at or SomeTeam = :team
              |or Some = :some.get(team)
              |or Some = :some.get(Yesterday)
              |or Some = :count
              |or Some = :count()
              |or Some = :count_distinct
              |or Some = :avg()
              |or Some = :avg
            """.stripMargin
        val doc = TextBasedDocument(text, fileOpt = None)
        val scanResult = soqlScanner.scan(doc, predictionMode, None)
        val errors = scanResult.errors
        errors.foreach(e =>  fail(s"\n=> (${e.line}, ${e.charPositionInLine}): " + e.msg))
    }

    test(" :System.today().addDays((-1) * someDays) ") {
        val text =
            """
              | [Select id from Case where ClosedDate <= :System.today().addDays((-1) * someDays)]
            """.stripMargin
        val doc = TextBasedDocument(text, fileOpt = None)
        val scanResult = soqlScanner.scan(doc, predictionMode, None)
        val errors = scanResult.errors
        errors.foreach(e =>  fail(s"\n=> (${e.line}, ${e.charPositionInLine}): " + e.msg))
    }

    test(" where Id in :trigger.new ") {
        val text =
            """
              | [Select id from Case where Id in :trigger.new ]
            """.stripMargin
        val doc = TextBasedDocument(text, fileOpt = None)
        val scanResult = soqlScanner.scan(doc, predictionMode, None)
        val errors = scanResult.errors
        errors.foreach(e =>  fail(s"\n=> (${e.line}, ${e.charPositionInLine}): " + e.msg))
    }

    test(" WHERE Id =: [SELECT Id FROM Account]") {
        val text =
            """
              | [SELECT ID FROM Account
              |  WHERE Id =: [SELECT Id FROM Account WHERE Name = '' LIMIT 1]
              |      AND Name <> 'some']
            """.stripMargin
        val doc = TextBasedDocument(text, fileOpt = None)
        val scanResult = soqlScanner.scan(doc, predictionMode, None)
        val errors = scanResult.errors
        errors.foreach(e =>  fail(s"\n=> (${e.line}, ${e.charPositionInLine}): " + e.msg))
    }

    test("group by f1, f2, f3 having count(Id) > 1 ") {
        val text =
            """
              | [SELECT Id FROM Object
              |WHERE Field in : finalizedProposalIds
              |group by f1, f2, f3 having count(Id) > 1 ]
            """.stripMargin
        val doc = TextBasedDocument(text, fileOpt = None)
        val scanResult = soqlScanner.scan(doc, predictionMode, None)
        val errors = scanResult.errors
        errors.foreach(e =>  fail(s"\n=> (${e.line}, ${e.charPositionInLine}): " + e.msg))
    }

    test("Date literals e.g. 1970-12-31T22:16:30.000Z ") {
        val text =
            """
              | select Id From Opportunity
              | where
              |    CreatedDate >= 2015-01-01
              |    or CreatedDate < 1970-12-31T22:16:30.000Z
              |    or CreatedDate <> 1970-12-31T10:00:00-08:00
              |
            """.stripMargin
        val doc = TextBasedDocument(text, fileOpt = None)
        val scanResult = soqlScanner.scan(doc, predictionMode, None)
        val errors = scanResult.errors
        errors.foreach(e =>  fail(s"\n=> (${e.line}, ${e.charPositionInLine}): " + e.msg))
    }

    test("limit :( isTest ? 5 : 1000 )") {
        val text =
            """
              |[select id from Object
              |where ownerid = :userinfo.getuserid()
              |order by some.field, lastname asc
              |limit :( isTest ? 5 : 1000 )]
              |
            """.stripMargin
        val doc = TextBasedDocument(text, fileOpt = None)
        val scanResult = soqlScanner.scan(doc, predictionMode, None)
        val errors = scanResult.errors
        errors.foreach(e =>  fail(s"\n=> (${e.line}, ${e.charPositionInLine}): " + e.msg))
    }

    test(" (Id)article.get('Id') ") {
        val text =
            """
              |SELECT KnowledgeArticleId
              |FROM KnowledgeArticleVersion
              |WHERE Id = :(Id) article.get('Id')
              |
            """.stripMargin
        val doc = TextBasedDocument(text, fileOpt = None)
        val scanResult = soqlScanner.scan(doc, predictionMode, None)
        val errors = scanResult.errors
        errors.foreach(e =>  fail(s"\n=> (${e.line}, ${e.charPositionInLine}): " + e.msg))
    }
}
