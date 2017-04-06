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

/**
  * Created by Andrey Gavrikov 
  */
case class MethodHeaderNode(range: Range) extends AstNode{
    override def nodeType: AstNodeType = MethodHeaderNodeType
    def methodName: Option[String] = getChild[MethodNameNode](MethodNameNodeType).map(_.name)
    def dataType: Option[DataType] = getChild[DataTypeNode](DataTypeNodeType).map(_.getDataType)
}
