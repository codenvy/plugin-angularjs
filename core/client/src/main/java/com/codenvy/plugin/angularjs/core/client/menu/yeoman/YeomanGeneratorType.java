/*******************************************************************************
 * Copyright (c) 2014 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
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
