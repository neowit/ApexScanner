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

import com.neowit.apex.scanner.antlr.ApexcodeParser.{ClassOrInterfaceVisibilityModifierContext, ClassSharingModifierContext}


case class ModifierNode(modifierType: ModifierNode.ModifierType, range: Range) extends AstNode {

    override def nodeType: AstNodeType = ModifierNodeType
    override def getDebugInfo: String = super.getDebugInfo + " " + modifierType
}

object ModifierNode {

    sealed trait ModifierType

    trait Visibility extends ModifierType
    case object PUBLIC extends Visibility
    case object PRIVATE extends Visibility
    case object PROTECTED extends Visibility
    case object GLOBAL extends Visibility

    case object FINAL extends ModifierType
    case object STATIC extends ModifierType
    case object TRANSIENT extends ModifierType
    case object WEBSERVICE extends ModifierType

    case object ABSTRACT extends ModifierType
    case object VIRTUAL extends ModifierType

    case object WITH_SHARING extends ModifierType
    case object WITHOUT_SHARING extends ModifierType

    def visitClassOrInterfaceVisibilityModifier(ctx: ClassOrInterfaceVisibilityModifierContext): AstNode = {
        if (null != ctx.ABSTRACT()) {
            return ModifierNode(ModifierNode.ABSTRACT, Range(ctx.VIRTUAL()))
        }
        if (null != ctx.GLOBAL()) {
            return ModifierNode(ModifierNode.GLOBAL, Range(ctx.GLOBAL()))
        }
        if (null != ctx.PRIVATE()) {
            return ModifierNode(ModifierNode.PRIVATE, Range(ctx.PRIVATE()))
        }
        if (null != ctx.PUBLIC()) {
            return ModifierNode(ModifierNode.PUBLIC, Range(ctx.PUBLIC()))
        }
        if (null != ctx.VIRTUAL()) {
            return ModifierNode(ModifierNode.VIRTUAL, Range(ctx.VIRTUAL()))
        }
        if (null != ctx.WEBSERVICE()) {
            return ModifierNode(ModifierNode.WEBSERVICE, Range(ctx.WEBSERVICE()))
        }
        NullNode
    }

    def visitClassSharingModifier(ctx: ClassSharingModifierContext): AstNode = {
        if (null != ctx.WITH_SHARING()) {
            return ModifierNode(ModifierNode.WITH_SHARING, Range(ctx.WITH_SHARING()))
        }
        if (null != ctx.WITHOUT_SHARING()) {
            return ModifierNode(ModifierNode.WITHOUT_SHARING, Range(ctx.WITHOUT_SHARING()))
        }
        //if no sharing modifier provided - by default assume WITHOUT_SHARING
        ModifierNode(ModifierNode.ABSTRACT, Range(ctx.WITHOUT_SHARING()))
    }
}
