package com.towcent.base.gen.portal;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.towcent.base.common.utils.BaseHttpClient;
import com.towcent.base.common.utils.FileUtils;
import com.towcent.base.common.utils.FreeMarkers;
import com.towcent.base.gen.entity.GenParamEntry;
import com.towcent.base.gen.entity.GenTemplate;
import com.towcent.base.gen.portal.vo.*;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * portal接口新生成工具
 */
public class PortalGenTools {

    private static Logger logger = LoggerFactory.getLogger(PortalGenTools.class);

    private static final String VOID = "void";

    /** 生成的对象(.java)集合 */
    private static List<String> loopList = Lists.newArrayList();

    /**
     * 批量生成代码
     * <code>
     *    String urlPath = "http://localhost:8980/js/a/api/apiDocMain/getInterfaceDetail?projectName={0}&interfaceNo={1}";
     *    String projectName = "牡丹服务";
     *    String interfaceNo = "1.0.1,1.0.2";
     * </code>
     * @param urlPath
     * @param projectName
     * @param interfaceNos
     */
    public static void batchCreateCode(String urlPath, String projectName, String interfaceNos) {
        String[] ints = StringUtils.split(interfaceNos, ",");
        for (String intf : ints) {
            parseAndCreateCode(urlPath, projectName, intf);
        }
    }

    /**
     * <code>
     *     String urlPath = "http://localhost:8980/js/a/api/apiDocMain/getInterfaceDetail?projectName={0}&interfaceNo={1}";
     *     String projectName = "牡丹服务";
     *     String interfaceNo = "1.0.1";
     * </code>
     *
     * @param urlPath     获取接口定义的链接地址
     * @param projectName 项目名称
     * @param interfaceNo 接口编号
     */
    public static void parseAndCreateCode(String urlPath, String projectName, String interfaceNo) {
        String json = getInterfaceJson(urlPath, projectName, interfaceNo);

        InterfaceVo vo = JSON.parseObject(json, InterfaceVo.class);

        // 1. 处理需要生成的模板
        List<GenTemplate> tempList = getGenTemplateList(vo);
        Map<String, Object> model = buildGenInterfaceData(vo);
        // 2. 批量生成文件 (接口)
        for (GenTemplate tpl : tempList) {
            generateToFile(tpl, model);
        }
        // 3. 另外生成对象Vo
        List<ApiDocObj> objs = (List<ApiDocObj>) model.get("secOutParamList");
        Map<String, ApiDocObj> docObjMap = (Map<String, ApiDocObj>) model.get("secOutParamMap");
        if (!CollectionUtils.isEmpty(objs)) {
            for (ApiDocObj obj : objs) {
                // 1. 构造属性列表
                List<GenParamEntry> paramEntries = GenUtils.getGenObjectOutEntrys(obj.getPropertyList(), docObjMap);
                // 2. 构造属性import列表
                Set<String> importSet = GenUtils.getGenParamImportList(paramEntries, docObjMap);
                // 3. 构造其他
                model.put("secOutParamList", paramEntries);
                model.put("secOutImportList", importSet);
                model.put("ObjName", GenUtils.firstUpperCase(obj.getAlias()));
                model.put("objDesc", obj.getFieldDesc());
                model.put("moduleName", obj.getModulesName());

                generateToFile(getGenParamOutSecTemplate(), model);
            }
        }
        System.out.println(vo);
    }

