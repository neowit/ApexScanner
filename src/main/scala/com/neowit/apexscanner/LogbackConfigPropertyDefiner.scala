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

package com.neowit.apexscanner

import ch.qos.logback.classic.spi.LoggingEvent
import ch.qos.logback.core.PropertyDefinerBase
import ch.qos.logback.core.spi.FilterReply

/**
  * Created by Andrey Gavrikov
  *
  * log configuration
  * -Dlog.path=/path/to/file.log
  * -Dlog.level.root=error    #log all events of level INFO
  * -Dlog.level.neowit=debug  #log com.neowit.* events of level DEBUG
  * -Dlog.stdout.enable=true # enable log to STDOUT
  */
trait LogbackConfigPropertyDefiner extends PropertyDefinerBase {
    val loggerName: String

    /**
      * get value of $loggerName from application.conf
      * @return
      */
    override def getPropertyValue: String = {
        System.getProperty( loggerName )
    }
}
/**
  * this is used to allow Logback to read one of its properties from System.properties
  * see logback.xml < define ... >
  */
class LogbackConfigPropertyDefinerRoot extends LogbackConfigPropertyDefiner {
    override val loggerName: String = "log.level.root"
}

/**
  * log level for com.neowit classes
  */
class LogbackConfigPropertyDefinerNeowit extends LogbackConfigPropertyDefiner {
    override val loggerName: String = "log.level.neowit"
}

class LogbackStdOutFilter extends ch.qos.logback.core.filter.Filter[LoggingEvent] {
    private val enabled = "true" == System.getProperty("log.stdout.enable")
    override def decide(event: LoggingEvent): FilterReply = {
        if (enabled) {
            FilterReply.ACCEPT
        }
        else {
            FilterReply.DENY
        }
    }
}
