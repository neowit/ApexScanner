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

trait DataTypeBase extends AstNode {

    override def nodeType: AstNodeType = DataTypeNodeType
    def text: String
    override def getDebugInfo: String = super.getDebugInfo + " " + text
}
case class DataType(qualifiedName: QualifiedNameNode, typeArgumentsOpt: Option[TypeArgumentsNode],
                    locationInterval: LocationInterval) extends DataTypeBase {
    def text: String =
        qualifiedName.text +
            typeArgumentsOpt.map(_.text).getOrElse("")

}

//VOID
case class DataTypeVoid(locationInterval: LocationInterval) extends DataTypeBase {
    override def text: String = "void"
}

//Some[] - array type
case class DataTypeArray(qualifiedName: QualifiedNameNode, locationInterval: LocationInterval) extends DataTypeBase {
    def text: String = qualifiedName.text + "[]"
}


