/**
 * 环信推送--可以用在web端
 * 参考文档：http://docs-im.easemob.com/im/100serverintegration/20users
 * 管理后台；http://console.easemob.com/app-detail/group
 * 前台页面：resources/static/easemob下的web-im压缩包
 * 修改web-im\demo\javascript\dist文件夹下的webim.config.js的appkey的值即可使用
 *
 * 环信推送实际使用的是websocket
 * socket地址为：socket地址：ws://im-api.easemob.com/ws/
 * device_uuid的生成规则： new Date().getTime() + Math.floor(Math.random().toFixed(6) * 1000000)
 * 发送下面的内容即即可连接socket并获取socket信息
 <open xmlns='urn:ietf:params:xml:ns:xmpp-framing' to='easemob.com' version='1.0'/>
 <auth xmlns='urn:ietf:params:xml:ns:xmpp-sasl' mechanism='PLAIN'>token</auth>
 <open xmlns='urn:ietf:params:xml:ns:xmpp-framing' to='easemob.com' version='1.0'/>
 <iq type='set' id='_bind_auth_2' xmlns='jabber:client'><bind xmlns='urn:ietf:params:xml:ns:xmpp-bind'><resource>webim</resource><os>webim</os><device_uuid>1547455827921</device_uuid><is_manual_login>true</is_manual_login></bind></iq>
 <iq type='set' id='_session_auth_2' xmlns='jabber:client'><session xmlns='urn:ietf:params:xml:ns:xmpp-session'/></iq>
 <iq from='1107190114089838#com-springboot-test-liufei_liufei@easemob.com/webim_1547455827921' to='easemob.com' type='result' xmlns='jabber:client' id='65ac0284-123a-461e-baef-9e5f499defe0:sendIQ'><query xmlns='jabber:iq:version'><name>easemob</name><version>1.4.2</version><os>webim</os></query></iq>
 <presence xmlns='jabber:client'/>
 <iq type='get' xmlns='jabber:client' id='005544ad-3949-48d4-bb59-a1125cbcc211:sendIQ'><query xmlns='jabber:iq:privacy'><list name='special'/></query></iq>
 * @author liufei
 * @date 2019/1/14 9:46
 */
package com.springboot.test.web.easemob;