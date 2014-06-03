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
package com.codenvy.plugin.angularjs.core.client.javascript.contentassist;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Florent Benoit
 */
public final class JsProposal extends JavaScriptObject {

    /**
     *
     */
    protected JsProposal() {
    }

    public native String getProposal()/*-{
        return this.proposal;
    }-*/;

    public native String getDescription()/*-{
        return this.description;
    }-*/;

    public native int getEscapePosition()/*-{
        return this.escapePosition ? this.escapePosition : -1;
    }-*/;

    public native Position[] getPositions()/*-{
        return this.positions ? this.positions : [];
    }-*/;

}
