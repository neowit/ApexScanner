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

import com.neowit.apexscanner.antlr.SoqlBaseVisitor
import com.neowit.apexscanner.nodes.AstNode
import com.neowit.apexscanner.{Project, VirtualDocument}

/**
  * Created by Andrey Gavrikov 
  */
object SoqlAstBuilderVisitor {
    val VISITOR_CREATOR_FUN: AstBuilder.VisitorCreatorFun = (projectOpt, documentOpt) => new SoqlAstBuilderVisitor(projectOpt, documentOpt)
}
class SoqlAstBuilderVisitor(override val projectOpt: Option[Project], override val documentOpt: Option[VirtualDocument]) extends SoqlBaseVisitor[AstNode] with AstBuilderVisitor {
    override def onComplete(): Unit = ???
}
