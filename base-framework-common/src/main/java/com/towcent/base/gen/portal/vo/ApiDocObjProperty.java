/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.towcent.base.gen.portal.vo;

import lombok.Data;

@Data
public class ApiDocObjProperty {

	private String id;
	private String fieldName;		// 字段名称
	private String fieldDesc;		// 字段描述
	private String fieldType;		// 字段类型(String|Integer|Double|Date|list|Object|Page)
	private String outFormat;		// 输出格式
	private String example;		// 示例值
	private Integer relevanceObjId;		// 关联对象Id
	private Integer objId;		// 所属对象Id
	private String apiStatus;//状态

}