/**
 * 
 */
package com.wy.stock.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.wy.stock.domain.Index;
import com.wy.stock.domain.IndustryHot;
import com.wy.stock.domain.IndustryHotStocks;
import com.wy.stock.domain.IndustryInfo;
import com.wy.stock.domain.IndustryStock;
import com.wy.stock.domain.NotionHot;
import com.wy.stock.domain.NotionHotStocks;
import com.wy.stock.domain.NotionInfo;
import com.wy.stock.domain.NotionStock;
import com.wy.stock.domain.StockCapFlow;
import com.wy.stock.domain.StockFiveChange;
import com.wy.stock.domain.StockHistory;
import com.wy.stock.domain.ExchangeInfo;
import com.wy.stock.service.IndexService;
import com.wy.stock.service.IndustryHotStocksService;
import com.wy.stock.service.IndustryInfoService;
import com.wy.stock.service.IndustryStockService;
import com.wy.stock.service.NotionHotStocksService;
import com.wy.stock.service.NotionInfoService;
import com.wy.stock.service.NotionStockService;
import com.wy.stock.service.StockFiveChangeService;
import com.wy.stock.service.StockHistoryService;
import com.wy.stock.service.ExchangeInfoService;
import com.wy.stock.utils.HttpUtils;
import com.wy.stock.utils.StockConstant;
import com.wy.stock.utils.StockUtils;

/**
 * 
 * @author leslie
 *
 */
public class StockParseToolImpl implements StockParseTool {

	private static Logger LOGGER = Logger.getLogger(StockParseToolImpl.class
			.getName());
	
	private ExchangeInfoService exchangeInfoService;
	
	private StockHistoryService stockHistoryService;
	
	private NotionInfoService notionInfoService;
	
	private IndustryInfoService industryInfoService;
	
	private NotionHotStocksService notionHotStocksService;
	
	private IndustryHotStocksService industryHotStocksService;
	
	private IndexService indexService;
	
	private NotionStockService notionStockService;
	
	private IndustryStockService industryStockService;
	
	private StockFiveChangeService stockFiveChangeService;
	
	public void getStockInfo(){
		/*
		 * 东财页面http://quote.eastmoney.com/center/list.html#10中的相应的url：
		 * 上证A股: http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/index.aspx?type=s&sortType=C&sortRule=-1&pageSize=20&page=2&jsName=quote_123&style=10&token=44c9d251add88e27b65ed86506f6e5da&_g=0.42779796849936247
		   上证B股: http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/index.aspx?type=s&sortType=C&sortRule=-1&pageSize=20&page=2&jsName=quote_123&style=11&token=44c9d251add88e27b65ed86506f6e5da&_g=0.9540072847157717
		   深证A股: http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/index.aspx?type=s&sortType=C&sortRule=-1&pageSize=20&page=2&jsName=quote_123&style=20&token=44c9d251add88e27b65ed86506f6e5da&_g=0.10397345130331814
		   深证B股: http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/index.aspx?type=s&sortType=C&sortRule=-1&pageSize=20&page=2&jsName=quote_123&style=21&token=44c9d251add88e27b65ed86506f6e5da&_g=0.06430959468707442
		 */
		String encoding = "UTF-8";
		String exchange = "";
		String type = "";
		int pages = 0;

		// 上证A股: 每页20条，一共56页.
		exchange = "上证";
		type = "A";
		pages = 1;
		for(int i = 1; i <= pages; i++){
			LOGGER.info("process " + exchange + " " + type + " page:" + i);
			String url = "http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/index.aspx?type=s&sortType=C&sortRule=-1&pageSize=20&page=" + i + "&jsName=quote_123&style=10&token=44c9d251add88e27b65ed86506f6e5da&_g=0.42779796849936247";
			String result = "";
			try {
				result = HttpUtils.getStringByHttpClient(url, encoding);
			} catch (IOException e) {
				// 再取一次
				try {
					result = HttpUtils.getStringByHttpClient(url, encoding);
				} catch (IOException e1) {
					LOGGER.error(e);
				}
			}
			if(StringUtils.isBlank(result)){
				continue;
			}
			if(pages == 1){
				pages = getStockInfoPages(result.substring(result.indexOf("=") + 1));
			}
			List<ExchangeInfo> stocks = getStockInfo(result.substring(result.indexOf("=") + 1), exchange, type);
			if(stocks != null && !stocks.isEmpty()){
				// insertStockInfoBatch 内部会先删除再插入
				exchangeInfoService.insertExchangeInfoBatch(stocks);
			}
		}
		
		// 上证B股: 每页20条，一共3页.
		exchange = "上证";
		type = "B";
		pages = 1;
		for(int i = 1; i <= pages; i++){
			LOGGER.info("process " + exchange + " " + type + " page:" + i);
			String url = "http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/index.aspx?type=s&sortType=C&sortRule=-1&pageSize=20&page=" + i + "&jsName=quote_123&style=11&token=44c9d251add88e27b65ed86506f6e5da&_g=0.9540072847157717";
			String result = "";
			try {
				result = HttpUtils.getStringByHttpClient(url, encoding);
			} catch (IOException e) {
				// 再取一次
				try {
					result = HttpUtils.getStringByHttpClient(url, encoding);
				} catch (IOException e1) {
					LOGGER.error(e);
				}
			}
			if(StringUtils.isBlank(result)){
				continue;
			}
			if(pages == 1){
				pages = getStockInfoPages(result.substring(result.indexOf("=") + 1));
			}
			List<ExchangeInfo> stocks = getStockInfo(result.substring(result.indexOf("=") + 1), exchange, type);
			if(stocks != null && !stocks.isEmpty()){
				// insertStockInfoBatch 内部会先删除再插入
				exchangeInfoService.insertExchangeInfoBatch(stocks);
			}
		}

		// 深证A股: 每页20条, 一共90页.
		exchange = "深证";
		type = "A";
		pages = 1;
		for(int i = 1; i <= pages; i++){
			LOGGER.info("process " + exchange + " " + type + " page:" + i);
			String url = "http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/index.aspx?type=s&sortType=C&sortRule=-1&pageSize=20&page=" + i + "&jsName=quote_123&style=20&token=44c9d251add88e27b65ed86506f6e5da&_g=0.10397345130331814";
			String result = "";
			try {
				result = HttpUtils.getStringByHttpClient(url, encoding);
			} catch (IOException e) {
				// 再取一次
				try {
					result = HttpUtils.getStringByHttpClient(url, encoding);
				} catch (IOException e1) {
					LOGGER.error(e);
				}
			}
			if(StringUtils.isBlank(result) || !result.contains("=")){
				continue;
			}
			if(pages == 1){
				pages = getStockInfoPages(result.substring(result.indexOf("=") + 1));
			}
			List<ExchangeInfo> stocks = getStockInfo(result.substring(result.indexOf("=") + 1), exchange, type);
			if(stocks != null && !stocks.isEmpty()){
				// insertStockInfoBatch 内部会先删除再插入
				exchangeInfoService.insertExchangeInfoBatch(stocks);
			}
		}
		// 深证B股: 每页20条, 一共3页.
		exchange = "深证";
		type = "B";
		pages = 1;
		for(int i = 1; i <= pages; i++){
			LOGGER.info("process " + exchange + " " + type + " page:" + i);
			String url = "http://hqdigi2.eastmoney.com/EM_Quote2010NumericApplication/index.aspx?type=s&sortType=C&sortRule=-1&pageSize=20&page=" + i + "&jsName=quote_123&style=21&token=44c9d251add88e27b65ed86506f6e5da&_g=0.06430959468707442";
			String result = "";
			try {
				result = HttpUtils.getStringByHttpClient(url, encoding);
			} catch (IOException e) {
				// 再取一次
				try {
					result = HttpUtils.getStringByHttpClient(url, encoding);
				} catch (IOException e1) {
					LOGGER.error(e);
				}
			}
			if(StringUtils.isBlank(result)){
				continue;
			}
			if(pages == 1){
				pages = getStockInfoPages(result.substring(result.indexOf("=") + 1));
			}
			List<ExchangeInfo> stocks = getStockInfo(result.substring(result.indexOf("=") + 1), exchange, type);
			if(stocks != null && !stocks.isEmpty()){
				// insertStockInfoBatch 内部会先删除再插入
				exchangeInfoService.insertExchangeInfoBatch(stocks);
			}
		}
	}
	
