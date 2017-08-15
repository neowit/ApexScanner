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

package com.neowit.apexscanner.nodes

import java.nio.file.Path

import com.neowit.apexscanner.Project
import com.neowit.apexscanner.ast.QualifiedName
import com.neowit.apexscanner.symbols.Symbol

object ClassLike {
    val CLASS_LIKE_TYPES: Set[AstNodeType] = Set(ClassNodeType, InterfaceNodeType, TriggerNodeType)
}
trait ClassLike extends AstNode with HasApexDoc with IsTypeDefinition with Symbol { self =>
    import ClassLike._

    def name: Option[String] = getChildInAst[IdentifierNode](IdentifierNodeType).map(_.name)
    def annotations: Seq[AnnotationNode] = getChildrenInAst[AnnotationNode](AnnotationNodeType)
    def modifiers: Set[ModifierNode] = getChildrenInAst[ModifierNode](ModifierNodeType).toSet

    override def symbolIsStatic: Boolean = modifiers.exists(_.modifierType == ModifierNode.STATIC)

    override def symbolValueType: Option[String] = getValueType.map(_.qualifiedName.toString)

    /**
      * find container of current class/interface
      */
    def parentClassOrInterface: Option[ClassLike] = findParentInAst(n => CLASS_LIKE_TYPES.contains(n.nodeType)).map(_.asInstanceOf[ClassLike])

    /**
      * find child with given name which represents inner class or enum
      * @param qualifiedName child name
      * @return
      */
    def findClassLikeChild(qualifiedName: QualifiedName): Option[ClassLike] = {
        findChildInAst{
            case n: ClassLike => n != this && n.qualifiedName.exists(_.couldBeMatch(qualifiedName))
            case _ => false
        }.map(_.asInstanceOf[ClassLike])
    }

    /**
      * get super class of current class/interface
      * @return
      */
    def getSuperClassOrInterface: Option[ClassLike] = {
        extendsNode match {
            case Some(_extendsNode) =>
                // find definition of super class in global class map
                getProject match {
                    case Some(project) =>
                        _extendsNode.dataType match {
                            case Some(valueType) =>
                                project.getByQualifiedName(valueType.qualifiedName) match {
                                    case Some(n: ClassLike) =>
                                        Option(n)
                                    case _ => None
                                }
                            case None => None
                        }
                    case None => None
                }
            case None => None
        }
    }

    /**
      * full qualified name, including Parent(s)
      * ParentClass.CurrentClass
      * @return
      */
    def qualifiedName: Option[QualifiedName] = {
        val thisNameComponents = name.map(Array(_)).getOrElse(Array.empty[String])
        val thisName = QualifiedName(thisNameComponents)
        parentClassOrInterface match {
          case Some(parent) =>
              QualifiedName.fromOptions(parent.qualifiedName, Option(thisName))
          case None =>
              Option(thisName)
        }
    }

    override def symbolName: String = name.getOrElse("")

    override def symbolLocation: Location = {
        findParentInAst(_.nodeType == FileNodeType)
            .map(_.asInstanceOf[FileNode])
            .map(f => new Location {
                override def range: Range = self.range

                override def project: Project = f.project

                override def path: Path = f.file
            })
            .getOrElse(LocationUndefined)
    }


    override def parentSymbol: Option[Symbol] = parentClassOrInterface

    def hasModifier(modifierType: ModifierNode.ModifierType): Boolean = {
        modifiers.exists(m => m.modifierType == modifierType)
    }

    def extendsNode: Option[ExtendsNode] = getChildInAst[ExtendsNode](ExtendsNodeType)
    def extendsText: Option[String] = extendsNode.flatMap(_.dataType.map(_.toString))

    def implements: Seq[ImplementsInterfaceNode] = getChildrenInAst[ImplementsInterfaceNode](ImplementsInterfaceNodeType)
    def implementsText: Seq[String] = implements.map(_.text)

    override def getApexDoc: Option[DocNode] = getChildInAst[DocNode](DocNodeType)

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
