package com.towcent.base.common.push;

import com.towcent.base.BaseTest;
import com.towcent.base.common.exception.RpcException;
import com.towcent.base.common.model.GtPushDto;
import com.towcent.base.manager.PushApi;
import org.junit.Test;

import javax.annotation.Resource;

public class GtPushTest extends BaseTest {

    @Resource
    private PushApi pushApi;

    @Test
    public void push() throws RpcException {
        String appId = "HZHJ4EgUcX6IEIYiYGw5b1";
        String gtAppKey = "teubmA7Ajn6KdmGVJSMNL8";
        String gtMasterSecret = "R3Z4rcTWqC8ztjDMOB86B1";

        GtPushDto dto = new GtPushDto();
        // Android
//		dto.setCid("57e470004647da7a28a869aef29827a7");
//		dto.setCid("57b7ada6b168c46777ad480c0d1e6148");
        // IOS
        dto.setCid("60817f40e1a68302e4f918e986e03fd9");
        dto.setTitle("我是推送标题");
        dto.setText("我是推送内容");
        dto.setIosFlag(true);
        // dto.setLogoUrl("http://47.244.99.64:81/huangtao/upload/123.jpg");
        pushApi.gtPushSingle(dto, appId, gtAppKey, gtMasterSecret);

        System.out.println("===================>");
    }

    @Test
    public void clearBadge() throws RpcException {
        String appId = "HZHJ4EgUcX6IEIYiYGw5b1";
        String gtAppKey = "teubmA7Ajn6KdmGVJSMNL8";
        String gtMasterSecret = "R3Z4rcTWqC8ztjDMOB86B1";

        String cid = "60817f40e1a68302e4f918e986e03fd9";
        pushApi.clearIOSBadgeForCid(cid, appId, gtAppKey, gtMasterSecret);

        System.out.println("===================>");
    }
}
