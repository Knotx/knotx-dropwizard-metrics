# Knot.x Dropwizard Metrics
This simple Knot.x module sends [Vert.x Dropwizard metrics](https://github.com/vert-x3/vertx-dropwizard-metrics/blob/master/src/main/asciidoc/java/index.adoc) 
gathered by already registered Dropwizard Registry to the chosen [Reporter](#reporters).

## How does it work
This module uses the [Vert.x Dropwizard Metrics](https://vertx.io/docs/vertx-dropwizard-metrics/java)
that implements the Vert.x Metrics Service Provider Interface (SPI) reporting metrics to the 
[Dropwizard metrics](https://github.com/dropwizard/metrics) library.
The concept base on connecting to already existing Dropwizard Registry that collects the metrics.
The registry is identified by the system property `vertx.metrics.options.registryName`. If the property
is not set, the default name `knotx-dropwizard-registry` will be used.
In the module [configuration](#configuration) a [Reporter](#reporters) can be configured to define
metrics consumer endpoint.

For any details on metrics and Vert.x please refer to this article:
- https://vertx.io/docs/vertx-dropwizard-metrics/java/

## How to use
> Note: Knot.x Dropwizard Metrics does not have any runtime dependency on Knot.x. 
> That means this module can be used on any Vert.x instance (including Knot.x of course :) ). 
> However, project depends on the [`knotx-dependencies`](https://github.com/Knotx/knotx-dependencies) 
> that defines Vert.x version used in this project.

### Prerequisites
In order to use Knot.x Dropwizard Metrics in your Vert.x instance you need to have Vert.x metrics enabled
and Dropwizard Registry created. The easiest way to do it is by passing the following commandline parameters
when running an instance:
```shell script
-Dvertx.metrics.options.enabled=true -Dvertx.metrics.options.registryName=my-knotx-registry
```

> Note: If you are using Knot.x instance, the only thing you need to do is to uncomment `METRICS_OPTS` line in `bin/knotx`:

```shell script
METRICS_OPTS="-Dvertx.metrics.options.enabled=true -Dvertx.metrics.options.registryName=knotx-dropwizard-registry"
```

### Configuration
Two things needs to be configured in order to setup module properly:
- `pollsPeriod` - how often (milliseconds) should metrics be sent
- `reporter` - that defines a `name` of the reporter that factory 
(registered via Service Provider Interface (SPI)) provides and `config` that is passed to the reporter.
See available [reporters implementations](#reporters) for more details on available configuration options. 

Sample configuration would look like this:
```hocon
{
    pollsPeriod: 3000
    reporter {
      name: "console"
      config {
        # any config for the Console Reporter
      }
    }
}
```

### Running
Knot.x Dropwizard Metrics Core contains a verticle `io.knotx.metrics.DropwizardMetricsVerticle`.
It should be deployed on the running Vert.x instance together with its [configuration](#configuration).

If you are using Knot.x follow these steps:
1. Update the [configration stores](https://github.com/Knotx/knotx-launcher#configuration-stores)
(usually `conf/bootstrap.json`) with new store for the this module:
```json
 ...
    "stores": [
       ...
    
      {
        "type": "file",
        "format": "conf",
        "config": {
          "path": "${KNOTX_HOME}/conf/dropwizardMetrics.conf"
        }
      }
    ]
 ...
```
2. Create `/conf/dropwizardMetrics.conf` file, define [a new module](https://github.com/Knotx/knotx-launcher#modules-configuration)
 for Knot.x Dropwizard Metrics and pass the configuration to it:
```hocon
modules {
  metrics = "io.knotx.metrics.DropwizardMetricsVerticle"
}

config.metrics {
  options.config {
    # module configuration here
  }
}
```
3. Add the proper dependencies for the metrics core and reporter that will be used to your Knot.x stack:

```kotlin
dependencies {
    // ...
    "dist"("io.knotx:knotx-dropwizard-core:version")
    "dist"("io.knotx:knotx-dropwizard-reporter-xxxxx:version")
}
```
4. Make sure you uncommented `METRICS_OPTS` line in `bin/knotx`.
> Note: If your project was created using [Knot.x Starter Kit](https://github.com/Knotx/knotx-starter-kit)
> you will have to copy `bin/knotx` file to your project (to `knotx/conf/bin/knotx`) to overwrite
> the default `bin/knotx` form the Knot.x Stack / Knot.x Docker image. Don't forget to set proper
> chmod after copying it.

## Dropwizard metrics tuning
There is great number of Vert.x metrics gathered by Dropwizard:
- https://vertx.io/docs/vertx-dropwizard-metrics/java/#_the_metrics

Additionally some metrics can be tracked when configured.

### Metrics Options
Additional options may be passed to the Dropwizard using [Vert.x Dropwizard Options](https://vertx.io/docs/apidocs/io/vertx/ext/dropwizard/DropwizardMetricsOptions.html)
via JSON configuration file.
In order to do that:
- create a `metrics-options.json` file
- pass additional parameter when starting instance `-Dvertx.metrics.options.configPath=path-to/metrics-options.json`

> Note: The easiest way to configure these options with Knot.x is to create the configuration file in the `conf` 
> directory, so that the path would be `-Dvertx.metrics.options.configPath=conf/metrics-options.json`.

Example `metrics-options.json` can look like this:

```json
{
  "enabled": true,
  "monitoredEventBusHandlers": [],
  "monitoredHttpServerUris": [],
  "monitoredHttpClientUris": [],
  "monitoredHttpClientEndpoints": [],
  "baseName": "knotx"
}
```

#### Monitoring EventBus handlers
The list of [`<Matches>`](https://vertx.io/docs/apidocs/io/vertx/ext/dropwizard/Match.html) for monitored event bus handlers, e.g.
```json
{
  "alias": "HandlebarsAction",
  "value": "knotx.knot.te.handlebars"
}
```

#### Monitoring Http Server uris
The list of [`<Matches>`](https://vertx.io/docs/apidocs/io/vertx/ext/dropwizard/Match.html) for monitored http server uris, e.g.
```json
{
  "alias": "api-v1",
  "type": "REGEX",
  "value": ".*/api/v1/.*"
}
```

#### Monitoring Http Cilent uris
The list of [`<Matches>`](https://vertx.io/docs/apidocs/io/vertx/ext/dropwizard/Match.html) for monitored http client uris
```json
{
  "alias": "Web API user",
  "type": "REGEX",
  "value": ".*/user.*"
}
```

#### Monitoring Http Cilent endpoints
The list of [`<Matches>`](https://vertx.io/docs/apidocs/io/vertx/ext/dropwizard/Match.html) for monitored http client endpoints
```json
{
    "alias": "googleapis",
    "value": "www.googleapis.com:443"
}
```

## Reporters
Reporters are implementations of Dropwizard's `ScheduledReporter` provided via the SPI using
Factory that implements `DropwizardMetricsReporterFactory` from the `api` module. Below you will
find the implementations delivered with this project.

### Console
Uses [`ConsoleReporter`](https://metrics.dropwizard.io/3.1.0/manual/core/#console) which periodically 
reports all registered metrics to the console.

#### Configuration
- `rateUnit` - string representation of the `java.util.concurrent.TimeUnit` value, rates will be 
converted to the given time unit (default `SECONDS`)
- `durationUnit` - string representation of the `java.util.concurrent.TimeUnit` value, durations 
will be converted to the given time unit (default `MILLISECONDS`)

### Logger
Uses [`Slf4jReporter`](https://metrics.dropwizard.io/3.1.0/manual/core/#slf4j) which periodically 
logs metrics to the `metrics` logger (you need to define that logger).

#### Configuration
- `rateUnit` - string representation of the `java.util.concurrent.TimeUnit` value, rates will be 
converted to the given time unit (default `SECONDS`)
- `durationUnit` - string representation of the `java.util.concurrent.TimeUnit` value, durations 
will be converted to the given time unit (default `MILLISECONDS`)
- `loggingLevel` - logging level used when reporting (default `INFO`)

### Graphite
> In progress....

## Creating a custom Reporter
To create a new Reporter available via SPI follow these instructions:
- Implement [`DropwizardMetricsReporterFactory`](/blob/master/api/src/main/kotlin/io/knotx/metrics/reporter/DropwizardMetricsReporterFactory.kt)
- add `META-INF/services/io.knotx.metrics.reporter.DropwizardMetricsReporterFactory` file to the resources that refers created factory in order to register it in the Service Provider