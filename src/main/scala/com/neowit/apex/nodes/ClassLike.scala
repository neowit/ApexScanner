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

package com.neowit.apex.nodes

trait ClassLike extends AstNode with HasApexDoc {

    def name: Option[String] = getChild[IdentifierNode](IdentifierNodeType).map(_.name)
    def annotations: Seq[AnnotationNode] = getChildren[AnnotationNode](AnnotationNodeType)
    def modifiers: Set[ModifierNode] = getChildren[ModifierNode](ModifierNodeType).toSet

    def extendsNode: Option[ExtendsNode] = getChild[ExtendsNode](ExtendsNodeType)
    def extendsText: Option[String] = extendsNode.flatMap(_.dataType.map(_.text))

    def implements: Seq[ImplementsInterfaceNode] = getChildren[ImplementsInterfaceNode](ImplementsInterfaceNodeType)
    def implementsText: Seq[String] = implements.map(_.text)

    override def getApexDoc: Option[DocNode] = getChild[DocNode](DocNodeType)

    /**
      * used for debug purposes
      *
      * @return textual representation of this node and its children
      */
    override def getDebugInfo: String = {
        val annotationsText = annotations.map(_.getDebugInfo).mkString(" ")
        val modifiersText = modifiers.map(_.modifierType).mkString(" ")
        val implementsStr = if (implementsText.isEmpty) "" else "implements " +  implementsText.mkString(",")
        super.getDebugInfo +
            annotationsText + " " +
            modifiersText + " " +
            name.getOrElse("") + " " +
            extendsText.map(t => "extends " + t).getOrElse("") + " " +
            implementsStr

    }
}
