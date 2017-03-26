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

import com.neowit.apex.scanner.antlr.ApexcodeParser.AnnotationContext

case class AnnotationNode(name: String, params: List[AnnotationParameter], locationInterval: LocationInterval) extends AstNode {
    override def nodeType: AstNodeType = AnnotationNodeType

    /**
      * used for debug purposes
      *
      * @return textual representation of this node and its children
      */
    override def getDebugInfo: String = {
        val myText =
        if (params.nonEmpty) {
            name + "(" + params.map(_.getDebugInfo).mkString(",") + ")"
        } else {
            name
        }
        super.getDebugInfo + " " + myText
    }
}

object AnnotationNode {
    def visitAnnotation(ctx: AnnotationContext): AstNode = {
        val annotation = AnnotationNode(name = ctx.annotationName().getText, params = Nil, LocationInterval(ctx))
        //TODO define annotation params
        annotation
    }
}

case class AnnotationParameter(name: String, value: String, locationInterval: LocationInterval) extends AstNode {
    override def nodeType: AstNodeType = AnnotationParameterNodeType
    override def getDebugInfo: String = {
        super.getDebugInfo + " " + name + "=" + value
    }
}