	/**
	 * 这里使用org.json,简单点， 没有用json-lib. json-lib方便和bean互转.
	 * 上证A股:
	 * var quote_123=
	 * {rank:["6002361,600236,桂冠电力,9.96,10.15,10.96,10.96,10.06,94242,902893,1.00,10.04%,10.44,9.04%,100.00%,107572,79,420484,482409,-1,0,0.00%,1.75,8.00%,27.40,001154|002428|003502|003520|003569|003705|003707|5009,10.96,0.00,2015-06-05 15:05:05,0,1128526848,11240127449,6.41",
	 *        "6008011,600801,华新水泥,12.65,12.81,13.92,13.92,12.71,139313,1046764,1.27,10.04%,13.31,9.57%,100.00%,25970,249,439226,607537,-1,0,0.00%,3.02,10.77%,174.00,001157|002424|003498|003535|003567|003596|003602|003701|003705|003707|50025,13.92,0.00,2015-06-05 15:05:05,0,971679872,12291750010,3.8"],
	 *  pages:53
	 *  }
	 *  
	 * 上证B股:
	 * var quote_123=
	 * {rank:["9009041,900904,神奇B股,2.955,2.955,2.950,3.016,2.940,149,5030,-0.005,-0.17%,2.973,2.57%,-52.68%,-138,0,2747,2282,0,-1,0.00%,0.721,0.00%,0.000,003636,2.944,2.956,2015-06-05 15:05:05,1,0,0,0","9009401,900940
	 *  pages:3
	 *  }
	 *  
	 *  深证A股:
	 *  var quote_123=
	 *  {rank:["3000402,300040,九洲电气,11.18,12.30,12.30,12.30,12.30,60,490,1.12,10.02%,12.30,0.00%,100.00%,86647,0,490,0,-1,0,0.00%,0.00,0.02%,0.00,001146|002457|003551|003569|003585|003595|003700|5004,12.30,0.00,2015-06-05 15:05:00,0,198448496,2218654246,33","0023202,002320,
	 *   pages: 85
	 *  }
	 *  
	 *  深证B股:
	 *  var quote_123=
	 *  {rank:["2005532,200553,沙隆达B,10.85,10.87,10.91,11.16,10.67,3321,30311,0.06,0.55%,10.96,4.52%,-60.85%,-470,51,15124,15186,-1,-1,0.09%,1.04,0.00%,0.00,003636,10.91,10.92,2015-06-05 15:05:00,1,0,0,0","2005812,200581,
	 *   pages: 3
	 *  }
	 * @param jsonString
	 * @return
	 * @throws JSONException
	 */
    @SuppressWarnings("unchecked")
	private List<ExchangeInfo> getStockInfo(String jsonString, String exchange, String type) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        List<ExchangeInfo> result = new ArrayList<ExchangeInfo>();
        Iterator<String> iterator1 = jsonObject.keys();
    	
