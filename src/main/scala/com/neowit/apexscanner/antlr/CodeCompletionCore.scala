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

import com.typesafe.scalalogging.LazyLogging
import org.antlr.v4.runtime._
import org.antlr.v4.runtime.atn._
import org.antlr.v4.runtime.misc.IntervalSet

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
  * translated to scala syntax by Andrey Gavrikov: https://github.com/neowit
  */

object CodeCompletionCore {
    type TokenList = ArrayBuffer[number]
    type RuleList = ArrayBuffer[number]

    // All the candidates which have been found. Tokens and rules are separated (both use a numeric value).
    // Token entries include a list of tokens that directly follow them (see also the "following" member in the FollowSetWithPath class).
    class CandidatesCollection {
        val tokens: mutable.HashMap[number, TokenList] = new mutable.HashMap[number, TokenList]()
        val rules: mutable.HashMap[number, RuleList] = new mutable.HashMap[number, RuleList]()
    }

    // A record for a follow set along with the path at which this set was found.
    // If there is only a single symbol in the interval set then we also collect and store tokens which follow
    // this symbol directly in its rule (i.e. there is no intermediate rule transition). Only single label transitions
    // are considered. This is useful if you have a chain of tokens which can be suggested as a whole, because there is
    // a fixed sequence in the grammar.
    class FollowSetWithPath {
        var intervals: IntervalSet = new IntervalSet()
        var path: RuleList = ArrayBuffer.empty
        var following: TokenList = ArrayBuffer.empty
    }

    // A list of follow sets (for a given state number) + all of them combined for quick hit tests.
    // This data is static in nature (because the used ATN states are part of a static struct: the ATN).
    // Hence it can be shared between all C3 instances, however it depends on the actual parser class (type).
    class FollowSetsHolder {
        var sets: ArrayBuffer[FollowSetWithPath] = ArrayBuffer.empty
        var combined: IntervalSet = new IntervalSet()
    }
    type FollowSetsPerState = mutable.HashMap[number, FollowSetsHolder]
    // Token stream position info after a rule was processed.
    type RuleEndStatus = mutable.HashSet[number]

    type number = Int

    case class PipelineEntry( state: ATNState, tokenIndex:Int )

    private val followSetsByATN: mutable.HashMap[String, FollowSetsPerState] = new mutable.HashMap()
}

// The main class for doing the collection process.
class CodeCompletionCore(parser: Parser) extends LazyLogging {
    import CodeCompletionCore._

    // Debugging options. Print human readable ATN state and other info.
    var showResult: Boolean = false;                 // Not dependent on showDebugOutput. Prints the collected rules + tokens to terminal.
    var showDebugOutput: Boolean = false;            // Enables printing ATN state info to terminal.
    var debugOutputWithTransitions: Boolean = false; // Only relevant when showDebugOutput is true. Enables transition printing for a state.
    var showRuleStack: Boolean = false;              // Also depends on showDebugOutput. Enables call stack printing for each rule recursion.

    // Tailoring of the result.
    var ignoredTokens: Set[number] = Set.empty
    var preferredRules: Set[number] = Set.empty      // Rules which replace any candidate token they contain.
    // This allows to return descriptive rules (e.g. className, instead of ID/identifier).


    //private var parser: Option[Parser] = None
    private val atn: ATN = parser.getATN
    private val vocabulary: Vocabulary = parser.getVocabulary
    private val ruleNames: Array[String] = parser.getRuleNames
    private val tokens: TokenList = ArrayBuffer.empty

    private var tokenStartIndex: number = 0

    private var statesProcessed: number = 0
    // A mapping of rule index + token stream position to end token positions.
    // A rule which has been visited before with the same input position will always produce the same output positions.
    private val shortcutMap: mutable.HashMap[number, mutable.HashMap[number, RuleEndStatus]] = new mutable.HashMap()
    private val candidates: CandidatesCollection = new CandidatesCollection() // The collected candidates (rules and tokens).

