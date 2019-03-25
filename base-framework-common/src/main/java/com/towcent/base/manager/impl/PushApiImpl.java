package com.towcent.base.manager.impl;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.PushPayload;
import com.alibaba.fastjson.JSON;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.IQueryResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.base.payload.MultiMedia;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.AbstractTemplate;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.gexin.rp.sdk.template.style.Style0;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.towcent.base.common.exception.RpcException;
import com.towcent.base.common.model.GtAps;
import com.towcent.base.common.model.GtPushDto;
import com.towcent.base.common.model.JPushDto;
import com.towcent.base.common.model.PushMessage;
import com.towcent.base.common.utils.StringUtils;
import com.towcent.base.common.utils.push.JPushFactory;
import com.towcent.base.common.utils.push.PayloadBuild;
import com.towcent.base.manager.PushApi;
import com.towcent.base.service.BaseService;
import com.towcent.base.service.SysAppSessionService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PushApiImpl extends BaseService implements PushApi {
	
	@Resource 
	private SysAppSessionService sessionApi;
	// @Resource
	// private SpringConfig springConfig;
	@Resource
	private BaseCommonApiImpl baseCommonApi;

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
	public void gtPushSingle(GtPushDto dto, String appId, String gtAppKey, String gtMasterSecret) throws RpcException {
		IGtPush push = new IGtPush(gtAppKey, gtMasterSecret);
		AbstractTemplate template = null;
		if (dto.isIosFlag()) {
			template = transmissionTemplate(dto, appId, gtAppKey);
		} else {
			template = notificationTemplate(dto, appId, gtAppKey);
		}

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

	@Override
	public void clearIOSBadgeForCid(String cid, String appId, String gtAppKey, String gtMasterSecret) throws RpcException {
		IGtPush push = new IGtPush(gtAppKey, gtMasterSecret);
		List<String> cidList = Lists.newArrayList();
		cidList.add(cid);
		IQueryResult res = push.setBadgeForCID("0", appId, cidList);
		Map<String, Object> resp = res.getResponse();
		if (!"Success".equalsIgnoreCase((String) resp.get("result"))) {
			logger.error(resp.toString());
		}
	}

	/**
	 * 普通模板<br />
	 * 在通知栏显示一条含图标、标题的通知，用户点击后激活您的应用
	 * @param dto
	 * @param appId
	 * @param appkey
	 * @return
	 */
	private NotificationTemplate notificationTemplate(GtPushDto dto, String appId, String appkey) {
		NotificationTemplate template = new NotificationTemplate();
		// 设置APPID与APPKEY
		template.setAppId(appId);
		template.setAppkey(appkey);
		// 透传消息设置，1为强制启动应⽤用，客户端接收到消息后就会⽴立即启动应⽤用；2为等待应⽤用启动
		template.setTransmissionType(1);
		Map<String, Object> transmissionObj = Maps.newHashMap();
		Map<String, Object> apsMap = Maps.newHashMap();
		apsMap.put("title", dto.getTitle());
		apsMap.put("body", dto.getText());
		transmissionObj.put("aps", apsMap);
		template.setTransmissionContent(JSON.toJSONString(transmissionObj));
		/* 设置定时展示时间 Template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00"); */
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

	/**
	 * 透传消息模板<br/>
	 * 透传消息是指消息传递到客户端只有消息内容，展现形式由客户端自行定义。客户端可自定义通知的展现形式，也可自定义通知到达之后的动作，或者不做任何展现。iOS推送也使用该模板。
	 * @param dto
	 * @param appId
	 * @param appkey
	 * @return
	 */
	private TransmissionTemplate transmissionTemplate(GtPushDto dto, String appId, String appkey) {
		TransmissionTemplate template = new TransmissionTemplate();
		template.setAppId(appId);
		template.setAppkey(appkey);
		// 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
		template.setTransmissionType(1);
		Map<String, Object> json = Maps.newHashMap();
		GtAps aps = new GtAps();
		GtAps.GtAlert alert = new GtAps.GtAlert();
		alert.setTitle(dto.getTitle());
		alert.setBody(dto.getText());
		alert.setPromptFlag("1");
		aps.setAlert(alert);
		Map<String, Object> apsMap = new HashMap<String, Object>(1) {
			{
				put("aps", aps);
			}
		};
		json.put("payload", JSON.toJSONString(apsMap));
		template.setTransmissionContent(JSON.toJSONString(json));
		/* 设置定时展示时间 template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00"); */

		// IOS APNs消息
		APNPayload payload = new APNPayload();
		payload.setAutoBadge("+1");
		payload.setContentAvailable(1);
		// ios 12.0 以上可以使用 Dictionary 类型的 sound
		payload.setSound("default");
		// payload.setCategory("$由客户端定义");
		// payload.addCustomMsg("payload", "payload");

		// 字典模式使用APNPayload.DictionaryAlertMsg
		payload.setAlertMsg(getDictionaryAlertMsg(dto));

		template.setAPNInfo(payload);
		return template;
	}

	public APNPayload.DictionaryAlertMsg getDictionaryAlertMsg(GtPushDto dto) {
		APNPayload.DictionaryAlertMsg msg = new APNPayload.DictionaryAlertMsg();
		msg.setTitle(dto.getTitle());
		msg.setBody(dto.getText());
		return msg;
	}
}
