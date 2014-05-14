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

package com.codenvy.plugin.angularjs.completion.dto.parser;


import com.codenvy.dto.server.DtoFactory;
import com.codenvy.plugin.angularjs.completion.dto.AngularTemplate;
import com.codenvy.plugin.angularjs.completion.dto.Function;
import com.codenvy.plugin.angularjs.completion.dto.Param;
import com.codenvy.plugin.angularjs.completion.dto.TemplateDotProvider;
import com.codenvy.plugin.angularjs.completion.dto.parser.api.AngularDocType;
import com.codenvy.plugin.angularjs.completion.dto.parser.api.CodeCommentParser;
import com.codenvy.plugin.angularjs.completion.dto.parser.api.CommentContext;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parse functions
 * @author Florent Benoit
 */
public class FunctionCommentParser implements CodeCommentParser {

    private DtoFactory dtoFactory;

    private AngularTemplate angularTemplate;

    private static final Pattern PARAM_PATTERN  = Pattern.compile("\\{(.*?)\\}\\s(.*?)\\s(.*)");
    private static final Pattern METHOD_PATTERN = Pattern.compile("(.*?)#(.*)");


    public FunctionCommentParser(DtoFactory dtoFactory, AngularTemplate angularTemplate) {
        this.dtoFactory = dtoFactory;
        this.angularTemplate = angularTemplate;
    }

    @Override
    public void onComment(CommentContext commentContext) {
        // register a new Method for the given provider
        Function function = dtoFactory.createDto(Function.class);
        List<Param> params = new ArrayList<>();
        function.setParams(params);
        List<String> paramNames = commentContext.getAttributeValues("param");
        String methodName = commentContext.getAttributeValue("name");

        if (paramNames != null) {
            for (String paramName : paramNames) {
                Param param = dtoFactory.createDto(Param.class);

                Matcher paramMatcher = PARAM_PATTERN.matcher(paramName);

                if (paramMatcher.find()) {
                    String pType = paramMatcher.group(1);
                    String pName = paramMatcher.group(2);
                    param.setName(pName);
                    param.setType(pType);

                    params.add(param);
                }
            }
        }

        // extract method name
        Matcher methodNameMatcher = METHOD_PATTERN.matcher(methodName);
        if (methodNameMatcher.find()) {
            // get provider from method name
            String providerName = methodNameMatcher.group(1);
            String mName = methodNameMatcher.group(2);

            function.setName(mName);

            TemplateDotProvider templateDotProvider = angularTemplate.getTemplateProvider(providerName);
            if (templateDotProvider != null) {
                templateDotProvider.getFunctions().add(function);
            }

        }


    }

    @Override
    public AngularDocType getSupportedType() {
        return AngularDocType.FUNCTION;
    }
}
