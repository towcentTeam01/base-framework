package com.towcent.base.common.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author huangtao
 * @date 2018-04-17 18:57:40
 * @version 1.0
 * @copyright facegarden.com
 */
@Setter @Getter
public class ApiDocParamOut implements Serializable {

    private static final long serialVersionUID = 1L;
	
	/**
     * 主键Id.
     */
	private Integer id;
	
	/**
     * 字段名称.
     */
	private String fieldName;
	
	/**
     * 字段描述.
     */
	private String fieldDesc;
	
	/**
     * 字段类型(String|Integer|Double|Date|list).
     */
	private String fieldType;
	
	/**
     * 输出格式(例如时间格式化).
     */
	private String outFormat;
	
	/**
     * 示例值.
     */
	private String example;
	
	/**
     * 接口Id.
     */
	private Integer interfaceId;
	
	
	// 扩展参数
	private List<ApiDocParamOutSec> outSecs;
}