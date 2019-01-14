package com.springboot.test.web.easemob.impl;

import com.springboot.test.web.easemob.api.EasemobAPI;
import com.springboot.test.web.easemob.api.FileAPI;
import com.springboot.test.web.easemob.util.EasemobResponseHandler;
import com.springboot.test.web.easemob.util.EasemobTokenUtil;
import io.swagger.client.ApiException;
import io.swagger.client.api.UploadAndDownloadFilesApi;

import java.io.File;


public class EasemobFile implements FileAPI {
    private EasemobResponseHandler responseHandler = new EasemobResponseHandler();
    private UploadAndDownloadFilesApi api = new UploadAndDownloadFilesApi();
    @Override
    public Object uploadFile(final Object file) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
                return api.orgNameAppNameChatfilesPost(EasemobTokenUtil.ORG_NAME, EasemobTokenUtil.APP_NAME,EasemobTokenUtil.getAccessToken(),(File)file,true);
             }
        });
    }

    @Override
    public Object downloadFile(final String fileUUID,final  String shareSecret,final Boolean isThumbnail) {
        return responseHandler.handle(new EasemobAPI() {
            @Override
            public Object invokeEasemobAPI() throws ApiException {
               return api.orgNameAppNameChatfilesUuidGet(EasemobTokenUtil.ORG_NAME,EasemobTokenUtil.APP_NAME,EasemobTokenUtil.getAccessToken(),fileUUID,shareSecret,isThumbnail);
            }
        });
    }
}
