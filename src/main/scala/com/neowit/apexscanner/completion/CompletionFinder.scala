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

import com.neowit.apexscanner.antlr.CodeCompletionCore.CandidatesCollection
import com.neowit.apexscanner.{Project, VirtualDocument}
import com.neowit.apexscanner.antlr.{ApexParserUtils, ApexcodeLexer, ApexcodeParser, CodeCompletionCore}
import com.neowit.apexscanner.ast.QualifiedName
import com.neowit.apexscanner.nodes._
import com.typesafe.scalalogging.LazyLogging
import org.antlr.v4.runtime.{BailErrorStrategy, CommonTokenStream, Token}

import scala.concurrent.{ExecutionContext, Future}
import com.neowit.apexscanner.symbols._
import org.antlr.v4.runtime.atn.PredictionMode

case class FindCaretScopeResult(caretScope: Option[CaretScope], caretToken: Token)
/**
  * Created by Andrey Gavrikov 
  */
class CompletionFinder(project: Project)(implicit ex: ExecutionContext) extends LazyLogging {

    def listCompletions(file: VirtualDocument, line: Int, column: Int): Future[Seq[Symbol]] = {
        val caret = new CaretInDocument(Position(line, column), file)
        val parser = createParser(caret.document)
        findCaretScope(caret, parser).map{
            case Some(FindCaretScopeResult(Some(CaretScope(_, Some(typeDefinition))), _)) =>
                typeDefinition.getValueType match {
                    case Some(valueType) =>
                        logger.debug("Caret value type: " + valueType)
                        getValueTypeMembers(valueType)
                    case None => Seq.empty
                }
            case Some(FindCaretScopeResult(Some(CaretScope(scopeNode, None)), caretToken)) =>
                // caret definition is not obvious, but we have an AST scope node
                val candidates = collectCandidates(caret, caretToken, parser)
                getCandidateSymbols(scopeNode, candidates)

            case Some(FindCaretScopeResult(None, caretToken)) =>
                // caret definition is not obvious, and we do not even have an AST scope node
                collectCandidates(caret, caretToken, parser)
                ???
            case _ =>
                Seq.empty
        }
    }

    def findCaretScope(caret: CaretInDocument, parser: ApexcodeParser): Future[Option[FindCaretScopeResult]] = {
        findCaretToken(caret, parser) match {
            case Some(caretToken) =>
                //now when we found token corresponding caret position try to understand context
                //collectCandidates(caret, caretToken, parser)
                val resolver = new CaretExpressionResolver(project)
                val tokens = parser.getTokenStream
                resolver.resolveCaretScope(caret, caretToken, tokens).map{
                    case caretScopeOpt @ Some( CaretScope(_, _)) =>
                        Option(FindCaretScopeResult(caretScopeOpt, caretToken))
                    case _  =>
                        Option(FindCaretScopeResult(None, caretToken))
                }
            case None =>
                Future.successful(None)
        }
    }

    /*
    def listCompletions2(file: VirtualDocument, line: Int, column: Int): Future[Seq[Symbol]] = {
        val caret = new CaretInFile(Position(line, column), file)
        findCaretToken(caret) match {
            case Some(findCaretTokenResult) =>
                //val caretReachedException = findCaretTokenResult.ex
                val caretToken = findCaretTokenResult.caretToken
                val parser = findCaretTokenResult.parser
                val tokens = parser.getTokenStream
                //caretReachedException.finalContext
                //now when we found token corresponding caret position try to understand context
                //collectCandidates(caret, caretToken, parser)
                val resolver = new CaretExpressionResolver(project)
                resolver.resolveCaretScope(caret, caretToken, tokens).map{
                    case Some(CaretScope(contextNode, Some(typeDefinition))) =>
                        typeDefinition.getValueType match {
                          case Some(valueType) =>
                              logger.debug("Caret value type: " + valueType)
                              getValueTypeMembers(valueType)
                          case None => Seq.empty
                        }
                    case Some(CaretScope(scopeNode, None)) =>
                        // caret definition is not obvious
                        val candidates = collectCandidates(caret, caretToken, parser)
                        getCandidateSymbols(scopeNode, candidates)
                    case _ =>
                        collectCandidates(caret, caretToken, parser)
                        ???
                }
            case None =>
                Future.successful(Seq.empty)
        }
    }
    */

    private def getCandidateSymbols(scope: AstNode, candidates: CandidatesCollection): Seq[Symbol] = {
        val kindsBuilder = Seq.newBuilder[SymbolKind]
        // TODO continue here
        // collect keywords and convert those to symbols (need to decide on symbol type)
        // see "The final step to get your completion strings is usually something like this:" https://github.com/mike-lischke/antlr4-c3
        candidates.rules.keys.foreach{
            case ApexcodeParser.RULE_classBodyMemberRef =>
                val kinds = Seq(SymbolKind.Enum, SymbolKind.Variable, SymbolKind.Method, SymbolKind.Property)
                kindsBuilder ++= kinds
            case ApexcodeParser.RULE_creator =>
            case ApexcodeParser.RULE_assignmentRightExpr =>
                // right part of assignment expression can be anything
                //TODO consider returning elements defined in nearby scope (e.g. local variables)
        }
        val allSymbolKinds = kindsBuilder.result()
        scope.getSymbolsOfKinds(allSymbolKinds)
    }

