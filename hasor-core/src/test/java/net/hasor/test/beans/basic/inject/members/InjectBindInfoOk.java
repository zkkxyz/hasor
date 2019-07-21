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
package net.hasor.test.beans.basic.inject.members;
import net.hasor.core.BindInfo;
import net.hasor.core.spi.BindInfoAware;
import net.hasor.test.beans.basic.pojo.PojoBean;
import net.hasor.test.beans.basic.pojo.PojoBeanRef;

public class InjectBindInfoOk extends PojoBeanRef implements BindInfoAware {
    @Override
    public void setBindInfo(BindInfo<?> bindInfo) {
        PojoBean pojoBean = new PojoBean();
        if (bindInfo != null) {
            pojoBean.setUuid("create by BindInfoAware ,bindID is " + bindInfo.getBindID());
        } else {
            pojoBean.setUuid("create by BindInfoAware ,bindID is null.");
        }
        this.setPojoBean(pojoBean);
    }
}