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

package com.neowit.apexscanner.stdlib.impl

import com.neowit.apexscanner.extlib.CodeLibrary

/**
  * Created by Andrey Gavrikov 
  */
trait StdlibJsonBaseVisitor[T] {
    def visit(context: ApexApiJson): CodeLibrary
    def visitApexApiJsonNamespace(name: String, context: ApexApiJsonNamespace): T
    def visitApexApiJsonClass(name: String, context: ApexApiJsonClass): T
    def visitApexApiJsonProperty(context: ApexApiJsonProperty): T
    def visitApexApiJsonMethodParameter(context: ApexApiJsonMethodParameter): T
    def visitApexApiJsonMethod(context: ApexApiJsonMethod): T

}
