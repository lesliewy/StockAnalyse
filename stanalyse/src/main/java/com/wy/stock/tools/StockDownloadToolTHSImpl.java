/**
 * 
 */
package com.wy.stock.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wy.stock.domain.IndustryHot;
import com.wy.stock.domain.NotionHot;
import com.wy.stock.domain.NotionInfo;
import com.wy.stock.service.IndustryHotService;
import com.wy.stock.service.NotionHotService;
import com.wy.stock.service.NotionInfoService;
import com.wy.stock.utils.HttpUtils;
import com.wy.stock.utils.StockConstant;
import com.wy.stock.utils.StockUtils;

/**
 * 
 * @author leslie
 *
 */
public class StockDownloadToolTHSImpl implements StockDownloadToolTHS {

	private static Logger LOGGER = Logger.getLogger(StockDownloadToolTHSImpl.class
			.getName());
	
	private NotionHotService notionHotService;
	
	private IndustryHotService industryHotService;
	
	private NotionInfoService notionInfoService;
	
	private StockParseToolTHS stockParseToolTHS;
	
	/**
	 * 获取概念、行业热点页面的html文件, 用于解析获取所有概念、行业信息 记入 ST_NOTION_INFO ST_INDUSTRY_INFO,
	 *  同样也用于解析板块热点的第一页数据, 记入 ST_NOTION_HOT, ST_INDUSTRY_HOT.
	 * 概念: http://q.10jqka.com.cn/gn/
	 * 同行顺行业: http://q.10jqka.com.cn/thshy/   该文件用于解析获取总页数.
	 * @param savedDir
	 * @param type
	 */
	public void downloadBoardHotHtmlFiles(File savedDir, String type){
       String dirPath = savedDir.getAbsolutePath() + File.separatorChar;
    	if(!savedDir.exists()){
    		savedDir.mkdirs();
    	}
    	// 概念板块热点
    	if("NOTION".equalsIgnoreCase(type)){
        	File file =  new File(dirPath + StockConstant.NOTION_HOT_HTML_FILE);
        	if(file.exists() && file.length() > 100){
        		LOGGER.info(file.getAbsoluteFile() + " exists, return now...");
        		return;
        	}
        	String url = "http://q.10jqka.com.cn/gn/";
        	LOGGER.info(StockConstant.NOTION_HOT_HTML_FILE + " downloading");
        	try {
        		HttpUtils.httpDownload(url, "GB2312", 10 * 1000, file);
    		} catch (FileNotFoundException e) {
    			if(file.exists()){
    				file.delete();
    			}
    			LOGGER.error(e);
    		} catch (IOException e) {
    			if(file.exists()){
    				file.delete();
    			}
    			LOGGER.error(e);
    		}
    	}else if("INDUSTRY".equalsIgnoreCase(type)){
        	File file =  new File(dirPath + StockConstant.INDUSTRY_HOT_IDENTIFIER + ".html");
        	if(file.exists() && file.length() > 100){
        		LOGGER.info(file.getAbsoluteFile() + " exists, return now...");
        		return;
        	}
        	String url = "http://q.10jqka.com.cn/thshy/";
        	LOGGER.info(StockConstant.INDUSTRY_HOT_HTML_FILE + " downloading");
        	try {
        		HttpUtils.httpDownload(url, "GB2312", 10 * 1000, file);
    		} catch (FileNotFoundException e) {
    			if(file.exists()){
    				file.delete();
    			}
    			LOGGER.error(e);
    		} catch (IOException e) {
    			if(file.exists()){
    				file.delete();
    			}
    			LOGGER.error(e);
    		}
    	}
    }
	