    /**
     * 通过远程接口获取所有接口定义的属性
     * @param urlPath     获取接口定义数据的地址
     * @param projectName 项目名称  例如: 牡丹服务
     * @param interfaceNo 接口编码  例如: 1.0.1
     * @return
     */
    public static String getInterfaceJson(String urlPath, String projectName, String interfaceNo) {
        try {
            String url = MessageFormat.format(urlPath, projectName, interfaceNo);
            String json = BaseHttpClient.httpGet(url);
            return json;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     *  将接口属性转换成生成代码所需的属性
     * @param interfaceVo 接口文档定义的数据集合
     * @return
     */
    public static Map<String, Object> buildGenInterfaceData(InterfaceVo interfaceVo) {
        Map<String, Object> model = Maps.newHashMap();
        // 1. 项目
        ApiProject apiProject = interfaceVo.getApiProject();
        if (null != apiProject) {
            // 基础包名
            model.put("packageName", apiProject.getProjectPackage());
            // 作者
            model.put("functionAuthor", "GenTools");
            // 版本号
            model.put("functionVersion", "0.0.1");
        }

        // 2. 接口
        ApiDocMain apiDocMain = interfaceVo.getApiDocMain();
        if (null != apiDocMain) {
            // 接口编号
            model.put("interNo", apiDocMain.getInterfaceNo());
            // 接口名称
            model.put("interName", apiDocMain.getInterfaceName());
            // 接口功能简述
            model.put("funDesc", apiDocMain.getInterfaceDesc());
            // 接口请求类型(POST|GET)
            model.put("reqType", apiDocMain.getRequestType());
            // 接口URL
            model.put("interUrl", apiDocMain.getInterfaceUrl());
            // 是否需要登录 1:需要
            model.put("isLogin", apiDocMain.getIsLogin());

            String[] urls = StringUtils.split(apiDocMain.getInterfaceUrl(), "/");
            // 模块名
            model.put("moduleName", urls[0]);
            // 小写类名
            model.put("className", urls[1]);
            // 类名(首字母大写)
            model.put("ClassName", GenUtils.firstUpperCase(urls[1]));
            // 方法名
            model.put("functionName", urls[2]);
            // 方法名(首字母大写)
            model.put("FunctionName", GenUtils.firstUpperCase(urls[2]));
        }

        // 3. 入参
        List<ApiDocIn> apiDocIns = interfaceVo.getApiDocIns();
        List<GenParamEntry> paramIns = GenUtils.getGenParamEntrys(apiDocIns);
        model.put("enParamList", paramIns);
        model.put("enImportList", GenUtils.getGenParamImportList(paramIns, null));

        // 4. 出参
        ApiDocOut docOut = interfaceVo.getApiDocOut();
        if (StringUtils.equalsIgnoreCase(VOID, docOut.getFieldType())) {
            return model;
        }

        // 4.1 对象列表
        List<ApiDocObj> objs = interfaceVo.getApiDocObjs();
        Map<String, ApiDocObj> docObjMap = Maps.newHashMap();
        if (!CollectionUtils.isEmpty(objs)) {
            for (ApiDocObj docObj : objs) {
                // 组装对象的全类名
                docObj.setImportStr(apiProject.getProjectPackage() + "." + docObj.getModulesName()
                        + ".vo.obj." + GenUtils.firstUpperCase(docObj.getAlias()));
                docObjMap.put(docObj.getId(), docObj);
            }
            model.put("secOutParamList", objs);
            model.put("secOutParamMap", docObjMap);
        }

        List<ApiDocOutProperty> outProperties = interfaceVo.getOutProperties();
        List<GenParamEntry> paramOuts = GenUtils.getGenParamOutEntrys(outProperties, docObjMap);
        model.put("outParamType", StringUtils.lowerCase(docOut.getFieldType()));
        model.put("outParamList", paramOuts);
        model.put("outImportList", GenUtils.getGenParamImportList(paramOuts, docObjMap));

        return model;
    }

    /**
     * 通过接口定义的数据筛选生成代码所需的模板集合
     * @param vo 接口定义的数据集合
     * @return
     */
    public static List<GenTemplate> getGenTemplateList(InterfaceVo vo) {
        List<GenTemplate> list = Lists.newArrayList();
        list.add((GenTemplate) GenUtils.fileToObject("controller.xml", GenTemplate.class));
        if (!CollectionUtils.isEmpty(vo.getApiDocIns())) {
            list.add((GenTemplate) GenUtils.fileToObject("paramIn.xml", GenTemplate.class));
        }
        if (!StringUtils.equalsIgnoreCase(VOID, vo.getApiDocOut().getFieldType())) {
            list.add((GenTemplate) GenUtils.fileToObject("paramOut.xml", GenTemplate.class));
        }
        list.add((GenTemplate) GenUtils.fileToObject("service.xml", GenTemplate.class));
        list.add((GenTemplate) GenUtils.fileToObject("serviceImpl.xml", GenTemplate.class));
        list.add((GenTemplate) GenUtils.fileToObject("test.xml", GenTemplate.class));
        return list;
    }

    /**
     * 生成到文件
     * @param tpl     文件模板
     * @param model   生成所需的数据集合
     * @return
     */
    public static String generateToFile(GenTemplate tpl, Map<String, Object> model) {
        // 获取生成文件
        String fileName = GenUtils.getProjectPath() + File.separator
                + StringUtils.replaceEach(FreeMarkers.renderString(tpl.getFilePath() + "/", model),
                new String[] { "//", "/", "." },
                new String[] { File.separator, File.separator, File.separator })
                + FreeMarkers.renderString(tpl.getFileName(), model);
        logger.debug("待生成的文件 fileName === " + fileName);
        try {
            // 如果是实体类，则先删除
            if (StringUtils.equalsIgnoreCase("paramIn", tpl.getName()) || StringUtils.equalsIgnoreCase("paramOut", tpl.getName())
                || StringUtils.equalsIgnoreCase("paramOutSec", tpl.getName())) {
                FileUtils.deleteFile(fileName);
            }

            File file = new File(fileName);

            //自定义导入模块
            String tempIn = "";
            String tempOut = "";
            //自定义方法体模块
            String temp2 = "";
            //自定义测试模块
            String temp3 = "";

            Map<String, String> contentMap = getFileMainContent(tpl, file, model);
            // 文件主体部分
            String fileMainContent = MapUtils.getString(contentMap, "fileMainContent");
            // 文件模块部分
            String fileModelContent = MapUtils.getString(contentMap, "fileModelContent");
            logger.debug(" fileMainContent === \r\n" + fileMainContent);
            logger.debug(" fileModelContent === \r\n" + fileModelContent);

            // 获取相应模块的字符串
            if (StringUtils.isNotBlank(fileModelContent)) {
                tempIn = fileModelContent.substring(0, fileModelContent.indexOf(";") + 1) + "\n";
                if (model.get("outParamList") != null && tpl.getFileName().endsWith("ServiceImpl.java")){
                    tempOut = fileModelContent.substring(fileModelContent.indexOf("In;") + 3, fileModelContent.indexOf("Out;") + 5);
                }

                if (tpl.getFileName().endsWith("ControllerTest.java")) {
                    temp2 = fileModelContent.substring(fileModelContent.indexOf("@Test") - 2);
                    temp3 = fileModelContent.substring(fileModelContent.indexOf("descMap.") - 2, fileModelContent.indexOf(");") + 3);
                } else {
                    temp2 = StringUtils.substringAfter(fileModelContent, "||");
                }
            }

            // 创建并写入文件
            if (!file.exists()) {
                FileUtils.createFile(fileName);
                FileUtils.writeToFile(fileName, fileMainContent, true);
            }

            // 导入模块  -- 入参
            if (StringUtils.isNotBlank(tempIn)) {
                FileUtils.writeToFile(file, tempIn, fileMainContent.indexOf("import"));
            }
            // 导入出参
            if (StringUtils.isNotBlank(tempOut)) {
                FileUtils.writeToFile(file, tempOut, fileMainContent.indexOf("import"));
            }

            // 方法体模块
            if (StringUtils.isNotBlank(temp2)) {
                File nFile = new File(fileName);
                FileUtils.writeToFile(nFile, temp2 + "\n", nFile.length() - 1);
            }

            // 测试用例模块
            if (StringUtils.isNotBlank(temp3)) {
                File nFile = new File(fileName);
                fileMainContent = FileUtils.readFileToString(nFile, "utf-8");
                int pos = fileMainContent.indexOf("descMap.")-2;
                if(pos < 0){
                    pos = fileMainContent.indexOf("static {")+9;
                }
                FileUtils.writeToFile(nFile, temp3, pos);
            }

            // 追加额外的 import 出参类型
            String outParamType = MapUtils.getString(model, "outParamType");
            if (tpl.getFileName().endsWith("ServiceImpl.java") && outParamType != null) {
                File nFile = new File(fileName);
                String importStr = "";
                if (StringUtils.equalsIgnoreCase(outParamType, "page")) {
                    importStr = "import com.towcent.base.common.page.PaginationDto;\nimport com.towcent.base.common.page.SimplePageDto;\n";
                } else if(StringUtils.equalsIgnoreCase(outParamType, "list")) {
                    importStr = "import java.util.List;\nimport com.google.common.collect.Lists;\n";
                }
                if (StringUtils.isNotBlank(importStr)) {
                    FileUtils.writeToFile(nFile, importStr, fileMainContent.indexOf("import"));
                }
            }

            logger.debug(" file create === " + fileName);
            return "生成成功：" + fileName + "<br/>";
        } catch (IOException e) {
            logger.debug("===生成失败");
        }
        return "生成失败：" + fileName + "<br/>";
    }

    private static GenTemplate getGenBaseControllerTemplate() {
        return (GenTemplate) GenUtils.fileToObject("baseController.xml", GenTemplate.class);
    }
    private static GenTemplate getGenBaseServiceTemplate() {
        return (GenTemplate) GenUtils.fileToObject("baseService.xml", GenTemplate.class);
    }
    private static GenTemplate getGenBaseServiceImplTemplate() {
        return (GenTemplate) GenUtils.fileToObject("baseServiceImpl.xml", GenTemplate.class);
    }
    private static GenTemplate getGenBaseTestTemplate() {
        return (GenTemplate) GenUtils.fileToObject("baseTest.xml", GenTemplate.class);
    }
    private static GenTemplate getGenParamOutSecTemplate() {
        return (GenTemplate) GenUtils.fileToObject("paramOutSec.xml", GenTemplate.class);
    }

    /**
     * 得到一个文件的主体部分<br/>
     *  1. 如果文件不存在, 则读取模板<br/>
     *  2. 存在，则直接读取原文件<br/>
     *  fileMainContent   文件主体部分<br/>
     *  fileModelContent  文件模块部分<br/>
     * @param tpl    文件模板对象
     * @param file   需要写入的文件
     * @param model  需要填充的数据
     * @return
     */
    private static Map<String, String> getFileMainContent(GenTemplate tpl, File file,
                                             Map<String, Object> model) {
        try {
            // 文件主体部分
            String fileMainContent = "";
            // 文件模块部分
            String fileModelContent = "";

            Map<String, String> contentMap = Maps.newHashMap();
            fileModelContent = tpl.getContent();
            if (tpl.getFileName().endsWith("Controller.java")) {
                fileMainContent = getGenBaseControllerTemplate().getContent();
            } else if (tpl.getFileName().endsWith("Service.java")) {
                fileMainContent = getGenBaseServiceTemplate().getContent();
            } else if (tpl.getFileName().endsWith("ServiceImpl.java")) {
                fileMainContent = getGenBaseServiceImplTemplate().getContent();
            } else if (tpl.getFileName().endsWith("ControllerTest.java")) {
                fileMainContent = getGenBaseTestTemplate().getContent();
            } else {
                fileMainContent = tpl.getContent();
                fileModelContent = null;
            }

            if (StringUtils.isNotBlank(fileModelContent)) {
                fileModelContent = FreeMarkers.renderString(StringUtils.trimToEmpty(fileModelContent), model);
            }

            if (file.exists()) {
                // 如果文件存在，则直接读取
                fileMainContent = FileUtils.readFileToString(file, "utf-8");
            } else {
                fileMainContent = FreeMarkers.renderString(StringUtils.trimToEmpty(fileMainContent), model);
            }
            contentMap.put("fileModelContent", fileModelContent);
            contentMap.put("fileMainContent", fileMainContent);
            return contentMap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String urlPath = "http://localhost:8980/js/a/api/apiDocMain/getInterfaceDetail?projectName={0}&interfaceNo={1}";
        // parseAndCreateCode();
    }

}
