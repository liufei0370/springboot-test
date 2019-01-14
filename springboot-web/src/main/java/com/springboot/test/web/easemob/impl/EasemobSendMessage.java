package com.springboot.test.web.easemob.impl;

import com.springboot.test.web.easemob.api.SendMessageAPI;
import com.springboot.test.web.easemob.util.EasemobResponseHandler;
import com.springboot.test.web.easemob.util.EasemobTokenUtil;
import io.swagger.client.api.MessagesApi;
import io.swagger.client.model.Msg;

public class EasemobSendMessage implements SendMessageAPI {
    private EasemobResponseHandler responseHandler = new EasemobResponseHandler();
    private MessagesApi api = new MessagesApi();
    @Override
    public Object sendMessage(final Object payload) {
        return responseHandler.handle(() -> api.orgNameAppNameMessagesPost(EasemobTokenUtil.ORG_NAME,EasemobTokenUtil.APP_NAME,EasemobTokenUtil.getAccessToken(), (Msg) payload));
    }
}