    /**
      * This is the main entry point. The caret token index specifies the token stream index for the token which currently
      * covers the caret (or any other position you want to get code completion candidates for).
      * Optionally you can pass in a parser rule context which limits the ATN walk to only that or called rules. This can significantly
      * speed up the retrieval process but might miss some candidates (if they are outside of the given context).
      */
    def collectCandidates(caretTokenIndex: number, context: Option[ParserRuleContext] = None): CandidatesCollection = {
        this.shortcutMap.clear()
        this.candidates.rules.clear()
        this.candidates.tokens.clear()
        this.statesProcessed = 0

        this.tokenStartIndex = context.map(_.start.getTokenIndex).getOrElse(0)
        val tokenStream: TokenStream = this.parser.getInputStream

        val currentIndex = tokenStream.index
        tokenStream.seek(this.tokenStartIndex)
        this.tokens.clear()
        var offset = 1
        var stop = false
        while (!stop) {
            val token = tokenStream.LT(offset)
            offset += 1
            this.tokens += token.getType

            stop = token.getTokenIndex >= caretTokenIndex || token.getType == Token.EOF

        }
        tokenStream.seek(currentIndex)

        val callStack: ArrayBuffer[number] = ArrayBuffer.empty
        //var startRule = context ? context.ruleIndex : 0;
        val startRule = context.map(_.getRuleIndex).getOrElse(0)
        this.processRule(this.atn.ruleToStartState(startRule), 0, callStack, new StringBuffer())

        if (this.showResult) {
            logger.debug("States processed: " + this.statesProcessed)
        }


        if (this.showResult) {
            logger.debug("\n\nCollected rules:\n")

            for (rule <- this.candidates.rules) {
                var  path = ""
                for (token <- rule._2) {
                    path += this.ruleNames(token) + " "
                }
                logger.debug(this.ruleNames(rule._1) + ", path: " + path)
            }

            val sortedTokens: mutable.HashSet[String] = mutable.HashSet.empty
            for (token <- this.candidates.tokens) {
                var value: String = this.vocabulary.getDisplayName(token._1)
                for (following <- token._2)
                    value += " " + this.vocabulary.getDisplayName(following)
                sortedTokens.add(value)
            }

            logger.debug("\n\nCollected tokens:\n")
            for (symbol <- sortedTokens) {
                logger.debug(symbol)
            }
            logger.debug("\n\n")
        }

        this.candidates
    }

    /**
      * Check if the predicate associated with the given transition evaluates to true.
      */
    private def checkPredicate(transition: PredicateTransition): Boolean =  {
        transition.getPredicate.eval(this.parser, new ParserRuleContext())
    }

    /**
      * Walks the rule chain upwards to see if that matches any of the preferred rules.
      * If found, that rule is added to the collection candidates and true is returned.
      */
    private def translateToRuleIndex(ruleStack: RuleList): Boolean = {
        if (this.preferredRules.isEmpty) {
            return false

        }

        // Loop over the rule stack from highest to lowest rule level. This way we properly handle the higher rule
        // if it contains a lower one that is also a preferred rule.
        //for (let i = 0; i < ruleStack.length; ++i) {
        for (i <- ruleStack.indices) {
            if (this.preferredRules.contains(ruleStack(i))) {
                // Add the rule to our candidates list along with the current rule path,
                // but only if there isn't already an entry like that.
                //var path = ruleStack.slice(0, i);
                val path = ruleStack.take(i)
                var addNew = true
                var keepGoing = true
                //for (let rule of this.candidates.rules.keys) {
                for (rule <- this.candidates.rules) {
                    if (keepGoing) {
                        if (rule._1 != ruleStack(i) || rule._2.length != path.length) {
                            //continue;
                        } else {
                            // Found an entry for this rule. Same path? If so don't add a new (duplicate) entry.
                            //if (path.every((v, j) => v === rule[ 1 ][j ] ) ) {
                            if (path == rule._2) {
                                addNew = false
                                keepGoing = false
                            }
                        }
                    }
                }

                if (addNew) {
                    this.candidates.rules += ruleStack(i) -> path
                    if (this.showDebugOutput)
                    logger.debug("=====> collected: ", this.ruleNames(i))
                    }
                return true
            }
        }

        false
    }


