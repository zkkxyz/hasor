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
package net.hasor.dataway.service;
import net.hasor.core.InjectSettings;
import net.hasor.dataql.DataQL;
import net.hasor.dataql.QueryResult;
import net.hasor.dataql.domain.ValueModel;
import net.hasor.dataway.daos.TestPathQuery;
import net.hasor.utils.StringUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.HashMap;

/**
 * 检测服务。
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2020-03-20
 */
@Singleton
public class CheckService {
    @InjectSettings("${HASOR_DATAQL_DATAWAY_API_URL}")
    private String apiUrl;
    @Inject
    private DataQL dataQL;

    public void checkApi(String apiPath) throws IOException {
        if (StringUtils.isBlank(this.apiUrl)) {
            throw new IllegalArgumentException("The API path is empty.");
        }
        if (!apiPath.startsWith(this.apiUrl)) {
            throw new IllegalArgumentException("The API prefix must be " + this.apiUrl);
        }
        if (!apiPath.matches("[\\$\\(\\)\\*\\+\\-\\.!#&',/:;=?@_~0-9a-zA-Z]+")) {
            throw new IllegalArgumentException("Allowed characters： !  #  $  &  '  (  )  *  +  ,  -  .  /  :  ;  =  ?  @  _  ~  0-9  a-z  A-Z");
        }
        if (!apiPath.endsWith("/")) {
            throw new IllegalArgumentException("The API must '/' end");
        }
        //
        QueryResult queryResult = new TestPathQuery(this.dataQL).execute(new HashMap<String, String>() {{
            put("apiPath", apiPath);
        }});
        if (((ValueModel) queryResult.getData()).asBoolean()) {
            throw new IllegalArgumentException("this API path has been used.");
        }
    }
}