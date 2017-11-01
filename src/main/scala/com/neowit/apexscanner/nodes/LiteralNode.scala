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

import com.neowit.apexscanner.ast.QualifiedName
import org.antlr.v4.runtime.tree.TerminalNode
import com.neowit.apexscanner.antlr.ApexcodeParser._

/**
  * Created by Andrey Gavrikov 
  */
object LiteralNode {
    private def getStandardNamespace(literalType: Int): QualifiedName = {
        literalType match {
            case IntegerLiteral => QualifiedName(Array("System", "Integer"))
            case LongLiteral => QualifiedName(Array("System", "Long"))
            case StringLiteral => QualifiedName(Array("System", "String"))
            case FloatingPointLiteral => QualifiedName(Array("System", "Decimal"))
            case BooleanLiteral => QualifiedName(Array("System", "Boolean"))
            case NULL => QualifiedName(Array("System", "Object"))
            //TODO add SoslLiteral & SoqlLiteral
            case x => throw new NotImplementedError(s"Support for literal of type $x is not implemented")
        }
    }
}
case class LiteralNode(literalType: Int, valueTerminal: TerminalNode, range: Range) extends LiteralLike {
    import LiteralNode._

    def getText: String = valueTerminal.getText

    override protected def resolveDefinitionImpl(actionContext: com.neowit.apexscanner.scanner.actions.ActionContext): Option[AstNode] = {
        getProject.flatMap(_.getByQualifiedName(getStandardNamespace(literalType)))
    }

}





