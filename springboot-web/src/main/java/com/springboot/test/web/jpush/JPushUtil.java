package com.springboot.test.web.jpush;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.springboot.test.beans.JPushInfo;
import com.springboot.test.beans.enums.JPushCategoryEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 极光推送
 * @author liufei
 * @date 2019/1/11 15:48
 */
public class JPushUtil {
    private static Logger logger = LoggerFactory.getLogger(JPushUtil.class);

    private static final String APP_KEY ="1ed42a9ee3b906a36b446317";
    private static final String MASTER_SECRET = "bfaf5109ac34f88cb78a96c1";

    public static void main(String[] args) {
        /*JPushInfo jPushInfo = new JPushInfo();
        jPushInfo.setTitle("极光推送标题");
        jPushInfo.setContent("极光推送内容");
        jPushInfo.setCategory(JPushCategoryEnum.ALL);//推送设备，默认设置为all
        testSendPush(jPushInfo);*/
        JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY, null, ClientConfig.getInstance());
        try {
//            jpushClient.sendAndroidMessageWithRegistrationID("标题","内容",new String[]{"13065ffa4e4e7ca3458"});
            jpushClient.sendAndroidMessageWithAlias("标题","内容",new String[]{"liufei"});
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }
    }

    public static void testSendPush(JPushInfo jPushInfo) {
        final JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY, null, ClientConfig.getInstance());
        final PushPayload payload = buildPushObject_android_and_ios(jPushInfo);
        try {
            PushResult result = jpushClient.sendPush(payload);
            logger.info("Got result - " + result);
            System.out.println(result);
        } catch (APIConnectionException e) {
            logger.error("Connection error. Should retry later. ", e);
            logger.error("Sendno: " + payload.getSendno());

        } catch (APIRequestException e) {
            logger.error("Error response from JPushUtil server. Should review and fix it. ", e);
            logger.info("HTTP Status: " + e.getStatus());
            logger.info("Error Code: " + e.getErrorCode());
            logger.info("Error Message: " + e.getErrorMessage());
            logger.info("Msg ID: " + e.getMsgId());
            logger.error("Sendno: " + payload.getSendno());
        }
    }

    public static PushPayload buildPushObject_android_and_ios(JPushInfo jPushInfo) {
        Map<String, String> extras = new HashMap<>();
        extras.put("test", "https://community.jiguang.cn/push");
        logger.info("发送的设备类别：{}",jPushInfo.getCategory());
        Platform platform;
        switch (jPushInfo.getCategory()){
            case ALL:platform=Platform.all();break;
            case IOS:platform=Platform.ios();break;
            case ANDROID:platform=Platform.android();break;
            case WINPHONE:platform=Platform.winphone();break;
            case ANDROID_IOS:platform=Platform.android_ios();break;
            case IOS_WINPHONE:platform=Platform.ios_winphone();break;
            case ANDROID_WINPHONE:platform=Platform.android_winphone();break;
            default:platform=Platform.all();
        }
        return PushPayload.newBuilder()
                .setPlatform(platform)
                .setAudience(Audience.registrationId("13065ffa4e4e7ca3458"))
                .setNotification(Notification.newBuilder()
                        .setAlert(jPushInfo.getContent())
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setTitle(jPushInfo.getTitle())
                                .addExtras(extras).build())
                        .addPlatformNotification(IosNotification.newBuilder()
                                .incrBadge(1)
                                .addExtra("extra_key", "extra_value").build())
                        .build())
                .build();
    }
}
