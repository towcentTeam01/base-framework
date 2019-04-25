package com.towcent.base.common.utils;

import com.towcent.base.common.exception.RpcException;
import com.towcent.base.common.model.JsSysDictData;
import com.towcent.base.manager.BaseCommonApi;
import org.apache.commons.collections.MapUtils;

import java.util.Map;

/**
 * 数据字典工具类
 */
public class DictUtils {

    public static Map<String, JsSysDictData> getDictMapByKey(String key) throws RpcException {
        return getDictMapByKey(0, key);
    }

    public static Map<String, JsSysDictData> getDictMapByKey(Integer merchantId, String key) throws RpcException {
        BaseCommonApi baseCommonApi = SpringContextHolder.getBean(BaseCommonApi.class);
        return baseCommonApi.getDictMapByKey(merchantId, key);
    }

    public static String getDictName(String key, String value) throws RpcException {
        return getDictName(0, key, value);
    }

    public static String getDictName(Integer merchantId, String key, String value) throws RpcException {
        BaseCommonApi baseCommonApi = SpringContextHolder.getBean(BaseCommonApi.class);
        JsSysDictData dictDtl = baseCommonApi.getDictByKeyVal(0, key, value);
        return getDictName(dictDtl);
    }

    public static String getDictName(JsSysDictData dictDtl) {
        if (null == dictDtl) {
            return "";
        }
        return dictDtl.getDictLabel();
    }

    public static String getDictName(Map<String, JsSysDictData> dictMap, String val) {
        JsSysDictData dictDtl = (JsSysDictData) MapUtils.getObject(dictMap, val);
        return getDictName(dictDtl);
    }

}
