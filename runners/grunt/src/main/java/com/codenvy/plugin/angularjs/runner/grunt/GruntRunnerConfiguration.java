/**
 * Copyright 2014 Codenvy, S.A.
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

package com.codenvy.plugin.angularjs.runner.grunt;

import com.codenvy.api.runner.dto.RunRequest;
import com.codenvy.api.runner.internal.RunnerConfiguration;

/**
 * Configuration for using Grunt as AngularJS runner.
 *
 * @author Florent Benoit
 */
public class GruntRunnerConfiguration extends RunnerConfiguration {

    private final int httpPort;
    private final int liveReloadPort;

    public GruntRunnerConfiguration(int memory, int httpPort, int liveReloadPort, RunRequest runRequest) {
        super(memory, runRequest);
        this.httpPort = httpPort;
        this.liveReloadPort = liveReloadPort;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public int getLiveReloadPort() {
        return liveReloadPort;
    }

    @Override
    public String toString() {
        return "GruntRunnerConfiguration{" +
               "memory=" + getMemory() +
               ", links=" + getLinks() +
               ", request=" + getRequest() +
               ", httpPort='" + getHttpPort() +
               ", liveReloadPort='" + getHttpPort() +
               '}';
    }
}
