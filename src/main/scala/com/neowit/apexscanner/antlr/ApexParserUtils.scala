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

package com.neowit.apexscanner.antlr

import java.util.regex.Pattern

import com.neowit.apexscanner.completion.CaretInDocument
import org.antlr.v4.runtime._
import org.antlr.v4.runtime.misc.Interval
/**
  * Created by Andrey Gavrikov 
  */
object ApexParserUtils {
    private val WORD_PATTERN_STR = "^[\\$A-Za-z_][A-Za-z0-9_]*$"
    private val WORD_PATTERN: Pattern = Pattern.compile(WORD_PATTERN_STR)

    def isWordToken(token: Token): Boolean = {
        isWordPattern(token.getText)
    }
    def isWordPattern(text: String): Boolean = {
        WORD_PATTERN.matcher(text).matches
    }
    def isDotToken(token: Token): Boolean = {
        "." == token.getText
    }
    /**
      * in most cases there is no need to dump syntax errors into console
      * @param parser - ApexcodeParser from which to remove console error listener
      */
    def removeConsoleErrorListener(parser: Parser): Unit = {
        parser.getErrorListeners.removeIf(p => p.isInstanceOf[ConsoleErrorListener])
        ()
    }

    def getPrevTokenOnChannel(startTokenIndex: Int, tokens: TokenStream, predicate: Token => Boolean, channel: Int = Token.DEFAULT_CHANNEL): Option[Token] = {
        var i = startTokenIndex - 1
        while (i >=0) {
            val token = tokens.get(i)

            if (channel == token.getChannel && predicate(token)) {
                return Option(token)
            }
            i -= 1
        }
        None
    }

    def getNextTokenOnChannel(startTokenIndex: Int, tokens: TokenStream, predicate: Token => Boolean): Option[Token] = {
        var i = startTokenIndex + 1
        val size = tokens.size()
        while (i < size) {
            val token = tokens.get(i)

            if (Token.DEFAULT_CHANNEL == token.getChannel && predicate(token)) {
                return Option(token)
            }
            i += 1
        }
        None
    }

    def findNextTokenOnChannel(startTokenIndex: Int, tokens: TokenStream, predicate: Token => Boolean): Option[Token] = {
        var i = startTokenIndex
        val size = tokens.size()
        while (i < size) {
            val token = tokens.get(i)

            if (Token.DEFAULT_CHANNEL == token.getChannel && predicate(token)) {
                return Option(token)
            }
            i += 1
        }
        None
    }

    def getTokensFromText(text: String): TokenStream = {
        val input = CharStreams.fromString(text)
        val lexer = new ApexcodeLexer(input)
        val tokens = new CommonTokenStream(lexer)
        tokens
    }

    /**
      * recover original text (with white-space) for given ParserRuleContext
      * @return
      */
    def getTextFromContext(ctx: ParserRuleContext): String = {
        val a: Int = ctx.start.getStartIndex
        val b: Int = ctx.stop.getStopIndex
        val interval: Interval = new Interval(a, b)
        ctx.start.getInputStream.getText(interval)
    }
    /**
      *
      * @param tokens "stream" must be initialised and loaded from lexer
      * @param startTokenIndex index of start token
      * @param startPredicate start token must match given condition
      * @param endPredicate end token must match given condition
      * @return tokens between token matching start predicate and ending token *before* toking matching end predicate
      */
    def getTokensBetween(tokens: TokenStream, startTokenIndex: Int, startPredicate: Token => Boolean, endPredicate: Token => Boolean): Seq[Token] = {
        tokens.seek(startTokenIndex)
        val interval =
        findNextTokenOnChannel(startTokenIndex, tokens, startPredicate) match {
            case Some(startToken) =>
                findNextTokenOnChannel(startToken.getTokenIndex, tokens, endPredicate) match {
                    case Some(endToken) =>
                        new Interval(startToken.getTokenIndex, endToken.getTokenIndex -1)
                    case None => new Interval(startToken.getTokenIndex, tokens.size() -1)
                }
            case None => new Interval(0, 0)
        }

        val start = interval.a
        val stop = interval.b
        getTokensBetween(tokens, start, stop)

    }
    def getTokensBetween(tokens: TokenStream, startTokenIndex: Int, endTokenIndex: Int): Seq[Token] = {
        tokens.seek(startTokenIndex)
        val start = startTokenIndex
        var stop = endTokenIndex
        if (start < 0 || stop < 0) {
            Seq.empty[Token]
        } else {
            if (stop >= tokens.size) stop = tokens.size - 1

            val resultBuilder = Seq.newBuilder[Token]
            var i = start
            while ( i <= stop ) {
                val t = tokens.get(i)
                resultBuilder += t
                if (t.getType == Token.EOF) {
                    i = stop // force exit
                }
                i += 1
            }
            resultBuilder.result()
        }

    }

    /**
      * @param caret
      * @param errorHandlerOpt
      * @return ApexcodeParser based on CommonTokenStream
      */
    def createParserWithCommonTokenStream(caret: CaretInDocument,
                                          errorHandlerOpt: Option[ANTLRErrorStrategy] = Option(new BailErrorStrategy)): (ApexcodeParser, CommonTokenStream) = {
        val lexer = new ApexcodeLexer(caret.document.getCharStream)
        val tokens = new CommonTokenStream(lexer)
        val parser = new ApexcodeParser(tokens)
        // do not dump parse errors into console (or any other default listeners)
        parser.removeErrorListeners()
        errorHandlerOpt.foreach(parser.setErrorHandler)
        //parser.getInterpreter.setPredictionMode(PredictionMode.SLL)
        //parser.getInterpreter.setPredictionMode(PredictionMode.LL)
        (parser, tokens)
    }

}
