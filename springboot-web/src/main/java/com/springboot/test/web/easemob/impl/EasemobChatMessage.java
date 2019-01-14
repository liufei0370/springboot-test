package com.springboot.test.web.easemob.impl;

import com.springboot.test.web.easemob.api.ChatMessageAPI;
import com.springboot.test.web.easemob.util.EasemobResponseHandler;
import com.springboot.test.web.easemob.util.EasemobTokenUtil;
import io.swagger.client.api.ChatHistoryApi;


public class EasemobChatMessage  implements ChatMessageAPI {

    private EasemobResponseHandler responseHandler = new EasemobResponseHandler();
    private ChatHistoryApi api = new ChatHistoryApi();

    @Override
    public Object exportChatMessages(final Long limit,final String cursor,final String query) {
        return responseHandler.handle(() -> api.orgNameAppNameChatmessagesGet(EasemobTokenUtil.ORG_NAME,EasemobTokenUtil.APP_NAME, EasemobTokenUtil.getAccessToken(),query,limit+"",cursor));
    }

    @Override
    public Object exportChatMessages(final String timeStr) {
        return responseHandler.handle(() -> api.orgNameAppNameChatmessagesTimeGet(EasemobTokenUtil.ORG_NAME,EasemobTokenUtil.APP_NAME,EasemobTokenUtil.getAccessToken(),timeStr));
    }
}
