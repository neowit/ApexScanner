/*
 * Copyright (c) 2017 Andrey Gavrikov.
 * this file is part of tooling-force.com application
 * https://github.com/neowit/tooling-force.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.neowit.apexscanner.scanner.actions

import com.neowit.apexscanner.scanner.actions.ActionContext.ActionContextId

import scala.collection.mutable
import java.util.concurrent.atomic.AtomicBoolean

import com.typesafe.scalalogging.LazyLogging

/**
  * Created by Andrey Gavrikov 
  */
object ActionContext {
    type ActionContextId = String

    private val _contextById = new mutable.HashMap[ActionContextId, ActionContext]()

    /**
      * create new instance of ActionContext
      * @param contextId id of given context
      */
    def apply(contextId: ActionContextId, actionType: ActionType): ActionContext = {
        val actionContext = actionType match {
            case ListCompletionsActionType => CompletionActionContext(contextId)
            case FindSymbolActionType => FindSymbolActionContext(contextId)
            case FindUsagesActionType =>  FindUsagesActionContext(contextId)
            case _ => throw new NotImplementedError("ActionContext.apply is not implemented for " + actionType.toString)
        }
        _contextById += actionContext.id -> actionContext
        actionContext
    }

    /**
      * attempt to retrieve previously created instance of ActionContext
      * @param contextId id of context to retrieve
      * @return None if context not found or Some(ActionContext) otherwise
      */
    def get(contextId: ActionContextId): Option[ActionContext] = _contextById.get(contextId)
    /**
      * forget given action. Use it when action is done and its context can be thrown away
      * @param actionContext action to remove from cache
      */
    def forget(actionContext: ActionContext): Option[ActionContext] = {
        _contextById.remove(actionContext.id)
    }

    /**
      * cancel all contexts created before specified time
      * @param timeMillis if context.createdTimeMillis < timeMillis THEN context will be cancelled
      */
    def cancelBefore(timeMillis: Long): Unit = {
        _contextById.values.foreach{context =>
            if (context.createdTimeMillis < timeMillis){
              context.cancel()
            }
        }
    }

    def cancelAll(actionType: ActionType): Unit = {
        _contextById.values.foreach{context =>
            if (actionType == context.actionType){
                context.cancel()
            }
        }
    }
}

trait ActionContext extends LazyLogging {
    private val _isCancelled: AtomicBoolean = new AtomicBoolean(false)
    private val _isDone: AtomicBoolean = new AtomicBoolean(false)


    val id: ActionContextId
    val actionType: ActionType
    val createdTimeMillis: Long = System.currentTimeMillis()
    /**
      * signal that task is now cancelled
      * @return false if this task was already cancelled
      */
    def cancel(): Boolean = {
        logger.trace("Attempting to cancel ActionContext with Id: " + id)
        if (_isCancelled.get()) {
            logger.trace("    ActionContext with Id: " + id + " is already cancelled")
            false
        } else {
            _isCancelled.set(true)
            logger.trace("    Cancelled ActionContext with Id: " + id)
            true
        }
    }

    /**
      * @return true if this task was cancelled before it completed normally.
      */
    def isCancelled: Boolean = _isCancelled.get()

    /**
      * Returns true if this task completed.
      * @return
      */
    def isDone: Boolean = _isDone.get()

    def markDone(): Boolean = {
        logger.trace("Attempting to markDone ActionContext with Id: " + id)
        if (_isDone.get()) {
            logger.trace("  ActionContext with Id: " + id + " is already Done")
            false
        } else {
            _isDone.set(true)
            ActionContext.forget(this)
            logger.trace("    Marked as Done ActionContext with Id: " + id)
            true
        }
    }

}
case class CompletionActionContext private (id: ActionContextId) extends ActionContext {
    override val actionType: ActionType = ListCompletionsActionType
}
case class FindSymbolActionContext private (id: ActionContextId) extends ActionContext {
    override val actionType: ActionType = FindSymbolActionType
}
case class FindUsagesActionContext private (id: ActionContextId) extends ActionContext {
    override val actionType: ActionType = FindUsagesActionType
}
