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

import com.neowit.apexscanner.TextBasedDocument
import com.neowit.apexscanner.ast.ApexAstBuilderVisitor
import com.neowit.apexscanner.scanner.ApexcodeScanner
import com.neowit.apexscanner.scanner.actions.SyntaxChecker
import org.antlr.v4.runtime.atn.PredictionMode

/**
  * Created by Andrey Gavrikov 
  */
case class ApexExpressionInSoqlNode(apexExpressionText: String, range: Range, fileOpt: Option[Path]) extends AstNode {
    override def nodeType: AstNodeType = FallThroughNodeType

    // parsing of apexExpressionText is done lazily, to avoid wasting time to parse expressions which may never be needed
    private var _isParsed = false
    override def children: Seq[AstNode] = {
        if (!_isParsed) {
            parseAsApexExpression()
            _isParsed = true
        }
        super.children
    }

    /**
      * convert exprContext TO STRING and parse with apex parse
      * and add result as a child of this ExpressionStatementNode
      * tokens inside exprContext are not good because these are SOQL grammar tokens and we need Apex grammar tokens
      *
      * @return
      */
    private def parseAsApexExpression(): AstNode = {
        getProject match {
            case Some(project) =>
                // find offset of this apex expression in outer Apex document
                // this offset will be relatively SOQL statement which is relative to Apex class
                val exprOffset = Position.toAbsolutePosition(range.start, Option(range.offset))
                val doc = TextBasedDocument(apexExpressionText, fileOpt, offset = Option(exprOffset))
                val errorListener = SyntaxChecker.errorListenerCreator(doc)
                val parser = ApexcodeScanner.createDefaultParser(doc, PredictionMode.SLL, errorListener)
                val exprCtx = parser.expression()
                val visitor = new ApexAstBuilderVisitor(Option(project), Option(doc))
                val node = visitor.visit(exprCtx)
                this.addChildToAst(node)
                node
            case None => NullNode
        }
    }
}