	/**
	 * 获取概念、行业热点页面的排名靠后的文件，json格式。
	 * 概念: 首页html: http://q.10jqka.com.cn/stock/gn/   后续json: http://q.10jqka.com.cn/interface/stock/gn/zdf/desc/3/quote/quote
	 * 同行顺行业: 首页html: http://q.10jqka.com.cn/stock/thshy/
	 */
	public void downloadBoardHotJsonFiles(File savedDir, int totalPages, String type) {
		String dirPath = savedDir.getAbsolutePath() + File.separatorChar;
		String url = "";
		File file = null;
		if("NOTION".equalsIgnoreCase(type)){
			// 获取全部页面.
			for(int page = 2; page <= totalPages; page++){
				url = "http://q.10jqka.com.cn/interface/stock/gn/zdf/desc/" + page + "/quote/quote";
				file = new File(dirPath + "notionHot_" + page + ".json");
				
				try {
					/*
					 * 不明白某些时候获取的某些文件是乱码。这里认为行数大于1的文件都是乱码，删除掉.
					 */
					if(file.exists() && StockUtils.getTotalLines(file.getAbsolutePath()) > 1){
						LOGGER.info(file.getAbsolutePath() + " may be not valid, delete...");
						file.delete();
					}
					// 已经存在的不再重新下载.
					if(!StringUtils.isEmpty(url) && file != null && !file.exists()){
						HttpUtils.httpDownload(url, "GB2312", 10 * 1000, file);
					}
				} catch (FileNotFoundException e) {
					if(file.exists()){
						file.delete();
					}
					LOGGER.error(e);
				} catch (IOException e) {
					if(file.exists()){
						file.delete();
					}
					LOGGER.error(e);
				}
			}
		}else if("INDUSTRY".equalsIgnoreCase(type)){
			for(int page = 2; page <= totalPages; page++){
				url = "http://q.10jqka.com.cn/interface/stock/thshy/zdf/desc/" + totalPages + "/quote/quote";
				file = new File(dirPath + "industryHot_" + totalPages + ".json");
				
				try {
					/*
					 * 不明白某些时候获取的某些文件是乱码。这里认为行数大于1的文件都是乱码，删除掉.
					 */
					if(file.exists() && StockUtils.getTotalLines(file.getAbsolutePath()) > 1){
						LOGGER.info(file.getAbsolutePath() + " may be not valid, delete...");
						file.delete();
					}
					// 已经存在的不再重新下载.
					if(!StringUtils.isEmpty(url) && file != null && !file.exists()){
						HttpUtils.httpDownload(url, "GB2312", 10 * 1000, file);
					}
				} catch (FileNotFoundException e) {
					if(file.exists()){
						file.delete();
					}
					LOGGER.error(e);
				} catch (IOException e) {
					if(file.exists()){
						file.delete();
					}
					LOGGER.error(e);
				}
			}
		}
	}	

	/**
	 * 获取概念、行业热点页面的排名靠后的文件，html格式.
	 * 概念: 首页html: http://q.10jqka.com.cn/stock/gn/   后续: http://q.10jqka.com.cn/interface/stock/gn/zdf/desc/3/quote/quote
	 * 同行顺行业: 首页html: http://q.10jqka.com.cn/stock/thshy/  后续: http://q.10jqka.com.cn/thshy/index/field/199112/order/desc/page/2/ajax/1/
	 * @author leslie  
	 * @param savedDir
	 * @param totalPages
	 * @param type  
	 * @since 1.0.0
	 */
	public void downloadBoardHotHtmlFiles(File savedDir, int totalPages, String type) {
		String dirPath = savedDir.getAbsolutePath() + File.separatorChar;
		String url = "";
		File file = null;
		if("NOTION".equalsIgnoreCase(type)){
			// 获取全部页面.
			for(int page = 2; page <= totalPages; page++){
				url = "http://q.10jqka.com.cn/interface/stock/gn/zdf/desc/" + page + "/quote/quote";
				file = new File(dirPath + "notionHot_" + page + ".json");
				
				try {
					/*
					 * 不明白某些时候获取的某些文件是乱码。这里认为行数大于1的文件都是乱码，删除掉.
					 */
					if(file.exists() && StockUtils.getTotalLines(file.getAbsolutePath()) > 1){
						LOGGER.info(file.getAbsolutePath() + " may be not valid, delete...");
						file.delete();
					}
					// 已经存在的不再重新下载.
					if(!StringUtils.isEmpty(url) && file != null && !file.exists()){
						HttpUtils.httpDownload(url, "GB2312", 10 * 1000, file);
					}
				} catch (FileNotFoundException e) {
					if(file.exists()){
						file.delete();
					}
					LOGGER.error(e);
				} catch (IOException e) {
					if(file.exists()){
						file.delete();
					}
					LOGGER.error(e);
				}
			}
		}else if("INDUSTRY".equalsIgnoreCase(type)){
			for(int page = 1; page <= totalPages; page++){
				// http://q.10jqka.com.cn/thshy/index/field/199112/order/desc/page/2/ajax/1/
				url = "http://q.10jqka.com.cn/thshy/index/field/199112/order/desc/page/" + page + "/ajax/1/";
				file = new File(dirPath + "industryHot_" + page + ".html");
				
				try {
					// 已经存在的不再重新下载.
					if(!StringUtils.isEmpty(url) && file != null && !file.exists()){
						HttpUtils.httpDownload(url, "GB2312", 10 * 1000, file);
					}
				} catch (FileNotFoundException e) {
					if(file.exists()){
						file.delete();
					}
					LOGGER.error(e);
				} catch (IOException e) {
					if(file.exists()){
						file.delete();
					}
					LOGGER.error(e);
				}
			}
		}
	}
	
