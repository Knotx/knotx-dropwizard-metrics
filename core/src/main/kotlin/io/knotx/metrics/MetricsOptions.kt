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

import io.vertx.core.json.JsonObject

data class MetricsOptions(
        val pollsPeriod: Long = 5000L,
        val reporter: ReporterOptions = ReporterOptions()
) {
    constructor(config: JsonObject) : this(
            config.getLong("pollsPeriod") ?: 5000L,
            ReporterOptions(config.getJsonObject("reporter"))
    )
}

data class ReporterOptions(
        val name: String = "",
        val config: JsonObject = JsonObject()
) {
    constructor(config: JsonObject) : this(
            config.getString("name"),
            config.getJsonObject("config") ?: JsonObject()
    )
}