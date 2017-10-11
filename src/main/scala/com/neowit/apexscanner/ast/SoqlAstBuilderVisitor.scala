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

package com.neowit.apexscanner.ast

import com.neowit.apexscanner.antlr.{ApexParserUtils, SoqlBaseVisitor, SoqlParser}
import com.neowit.apexscanner.nodes._
import com.neowit.apexscanner.nodes.soql._
import com.neowit.apexscanner.{Project, VirtualDocument}
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.tree.RuleNode

import scala.collection.JavaConverters._
/**
  * Created by Andrey Gavrikov 
  */
object SoqlAstBuilderVisitor {
    val VISITOR_CREATOR_FUN: AstBuilder.VisitorCreatorFun =
        (projectOpt: Option[Project], documentOpt: Option[VirtualDocument]) => new SoqlAstBuilderVisitor(projectOpt, documentOpt)
}
class SoqlAstBuilderVisitor(override val projectOpt: Option[Project],
                            override val documentOpt: Option[VirtualDocument]) extends SoqlBaseVisitor[AstNode] with AstBuilderVisitor {

    private val _documentOffsetPosition: Position = {
        documentOpt.flatMap(_.offset) match {
            case Some(_offset) => _offset
            case None => Position(0, 0)
        }
    }

    override def defaultResult(): AstNode = NullNode

    override def visitChildren(node: RuleNode): AstNode = {

        val range = node match {
            case n: ParserRuleContext => Range(n, _documentOffsetPosition)
            case _ =>
                throw new NotImplementedError("Unhandled case is this really a node without location ?" + node)
        }
        val fallThroughNode = FallThroughNode(range)
        visitChildren(fallThroughNode, node)
        //super.visitChildren(node)
    }

    override def onComplete(): Unit = ()


    override def visitCompilationUnit(ctx: SoqlParser.CompilationUnitContext): AstNode = {
        val soqlQueryStr = ctx.soqlStatement().getText
        val node = SoqlQueryNode(soqlQueryStr, Range(ctx, _documentOffsetPosition))
        visitChildren(node, ctx)
    }

    override def visitSelectStatement(ctx: SoqlParser.SelectStatementContext): AstNode = {
        if (null != ctx.countFunction()) {
            SelectCountNode(Range(ctx, _documentOffsetPosition))
        } else {
            visitChildren(SelectStatementNode(Range(ctx, _documentOffsetPosition)), ctx)
        }
    }

    //TODO - check if this method actually gets called
    override def visitSelectItems(ctx: SoqlParser.SelectItemsContext): AstNode = {
        if (null != ctx.selectItem()) {
            // select one, two, three, TYPEOF Some ... END
            val selectItems: Seq[AstNode] = ctx.selectItem().asScala.map(visit)
            val expressionListNode = SelectItemsNode(selectItems, Range(ctx, _documentOffsetPosition))
            selectItems.map(_.setParentInAst(expressionListNode))
            visitChildren(expressionListNode, ctx)
        } else {
            NullNode
        }
    }

    override def visitSelectItem(ctx: SoqlParser.SelectItemContext): AstNode = {
        val aliasOpt = if (null == ctx.alias()) None else Option(ctx.alias().getText)
        visitChildren(SelectItemExpressionNode(aliasOpt, Range(ctx, _documentOffsetPosition)), ctx)
    }

    override def visitFieldItem(ctx: SoqlParser.FieldItemContext): AstNode = {
        visitChildren(FieldItemNode(Range(ctx, _documentOffsetPosition)), ctx)
    }

    override def visitChildRelationshipQuery(ctx: SoqlParser.ChildRelationshipQueryContext): AstNode = {
        if (null != ctx.subquery()) {
            visitChildren(ChildRelationshipSubqueryNode(ctx.getText, Range(ctx, _documentOffsetPosition)), ctx)
        } else {
            NullNode
        }
        //super.visitChildRelationshipQuery(ctx)
    }

    /* TODO
    override def visitSubquery(ctx: SoqlParser.SubqueryContext): AstNode = {
        visitChildren(SubqueryNode(ctx.getText, Range(ctx, _documentOffsetPosition)), ctx)
    }
    */

    override def visitTypeOfExpression(ctx: SoqlParser.TypeOfExpressionContext): AstNode = {
        visitChildren(TypeOfExpressionNode(Range(ctx, _documentOffsetPosition)), ctx)
    }

    override def visitFieldNameOrRef(ctx: SoqlParser.FieldNameOrRefContext): AstNode = {
        // generate qualified name from Identifier(s)
        val identifiers: Array[String] = ctx.Identifier().asScala.map(_.getText).toArray
        val qualifiedName = QualifiedName(identifiers)
        FieldNameOrRefNode(qualifiedName, Range(ctx, _documentOffsetPosition))
    }

    override def visitFromExpression(ctx: SoqlParser.FromExpressionContext): AstNode = {
        if (null != ctx.Identifier()) {
            val aliasOpt = if (null != ctx.alias()) Option(ctx.alias().getText) else None
            // generate qualified name from Identifier(s)
            val identifiers: Array[String] = ctx.Identifier().asScala.map(_.getText).toArray
            val qualifiedNameOpt =
                if (identifiers.nonEmpty) {
                    Option(QualifiedName(identifiers))
                } else {
                    // FROM is empty
                    None
                }
            FromNode(qualifiedNameOpt, aliasOpt, Range(ctx, _documentOffsetPosition))
        } else if (null != ctx.fromExpression()) {
            val fromExpressionNode = FromExpressionNode(Range(ctx, _documentOffsetPosition))
            val expressions: Seq[AstNode] = ctx.fromExpression().asScala.map(visit)
            expressions.map(_.setParentInAst(fromExpressionNode))
            fromExpressionNode
        } else {
            NullNode
        }
    }

    override def visitFromSubqueryStatement(ctx: SoqlParser.FromSubqueryStatementContext): AstNode = {
        if (null != ctx.fromRelationshipPath() && null != ctx.fromRelationshipPath().Identifier()) {
            val aliasOpt = if (null != ctx.alias()) Option(ctx.alias().getText) else None
            val identifiers: Array[String] = ctx.fromRelationshipPath().Identifier().asScala.map(_.getText).toArray
            val qualifiedNameOpt =
                if (identifiers.nonEmpty) {
                    Option(QualifiedName(identifiers))
                } else {
                    // FROM is empty
                    None
                }
            FromNode(qualifiedNameOpt, aliasOpt, Range(ctx, _documentOffsetPosition))
        } else {
            NullNode
        }
    }

    override def visitWhereStatement(ctx: SoqlParser.WhereStatementContext): AstNode = {
        visitChildren(SoqlWhereNode(Range(ctx, _documentOffsetPosition)), ctx)
    }

    override def visitWhereFieldExpression(ctx: SoqlParser.WhereFieldExpressionContext): AstNode = {
        visitChildren(SoqlWhereFieldExpressionNode(Range(ctx, _documentOffsetPosition)), ctx)
    }

    override def visitIgnoredApexExpression(ctx: SoqlParser.IgnoredApexExpressionContext): AstNode = {
        val exprContext = if (null != ctx.apexBlock()) ctx.apexBlock() else ctx.apexExpression()
        if (null != exprContext) {
            val apexExpressionText = ApexParserUtils.getTextFromContext(exprContext)
            ApexExpressionInSoqlNode(apexExpressionText, Range(exprContext, _documentOffsetPosition), documentOpt.flatMap(_.fileOpt))
        } else {
            NullNode
        }
    }

    //TODO implement all relevant visitXXX methods
}
