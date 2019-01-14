package com.springboot.test.web.easemob.impl;

import com.springboot.test.web.easemob.api.ChatRoomAPI;
import com.springboot.test.web.easemob.api.EasemobAPI;
import com.springboot.test.web.easemob.util.EasemobResponseHandler;
import com.springboot.test.web.easemob.util.EasemobTokenUtil;
import io.swagger.client.ApiException;
import io.swagger.client.StringUtil;
import io.swagger.client.api.ChatRoomsApi;
import io.swagger.client.model.Chatroom;
import io.swagger.client.model.ModifyChatroom;
import io.swagger.client.model.UserNames;

public class EasemobChatRoom implements ChatRoomAPI {
    private EasemobResponseHandler responseHandler = new EasemobResponseHandler();
    private ChatRoomsApi api = new ChatRoomsApi();

    @Override
    public Object createChatRoom(final Object payload) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatroomsPost(EasemobTokenUtil.ORG_NAME,EasemobTokenUtil.APP_NAME,EasemobTokenUtil.getAccessToken(), (Chatroom) payload);
            }
        });
    }

    @Override
    public Object modifyChatRoom(final String roomId,final Object payload) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatroomsChatroomIdPut(EasemobTokenUtil.ORG_NAME,EasemobTokenUtil.APP_NAME,EasemobTokenUtil.getAccessToken(),roomId, (ModifyChatroom) payload);
            }
        });
    }

    @Override
    public Object deleteChatRoom(final String roomId) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatroomsChatroomIdDelete(EasemobTokenUtil.ORG_NAME,EasemobTokenUtil.APP_NAME,EasemobTokenUtil.getAccessToken(),roomId);
            }
        });
    }

    @Override
    public Object getAllChatRooms() {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatroomsGet(EasemobTokenUtil.ORG_NAME,EasemobTokenUtil.APP_NAME,EasemobTokenUtil.getAccessToken());
            }
        });
    }

    @Override
    public Object getChatRoomDetail(final String roomId) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatroomsChatroomIdGet(EasemobTokenUtil.ORG_NAME,EasemobTokenUtil.APP_NAME,EasemobTokenUtil.getAccessToken(),roomId);
            }
        });
    }

    @Override
    public Object addSingleUserToChatRoom(final String roomId,final String userName) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatroomsChatroomIdUsersUsernamePost(EasemobTokenUtil.ORG_NAME,EasemobTokenUtil.APP_NAME,EasemobTokenUtil.getAccessToken(),roomId,userName);
            }
        });
    }

    @Override
    public Object addBatchUsersToChatRoom(final String roomId,final Object payload) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatroomsChatroomIdUsersPost(EasemobTokenUtil.ORG_NAME,EasemobTokenUtil.APP_NAME,EasemobTokenUtil.getAccessToken(),roomId, (UserNames) payload);
            }
        });
    }

    @Override
    public Object removeSingleUserFromChatRoom(final String roomId,final String userName) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatroomsChatroomIdUsersUsernameDelete(EasemobTokenUtil.ORG_NAME,EasemobTokenUtil.APP_NAME,EasemobTokenUtil.getAccessToken(),roomId,userName);
            }
        });
    }

    @Override
    public Object removeBatchUsersFromChatRoom(final String roomId,final String[] userNames) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatroomsChatroomIdUsersUsernamesDelete(EasemobTokenUtil.ORG_NAME,EasemobTokenUtil.APP_NAME,EasemobTokenUtil.getAccessToken(),roomId, StringUtil.join(userNames,","));
            }
        });
    }
}
