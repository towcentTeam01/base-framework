/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.towcent.base.gen.portal.vo;

import lombok.Data;

@Data
public class ApiDocIn {

	private String id;
	private String paramName;		// 参数名称
	private String paramDesc;		// 参数描述
	private String paramType;		// 参数类型(String|Integer|Double|Date)
	private String valDeclare;		// 取值说明
	private String required;		// 是否必填项(0:否 1:是)
	private String regular;		// 正则表达式
	private Integer interfaceId;		// 接口Id
	private String apiStatus;//状态
	private String example;		// 示例值

}