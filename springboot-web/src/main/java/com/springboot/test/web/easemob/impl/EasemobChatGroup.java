package com.springboot.test.web.easemob.impl;

import com.springboot.test.web.easemob.api.ChatGroupAPI;
import com.springboot.test.web.easemob.api.EasemobAPI;
import com.springboot.test.web.easemob.util.EasemobResponseHandler;
import com.springboot.test.web.easemob.util.EasemobTokenUtil;
import io.swagger.client.ApiException;
import io.swagger.client.StringUtil;
import io.swagger.client.api.GroupsApi;
import io.swagger.client.model.*;


public class EasemobChatGroup implements ChatGroupAPI {

    private EasemobResponseHandler responseHandler = new EasemobResponseHandler();
    private GroupsApi api = new GroupsApi();
    @Override
    public Object getChatGroups(final Long limit,final String cursor) {
        return responseHandler.handle(() -> api.orgNameAppNameChatgroupsGet(EasemobTokenUtil.ORG_NAME,EasemobTokenUtil.APP_NAME, EasemobTokenUtil.getAccessToken(),limit+"",cursor));
    }

    @Override
    public Object getChatGroupDetails(final String[] groupIds) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatgroupsGroupIdsGet(EasemobTokenUtil.ORG_NAME,EasemobTokenUtil.APP_NAME,EasemobTokenUtil.getAccessToken(),StringUtil.join(groupIds,","));
            }
        });
    }
    @Override
    public Object createChatGroup(final Object payload) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatgroupsPost(EasemobTokenUtil.ORG_NAME,EasemobTokenUtil.APP_NAME,EasemobTokenUtil.getAccessToken(), (Group) payload);
            }
        });
    }

    @Override
    public Object modifyChatGroup(final String groupId,final Object payload) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatgroupsGroupIdPut(EasemobTokenUtil.ORG_NAME,EasemobTokenUtil.APP_NAME,EasemobTokenUtil.getAccessToken(),groupId, (ModifyGroup) payload);
            }
        });
    }

    @Override
    public Object deleteChatGroup(final String groupId) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatgroupsGroupIdDelete(EasemobTokenUtil.ORG_NAME,EasemobTokenUtil.APP_NAME,EasemobTokenUtil.getAccessToken(),groupId);
            }
        });
    }

    @Override
    public Object getChatGroupUsers(final String groupId) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatgroupsGroupIdUsersGet(EasemobTokenUtil.ORG_NAME,EasemobTokenUtil.APP_NAME,EasemobTokenUtil.getAccessToken(),groupId);
            }
        });
    }

    @Override
    public Object addSingleUserToChatGroup(final String groupId,final String userId) {
        final UserNames userNames = new UserNames();
        UserName userList = new UserName();
        userList.add(userId);
        userNames.usernames(userList);
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatgroupsGroupIdUsersPost(EasemobTokenUtil.ORG_NAME,EasemobTokenUtil.APP_NAME,EasemobTokenUtil.getAccessToken(),groupId,userNames);
            }
        });
    }

    @Override
    public Object addBatchUsersToChatGroup(final String groupId,final Object payload) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatgroupsGroupIdUsersPost(EasemobTokenUtil.ORG_NAME,EasemobTokenUtil.APP_NAME,EasemobTokenUtil.getAccessToken(),groupId, (UserNames) payload);
            }
        });
    }

    @Override
    public Object removeSingleUserFromChatGroup(final String groupId,final String userId) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatgroupsGroupIdUsersUsernameDelete(EasemobTokenUtil.ORG_NAME,EasemobTokenUtil.APP_NAME,EasemobTokenUtil.getAccessToken(),groupId,userId);
            }
        });
    }

    @Override
    public Object removeBatchUsersFromChatGroup(final String groupId,final String[] userIds) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatgroupsGroupIdUsersMembersDelete(EasemobTokenUtil.ORG_NAME,EasemobTokenUtil.APP_NAME,EasemobTokenUtil.getAccessToken(),groupId,StringUtil.join(userIds,","));
            }
        });
    }

    @Override
    public Object transferChatGroupOwner(final String groupId,final Object payload) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatgroupsGroupidPut(EasemobTokenUtil.ORG_NAME,EasemobTokenUtil.APP_NAME,EasemobTokenUtil.getAccessToken(),groupId, (NewOwner) payload);
            }
        });
    }

    @Override
    public Object getChatGroupBlockUsers(final String groupId) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatgroupsGroupIdBlocksUsersGet(EasemobTokenUtil.ORG_NAME,EasemobTokenUtil.APP_NAME,EasemobTokenUtil.getAccessToken(),groupId);
            }
        });
    }

    @Override
    public Object addSingleBlockUserToChatGroup(final String groupId,final String userId) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatgroupsGroupIdBlocksUsersUsernamePost(EasemobTokenUtil.ORG_NAME,EasemobTokenUtil.APP_NAME,EasemobTokenUtil.getAccessToken(),groupId,userId);
            }
        });
    }

    @Override
    public Object addBatchBlockUsersToChatGroup(final String groupId,final Object payload) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatgroupsGroupIdBlocksUsersPost(EasemobTokenUtil.ORG_NAME,EasemobTokenUtil.APP_NAME,EasemobTokenUtil.getAccessToken(),groupId, (UserNames) payload);
            }
        });
    }

    @Override
    public Object removeSingleBlockUserFromChatGroup(final String groupId,final String userId) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatgroupsGroupIdBlocksUsersUsernameDelete(EasemobTokenUtil.ORG_NAME,EasemobTokenUtil.APP_NAME,EasemobTokenUtil.getAccessToken(),groupId,userId);
            }
        });
    }

    @Override
    public Object removeBatchBlockUsersFromChatGroup(final String groupId,final String[] userIds) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatgroupsGroupIdBlocksUsersUsernamesDelete(EasemobTokenUtil.ORG_NAME,EasemobTokenUtil.APP_NAME,EasemobTokenUtil.getAccessToken(),groupId,StringUtil.join(userIds,","));
            }
        });
    }
}
