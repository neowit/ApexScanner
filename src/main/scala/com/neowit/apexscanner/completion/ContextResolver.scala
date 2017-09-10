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

package com.neowit.apexscanner.completion

import com.neowit.apexscanner.Project
import com.neowit.apexscanner.antlr.{ApexParserUtils, ApexcodeParser}
import com.neowit.apexscanner.antlr.ApexcodeParser._
import com.neowit.apexscanner.ast.ApexAstBuilderVisitor
import com.neowit.apexscanner.nodes._
import com.neowit.apexscanner.resolvers.{AscendingDefinitionFinder, NodeBySymbolTextFinder}
import org.antlr.v4.runtime.tree.{ErrorNode, ParseTree}
import org.antlr.v4.runtime.{Token, TokenStream}

/**
  * Created by Andrey Gavrikov 
  */
class ContextResolver(project: Project, astScopeNode: AstNode, lastAstNode: AstNode) {
    private val _visitor = new ApexAstBuilderVisitor(projectOpt = None, documentOpt = None)

    def resolveContext(context: ParseTree, tokens: TokenStream): Option[IsTypeDefinition] = {
        context match {
            case c: PrimaryExprContext => // single token
                resolvePrimary(c)
            case c: CreatorExpressionContext =>
                resolveCreator(c, tokens, lastAstNode)
            case c: TypeCastExprContext => // ((ObjType)some).<caret>
                resolveTypeCast(c)
            case c: ExprDotExpressionContext => // aaa.bbb.<caret>
                resolveExprDotExpression(c)
            case c: ClassVariableContext => // String str = new List<Map<String, Set<Integer>>>(<caret>
                resolveClassVariableContext(c)
            case c: CodeBlockContext => // Opportunity opp; this.opp.<CARET>
                resolveCodeBlockContext(c)
            case _:InfixAddExprContext | _:InfixAndExprContext | _:InfixEqualityExprContext |
                 _:InfixMulExprContext | _:InfixOrExprContext | _:InfixShiftExprContext if context.getChildCount > 0=>
                // all infix expressions are served here
                getLastNonErrorChild(context) match {
                    case Some(lastChild) =>
                        resolveContext(lastChild, tokens)
                    case None =>
                        // looks like there are no non error children available
                        None
                }
            case _: SpecialCaretExprContext =>
                // looks like we have a free standing caret, not bound to any specific expression
                None
            case x =>
                println(x)
                ???
        }
    }
    private def getLastNonErrorChild(context: ParseTree): Option[ParseTree] = {
        var i = context.getChildCount - 1
        while (i >=0) {
            context.getChild(i) match {
                case ch: ErrorNode => // keep going
                case ch => return Option(ch)
            }
            i -= 1
        }
        None
    }

    private def resolvePrimary(context: PrimaryExprContext): Option[IsTypeDefinition] = {
        val astNode = _visitor.visitPrimaryExpr(context)
        // look inside expression itself, it may be type cast, e.g. ((ObjectType)obj)
        val primary = context.primary()
        if (null != primary.Identifier()) {
            findTokenTypeDefinition(primary.Identifier().getSymbol, astNode).orElse{
                findTokenTypeDefinition(primary.Identifier().getSymbol, lastAstNode)
            }
        } else {
            // must be something more complex than primary expression
            None
        }
    }
    private def resolveCreator(context: CreatorExpressionContext, tokens: TokenStream, lastAstNode: AstNode): Option[IsTypeDefinition] = {
        _visitor.visitCreatorExpression(context) match {
            case creator: CreatorNode if creator.getValueType.nonEmpty =>
                Option(creator)
            case _ if null != context.creator() && context.creator().getChildCount > 0 =>
                // failed to parse something meaningful
                // try to determine type from tokens
                // 1. find start and end of type part
                val newTokenIndex = context.NEW().getSymbol.getTokenIndex
                val endPredicate: Token => Boolean = t => t.getText == "("
                // find where `new Type<bla>`(...) ends
                ApexParserUtils.findNextTokenOnChannel(newTokenIndex + 1, tokens, endPredicate) match {
                    case Some(lastTokenOfTypeExpression) =>
                        val typeTokens = ApexParserUtils.getTokensBetween(tokens, newTokenIndex + 1, lastTokenOfTypeExpression.getTokenIndex - 1)
                        val expr = "new " + typeTokens.map(_.getText).mkString("") + "()" // fake possible expression, e.g. `new SomeClass()`
                        val _tokens = ApexParserUtils.getTokensFromText(expr)
                        val parser = new ApexcodeParser(_tokens)
                        ApexParserUtils.removeConsoleErrorListener(parser)
                        // visit expression to try and build valid CreatorNode
                        val tree = parser.expression()
                        resolveContext(tree, _tokens)
                    case None =>
                        None
                }
                //val startPredicate: Token => Boolean = t => t.getText.toLowerCase == "new"
            case _ => ???
        }
    }
    private def resolveTypeCast(context: TypeCastExprContext): Option[IsTypeDefinition] = {
        _visitor.visitTypeCastExpr(context) match {
            case n: TypeCastNode =>
                Option(n)
            case _ => ???
        }
    }
    private def resolveExprDotExpression(context: ExprDotExpressionContext): Option[IsTypeDefinition] = {
        _visitor.visitExprDotExpression(context) match {
            case n: ExpressionDotExpressionNode =>
                // add sub-tree to main AST
                astScopeNode.addChildToAst(n)
                // finally try to resolve definition of fudged expression
                n.resolveDefinition() match {
                    case defOpt @ Some(_: IsTypeDefinition) =>
                        defOpt.map(_.asInstanceOf[IsTypeDefinition])
                    case x =>
                        println(x)
                        // TODO
                        // if this is valid branch then use ExpressionDotExpressionNode.getResolvedPartDefinition(node)
                        // to find definition of node preceding the caret
                        ???
                }
            case _ => ???
        }
    }
    private def resolveClassVariableContext(context: ClassVariableContext): Option[IsTypeDefinition] = {
        _visitor.visitClassVariable(context) match {
            case n: ClassVariableNode =>
                // add sub-tree to main AST
                astScopeNode.addChildToAst(n)
                if (n.children.nonEmpty) {
                    val lastChild = n.children.last // last child of variable declaration should contain context
                    lastChild match {
                        case _lastChild: IsTypeDefinition =>
                            Option(_lastChild)
                        case _lastChild: HasTypeDefinition =>
                            // finally try to resolve definition of fudged expression
                            _lastChild.resolveDefinition() match {
                                case defOpt @ Some(_: IsTypeDefinition) =>
                                    defOpt.map(_.asInstanceOf[IsTypeDefinition])
                                case _ =>
                                    // TODO
                                    // if this is valid branch then use ExpressionDotExpressionNode.getResolvedPartDefinition(node)
                                    // to find definition of node preceding the caret
                                    ???
                            }
                    }
                } else {
                    ???
                }
            case _ => ???
        }
    }

