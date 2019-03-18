/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.towcent.base.gen.portal.vo;

import lombok.Data;

/**
 * 项目表Entity
 * @author zhangjunjie
 * @version 2019-01-23
 */
@Data
public class ApiProject {

	private String id;
	private String projectName;		// 项目名称
	private String projectDesc;		// 项目描述
	private String apiStatus;//状态

	private String projectPackage;  // 项目基础包名
	private String devBasePath;    // 测试URL
	private String proBasePath;    // 正式URL

}