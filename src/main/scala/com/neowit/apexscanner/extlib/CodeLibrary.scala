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

package com.neowit.apexscanner.extlib

import com.neowit.apexscanner.Project
import com.neowit.apexscanner.ast.QualifiedName
import com.neowit.apexscanner.nodes.{AstNode, HasQualifiedName, NamespaceNode}

import scala.collection.mutable

/**
  * Created by Andrey Gavrikov 
  */
trait CodeLibrary {
    def getName: String

    def load(project: Project): CodeLibrary
    def isLoaded: Boolean

    private val _containerByQName = new mutable.HashMap[QualifiedName, AstNode with HasQualifiedName]()
    private val _namespaceByQName = new mutable.HashMap[QualifiedName, NamespaceNode]()

    /**
      * add given class/interface/trigger to global map of top level containers
      * @param node usually class/interface/trigger
      */
    def addByQualifiedName(node: AstNode with HasQualifiedName): Unit = {
        node.qualifiedName match {
            case Some(qName) => _containerByQName += qName -> node
            case None =>
        }
        // check if this node is a Namespace
        node match {
            case n @ NamespaceNode(Some(name)) =>
                n.qualifiedName.foreach(qName => _namespaceByQName += qName -> n)
            case _ => // do nothing
        }
    }

    /**
      * use this to purge from "node-by-QualifiedName" cache all nodes added from specific ClassLike file
      * useful when source has changed and its AST needs to be re-built next time when it is required
      * @param qualifiedName e.g. class name
      */
    def removeByQualifiedName(qualifiedName: QualifiedName): Unit = {
        // remove all nodes with key starting with given QualifiedName
        _containerByQName.filterKeys(_.startsWith(qualifiedName))
            .foreach(key => _containerByQName.remove(qualifiedName))
    }

    def getByQualifiedName(qualifiedNameOpt: Option[QualifiedName]): Option[AstNode] = {
        qualifiedNameOpt match {
            case Some(qualifiedName) => getByQualifiedName(qualifiedName)
            case None => None
        }
    }
    /**
      * NOTE: this method only finds nodes physically added via addByQualifiedName()
      * It will not find class methods/variables or Enum constants,
      * when target node type is not already known to be Namespace or Class - use QualifiedNameDefinitionFinder
      */
    def getByQualifiedName(qualifiedName: QualifiedName): Option[AstNode] = {
        _containerByQName.get(qualifiedName).orElse{
            //check if this name is in one of available namespaces
            // first try to find namespace which contains class with name == qualifiedName
            val namespaceQNameOpt =
            getDefaultNamespaces.find{ qName =>
                val fullName = QualifiedName(qName, qualifiedName)
                _containerByQName.contains(fullName)
            }
            // retrieve class node from namespace (if namespace found)
            namespaceQNameOpt match {
                case Some(namespaceQName) =>
                    val fullName = QualifiedName(namespaceQName, qualifiedName)
                    _containerByQName.get(fullName)
                case None => None
            }
        }
    }

    /**
      * if getByQualifiedName(qualifiedName) returns nothing then there are may be cases when we can look for specific qualifiedName
      * inside default namespaces, i.e. namespaces which do not require namespace name to be specified
      * e.g. System.Test.isRunningTest() and Test.isRunningTest() are both allowed
      * @return
      */
    protected def getDefaultNamespaces: Seq[QualifiedName] = {
        Seq.empty
    }
}
