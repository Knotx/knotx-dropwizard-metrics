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
package io.knotx.metrics

import com.codahale.metrics.ScheduledReporter
import com.codahale.metrics.SharedMetricRegistries
import io.knotx.metrics.reporter.DropwizardMetricsReporterFactory
import io.vertx.core.AbstractVerticle
import io.vertx.core.Context
import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
import java.util.ServiceLoader
import java.util.concurrent.TimeUnit

class DropwizardMetricsVerticle() : AbstractVerticle() {

    private var reporter: ScheduledReporter? = null
    private var options: MetricsOptions = MetricsOptions()

    override fun init(vertx: Vertx?, context: Context?) {
        super.init(vertx, context)
        options = MetricsOptions(config())
    }

    override fun start(startFuture: Future<Void>) {
        LOGGER.info("Starting <${this::class.java.simpleName}>")

        val registryName: String? = System.getProperty(REGISTRY_PROPERTY)
        if (registryName == null) {
            LOGGER.info("Property '$REGISTRY_PROPERTY' not set. The default registry '$DEFAULLT_REGISTRY_NAME' will be used.")
        }

        val dropwizardRegistry = SharedMetricRegistries.getOrCreate(registryName ?: DEFAULLT_REGISTRY_NAME)

        val factory = ServiceLoader.load(DropwizardMetricsReporterFactory::class.java)
                .firstOrNull { f -> options.reporter.name == f.name }

        if (factory != null) {
            reporter = factory.create(dropwizardRegistry, options.reporter.config)
            reporter!!.start(options.pollsPeriod, TimeUnit.MILLISECONDS)
        } else {
            throw RuntimeException("Reporter factory for '${options.reporter.name}' not found!")
        }
    }

    override fun stop() {
        reporter?.stop()
    }

    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(DropwizardMetricsVerticle::class.java)
        const val REGISTRY_PROPERTY = "vertx.metrics.options.registryName"
        const val DEFAULLT_REGISTRY_NAME = "knotx-dropwizard-registry"
    }

}
