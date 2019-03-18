package com.towcent.base.gen.portal.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 接口Vo
 */
@Data
public class InterfaceVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 项目对象
     */
    private ApiProject apiProject;

    /**
     * 接口对象
     */
    private ApiDocMain apiDocMain;

    /**
     *  接口入参列表
     */
    private List<ApiDocIn> apiDocIns;

    /**
     * 出参对象
     */
    private ApiDocOut apiDocOut;

    /**
     * 出参属性列表
     */
    private List<ApiDocOutProperty> outProperties;

    /**
     * 接口关联的对象列表
     */
    private List<ApiDocObj> apiDocObjs;

}
