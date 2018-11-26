package com.towcent.base.manager.impl;

import java.util.List;

import javax.annotation.Resource;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.style.Style0;
import com.towcent.base.common.config.SpringConfig;
import com.towcent.base.common.model.GtPushDto;
import com.towcent.base.common.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.towcent.base.common.exception.RpcException;
import com.towcent.base.common.exception.ServiceException;
import com.towcent.base.common.model.JPushDto;
import com.towcent.base.common.model.PushMessage;
import com.towcent.base.common.utils.push.JPushFactory;
import com.towcent.base.common.utils.push.PayloadBuild;
import com.towcent.base.manager.PushApi;
import com.towcent.base.service.BaseService;
import com.towcent.base.service.SysAppSessionService;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;

@Service
public class PushApiImpl extends BaseService implements PushApi {
	
	@Resource 
	private SysAppSessionService sessionApi;

	@Resource
	private SpringConfig springConfig;

	@Override
	public void pushMsg(PushMessage message) throws RpcException {
		message.setTarget("0");
		message.setBadge(1);
		
//		try {
			List<String> tokens = null; // sessionApi.getAppTokenListByParam(message.getAccount(), message.getAppType());
			if (!CollectionUtils.isEmpty(tokens)) {
				for (String token : tokens) {
					message.setToken(token);
					// ChatUtil.pushMessage4Ios(message);					
				}
			}
//		} catch (ServiceException e) {
//			logger.error("", e);
//			throw new RpcException("", e);
//		}
	}
	
	@Override
	public void jpushAll(JPushDto dto) throws RpcException {
		try {
			PushPayload payload = PayloadBuild.buildPushAllPayload(dto.getTitle(), dto.getContent(), dto.getExtraMap());
			PushResult result = JPushFactory.getJPushClient().sendPush(payload);
			logger.info("推送结果:{}.", result);
		} catch (APIConnectionException e) {
			logger.error("", e);
		} catch (APIRequestException e) {
			logger.error("", e);
		}
	}
	
	@Override
	public void jpushMsg(JPushDto dto) throws RpcException {
		try {
			PushPayload payload = PayloadBuild.buildPushObjectAndroidAndIos(
					dto.getUserId() + "", dto.getTitle(), dto.getContent(), dto.getExtraMap());
			JPushClient client = JPushFactory.getJPushClient();
			PushResult result = client.sendPush(payload);
			logger.info("推送结果:{}.", result);
		} catch (APIConnectionException e) {
			logger.error("", e);
		} catch (APIRequestException e) {
			logger.error("", e);
		}
		
	}

	@Override
	public void gtPushSingle(GtPushDto dto, String appId) throws RpcException {
		IGtPush push = new IGtPush(springConfig.getGtAppKey(), springConfig.getGtMasterSecret());
		NotificationTemplate template = notificationTemplate(dto, appId, springConfig.getGtAppKey());

		SingleMessage message = new SingleMessage();
		message.setOffline(true);
		// 离线有效时间，单位为毫秒，可选
		message.setOfflineExpireTime(24 * 3600 * 1000);
		message.setData(template);
		// 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
		message.setPushNetWorkType(0);

		// 推送范围 （cid与别名二选一）
		Target target = new Target();
		target.setAppId(appId);
		target.setClientId(dto.getCid());
		// target.setAlias(Alias);

		IPushResult ret = null;
		try {
			ret = push.pushMessageToSingle(message, target);
		} catch (RequestException e) {
			e.printStackTrace();
			ret = push.pushMessageToSingle(message, target, e.getRequestId());
		}
		if (ret != null) {
			logger.info(ret.getResponse().toString());
		} else {
			logger.info("服务器响应异常");
		}
	}

	private NotificationTemplate notificationTemplate(GtPushDto dto, String appId, String appkey) {
		NotificationTemplate template = new NotificationTemplate();
		// 设置APPID与APPKEY
		template.setAppId(appId);
		template.setAppkey(appkey);
		// 透传消息设置，1为强制启动应⽤用，客户端接收到消息后就会⽴立即启动应⽤用；2为等待应⽤用启动
		template.setTransmissionType(1);
		template.setTransmissionContent("请输⼊入您要透传的内容");
		// 设置定时展示时间
		// template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
		Style0 style = new Style0();
		// 设置通知栏标题与内容
		style.setTitle(dto.getTitle());
		style.setText(dto.getText());
		// 配置通知栏图标
		if (StringUtils.isNotBlank(dto.getLogo())) {
			style.setLogo(dto.getLogo());
		}
		// 配置通知栏⽹网络图标
		if (StringUtils.isNotBlank(dto.getLogoUrl())) {
			style.setLogoUrl(dto.getLogoUrl());
		}
		// 设置通知是否响铃，震动，或者可清除
		style.setRing(true);
		style.setVibrate(true);
		style.setClearable(true);
		template.setStyle(style);
		return template;
	}
}
