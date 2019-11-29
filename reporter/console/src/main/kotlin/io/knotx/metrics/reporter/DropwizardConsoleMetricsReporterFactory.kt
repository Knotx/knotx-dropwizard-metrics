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

import com.codahale.metrics.ConsoleReporter
import com.codahale.metrics.MetricRegistry
import com.codahale.metrics.ScheduledReporter
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory

class DropwizardConsoleMetricsReporterFactory : DropwizardMetricsReporterFactory {
    override fun getName(): String = "console"

    override fun create(registry: MetricRegistry, config: JsonObject): ScheduledReporter {
        LOGGER.info("Creating Reporter factory for <{}>", getName())

        val reporter = ConsoleReporter.forRegistry(registry)
        reporter.buildWith(config.getTimeUnit("rateUnit"), { v -> reporter.convertRatesTo(v) })
                .buildWith(config.getTimeUnit("durationUnit"), { v -> reporter.convertDurationsTo(v) })

        return reporter.build()
    }

    private fun <T> ConsoleReporter.Builder.buildWith(e: T?, build: (e: T) -> Unit): ConsoleReporter.Builder {
        e?.let(build)
        return this
    }

    companion object Options {

        val LOGGER: Logger = LoggerFactory.getLogger(DropwizardConsoleMetricsReporterFactory::class.java)
    }

}