        Timestamp timestamp = new Timestamp(Calendar.getInstance()
				.getTimeInMillis());
        while (iterator1.hasNext()) {
        	String key1 = (String) iterator1.next();
        	if(!"rank".equals(key1)){
        		continue;
        	}
        	JSONArray rankArray = jsonObject.getJSONArray(key1);
        	for(int i = 0; i < rankArray.length(); i++){
            	// 一支股票数据
            	String oneStock = (String) rankArray.get(i);
            	LOGGER.info("oneStock: " + oneStock);
            	if(StringUtils.isBlank(oneStock)){
            		continue;
            	}
            	String[] oneStockArray = oneStock.split(",");
            	
            	ExchangeInfo stockInfo = new ExchangeInfo();
            	stockInfo.setName(oneStockArray[2]);
            	stockInfo.setCode(oneStockArray[1]);
            	stockInfo.setExchange(exchange);
            	stockInfo.setType(type);
            	stockInfo.setTimestamp(timestamp);
            	result.add(stockInfo);
        	}
        }
    	return result;
    }
    
    @SuppressWarnings("unchecked")
	private int getStockInfoPages(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        Iterator<String> iterator1 = jsonObject.keys();
    	
        int result = 100;
        while (iterator1.hasNext()) {
        	String key1 = (String) iterator1.next();
        	if(!"pages".equals(key1)){
        		continue;
        	}
        	result = jsonObject.getInt("pages");
        	break;
        }
    	return result;
    }
    
    /**
     * 解析指定目录下的所有的csv文件.
     */
    public void parseAllHistCsv(File dir){
    	if(dir == null || !dir.exists()){
    		LOGGER.info("dir not exists, return now...");
    		return;
    	}
    	// 查询所有的股票
    	List<ExchangeInfo> allStocks = exchangeInfoService.queryAllExchangeInfo();
    	if(allStocks == null || allStocks.isEmpty()){
    		return;
    	}
    	/*
    	// 查询所有已处理过的
    	List<StockInfo> processedStocks = stockHistoryService.queryAllHistStock();
    	*/
    	// 查询所有股票的最近的一笔交易日期, 并转换为map. key: code_exchange_type  value: tradeDate
    	Map<String, String> maxDateMap =  trans2MaxDateMap(stockHistoryService.queryAllHistStockMaxDate());
    	
    	String dirPath = dir.getAbsolutePath() + File.separatorChar;
    	// 遍历
    	int num = 0;
    	for(ExchangeInfo stock : allStocks){
    		LOGGER.info(num++ + " process " + stock.getExchange() + " " + stock.getCode() + " " + stock.getType());
    		String stockStr = "";
    		if("上证".equals(stock.getExchange())){
    			stockStr = stock.getCode() + ".ss";
    		}else if("深证".equals(stock.getExchange())){
    			stockStr = stock.getCode() + ".sz";
    		}
    		File csvFile = new File(dirPath + stockStr + ".csv");
    		if(!csvFile.exists() || csvFile.length() < 100){
    			LOGGER.info(csvFile.getAbsoluteFile() + " not exists.");
    			continue;
    		}
    		try {
				parseHistCsv(csvFile, stock, maxDateMap);
			} catch (IOException e) {
				LOGGER.error(e);
			}
    	}
    }
    
    private Map<String, String> trans2MaxDateMap(List<StockHistory> processedStocks){
    	Map<String, String> result = new HashMap<String, String>();
    	if(processedStocks == null || processedStocks.isEmpty()){
    		return result;
    	}
    	for(StockHistory stock : processedStocks){
    		result.put(stock.getCode() + "_" + stock.getExchange() + "_" + stock.getType(), stock.getTradeDate().toString());
    	}
    	return result;
    }
    
    /**
     * 解析指定的csv文件.
     */
    public void parseHistCsv(File file, ExchangeInfo stock, Map<String, String> maxDateMap) throws IOException{
    	if(file == null || !file.exists() || !file.getName().endsWith("csv")){
    		LOGGER.info(file.getAbsolutePath() + " not exists or not a csv, return now...");
    		return;
    	}
    	List<StockHistory> stockHistoryList = new ArrayList<StockHistory>();
    	BufferedReader br = null;
    	String tradeDateStr;
		String openStr;
		String highStr;
		String lowStr;
		String closeStr;
		String volumnStr;
		String adjCloseStr;
		String line;
		
		Timestamp timestamp = new Timestamp(Calendar.getInstance()
				.getTimeInMillis());
    	try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			while ((line = br.readLine()) != null ) {
			   // 去掉标题头
				if(line.contains("Date")){
					continue;
				}
			   String[] info = line.split(",");
			   tradeDateStr = info[0].trim();
			   openStr = info[1].trim();
			   highStr = info[2].trim();
			   lowStr = info[3].trim();
			   closeStr = info[4].trim();
			   volumnStr = info[5].trim();
			   adjCloseStr = info[6].trim();
			   
			   StockHistory stockHistory = new StockHistory();
			   stockHistory.setExchange(stock.getExchange());
			   stockHistory.setCode(stock.getCode());
			   stockHistory.setType(stock.getType());
			   stockHistory.setTradeDate(Timestamp.valueOf(tradeDateStr + " 00:00:00"));
			   stockHistory.setOpen(Float.valueOf(openStr));
			   stockHistory.setHigh(Float.valueOf(highStr));
			   stockHistory.setLow(Float.valueOf(lowStr));
			   stockHistory.setClose(Float.valueOf(closeStr));
			   stockHistory.setVolumn(new BigDecimal(volumnStr));
			   stockHistory.setAdjClose(Float.valueOf(adjCloseStr));
			   stockHistory.setTimestamp(timestamp);
			   // 如果存在了，就直接退出.
			   if(maxDateMap != null){
				   String maxDate = maxDateMap.get(stock.getCode() + "_" + stock.getExchange() + "_" + stock.getType());
				   if(!StringUtils.isBlank(maxDate) && Timestamp.valueOf(maxDate).compareTo(stockHistory.getTradeDate()) == 0){
					   break; 
				   }
			   }
				   
			   stockHistoryList.add(stockHistory);
			   
			   if(stockHistoryList.size() % 100 == 0){
				   LOGGER.info("stockHistoryList size: " + stockHistoryList.size());
			   }
			   
			   if(stockHistoryList.size() % 2000 == 0){
				   LOGGER.info("stockHistoryList size: " + stockHistoryList.size() + " insert now...");
				   stockHistoryService.insertStockHistoryBatch(stockHistoryList);
				   stockHistoryList.clear();
			   }
			}
			// 插入剩下的.
			LOGGER.info("last stockHistoryList size: " + stockHistoryList.size() + " insert now...");
			stockHistoryService.insertStockHistoryBatch(stockHistoryList);
			stockHistoryList.clear();
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}finally{
			if(br != null){
				br.close();
			}
		}
    }
    
    /**
     * 获取概念板块热点数据。 http://quote.eastmoney.com/center/BKList.html#notion_0_0?sortRule=0
     * 并存入指定的目录中.
     * 
     * @param savedDir 保存文件的目录.
     * @param type
     */
    private void downloadBoardInfoFiles(File savedDir, String type){
    	String dirPath = savedDir.getAbsolutePath() + File.separatorChar;
    	if(!savedDir.exists()){
    		savedDir.mkdirs();
    	}
    	// 概念板块热点
    	if("NOTION".equalsIgnoreCase(type)){
        	File file =  new File(dirPath + "notionInfo.html");
        	if(file.exists() && file.length() > 100){
        		LOGGER.info(file.getAbsoluteFile() + " exists, return now...");
        		return;
        	}
        	//String url = "http://quote.eastmoney.com/center/BKList.html#notion_0_0?sortRule=0";
        	String url = "http://quote.eastmoney.com/centerv2/hsbk/gnbk";
        	LOGGER.info("notionInfo downloading");
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
        	
    	}else if("INDUSTRY".equalsIgnoreCase(type)){
        	File file =  new File(dirPath + "industryInfo.html");
        	if(file.exists() && file.length() > 100){
        		LOGGER.info(file.getAbsoluteFile() + " exists, return now...");
        		return;
        	}
        	String url = "http://quote.eastmoney.com/center/BKList.html#trade_0_0?sortRule=0";
        	LOGGER.info("industryInfo downloading");
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
    
	private Document getNotionIndustryDoc(File file){
		if(!file.exists()){
			LOGGER.error("file: " + file.getAbsolutePath() + " not exists.");
			return null;
		}
		
		String content = StockUtils.getFileContent(file);
		LOGGER.info("content: " + content);
		Document doc = Jsoup.parse(content);
		
		if(doc == null){
			LOGGER.error("doc is null, return now...");
			return null;
		}
		return doc;
	}
    
	/**
	 * 解析html获取概念信息对象, NotionInfo
	 */
	public List<NotionInfo> parseNotionInfoFromDoc(Document notionInfoDoc, String source) {
		// #treemenu > div.bd > dl.node-section.js-section-open > dd:nth-child(10) > ul > li:nth-child(1) > div.hover-pop.col-6 > ul:nth-child(1) > li:nth-child(1) > a
		// #treemenu > div.bd > dl.node-section.js-section-open > dd:nth-child(10) > ul > li:nth-child(1) > div.hover-pop.col-6 > ul:nth-child(1)
		// #treemenu > div.bd > dl.node-section.js-section-open > dd:nth-child(10) > ul > li:nth-child(1) > div.hover-pop.col-6 > ul:nth-child(2)
		
		// list.html#28003498_0_2:  #treemenu > div.bd > dl.node-section.js-section-open > dd:nth-child(10) > ul > li:nth-child(1) > div.hover-pop.col-6 > ul:nth-child(1) > li:nth-child(1) > a
		// AB股:                    #treemenu > div.bd > dl.node-section.js-section-open > dd:nth-child(10) > ul > li:nth-child(1) > div.hover-pop.col-6 > ul:nth-child(1) > li:nth-child(1) > a > span	
		Iterator<Element> ulIter = notionInfoDoc.select("#treemenu > div.bd ul > li:nth-child(1) ul").iterator();
		
		if(ulIter == null){
			LOGGER.error("ulIter is null, reutrn null now...");
			return null;
		}
		
		List<NotionInfo> list = new ArrayList<NotionInfo>();
		Timestamp timestamp = new Timestamp(Calendar.getInstance()
				.getTimeInMillis());
		while(ulIter.hasNext()){
			Element ulElement = ulIter.next();
			
			Iterator<Element> liIter = ulElement.select("> li").iterator();
			if(liIter == null){
				continue;
			}
			
			while(liIter.hasNext()){
				Element liElement = liIter.next();
				
				// url标识
				String notionUrl = liElement.select("> a").attr("href");
				// notionName
				String notionName = liElement.select("> a > span").text();
				
				NotionInfo notionInfo = new NotionInfo();
				notionInfo.setNotionUrl(notionUrl);
				notionInfo.setNotionName(notionName);
				notionInfo.setSource(source);
				notionInfo.setType("");
				notionInfo.setTimestamp(timestamp);
				list.add(notionInfo);
			}
		}
		return list;
	}
	
	/**
	 * 解析html获取行业信息对象, IndustryInfo
	 */
	public List<IndustryInfo> parseIndustryInfoFromDoc(Document industryInfoDoc, String source) {
		// #treemenu > div.bd > dl.node-section.js-section-open > dd.js-sub-open > ul > li:nth-child(3) > div.hover-pop.col-4 > ul:nth-child(1) > li:nth-child(1) > a
		
		// list.html#28002474_0_2:  #treemenu > div.bd > dl.node-section.js-section-open > dd.js-sub-open > ul > li:nth-child(3) > div.hover-pop.col-4 > ul:nth-child(1) > li:nth-child(1) > a
		// 保险:                    #treemenu > div.bd > dl.node-section.js-section-open > dd.js-sub-open > ul > li:nth-child(3) > div.hover-pop.col-4 > ul:nth-child(1) > li:nth-child(1) > a > span
		
		Iterator<Element> ulIter = industryInfoDoc.select("#treemenu > div.bd > dl:nth-child(2) > dd:nth-child(10) > ul > li:nth-child(3) > div > ul").iterator();
		if(ulIter == null){
			LOGGER.error("ulIter is null, reutrn null now...");
			return null;
		}
		
		List<IndustryInfo> list = new ArrayList<IndustryInfo>();
		Timestamp timestamp = new Timestamp(Calendar.getInstance()
				.getTimeInMillis());
		while(ulIter.hasNext()){
			Element ulElement = ulIter.next();
			
			Iterator<Element> liIter = ulElement.select("> li").iterator();
			if(liIter == null){
				continue;
			}
			
			while(liIter.hasNext()){
				Element liElement = liIter.next();
				
				// url标识
				String industryUrl = liElement.select("> a").attr("href");
				// industryName
				String industryName = liElement.select("> a > span").text();
				
				IndustryInfo industryInfo = new IndustryInfo();
				industryInfo.setIndustryUrl(industryUrl);
				industryInfo.setIndustryName(industryName);
				industryInfo.setSource(source);
				industryInfo.setTimestamp(timestamp);
				list.add(industryInfo);
			}
		}
		return list;
	}

	/**
	 * 解析概念热点json文件 notionHot.json
	 * 
	 * varBKCache={
    Notion: [
        [
            "迪士尼,6.21%,3767.95,9.81,25,0,002162,斯米克,10.04,540,3",
            "网络金融,5.28%,944.54,29.03,21,1,300033,同花顺,10.00,637,3",
            "彩票概念,5.21%,722.52,23.02,10,2,002117,东港股份,10.00,671,3",
            "参股保险,4.58%,631.05,10.09,11,2,000627,天茂集团,10.00,604,3",
	 * 
	 */
	public List<NotionHot> parseNotionHotFromJson(File jsonFile) {
		if(!jsonFile.exists()){
			LOGGER.error(jsonFile.getAbsolutePath() + " not exists, return null now...");
			return null;
		}
		String content = StockUtils.getFileContent(jsonFile);
		String jsonString = content.substring(content.indexOf("=") + 1);

        JSONObject jsonObject = new JSONObject(jsonString);
        List<NotionHot> result = new ArrayList<NotionHot>();
        @SuppressWarnings("unchecked")
		Iterator<String> iterator1 = jsonObject.keys();
    	
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, -12);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Timestamp tradeDate = new Timestamp(cal.getTimeInMillis());
        Timestamp timestamp = new Timestamp(Calendar.getInstance()
				.getTimeInMillis());
        
        while (iterator1.hasNext()) {
        	String key1 = (String) iterator1.next();
        	if(!"Notion".equals(key1)){
        		continue;
        	}
        	// 取第一个
        	JSONArray notionArray = jsonObject.getJSONArray(key1).getJSONArray(0);
        	for(int i = 0; i < notionArray.length(); i++){
        		int rank = i + 1;
        		NotionHot notionHot = new NotionHot();
        		
        		String[] strs = notionArray.getString(i).split(",");
        		// 板块名称
        		String notionName = strs[0];
        		// 涨跌幅
        		Float changePercent = Float.valueOf(strs[1].replace("%", ""));
        		// 总市值(亿)
        		Float totalMarketCap = Float.valueOf(strs[2]);
        		// 换手率
        		Float turnoverRate = Float.valueOf(strs[3]);
        		// 上涨家数
        		int riseStocksNum = Integer.valueOf(strs[4]);
        		// 下跌家数
        		int fallStocksNum = Integer.valueOf(strs[5]);
        		// 领涨股
        		String riseLeadStockName = strs[7];
        		// 领涨股涨跌幅度
        		Float riseLeadStockChangePercent = Float.valueOf(strs[8]);
        		
        		notionHot.setTradeDate(tradeDate);
        		notionHot.setRank(rank);
        		notionHot.setNotionName(notionName);
        		notionHot.setChangePercent(changePercent);
        		notionHot.setTotalMarketCap(totalMarketCap);
        		notionHot.setTurnoverRate(turnoverRate);
        		notionHot.setRiseStocksNum(riseStocksNum);
        		notionHot.setFallStocksNum(fallStocksNum);
        		notionHot.setRiseLeadStockName(riseLeadStockName);
        		notionHot.setRiseLeadStockChangePercent(riseLeadStockChangePercent);
        		notionHot.setTimestamp(timestamp);
        		result.add(notionHot);
        	}
        }
    	return result;
	}

	/**
	 * 解析行业热点json文件 industryHot.json
	 * 
	 * varBKCache={
    Trade: [
        [
            "公用事业,5.70%,1321.16,14.21,39,2,600187,国中水务,10.05,427,2",
            "民航机场,3.88%,2028.67,4.50,10,1,000089,深圳机场,9.94,420,2",
	 * 
	 */
	public List<IndustryHot> parseIndustryHotFromJson(File jsonFile) {
		if(!jsonFile.exists()){
			LOGGER.error(jsonFile.getAbsolutePath() + " not exists, return null now...");
			return null;
		}
		String content = StockUtils.getFileContent(jsonFile);
		String jsonString = content.substring(content.indexOf("=") + 1);

        JSONObject jsonObject = new JSONObject(jsonString);
        List<IndustryHot> result = new ArrayList<IndustryHot>();
        @SuppressWarnings("unchecked")
		Iterator<String> iterator1 = jsonObject.keys();
    	
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, -12);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Timestamp tradeDate = new Timestamp(cal.getTimeInMillis());
        Timestamp timestamp = new Timestamp(Calendar.getInstance()
				.getTimeInMillis());
        
        while (iterator1.hasNext()) {
        	String key1 = (String) iterator1.next();
        	if(!"Trade".equals(key1)){
        		continue;
        	}
        	// 取第一个
        	JSONArray industryArray = jsonObject.getJSONArray(key1).getJSONArray(0);
        	for(int i = 0; i < industryArray.length(); i++){
        		int rank = i + 1;
        		IndustryHot industryHot = new IndustryHot();
        		
        		String[] strs = industryArray.getString(i).split(",");
        		// 板块名称
        		String notionName = strs[0];
        		// 涨跌幅
        		Float changePercent = Float.valueOf(strs[1].replace("%", ""));
        		// 总市值(亿)
        		Float totalMarketCap = Float.valueOf(strs[2]);
        		// 换手率
        		Float turnoverRate = Float.valueOf(strs[3]);
        		// 上涨家数
        		int riseStocksNum = Integer.valueOf(strs[4]);
        		// 下跌家数
        		int fallStocksNum = Integer.valueOf(strs[5]);
        		// 领涨股
        		String riseLeadStockName = strs[7];
        		// 领涨股涨跌幅度
        		Float riseLeadStockChangePercent = Float.valueOf(strs[8]);
        		
        		industryHot.setTradeDate(tradeDate);
        		industryHot.setRank(rank);
        		industryHot.setIndustryName(notionName);
        		industryHot.setChangePercent(changePercent);
        		industryHot.setTotalMarketCap(totalMarketCap);
        		industryHot.setTurnoverRate(turnoverRate);
        		industryHot.setRiseStocksNum(riseStocksNum);
        		industryHot.setFallStocksNum(fallStocksNum);
        		industryHot.setRiseLeadStockName(riseLeadStockName);
        		industryHot.setRiseLeadStockChangePercent(riseLeadStockChangePercent);
        		industryHot.setTimestamp(timestamp);
        		result.add(industryHot);
        	}
        }
    	return result;
	}
	
	/**
	 * 解析目录下所有格式为 notion_28003499.json 的文件.
	 * @date   格式为 151021
	 */
	public void persistNotionHotStocksFromJson(String date) {
		String dirPath = StockConstant.BOARD_HOT_FILE_PATH + "20" + date.substring(0, 2) + File.separatorChar + 
				 date.substring(2, 4) + File.separatorChar + date.substring(4, 6) + File.separatorChar;
		
		File parent = new File(dirPath);
		com.wy.stock.utils.FileNameSelector selector = new com.wy.stock.utils.FileNameSelector(StockConstant.NOTION_IDENTIFIER + "_", ".json");
		File[] notionFiles = parent.listFiles(selector);
		if(notionFiles != null && notionFiles.length > 0){
			for(File file : notionFiles){
				List<NotionHotStocks> notionHotStocksList = parseNotionHotStocksFromJson(file);
				if(notionHotStocksList != null && !notionHotStocksList.isEmpty()){
					notionHotStocksService.insertNotionHotStocksBatch(notionHotStocksList);
				}
			}
		}
	}
	
	/**
	 * 解析目录下所有格式为 industry_28002429.json 的文件.
	 * @date   格式为 151021
	 */
	public void persistIndustryHotStocksFromJson(String date) {
		String dirPath = StockConstant.BOARD_HOT_FILE_PATH + "20" + date.substring(0, 2) + File.separatorChar + 
				 date.substring(2, 4) + File.separatorChar + date.substring(4, 6) + File.separatorChar;
		
		File parent = new File(dirPath);
		com.wy.stock.utils.FileNameSelector selector = new com.wy.stock.utils.FileNameSelector(StockConstant.INDUSTRY_IDENTIFIER + "_", ".json");
		File[] industryFiles = parent.listFiles(selector);
		if(industryFiles != null && industryFiles.length > 0){
			for(File file : industryFiles){
				List<IndustryHotStocks> industryHotStocksList = parseIndustryHotStocksFromJson(file);
				if(industryHotStocksList != null && !industryHotStocksList.isEmpty()){
					industryHotStocksService.insertIndustryHotStocksBatch(industryHotStocksList);
				}
			}
		}
	}
	
	
	/**
	 * 解析概念热点板块下的股票信息
	 * {rank:["6013981,601398,工商银行,4.50,4.50,4.77,4.95,4.48,181024,3956092,0.27,6.00%,4.58,10.44%,57.41%,17893,0,2359917,   1596174,1,1,4.61%,3.55,0.15%,5.48,001150|002475|003499|003500|003596|003610|003611|003612|003707|003716|5007|5009|50016,4.60,4.61,    2015-10-21 15:04:05,0,269612220416,1213254991872,3.12",
	 *        "6000161,600016,民生银行,8.57,8.57,9.00,9.40,8.54,215825,2476206,0.43,5.02%,8. 72,10.04%,91.53%,16600,9343,851047,1625159,1,1,4.17%,3.16,0.84%,5.63, 001150|002475|003499|003500|003514|003528|003596|003610|003611|003612|003707|003716|5007|5009|50016,8.75,8.77,2015-10-21 15:04:05,0,  29551769600,253258656454,11.8",
	 * @param jsonFile
	 * @return
	 */
	public List<NotionHotStocks> parseNotionHotStocksFromJson(File jsonFile) {
		if(!jsonFile.exists()){
			LOGGER.error(jsonFile.getAbsolutePath() + " not exists, return null now...");
			return null;
		}
		LOGGER.info("processing file: " + jsonFile.getAbsolutePath());
		String notionCode = jsonFile.getName().split("_")[1].substring(0, 8);
		String content = StockUtils.getFileContent(jsonFile);
		String jsonString = content.substring(content.indexOf("=") + 1);

        JSONObject jsonObject = new JSONObject(jsonString);
        List<NotionHotStocks> result = new ArrayList<NotionHotStocks>();
        
        // 查询概念热点板块代码、名称.
        Map<String, String> notionUrlNameMap = notionInfoService.queryNotionUrlNameMap(StockConstant.DFCF_FLAG);
        @SuppressWarnings("unchecked")
		Iterator<String> iterator1 = jsonObject.keys();
    	
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, -12);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Timestamp tradeDate = new Timestamp(cal.getTimeInMillis());
        Timestamp timestamp = new Timestamp(Calendar.getInstance()
				.getTimeInMillis());
        
        while (iterator1.hasNext()) {
        	String key1 = (String) iterator1.next();
        	if(!"rank".equals(key1)){
        		continue;
        	}
        	// 取第一个
        	JSONArray notionArray = jsonObject.getJSONArray(key1);
        	for(int i = 0; i < notionArray.length(); i++){
        		int rank = i + 1;
        		NotionHotStocks notionHotStocks = new NotionHotStocks();
        		
        		String[] strs = notionArray.getString(i).split(",");
        		// 概念名称  url: list.html#28003490_0_2
        		String notionName = notionUrlNameMap.get("list.html#" + notionCode + "_0_2");
        		// 股票代码
        		String code = strs[1];
        		// 股票名称
        		String stockName = strs[2];
        		// 最新价
        		Float newPrice = Float.valueOf(strs[5]);
        		// 涨跌幅
        		Float changePercent = Float.valueOf(strs[11].replace("%", ""));
        		notionHotStocks.setTradeDate(tradeDate);
        		notionHotStocks.setRank(rank);
        		notionHotStocks.setNotionName(notionName);
        		notionHotStocks.setCode(code);
        		notionHotStocks.setStockName(stockName);
        		notionHotStocks.setNewPrice(newPrice);
        		notionHotStocks.setChangePercent(changePercent);
        		notionHotStocks.setTimestamp(timestamp);
        		result.add(notionHotStocks);
        	}
        }
    	return result;
	}
	
	/**
	 * 解析行业热点板块下的股票信息
	 *rank:["0022602,002260,德奥通航,26.40,29.04,29.04,29.04,28.44,19054,65708,2.64,10.00%,29.00,2.27%,100.00%,39361,199,     57282,8425,-1,0,0.00%,0.87,2.48%,0.00,001153|002456|003704,29.04,0.00,2015-10-21 15:05:00,0,265200000,7001279899,6.66",
	 *		"0027592,002759,天际股份,35.19,35.25,35.87,37.00,34.35,19905,55564,0.68,1.93%,35.82,7.53%,44.06%,397,1199,28443,27120,-1,0,1.38%,1.99,23.15%,  52.75,001153|002456|003501|003571|5001,35.87,35.88,2015-10-21 15:05:00,0,24000000,844559967,12.02", 
	 * @param jsonFile
	 * @return
	 */
	public List<IndustryHotStocks> parseIndustryHotStocksFromJson(File jsonFile) {
		if(!jsonFile.exists()){
			LOGGER.error(jsonFile.getAbsolutePath() + " not exists, return null now...");
			return null;
		}
		LOGGER.info("processing file: " + jsonFile.getAbsolutePath());
		String industryCode = jsonFile.getName().split("_")[1].substring(0, 8);
		String content = StockUtils.getFileContent(jsonFile);
		String jsonString = content.substring(content.indexOf("=") + 1);

        JSONObject jsonObject = new JSONObject(jsonString);
        List<IndustryHotStocks> result = new ArrayList<IndustryHotStocks>();
        
        // 查询行业热点板块代码、名称.
        Map<String, String> industryUrlNameMap = industryInfoService.queryIndustryUrlNameMap(StockConstant.DFCF_FLAG);
        @SuppressWarnings("unchecked")
		Iterator<String> iterator1 = jsonObject.keys();
    	
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, -12);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Timestamp tradeDate = new Timestamp(cal.getTimeInMillis());
        Timestamp timestamp = new Timestamp(Calendar.getInstance()
				.getTimeInMillis());
        
        while (iterator1.hasNext()) {
        	String key1 = (String) iterator1.next();
        	if(!"rank".equals(key1)){
        		continue;
        	}
        	// 取第一个
        	JSONArray notionArray = jsonObject.getJSONArray(key1);
        	for(int i = 0; i < notionArray.length(); i++){
        		int rank = i + 1;
        		IndustryHotStocks industryHotStocks = new IndustryHotStocks();
        		String[] strs = notionArray.getString(i).split(",");
        		// 行业名称  url: list.html#28002456_0_2
        		String industryName = industryUrlNameMap.get("list.html#" + industryCode + "_0_2");
        		// 股票代码
        		String code = strs[1];
        		// 股票名称
        		String stockName = strs[2];
        		// 最新价
        		Float newPrice = Float.valueOf(strs[5]);
        		// 涨跌幅
        		Float changePercent = Float.valueOf(strs[11].replace("%", ""));
        		industryHotStocks.setTradeDate(tradeDate);
        		industryHotStocks.setRank(rank);
        		industryHotStocks.setIndustryName(industryName);
        		industryHotStocks.setCode(code);
        		industryHotStocks.setStockName(stockName);
        		industryHotStocks.setNewPrice(newPrice);
        		industryHotStocks.setChangePercent(changePercent);
        		industryHotStocks.setTimestamp(timestamp);
        		result.add(industryHotStocks);
        	}
        }
    	return result;
	}
	
	/**
	 * 解析概念、行业热点板块下的股票信息,记入ST_NOTION_STOCK, ST_INDUSTRY_STOCK.
	 * {rank:["6013981,601398,工商银行,4.50,4.50,4.77,4.95,4.48,181024,3956092,0.27,6.00%,4.58,10.44%,57.41%,17893,0,2359917,   1596174,1,1,4.61%,3.55,0.15%,5.48,001150|002475|003499|003500|003596|003610|003611|003612|003707|003716|5007|5009|50016,4.60,4.61,    2015-10-21 15:04:05,0,269612220416,1213254991872,3.12",
	 *        "6000161,600016,民生银行,8.57,8.57,9.00,9.40,8.54,215825,2476206,0.43,5.02%,8. 72,10.04%,91.53%,16600,9343,851047,1625159,1,1,4.17%,3.16,0.84%,5.63, 001150|002475|003499|003500|003514|003528|003596|003610|003611|003612|003707|003716|5007|5009|50016,8.75,8.77,2015-10-21 15:04:05,0,  29551769600,253258656454,11.8",
	 * @param jsonFile
	 * @return
	 */
	public void persistNotionIndustryStockFromJson(File dir, String type) {
		if("NOTION".equals(type)){
			// 先删除所有的
			notionStockService.deleteAllNotionStock(StockConstant.DFCF_FLAG);
			Map<String, String> notionUrlNameMap = notionInfoService.queryNotionUrlNameMap(StockConstant.DFCF_FLAG);
			com.wy.stock.utils.FileNameSelector selector = new com.wy.stock.utils.FileNameSelector("all_" + StockConstant.NOTION_IDENTIFIER + "_", ".json");
			File[] notionFiles = dir.listFiles(selector);
			if(notionFiles != null && notionFiles.length > 0){
				List<NotionStock> result = new ArrayList<NotionStock>();
				int j = 1;
				for(File jsonFile : notionFiles){
					LOGGER.info(j++ + " processing file: " + jsonFile.getAbsolutePath());
					String notionCode = jsonFile.getName().split("_")[2].substring(0, 8);
					String notionName = notionUrlNameMap.get("list.html#" + notionCode + "_0_2");
					String content = StockUtils.getFileContent(jsonFile);
					String jsonString = content.substring(content.indexOf("=") + 1);

			        JSONObject jsonObject = new JSONObject(jsonString);
			        
			        @SuppressWarnings("unchecked")
					Iterator<String> iterator1 = jsonObject.keys();
			    	
			        Timestamp timestamp = new Timestamp(Calendar.getInstance()
							.getTimeInMillis());
			        
			        while (iterator1.hasNext()) {
			        	String key1 = (String) iterator1.next();
			        	if(!"rank".equals(key1)){
			        		continue;
			        	}
			        	// 取第一个
			        	JSONArray notionArray = jsonObject.getJSONArray(key1);
			        	for(int i = 0; i < notionArray.length(); i++){
			        		NotionStock notionStock = new NotionStock();
			        		
			        		String[] strs = notionArray.getString(i).split(",");
			        		// 股票代码
			        		String code = strs[1];
			        		// 股票名称
			        		String stockName = strs[2];
			        		notionStock.setNotionCode(notionCode);
			        		notionStock.setNotionName(notionName);
			        		notionStock.setCode(code);
			        		notionStock.setStockName(stockName);
			        		notionStock.setTimestamp(timestamp);
			        		result.add(notionStock);
			        	}
			        }
			        if(result != null && !result.isEmpty()){
						notionStockService.insertNotionStockBatch(result);
						result.clear();
					}
				}
			}
		}else if("INDUSTRY".equals(type)){
			// 先删除所有的
			industryStockService.deleteAllIndustryStock(StockConstant.DFCF_FLAG);
			Map<String, String> industryUrlNameMap = industryInfoService.queryIndustryUrlNameMap(StockConstant.DFCF_FLAG);
			com.wy.stock.utils.FileNameSelector selector = new com.wy.stock.utils.FileNameSelector("all_" + StockConstant.INDUSTRY_IDENTIFIER + "_", ".json");
			File[] industryFiles = dir.listFiles(selector);
			if(industryFiles != null && industryFiles.length > 0){
				List<IndustryStock> result = new ArrayList<IndustryStock>();
				int j = 1;
				for(File jsonFile : industryFiles){
					LOGGER.info(j++ + " processing file: " + jsonFile.getAbsolutePath());
					String industryCode = jsonFile.getName().split("_")[2].substring(0, 8);
					String industryName = industryUrlNameMap.get("list.html#" + industryCode + "_0_2");
					String content = StockUtils.getFileContent(jsonFile);
					String jsonString = content.substring(content.indexOf("=") + 1);

			        JSONObject jsonObject = new JSONObject(jsonString);
			        
			        @SuppressWarnings("unchecked")
					Iterator<String> iterator1 = jsonObject.keys();
			    	
			        Timestamp timestamp = new Timestamp(Calendar.getInstance()
							.getTimeInMillis());
			        
			        while (iterator1.hasNext()) {
			        	String key1 = (String) iterator1.next();
			        	if(!"rank".equals(key1)){
			        		continue;
			        	}
			        	// 取第一个
			        	JSONArray industryArray = jsonObject.getJSONArray(key1);
			        	for(int i = 0; i < industryArray.length(); i++){
			        		IndustryStock industryStock = new IndustryStock();
			        		
			        		String[] strs = industryArray.getString(i).split(",");
			        		// 股票代码
			        		String code = strs[1];
			        		// 股票名称
			        		String stockName = strs[2];
			        		industryStock.setIndustryCode(industryCode);
			        		industryStock.setIndustryName(industryName);
			        		industryStock.setCode(code);
			        		industryStock.setStockName(stockName);
			        		industryStock.setTimestamp(timestamp);
			        		result.add(industryStock);
			        	}
			        }
			        if(result != null && !result.isEmpty()){
						industryStockService.insertIndustryStockBatch(result);
						result.clear();
					}
				}
			}
		}
	}

	/**
	 * 解析index.json
	 * {
    "quotation": [
           0       1      2       3      4        5       6       7        8        9      10    11     12     13    14    15  16  17    18      19   20     21     22     23      24      25      26                27         28
        {quotation:[
        "0000011,000001,上证指数,3320.68,3292.29,3368.74,3373.78,3282.99,37545202,323739341,48.06,1.45%,1159.74,2.73%,0.00%,  0,  0,  0,323739341,  1,  -1,  0.04%,  0.83,  0.00%,  0.00,,  0.00,  3435.73,  2015-10-22 15:04:06,  7,0,0,0",
        "3990012,399001,深证成指,10915.99,10921.18,11305.10,11320.01,10910.88,51771350,326831309,389.12,3.56%,15.84,3.75%,0.00%,0,0,0,326831309,1,1,0.02%,0.92,0.00%,0.00,,0.00,11383.05,2015-10-22 15:05:00,7,0,0,0"        }
	 * @param jsonFile
	 * @return
	 */
	public List<Index> parseIndexFromJson(File jsonFile) {
		if(!jsonFile.exists()){
			LOGGER.error(jsonFile.getAbsolutePath() + " not exists, return null now...");
			return null;
		}
		LOGGER.info("processing file: " + jsonFile.getAbsolutePath());
		String content = StockUtils.getFileContent(jsonFile);
		String jsonString = content.substring(content.indexOf("=") + 1);

        JSONObject jsonObject = new JSONObject(jsonString);
        List<Index> result = new ArrayList<Index>();
        
        @SuppressWarnings("unchecked")
		Iterator<String> iterator1 = jsonObject.keys();
    	
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, -12);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Timestamp tradeDate = new Timestamp(cal.getTimeInMillis());
        Timestamp timestamp = new Timestamp(Calendar.getInstance()
				.getTimeInMillis());
		String tradeDateStr = new SimpleDateFormat("YYYY-MM-dd").format(tradeDate);
		// 重新设置tradeDate, 确保最后是 00:00:00, 上面Calendar的方式容易出现 00:00:01
		tradeDate = Timestamp.valueOf(tradeDateStr + " 00:00:00");
        while (iterator1.hasNext()) {
        	String key1 = (String) iterator1.next();
        	if(!"quotation".equals(key1)){
        		continue;
        	}
        	// 取第一个
        	JSONArray indexArray = jsonObject.getJSONArray(key1);
        	for(int i = 0; i < indexArray.length(); i++){
        		Index index = new Index();
        		String[] strs = indexArray.getString(i).split(",");
        		String indexCode = strs[1];
        		String indexName = strs[2];
        		// 最新价
        		Float newPrice = Float.valueOf(strs[5]);
        		// 涨跌额
        		Float changeAmount = Float.valueOf(strs[10]);
        		// 涨跌幅度
        		Float changePercent = Float.valueOf(strs[11].replace("%", ""));
        		Float open = Float.valueOf(strs[4]);
        		Float high = Float.valueOf(strs[6]);
        		Float low = Float.valueOf(strs[7]);
        		Float close = Float.valueOf(strs[5]);
        		Float lastClose = Float.valueOf(strs[3]);
        		Float amplitude = Float.valueOf(strs[13].replace("%", ""));
        		Float volumn = Float.valueOf(strs[9])/10000.0f;
        		Float volumnAmount = Float.valueOf(strs[8])/10000.0f;
        		index.setTradeDate(tradeDate);
        		index.setIndexCode(indexCode);
        		index.setIndexName(indexName);
        		index.setNewPrice(newPrice);
        		index.setChangeAmount(changeAmount);
        		index.setChangePercent(changePercent);
        		index.setOpen(open);
        		index.setHigh(high);
        		index.setLow(low);
        		index.setClose(close);
        		index.setClose(lastClose);
        		index.setLastClose(lastClose);
        		index.setAmplitude(amplitude);
        		index.setVolumn(volumn);
        		index.setVolumnAmount(volumnAmount);
        		index.setTimestamp(timestamp);
        		result.add(index);
        	}
        }
    	return result;
	}
	
	/**
	 * 将index.json中的数据插入数据库.
	 * @param jsonFile
	 */
	public void persistIndexFromJson(File jsonFile) {
		if(!jsonFile.exists()){
			LOGGER.error(jsonFile.getAbsolutePath() + " not exists, return null now...");
			return;
		}
		List<Index> indexList = parseIndexFromJson(jsonFile);
		if(indexList != null && !indexList.isEmpty()){
			indexService.insertIndexBatch(indexList);
		}
	}
	
	/**
	 * 解析个股页面，插入ST_STOCK_FIVE_CHANGE, 这里不区分上证、深证
	 * rank:[
	 * "6007531,600753,东方银星,30.44,30.30,33.48,33.48,29.70,31700,100621,3.04,9.99%,31.50,12.42%,100.00%,16887,115,44022,56598,-1,0,6.29%,1.51,7.86%,71.74,001156|002451,33.48,0.00,2015-10-23 15:04:07,0,128000000,3910399902,5.18",
	 * "6004871,600487,亨通光电,11.26,11.35,12.35,12.39,11.21,77016,664621,1.09,9.68%,11.59,10.48%,43.94%,869,28,264915,399705,1,1,3.26%,1.26,5.77%,56.00,   001159|002448|003520|003571|003594|003633|003701|003705|003707,12.31,12.32,2015-10-23 15:04:07,0,1151269120,13009341276,11.2", 
	 */
	public void persistStockFiveChange(File dir) {
		Timestamp timestamp = new Timestamp(Calendar.getInstance()
				.getTimeInMillis());
		List<StockFiveChange> stockFiveChangeList = new ArrayList<StockFiveChange>();
		
		com.wy.stock.utils.FileNameSelector selector = new com.wy.stock.utils.FileNameSelector("five_change" + "_", ".json");
		File[] fiveChangeFiles = dir.listFiles(selector);
		if(fiveChangeFiles != null && fiveChangeFiles.length > 0){
			for(File jsonFile : fiveChangeFiles){
				LOGGER.info("processing file: " + jsonFile.getAbsolutePath());
				String content = StockUtils.getFileContent(jsonFile);
				String jsonString = content.substring(content.indexOf("=") + 1);

		        JSONObject jsonObject = new JSONObject(jsonString);
		        @SuppressWarnings("unchecked")
				Iterator<String> iterator1 = jsonObject.keys();
		        while (iterator1.hasNext()) {
		        	String key1 = (String) iterator1.next();
		        	if(!"rank".equals(key1)){
		        		continue;
		        	}
		        	// 取第一个
		        	JSONArray fiveChangeArray = jsonObject.getJSONArray(key1);
		        	for(int i = 0; i < fiveChangeArray.length(); i++){
		        		StockFiveChange stockFiveChange = new StockFiveChange();
		        		String[] strs = fiveChangeArray.getString(i).split(",");
		        		// 股票代码
		        		String code = strs[1];
		        		// 股票名称
		        		String stockName = strs[2];
		        		// 涨跌额
		        		Float changePercent = Float.valueOf(strs[11].replace("%", ""));
		        		// 5分钟涨跌
		        		Float fiveChangePercent = Float.valueOf(strs[21].replace("%", ""));
		        		stockFiveChange.setCode(code);
		        		stockFiveChange.setStockName(stockName);
		        		stockFiveChange.setChangePercent(changePercent);
		        		stockFiveChange.setFiveChangePercent(fiveChangePercent);
		        		stockFiveChange.setTimestamp(timestamp);
		        		stockFiveChangeList.add(stockFiveChange);
		        	}
		        }
			}
		}
		
		if(stockFiveChangeList != null && !stockFiveChangeList.isEmpty()){
			// 先删除所有的
			stockFiveChangeService.deleteStockFiveChange();
			stockFiveChangeService.insertStockFiveChangeBatch(stockFiveChangeList);
		}
	}
	
	/**
	 * 从json文件中解析所有个股的资金情况.
	 * var PyJtLOXm={pages:1,date:"2014-10-22",data:
	 * ["2,002702,海欣食品,21.21,10.01,33925.84,32.56,33703.14,32.35,222.70,0.21,-16752.44,-16.08,-17173.41,-16.48,2016-04-22 15:00:00",
	 *  "1,603798,康普顿,64.77,10.00,23912.23,22.44,6651.59,6.24,17260.64,16.20,-20236.99,-18.99,-3675.24,-3.45,2016-04-22 15:00:00",
	 *  "1,601318,中国平安,32.42,0.62,19533.40,14.02,22535.11,16.17,-3001.71,-2.15,-15240.64,-10.94,-4292.76,-3.08,2016-04-22 15:00:00",
	 *  "2,002018,华信国际,12.44,-9.99,-78452.50,-29.95,-58571.48,-22.36,-19881.01,-7.59,25042.86,9.56,53409.63,20.39,2016-04-22 15:00:00"]}	 
	 **/
	@SuppressWarnings("unchecked")
	public List<StockCapFlow> parseStockCapFlow(File capFlowJsonFile) {
    	if(capFlowJsonFile == null || !capFlowJsonFile.exists() || !capFlowJsonFile.getAbsolutePath().endsWith(".json")){
    		LOGGER.info(capFlowJsonFile.getAbsolutePath() + " not exists or not a json file, return now...");
    		return null;
    	}
    	String capFlowStr = StockUtils.getFileContent(capFlowJsonFile);
    	String capFlowJsonStr = capFlowStr.split("=")[1];
    	

        JSONObject jsonObject = new JSONObject(capFlowJsonStr);
        List<StockCapFlow> result = new ArrayList<StockCapFlow>();
        Iterator<String> iterator1 = jsonObject.keys();
    	
        Timestamp timestamp = new Timestamp(Calendar.getInstance()
				.getTimeInMillis());
        while (iterator1.hasNext()) {
        	String key1 = (String) iterator1.next();
        	// 取data项数据.
        	if(!"data".equals(key1)){
        		continue;
        	}
        	JSONArray rankArray = jsonObject.getJSONArray(key1);
        	for(int i = 0; i < rankArray.length(); i++){
            	// 一支股票数据
            	String stockCapFlowStr = (String) rankArray.get(i);
            	LOGGER.info(i + " stockCapFlowStr: " + stockCapFlowStr);
            	if(StringUtils.isBlank(stockCapFlowStr)){
            		continue;
            	}
            	String[] stockCapFlowArray = stockCapFlowStr.split(",");
            	
            	StockCapFlow stockCapFlow = new StockCapFlow();
            	// tradeDate 不是执行程序的时间，而是文件中的时间。
            	stockCapFlow.setTradeDate(Timestamp.valueOf(stockCapFlowArray[15]));
            	stockCapFlow.setCode(stockCapFlowArray[1]);
            	// 如果没有收盘价，该股就是停牌状态，跳过.
            	String closeStr = stockCapFlowArray[3];
            	if(StringUtils.isBlank(closeStr) || "-".equals(StringUtils.trim(closeStr))){
            		continue;
            	}
            	stockCapFlow.setClose(Float.valueOf(closeStr));
            	stockCapFlow.setChangePercent(Float.valueOf(stockCapFlowArray[4]));
            	if(!NumberUtils.isNumber(stockCapFlowArray[5])){
            		continue;
            	}
            	stockCapFlow.setMainNetIn(new BigDecimal(stockCapFlowArray[5]));
            	stockCapFlow.setMainNetInPercent(Float.valueOf(stockCapFlowArray[6]));
            	stockCapFlow.setSuperLarge(new BigDecimal(stockCapFlowArray[7]));
            	stockCapFlow.setSuperLargePercent(Float.valueOf(stockCapFlowArray[8]));
            	stockCapFlow.setLarge(new BigDecimal(stockCapFlowArray[9]));
            	stockCapFlow.setLargePercent(Float.valueOf(stockCapFlowArray[10]));
            	stockCapFlow.setMiddle(new BigDecimal(stockCapFlowArray[11]));
            	stockCapFlow.setMiddlePercent(Float.valueOf(stockCapFlowArray[12]));
            	stockCapFlow.setSmall(new BigDecimal(stockCapFlowArray[13]));
            	stockCapFlow.setSmallPercent(Float.valueOf(stockCapFlowArray[14]));
            	stockCapFlow.setTimestamp(timestamp);
            	result.add(stockCapFlow);
        	}
        }
    	return result;
	}
	
	/**
	 * 从html文件解析个股历史资金情况: http://data.eastmoney.com/zjlx/600736.html
	 * @param capFlowHtmlFile
	 * @return
	 */
	public List<StockCapFlow> parseStockCapFlowHist(File capFlowHtmlFile){
		if(capFlowHtmlFile == null || !capFlowHtmlFile.exists() || !capFlowHtmlFile.getAbsolutePath().endsWith(".html")){
    		LOGGER.info(capFlowHtmlFile.getAbsolutePath() + " not exists or not a html file, return now...");
    		return null;
    	}

		LOGGER.info("processing file: " + capFlowHtmlFile.getAbsolutePath());
		
		Document doc = Jsoup.parse(StockUtils.getFileContent(capFlowHtmlFile));
		if(doc == null){
			LOGGER.error("doc is null, return now...");
			return null;
		}
		Iterator<Element> trIter = doc.select("#dt_1 > tbody > tr").iterator();
		if(trIter == null){
			LOGGER.error("trIter is null, reutrn null now...");
			return null;
		}
		List<StockCapFlow> list = new ArrayList<StockCapFlow>();
		// 股票代码.
		String code = capFlowHtmlFile.getName().split("_")[1].split("\\.")[0];
		Timestamp timestamp = new Timestamp(Calendar.getInstance()
				.getTimeInMillis());
		while(trIter.hasNext()){
			Element trElement = trIter.next();
			// 日期: #dt_1 > tbody > tr:nth-child(1) > td:nth-child(1)
			// 某些情况下一个数据也没有，显示： “没有相关数据."
			String tradeDateStr = trElement.select("> td:nth-child(1)").text().trim() + " 15:00:00";
			if(!tradeDateStr.startsWith("2")){
				continue;
			}
			Timestamp tradeDate = Timestamp.valueOf(tradeDateStr);
			// 收盘价: #dt_1 > tbody > tr:nth-child(1) > td:nth-child(2) > span
			Float close = Float.valueOf(trElement.select("> td:nth-child(2) > span").text().trim());
			// 涨跌幅: #dt_1 > tbody > tr:nth-child(1) > td:nth-child(3) > span
			// 某些情况下只有一个"-", 直接跳过这些情况.
			String changePercentStr = trElement.select("> td:nth-child(3) > span").text().trim().replace("%", "");
			if(!NumberUtils.isNumber(changePercentStr)){
				continue;
			}
			Float changePercent = Float.valueOf(changePercentStr);
			
			// 主力净流入: #dt_1 > tbody > tr:nth-child(1) > td:nth-child(4) > span
			String mainNetInStr = trElement.select("> td:nth-child(4) > span").text().trim();
			// 主力净占比: #dt_1 > tbody > tr:nth-child(1) > td:nth-child(5) > span
			String mainNetInPercentStr = trElement.select("> td:nth-child(5) > span").text().trim().replace("%", "");
			// 超大单净流入: #dt_1 > tbody > tr.over > td:nth-child(6) > span
			String superLargeStr = trElement.select("> td:nth-child(6) > span").text().trim();
			// 超大单净流入净占比: #dt_1 > tbody > tr.over > td:nth-child(7) > span
			String superLargePercentStr = trElement.select("> td:nth-child(7) > span").text().trim().replace("%", "");
			// 大单净流入: #dt_1 > tbody > tr.over > td:nth-child(8) > span
			String largeStr = trElement.select("> td:nth-child(8) > span").text().trim();
			// 大单净流入净占比: #dt_1 > tbody > tr.over > td:nth-child(9) > span
			String largePercentStr = trElement.select("> td:nth-child(9) > span").text().trim().replace("%", "");
			// 中单净流入: #dt_1 > tbody > tr.over > td:nth-child(10) > span
			String middleStr = trElement.select("> td:nth-child(10) > span").text().trim();
			// 中单净流入净占比: #dt_1 > tbody > tr.over > td:nth-child(11) > span
			String middlePercentStr = trElement.select("> td:nth-child(11) > span").text().trim().replace("%", "");
			// 小单净流入: #dt_1 > tbody > tr.over > td:nth-child(12) > span
			String smallStr = trElement.select("> td:nth-child(12) > span").text().trim();
			// 小单净流入净占比: #dt_1 > tbody > tr.over > td:nth-child(13) > span
			String smallPercentStr = trElement.select("> td:nth-child(13) > span").text().trim().replace("%", "");
			if(!isChineseNumeric(mainNetInStr) || !isChineseNumeric(superLargeStr) || !isChineseNumeric(largeStr) || 
				!isChineseNumeric(middleStr) || !isChineseNumeric(smallStr) || !NumberUtils.isNumber(mainNetInPercentStr) ||
				!NumberUtils.isNumber(superLargePercentStr) || !NumberUtils.isNumber(largePercentStr) || !NumberUtils.isNumber(middlePercentStr) ||
				!NumberUtils.isNumber(smallPercentStr)){
				continue;
			}
			
			BigDecimal mainNetIn = transChinese2Num(mainNetInStr);
			Float mainNetInPercent = Float.valueOf(mainNetInPercentStr);

			BigDecimal superLarge = transChinese2Num(superLargeStr);
			Float superLargePercent = Float.valueOf(superLargePercentStr);
			
			BigDecimal large = transChinese2Num(largeStr);
			Float largePercent = Float.valueOf(largePercentStr);
			
			BigDecimal middle = transChinese2Num(middleStr);
			Float middlePercent = Float.valueOf(middlePercentStr);
			
			BigDecimal small = transChinese2Num(smallStr);
			Float smallPercent = Float.valueOf(smallPercentStr);
			
			StockCapFlow capFlow = new StockCapFlow();
			capFlow.setTradeDate(tradeDate);
			capFlow.setCode(code);
			capFlow.setClose(close);
			capFlow.setChangePercent(changePercent);
			capFlow.setMainNetIn(mainNetIn);
			capFlow.setMainNetInPercent(mainNetInPercent);
			capFlow.setSuperLarge(superLarge);
			capFlow.setSuperLargePercent(superLargePercent);
			capFlow.setLarge(large);
			capFlow.setLargePercent(largePercent);
			capFlow.setMiddle(middle);
			capFlow.setMiddlePercent(middlePercent);
			capFlow.setSmall(small);
			capFlow.setSmallPercent(smallPercent);
			capFlow.setTimestamp(timestamp);
			list.add(capFlow);
		}
		return list;
	
	}
	
	private BigDecimal transChinese2Num(String str){
		BigDecimal result = null;
		if(str.endsWith("亿")){
			result = new BigDecimal(str.replace("亿", "")).multiply(new BigDecimal("10000"));
		}else if(str.endsWith("万")){
			result = new BigDecimal(str.replace("万", ""));
		}else{
			result = new BigDecimal(str);
		}
		return result;
	}
	
	private boolean isChineseNumeric(String str){
		String newStr = str.replace("亿", "").replace("万", "");
		return NumberUtils.isNumber(newStr);
	}
	
	
    /**
     * 下载html文件: http://quote.eastmoney.com/center/BKList.html#notion_0_0?sortRule=0
     * 解析左边的树中的板块下面的概念板块、行业板块获取对应的url.
     * 东方财富网的.
     * 
     * 2018-03-18: 不能使用了，新加的板块在下载的html中没有.
     */
	public void downloadParseBoardCode(String source) {
		// 下载html, 只需要下载一个,左侧的树是一样的.
		downloadBoardInfoFiles(new File(StockUtils.getDailyStockSaveDir("B")), "NOTION");
		
		String notionInfoFilePath = StockUtils.getDailyStockSaveDir("B") + "notionInfo.html";
		File notionInfoFile = new File(notionInfoFilePath);
    	if(!notionInfoFile.exists()){
    		LOGGER.error(notionInfoFile.getAbsolutePath() + " not exists, return now...");
    		return;
    	}
    	
    	Document doc = getNotionIndustryDoc(notionInfoFile);
		/*
		 * 概念
		 */
    	// 解析文件.
		List<NotionInfo> notionInfoList = parseNotionInfoFromDoc(doc, source);
		if(notionInfoList != null && !notionInfoList.isEmpty()){
			notionInfoService.deleteNotionInfoBySource(source);
			notionInfoService.insertNotionInfoBatch(notionInfoList);
		}
		
		/*
		 * 行业
		 */
    	// 解析文件.
		List<IndustryInfo> industryInfoList = parseIndustryInfoFromDoc(doc, source);
		if(industryInfoList != null && !industryInfoList.isEmpty()){
			industryInfoService.deleteIndustryInfoBySource(source);
			industryInfoService.insertIndustryInfoBatch(industryInfoList);
		}
		
	}
	
	public ExchangeInfoService getExchangeInfoService() {
		return exchangeInfoService;
	}

	public void setExchangeInfoService(ExchangeInfoService exchangeInfoService) {
		this.exchangeInfoService = exchangeInfoService;
	}

	public StockHistoryService getStockHistoryService() {
		return stockHistoryService;
	}

	public void setStockHistoryService(StockHistoryService stockHistoryService) {
		this.stockHistoryService = stockHistoryService;
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

	public NotionHotStocksService getNotionHotStocksService() {
		return notionHotStocksService;
	}

	public void setNotionHotStocksService(
			NotionHotStocksService notionHotStocksService) {
		this.notionHotStocksService = notionHotStocksService;
	}

	public IndustryHotStocksService getIndustryHotStocksService() {
		return industryHotStocksService;
	}

	public void setIndustryHotStocksService(
			IndustryHotStocksService industryHotStocksService) {
		this.industryHotStocksService = industryHotStocksService;
	}

	public IndexService getIndexService() {
		return indexService;
	}

	public void setIndexService(IndexService indexService) {
		this.indexService = indexService;
	}

	public NotionStockService getNotionStockService() {
		return notionStockService;
	}

	public void setNotionStockService(NotionStockService notionStockService) {
		this.notionStockService = notionStockService;
	}

	public IndustryStockService getIndustryStockService() {
		return industryStockService;
	}

	public void setIndustryStockService(IndustryStockService industryStockService) {
		this.industryStockService = industryStockService;
	}

	public StockFiveChangeService getStockFiveChangeService() {
		return stockFiveChangeService;
	}

	public void setStockFiveChangeService(
			StockFiveChangeService stockFiveChangeService) {
		this.stockFiveChangeService = stockFiveChangeService;
	}

}
