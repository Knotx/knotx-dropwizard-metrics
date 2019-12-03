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
rootProject.name = "knotx-dropwizard-metrics"

include("knotx-dropwizard-core")
include("knotx-dropwizard-api")
include("knotx-dropwizard-reporter-common")
include("knotx-dropwizard-reporter-console")
include("knotx-dropwizard-reporter-graphite")
include("knotx-dropwizard-reporter-logger")
project(":knotx-dropwizard-core").projectDir = file("core")
project(":knotx-dropwizard-api").projectDir = file("api")
project(":knotx-dropwizard-reporter-common").projectDir = file("reporter/common")
project(":knotx-dropwizard-reporter-console").projectDir = file("reporter/console")
project(":knotx-dropwizard-reporter-graphite").projectDir = file("reporter/graphite")
project(":knotx-dropwizard-reporter-logger").projectDir = file("reporter/logger")