    /**
      * This method follows the given transition and collects all symbols within the same rule that directly follow it
      * without intermediate transitions to other rules and only if there is a single symbol for a transition.
      */
    private def getFollowingTokens(transition: Transition): ArrayBuffer[number] = {
        val result: ArrayBuffer[number] = ArrayBuffer.empty

        //var seen: Array[ATNState] = Array.empty
        val pipeline: ArrayBuffer[ATNState] = ArrayBuffer(transition.target)

        while (pipeline.nonEmpty) {
            val state = pipeline.last
            //let state = pipeline.pop();
            if (pipeline.nonEmpty) {
                pipeline.remove(pipeline.size - 1)
            }

            //for (let transition of state!.getTransitions()) {
            for (transition <- state.getTransitions ) {
                if (transition.getSerializationType == Transition.ATOM) {
                    if (!transition.isEpsilon) {
                        val list = transition.label.toList
                        if (list.size() == 1 && !this.ignoredTokens.contains(list.get(0) )) {
                            //result.push(list[0]);
                            //pipeline.push(transition.target);
                            result += list.get(0)
                            pipeline += transition.target
                        }
                    } else {
                        //pipeline.push(transition.target);
                        pipeline += transition.target
                    }
                }
            }
        }

        result
    }


    /**
      * Entry point for the recursive follow set collection function.
      */
    private def determineFollowSets(start: ATNState, stop: ATNState): ArrayBuffer[FollowSetWithPath] = {
        val result: ArrayBuffer[FollowSetWithPath] = ArrayBuffer.empty
        val seen: mutable.HashSet[ATNState] = mutable.HashSet.empty
        val ruleStack: ArrayBuffer[number] = ArrayBuffer.empty

        this.collectFollowSets(start, stop, followSets = result, seen = seen, ruleStack = ruleStack)

        result
    }

    /**
      * Collects possible tokens which could be matched following the given ATN state. This is essentially the same
      * algorithm as used in the LL1Analyzer class, but here we consider predicates also and use no parser rule context.
      */
    private def collectFollowSets(s: ATNState, stopState: ATNState, followSets: ArrayBuffer[FollowSetWithPath],
                                  seen: mutable.HashSet[ATNState], ruleStack: ArrayBuffer[number]): Unit = {

        if (seen.contains(s))
            return

        seen += s

        if (s == stopState || s.getStateType == ATNState.RULE_STOP) {
            val set = new FollowSetWithPath()
            set.intervals = IntervalSet.of(Token.EPSILON)
            //set.path = ruleStack.slice();
            set.path = ruleStack.clone()
            //followSets.push(set);
            followSets += set
            return
        }
        for ( transition <- s.getTransitions) {
            if (transition.getSerializationType == Transition.RULE) {
                val ruleTransition: RuleTransition = transition.asInstanceOf[RuleTransition]
                if (ruleStack.indexOf(ruleTransition.target.ruleIndex) != -1) {
                    //continue;
                } else {
                    //ruleStack.push(ruleTransition.target.ruleIndex);
                    ruleStack += ruleTransition.target.ruleIndex
                    this.collectFollowSets(transition.target, stopState, followSets, seen, ruleStack)
                    //ruleStack.pop();
                    if (ruleStack.nonEmpty) {
                        ruleStack.remove(ruleStack.size -1)
                    }

                }


            } else if (transition.getSerializationType == Transition.PREDICATE) {
                if (this.checkPredicate(transition.asInstanceOf[PredicateTransition]))
                    this.collectFollowSets(transition.target, stopState, followSets, seen, ruleStack)
            } else if (transition.isEpsilon) {
                this.collectFollowSets(transition.target, stopState, followSets, seen, ruleStack)
            } else if (transition.getSerializationType == Transition.WILDCARD) {
                val set = new FollowSetWithPath()
                set.intervals = IntervalSet.of(Token.MIN_USER_TOKEN_TYPE, this.atn.maxTokenType)
                //set.path = ruleStack.slice();
                set.path = ruleStack.clone()
                //followSets.push(set);
                followSets += set
            } else {
                var label = transition.label
                if (null != label && label.size > 0) {
                    if (transition.getSerializationType == Transition.NOT_SET) {
                        label = label.complement(IntervalSet.of(Token.MIN_USER_TOKEN_TYPE, this.atn.maxTokenType))
                    }
                    val set = new FollowSetWithPath()
                    set.intervals = label
                    //set.path = ruleStack.slice();
                    set.path = ruleStack.clone()
                    set.following = this.getFollowingTokens(transition)
                    //followSets.push(set);
                    followSets += set
                }
            }
        }

    }


