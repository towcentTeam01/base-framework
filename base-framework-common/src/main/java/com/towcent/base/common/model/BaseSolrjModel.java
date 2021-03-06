/*
 * 文 件 名:  BaseSolrJModel.java
 * 版   权: Copyright www.face-garden.com Corporation 2013 版权所有
 * 描     述:  <描述>
 * 修 改 人:  shiwei
 * 修改时间:  2013-7-27
 * 跟踪单号: <跟踪单号>
 * 修改单号: <修改单号>
 * 修改内容: <修改内容>
 */
package com.towcent.base.common.model;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  shiwei
 * @version  [版本号, 2013-7-27]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class BaseSolrjModel implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    // 用来区分获取到的solr服务连接
    private String            solrAlias;

    public String getSolrAlias()
    {
        return solrAlias;
    }

    public void setSolrAlias(String solrAlias)
    {
        this.solrAlias = solrAlias;
    }

    public String toString()
    {
        try
        {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }
        catch (Exception e)
        {
        }
        return super.toString();
    }

    @Override
    public boolean equals(Object arg0)
    {
        StringBuffer methodName = new StringBuffer();
        Field[] srcfields = this.getClass().getDeclaredFields();
        Method method = null;
        Method method1 = null;
        for (Field field : srcfields)
        {
            if (field.getName().equals("serialVersionUID"))
            {
                continue;
            }
            try
            {
                methodName.delete(0, methodName.length());
                methodName.append("get");
                methodName.append(field.getName().substring(0, 1).toUpperCase());
                methodName.append(field.getName().substring(1));
                method = this.getClass().getMethod(methodName.toString());
                method1 = arg0.getClass().getMethod(methodName.toString());

                if (!method.getClass().equals(method1.getClass())) {
                    return false;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return true;
    }
}
