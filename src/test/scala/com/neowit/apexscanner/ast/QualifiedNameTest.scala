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

import org.scalatest.funsuite.AnyFunSuite
/**
  * Created by Andrey Gavrikov 
  */
class QualifiedNameTest extends AnyFunSuite {
    test("Qualified Name Merge") {
        var result =
            QualifiedName(
                QualifiedName(Array("Parent")),
                QualifiedName(Array("Child"))
            )
        assertResult(QualifiedName(Array("Parent", "Child")))(result)

        result =
            QualifiedName(
                QualifiedName(Array("Parent")),
                QualifiedName(Array("paRent", "Child"))
            )
        assertResult(QualifiedName(Array("Parent", "Child")))(result)


        // System.Parent
        // Parent.Child
        result =
            QualifiedName(
                QualifiedName(Array("System", "Parent")),
                QualifiedName(Array("Parent", "Child"))
            )
        assertResult(QualifiedName(Array("System", "Parent", "Child")))(result)
    }


    test("Name Comparison") {
        assert(QualifiedName(Array("tEst")) == QualifiedName(Array("Test")))
        assert(QualifiedName(Array("tEst", "Other")) == QualifiedName(Array("Test", "othEr")))
        assert(QualifiedName(Array("tEst", "SEcond", "_third")) == QualifiedName(Array("Test", "seCond", "_Third")))
    }
}