    /**
      * Walks the ATN for a single rule only. It returns the token stream position for each path that could be matched in this rule.
      * The result can be empty in case we hit only non-epsilon transitions that didn't match the current input or if we
      * hit the caret position.
      */
    private def processRule(startState: ATNState, tokenIndex: number, callStack: ArrayBuffer[number], indentation: StringBuffer): RuleEndStatus = {

        // Start with rule specific handling before going into the ATN walk.

        // Check first if we've taken this path with the same input before.
        val positionMap = new mutable.HashMap[number, RuleEndStatus]()
        this.shortcutMap.get(startState.ruleIndex) match {
            case Some(_positionMap) =>
                if (_positionMap.contains(tokenIndex)) {
                    if (this.showDebugOutput) {
                        logger.debug("=====> shortcut")
                    }
                    return _positionMap(tokenIndex)
                }

            case None =>
                this.shortcutMap += startState.ruleIndex -> positionMap;
        }

        val result: RuleEndStatus = mutable.HashSet.empty
        // For rule start states we determine and cache the follow set, which gives us 3 advantages:
        // 1) We can quickly check if a symbol would be matched when we follow that rule. We can so check in advance
        //    and can save us all the intermediate steps if there is no match.
        // 2) We'll have all symbols that are collectable already together when we are at the caret when entering a rule.
        // 3) We get this lookup for free with any 2nd or further visit of the same rule, which often happens
        //    in non trivial grammars, especially with (recursive) expressions and of course when invoking code completion
        //    multiple times.
        //var setsPerState = CodeCompletionCore.followSetsByATN.get(this.parser.constructor.name);
        //TODO check if this.parser.constructor.name is correctly translated
        var setsPerState = CodeCompletionCore.followSetsByATN.getOrElse(this.parser.getClass.getName, null)
        if (null == setsPerState) {
            setsPerState = new FollowSetsPerState()
            //CodeCompletionCore.followSetsByATN.set(this.parser.constructor.name, setsPerState);
            //TODO check if this.parser.constructor.name is correctly translated
            CodeCompletionCore.followSetsByATN += (this.parser.getClass.getName -> setsPerState)
        }

        var followSets = setsPerState.getOrElse(startState.stateNumber, null)
        if (null == followSets) {
            followSets = new FollowSetsHolder()
            setsPerState += (startState.stateNumber -> followSets)
            val stop = this.atn.ruleToStopState(startState.ruleIndex)
            followSets.sets = this.determineFollowSets(startState, stop)

            // Sets are split by path to allow translating them to preferred rules. But for quick hit tests
            // it is also useful to have a set with all symbols combined.
            val combined = new IntervalSet()
            for (set <- followSets.sets) {
                //combined.addAll(set.intervals)
                combined.addAll(set.intervals)
            }
            followSets.combined = combined
        }

        //callStack.push(startState.ruleIndex);
        callStack += startState.ruleIndex

        var currentSymbol = this.tokens(tokenIndex)

        if (tokenIndex >= this.tokens.length - 1) { // At caret?
            if (this.preferredRules.contains(startState.ruleIndex)) {
                // No need to go deeper when collecting entries and we reach a rule that we want to collect anyway.
                this.translateToRuleIndex(callStack)
            } else {
                // Convert all follow sets to either single symbols or their associated preferred rule and add
                // the result to our candidates list.
                for (set <- followSets.sets) {
                    //let fullPath = callStack.slice();
                    val fullPath = callStack.clone()
                    //fullPath.push(...set.path);
                    fullPath ++= set.path
                    if (!this.translateToRuleIndex(fullPath)) {
                        for (symbol <- set.intervals.toArray) {
                            if (!this.ignoredTokens.contains(symbol)) {
                                if (this.showDebugOutput){
                                    logger.debug("=====> collected: ", this.vocabulary.getDisplayName(symbol))
                                }
                                if (!this.candidates.tokens.contains(symbol))
                                    this.candidates.tokens += (symbol -> set.following) // Following is empty if there is more than one entry in the set.
                                else {
                                    // More than one following list for the same symbol.
                                    //if (this.candidates.tokens.get(symbol) != set.following)
                                    if (this.candidates.tokens(symbol) != set.following)
                                        this.candidates.tokens += (symbol ->  ArrayBuffer.empty)
                                }
                            }
                        }
                    }
                }
            }

            //callStack.pop();
            if (callStack.nonEmpty) {
                callStack.remove(callStack.size - 1)
            }
            return result

        } else {
            // Process the rule if we either could pass it without consuming anything (epsilon transition)
            // or if the current input symbol will be matched somewhere after this entry point.
            // Otherwise stop here.
            if (!followSets.combined.contains(Token.EPSILON) && !followSets.combined.contains(currentSymbol)) {
                //callStack.pop();
                if (callStack.nonEmpty) {
                    callStack.remove(callStack.size - 1)
                }
                return result
            }
        }

        // The current state execution pipeline contains all yet-to-be-processed ATN states in this rule.
        // For each such state we store the token index + a list of rules that lead to it.
        val statePipeline: ArrayBuffer[PipelineEntry] = ArrayBuffer.empty
        var currentEntry: PipelineEntry = null

        // Bootstrap the pipeline.
        //statePipeline.push({ state: startState, tokenIndex: tokenIndex });
        statePipeline += PipelineEntry( state = startState, tokenIndex= tokenIndex)

        var skipToNextLoop = false
        //while (statePipeline.length > 0) {
        while (statePipeline.nonEmpty) {
            //currentEntry = statePipeline.pop()!;
            currentEntry = statePipeline.remove(statePipeline.size -1)
            this.statesProcessed += 1

            currentSymbol = this.tokens(currentEntry.tokenIndex)

            val atCaret = currentEntry.tokenIndex >= this.tokens.length - 1
            if (this.showDebugOutput) {
                this.printDescription(indentation.toString, currentEntry.state, this.generateBaseDescription(currentEntry.state), currentEntry.tokenIndex)
                if (this.showRuleStack) {
                    this.printRuleState(callStack.toArray)
                }
            }

            currentEntry.state.getStateType match {
                case ATNState.RULE_START => // Happens only for the first state in this rule, not subrules.
                    indentation.append("  ")
                    //break;

                // Found the end of this rule. Determine the following states and return to the caller.
                case ATNState.RULE_STOP =>
                    // Record the token index we are at, to report it to the caller.
                    result.add(currentEntry.tokenIndex)
                    //continue;
                    skipToNextLoop = true

                case _ =>
                //default:
                //    break;
            }
            if (!skipToNextLoop) {
                val transitions = currentEntry.state.getTransitions
                //for (let i = transitions.length - 1; i >= 0; --i) {
                var skipToNextTransitionsLoop = false
                for ( transition <- transitions ) {
                    transition.getSerializationType match {
                        case Transition.RULE =>
                            val endStatus = this.processRule(transition.target, currentEntry.tokenIndex, callStack, indentation)
                            for (position <- endStatus) {
                                //statePipeline.push({ state: (<RuleTransition>transition).followState, tokenIndex: position });
                                statePipeline += PipelineEntry(state = transition.asInstanceOf[RuleTransition].followState, tokenIndex = position)
                            }

                        case Transition.PREDICATE =>
                            if (this.checkPredicate(transition.asInstanceOf[PredicateTransition])) {
                                //statePipeline.push({ state: transition.target, tokenIndex: currentEntry.tokenIndex });
                                statePipeline += PipelineEntry(state = transition.target, tokenIndex = currentEntry.tokenIndex)
                            }
                        case Transition.WILDCARD =>
                            if (atCaret) {
                                if (!this.translateToRuleIndex(callStack)) {
                                    for (token <- IntervalSet.of(Token.MIN_USER_TOKEN_TYPE, this.atn.maxTokenType).toArray ) {
                                        if (!this.ignoredTokens.contains(token))
                                            this.candidates.tokens += (token -> ArrayBuffer.empty[number])
                                    }
                                }
                            } else {
                                //statePipeline.push({ state: transition.target, tokenIndex: currentEntry.tokenIndex + 1 });
                                statePipeline += PipelineEntry( state = transition.target, tokenIndex= currentEntry.tokenIndex + 1)
                            }
                        case _ =>
                            if (transition.isEpsilon) {
                                // Jump over simple states with a single outgoing epsilon transition.
                                //statePipeline.push({ state: transition.target, tokenIndex: currentEntry.tokenIndex });
                                statePipeline += PipelineEntry( state = transition.target, tokenIndex= currentEntry.tokenIndex)
                                //continue;
                                skipToNextTransitionsLoop = true
                            }
                    }
                    if (!skipToNextTransitionsLoop) {
                        var set = transition.label
                        if (null != set && set.size > 0) {
                            if (transition.getSerializationType == Transition.NOT_SET) {
                                set = set.complement(IntervalSet.of(Token.MIN_USER_TOKEN_TYPE, this.atn.maxTokenType))
                            }
                            if (atCaret) {
                                if (!this.translateToRuleIndex(callStack)) {
                                    val list = set.toArray
                                    val addFollowing = list.size == 1
                                    for (symbol <- list)
                                        if (!this.ignoredTokens.contains(symbol)) {
                                            if (this.showDebugOutput)
                                                logger.debug("=====> collected: ", this.vocabulary.getDisplayName(symbol))

                                            if (addFollowing)
                                                this.candidates.tokens += (symbol -> this.getFollowingTokens(transition))
                                            else
                                                this.candidates.tokens += (symbol -> ArrayBuffer.empty[number])
                                        }
                                }
                            } else {
                                if (set.contains(currentSymbol)) {
                                    if (this.showDebugOutput) {
                                        logger.debug("=====> consumed: " + this.vocabulary.getDisplayName(currentSymbol))
                                    }

                                    //statePipeline.push({ state: transition.target, tokenIndex: currentEntry.tokenIndex + 1 });
                                    statePipeline += PipelineEntry(state = transition.target, tokenIndex = currentEntry.tokenIndex + 1)
                                }
                            }
                        }
                    }
                }

            }
        }

        //callStack.pop();
        if (callStack.nonEmpty) {
            callStack.remove(callStack.size - 1)
        }
        // Cache the result, for later lookup to avoid duplicate walks.
        positionMap += (tokenIndex -> result)

        result
    }

