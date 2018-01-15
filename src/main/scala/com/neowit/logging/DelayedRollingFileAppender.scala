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

package com.neowit.logging

import ch.qos.logback.core.rolling.RollingFileAppender

/**
  * Created by Andrey Gavrikov
  *
  * by default logback creates file when application starts, even if nothing is sent to log
  * this may cause unwanted empty files
  * DelayedRollingFileAppender allows to delay file creation until something actually needs to be written to log file
  */
class DelayedRollingFileAppender[T] extends RollingFileAppender[T]{
    private var _needToCreateFile = true


    override def start(): Unit = {
        this.started = true
    }

    override def subAppend(event: T): Unit = {
        if (_needToCreateFile) {
            super.start() // allow file creation
            _needToCreateFile = false
        }
        super.subAppend(event)
    }
}
