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

package com.neowit.apexscanner.completion

import org.antlr.v4.runtime.misc.IntervalSet
import org.antlr.v4.runtime._

/**
  * Created by Andrey Gavrikov 
  */
class CompletionErrorStrategy extends DefaultErrorStrategy{
    override def reportError(recognizer: Parser, e: RecognitionException): Unit = {
        if (e != null && e.getOffendingToken != null &&
            e.getOffendingToken.getType == CaretToken.CARET_TOKEN_TYPE) {
            return
        }
        super.reportError(recognizer, e)
    }


    override def recover(recognizer: Parser, e: RecognitionException): Unit = {
        if (e != null && e.getOffendingToken != null) {
            if (e.getOffendingToken.getType == CaretToken.CARET_TOKEN_TYPE) {
                throw new CaretReachedException(recognizer, recognizer.getContext, e.getOffendingToken.asInstanceOf[CaretToken], Some(e))
            } else if (e.getInputStream.index() + 1 <= e.getInputStream.size() &&
                e.getInputStream.asInstanceOf[CommonTokenStream].LT(2).getType == CaretToken.CARET_TOKEN_TYPE) {
                throw new CaretReachedException(recognizer, recognizer.getContext, e.getInputStream.asInstanceOf[CommonTokenStream].LT(2).asInstanceOf[CaretToken], Some(e))
            }
        }
        super.recover(recognizer, e)
    }
    override def consumeUntil(recognizer: Parser, set: IntervalSet): Unit = {
        super.consumeUntil(recognizer, set)
    }

    override def recoverInline(recognizer: Parser): Token = {
        if (recognizer.getInputStream.LA(1) == CaretToken.CARET_TOKEN_TYPE) {
            throw new CaretReachedException(recognizer, recognizer.getContext, recognizer.getInputStream.LT(1).asInstanceOf[CaretToken])
        }
        super.recoverInline(recognizer)
    }

    override def singleTokenInsertion(recognizer: Parser): Boolean = {
        if (recognizer.getInputStream.LA(1) == CaretToken.CARET_TOKEN_TYPE) {
            return false
        }
        super.singleTokenInsertion(recognizer)
    }

    override def singleTokenDeletion(recognizer: Parser): Token = {
        if (recognizer.getInputStream.LA(1) == CaretToken.CARET_TOKEN_TYPE) {
            return null
        }
        super.singleTokenDeletion(recognizer)
    }

    override def sync(recognizer: Parser): Unit = {
        if (recognizer.getInputStream.LA(1) == CaretToken.CARET_TOKEN_TYPE) {
            return
        }
        super.sync(recognizer)
    }
}
