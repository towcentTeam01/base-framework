package com.towcent.base.common.model;

import com.towcent.base.common.page.PaginationDto;
import com.towcent.base.common.page.SimplePage;
import com.towcent.base.common.page.SimplePageDto;
import com.towcent.base.common.utils.StringUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * 分页工具类
 */
public final class PageUtils {

    public final static String orderBy = "orderBy";
    public final static String orderByField = "orderByField";

    /** 降序 */
    public final static String DESC = "DESC";
    /** 升序 */
    public final static String ASC = "ASC";

    /**
     * 获取排序字符串 -- 默认值为降序
     * @param params
     * @return
     */
    public static String getOrderByDesc(Map<String, Object> params) {
        return getOrderBy(params, DESC);
    }

    /**
     * 排序字符串(降序/升序)
     * @param params
     * @param def 默认值
     * @return
     */
    public static String getOrderBy(Map<String, Object> params, String def) {
        return MapUtils.getString(params, orderBy, def);
    }

    /**
     * portal构造排序
     * @param orderBySrc  排序 0:降序 1:升序
     * @param params
     * @param def         0:降序 1:升序
     */
    public static void buildOrderBy(String orderBySrc, Map<String, Object> params, String def) {
        params.put("orderBy", buildOrderBy(orderBySrc, def));
    }

    /**
     * portal构造排序
     * @param orderBySrc  排序 0:降序 1:升序
     * @param def         0:降序 1:升序
     * @return
     */
    public static String buildOrderBy(String orderBySrc, String def) {
        if (StringUtils.isBlank(orderBySrc)) {
            orderBySrc = def;
        }
        return "0".equals(orderBySrc) ? "DESC" : "ASC";
    }

    /**
     * 排序字段
     * @param params
     * @param def  默认值
     * @return
     */
    public static String getOrderByField(Map<String, Object> params, String def) {
        return MapUtils.getString(params, orderByField, def);
    }

    /**
     * 构建默认的分页对象
     * @return
     */
    public static SimplePage buildDefaultPage() {
        SimplePage simplePage = new SimplePage();
        simplePage.setPageNo(1);
        simplePage.setPageSize(100);
        return simplePage;
    }

    /**
     * 通过SimplePageDto构建Page对象
     * @param pageDto
     * @param totalCount
     * @return
     */
    public static SimplePage buildPage(SimplePageDto pageDto, int totalCount) {
        SimplePage page = new SimplePage(pageDto.getPageNo(), pageDto.getPageSize(), totalCount);
        return page;
    }

    /**
     * 判断分页对象是否为空
     *
     * @param o
     * @return
     */
    public static boolean isPageEmpty(PaginationDto<?> o) {
        if (null == o || CollectionUtils.isEmpty(o.getList())) {
            return true;
        }
        return false;
    }

}