	/**
	 * 下载指数的html文件: http://q.10jqka.com.cn/stock/zs/, 用于解析记入 ST_INDEX.
	 */
	public void downloadIndexFiles() {
    	String commonUrl = "http://q.10jqka.com.cn/zs/detail/code/";
    	Map<String, String> indexCodeMap = StockUtils.getIndexCodeMap("job-T");
    	File file = null;
    	try {
    		for(String code : indexCodeMap.keySet()){
    			file =  new File(StockUtils.getDailyStockSaveDir("B") + "index_" + code + ".html");
    	    	if(file.exists() && file.length() > 100){
    	    		continue;
    	    	}
    	    	if(!file.getParentFile().exists()){
    	    		file.getParentFile().mkdirs();
    	    	}
    			HttpUtils.httpDownload(commonUrl + code, "GB2312", 10 * 1000, file);
    		}
		} catch (FileNotFoundException e) {
			if(file.exists()){
				file.delete();
			}
			LOGGER.error(e);
		} catch (IOException e) {
			if(file.exists()){
				file.delete();
			}
			LOGGER.error(e);
		}
	}
    
	/**
	 * 下载热点板块内的股票信息:  用于解析记入 ST_NOTION_HOT_STOCKS, ST_INDUSTRY_HOT_STOCKS
	 * 包含涨跌幅排名靠前的以及靠后的板块中的股票信息，
	 * json url 同花顺行业: http://q.10jqka.com.cn/interface/stock/detail/zdf/desc/2/1/jsjyy
	 * json url 同花顺概念: http://q.10jqka.com.cn/interface/stock/detail/zdf/desc/1/3/xgycxg
	 * @param date 日期，格式为 151020   和ST_JOB中的一样.
	 * @param type  NOTION  INDUSTRY
	 */
	public void downloadBoardHotStocksFiles(String date, String type) {
		String tradeDate = "20" + date.substring(0, 2) + "-" + date.substring(2, 4) + "-" + date.substring(4, 6);
		File dir = new File(StockUtils.getDailyStockSaveDir("B"));
		if(!dir.exists()){
			dir.mkdirs();
		}
		
		if("NOTION".equals(type)){
            // http://q.10jqka.com.cn/gn/detail/field/199112/order/desc/page/3/ajax/1/code/300220  返回的是html
			
			/*
			 * 下载所有的notionUrl页面
			 */
			downloadAllNotionUrlHtml();
            
			/*
			 * 解析notionUrl页面, 将数据插入ST_NOTION_HOT
			 */
			stockParseToolTHS.persistNotionIndustryHotFromStocksHtml(dir, tradeDate);
			
			/*
			 * 根据ST_NOTION_HOT 中的Rank，下载排名靠前和靠后的文件.
			 */
			downloadNotionHotListHtmls(tradeDate);
		}else if("INDUSTRY".equals(type)){
			// 先查询ST_INDUSTRY_HOT中热点板块, 关联 ST_INDUSTRY_INFO, 根据url来下载.
			List<IndustryHot> industryHotList = industryHotService.queryIndustryHotInfoByDate(tradeDate, StockConstant.THS_FLAG);
			if(industryHotList == null || industryHotList.isEmpty()){
				LOGGER.error("industryHotList is null or empty, return now...");
				return;
			}
			                 // http://q.10jqka.com.cn/thshy/detail/field/199112/order/desc/page/2/ajax/1/code/881156
			String commonUrl = "http://q.10jqka.com.cn/thshy/detail/field/199112/order/desc/page/";
			for(IndustryHot industryHot : industryHotList){
				int rank = industryHot.getRank();
				String industryName = industryHot.getIndustryName();
				String industryCode = industryHot.getIndustryCode();
				int pageSize = StockConstant.INDUSTRY_HOT_PAGE_SIZE;
				int corpsNum = industryHot.getCorpsNum();
				int a = corpsNum / pageSize;  // 取整
				float b = corpsNum / Float.valueOf(pageSize);  // 浮点数.
				String url1 = "";
				String url2 = "";
				File file1 = null;
				File file2 = null;
				// 第一页必须取
				if(rank <= StockConstant.INDUSTRY_HOT_PAGE_SIZE){
					url1 = commonUrl + "1" + "/ajax/1/code/" + industryCode;
					file1 = new File(StockUtils.getDailyStockSaveDir("B") + "industryHot_" + industryName + "_" + "1" + ".html");
					try {
						if(!StringUtils.isEmpty(url1) && !file1.exists()){
							HttpUtils.httpDownload(url1, "GB2312", 10 * 1000, file1);
						}
		    		} catch (FileNotFoundException e) {
		    			if(file1.exists()){
		    				file1.delete();
		    			}
		    			LOGGER.error(e);
		    		} catch (IOException e) {
		    			if(file1.exists()){
		    				file1.delete();
		    			}
		    			LOGGER.error(e);
		    		}
				}
				if(corpsNum <= pageSize){   // 板块包含的股票个数 <= pageSize, 只取第一页.
					url1 = commonUrl + "1" + "/ajax/1/code/" + industryCode;
					file1 = new File(StockUtils.getDailyStockSaveDir("B") + "industryHot_" + industryName + "_" + "1" + ".html");
				}else if(a == b){    // 刚好能被pageSize整除
					url1 = commonUrl + a + "/ajax/1/code/" + industryCode;
					file1 = new File(StockUtils.getDailyStockSaveDir("B") + "industryHot_" + industryName + "_"  + a + ".html");
					url2 = commonUrl + (a - 1) + "/ajax/1/code/" + industryCode;
					file2 = new File(StockUtils.getDailyStockSaveDir("B") + "industryHot_" + industryName + "_"  + (a - 1) + ".html");
				}else if( a < b){    // 不能被pageSize整除情况.
					url1 = commonUrl + (a + 1) + "/ajax/1/code/" + industryCode;
					file1 = new File(StockUtils.getDailyStockSaveDir("B") + "industryHot_" + industryName + "_"  + (a + 1) + ".html");
					url2 = commonUrl + a + "/ajax/1/code/" + industryCode;
					file2 = new File(StockUtils.getDailyStockSaveDir("B") + "industryHot_" + industryName + "_"  + a + ".html");
				}
				try {
					if(!StringUtils.isEmpty(url1) && !file1.exists()){
						HttpUtils.httpDownload(url1, "GB2312", 10 * 1000, file1);
					}
	        		if(!StringUtils.isEmpty(url2) && !file2.exists()){
	        			HttpUtils.httpDownload(url2, "GB2312", 10 * 1000, file2);
	        		}
	    		} catch (FileNotFoundException e) {
	    			if(file1.exists()){
	    				file1.delete();
	    			}
	    			if(file2.exists()){
	    				file2.delete();
	    			}
	    			LOGGER.error(e);
	    		} catch (IOException e) {
	    			if(file1.exists()){
	    				file1.delete();
	    			}
	    			if(file2.exists()){
	    				file2.delete();
	    			}
	    			LOGGER.error(e);
	    		}
			}
		}
	}
	
