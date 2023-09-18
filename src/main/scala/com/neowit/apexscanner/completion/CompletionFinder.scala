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

                Seq.empty

            case Some(FindCaretScopeResult(None, caretToken)) =>
                if (actionContext.isCancelled) {
                    return Seq.empty
                }
                Seq.empty
            case _ =>
                Seq.empty
        }
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
                    //fullyQualifiedName
                    QualifiedName(Array("List"))//arrays in Apex is the same as List, so String[] is the same as List<String>
                )
            case ValueTypeVoid => Seq.empty
            case ValueTypeAnnotation(qualifiedName) => AnnotationNode.getStdAnnotations
            case ValueTypeAny => ???
            case _ => Seq()
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
