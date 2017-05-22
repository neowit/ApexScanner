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

import java.nio.file.Path

import com.neowit.apexscanner.Project
import com.neowit.apexscanner.antlr.{ApexParserUtils, ApexcodeParser}
import com.neowit.apexscanner.nodes._
import com.neowit.apexscanner.resolvers.{AscendingDefinitionFinder, NodeByLocationFinder}
import com.typesafe.scalalogging.LazyLogging
import org.antlr.v4.runtime.CommonTokenStream

import scala.concurrent.{ExecutionContext, Future}

import com.neowit.apexscanner.symbols._

/**
  * Created by Andrey Gavrikov 
  */
class CompletionFinder(project: Project)(implicit ex: ExecutionContext) extends LazyLogging {

    private def findCaretToken(caret: CaretInFile): Option[CaretReachedException] = {
        val lexer = ApexParserUtils.getDefaultLexer(caret.file)
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

    def listCompletions(file: Path, line: Int, column: Int): Future[Seq[Symbol]] = {
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
        project.getAst(caret.file).map{
            case Some(_res) =>
                val locationFinder = new NodeByLocationFinder(caret.position)
                val rootNode = _res.rootNode
                locationFinder.findInside(rootNode) match {
                    case finalNode @ Some(_) =>
                        //finalNode is a node in (or right before) Caret position
                        finalNode
                    case None => None
                }

            case _ =>
                logger.debug("Failed to build AST for file: " + caret.file)
                None
        }
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
                    val defFinder = new AscendingDefinitionFinder()
                    val definitions = defFinder.findDefinition(prevNodeInDotExpression, prevNodeInDotExpression)
                    definitions match {
                        case Nil =>
                            // definition not found in current file
                            // try other places
                            ???
                        case head :: Nil =>
                            // exactly 1 node found, looks promissing
                            head match {
                                case n:IsTypeDefinition =>
                                    Option(n)
                                case _ => None
                            }
                        case nodes =>
                            nodes.find(_.isInstanceOf[IsTypeDefinition]).map(_.asInstanceOf[IsTypeDefinition])
                    }

                case None => None
              }
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
}
