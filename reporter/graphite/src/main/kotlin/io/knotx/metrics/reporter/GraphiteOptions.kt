package io.knotx.metrics.reporter

import io.vertx.core.json.JsonObject

data class GraphiteOptions(
        val address: String = "localhost",
        val port: Int = 2003
) {
    constructor(config: JsonObject) : this(
            config.getString("address"),
            config.getInteger("port")
    )
}
