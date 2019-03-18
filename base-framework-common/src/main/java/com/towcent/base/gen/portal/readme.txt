1. 新版生成代码工具

import com.towcent.base.gen.portal.PortalGenTools;
import org.apache.commons.lang3.StringUtils;

import java.util.ResourceBundle;

/**
 * 新生成Portal接口代码工具类
 * 依赖base-framework:1.5.2-SNAPSHOT版本
 */
public class GenToolMain {

    public static void main(String[] args) {
        System.out.println(StringUtils.center("生成开始", 30, "="));
        /**
        gen.url.path = "http://localhost:8980/js/a/api/apiDocMain/getInterfaceDetail?projectName={0}&interfaceNo={1}"
        gen.project.name = "智租充电"
        gen.interface.nos = 1.0.1,1.0.2
        */
        ResourceBundle resource = ResourceBundle.getBundle("application");
        String urlPath = resource.getString("gen.url.path");
        String projectName = resource.getString("gen.project.name");
        String interfaceNos = resource.getString("gen.interface.nos");

        // 调用生成代码的方法
        PortalGenTools.batchCreateCode(urlPath, projectName, interfaceNos);

        System.out.println(StringUtils.center("生成完毕", 30, "="));
    }
}