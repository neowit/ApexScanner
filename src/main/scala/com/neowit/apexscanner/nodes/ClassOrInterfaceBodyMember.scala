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

/**
  * Created by Andrey Gavrikov 
  */
trait ClassOrInterfaceBodyMember extends Symbol {
    def getClassOrInterfaceNode: ClassLike
    def getClassOrInterfaceName: Option[QualifiedName] = getClassOrInterfaceNode.qualifiedName

    protected def getSelf: AstNode

    override def symbolLocation: Location = {
        getSelf.getFileNode  match {
            case Some(fileNode) =>
                new Location {

                    override def project: Project = fileNode.project

                    override def range: Range = getSelf.range

                    override def path: Path = fileNode.file
                }
            case None => LocationUndefined
        }
    }
    override def visibility: Option[String] = {
        getSelf.findChildInAst{
            case ModifierNode(modifierType, _) if modifierType.isInstanceOf[ModifierNode.Visibility] => true
            case _ => false
        }.map{
            case ModifierNode(modifierType, _) => modifierType.toString
            case _ => "private"
        }
    }
}
