/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.towcent.base.gen.portal.vo;

import lombok.Data;

/**
 * API接口出参表Entity
 * @author zhangjunjie
 * @version 2019-01-23
 */
@Data
public class ApiDocOut {

	private String id;
	private String fieldDesc;		// 字段描述
	private String fieldType;		// 字段类型(void|Object|List|Page)
	private Integer interfaceId;		// 接口Id
	private String apiStatus;//状态

}