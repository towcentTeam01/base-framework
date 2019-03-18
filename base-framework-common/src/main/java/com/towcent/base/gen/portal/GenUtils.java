package com.towcent.base.gen.portal;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.towcent.base.common.mapper.JaxbMapper;
import com.towcent.base.gen.entity.GenParamEntry;
import com.towcent.base.gen.portal.vo.ApiDocIn;
import com.towcent.base.gen.portal.vo.ApiDocObj;
import com.towcent.base.gen.portal.vo.ApiDocObjProperty;
import com.towcent.base.gen.portal.vo.ApiDocOutProperty;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 代码生成工具类
 */
public class GenUtils {

    private static Logger logger = LoggerFactory.getLogger(GenUtils.class);

    /**
     * 获取工程路径
     *
     * @return
     */
    public static String getProjectPath() {
        String projectPath = "";
        try {
            File file = new DefaultResourceLoader().getResource("").getFile();
            if (file != null) {
                while (true) {
                    File f = new File(file.getPath() + File.separator + "src" + File.separator + "main");
                    if (f == null || f.exists()) {
                        break;
                    }
                    if (file.getParentFile() != null) {
                        file = file.getParentFile();
                    } else {
                        break;
                    }
                }
                projectPath = file.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return projectPath;
    }

    /**
     * XML文件转换为对象
     *
     * @param fileName
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T fileToObject(String fileName, Class<?> clazz) {
        try {
            String pathName = "/portalTemplate/" + fileName;
            Resource resource = new ClassPathResource(pathName);
            InputStream is = resource.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line).append("\r\n");
            }
            if (is != null) {
                is.close();
            }
            if (br != null) {
                br.close();
            }
            return (T) JaxbMapper.fromXml(sb.toString(), clazz);
        } catch (IOException e) {
            logger.warn("Error file convert: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 首字母大写
     *
     * @param str
     * @return
     */
    public static String firstUpperCase(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String getSimpleJavaType(String fieldType) {
        if (StringUtils.isBlank(fieldType)) {
            return "String";
        }
        if ("Date".equalsIgnoreCase(fieldType)) {
            return "Date";
        }
        if ("String".equalsIgnoreCase(fieldType)) {
            return "String";
        }
        if ("int".equalsIgnoreCase(fieldType) || "integer".equalsIgnoreCase(fieldType)) {
            return "Integer";
        }
        if ("double".equalsIgnoreCase(fieldType)) {
            return "BigDecimal";
        }
        if ("list".equalsIgnoreCase(fieldType)) {
            return "List";
        }
        if ("boolean".equalsIgnoreCase(fieldType)) {
            return "Boolean";
        }
        if ("long".equalsIgnoreCase(fieldType)) {
            return "Long";
        }
        return "String";
    }

    public static Set<String> getGenParamImportList(List<GenParamEntry> list, Map<String, ApiDocObj> docObjMap) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }

        Set<String> importList = Sets.newHashSet();
        for (GenParamEntry genParamEntry : list) {
            if ("BigDecimal".equals(genParamEntry.getSimpleJavaType())) {
                importList.add("java.math.BigDecimal");
            }
            if ("Date".equals(genParamEntry.getSimpleJavaType())) {
                importList.add("java.util.Date");
                importList.add("com.fasterxml.jackson.annotation.JsonFormat");
            }
            if ("List".equals(genParamEntry.getSimpleJavaType())) {
                importList.add("java.util.List");
            }
            if (genParamEntry.getObjId() != null) {
                // 所关联的对象不为空
                ApiDocObj docObj = docObjMap.get(genParamEntry.getObjId() + "");
                if (null != docObj) {
                    importList.add(docObj.getImportStr());
                }
            }
        }

        return importList;
    }

    private static boolean isNotBaseField(String fieldType) {
        if (StringUtils.isBlank(fieldType)) {
            return false;
        }
        if ("list".equalsIgnoreCase(fieldType)) {
            return true;
        }
        return false;
    }

    /**
     * 通过接口文档定义的请求参数列表, 构造成代码生成所需的属性列表
     * @param apiDocIns 接口请求参数列表
     * @return {@link GenParamEntry}
     */
    public static List<GenParamEntry> getGenParamEntrys(List<ApiDocIn> apiDocIns) {
        List<GenParamEntry> list = Lists.newArrayList();
        if (CollectionUtils.isEmpty(apiDocIns)) {
            return list;
        }

        for (ApiDocIn docIn : apiDocIns) {
            GenParamEntry entry = new GenParamEntry();
            entry.setDescription(docIn.getParamDesc());
            entry.setFieldName(docIn.getParamName());
            entry.setFieldType(docIn.getParamType());
            entry.setIsNotNull(StringUtils.equals("1", docIn.getRequired()) ? "Y" : "N");
            entry.setNotBaseField(isNotBaseField(docIn.getParamType()));
            entry.setSimpleJavaType(getSimpleJavaType(docIn.getParamType()));
            entry.setExample(docIn.getExample());
            if (StringUtils.equalsIgnoreCase(docIn.getParamType(), "Date")) {
                entry.setOutFormat(docIn.getRegular());
            }
            list.add(entry);
        }
        return list;
    }

    /**
     * 通过接口文档定义的响应参数列表, 构造成代码生成所需的属性列表
     * @param outProperties 接口响应参数列表
     * @return {@link GenParamEntry}
     */
    public static List<GenParamEntry> getGenParamOutEntrys(List<ApiDocOutProperty> outProperties, Map<String, ApiDocObj> docObjMap) {
        List<GenParamEntry> list = Lists.newArrayList();
        if (CollectionUtils.isEmpty(outProperties)) {
            return list;
        }

        for (ApiDocOutProperty docIn : outProperties) {
            GenParamEntry entry = new GenParamEntry();
            entry.setDescription(docIn.getFieldDesc());
            entry.setFieldName(docIn.getFieldName());
            entry.setFieldType(docIn.getFieldType());
            entry.setIsNotNull("N");
            entry.setNotBaseField(isNotBaseField(docIn.getFieldType()));
            entry.setSimpleJavaType(getSimpleJavaType(docIn.getFieldType()));
            entry.setOutFormat(docIn.getOutFormat());
            entry.setExample(docIn.getExample());
            if (null != docIn.getObjId()) {
                entry.setObjId(docIn.getObjId() + "");
                ApiDocObj obj = docObjMap.get(docIn.getObjId() + "");
                entry.setSimpleJavaType(firstUpperCase(obj.getAlias()));
            }
            list.add(entry);
        }
        return list;
    }

    /**
     * 通过对象的属性列表，构造代码生成需要的参数属性列表
     * @param objProperties 对象所包含的属性列表
     * @param docObjMap 此接口包含的所有对象映射表
     * @return {@link GenParamEntry}
     */
    public static List<GenParamEntry> getGenObjectOutEntrys(List<ApiDocObjProperty> objProperties, Map<String, ApiDocObj> docObjMap) {
        List<GenParamEntry> list = Lists.newArrayList();
        if (CollectionUtils.isEmpty(objProperties)) {
            return list;
        }

        for (ApiDocObjProperty docIn : objProperties) {
            GenParamEntry entry = new GenParamEntry();
            entry.setDescription(docIn.getFieldDesc());
            entry.setFieldName(docIn.getFieldName());
            entry.setFieldType(docIn.getFieldType());
            entry.setIsNotNull("N");
            entry.setNotBaseField(isNotBaseField(docIn.getFieldType()));
            entry.setSimpleJavaType(getSimpleJavaType(docIn.getFieldType()));
            entry.setOutFormat(docIn.getOutFormat());
            entry.setExample(docIn.getExample());
            if (null != docIn.getRelevanceObjId()) {
                entry.setObjId(docIn.getRelevanceObjId() + "");
                ApiDocObj obj = docObjMap.get(docIn.getRelevanceObjId() + "");
                entry.setSimpleJavaType(firstUpperCase(obj.getAlias()));
            }
            list.add(entry);
        }
        return list;
    }

}
