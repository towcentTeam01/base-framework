package com.towcent.base.common.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

/**
 * 个推 -- 推送对象
 * @author huangtao
 */
@Setter @Getter
public class GtPushDto implements Serializable {

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 通知栏标题
     */
    private String title;

    /**
     * 通知栏内容
     */
    private String text;

    /***
     * 通知的图标名称，包含后缀名(需要在客户端开发时嵌入),如“push.png”
     */
    private String logo;

    /**
     * 通知栏网络图标
     */
    private String logoUrl;

    /** 扩展参数 */
    private Map<String, String> extraMap;

}
