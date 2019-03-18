/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.towcent.base.gen.portal.vo;

import lombok.Data;

import java.util.List;

/**
 * 对象表Entity
 * @author zhangjunjie
 * @version 2019-01-23
 */
@Data
public class ApiDocObj {

	private String id;
	// 字段描述
	private String fieldDesc;
	// 项目Id
	private Integer projectId;
	// 状态
	private String apiStatus;
	private String modulesName;
	private String alias;

	private List<ApiDocObjProperty> propertyList;

	/** 导入的全类名 */
	private String importStr;

}