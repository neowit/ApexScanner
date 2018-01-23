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
import com.neowit.apexscanner.antlr.{ApexcodeLexer, ApexcodeParser, CodeCompletionCore}
import com.neowit.apexscanner.ast.QualifiedName
import com.neowit.apexscanner.nodes._
import com.neowit.apexscanner.resolvers.QualifiedNameDefinitionFinder
import com.neowit.apexscanner.scanner.actions.ActionContext
import com.typesafe.scalalogging.LazyLogging
import org.antlr.v4.runtime._
import com.neowit.apexscanner.symbols._

case class FindCaretScopeResult(caretScope: Option[CaretScope], caretToken: Token)
/**
  * Created by Andrey Gavrikov 
  */
object CompletionFinder extends LazyLogging {
    /*
    def createParser_old(caret: CaretInDocument, errorHandlerOpt: Option[ANTLRErrorStrategy] = Option(new BailErrorStrategy)): ApexcodeParser = {
        val lexer = ApexParserUtils.getDefaultLexer(caret.document)
        val tokens = new CommonTokenStream(lexer)
        val parser = new ApexcodeParser(tokens)
        // do not dump parse errors into console (or any other default listeners)
        parser.removeErrorListeners()
        errorHandlerOpt.foreach(parser.setErrorHandler)
        //parser.getInterpreter.setPredictionMode(PredictionMode.SLL)
        //parser.getInterpreter.setPredictionMode(PredictionMode.LL)
        parser
    }
    */



}
class CompletionFinder(project: Project) extends LazyLogging {

    def listCompletions(file: VirtualDocument, line: Int, column: Int, actionContext: ActionContext): Seq[Symbol] = {
        val caret = new CaretInDocument(Position(line, column), file)
        listCompletions(caret, actionContext)
    }

    def listCompletions(caret: CaretInDocument, actionContext: ActionContext): Seq[Symbol] = {
        val scopeFinder = new CaretScopeFinder(project, actionContext)
        scopeFinder.findCaretScope(caret) match {
            case Some(FindCaretScopeResult(Some(CaretScope(_, Some(typeDefinition))), _)) =>
                if (actionContext.isCancelled) {
                    return Seq.empty
                }
                getValueTypeMembers(typeDefinition, actionContext)
            case Some(FindCaretScopeResult(Some(CaretScope(scopeNode, None)), caretToken)) =>
                if (actionContext.isCancelled) {
                    return Seq.empty
                }
                /*
                // caret definition is not obvious, but we have an AST scope node
                val (parser, _) = ApexParserUtils.createParserWithCommonTokenStream(caret)
                val candidates = collectCandidates(caret, caretToken, parser)
                getCandidateSymbols(scopeNode, candidates)
                */

                Seq.empty

            case Some(FindCaretScopeResult(None, caretToken)) =>
                if (actionContext.isCancelled) {
                    return Seq.empty
                }
                /*
                // caret definition is not obvious, and we do not even have an AST scope node
                val (parser, _) = ApexParserUtils.createParserWithCommonTokenStream(caret)
                collectCandidates(caret, caretToken, parser)
                */
                Seq.empty
            case _ =>
                Seq.empty
        }
    }

    /*
    private def getCandidateSymbols(scope: AstNode, candidates: CandidatesCollection): Seq[Symbol] = {
        val kindsBuilder = Seq.newBuilder[SymbolKind]
        // TODO continue here
        // collect keywords and convert those to symbols (need to decide on symbol type)
        // see "The final step to get your completion strings is usually something like this:" https://github.com/mike-lischke/antlr4-c3
        candidates.rules.keys.foreach{
            //case ApexcodeParser.RULE_classBodyMemberRef =>
            //    val kinds = Seq(SymbolKind.Enum, SymbolKind.Variable, SymbolKind.Method, SymbolKind.Property)
            //    kindsBuilder ++= kinds
            case ApexcodeParser.RULE_creator =>
            case ApexcodeParser.RULE_assignmentRightExpr =>
                // right part of assignment expression can be anything
                //TODO consider returning elements defined in nearby scope (e.g. local variables)
        }
        val allSymbolKinds = kindsBuilder.result()
        scope.getSymbolsOfKinds(allSymbolKinds)
    }
    */





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
            //ApexcodeParser.RULE_classBodyMemberRef,
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

    private def getValueTypeMembers(typeDefinition: IsTypeDefinition, actionContext: ActionContext): Seq[Symbol] = {
        typeDefinition.resolveDefinition(actionContext) match {
            case Some(defNode: AstNode with IsTypeDefinition) =>
                if (actionContext.isCancelled) {
                    return Seq.empty
                }
                val fullyQualifiedName = QualifiedName.getFullyQualifiedValueTypeName(defNode)
                typeDefinition.getValueType match {
                    case Some(valueType) =>
                        logger.debug("Caret value type: " + valueType)
                        if (actionContext.isCancelled) {
                            return Seq.empty
                        }
                        // first try simple option, by "Value Type" as defined in the code
                        val allSymbols = getValueTypeMembers(valueType, fullyQualifiedName)
                        defNode.filterCompletionSymbols(allSymbols)
                    case None =>
                        Seq.empty
                }
            case _ =>
                throw new IllegalStateException("CompletionFinder.getValueTypeMembers: typeDefinition could not resolve its own definition")
        }
    }

    private def getValueTypeMembers(valueType: ValueType, fullyQualifiedName: QualifiedName): Seq[Symbol] = {
        valueType match {
            case ValueTypeComplex(qualifiedName, typeArguments) => getSymbolsOf(fullyQualifiedName)
            case ValueTypeSimple(qualifiedName) => getSymbolsOf(fullyQualifiedName)
            case ValueTypeClass(qualifiedName) => getSymbolsOf(fullyQualifiedName) //TODO - is this ever used ?
            case ValueTypeInterface(qualifiedName) => getSymbolsOf(fullyQualifiedName)
            case ValueTypeTrigger(qualifiedName) => getSymbolsOf(fullyQualifiedName)
            case ValueTypeEnum(qualifiedName) => getSymbolsOf(fullyQualifiedName)
            case ValueTypeEnumConstant(qualifiedName) => getSymbolsOf(fullyQualifiedName)
            case ValueTypeArray(qualifiedNameNode) =>
                getValueTypeMembers(
                    ValueTypeComplex(
                        QualifiedName(Array("List")),
                        Seq(ValueTypeSimple(qualifiedNameNode.qualifiedName))
                    ),
                    fullyQualifiedName
                )
            case ValueTypeVoid => Seq.empty
            case ValueTypeAnnotation(qualifiedName) => AnnotationNode.getStdAnnotations
            case ValueTypeAny => ???
        }
    }
    private def getSymbolsOf(qualifiedName: QualifiedName): Seq[Symbol] = {
        val qualifiedNameDefinitionFinder = new QualifiedNameDefinitionFinder(project)
        val res =
        qualifiedNameDefinitionFinder.findDefinition(qualifiedName) match {
            case Some(node) =>
                node.getSymbolsForCompletion
            case None => Seq.empty
        }
        res
    }
}
