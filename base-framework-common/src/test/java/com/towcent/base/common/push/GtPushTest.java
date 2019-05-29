package com.towcent.base.common.push;

import com.google.common.collect.Lists;
import com.towcent.base.BaseTest;
import com.towcent.base.common.exception.RpcException;
import com.towcent.base.common.model.GtPushDto;
import com.towcent.base.manager.PushApi;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

public class GtPushTest extends BaseTest {

    @Resource
    private PushApi pushApi;

    @Test
    public void push() throws RpcException {
//        String appId = "HZHJ4EgUcX6IEIYiYGw5b1";
//        String gtAppKey = "teubmA7Ajn6KdmGVJSMNL8";
//        String gtMasterSecret = "R3Z4rcTWqC8ztjDMOB86B1";
        String appId = "FoMdsSQKUZ6yeat0XYuTp2";
        String gtAppKey = "ykBD9DfaUs79IUw0OeZtk";
        String gtMasterSecret = "Fh8FchYQnO7XaKGHzTaSo9";

        GtPushDto dto = new GtPushDto();
        // Android
//		dto.setCid("db8738b1a603ca277aaa931925966fb7");
//		dto.setCid("be915c26e6f5948eb23ca9a7cf278ed8");
        // IOS
//        dto.setCid("64756ce2409f04db85925e6958a16e0f");
        dto.setCid("db8738b1a603ca277aaa931925966fb7");
        dto.setTitle("我是推送标题");
        dto.setText("我是推送内容");
        dto.setIosFlag(true);
        // dto.setLogoUrl("http://img.cplchain.com/md_trip_icon.png");
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

    @Test
    public void gtPushBatch() throws RpcException {
        String appId = "FoMdsSQKUZ6yeat0XYuTp2";
        String gtAppKey = "ykBD9DfaUs79IUw0OeZtk";
        String gtMasterSecret = "Fh8FchYQnO7XaKGHzTaSo9";

        List<String> cids = Lists.newArrayList();
        cids.add("db8738b1a603ca277aaa931925966fb7");

        GtPushDto dto = new GtPushDto();
        dto.setTitle("我是推送标题");
        dto.setText("我是推送内容");
        dto.setIosFlag(true);
        pushApi.gtPushBatch(cids, dto, appId, gtAppKey, gtMasterSecret);

        System.out.println("===================>");
    }
}
