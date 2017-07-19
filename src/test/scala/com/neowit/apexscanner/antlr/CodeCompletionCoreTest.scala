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

import com.neowit.apexscanner.completion.{CaretInFixedDocument, CaretScopeFinder, CompletionFinder}
import org.antlr.v4.runtime.Token
import org.scalatest.FunSuite

/**
  * Created by Andrey Gavrikov 
  */
class CodeCompletionCoreTest extends FunSuite {

    test("CollectCandidates: `con.`") {
        val text =
            """
              |class CompletionTester {
              | public void testCompletion() {
              |		CompletionTester con = new CompletionTester();
              |     con.<CARET>
              | }
              |}
            """.stripMargin
        val (caretToken, parser) = getCaretTokenAndParser(text)
        val c3 = new CodeCompletionCore(parser)
        // parse
        parser.compilationUnit()

        c3.showResult = true
        val candidates = c3.collectCandidates(caretToken.getTokenIndex )
        assertResult(0, "Did not expect any rules")(candidates.rules.size)

        val expectedTokens = Set[Int](
            ApexcodeLexer.Identifier,
            ApexcodeLexer.CLASS,
            ApexcodeLexer.THIS,
            ApexcodeLexer.SUPER,
            ApexcodeLexer.IntegerLiteral,
            ApexcodeLexer.FloatingPointLiteral,
            ApexcodeLexer.StringLiteral,
            ApexcodeLexer.BooleanLiteral,
            ApexcodeLexer.NULL,
            ApexcodeLexer.SoslLiteral,
            ApexcodeLexer.SoqlLiteral,
            ApexcodeLexer.NEW
        )
        val candidateTokenSet = candidates.tokens.keySet
        expectedTokens.foreach{n =>
            assert(candidateTokenSet.contains(n), "Missing token: " + ApexcodeLexer.VOCABULARY.getDisplayName(n))
        }
    }

    test("CollectCandidates: `con.acc() +  `") {
        val text =
            """class CompletionTester {
              |
              |	public void testCompletion() {
              |		CompletionTester con = new CompletionTester();
              |     con.acc() + <CARET>
              |	}
              |
              |}
            """.stripMargin
        val (caretToken, parser) = getCaretTokenAndParser(text)
        val c3 = new CodeCompletionCore(parser)
        // parse
        parser.compilationUnit()

        c3.showResult = true
        //c3.showDebugOutput = true
        val candidates = c3.collectCandidates(caretToken.getTokenIndex )
        assertResult(0, "Did not expect any rules")(candidates.rules.size)

        val expectedTokens = Set[Int](
            ApexcodeLexer.THIS,
            ApexcodeLexer.SUPER,
            ApexcodeLexer.IntegerLiteral,
            ApexcodeLexer.FloatingPointLiteral,
            ApexcodeLexer.StringLiteral,
            ApexcodeLexer.BooleanLiteral,
            ApexcodeLexer.NULL,
            ApexcodeLexer.SoslLiteral,
            ApexcodeLexer.SoqlLiteral,
            ApexcodeLexer.Identifier,
            ApexcodeLexer.NEW
        )
        val candidateTokenSet = candidates.tokens.keySet
        expectedTokens.foreach{n =>
            assert(candidateTokenSet.contains(n), "Missing token: " + ApexcodeLexer.VOCABULARY.getDisplayName(n))
        }

    }

    test("CollectCandidates: `str =  `") {
        val text =
            """class CompletionTester {
              |
              |	public void testCompletion() {
              |     str = <CARET>
              |	}
              |}
            """.stripMargin
        val (caretToken, parser) = getCaretTokenAndParser(text)
        val c3 = new CodeCompletionCore(parser)
        // parse
        parser.compilationUnit()

        c3.showResult = true
        /*
        c3.preferredRules = Set[CodeCompletionCore.number](
            ApexcodeParser.RULE_assignmentRightExpr
        )
        */
        //c3.showDebugOutput = true
        val candidates = c3.collectCandidates(caretToken.getTokenIndex )
        /*
        val expectedRules = Set[Int](
            ApexcodeParser.RULE_assignmentRightExpr
        )
        assert(candidates.rules.size >= expectedRules.size, "Less rules returned than expected")
        val candidateRuleSet = candidates.rules.keySet
        expectedRules.foreach{n =>
            assert(candidateRuleSet.contains(n), "Missing token: " + ApexcodeParser.VOCABULARY.getDisplayName(n))
        }
        */
        assertResult(0, "Did not expect any rules")(candidates.rules.size)

        val expectedTokens = Set[Int](
            ApexcodeLexer.THIS,
            ApexcodeLexer.SUPER,
            ApexcodeLexer.IntegerLiteral,
            ApexcodeLexer.FloatingPointLiteral,
            ApexcodeLexer.StringLiteral,
            ApexcodeLexer.BooleanLiteral,
            ApexcodeLexer.NULL,
            ApexcodeLexer.SoslLiteral,
            ApexcodeLexer.SoqlLiteral,
            ApexcodeLexer.Identifier,
            ApexcodeLexer.NEW
        )
        val candidateTokenSet = candidates.tokens.keySet
        expectedTokens.foreach{n =>
            assert(candidateTokenSet.contains(n), "Missing token: " + ApexcodeLexer.VOCABULARY.getDisplayName(n))
        }

    }

    private def getCaretTokenAndParser(text: String): (Token, ApexcodeParser) = {
        val caretOriginal = CaretUtils.getCaret(text, Paths.get("ignored"))
        val fixedDocument = CompletionFinder.injectFixerToken(caretOriginal)
        val caretInFixedDocument = new CaretInFixedDocument(caretOriginal.position, fixedDocument, caretOriginal.document)

        val parser = CompletionFinder.createParser(caretInFixedDocument, None)
        CaretScopeFinder.findCaretToken(caretInFixedDocument, parser) match {
            case Some(caretToken) =>
                (caretToken, parser)
            case None => throw new IllegalArgumentException("Failed to detect <CARET> position in : " + text)

        }

    }
}
