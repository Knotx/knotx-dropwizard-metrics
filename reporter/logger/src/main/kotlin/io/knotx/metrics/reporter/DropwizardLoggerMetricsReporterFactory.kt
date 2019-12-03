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
import com.codahale.metrics.Slf4jReporter
import io.knotx.metrics.getTimeUnit
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory

class DropwizardLoggerMetricsReporterFactory : DropwizardMetricsReporterFactory {
    override val name = "logger"

    override fun create(registry: MetricRegistry, config: JsonObject): ScheduledReporter {
        LOGGER.info("Creating <$name> dropwizard metrics reporter")

        return Slf4jReporter.forRegistry(registry).apply {
                    config.getTimeUnit("rateUnit")?.let { convertRatesTo(it) }
                    config.getTimeUnit("durationUnit")?.let { convertDurationsTo(it) }
                    config.getLogLevel("loggingLevel")?.let { withLoggingLevel(it) }
                }
                .build()
    }

    fun JsonObject.getLogLevel(key: String)= getString(key)?.let {
            try {
                Slf4jReporter.LoggingLevel.valueOf(it.toUpperCase())
            } catch (e: Exception) {
                LOGGER.error("Problem with parsing value '$it'")
                null
            }
        }

    companion object Options {
        val LOGGER: Logger = LoggerFactory.getLogger(DropwizardLoggerMetricsReporterFactory::class.java)
    }

}