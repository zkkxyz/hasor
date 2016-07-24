/*
 * Copyright 2008-2009 the original author or authors.
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
package net.hasor.restful.invoker;
import net.hasor.restful.InvokerContext;
import net.hasor.restful.RestfulContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 线程安全
 * @version : 2013-6-5
 * @author 赵永春 (zyc@hasor.net)
 */
class InvContext extends HashMap<String, Object> implements InvokerContext {
    Map<String, List<String>> queryParamLocal;
    Map<String, Object>       pathParamsLocal;
    //
    private MappingToDefine     define;
    private Method              targetMethod;
    private String              viewName;
    private RestfulContext      context;
    private HttpServletRequest  httpRequest;
    private HttpServletResponse httpResponse;
    //
    //
    public InvContext(MappingToDefine define, Method targetMethod, RestfulContext context) {
        this.define = define;
        this.targetMethod = targetMethod;
        this.context = context;
        //
    }
    @Override
    public HttpServletRequest getHttpRequest() {
        return httpRequest;
    }
    @Override
    public HttpServletResponse getHttpResponse() {
        return httpResponse;
    }
    @Override
    public Method getTarget() {
        return targetMethod;
    }
    @Override
    public RestfulContext getContext() {
        return context;
    }
    @Override
    public String getViewName() {
        return this.viewName;
    }
    @Override
    public String setViewName(String viewName) {
        return this.viewName = viewName;
    }
    public MappingToDefine getDefine() {
        return define;
    }
    //
    public void initParams(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        this.httpRequest = httpRequest;
        this.httpResponse = httpResponse;
        this.viewName = httpRequest.getRequestURI();
        Enumeration<?> paramEnum = httpRequest.getParameterNames();
        while (paramEnum.hasMoreElements()) {
            Object paramKey = paramEnum.nextElement();
            String key = paramKey.toString();
            String val = httpRequest.getParameter(key);
            this.put("req_" + key, val);
        }
    }
}