	private void downloadNotionHotListHtmls(String tradeDate){
		// 先查询ST_INDUSTRY_HOT中热点板块, 关联 ST_INDUSTRY_INFO, 根据url来下载.
		List<NotionHot> notionHotList = notionHotService.queryNotionHotInfoByDateStr(tradeDate, StockConstant.THS_NOTION_TYPE1, StockConstant.THS_FLAG);
		if(notionHotList == null || notionHotList.isEmpty()){
			LOGGER.error("notionHotList is null or empty, return now...");
			return;
		}
		
		File file = null;
		try {
			for(NotionHot notionHot : notionHotList){
				String notionName = notionHot.getNotionName();
				String notionCode = notionHot.getNotionCode();
				int rank = notionHot.getRank();
				if(rank > 50 && rank < 130){
					continue;
				}
				
				/*
				 * 某些情况，下载的5个列表页面是无效的，不包含数据，所以要排除这种情况。 
				 * 这里判断同一个notionName的5个文件大小都一样的话，就认为是无效的，删除掉.
				 */
				deleteInvalidFiles(tradeDate, notionName);
				
				/*
				 * 下载概念列表页面，获取前50条记录，每页10条.
				 */
				for(int page = 1; page <= 5; page++){
					String url = "http://q.10jqka.com.cn/gn/detail/field/199112/order/desc/page/" + page + "/ajax/1/code/" + notionCode;
					file = new File(StockUtils.getDailyStockSaveDir("B") + "notionHot_" + notionName + "_" + page + ".html");
						if(!StringUtils.isEmpty(url) && !file.exists()){
							HttpUtils.httpDownload(url, "GB2312", 10 * 1000, file);
						}
				}
			}
		}catch (FileNotFoundException e) {
			if(file.exists()){
				file.delete();
			}
			LOGGER.error(e);
		} catch (IOException e) {
			if(file.exists()){
				file.delete();
			}
			LOGGER.error(e);
		}
	}
	
