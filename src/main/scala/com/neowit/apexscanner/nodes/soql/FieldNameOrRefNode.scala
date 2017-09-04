package com.neowit.apexscanner.nodes.soql

import com.neowit.apexscanner.ast.QualifiedName
import com.neowit.apexscanner.nodes.{AstNode, AstNodeType, FieldNameOrRefNodeType, HasTypeDefinition, Range}

/**
  * Created by Andrey Gavrikov 
  */
case class FieldNameOrRefNode(qualifiedName: QualifiedName, range: Range) extends AstNode with HasTypeDefinition {
    override def nodeType: AstNodeType = FieldNameOrRefNodeType

    override protected def resolveDefinitionImpl(): Option[AstNode] = {
        // assume first component of qualifiedName is alias
        val maybeAliasName = qualifiedName.getFirstComponent
        val fromObjectRefOpt =
            SoqlAstUtils.findFromNode(this, Option(maybeAliasName)) match {
                case Some(FromNode(objectRef, _, _)) => Option(objectRef)
                case None =>
                    // looks like first component of QualifiedName is not an alias
                    SoqlAstUtils.findFromNode(this, aliasOpt = None) match {
                        case Some(FromNode(objectRef, _, _)) => Option(objectRef)
                        case None => None
                    }
            }

        fromObjectRefOpt  match {
            case Some(fromObjectRef) =>
                getProject.flatMap(_.getByQualifiedName(fromObjectRef))
            case None => None
        }
    }
}
