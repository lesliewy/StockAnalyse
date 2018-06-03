/**
 * Project Name:stanalyse  
 * File Name:StockParseToolDFCF.java  
 * Package Name:com.wy.stock.tools  
 * Date:Mar 19, 2018 9:29:51 PM  
 * Copyright (c) 2018, wy All Rights Reserved.  
 *  
 */
package com.wy.stock.tools;

import java.io.File;

import com.wy.stock.service.ExchangeInfoService;
import com.wy.stock.utils.StockConstant;
import com.wy.stock.utils.StockUtils;

/**
 * date: Mar 19, 2018 9:29:51 PM <br/>  
 *  
 * @author leslie  
 * @version   
 * @since version 1.0  
 */
public class StockParseToolDFCF {

	private StockDownloadTool stockDownloadTool;
	/**
	 * 更新ST_NOTION_INFO
	 * @author leslie    
	 * @since 1.0.0
	 */
	public void persistNotionInfo(){
		// 先下载json, http://quote.eastmoney.com/centerv2/hsbk/gnbk  页面中的json请求.
		stockDownloadTool.downloadBKJsonDFCF(StockConstant.NOTION_IDENTIFIER);
		File dir = new File(StockUtils.getDailyStockSaveDir("A"));
		com.wy.stock.utils.FileNameSelector selector = new com.wy.stock.utils.FileNameSelector("notion_dfcf_", ".json");
		File[] files = dir.listFiles(selector);
		if(files != null && files.length > 0){
			for(File file : files){
				
			}
		}
		
	}
}
