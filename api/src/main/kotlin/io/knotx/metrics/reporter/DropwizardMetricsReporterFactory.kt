/*
 * Copyright (C) 2019 Knot.x Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.knotx.metrics.reporter

import com.codahale.metrics.MetricRegistry
import com.codahale.metrics.ScheduledReporter
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
import java.util.concurrent.TimeUnit

interface DropwizardMetricsReporterFactory {
    /**
     * The reporter name that is used in operations configuration.
     * @return the reporter name
     */
    fun getName(): String?

    /**
     * Creates metrics reporter instance.
     * @param registry reporter registry
     * @param config reporter configuration
     * @return reporter instance
     */
    fun create(registry: MetricRegistry, config: JsonObject): ScheduledReporter

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(DropwizardMetricsReporterFactory::class.java)
    }

    fun JsonObject.getTimeUnit(key: String): TimeUnit? {
        return getString(key)?.let {
            try {
                return TimeUnit.valueOf(it.toUpperCase())
            } catch (e: Exception) {
                LOGGER.error("Problem with parsing value '$it' to TimeUnit class")
            }
            return null
        }
    }
}