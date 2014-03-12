/*
 * CODENVY CONFIDENTIAL
 *  __________________
 *
 *   [2014] Codenvy, S.A.
 *   All Rights Reserved.
 *
 *  NOTICE:  All information contained herein is, and remains
 *  the property of Codenvy S.A. and its suppliers,
 *  if any.  The intellectual and technical concepts contained
 *  herein are proprietary to Codenvy S.A.
 *  and its suppliers and may be covered by U.S. and Foreign Patents,
 *  patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material
 *  is strictly forbidden unless prior written permission is obtained
 *  from Codenvy S.A..
 */

/*
 * CODENVY CONFIDENTIAL
 *  __________________
 *
 *   [2014] Codenvy, S.A.
 *   All Rights Reserved.
 *
 *  NOTICE:  All information contained herein is, and remains
 *  the property of Codenvy S.A. and its suppliers,
 *  if any.  The intellectual and technical concepts contained
 *  herein are proprietary to Codenvy S.A.
 *  and its suppliers and may be covered by U.S. and Foreign Patents,
 *  patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material
 *  is strictly forbidden unless prior written permission is obtained
 *  from Codenvy S.A..
 */

package com.codenvy.plugin.angularjs.completion.dto.parser.api;

/**
 * @author Florent Benoit
 */
public enum AngularDocType {

    DIRECTIVE("directive"),
    EVENT("event"),
    FILTER("filter"),
    FUNCTION("function"),
    INTERFACE("interface"),
    INPUT("input"),
    INPUTTYPE("inputtype"),
    METHOD("method"),
    MODULE("module"),
    OBJECT("object"),
    OVERVIEW("overview"),
    PROPERTY("property"),
    PROVIDER("provider"),
    SERVICE("service"),
    TYPE("type"),
    UNKNOWN("unknown");

    private String type;

    AngularDocType(String type) {
        this.type = type;
    }


    String getType() {
        return  type;
    }
}
