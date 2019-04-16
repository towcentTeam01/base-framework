package com.towcent.base.common.page;

import com.towcent.base.common.utils.Md5Utils;
import com.towcent.base.common.utils.ReflectHelper;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 分页返回对象
 * @author HuangTao
 * @version 2015年4月25日
 */
public class PaginationDto<E> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 当前页的数据
	 */
	private List<E> list;

	private Integer totalCount = 0;
	
	private Integer totalPage;

	/**
	 * 唯一标识符
	 */
	private String uniqueIdentifier;

	public PaginationDto() {
	}

	public PaginationDto(int totalCount, List<E> list) {
		super();
		this.totalCount = totalCount;
		this.list = list;
	}

	public List<E> getList() {
		return list;
	}

	public void setList(List<E> list) {
		this.list = list;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public String getUniqueIdentifier() {
		return uniqueIdentifier;
	}

	public void setUniqueIdentifier(String uniqueIdentifier) {
		this.uniqueIdentifier = uniqueIdentifier;
	}

	public void setUniqueIdentifier() {
		try {
			if (CollectionUtils.isEmpty(list)) {
				return;
			}
			StringBuilder sb = new StringBuilder();
			for (E e : list) {
				Object idObj = ReflectHelper.getValueByFieldName(e, "id");
				if (null == idObj) {
					break;
				}
				sb.append(idObj);

				Object o = ReflectHelper.getValueByFieldName(e, "updateDate");
				if (null != o) {
					Date updateDate = (Date) o;
					sb.append(updateDate.getTime());
				}
			}
			this.uniqueIdentifier = Md5Utils.encryption(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setTotalPage(Integer pageSize) {
		if (totalCount == null) {
			totalCount = 0;
		}
		if (pageSize == null || pageSize == 0) {
			pageSize = 10;
		}
		this.totalPage =  (totalCount  +  pageSize  - 1) / pageSize;
	}
}
