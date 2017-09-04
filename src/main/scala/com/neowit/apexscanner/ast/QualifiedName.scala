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

package com.neowit.apexscanner.ast

import com.neowit.apexscanner.nodes._

/**
  * Created by Andrey Gavrikov
  *
  * this is different from QualifiedNameNode as not being part of AST
  */
case class QualifiedName(components: Array[String]) {
    protected val componentsLower:Array[String] = components.map(_.toLowerCase)
    val length: Int = components.length

    def getFirstComponent: String = components.head
    def getLastComponent: String = components.last

    lazy val head: QualifiedName = QualifiedName(Array(getFirstComponent))
    lazy val tailOption: Option[QualifiedName] = {
        if (length > 1) {
            Option(QualifiedName(components.drop(1)))
        } else {
            None
        }
    }

    def endsWith(name: QualifiedName): Boolean = componentsLower.endsWith(name.componentsLower)

    def contains(name: String): Boolean = {
        null != name && componentsLower.contains(name.toLowerCase)
    }
    /**
      * check if two Qualified Names may be match by assuming that if one ends with another then they may be matching
      * @param otherName name to compare with
      * @return
      */
    def couldBeMatch(otherName: QualifiedName): Boolean = {
        val (left, right) =
            if (length > otherName.length)
                (this, otherName)
            else
                (otherName, this)
        left.endsWith(right)
    }

    def couldBeMatch(otherName: Option[QualifiedName]): Boolean = {
        otherName match {
            case Some(name) =>  couldBeMatch(name)
            case None => false
        }
    }

    override def toString: String = components.mkString(".")

    override def canEqual(that: Any): Boolean = {
        that match {
            case qName @ QualifiedName(_) =>
                componentsLower.sameElements(qName.componentsLower)
            case _ => false
        }
    }


    override def hashCode(): Int = {
        componentsLower.mkString("").hashCode
    }

    override def equals(obj: scala.Any): Boolean = {
        canEqual(obj)
    }
}

object QualifiedName {
    def apply(n1: QualifiedName, n2: String): QualifiedName = {
        QualifiedName(n1.components ++ Array(n2))
    }

    def apply(names: String*): QualifiedName = {
        QualifiedName(names.toArray)
    }
    /**
      * merge parent and child names
      * @return
      */
    def fromOptions(parentOpt: Option[QualifiedName], childOpt: Option[QualifiedName]): Option[QualifiedName] = {
        parentOpt match {
          case Some(parent) if childOpt.isDefined =>
              childOpt.map(child => QualifiedName(parent, child))
          case Some(parent) if childOpt.isEmpty =>
              parentOpt
          case _ =>
                childOpt
        }
    }

    /**
      * merge parent and child names
      * e.g.
      * - apply(OuterClass, InnerClass) => OuterClass.InnerClass
      * - apply(OuterClass, OuterClass.InnerClass) => OuterClass.InnerClass
      * @param parent name of parent in class hierarchy
      * @param child name of child (may already contain parent) in class hierarchy
      * @return
      */
    def apply(parent: QualifiedName, child: QualifiedName): QualifiedName = {
        import scala.util.control.Breaks._
        val parentComponents = parent.components
        val childHead = child.getFirstComponent.toLowerCase
        val resultBuilder = Array.newBuilder[String]
        breakable {
            for (name <- parentComponents) {
                if (childHead == name.toLowerCase) {
                    //parent name is already included
                    break
                } else {
                    // parent name needs to be added
                    resultBuilder += name
                }
            }
        }
        QualifiedName(resultBuilder.result() ++ child.components)
    }
    private val _CLASS_BODY_MEMBER_TYPES = ClassLike.CLASS_LIKE_TYPES + EnumNodeType
    /**
      * class variable defined like so
      *     InnerClass1 var1;
      * - where InnerClass1 reside inside current outer class
      * should have qualified valuetype name OuterClass.InnerClass1
      * @param typeDefNode IsTypeDefinition node
      * @return
      */
    def getFullyQualifiedValueTypeName(typeDefNode: AstNode with IsTypeDefinition): QualifiedName = {
        val resNameOpt =
            typeDefNode.getValueType.flatMap{childName =>
                typeDefNode.findParentInAst{
                    case n:ClassLike if n.supportsInnerClasses => true
                    case _ => false
                } match {
                    case Some(parentClassNode: ClassLike) =>
                        //check if parent does indeed contain InnerClass childName node
                        val predicate: AstNode => Boolean = {
                            case n: IsTypeDefinition =>
                                _CLASS_BODY_MEMBER_TYPES.contains(n.nodeType) && childName.qualifiedName.couldBeMatch(n.qualifiedName)
                            case _ => false

                        }
                        parentClassNode.findChildInAst( predicate ) match {
                        //parentClassNode.findChildInAst(n => ClassLike.CLASS_LIKE_TYPES.contains(n.nodeType) ) match {
                          case Some(_) =>
                              parentClassNode.qualifiedName.map{outerClassName =>
                                  QualifiedName(outerClassName, childName.qualifiedName)
                              }
                          case None =>
                              // given child is not defined as a ClassLike member of parentClassNode
                              //i.e. it is not inner class or enum
                              Option(childName.qualifiedName)
                        }
                    case _ =>
                        // there is no parent
                        Option(childName.qualifiedName)
                }
            }
        resNameOpt match {
            case Some(resultName) => resultName
            case None => throw new NotImplementedError("Handling of AstNode with IsTypeDefinition without Qualified Name is not implemented")
        }
    }

    /**
      * try to assemble full name, including parent's name
      * @param astNode
      * @return
      */
    def getFullyQualifiedName(astNode: AstNode with HasQualifiedName): Option[QualifiedName] = {
        astNode.qualifiedName.flatMap{childName =>
            astNode.findParentInAst(n => ClassLike.CLASS_LIKE_TYPES.contains(n.nodeType)) match {
                case Some(parentClassNode: ClassLike) =>
                    parentClassNode.qualifiedName.map{outerClassName =>
                        QualifiedName(outerClassName, childName)
                    }
                case _ =>
                    // there is no parent or it is not ClassLike
                    Option(childName)
            }
        }
    }
}
