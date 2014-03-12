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
 * __________________
 * 
 *  [2014] Codenvy, S.A.
 *  All Rights Reserved.
 * 
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.plugin.angularjs.completion.dto.parser;



import com.codenvy.plugin.angularjs.completion.dto.parser.api.CodeCommentParser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class AngularJSDocParser {

    private List<CodeCommentParser> callbacks;

    public AngularJSDocParser() {
        this.callbacks = new ArrayList<CodeCommentParser>();
    }

    public void parse(Path path) throws IOException {
        ParseFile parseFile = new ParseFile();
        for (CodeCommentParser codeCommentParser : callbacks) {
            parseFile.addCodeCommentParser(codeCommentParser);
        }
        Files.walkFileTree(path, parseFile);
    }


    public void addCodeCommentParser(CodeCommentParser codeCommentParser) {
        this.callbacks.add(codeCommentParser);
    }


}