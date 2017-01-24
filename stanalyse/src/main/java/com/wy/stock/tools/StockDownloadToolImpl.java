/**
 * 
 */
package com.wy.stock.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import com.wy.stock.domain.IndustryHot;
import com.wy.stock.domain.IndustryInfo;
import com.wy.stock.domain.NotionHot;
import com.wy.stock.domain.NotionInfo;
import com.wy.stock.domain.StockInfo;
import com.wy.stock.service.IndustryHotService;
import com.wy.stock.service.IndustryInfoService;
import com.wy.stock.service.NotionHotService;
import com.wy.stock.service.NotionInfoService;
import com.wy.stock.service.StockInfoService;
import com.wy.stock.utils.HttpUtils;
import com.wy.stock.utils.StockConstant;
import com.wy.stock.utils.StockUtils;

/**
 * 
 * @author leslie
 *
 */
public class StockDownloadToolImpl implements StockDownloadTool {

	private static Logger LOGGER = Logger.getLogger(StockDownloadToolImpl.class
			.getName());
	
	private StockInfoService stockInfoService;
	
	private NotionInfoService notionInfoService;
	
	private IndustryInfoService industryInfoService;
	
	private NotionHotService notionHotService;
	
	private IndustryHotService industryHotService;
	
    /**
     * 获取股票历史数据。 参考: http://www.cnblogs.com/seacryfly/articles/stock.html
     * 并存入指定的目录中.
     * 
     * @param savedDir 保存csv文件的目录.
     */
    public void downloadCsvFiles(File savedDir){
    	// 查询所有的股票信息.
    	List<StockInfo> allStocks = stockInfoService.queryAllStockInfo();
    	if(allStocks == null || allStocks.isEmpty()){
    		LOGGER.info("allStocks is empty, return now...");
    		return;
    	}
    	String dirPath = savedDir.getAbsolutePath() + File.separatorChar;
    	if(!savedDir.exists()){
    		savedDir.mkdirs();
    	}
    	String stockStr = "";
    	int num = 0;
    	for(StockInfo stock : allStocks){
    		if("上证".equals(stock.getExchange())){
    			stockStr = stock.getCode() + ".ss";
    		}else if("深证".equals(stock.getExchange())){
    			stockStr = stock.getCode() + ".sz";
    		}
        	String url = StockConstant.STOCK_CSV_URL_PRE + stockStr;
        	File file =  new File(dirPath + stockStr + ".csv");
        	// 存在且非空的不需要重新下载.
        	if(file.exists() && file.length() > 100){
        		continue;
        	}
        	LOGGER.info(num++ + " download " + stock.getExchange() + " " + stock.getCode() + " " + stock.getName() + " " + stock.getType());
        	try {
    			HttpUtils.httpDownload(url, new FileOutputStream(file));
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
     * 获取概念板块热点数据。http://quote.eastmoney.com/hq2data/bk/data/notion.js
     * 并存入指定的目录中.
     * 
     * @param savedDir 保存文件的目录.
     * @param type
     * 
     */
    public void downloadBoardHotFiles(File savedDir, String type){
    	String dirPath = savedDir.getAbsolutePath() + File.separatorChar;
    	if(!savedDir.exists()){
    		savedDir.mkdirs();
    	}
    	// 概念板块热点
    	if("NOTION".equalsIgnoreCase(type)){
        	File file =  new File(dirPath + "notionHot.json");
        	if(file.exists() && file.length() > 100){
        		LOGGER.info(file.getAbsoluteFile() + " exists, return now...");
        		return;
        	}
        	String url = "http://quote.eastmoney.com/hq2data/bk/data/notion.js";
        	LOGGER.info("notionHot downloading");
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
        	File file =  new File(dirPath + "industryHot.json");
        	if(file.exists() && file.length() > 100){
        		LOGGER.info(file.getAbsoluteFile() + " exists, return now...");
        		return;
        	}
        	// String url = "http://quote.eastmoney.com/center/BKList.html#trade_0_0?sortRule=0";
        	String url = "http://quote.eastmoney.com/hq2data/bk/data/trade.js";
        	LOGGER.info("industryHot downloading");
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
	 * 下载热点板块内的股票信息:  http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/index.aspx?type=s&sortType=C&sortRule=-1&pageSize=20&page=1&jsName=quote_123&style=28003637
	 * 
	 * @param date 日期，格式为 151020   和ST_JOB中的一样.
	 * @param type  NOTION  INDUSTRY
	 */
	public void downloadBoardHotStocksFiles(String date, String type) {
		String tradeDate = "20" + date.substring(0, 2) + "-" + date.substring(2, 4) + "-" + date.substring(4, 6);
		if("NOTION".equals(type)){
			// 先查询ST_NOTION_HOT中热点板块, 关联 ST_NOTION_INFO
			List<NotionHot> notionHotList = notionHotService.queryNotionHotInfoByDateStr(tradeDate, StockConstant.THS_NOTION_TYPE1, StockConstant.DFCF_FLAG);
			if(notionHotList == null || notionHotList.isEmpty()){
				LOGGER.error("notionHotList is null or empty, return now...");
				return;
			}
			
			int limit = 0;
			// http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/index.aspx?type=s&sortType=C&sortRule=-1&pageSize=20&page=1&jsName=quote_123&style=28003610
			String preNotionUrl = "http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/index.aspx?type=s&sortType=C&sortRule=-1&pageSize=" + StockConstant.NOTION_HOT_STOCKS_UPPER + "&page=1&jsName=quote_123&style=";
			for(NotionHot notionHot : notionHotList){
				limit++;
				if(limit > StockConstant.NOTION_HOT_UPPER){
					break;
				}
				String notionCode = notionHot.getNotionUrl().substring(10, 18);
				String notionHotStockUrl = preNotionUrl + notionCode;
				File file =  new File(StockUtils.getDailyStockSaveDir("B") + "notion_" + notionCode + ".json");
	        	if(file.exists() && file.length() > 100){
	        		LOGGER.info(file.getAbsoluteFile() + " exists, return now...");
	        		continue;
	        	}
				try {
	        		HttpUtils.httpDownload(notionHotStockUrl, "UTF-8", 10 * 1000, file);
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
		}else if("INDUSTRY".equals(type)){
			// 先查询ST_INDUSTRY_HOT中热点板块, 关联 ST_INDUSTRY_INFO
			List<IndustryHot> industryHotList = industryHotService.queryIndustryHotInfoByDate(tradeDate, StockConstant.DFCF_FLAG);
			if(industryHotList == null || industryHotList.isEmpty()){
				LOGGER.error("industryHotList is null or empty, return now...");
				return;
			}
			
			int limit = 0;
			// http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/index.aspx?type=s&sortType=C&sortRule=-1&pageSize=20&page=1&jsName=quote_123&style=28002475
			String preIndustryUrl = "http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/index.aspx?type=s&sortType=C&sortRule=-1&pageSize=" + StockConstant.INDUSTRY_HOT_STOCKS_UPPER + "&page=1&jsName=quote_123&style=";
			for(IndustryHot industryHot : industryHotList){
				limit++;
				if(limit > StockConstant.INDUSTRY_HOT_UPPER){
					break;
				}
				String industryCode = industryHot.getIndustryUrl().substring(10, 18);
				String industryHotStockUrl = preIndustryUrl + industryCode;
				File file =  new File(StockUtils.getDailyStockSaveDir("B") + "industry_" + industryCode + ".json");
	        	if(file.exists() && file.length() > 100){
	        		LOGGER.info(file.getAbsoluteFile() + " exists, return now...");
	        		continue;
	        	}
				try {
	        		HttpUtils.httpDownload(industryHotStockUrl, "UTF-8", 10 * 1000, file);
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
	 * 下载指数的json文件: http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/Index.aspx?type=z&jsName=quote_hs&reference=rtj&ids=0000011,3990012,0003001,3990062,3990052
	 * @param  格式为151021， 用于保存文件的路径.
	 */
	public void downloadIndexFiles() {
		File file =  new File(StockUtils.getDailyStockSaveDir("B") + "index.json");
    	if(file.exists() && file.length() > 100){
    		LOGGER.info(file.getAbsoluteFile() + " exists, return now...");
    		return;
    	}
    	if(!file.getParentFile().exists()){
    		file.getParentFile().mkdirs();
    	}
    	String indexUrl = "http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/Index.aspx?type=z&jsName=quote_hs&reference=rtj&ids=0000011,3990012,0003001,3990062,3990052";
    	try {
    		HttpUtils.httpDownload(indexUrl, "UTF-8", 10 * 1000, file);
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
	 * 下载包含所有stock的概念、行业，用于解析将信息插入 ST_NOTION_STOCK, ST_INDUSTRY_STOCK
	 * http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/index.aspx?type=s&sortType=C&sortRule=-1&pageSize=1000&page=1&jsName=quote_123&style=28003540
	 */
	public void downloadBoardHotFilesAll(File savedDir, String type) {
		if("NOTION".equals(type)){
			// 先查询ST_NOTION_INFO中热点板块
			List<NotionInfo> notionInfoList = notionInfoService.queryAllNotionInfo(StockConstant.DFCF_FLAG);
			if(notionInfoList == null || notionInfoList.isEmpty()){
				LOGGER.error("notionInfoList is null or empty, return now...");
				return;
			}
			// http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/index.aspx?type=s&sortType=C&sortRule=-1&pageSize=1000&page=1&jsName=quote_123&style=28003540
			String preNotionUrl = "http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/index.aspx?type=s&sortType=C&sortRule=-1&pageSize=1000&page=1&jsName=quote_123&style=";
			for(NotionInfo notionInfo : notionInfoList){
				String notionCode = notionInfo.getNotionUrl().substring(10, 18);
				String url = preNotionUrl + notionCode;
				File file =  new File(StockUtils.getDailyStockSaveDir("B") + "all_notion_" + notionCode + ".json");
	        	if(file.exists() && file.length() > 100){
	        		LOGGER.info(file.getAbsoluteFile() + " exists, return now...");
	        		continue;
	        	}
				try {
	        		HttpUtils.httpDownload(url, "UTF-8", 10 * 1000, file);
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
		}else if("INDUSTRY".equals(type)){
			// 先查询ST_INDUSTRY_INFO中热点板块
			List<IndustryInfo> industryInfoList = industryInfoService.queryAllIndustryInfo(StockConstant.DFCF_FLAG);
			if(industryInfoList == null || industryInfoList.isEmpty()){
				LOGGER.error("industryInfoList is null or empty, return now...");
				return;
			}
			String preIndustryUrl = "http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/index.aspx?type=s&sortType=C&sortRule=-1&pageSize=1000&page=1&jsName=quote_123&style=";
			for(IndustryInfo industryInfo : industryInfoList){
				String industryCode = industryInfo.getIndustryUrl().substring(10, 18);
				String url = preIndustryUrl + industryCode;
				File file =  new File(StockUtils.getDailyStockSaveDir("B") + "all_industry_" + industryCode + ".json");
	        	if(file.exists() && file.length() > 100){
	        		LOGGER.info(file.getAbsoluteFile() + " exists, return now...");
	        		continue;
	        	}
				try {
	        		HttpUtils.httpDownload(url, "UTF-8", 10 * 1000, file);
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
	 * 下载当前的个股json,5分钟降序排列. http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/index.aspx?type=s&sortType=G&sortRule=-1&pageSize=50&page=1&jsName=quote_123&style=10
	 * @param type: 10 上证；  20 深证
	 */
	public void downloadStockFiveChange(String type) {
		File file =  new File(StockUtils.getDailyStockSaveDir("B") + "five_change_" + type + ".json");
		// 创建父目录
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		// 先删除之前的
		if(file.exists()){
			file.delete();
		}
		String url = "http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/index.aspx?type=s&sortType=G&sortRule=-1&pageSize=30&page=1&jsName=quote_123&style=" + type;
		try {
    		HttpUtils.httpDownload(url, "UTF-8", 10 * 1000, file);
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
	 * 下载个股资金流向数据: http://nufm.dfcfw.com/EM_Finance2014NumericApplication/JS.aspx/JS.aspx?type=ct&st=(BalFlowMain)&sr=-1&p=1&ps=5000&js=var%20PyJtLOXm={pages:(pc),date:%222014-10-22%22,data:[(x)]}&token=894050c76af8597a853f5b408b759f5d&cmd=C._AB&sty=DCFFITA&rt=48713818
	 * 每页显示5000个，只1页.
	 * 上面的url来自: http://data.eastmoney.com/zjlx/detail.html
	 * 注意，这个链接只能下载当日的.
	 * 
	 * @param dir 保存文件的路径.
	 * @return file 下载的文件
	 */
	public File downloadStockCapFlow(File dir) {
		File file =  new File(dir.getAbsolutePath() + File.separatorChar + "capFlow" + ".json");
		// 创建父目录
		if(!dir.exists()){
			dir.mkdirs();
		}
		// 先删除之前的
		if(file.exists()){
			file.delete();
		}
		String url = "http://nufm.dfcfw.com/EM_Finance2014NumericApplication/JS.aspx/JS.aspx?type=ct&st=(BalFlowMain)&sr=-1&p=1&ps=5000&js=var%20PyJtLOXm={pages:(pc),date:%222014-10-22%22,data:[(x)]}&token=894050c76af8597a853f5b408b759f5d&cmd=C._AB&sty=DCFFITA&rt=48713818";
		try {
    		HttpUtils.httpDownload(url, "UTF-8", 10 * 1000, file);
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
		return file;
	}
	
	/**
	 * 下载个股历史资金流向: http://data.eastmoney.com/zjlx/600736.html
	 * 不是json数据，直接解析html就可得到.
	 * @param dir 文件保存的目录.
	 * @param code 股票代码.
	 * @return file 下载的html文件.
	 */
	public File downloadStockCapFlowHist(File dir, String code) {
		File file =  new File(dir.getAbsolutePath() + File.separatorChar + "capFlow_" + code + ".html");
		// 创建父目录
		if(!dir.exists()){
			dir.mkdirs();
		}
		// 不删除之前的文件，因为一天之内该数据不会发生改变.
		if(file.exists()){
			return file;
		}
		String url = "http://data.eastmoney.com/zjlx/" + code + ".html";
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
		return file;
	}
	
	/**
	 * 下载实时资金明细，每分钟的. 用于绘制类似 http://data.eastmoney.com/zjlx/000839.html 中的flash图.
	 * 下载文件的url: http://s1.dfcfw.com/allXML/000839.xml?rt=24361252
	 * @param dir 文件保存的目录.
	 * @param code 股票代码.
	 * @return file 下载的html文件.
	 */
	public File downloadStockCapFlowDetail(File dir, String code) {
		File file =  new File(dir.getAbsolutePath() + File.separatorChar + "capFlowDetail_" + code + ".dat");
		// 创建父目录
		if(!dir.exists()){
			dir.mkdirs();
		}
		// 不删除之前的文件，因为一天之内该数据不会发生改变.
		if(file.exists()){
			return file;
		}
		String url = "http://s1.dfcfw.com/allXML/" + code + ".xml?rt=24361252";
		try {
    		HttpUtils.httpDownload(url, "UTF-8", 1 * 1000, file);
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
		return file;
	}

	public StockInfoService getStockInfoService() {
		return stockInfoService;
	}

	public void setStockInfoService(StockInfoService stockInfoService) {
		this.stockInfoService = stockInfoService;
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

	public IndustryInfoService getIndustryInfoService() {
		return industryInfoService;
	}

	public void setIndustryInfoService(IndustryInfoService industryInfoService) {
		this.industryInfoService = industryInfoService;
	}

}
