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

import com.neowit.apexscanner.{Project, VirtualDocument}
import com.neowit.apexscanner.antlr.{ApexParserUtils, ApexcodeParser}
import com.neowit.apexscanner.nodes._
import com.neowit.apexscanner.resolvers.{AscendingDefinitionFinder, NodeByLocationFinder}
import com.typesafe.scalalogging.LazyLogging
import org.antlr.v4.runtime.{CommonTokenStream, Token}

import scala.concurrent.{ExecutionContext, Future}
import com.neowit.apexscanner.symbols._

/**
  * Created by Andrey Gavrikov 
  */
class CompletionFinder(project: Project)(implicit ex: ExecutionContext) extends LazyLogging {

    private def findCaretToken(caret: CaretInFile): Option[CaretReachedException] = {
        val lexer = ApexParserUtils.getDefaultLexer(caret.document)
        val tokenSource = new CodeCompletionTokenSource(lexer, caret)
        val tokens: CommonTokenStream = new CommonTokenStream(tokenSource)
        val parser = new ApexcodeParser(tokens)
        // do not dump parse errors into console
        ApexParserUtils.removeConsoleErrorListener(parser)
        parser.setBuildParseTree(true)
        parser.setErrorHandler(new CompletionErrorStrategy())
        try {
            // run actual scan, trying to identify caret position
            parser.compilationUnit()
            None
        } catch {
            case ex: CaretReachedException =>
                //println("found caret?")
                logger.debug("caret token: " + ex.caretToken.getText)
                Option(ex)
            //return (CompletionUtils.breakExpressionToATokens(ex), Some(ex))
            case e:Throwable =>
                logger.debug(e.getMessage)
                None
        }
    }

    def listCompletions(file: VirtualDocument, line: Int, column: Int): Future[Seq[Symbol]] = {
        val caret = new CaretInFile(Position(line, column), file)
        findCaretToken(caret) match {
            case Some(caretReachedException) =>
                //caretReachedException.finalContext
                //now when we found token corresponding caret position try to understand context
                val symbols =
                    findContextOrScope(caretReachedException, caret).map{
                        case Some(finalNode) =>
                            findTypeDefinition(finalNode) match {
                                case Some(definition) =>
                                    listSymbols(definition)
                                case None => Seq.empty
                            }
                        case None => Seq.empty
                    }
                symbols
            case None =>
                Future.successful(Seq.empty)
        }
    }

    // find scope
    private def findContextOrScope(caretEx: CaretReachedException, caret: CaretInFile): Future[Option[AstNode]] = {
        project.getAst(caret.document.file).map{
            case Some(_res) =>
                val scopeToken = findSuitableScopeToken(caretEx, caret)
                val position = Position(scopeToken.getLine, scopeToken.getCharPositionInLine + 1)
                val locationFinder = new NodeByLocationFinder(position)
                val rootNode = _res.rootNode
                locationFinder.findInside(rootNode) match {
                    case finalNode @ Some(_) =>
                        //finalNode is a node in (or right before) Caret position
                        finalNode
                    case None => None
                }

            case _ =>
                logger.debug("Failed to build AST for file: " + caret.document)
                None
        }
    }

    /**
      * TODO
      * find token which can be used to find current context definition caret token often gets associated
      * with the NEXT token after Caret instead of Previous one
      * this method is trying to identify if caret token has gone too far and if we need to use the
      * token which goes BEFORE caret, if that token is a word or "."
      * @param caretEx caret area details
      * @param caret caret definition
      * @return
      */
    private def findSuitableScopeToken(caretEx: CaretReachedException, caret: CaretInFile): Token = {
        /*
        if (ApexParserUtils.isWordToken(caretEx.caretToken)) {
            // use caret position as is
            caretEx.caretToken
        } else {
            // move back and try to find more suitable token to use as a caret definition base
            val inputStream = caretEx.getInputStream
            val caretTokenIndex = caretEx.caretToken.getTokenIndex
            if (ApexParserUtils.isDotToken(inputStream.get(caretTokenIndex-1)) && caretTokenIndex > 2) {
                // check if token before dot is a word token
                if (ApexParserUtils.isWordToken(inputStream.get(caretTokenIndex-2))) {
                    inputStream.get(caretTokenIndex-2)
                } else {
                    // TODO implement support for other options
                    caretEx.caretToken
                }
            } else {
                // TODO implement support for other options
                caretEx.caretToken
            }
        }
        */
        ???
    }

    private def findTypeDefinition(node: AstNode): Option[IsTypeDefinition] = {
        node match {
            case FallThroughNode(range) => //con.;
                ???
            case n @ IdentifierNode(name, range)  => //con.acc; //parent node: ExpressionDotExpressionNode
                findTypeDefinitionByIdentifier(n)
            case ExpressionStatementNode(range) => //con.acc() + ;
                ???
            case continueHere =>
                ???
        }
    }
    //con.acc; //parent node: ExpressionDotExpressionNode
    private def findTypeDefinitionByIdentifier(node: IdentifierNode): Option[IsTypeDefinition] = {
        node.getParentInAst(skipFallThroughNodes = true) match {
          case Some(parent @ ExpressionDotExpressionNode(range)) =>
              //first.second.exp<Caret>
              // get next to last node (i.e. 'second' from example above)
              parent.children.reverse.drop(1).headOption match {
                case Some(prevNodeInDotExpression) =>
                    findDefinitionAscending(prevNodeInDotExpression)
                case None => None
              }
          case Some(parent @ ExpressionStatementNode(range)) =>
              findDefinitionAscending(node)
          case _ =>
                ???
        }
    }

    private def listSymbols(node: IsTypeDefinition): Seq[Symbol] = {
        node.getValueType match {
          case Some(valueType) =>
              //TODO - continue here
              //TODO - having Qualified Name it should now be possible to find type details
              project.getByQualifiedName(valueType.qualifiedName) match {
                case Some(valueTypeNode) =>
                    valueTypeNode.findChildrenInAst(_.isSymbol).map(_.asInstanceOf[Symbol])
                case None =>
                    logger.debug("No data for value type: " + valueType.qualifiedName)
                    Seq.empty
              }
          case None => Seq.empty
        }
    }
    private def findDefinitionAscending(node: AstNode): Option[IsTypeDefinition] = {
        val defFinder = new AscendingDefinitionFinder()
        val definitions = defFinder.findDefinition(node, node)
        definitions match {
            case Nil =>
                // definition not found in current file
                // try other places
                ???
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