    private def findCaretToken(caret: CaretInDocument, parser: ApexcodeParser): Option[Token] = {
        //val lexer = ApexParserUtils.getDefaultLexer(caret.document)
        //val tokens = new CommonTokenStream(lexer)
        //val parser = new ApexcodeParser(tokens)
        //// do not dump parse errors into console (or any other default listeners)
        //parser.removeErrorListeners()
        //parser.setErrorHandler(new BailErrorStrategy)
        ////parser.getInterpreter.setPredictionMode(PredictionMode.SLL)
        parser.getInterpreter.setPredictionMode(PredictionMode.LL)
        try {
            // run actual scan, trying to identify caret position
            parser.compilationUnit()
            //val tree = parser.compilationUnit()
            //print(tree.toStringTree(parser))
        } catch {
            case e:Throwable =>
                logger.debug(e.getMessage)
        }
        var i = 0
        val tokens = parser.getInputStream
        var token: Token = tokens.get(i)
        while (caret.isAfter(token) && Token.EOF != token.getType) {
            i += 1
            token = tokens.get(i)
        }
        if (caret.isInside(token) || caret.isBefore(token)) {
            Option(token)
        } else {
            None
        }
    }

    def createParser(document: VirtualDocument): ApexcodeParser = {
        val lexer = ApexParserUtils.getDefaultLexer(document)
        val tokens = new CommonTokenStream(lexer)
        val parser = new ApexcodeParser(tokens)
        // do not dump parse errors into console (or any other default listeners)
        parser.removeErrorListeners()
        parser.setErrorHandler(new BailErrorStrategy)
        //parser.getInterpreter.setPredictionMode(PredictionMode.SLL)
        //parser.getInterpreter.setPredictionMode(PredictionMode.LL)
        parser
    }

    def collectCandidates(caret: CaretInDocument, caretToken: Token, parser: ApexcodeParser): CandidatesCollection = {
        /*
        val lexer = ApexParserUtils.getDefaultLexer(caret.document)
        val tokens: CommonTokenStream = new CommonTokenStream(lexer)
        val parser = new ApexcodeParser(tokens)
        // do not dump parse errors into console
        ApexParserUtils.removeConsoleErrorListener(parser)
        parser.compilationUnit()
        */

        val core = new CodeCompletionCore(parser)
        core.ignoredTokens = Set(
            ApexcodeLexer.APEXDOC_COMMENT,
            ApexcodeLexer.COMMENT,
            ApexcodeLexer.LINE_COMMENT,
            ApexcodeLexer.BooleanLiteral,
            ApexcodeLexer.StringLiteral,
            ApexcodeLexer.IntegerLiteral,
            ApexcodeLexer.FloatingPointLiteral,
            ApexcodeLexer.SoqlLiteral,
            ApexcodeLexer.SoslLiteral,
            ApexcodeLexer.NULL,
            ApexcodeLexer.VOID

        )
        core.preferredRules = Set(
            ApexcodeParser.RULE_classBodyMemberRef,
            ApexcodeParser.RULE_assignmentRightExpr,
            ApexcodeParser.RULE_creator
        )
        core.showResult = true
        //core.showDebugOutput = true
        //core.showRuleStack = true
        //core.debugOutputWithTransitions = true

        val tokenIndex = caretToken.getTokenIndex
        val res = core.collectCandidates(tokenIndex)
        logger.debug(res.toString)
        res
    }

    private def getValueTypeMembers(valueType: ValueType): Seq[Symbol] = {
        valueType match {
            case ValueTypeComplex(qualifiedName, typeArguments) => ???
            case ValueTypeSimple(qualifiedName) => getSymbolsOf(qualifiedName)
            case ValueTypeClass(qualifiedName) => getSymbolsOf(qualifiedName) //TODO - is this ever used ?
            case ValueTypeInterface(qualifiedName) => ???
            case ValueTypeTrigger(qualifiedName) => ???
            case ValueTypeEnum(qualifiedName) => ???
            case ValueTypeVoid => ???
            case ValueTypeAny => ???
            case ValueTypeArray(qualifiedNameNode) => ???
        }
    }
    private def getSymbolsOf(qualifiedName: QualifiedName): Seq[Symbol] = {
        project.getByQualifiedName(qualifiedName) match {
            case Some(node) => node.findChildrenInAst(_.isSymbol).map(_.asInstanceOf[ClassOrInterfaceBodyMember])
            case None => Seq.empty
        }

    }
}
