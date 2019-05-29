package com.towcent.base.manager;

import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.towcent.base.common.exception.RpcException;
import com.towcent.base.common.model.GtPushDto;
import com.towcent.base.common.model.JPushDto;
import com.towcent.base.common.model.PushMessage;

import java.util.List;

/**
 * 推送接口
 * @author huangtao
 *
 */
public interface PushApi {
	
	/**
	 * 推送消息(自己实现的推送)
	 * @param message
	 * @throws RpcException
	 */
	void pushMsg(PushMessage message) throws RpcException;
	
	/**
	 * 推送所有设备<br>
	 * 极光推送
	 * @param dto
	 * @throws RpcException
	 */
	void jpushAll(JPushDto dto) throws RpcException;
	
	/**
	 * 单点推送<br>.
	 * 极光推送
	 * @Title jpushMsg
	 * @param dto
	 * @throws RpcException
	 */
	void jpushMsg(JPushDto dto) throws RpcException;

	/**
	 * 个推  --> 单个推送
	 * @param dto
	 * @param appId
	 * @throws RpcException
	 */
	void gtPushSingle(GtPushDto dto, String appId, String gtAppKey, String gtMasterSecret) throws RpcException;

	/**
	 * 清除IOS角标
	 * @param cid     设备在个推服务器的唯一标识
	 * @param appId
	 * @param gtAppKey
	 * @param gtMasterSecret
	 * @throws RpcException
	 */
	void clearIOSBadgeForCid(String cid, String appId, String gtAppKey, String gtMasterSecret) throws RpcException;

	/**
	 * 批量发送推送
	 * @param cids
	 * @param dto
	 * @param appId
	 * @param gtAppKey
	 * @param gtMasterSecret
	 * @throws RpcException
	 */
	void gtPushBatch(List<String> cids, GtPushDto dto, String appId, String gtAppKey, String gtMasterSecret) throws RpcException;

}
