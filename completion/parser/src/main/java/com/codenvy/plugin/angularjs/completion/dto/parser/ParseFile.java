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

package com.codenvy.plugin.angularjs.completion.dto.parser;

import com.codenvy.plugin.angularjs.completion.dto.parser.api.AngularDocType;
import com.codenvy.plugin.angularjs.completion.dto.parser.api.CodeCommentParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Florent Benoit
 */
public class ParseFile extends SimpleFileVisitor<Path> {

    private Pattern pattern;

    private Pattern ngDocTypePattern;
    private Pattern ngNamePattern;
    private Pattern ngParamPattern;

    private List<CodeCommentParser> callbacks;

    public ParseFile() {
        this.callbacks = new ArrayList<CodeCommentParser>();
        this.pattern = Pattern.compile("/\\*\\*(.*?)\\*/", Pattern.DOTALL);

        this.ngDocTypePattern = Pattern.compile("@ngdoc(.*?)\n");
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

        if (!file.toFile().getName().endsWith(".js")) {
            return FileVisitResult.CONTINUE;
        }

        // Parse comments and send them to the parser
        BufferedReader bufferedReader = Files.newBufferedReader(file, Charset.defaultCharset());
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line).append("\n");
        }

        String content = stringBuilder.toString();

        // parse comments
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {

            // Get a comment inside /** .... */
            String comment = matcher.group();

            // Check if there is a @ngdoc element
            Matcher docTypeMatcher = ngDocTypePattern.matcher(comment);
            if (docTypeMatcher.find()) {

                // Check if match
                String ngDocType = docTypeMatcher.group(1).trim();

                // not angular doc, continue
                if (ngDocType == null || "".equals(ngDocType)) {
                    continue;
                }

                // build the given context element
                AngularCommentContext angularCommentContext = new AngularCommentContext(ngDocType, comment);
                boolean missingCallback = true;
                for (CodeCommentParser codeCommentParser : callbacks) {
                    if (angularCommentContext.getType() == codeCommentParser.getSupportedType()) {
                        codeCommentParser.onComment(angularCommentContext);
                        missingCallback = false;
                    }
                }
                if (missingCallback) {
                    System.err.println("Unable to find callback for the type :" + ngDocType);
                }

                System.out.println("   Name: " + angularCommentContext.getAttributeValue("name"));
                System.out.println("   Type: " + angularCommentContext.getType());
                if (angularCommentContext.getType() == AngularDocType.METHOD) {
                    System.out.println("       params: " + angularCommentContext.getAttributeValues("param"));
                }


            }
        }


        return FileVisitResult.CONTINUE;
    }


    public void addCodeCommentParser(CodeCommentParser codeCommentParser) {
        this.callbacks.add(codeCommentParser);
    }
}