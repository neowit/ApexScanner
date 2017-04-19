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

package com.neowit.apex.ast

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

    def endsWith(name: QualifiedName): Boolean = componentsLower.endsWith(name.componentsLower)

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

    override def toString: String = components.mkString(".")
}

object QualifiedName {
    def apply(n1: QualifiedName, n2: String): QualifiedName = {
        QualifiedName(n1.components ++ Array(n2))
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

}
