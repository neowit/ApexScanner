package com.neowit.apex.scanner

import java.nio.file.Path


sealed trait BaseResult

case class SyntaxCheckResult(sourceFile: Path, errors: Seq[SyntaxError]) extends BaseResult