    private val atnStateTypeMap: Array[String] = Array[String] (
    "invalid",
    "basic",
    "rule start",
    "block start",
    "plus block start",
    "star block start",
    "token start",
    "rule stop",
    "block end",
    "star loop back",
    "star loop entry",
    "plus loop back",
    "loop end"
    )

    private def generateBaseDescription(state: ATNState): String = {
        val stateValue = if (state.stateNumber == ATNState.INVALID_STATE_NUMBER) "Invalid" else state.stateNumber

        "[" + stateValue + " " + atnStateTypeMap(state.getStateType) + "] in " + this.ruleNames(state.ruleIndex)
    }

    private def printDescription(currentIndent: String, state: ATNState, baseDescription: String, tokenIndex: number): Unit = {
        var output = currentIndent

        var transitionDescription = ""
        if (this.debugOutputWithTransitions) {
            for (transition <- state.getTransitions) {
                var labels = ""
                val symbols: Array[number] = if (null != transition.label) transition.label.toArray else Array.empty[number]
                if (symbols.length > 2) {
                    // Only print start and end symbols to avoid large lists in debug output.
                    labels = this.vocabulary.getDisplayName(symbols(0)) + " .. " + this.vocabulary.getDisplayName(symbols(symbols.length - 1))
                } else {
                    for (symbol <- symbols) {
                        if (labels.length > 0)
                            labels += ", "
                        labels += this.vocabulary.getDisplayName(symbol)
                    }
                }
                if (labels.length == 0)
                    labels = "ε"

                transitionDescription += "\n" + currentIndent + "\t(" + labels + ") " + "[" + transition.target.stateNumber + " " +
                    //ATNStateType[transition.target.stateType] + "] in " + this.ruleNames[transition.target.ruleIndex];
                    atnStateTypeMap(transition.target.getStateType) + "] in " + this.ruleNames(transition.target.ruleIndex)
            }
        }

        if (tokenIndex >= this.tokens.length - 1)
            output += "<<" + this.tokenStartIndex + tokenIndex + ">> "
        else
            output += "<" + this.tokenStartIndex + tokenIndex + "> "

        logger.debug(output + "Current state: " + baseDescription + transitionDescription)
    }
    private def printRuleState(stack: Array[number]): Unit = {
        if (stack.length == 0) {
            logger.debug("<empty stack>")
            return
        }

        for (rule <- stack)
            logger.debug(this.ruleNames(rule))
    }
}