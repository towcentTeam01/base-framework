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
        GtPushDto dto = new GtPushDto();
        dto.setCid("12fe769da80814dcc95053566c915f1b");
        dto.setTitle("我是推送标题");
        dto.setText("我是推送内容");
        dto.setLogoUrl("http://47.244.99.64:81/huangtao/upload/123.jpg");
        pushApi.gtPushSingle(dto, "");

        System.out.println("===================>");
    }
}
