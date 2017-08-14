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

package com.neowit.apexscanner.resolvers

import com.neowit.apexscanner.Project
import com.neowit.apexscanner.ast.QualifiedName
import com.neowit.apexscanner.matchers.MethodMatcher
import com.neowit.apexscanner.nodes._

import scala.annotation.tailrec
import scala.concurrent.{ExecutionContext, Future}

object AscendingDefinitionFinder {
    type NodeMatcherFunc = AstNode => Boolean

    def variableMatchFunc(targetName: QualifiedName): NodeMatcherFunc = {
        case node: IsTypeDefinition =>
            node.qualifiedName.exists(_.couldBeMatch(targetName))
        case _ => false
    }

    def methodMatchFunc(targetCaller: MethodCallNode): NodeMatcherFunc = (n: AstNode) => {
        val methodMatcher = new MethodMatcher(targetCaller)
        n match {
            case node: MethodNode =>
                methodMatcher.isSameMethod(node)
            case _ => false
        }
    }
}
/**
  * Created by Andrey Gavrikov
  *
  * attempts to find node defining expression at specified location
  * this is ASCENDING lookup, starts from the bottom and goes up
  * Use this to find definition in the same file as target node
  */
class AscendingDefinitionFinder() {
    import AscendingDefinitionFinder._

    /**
      * this method returns closest definition
      * e.g.
      * 1.    MyType str;
      * 2.    str = "";
      * - definition for `str` is VariableNode in line 1 containing `MyType str;`
      *
      * However - if user requested definition for `MyType` in line 1 this method will return DataTypeNode containing `MyType`
      * in line 1.
      * In order to get an ultimate ClassNode defining MyClass use AscendingDefinitionFinder.findUltimateDefinition()
      *
      * @param rootNode where to start looking for definition of node residing in provided position
      * @param position location of node which definition is requested
      * @return
      */
    def findDefinition(rootNode: AstNode, position: Position): Seq[AstNode] = {
        // first find actual node which we need to find the definition for
        val nodeFinder = new NodeByLocationFinder(position)
        nodeFinder.findInside(rootNode) match {
            case Some(targetNode: HasTypeDefinition)=>
                //targetNode.resolveDefinition()
                targetNode.resolveDefinition().map(Seq(_)).getOrElse(Seq.empty)
            case Some(targetNode)=>
                println("AscendingDefinitionFinder: " + targetNode)
                // TODO - make sure we do not need this
                //findDefinition(targetNode, targetNode)
                throw new NotImplementedError("Handling of node without HasTypeDefinition is not implemented and should not be needed")
            case None =>
                Seq.empty
        }
    }

    /**
      * this method tries to find an ultimate defining node
      * e.g.
      * 1. MyClass cls;
      * 2. cls = "";
      * - user requested definition of `cls` in line 2
      * findUltimateDefinition() will return ClassNode defining MyClass
      *
      * @param rootNode where to start looking for definition of node residing in provided position
      * @param position location of node which definition is requested
      * @param project current project
      * @param ec execution context to run
      * @return
      */
    def findUltimateDefinition(rootNode: AstNode, position: Position, project: Project)
                              (implicit ec: ExecutionContext): Future[Seq[AstNode with IsTypeDefinition]] = {
        val qualifiedNameDefinitionFinder = new QualifiedNameDefinitionFinder(project)
        val nodes = findDefinition(rootNode, position)

        val futureResults: Seq[Future[Option[AstNode]]] =
            nodes.map{
                case n: DataTypeNode =>
                    //this is a type defining node
                    //e.g. MyType where caret is placed like so MyT<Caret>ype
                    // - check if this type is defined in available source
                    n.qualifiedName match {
                        case Some(qualifiedName) =>
                            qualifiedNameDefinitionFinder.findDefinition(qualifiedName)
                        case None => Future.successful(Option(n)) //do not really expect this
                    }
                case n => Future.successful(Option(n)) // assume this node is the target
            }

        // convert Seq[Future[Option[AstNode]]] to Future[Seq[Option[AstNode]]]
        val res: Future[Seq[AstNode with IsTypeDefinition]] =
            Future.sequence(futureResults).map{ nodeOpts =>
                val allDefNodes = nodeOpts.filter(_.isDefined).map(_.get)
                allDefNodes.filter {
                    case defNode: AstNode with IsTypeDefinition => true
                    case _ => false
                }.map {
                    case defNode: AstNode with IsTypeDefinition =>
                        defNode
                }
            }
        res
    }

    def findDefinition(target: AstNode, startNode: AstNode): Seq[AstNode] = {
        target match {
            case methodCaller: MethodCallNode =>
                // looks like target is a method call
                // try to resolve call parameter types first
                //val paramTypes = resolveMethodCallParameters(methodCaller)
                val paramTypes = methodCaller.getParameterTypes
                methodCaller.setResolvedParameterTypes(paramTypes)
                val matchFunc = methodMatchFunc(methodCaller)
                findDefinitionInternal(target, methodCaller.methodName, startNode, matchFunc, Seq.empty)
            case t:IdentifierNode =>
                val targetName = QualifiedName(Array(t.name))
                // assume variable
                val matchFunc = variableMatchFunc(targetName)
                findDefinitionInternal(target, targetName, startNode, matchFunc, Seq.empty)
            case t:DataTypeNodeGeneric =>
                val targetName = t.qualifiedNameNode.qualifiedName
                // assume variable
                val matchFunc = variableMatchFunc(targetName)
                findDefinitionInternal(target, targetName, startNode, matchFunc, Seq.empty)
            case t:FallThroughNode =>
                //TODO - given an expression (e.g. method parameter) - resolve its final type
                // maybe FallThroughNode is not the best type here
                //resolveExpressionType(t)
                ???

            case _ =>
                Seq.empty
        }

    }

    /**
      * starting from startNode go UP  the scope (node hierarchy) and look for potential definition of given target node
      * @param target node definition of which we are trying to find
      * @param targetName  QualifiedName of the node we are trying to find
      * @param startNode lowest node to start from and go UP
      * @return
      */
    @tailrec
    private def findDefinitionInternal(target: AstNode, targetName: QualifiedName, startNode: AstNode,
                                       isMatching: AstNode => Boolean, foundNodes: Seq[AstNode]): Seq[AstNode] = {

        startNode.getParentInAst(true) match {
            case Some(parent) =>
                parent.findChildrenInAst(n => isMatching(n) && n != target && !foundNodes.contains(n), recursively = true) match {
                    case definitionNodes if definitionNodes.nonEmpty =>
                        findDefinitionInternal(target, targetName, parent, isMatching, foundNodes ++ definitionNodes)
                    case _ =>
                        // go higher in parents hierarchy
                        findDefinitionInternal(target, targetName, parent, isMatching, foundNodes)
                }
            case None =>
                foundNodes
        }
    }
}
