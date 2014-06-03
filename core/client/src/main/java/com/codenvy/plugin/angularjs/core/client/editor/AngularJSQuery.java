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
package com.codenvy.plugin.angularjs.core.client.editor;

import java.util.ArrayList;
import java.util.List;

/**
 * Store data about a code completion query.
 *
 * @author Florent Benoit
 */
public class AngularJSQuery {

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    private String prefix;

    private List<String> existingAttributes;

    public AngularJSQuery() {
        this.existingAttributes = new ArrayList<>();
    }

    public List<String> getExistingAttributes() {
        return existingAttributes;
    }

}
