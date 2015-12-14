/*
 * Copyright 2008-2009 the original 赵永春(zyc@hasor.net).
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
package test.net.hasor.rsf._07_network;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import org.junit.Test;
import net.hasor.core.Settings;
import net.hasor.core.setting.StandardContextSettings;
import net.hasor.rsf.RsfEnvironment;
import net.hasor.rsf.RsfSettings;
import net.hasor.rsf.address.InterAddress;
import net.hasor.rsf.domain.RsfException;
import net.hasor.rsf.domain.RsfRuntimeUtils;
import net.hasor.rsf.rpc.context.DefaultRsfEnvironment;
import net.hasor.rsf.rpc.context.DefaultRsfSettings;
import net.hasor.rsf.rpc.net.ReceivedListener;
import net.hasor.rsf.rpc.net.RsfNetChannel;
import net.hasor.rsf.rpc.net.RsfServerNetManager;
import net.hasor.rsf.rpc.net.SendCallBack;
import net.hasor.rsf.serialize.SerializeCoder;
import net.hasor.rsf.serialize.coder.JavaSerializeCoder;
import net.hasor.rsf.transform.protocol.RequestInfo;
import net.hasor.rsf.transform.protocol.ResponseInfo;
/**
 * 
 * @version : 2015年12月13日
 * @author 赵永春(zyc@hasor.net)
 */
public class NetworkTest implements ReceivedListener {
    private RsfServerNetManager server() throws IOException, URISyntaxException {
        Settings setting = new StandardContextSettings("07_server-config.xml");//create Settings
        RsfSettings rsfSetting = new DefaultRsfSettings(setting);//create RsfSettings
        RsfEnvironment rsfEnvironment = new DefaultRsfEnvironment(null, rsfSetting);//create RsfEnvironment
        RsfServerNetManager server = new RsfServerNetManager(rsfEnvironment, this);
        server.start();
        return server;
    }
    private RsfServerNetManager client() throws IOException, URISyntaxException {
        Settings setting = new StandardContextSettings("07_client-config.xml");//create Settings
        RsfSettings rsfSetting = new DefaultRsfSettings(setting);//create RsfSettings
        RsfEnvironment rsfEnvironment = new DefaultRsfEnvironment(null, rsfSetting);//create RsfEnvironment
        RsfServerNetManager client = new RsfServerNetManager(rsfEnvironment, this);
        client.start();
        return client;
    }
    private RequestInfo buildInfo() throws Throwable {
        RequestInfo request = new RequestInfo();
        request.setRequestID(RsfRuntimeUtils.genRequestID());
        request.setServiceGroup("RSF");
        request.setServiceName("test.net.hasor.rsf.services.EchoService");
        request.setServiceVersion("1.0.0");
        request.setSerializeType("serializeType");
        request.setTargetMethod("targetMethod");
        request.setClientTimeout(6000);
        request.setReceiveTime(System.currentTimeMillis());
        //
        SerializeCoder coder = new JavaSerializeCoder();
        request.addParameter("java.lang.String", coder.encode("say Hello."));
        request.addParameter("java.lang.Long", coder.encode(111222333444555L));
        request.addParameter("java.util.Date", coder.encode(new Date()));
        request.addParameter("java.lang.Object", coder.encode(null));
        //
        request.addOption("auth", "yes");
        request.addOption("user", "guest");
        request.addOption("password", null);
        return request;
    }
    //
    private RsfServerNetManager server;
    private RsfServerNetManager client;
    @Test()
    public void testNetwork() throws Throwable {
        server = server();
        client = client();
        //
        RsfNetChannel channel = client.getChannel(new InterAddress("192.168.31.175", 8000, "unit"));
        //
        channel.sendData(buildInfo(), new SendCallBack() {
            @Override
            public void failed(long requestID, RsfException e) {
                System.out.println("failed:" + requestID);
            }
            @Override
            public void complete(long requestID) {
                System.out.println("complete:" + requestID);
            }
        });
        Thread.sleep(10000);
    }
    @Override
    public void receivedMessage(ResponseInfo response) {
        System.out.println("received ResponseInfo:" + response.getRequestID());
    }
    @Override
    public void receivedMessage(RequestInfo response) {
        System.out.println("received RequestInfo:" + response.getRequestID());
    }
}