	private void deleteInvalidFiles(String tradeDate, String notionName){
		com.wy.stock.utils.FileNameSelector selector = null;
		String dirPath = StockConstant.BOARD_HOT_FILE_PATH + tradeDate.substring(0, 4) + File.separatorChar + 
				tradeDate.substring(5, 7) + File.separatorChar + tradeDate.substring(8, 10) + File.separatorChar;
		File parent = new File(dirPath);
		if(! parent.exists()){
			LOGGER.error(dirPath + " not exists.");
			return;
		}
		
		selector = new com.wy.stock.utils.FileNameSelector(StockConstant.NOTION_HOT_IDENTIFIER + "_" + notionName + "_", ".html");
		File[] notionFiles = parent.listFiles(selector);
		if(notionFiles != null){
			int length = notionFiles.length;
			if(length > 4){
				/*
				 * 0.html结尾的不是列表文件, 不考虑; 遍历一次将其排除.
				 */
				ArrayList<File> files = new ArrayList<File>();
				for(File notionFile : notionFiles){
					String name = notionFile.getName();
					if(! name.endsWith("0.html")){
						files.add(notionFile);
					}
				}
				int index = 0;
				long preSize = files.get(0).length();
				long curSize = 0;
				for(File file : files){
					curSize = file.length();
					if(curSize != preSize){
						break;
					}
					preSize = file.length();
					index++;
				}
				/*
				 * 删除文件
				 */
				if(index == files.size()){
					for(File notionFile : files){
						notionFile.delete();
					}
					LOGGER.info("delete files, prefix: " + selector.getPrefix() + "; post: " + selector.getPost());
				}
			}
		}
	}
	
	private void downloadAllNotionUrlHtml(){
		/*
		 *  查询ST_NOTION_INFO
		 */
		List<NotionInfo> notionInfoList = notionInfoService.queryNotionInfoByType(StockConstant.THS_NOTION_TYPE1,  StockConstant.THS_FLAG);
		if(notionInfoList == null || notionInfoList.isEmpty()){
			LOGGER.error("notionInfoList is null or empty, return now...");
			return;
		}
		
		/*
		 * 下载所有的notionUrl对应的页面，解析并存入ST_NOTION_HOT
		 */
		File file = null;
		try {
		   for(NotionInfo info : notionInfoList){
			   	String notionUrl = info.getNotionUrl();
				String notionName = info.getNotionName();
				
				file = new File(StockUtils.getDailyStockSaveDir("B") + "notionHot_" + notionName + "_" + "0" + ".html");
				if(!StringUtils.isEmpty(notionUrl) && !file.exists()){
					HttpUtils.httpDownload(notionUrl, "GBK", 10 * 1000, file);
				}
			}
		}catch (FileNotFoundException e) {
			if(file.exists()){
				file.delete();
			}
			LOGGER.error(e);
		} catch (IOException e) {
			if(file.exists()){
				file.delete();
			}
			LOGGER.error(e);
		}
	}

	public NotionHotService getNotionHotService() {
		return notionHotService;
	}

	public void setNotionHotService(NotionHotService notionHotService) {
		this.notionHotService = notionHotService;
	}

	public IndustryHotService getIndustryHotService() {
		return industryHotService;
	}

	public void setIndustryHotService(IndustryHotService industryHotService) {
		this.industryHotService = industryHotService;
	}

	public NotionInfoService getNotionInfoService() {
		return notionInfoService;
	}

	public void setNotionInfoService(NotionInfoService notionInfoService) {
		this.notionInfoService = notionInfoService;
	}

	public StockParseToolTHS getStockParseToolTHS() {
		return stockParseToolTHS;
	}

	public void setStockParseToolTHS(StockParseToolTHS stockParseToolTHS) {
		this.stockParseToolTHS = stockParseToolTHS;
	}
	
}
