/**
 * 
 */
package com.towcent.base.common.spider;

import com.towcent.base.service.BaseService;
import org.springframework.stereotype.Service;

/**
 * 启动爬虫线程入口
 * @author shiwei
 *
 */
@Service
public class SpiderThread extends BaseService {

//	@Resource
//	private JsSysConfigService propertyService;
//
//	public void spiderThread(Integer merchantId){
//		// 查询数据库并启动执行爬虫命令
//		try {
//			String spiderService = propertyService.getSysPropertyToString(merchantId, "spider_service");
//
//			if(StringUtils.isNotEmpty(spiderService)){
//				String[] spiders = spiderService.split("#");
//				SpiderProcesser spiderProcesser = null;
//				for (String spider : spiders) {
//					if(StringUtils.isNotEmpty(spider)){
//						spiderProcesser = new SpiderProcesser();
//						spiderProcesser.setServiceId(spider);
//						spiderProcesser.spider();
//					}
//				}
//			}
//		} catch (ServiceException e) {
//			logger.error("获取爬虫执行分析对象失败", e);
//		}
//	}
}
