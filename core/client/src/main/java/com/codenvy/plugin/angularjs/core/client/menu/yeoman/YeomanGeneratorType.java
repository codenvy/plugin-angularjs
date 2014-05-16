/*
 * Copyright 2014 Codenvy, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codenvy.plugin.angularjs.core.client.menu.yeoman;

/**
 * Type of all items that can be generated by Yeoman generator.
 *
 * @author Florent Benoit
 */
public enum YeomanGeneratorType {

    CONTROLLER("Controller", "CONTROLLERS"),
    CONSTANT("Constant", "CONSTANTS"),
    DECORATOR("Decorator", "DECORATORS"),
    DIRECTIVE("Directive", "DIRECTIVES"),
    FACTORY("Factory", "FACTORIES"),
    FILTER("Filter", "FILTERS"),
    PROVIDER("Provider", "PROVIDERS"),
    ROUTE("Route", "ROUTES"),
    SERVICE("Service", "SERVICES"),
    VALUE("Value", "VALUES"),
    VIEW("View", "VIEWS");

    /**
     * Name of the type.
     */
    String name;

    /**
     * Label name of this type.
     */
    String labelName;

    YeomanGeneratorType(String name, String labelName) {
        this.name = name;
        this.labelName = labelName;
    }

    public String getName() {
        return name;
    }

    public String getLabelName() {
        return labelName;
    }

}