    private def resolveCodeBlockContext(context: CodeBlockContext): Option[IsTypeDefinition] = {
        _visitor.visitCodeBlock(context) match {
            case n if n.children.nonEmpty=>
                // add sub-tree to main AST
                astScopeNode.addChildToAst(n)
                val lastChild = n.children.last // last child should contain context
                lastChild match {
                    case ex: IsTypeDefinition =>
                        Option(ex)
                    case ex: ExpressionStatementNode =>
                        ex.resolveDefinition() match {
                            case defOpt @ Some(_: IsTypeDefinition) =>
                                defOpt.map(_.asInstanceOf[IsTypeDefinition])
                            case _ =>
                                // TODO
                                ???
                        }

                }
            case _ =>
                ???
        }
    }
    /**
      * looks for definition of "tokenToResolve" when AST node is NOT available.
      * This method is a last resort - it is unreliable because does matching using token text
      * @param startNode search will start inside this AST node
      * @param tokenToResolve
      * @return
      */
    private def findTokenTypeDefinition(tokenToResolve: Token, startNode: AstNode): Option[IsTypeDefinition] = {
        val rootNode = startNode
        val finder = new NodeBySymbolTextFinder(tokenToResolve.getText)

        /**
          * @param currentStartNode search will continue inside this AST node
          * @param processedParents nodes already visited
          */
        def _findTypeDefinition(currentStartNode: AstNode, processedParents: Set[AstNode]): Option[IsTypeDefinition] = {
            finder.findInside(rootNode, processedParents) match {
                case Some(node) =>
                    findNodeTypeDefinition(node)
                case None =>
                    currentStartNode.getParentInAst(skipFallThroughNodes = true) match {
                        case Some(parent) =>
                            _findTypeDefinition(parent, processedParents + currentStartNode)
                        case None =>
                            None
                    }
            }

        }
        _findTypeDefinition(startNode, Set(startNode))
    }

    /**
      * looks for definition of given AST node when AST node is available
      * @param node AST node which we need to find a definition for
      * @return
      */
    private def findNodeTypeDefinition(node: AstNode): Option[IsTypeDefinition] = {
        node match {
            case _:IsTypeDefinition =>
                // given node is already a type definition, nothing to search
                Option(node.asInstanceOf[IsTypeDefinition])
            case _ =>
                val defFinder = new AscendingDefinitionFinder()
                val definitions = defFinder.findDefinition(node, node)
                definitions match {
                    case Nil =>
                        // definition not found in current file
                        //TODO
                        None
                    case head :: Nil =>
                        // exactly 1 node found, looks promising
                        head match {
                            case n:IsTypeDefinition =>
                                Option(n)
                            case _ => None
                        }
                    case nodes =>
                        nodes.find(_.isInstanceOf[IsTypeDefinition]).map(_.asInstanceOf[IsTypeDefinition])
                }
        }
    }

}

