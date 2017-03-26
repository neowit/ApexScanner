package com.neowit.apex.nodes

sealed trait AstNodeType

case object AnnotationNodeType extends AstNodeType
case object AnnotationParameterNodeType extends AstNodeType
case object ClassNodeType extends AstNodeType
case object ClassVariableNodeType extends AstNodeType
case object DataTypeNodeType extends AstNodeType
case object DocNodeType extends AstNodeType
case object ExpressionNodeType extends AstNodeType
case object ExtendsNodeType extends AstNodeType
case object FallThroughNodeType extends AstNodeType
case object FileNodeType extends AstNodeType
case object IdentifierNodeType extends AstNodeType
case object ImplementsInterfaceNodeType extends AstNodeType
case object InterfaceNodeType extends AstNodeType
case object MethodNodeType extends AstNodeType
case object MethodParameterNodeType extends AstNodeType
case object ModifierNodeType extends AstNodeType
case object NullNodeType extends AstNodeType
case object QualifiedNameNodeType extends AstNodeType
case object TypeArgumentsNodeType extends AstNodeType
case object VariableNodeType extends AstNodeType

