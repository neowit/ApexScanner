package com.neowit.apex.scanner.actions

import java.nio.file.Path


sealed trait BaseResult

case class SyntaxCheckResult(sourceFile: Path, errors: Seq[SyntaxError]) extends BaseResult
