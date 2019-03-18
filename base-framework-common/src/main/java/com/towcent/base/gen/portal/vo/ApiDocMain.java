/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.towcent.base.gen.portal.vo;

import lombok.Data;

/**
 * API接口主表Entity
 * @author zhangjunjie
 * @version 2019-01-23
 */
@Data
public class ApiDocMain {

	private String id;
	private String interfaceGroup;		// 接口组(数据字典interface_group)
	private String interfaceNo;		// 接口编码（1.1.0）第一位是组，第二位是模块，第三位是序号
	private String interfaceName;		// 接口名称
	private String interfaceDesc;		// 接口描述
	private String requestType;		// 请求类型(GET|POST)
	private String interfaceUrl;		// 接口URL
	private String isLogin;		// 是否需要验证登录(0:否 1:是)
	private Integer projectId;		// 项目Id
	private String apiStatus;//状态

}