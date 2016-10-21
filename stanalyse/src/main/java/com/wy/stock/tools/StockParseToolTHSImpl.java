/**
 * 
 */
package com.wy.stock.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
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
import com.wy.stock.domain.StockFiveChange;
import com.wy.stock.service.IndexService;
import com.wy.stock.service.IndustryHotService;
import com.wy.stock.service.IndustryHotStocksService;
import com.wy.stock.service.IndustryInfoService;
import com.wy.stock.service.IndustryStockService;
import com.wy.stock.service.NotionHotService;
import com.wy.stock.service.NotionHotStocksService;
import com.wy.stock.service.NotionInfoService;
import com.wy.stock.service.NotionStockService;
import com.wy.stock.service.StockFiveChangeService;
import com.wy.stock.service.StockInfoService;
import com.wy.stock.utils.StockConstant;
import com.wy.stock.utils.StockUtils;

/**
 * 
 * @author leslie
 *
 */
public class StockParseToolTHSImpl implements StockParseToolTHS {

	private static Logger LOGGER = Logger.getLogger(StockParseToolTHSImpl.class
			.getName());
	
	private NotionInfoService notionInfoService;
	
	private IndustryInfoService industryInfoService;
	
	private NotionHotService notionHotService;
	
	private IndustryHotService industryHotService;
	
	private NotionHotStocksService notionHotStocksService;
	
	private IndustryHotStocksService industryHotStocksService;
	
	private IndexService indexService;
	
	private NotionStockService notionStockService;
	
	private IndustryStockService industryStockService;
	
	private StockFiveChangeService stockFiveChangeService;
	
	private StockInfoService stockInfoService;
	
    
	/**
	 * 解析html获取概念信息对象, NotionInfo
	 */
	public List<NotionInfo> parseNotionInfoFromDoc(Document notionInfoDoc) {
		//阿里巴巴概念 body > div.wrap > div.category.m_links > div > div:nth-child(1) > div > a:nth-child(1)
		//二胎概念: body > div.wrap > div.category.m_links > div > div:nth-child(1) > div > a:nth-child(18)
		
		// 分散染料: body > div.wrap > div.category.m_links > div > div:nth-child(2) > div > a:nth-child(1)
		
		// O2O概念: body > div.wrap > div.category.m_links > div > div:nth-child(3) > div > a:nth-child(1)
		
		// 网络安全: body > div.wrap > div.category.m_links > div > div:nth-child(5) > div > a:nth-child(1)
		Iterator<Element> divIter = notionInfoDoc.select("body > div.wrap > div.category.m_links > div > div").iterator();
		if(divIter == null){
			LOGGER.error("divIter is null, reutrn null now...");
			return null;
		}
		
		List<NotionInfo> list = new ArrayList<NotionInfo>();
		Timestamp timestamp = new Timestamp(Calendar.getInstance()
				.getTimeInMillis());
		while(divIter.hasNext()){
			Element divElement = divIter.next();
			
			Iterator<Element> aIter = divElement.select("> div > a").iterator();
			if(aIter == null){
				continue;
			}
			
			while(aIter.hasNext()){
				Element aElement = aIter.next();
				
				// url标识
				String notionUrl = aElement.attr("href");
				// notionName  替换掉"/", 方便目录的使用.
				String notionName = aElement.text().replace("/", "-");
				
				NotionInfo notionInfo = new NotionInfo();
				notionInfo.setNotionUrl(notionUrl);
				notionInfo.setNotionName(notionName);
				notionInfo.setSource(StockConstant.THS_FLAG);
				// 这里先设为0.
				notionInfo.setCorpsNum(0);
				notionInfo.setTimestamp(timestamp);
				list.add(notionInfo);
			}
		}
		return list;
	}
	
	/**
	 * 解析html获取概念信息对象, IndustryInfo
	 */
	public List<IndustryInfo> parseIndustryInfoFromDoc(Document industryInfoDoc) {
		// 白色家电: body > div.wrap > div.category.m_links > div > div:nth-child(1) > div > a:nth-child(1)
		// 房地产开发: body > div.wrap > div.category.m_links > div > div:nth-child(2) > div > a:nth-child(1)
		// 物流: body > div.wrap > div.category.m_links > div > div:nth-child(5) > div > a:nth-child(1)
		Iterator<Element> divIter = industryInfoDoc.select("body > div.wrap > div.category.m_links > div > div").iterator();
		if(divIter == null){
			LOGGER.error("divIter is null, reutrn null now...");
			return null;
		}
		
		List<IndustryInfo> list = new ArrayList<IndustryInfo>();
		Timestamp timestamp = new Timestamp(Calendar.getInstance()
				.getTimeInMillis());
		while(divIter.hasNext()){
			Element divElement = divIter.next();
			
			Iterator<Element> aIter = divElement.select("> div > a").iterator();
			if(aIter == null){
				continue;
			}
			
			while(aIter.hasNext()){
				Element aElement = aIter.next();
				
				// url标识
				String industryUrl = aElement.attr("href");
				// industryName
				String industryName = aElement.text().replace("/", "-");
				
				IndustryInfo industryInfo = new IndustryInfo();
				industryInfo.setIndustryUrl(industryUrl);
				industryInfo.setIndustryName(industryName);
				industryInfo.setSource(StockConstant.THS_FLAG);
				// 这里先设为0.
				industryInfo.setCorpsNum(0);
				industryInfo.setTimestamp(timestamp);
				list.add(industryInfo);
			}
		}
		return list;
	}

	/**
	 * 解析概念热点html文件 notionHot.html, 只解析第一页的数据，后面的暂时不考虑.
	 * 
	 */
	public void persistNotionHotFromHtml(File html) {
		if(!html.exists()){
			LOGGER.error(html.getAbsolutePath() + " not exists, return null now...");
			return;
		}
		final String TYPE = StockConstant.THS_NOTION_TYPE1;
		// 不存在corpsNum
		int corpsNum = -1;
		Timestamp timestamp = new Timestamp(Calendar.getInstance()
				.getTimeInMillis());
		Document notionDoc = getNotionIndustryDoc(html);
		// body > div.container.w1200 > div.category.boxShadow.m_links > div > div:nth-child(1) > div > a:nth-child(1)
		// body > div.container.w1200 > div.category.boxShadow.m_links > div > div:nth-child(1) > div > a:nth-child(5)
		// body > div.container.w1200 > div.category.boxShadow.m_links > div > div:nth-child(2) > div > a:nth-child(1)
		Iterator<Element> outerIter = notionDoc.select("body > div.container.w1200 > div.category.boxShadow.m_links > div > div").iterator();
		if(outerIter == null){
			LOGGER.error("outerIter is null, reutrn null now...");
			return;
		}
		
		List<NotionInfo> notionInfoList = new ArrayList<NotionInfo>();
		while(outerIter.hasNext()){
		   	Element innerElement = outerIter.next();
			Iterator<Element> innerIter = innerElement.select("> div > a").iterator();
			if(innerIter == null){
				LOGGER.error("innerIter is null, continue....");
				continue;
			}
			while(innerIter.hasNext()){
				Element element = innerIter.next();
				String notionName = element.select(" a").text();
				String notionUrl = element.select("a").attr("href");
				if(StringUtils.isEmpty(notionUrl)){
					LOGGER.error("notionUrl is empty, continue");
					continue;
				}
				String notionCode = notionUrl.split("/")[6].replace("gn_", "");
				
    			NotionInfo notionInfoNew = new NotionInfo();
    			notionInfoNew.setType(TYPE);
    			notionInfoNew.setNotionUrl(notionUrl);
    			notionInfoNew.setNotionName(notionName);
    			notionInfoNew.setNotionCode(notionCode);
    			notionInfoNew.setTimestamp(timestamp);
    			notionInfoNew.setSource(StockConstant.THS_FLAG);
    			notionInfoNew.setCorpsNum(corpsNum);
    			notionInfoList.add(notionInfoNew);
			}
		}
		// 先删除ST_NOTION_INFO中已经存在的，然后插入.
		notionInfoService.deleteNotionInfoByType(TYPE, StockConstant.THS_FLAG);
		notionInfoService.insertNotionInfoBatch(notionInfoList);
		
		/*
		Iterator<Element> trIter = notionDoc.select("body > div.wrap > div.page-main > div.clearfix > div > table > tbody > tr").iterator();
		if(trIter == null){
			LOGGER.error("trIter is null, reutrn null now...");
			return null;
		}
		
		List<NotionHot> list = new ArrayList<NotionHot>();
		// 取文件里的更新时间: 更新时间：2016-06-03 11:58:07 body > div.wrap > div.page-main > div.clearfix > span
		String updateTimeStr = notionDoc.select("body > div.wrap > div.page-main > div.clearfix > span").text();
		int dateIndex = updateTimeStr.indexOf("20");
		// 2016-06-03 11:58:07
		String updateTimeStrNew = updateTimeStr.substring(dateIndex);
		
		
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.valueOf(updateTimeStrNew.substring(0, 4)));
        cal.set(Calendar.MONTH, Integer.valueOf(updateTimeStrNew.substring(5, 7)) - 1);
        cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(updateTimeStrNew.substring(8, 10)));
        cal.set(Calendar.HOUR, -12);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
		Timestamp tradeDate = new Timestamp(cal.getTimeInMillis());
		Timestamp timestamp = new Timestamp(Calendar.getInstance()
				.getTimeInMillis());
		String tradeDateStr = new SimpleDateFormat("YYYY-MM-dd").format(tradeDate);
		
		// 重新设置tradeDate, 确保最后是 00:00:00, 上面Calendar的方式容易出现 00:00:01
		tradeDate = Timestamp.valueOf(tradeDateStr + " 00:00:00");
		while(trIter.hasNext()){
			Element trElement = trIter.next();
			// 序号 body > div.wrap > div.page-main > div.clearfix > div > table > tbody > tr:nth-child(1) > td.first.tc.highlight
			int rank = Integer.valueOf(trElement.select("> td:nth-child(1)").text());
			// 板块名称 body > div.wrap > div.page-main > div.clearfix > div > table > tbody > tr:nth-child(1) > td.tl.highlight > a
			String notionName = trElement.select("> td:nth-child(2) > a").text();
			// 板块url
			String notionUrl = trElement.select("> td:nth-child(2) > a").attr("href");
			// 公司家数 body > div.wrap > div.page-main > div.clearfix > div > table > tbody > tr:nth-child(1) > td.highlight
			int corpsNum = Integer.valueOf(trElement.select("> td:nth-child(3)").text());
			// 平均价格 body > div.wrap > div.page-main > div.clearfix > div > table > tbody > tr:nth-child(1) > td.c-rise.highlight
			Float avgPrice = Float.valueOf(trElement.select("> td:nth-child(4)").text());
			// 涨跌幅 body > div.wrap > div.page-main > div.clearfix > div > table > tbody > tr:nth-child(1) > td.c-rise.cur.highlight
			Float changePercent = Float.valueOf(trElement.select("> td:nth-child(5)").text().replace("%", ""));
			// 总成交量(万手)
			// 总成交额(亿)
			// 领涨股
			String riseLeadStockName = trElement.select("> td:nth-child(9)").text();
			// 领涨股涨跌幅
			Float riseLeadStockChangePercent = Float.valueOf(trElement.select("> td:nth-child(11)").text().replace("%", ""));
			
			NotionHot notionHot = new NotionHot();
			notionHot.setTradeDate(tradeDate);
			notionHot.setTradeDateStr(tradeDateStr);
    		notionHot.setRank(rank);
    		notionHot.setNotionName(notionName);
    		notionHot.setChangePercent(changePercent);
    		// 设置默认值
    		notionHot.setTotalMarketCap(-1f);
    		notionHot.setTurnoverRate(-1f);
    		notionHot.setRiseStocksNum(-1);
    		notionHot.setFallStocksNum(-1);
    		notionHot.setRiseLeadStockName(riseLeadStockName);
    		notionHot.setRiseLeadStockChangePercent(riseLeadStockChangePercent);
    		notionHot.setTimestamp(timestamp);
    		notionHot.setSource(StockConstant.THS_FLAG);
    		notionHot.setAvgPrice(avgPrice);
    		list.add(notionHot);
    		
    		// 检查ST_NOTION_INFO中是否存在，不存在插入，存在更新.
    		NotionInfo notionInfo = notionInfoService.queryNotionInfoByName(notionName, StockConstant.THS_FLAG);
    		if(notionInfo != null){
        		NotionInfo notionInfoNew = new NotionInfo();
        		notionInfoNew.setNotionUrl(notionUrl);
        		notionInfoNew.setTimestamp(timestamp);
        		notionInfoNew.setCorpsNum(corpsNum);
        		notionInfoNew.setNotionName(notionName);
        		notionInfoNew.setSource(StockConstant.THS_FLAG);
        		// 不更新，没有notionCode.
        		String notionCode = "";
        		if(!StringUtils.isEmpty(notionUrl) && corpsNum > 0 && !StringUtils.isEmpty(notionCode)
        				&& !StringUtils.isEmpty(notionName)){
        			notionInfoService.updateByNotionName(notionInfoNew);
        		}
        		// 更新corpsNum
        		if(!StringUtils.isEmpty(notionName) && corpsNum > 0 && corpsNum != notionInfo.getCorpsNum()){
        			notionInfoService.updateCorpsNumByNotionName(notionInfoNew);
        		}
    		}else{
    			NotionInfo notionInfoNew = new NotionInfo();
    			notionInfoNew.setNotionUrl(notionUrl);
    			notionInfoNew.setNotionName(notionName);
    			notionInfoNew.setTimestamp(timestamp);
    			notionInfoNew.setSource(StockConstant.THS_FLAG);
    			notionInfoNew.setCorpsNum(corpsNum);
    			notionInfoService.insertNotionInfo(notionInfoNew);
    		}
		}
		return list;
		*/
	}
	
	/**
	 * 解析概念热点html文件 industryHot.html, 只解析第一页的数据，后面的暂时不考虑.
	 * 
	 */
	public List<IndustryHot> parseIndustryHotFromHtml(File html) {
		if(!html.exists()){
			LOGGER.error(html.getAbsolutePath() + " not exists, return null now...");
			return null;
		}
		Document industryDoc = getNotionIndustryDoc(html);
		Iterator<Element> trIter = industryDoc.select("body > div.wrap > div.table_wrap > table > tbody > tr").iterator();
		if(trIter == null){
			LOGGER.error("trIter is null, reutrn null now...");
			return null;
		}
		List<IndustryHot> list = new ArrayList<IndustryHot>();
		// 取文件里的更新时间: 更新时间：2016-06-03 11:58:07 body > div.wrap > div.table_wrap > div > div.update_time
		String updateTimeStr = industryDoc.select("body > div.wrap > div.table_wrap > div > div.update_time").text();
		int dateIndex = updateTimeStr.indexOf("20");
		// 2016-06-03 11:58:07
		String updateTimeStrNew = updateTimeStr.substring(dateIndex);
		
		
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.valueOf(updateTimeStrNew.substring(0, 4)));
        cal.set(Calendar.MONTH, Integer.valueOf(updateTimeStrNew.substring(5, 7)) - 1);
        cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(updateTimeStrNew.substring(8, 10)));
        cal.set(Calendar.HOUR, -12);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
		Timestamp tradeDate = new Timestamp(cal.getTimeInMillis());
		Timestamp timestamp = new Timestamp(Calendar.getInstance()
				.getTimeInMillis());
		String tradeDateStr = new SimpleDateFormat("YYYY-MM-dd").format(tradeDate);
		// 重新设置tradeDate, 确保最后是 00:00:00, 上面Calendar的方式容易出现 00:00:01
		tradeDate = Timestamp.valueOf(tradeDateStr + " 00:00:00");
		while(trIter.hasNext()){
			Element trElement = trIter.next();
			// 序号
			int rank = Integer.valueOf(trElement.select("> td:nth-child(1)").text());
			// 板块名称
			String industryName = trElement.select("> td:nth-child(2) > a").text();
			// 板块url
			String industryUrl = trElement.select("> td:nth-child(2) > a").attr("href");
			// 公司家数
			int corpsNum = Integer.valueOf(trElement.select("> td:nth-child(3)").text());
			// 平均价格
			Float avgPrice = Float.valueOf(trElement.select("> td:nth-child(4)").text());
			// 涨跌幅
			Float changePercent = Float.valueOf(trElement.select("> td:nth-child(5)").text().replace("%", ""));
			// 总成交量(万手)
			// 总成交额(亿)
			// 领涨股
			String riseLeadStockName = trElement.select("> td:nth-child(9)").text();
			// 领涨股涨跌幅
			Float riseLeadStockChangePercent = Float.valueOf(trElement.select("> td:nth-child(11)").text().replace("%", ""));
			
			IndustryHot industryHot = new IndustryHot();
			industryHot.setTradeDate(tradeDate);
			industryHot.setTradeDateStr(tradeDateStr);
			industryHot.setRank(rank);
			industryHot.setIndustryName(industryName);
			industryHot.setChangePercent(changePercent);
    		// 设置默认值
			industryHot.setTotalMarketCap(-1f);
			industryHot.setTurnoverRate(-1f);
			industryHot.setRiseStocksNum(-1);
			industryHot.setFallStocksNum(-1);
			industryHot.setRiseLeadStockName(riseLeadStockName);
			industryHot.setRiseLeadStockChangePercent(riseLeadStockChangePercent);
			industryHot.setTimestamp(timestamp);
			industryHot.setSource(StockConstant.THS_FLAG);
			industryHot.setAvgPrice(avgPrice);
    		list.add(industryHot);
    		
    		// 检查ST_INDUSTRY_INFO中是否存在，不存在插入，存在更新.
    		IndustryInfo industryInfo = industryInfoService.queryIndustryInfoByName(industryName, StockConstant.THS_FLAG);
    		if(industryInfo != null){
        		IndustryInfo industryInfoNew = new IndustryInfo();
        		industryInfoNew.setIndustryUrl(industryUrl);
        		industryInfoNew.setTimestamp(timestamp);
        		industryInfoNew.setCorpsNum(corpsNum);
        		industryInfoNew.setIndustryName(industryName);
        		industryInfoNew.setSource(StockConstant.THS_FLAG);
        		// 不更新，没有industryCode.
        		String industryCode = "";
        		if(!StringUtils.isEmpty(industryUrl) && corpsNum > 0 && !StringUtils.isEmpty(industryCode)
        				&& !StringUtils.isEmpty(industryName)){
        			industryInfoService.updateByIndustryName(industryInfoNew);
        		}
        		// 更新corpsNum
        		if(!StringUtils.isEmpty(industryName) && corpsNum > 0 && corpsNum != industryInfo.getCorpsNum()){
        			industryInfoService.updateCorpsNumByIndustryName(industryInfoNew);
        		}
    		}else{
    			IndustryInfo industryInfoNew = new IndustryInfo();
    			industryInfoNew.setIndustryUrl(industryUrl);
    			industryInfoNew.setIndustryName(industryName);
    			industryInfoNew.setTimestamp(timestamp);
    			industryInfoNew.setSource(StockConstant.THS_FLAG);
    			industryInfoNew.setCorpsNum(corpsNum);
    			industryInfoService.insertIndustryInfo(industryInfoNew);
    		}
		}
		return list;
	}
	
	/**
	 * 解析概念热点板块下的股票信息 notion_大飞机.html
	 * @param jsonFile
	 * @return
	 */
	public List<NotionHotStocks> parseNotionHotStocksFromHtml(File html) {
		if(!html.exists()){
			LOGGER.error(html.getAbsolutePath() + " not exists, return null now...");
			return null;
		}
		LOGGER.info("processing file: " + html.getAbsolutePath());
		
		Document notionHotDoc = getNotionIndustryDoc(html);
		// 取文件里的更新时间: 更新时间：2016-06-04 10:39:10 body > div.wrap > div.page-main > div.clearfix > span
		String updateTimeStr = notionHotDoc.select("body > div.wrap > div.page-main > div.clearfix > span").text();
		int dateIndex = updateTimeStr.indexOf("20");
		// 2016-06-04 10:39:10
		String updateTimeStrNew = updateTimeStr.substring(dateIndex);
		
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.valueOf(updateTimeStrNew.substring(0, 4)));
        cal.set(Calendar.MONTH, Integer.valueOf(updateTimeStrNew.substring(5, 7)) - 1);
        cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(updateTimeStrNew.substring(8, 10)));
        cal.set(Calendar.HOUR, -12);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Timestamp tradeDate = new Timestamp(cal.getTimeInMillis());
        Timestamp timestamp = new Timestamp(Calendar.getInstance()
				.getTimeInMillis());
        
        // 板块代码 body > div.wrap > div.page-main > div:nth-child(1) > div > div.stock.clearfix > div.stock-name.fl > span
        String notionCode = notionHotDoc.select("body > div.wrap > div.page-main > div:nth-child(1) > div > div.stock.clearfix > div.stock-name.fl > span").text();
        String notionName = html.getName().split("_")[1].replace(".html", "");
        NotionInfo updateInfo = new NotionInfo();
        updateInfo.setNotionCode(notionCode);
        updateInfo.setNotionName(notionName);
        updateInfo.setSource(StockConstant.THS_FLAG);
        notionInfoService.updateNotionCodeByNotionName(updateInfo);
        
		Iterator<Element> trIter = notionHotDoc.select("body > div.wrap > div.page-main > div.clearfix > div > table > tbody > tr").iterator();
		if(trIter == null){
			LOGGER.error("trIter is null, reutrn null now...");
			return null;
		}
		
		List<NotionHotStocks> list = new ArrayList<NotionHotStocks>();
		// 记入 ST_NOTION_STOCK, 先排除ST_NOTION_STOCK 中已经有的.
		List<NotionStock> notionStockList = new ArrayList<NotionStock>();
		List<String> notionStockExisted = notionStockService.queryCodeByNotionCode(notionCode, StockConstant.THS_FLAG);
		while(trIter.hasNext()){
			Element trElement = trIter.next();
			// 序号
			int rank = Integer.valueOf(trElement.select("> td:nth-child(1)").text());
			// 股票代码 body > div.wrap > div.page-main > div.clearfix > div > table > tbody > tr:nth-child(1) > td.tc.highlight > a
			String code = trElement.select("> td:nth-child(2) > a").text();
			// 股票名称 body > div.wrap > div.page-main > div.clearfix > div > table > tbody > tr:nth-child(1) > td.tc.highlight > a
			String stockName = trElement.select("> td:nth-child(3) > a").text();
			// 最新价 body > div.wrap > div.page-main > div.clearfix > div > table > tbody > tr:nth-child(1) > td:nth-child(4)
			Float newPrice = Float.valueOf(trElement.select("> td:nth-child(4)").text());
			// 涨跌幅
			Float changePercent = Float.valueOf(trElement.select("> td:nth-child(6)").text().replace("%", ""));
			
			NotionHotStocks notionHotStocks = new NotionHotStocks();
			notionHotStocks.setTradeDate(tradeDate);
			notionHotStocks.setRank(rank);
			notionHotStocks.setNotionName(notionName);
			notionHotStocks.setCode(code);
			notionHotStocks.setStockName(stockName);
			notionHotStocks.setNewPrice(newPrice);
			notionHotStocks.setChangePercent(changePercent);
			notionHotStocks.setTimestamp(timestamp);
    		list.add(notionHotStocks);
    		
    		if(notionStockExisted != null && !notionStockExisted.contains(code)){
        		NotionStock notionStock = new NotionStock();
        		notionStock.setNotionCode(notionCode);
        		notionStock.setNotionName(notionName);
        		notionStock.setCode(code);
        		notionStock.setStockName(stockName);
        		notionStock.setTimestamp(timestamp);
        		notionStock.setSource(StockConstant.THS_FLAG);
        		notionStockList.add(notionStock);
    		}
		}
		
		if(notionStockList != null && !notionStockList.isEmpty()){
			notionStockService.insertNotionStockBatch(notionStockList);
		}
    	return list;
	}
	
	/**
	 * 解析页面: http://q.10jqka.com.cn/gn/detail/code/gn_300168/
	 * 获取notionHot信息.
	 */
	public List<NotionHot> parseNotionHotStocksFromStocksHtml(File htmlDir) {
		if(!htmlDir.exists()){
			LOGGER.error(htmlDir.getAbsolutePath() + " not exists, return null now...");
			return null;
		}
		LOGGER.info("processing Dir: " + htmlDir.getAbsolutePath());
		/*
		 * 页面中不存在tradeDate, 直接获取当天日期
		 */
		String tradeDateStr = new SimpleDateFormat("YYYY-MM-dd 00:00:00").format(Calendar.getInstance().getTime());
        Timestamp tradeDate = Timestamp.valueOf(tradeDateStr);
        Timestamp timestamp = new Timestamp(Calendar.getInstance()
				.getTimeInMillis());
		
        /*
         * 处理所有notionUrl对应的页面, 即尾部是 _0.html的文件.
         */
		com.wy.stock.utils.FileNameSelector selector = new com.wy.stock.utils.FileNameSelector("notionHot" + "_", "_0.html");
		File[] htmls = htmlDir.listFiles(selector);
		if(htmls != null && htmls.length > 0){
			for(File html : htmls){
				String fileName = html.getAbsolutePath();
				Document notionHotDoc = getNotionIndustryDoc(html);
				String notionName = html.getName().split("_")[1];
				String changePercentStr = notionHotDoc.select("body > div.container.w1200 > div:nth-child(3) > div.body > div > div.board-main.w900 > div.heading > div.board-infos > dl:nth-child(6) > dd").text();
				if(!NumberUtils.isNumber(changePercentStr)){
					LOGGER.error(fileName + " changePerchent  is not valid, skipped.");
					continue;
				}
				Float changePercent = Float.valueOf(changePercentStr);
				
			}
		

        
        
        
        
        
		Iterator<Element> trIter = notionHotDoc.select("body > div.wrap > div.page-main > div.clearfix > div > table > tbody > tr").iterator();
		if(trIter == null){
			LOGGER.error("trIter is null, reutrn null now...");
			return null;
		}
		
		List<NotionHotStocks> list = new ArrayList<NotionHotStocks>();
		// 记入 ST_NOTION_STOCK, 先排除ST_NOTION_STOCK 中已经有的.
		List<NotionStock> notionStockList = new ArrayList<NotionStock>();
		List<String> notionStockExisted = notionStockService.queryCodeByNotionCode(notionCode, StockConstant.THS_FLAG);
		while(trIter.hasNext()){
			Element trElement = trIter.next();
			// 序号
			int rank = Integer.valueOf(trElement.select("> td:nth-child(1)").text());
			// 股票代码 body > div.wrap > div.page-main > div.clearfix > div > table > tbody > tr:nth-child(1) > td.tc.highlight > a
			String code = trElement.select("> td:nth-child(2) > a").text();
			// 股票名称 body > div.wrap > div.page-main > div.clearfix > div > table > tbody > tr:nth-child(1) > td.tc.highlight > a
			String stockName = trElement.select("> td:nth-child(3) > a").text();
			// 最新价 body > div.wrap > div.page-main > div.clearfix > div > table > tbody > tr:nth-child(1) > td:nth-child(4)
			Float newPrice = Float.valueOf(trElement.select("> td:nth-child(4)").text());
			// 涨跌幅
			Float changePercent = Float.valueOf(trElement.select("> td:nth-child(6)").text().replace("%", ""));
			
			NotionHotStocks notionHotStocks = new NotionHotStocks();
			notionHotStocks.setTradeDate(tradeDate);
			notionHotStocks.setRank(rank);
			notionHotStocks.setNotionName(notionName);
			notionHotStocks.setCode(code);
			notionHotStocks.setStockName(stockName);
			notionHotStocks.setNewPrice(newPrice);
			notionHotStocks.setChangePercent(changePercent);
			notionHotStocks.setTimestamp(timestamp);
    		list.add(notionHotStocks);
    		
    		if(notionStockExisted != null && !notionStockExisted.contains(code)){
        		NotionStock notionStock = new NotionStock();
        		notionStock.setNotionCode(notionCode);
        		notionStock.setNotionName(notionName);
        		notionStock.setCode(code);
        		notionStock.setStockName(stockName);
        		notionStock.setTimestamp(timestamp);
        		notionStock.setSource(StockConstant.THS_FLAG);
        		notionStockList.add(notionStock);
    		}
		}
		
		if(notionStockList != null && !notionStockList.isEmpty()){
			notionStockService.insertNotionStockBatch(notionStockList);
		}
    	return list;
	}
	
	/**
	 * 解析行业热点板块下的股票信息
	 * @param html
	 * @return
	 */
	public List<IndustryHotStocks> parseIndustryHotStocksFromHtml(File html) {
		if(!html.exists()){
			LOGGER.error(html.getAbsolutePath() + " not exists, return null now...");
			return null;
		}
		LOGGER.info("processing file: " + html.getAbsolutePath());
		
		Document industryHotDoc = getNotionIndustryDoc(html);
		// 取文件里的更新时间: 更新时间：2016-06-04 10:51:08 body > div.wrap > div.wrap.dybk > div.table_wrap > div > div.update_time
		String updateTimeStr = industryHotDoc.select("body > div.wrap > div.wrap.dybk > div.table_wrap > div > div.update_time").text();
		int dateIndex = updateTimeStr.indexOf("20");
		// 2016-06-04 10:51:08
		String updateTimeStrNew = updateTimeStr.substring(dateIndex);
		
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.valueOf(updateTimeStrNew.substring(0, 4)));
        cal.set(Calendar.MONTH, Integer.valueOf(updateTimeStrNew.substring(5, 7)) - 1);
        cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(updateTimeStrNew.substring(8, 10)));
        cal.set(Calendar.HOUR, -12);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Timestamp tradeDate = new Timestamp(cal.getTimeInMillis());
        Timestamp timestamp = new Timestamp(Calendar.getInstance()
				.getTimeInMillis());
        
        // 板块代码 body > div.wrap > div.wrap.dybk > div.flash_wrap.bt1 > div.stockinfo.clearfix > div.stock_name > span
        String industryCode = industryHotDoc.select("body > div.wrap > div.wrap.dybk > div.flash_wrap.bt1 > div.stockinfo.clearfix > div.stock_name > span").text().replace("（", "").replace("）", "");
        String industryName = html.getName().split("_")[1].replace(".html", "");
        IndustryInfo updateInfo = new IndustryInfo();
        updateInfo.setIndustryCode(industryCode);
        updateInfo.setIndustryName(industryName);
        updateInfo.setSource(StockConstant.THS_FLAG);
        industryInfoService.updateIndustryCodeByIndustryName(updateInfo);
        
		Iterator<Element> trIter = industryHotDoc.select("body > div.wrap > div.wrap.dybk > div.table_wrap > table > tbody > tr").iterator();
		if(trIter == null){
			LOGGER.error("trIter is null, reutrn null now...");
			return null;
		}
		
		List<IndustryHotStocks> list = new ArrayList<IndustryHotStocks>();
		// 记入 ST_INDUSTRY_STOCK, 先排除ST_INDUSTRY_STOCK 中已经有的.
		List<IndustryStock> industryStockList = new ArrayList<IndustryStock>();
		List<String> industryStockExisted = industryStockService.queryCodeByIndustryCode(industryCode, StockConstant.THS_FLAG);
		while(trIter.hasNext()){
			Element trElement = trIter.next();
			// 序号 body > div.wrap > div.wrap.dybk > div.table_wrap > table > tbody > tr:nth-child(1) > td.first.tc
			int rank = Integer.valueOf(trElement.select("> td:nth-child(1)").text());
			// 股票代码 
			String code = trElement.select("> td:nth-child(2) > a").text();
			// 股票名称 
			String stockName = trElement.select("> td:nth-child(3) > a").text();
			// 最新价 
			Float newPrice = Float.valueOf(trElement.select("> td:nth-child(4)").text());
			// 涨跌幅
			Float changePercent = Float.valueOf(trElement.select("> td:nth-child(6)").text().replace("%", ""));
			
			IndustryHotStocks industryHotStocks = new IndustryHotStocks();
			industryHotStocks.setTradeDate(tradeDate);
			industryHotStocks.setRank(rank);
			industryHotStocks.setIndustryName(industryName);
			industryHotStocks.setCode(code);
			industryHotStocks.setStockName(stockName);
			industryHotStocks.setNewPrice(newPrice);
			industryHotStocks.setChangePercent(changePercent);
			industryHotStocks.setTimestamp(timestamp);
    		list.add(industryHotStocks);
    		
    		if(industryStockExisted != null && !industryStockExisted.contains(code)){
        		IndustryStock industryStock = new IndustryStock();
        		industryStock.setIndustryCode(industryCode);
        		industryStock.setIndustryName(industryName);
        		industryStock.setCode(code);
        		industryStock.setStockName(stockName);
        		industryStock.setTimestamp(timestamp);
        		industryStock.setSource(StockConstant.THS_FLAG);
        		industryStockList.add(industryStock);
    		}
		}
		
		if(industryStockList != null && !industryStockList.isEmpty()){
			industryStockService.insertIndustryStockBatch(industryStockList);
		}
		
    	return list;
	}
	
	/**
	 * 解析notion_大飞机.html industry_国防军工.html, 并将结果存入 ST_NOTION_HOT_STOCKS, ST_INDUSTRY_HOT_STOCKS.
	 * @param date
	 * @param type
	 */
	public void persistNotionIndustryHotStocksFromhtml(String date, String type){
		String dirPath = StockConstant.BOARD_HOT_FILE_PATH + "20" + date.substring(0, 2) + File.separatorChar + 
				 date.substring(2, 4) + File.separatorChar + date.substring(4, 6) + File.separatorChar;
		if("NOTION".equals(type)){
			File parent = new File(dirPath);
			com.wy.stock.utils.FileNameSelector selector = new com.wy.stock.utils.FileNameSelector(StockConstant.NOTION_IDENTIFIER + "_", ".html");
			File[] notionFiles = parent.listFiles(selector);
			if(notionFiles != null && notionFiles.length > 0){
				for(File file : notionFiles){
					List<NotionHotStocks> notionHotStocksList = parseNotionHotStocksFromHtml(file);
					if(notionHotStocksList != null && !notionHotStocksList.isEmpty()){
						notionHotStocksService.insertNotionHotStocksBatch(notionHotStocksList);
					}
				}
			}
		}else if("INDUSTRY".equals(type)){
			File parent = new File(dirPath);
			com.wy.stock.utils.FileNameSelector selector = new com.wy.stock.utils.FileNameSelector(StockConstant.INDUSTRY_IDENTIFIER + "_", ".html");
			File[] industryFiles = parent.listFiles(selector);
			if(industryFiles != null && industryFiles.length > 0){
				for(File file : industryFiles){
					List<IndustryHotStocks> industryHotStocksList = parseIndustryHotStocksFromHtml(file);
					if(industryHotStocksList != null && !industryHotStocksList.isEmpty()){
						industryHotStocksService.insertIndustryHotStocksBatch(industryHotStocksList);
					}
				}
			}
		}
	}
	
	public void persistNotionIndustryHotFromStocksHtml(File htmlDir){
		if(htmlDir == null){
			LOGGER.error("htmlDir is null, return now...");
			return;
		}
		if(!htmlDir.exists()){
			LOGGER.error(htmlDir.getAbsolutePath() + " not exists, return now....");
			return;
		}
		
		List<NotionHot> notionHotList = parseNotionHotStocksFromStocksHtml(htmlDir);
		if(notionHotList != null && !notionHotList.isEmpty()){
			notionHotService.insertNotionHotBatch(notionHotList);
		}
	}
	
	public void persistNotionIndustryHotStocksFromJson(String date, String type){
		String dirPath = StockConstant.BOARD_HOT_FILE_PATH + "20" + date.substring(0, 2) + File.separatorChar + 
				 date.substring(2, 4) + File.separatorChar + date.substring(4, 6) + File.separatorChar;
		String currDateStr = "20" + date.substring(0, 2) + "-" + date.substring(2, 4) + "-" + date.substring(4, 6);
		Map<String, String> codeNameMap = stockInfoService.queryStockCodeNameMap();
        Timestamp timestamp = new Timestamp(Calendar.getInstance()
				.getTimeInMillis());
		if("NOTION".equals(type)){
			File parent = new File(dirPath);
			com.wy.stock.utils.FileNameSelector selector = new com.wy.stock.utils.FileNameSelector(StockConstant.NOTION_HOT_IDENTIFIER + "_", ".json");
			File[] notionFiles = parent.listFiles(selector);
			
			List<NotionHotStocks> notionHotStocksList = new ArrayList<NotionHotStocks>();
			if(notionFiles != null && notionFiles.length > 0){
				for(File jsonFile : notionFiles){
					LOGGER.info("processing file: " + jsonFile.getAbsolutePath());
					if(jsonFile.getAbsolutePath().split("_").length != 3){
						continue;
					}
					// 从文件名中获取notionName
					String notionName = jsonFile.getAbsolutePath().split("_")[1];
					// 从文件名中解析出当前页，用于计算序号.
					int currPage = Integer.valueOf(jsonFile.getAbsolutePath().split("_")[2].split("\\.")[0]);
					String content = StockUtils.getFileContent(jsonFile);
					String jsonString = "{" + content;

			        JSONObject jsonObject = new JSONObject(jsonString);
			        @SuppressWarnings("unchecked")
					Iterator<String> iterator1 = jsonObject.keys();
			        while (iterator1.hasNext()) {
			        	String key1 = (String) iterator1.next();
			        	if(!"data".equals(key1)){
			        		continue;
			        	}
			        	// 取第一个
			        	JSONArray notionArray = jsonObject.getJSONArray(key1);
			        	for(int i = 0; i < notionArray.length(); i++){
			        		JSONObject jsonObj = notionArray.getJSONObject(i);
			        		// 交易日期
			        		String tradeDateStr = jsonObj.getString("rtime").split(" ")[0];
			        		if(tradeDateStr.compareTo(currDateStr) < 0){
			        			continue;
			        		}
			        		Timestamp tradeDate = Timestamp.valueOf(tradeDateStr + " 00:00:00");
			        		// 序号
			        		int rank = StockConstant.NOTION_HOT_PAGE_SIZE * (currPage - 1) + i + 1;
			        		// 股票代码
			        		String code = jsonObj.getString("stockcode");
			        		// 股票名称
			        		String stockName = codeNameMap.get(code);
			        		if(StringUtils.isEmpty(stockName)){
			        			LOGGER.info("=== code: " + code + " stockName is null.");
			        			continue;
			        		}
			        		// 最新价
			        		Float newPrice = Float.valueOf(jsonObj.getString("zxj"));
			        		// 涨跌幅
			        		Float changePercent = Float.valueOf(jsonObj.getString("zdf"));
			        		
			        		NotionHotStocks notionHotStocks = new NotionHotStocks();
			    			notionHotStocks.setTradeDate(tradeDate);
			    			notionHotStocks.setRank(rank);
			    			notionHotStocks.setNotionName(notionName);
			    			notionHotStocks.setCode(code);
			    			notionHotStocks.setStockName(stockName);
			    			notionHotStocks.setNewPrice(newPrice);
			    			notionHotStocks.setChangePercent(changePercent);
			    			notionHotStocks.setTimestamp(timestamp);
			    			notionHotStocksList.add(notionHotStocks);
			        	}
			        	break;
			        }
				}
				if(notionHotStocksList != null && !notionHotStocksList.isEmpty()){
					notionHotStocksService.insertNotionHotStocksBatch(notionHotStocksList);
				}
			}
		}else if("INDUSTRY".equals(type)){
			File parent = new File(dirPath);
			com.wy.stock.utils.FileNameSelector selector = new com.wy.stock.utils.FileNameSelector(StockConstant.INDUSTRY_HOT_IDENTIFIER + "_", ".json");
			File[] industryFiles = parent.listFiles(selector);
			
			List<IndustryHotStocks> industryHotStocksList = new ArrayList<IndustryHotStocks>();
			if(industryFiles != null && industryFiles.length > 0){
				for(File jsonFile : industryFiles){
					LOGGER.info("processing file: " + jsonFile.getAbsolutePath());
					if(jsonFile.getAbsolutePath().split("_").length != 3){
						continue;
					}
					// 从文件名中获取industryName
					String industryName = jsonFile.getAbsolutePath().split("_")[1];
					// 从文件名中解析出当前页，用于计算序号.
					int currPage = Integer.valueOf(jsonFile.getAbsolutePath().split("_")[2].split("\\.")[0]);
					String content = StockUtils.getFileContent(jsonFile);
					String jsonString = "{" + content;

			        JSONObject jsonObject = new JSONObject(jsonString);
			        @SuppressWarnings("unchecked")
					Iterator<String> iterator1 = jsonObject.keys();
			        while (iterator1.hasNext()) {
			        	String key1 = (String) iterator1.next();
			        	if(!"data".equals(key1)){
			        		continue;
			        	}
			        	// 取第一个
			        	JSONArray industryArray = jsonObject.getJSONArray(key1);
			        	for(int i = 0; i < industryArray.length(); i++){
			        		JSONObject jsonObj = industryArray.getJSONObject(i);
			        		// 交易日期
			        		String tradeDateStr = jsonObj.getString("rtime").split(" ")[0];
			        		if(tradeDateStr.compareTo(currDateStr) < 0){
			        			continue;
			        		}
			        		Timestamp tradeDate = Timestamp.valueOf(tradeDateStr + " 00:00:00");
			        		// 序号
			        		int rank = StockConstant.INDUSTRY_HOT_PAGE_SIZE * (currPage - 1) + i + 1;
			        		// 股票代码
			        		String code = jsonObj.getString("stockcode");
			        		// 股票名称
			        		String stockName = codeNameMap.get(code);
			        		if(StringUtils.isEmpty(stockName)){
			        			LOGGER.info("=== code: " + code + " stockName is null.");
			        			continue;
			        		}
			        		// 最新价
			        		Float newPrice = Float.valueOf(jsonObj.getString("zxj"));
			        		// 涨跌幅
			        		Float changePercent = Float.valueOf(jsonObj.getString("zdf"));
			        		
			        		IndustryHotStocks industryHotStocks = new IndustryHotStocks();
			        		industryHotStocks.setTradeDate(tradeDate);
			        		industryHotStocks.setRank(rank);
			        		industryHotStocks.setIndustryName(industryName);
			        		industryHotStocks.setCode(code);
			        		industryHotStocks.setStockName(stockName);
			        		industryHotStocks.setNewPrice(newPrice);
			        		industryHotStocks.setChangePercent(changePercent);
			        		industryHotStocks.setTimestamp(timestamp);
			    			industryHotStocksList.add(industryHotStocks);
			        	}
			        	break;
			        }
				}
				if(industryHotStocksList != null && !industryHotStocksList.isEmpty()){
					industryHotStocksService.insertIndustryHotStocksBatch(industryHotStocksList);
				}
			}
		
		}
	}
	
	/**
	 * 解析个股页面，插入ST_STOCK_FIVE_CHANGE, 这里不区分上证、深证
	 * 5分钟涨跌用的仍然是东方财富的，同花顺没有.
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
	 * 解析notionHot.html, industryHot.html, 登记ST_NOTION_INFO, ST_INDUSTRY_INFO.
	 * @param html
	 * @param type
	 */
	public void persistNotionIndustryInfo(File html, String type){
		if(!html.exists()){
    		LOGGER.error(html.getAbsolutePath() + " not exists, return now...");
    		return;
		}
    	Document doc = getNotionIndustryDoc(html);
		/*
		 * 概念
		 */
    	// 解析文件.
    	if("NOTION".equals(type)){
    		List<NotionInfo> notionInfoList = parseNotionInfoFromDoc(doc);
    		if(notionInfoList != null && !notionInfoList.isEmpty()){
    			notionInfoService.deleteNotionInfoBySource(StockConstant.THS_FLAG);
    			notionInfoService.insertNotionInfoBatch(notionInfoList);
    		}
    	}else if("INDUSTRY".equals(type)){
    		/*
    		 * 行业
    		 */
        	// 解析文件.
    		List<IndustryInfo> industryInfoList = parseIndustryInfoFromDoc(doc);
    		if(industryInfoList != null && !industryInfoList.isEmpty()){
    			industryInfoService.deleteIndustryInfoBySource(StockConstant.THS_FLAG);
    			industryInfoService.insertIndustryInfoBatch(industryInfoList);
    		}
    	}
	}
	
	/**
	 * 解析热点概念、行业信息，即 notionHot.html, industryHot.html，插入数据库 ST_NOTION_HOT, ST_INDUSTRY_HOT;
	 * @param html
	 * @type type
	 */
	public void persistNotionIndustryHot(File html, String type){
		if(!html.exists()){
    		LOGGER.error(html.getAbsolutePath() + " not exists, return now...");
    		return;
		}
    	/* 
    	 * 解析文件. 
    	 * 对于概念页面，解析头部的概念名称及相关信息插入ST_NOTION_INFO.
    	 */
    	if("NOTION".equals(type)){
    		persistNotionHotFromHtml(html);
    		/*
    		List<NotionHot> notionHotList = parseNotionHotFromHtml(html);
    		if(notionHotList != null && !notionHotList.isEmpty()){
    			// 先根据tradeDate删除已有的.
    			String tradeDateStr = notionHotList.get(0).getTradeDateStr();
    			notionHotService.deleteNotionHotByTradeDateStr(tradeDateStr, StockConstant.THS_FLAG);
    			notionHotService.insertNotionHotBatch(notionHotList);
    		}
    		*/
    	}else if("INDUSTRY".equals(type)){
    		List<IndustryHot> industryHotList = parseIndustryHotFromHtml(html);
    		if(industryHotList != null && !industryHotList.isEmpty()){
    			// 先根据tradeDate删除已有的.
    			String tradeDateStr = industryHotList.get(0).getTradeDateStr();
    			industryHotService.deleteIndustryHotByTradeDateStr(tradeDateStr, StockConstant.THS_FLAG);
    			industryHotService.insertIndustryHotBatch(industryHotList);
    		}
    	}
	}
	
	public void persistNotionIndustryHot(File saveDir,  int totalPages, String type) {
		Map<String, String> codeNameMap = stockInfoService.queryStockCodeNameMap();
		Timestamp timestamp = new Timestamp(Calendar.getInstance().getTimeInMillis());
		if("NOTION".equalsIgnoreCase(type)){
			com.wy.stock.utils.FileNameSelector selector = new com.wy.stock.utils.FileNameSelector("notionHot" + "_", ".json");
			File[] notionHotJsonFiles = saveDir.listFiles(selector);
			
			if(notionHotJsonFiles != null && notionHotJsonFiles.length > 0){
				Map<String, String> notionHyCodeMap = notionInfoService.queryNotionInfoMap(StockConstant.THS_FLAG);
				List<NotionHot> result = new ArrayList<NotionHot>();
				for(File jsonFile : notionHotJsonFiles){
					LOGGER.info("processing file: " + jsonFile.getAbsolutePath());
					if(jsonFile.getAbsolutePath().split("_").length != 2){
						continue;
					}
					// 从文件名中解析出当前页，用于计算序号.
					int currPage = Integer.valueOf(jsonFile.getAbsolutePath().split("_")[1].split("\\.")[0]);
					String content = StockUtils.getFileContent(jsonFile);
					String jsonString = "{" + content.split("],")[0] + "]" + "}";

			        JSONObject jsonObject = new JSONObject(jsonString);
			        @SuppressWarnings("unchecked")
					Iterator<String> iterator1 = jsonObject.keys();
			        while (iterator1.hasNext()) {
			        	String key1 = (String) iterator1.next();
			        	if(!"data".equals(key1)){
			        		continue;
			        	}
			        	// 取第一个
			        	JSONArray notionArray = jsonObject.getJSONArray(key1);
			        	for(int i = 0; i < notionArray.length(); i++){
			        		JSONObject jsonObj = notionArray.getJSONObject(i);
			        		// 交易日期
			        		String tradeDateStr = jsonObj.getString("rtime").split(" ")[0];
			        		Timestamp tradeDate = Timestamp.valueOf(tradeDateStr + " 00:00:00");
			        		// 序号
			        		int rank = StockConstant.NOTION_HOT_PAGE_SIZE * (currPage - 1) + i + 1;
			        		// 板块名称
			        		String notionName = notionHyCodeMap.get(jsonObj.getString("hycode"));
			        		// 涨跌幅
			        		Float changePercent = Float.valueOf(jsonObj.getString("zdf"));
			        		// 领涨股
			        		String gainerCode = jsonObj.getString("gainercode");
			        		String riseLeadStockName = codeNameMap.get(gainerCode);
			        		if(StringUtils.isBlank(riseLeadStockName)){
			        			riseLeadStockName = StringUtils.isBlank(gainerCode) ? "999999" : gainerCode;
			        		}
			        		// 领涨股涨跌幅 RISE_LEAD_STOCK_CHANGE_PCT
			        		Float riseLeadStockChangePercent = Float.valueOf(jsonObj.getString("gainerzdf"));
			        		// 平均价格
			        		Float averagePrice = Float.valueOf(jsonObj.getString("jj"));
			        		// 公司家数.
			        		int corpsNum = jsonObj.getInt("num");
			        		// 板块代码
			        		String notionCode = jsonObj.getString("platecode");
			        		// hycode
			        		String hycode = jsonObj.getString("hycode");
			        		// notionUrl
			        		String notionUrl = "http://q.10jqka.com.cn/stock/gn/" + hycode + "/";
			        		
			        		NotionHot notionHot = new NotionHot();
			        		// 设置默认值
			        		notionHot.setTotalMarketCap(-1f);
			        		notionHot.setTurnoverRate(-1f);
			        		notionHot.setRiseStocksNum(-1);
			        		notionHot.setFallStocksNum(-1);
			        		notionHot.setTradeDate(tradeDate);
			        		notionHot.setTradeDateStr(tradeDateStr);
			        		notionHot.setRank(rank);
			        		notionHot.setNotionName(notionName);
			        		notionHot.setChangePercent(changePercent);
			        		notionHot.setRiseLeadStockName(riseLeadStockName);
			        		notionHot.setRiseLeadStockChangePercent(riseLeadStockChangePercent);
			        		notionHot.setAvgPrice(averagePrice);
			        		notionHot.setSource(StockConstant.THS_FLAG);
			        		notionHot.setTimestamp(timestamp);
			        		result.add(notionHot);
			        		
			        		// 检查ST_NOTION_INFO中是否存在，不存在插入，存在更新.
			        		NotionInfo notionInfo = notionInfoService.queryNotionInfoByName(notionName, StockConstant.THS_NOTION_TYPE0, StockConstant.THS_FLAG);
			        		if(notionInfo != null){
			            		NotionInfo notionInfoNew = new NotionInfo();
			            		notionInfoNew.setNotionUrl(notionUrl);
			            		notionInfoNew.setTimestamp(timestamp);
			            		notionInfoNew.setCorpsNum(corpsNum);
			            		notionInfoNew.setNotionCode(notionCode);
			            		notionInfoNew.setNotionName(notionName);
			            		notionInfoNew.setSource(StockConstant.THS_FLAG);
			            		if(!StringUtils.isEmpty(notionUrl) && corpsNum > 0 && !StringUtils.isEmpty(notionCode)
			            				&& !StringUtils.isEmpty(notionName)){
			            			notionInfoService.updateByNotionName(notionInfoNew);
			            		}
			        		}else{
			        			NotionInfo notionInfoNew = new NotionInfo();
			        			notionInfoNew.setNotionUrl(notionUrl);
			        			notionInfoNew.setNotionName(notionName);
			        			notionInfoNew.setTimestamp(timestamp);
			        			notionInfoNew.setSource(StockConstant.THS_FLAG);
			        			notionInfoNew.setCorpsNum(corpsNum);
			        			notionInfoNew.setNotionCode(notionCode);
			        			notionInfoService.insertNotionInfo(notionInfoNew);
			        		}
			        	}
			        	break;
			        }
				}
				/*
				 * 插入ST_NOTION_HOT
				 */
	    		if(result != null && !result.isEmpty()){
	    			// 不删除已有的.
	    			notionHotService.insertNotionHotBatch(result);
	    		}
			}
		}else if("INDUSTRY".equalsIgnoreCase(type)){
			com.wy.stock.utils.FileNameSelector selector = new com.wy.stock.utils.FileNameSelector("industryHot" + "_", ".json");
			File[] industryHotJsonFiles = saveDir.listFiles(selector);
			
			if(industryHotJsonFiles != null && industryHotJsonFiles.length > 0){
				Map<String, String> industryHyCodeMap = industryInfoService.queryIndustryInfoMap(StockConstant.THS_FLAG);
				List<IndustryHot> result = new ArrayList<IndustryHot>();
				for(File jsonFile : industryHotJsonFiles){
					LOGGER.info("processing file: " + jsonFile.getAbsolutePath());
					if(jsonFile.getAbsolutePath().split("_").length != 2){
						continue;
					}
					// 从文件名中解析出当前页，用于计算序号.
					int currPage = Integer.valueOf(jsonFile.getAbsolutePath().split("_")[1].split("\\.")[0]);
					String content = StockUtils.getFileContent(jsonFile);
					String jsonString = "{" + content.split("],")[0] + "]" + "}";

			        JSONObject jsonObject = new JSONObject(jsonString);
			        @SuppressWarnings("unchecked")
					Iterator<String> iterator1 = jsonObject.keys();
			        while (iterator1.hasNext()) {
			        	String key1 = (String) iterator1.next();
			        	if(!"data".equals(key1)){
			        		continue;
			        	}
			        	// 取第一个
			        	JSONArray notionArray = jsonObject.getJSONArray(key1);
			        	for(int i = 0; i < notionArray.length(); i++){
			        		JSONObject jsonObj = notionArray.getJSONObject(i);
			        		// 交易日期
			        		String tradeDateStr = jsonObj.getString("rtime").split(" ")[0];
			        		Timestamp tradeDate = Timestamp.valueOf(tradeDateStr + " 00:00:00");
			        		// 序号
			        		int rank = StockConstant.INDUSTRY_HOT_PAGE_SIZE * (currPage - 1) + i + 1;
			        		// 板块名称
			        		String industryName = industryHyCodeMap.get(jsonObj.getString("hycode"));
			        		// 涨跌幅
			        		Float changePercent = Float.valueOf(jsonObj.getString("zdf"));
			        		// 领涨股
			        		String gainerCode = jsonObj.getString("gainercode");
			        		String riseLeadStockName = codeNameMap.get(gainerCode);
			        		if(StringUtils.isBlank(riseLeadStockName)){
			        			riseLeadStockName = StringUtils.isBlank(gainerCode) ? "999999" : gainerCode;
			        		}
			        		// 领涨股涨跌幅 RISE_LEAD_STOCK_CHANGE_PCT
			        		Float riseLeadStockChangePercent = Float.valueOf(jsonObj.getString("gainerzdf"));
			        		// 平均价格
			        		Float averagePrice = Float.valueOf(jsonObj.getString("jj"));
			        		// 公司家数.
			        		int corpsNum = jsonObj.getInt("num");
			        		// 板块代码
			        		String industryCode = jsonObj.getString("platecode");
			        		// hycode
			        		String hycode = jsonObj.getString("hycode");
			        		// industryUrl
			        		String industryUrl = "http://q.10jqka.com.cn/stock/thshy/" + hycode + "/";
			        		
			        		IndustryHot industryHot = new IndustryHot();
			        		// 设置默认值
			        		industryHot.setTotalMarketCap(-1f);
			        		industryHot.setTurnoverRate(-1f);
			        		industryHot.setRiseStocksNum(-1);
			        		industryHot.setFallStocksNum(-1);
			        		industryHot.setTradeDate(tradeDate);
			        		industryHot.setTradeDateStr(tradeDateStr);
			        		industryHot.setRank(rank);
			        		industryHot.setIndustryName(industryName);
			        		industryHot.setChangePercent(changePercent);
			        		industryHot.setRiseLeadStockName(riseLeadStockName);
			        		industryHot.setRiseLeadStockChangePercent(riseLeadStockChangePercent);
			        		industryHot.setAvgPrice(averagePrice);
			        		industryHot.setSource(StockConstant.THS_FLAG);
			        		industryHot.setTimestamp(timestamp);
			        		result.add(industryHot);
			        		
			        		// 检查ST_INDUSTRY_INFO中是否存在，不存在插入，存在更新.
			        		IndustryInfo industryInfo = industryInfoService.queryIndustryInfoByName(industryName, StockConstant.THS_FLAG);
			        		IndustryInfo industryInfoNew = new IndustryInfo();
			        		if(industryInfo != null){
			            		industryInfoNew.setIndustryUrl(industryUrl);
			            		industryInfoNew.setTimestamp(timestamp);
			            		industryInfoNew.setCorpsNum(corpsNum);
			            		industryInfoNew.setIndustryCode(industryCode);
			            		industryInfoNew.setIndustryName(industryName);
			            		industryInfoNew.setSource(StockConstant.THS_FLAG);
			            		if(!StringUtils.isEmpty(industryUrl) && corpsNum > 0 && !StringUtils.isEmpty(industryCode)
			            				&& !StringUtils.isEmpty(industryName)){
			            			industryInfoService.updateByIndustryName(industryInfoNew);
			            		}
			        		}else{
			        			industryInfoNew.setIndustryUrl(industryUrl);
			        			industryInfoNew.setIndustryName(industryName);
			        			industryInfoNew.setTimestamp(timestamp);
			        			industryInfoNew.setSource(StockConstant.THS_FLAG);
			        			industryInfoNew.setIndustryCode(industryCode);
			        			industryInfoNew.setCorpsNum(corpsNum);
			        			industryInfoService.insertIndustryInfo(industryInfoNew);
			        		}
			        	}
			        	break;
			        }
				}
	    		if(result != null && !result.isEmpty()){
	    			// 不删除已有的.
	    			industryHotService.insertIndustryHotBatch(result);
	    		}
			}
		}
	}
	
	/**
	 * 将index.html中的数据插入数据库: ST_INDEX.
	 * @param jsonFile
	 */
	public void persistIndexFromHtml(File html) {
		if(!html.exists()){
			LOGGER.error(html.getAbsolutePath() + " not exists, return null now...");
			return;
		}
		List<Index> indexList = parseIndexFromHtml(html);
		if(indexList != null && !indexList.isEmpty()){
			indexService.insertIndexBatch(indexList);
		}
	}
	
	/**
	 * 解析index.html
	 * @param html
	 * @return
	 */
	private List<Index> parseIndexFromHtml(File html) {
		if(!html.exists()){
			LOGGER.error(html.getAbsolutePath() + " not exists, return null now...");
			return null;
		}
		LOGGER.info("processing file: " + html.getAbsolutePath());
		
		// body > div.wrap > div.table_wrap.bt0 > table > tbody > tr:nth-child(1) > td.tc.fb > a
		// body > div.wrap > div.table_wrap.bt0 > table > tbody > tr:nth-child(2) > td:nth-child(2) > a
		// body > div.wrap > div.table_wrap.bt0 > table > tbody > tr:nth-child(3) > td.tc.fb > a
		Document indexDoc = getNotionIndustryDoc(html);
		Iterator<Element> trIter = indexDoc.select("body > div.wrap > div.table_wrap.bt0 > table > tbody > tr").iterator();
		if(trIter == null){
			LOGGER.error("trIter is null, reutrn null now...");
			return null;
		}
		
		List<Index> list = new ArrayList<Index>();
		Map<String, String> indexCodeMap = StockUtils.getIndexCodeMap();
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
		while(trIter.hasNext()){
			Element trElement = trIter.next();
			// 指数名称 body > div.wrap > div.table_wrap.bt0 > table > tbody > tr:nth-child(1) > td.tc.fb > a
			// body > div.wrap > div.table_wrap.bt0 > table > tbody > tr:nth-child(12) > td:nth-child(2)
			String indexName = trElement.select("> td:nth-child(2) > a").text();
			if(StringUtils.isBlank(indexName)){
				indexName = trElement.select("> td:nth-child(2)").text();
			}
			String indexCode = indexCodeMap.get(StringUtils.trim(indexName));
			// 最新价 body > div.wrap > div.table_wrap.bt0 > table > tbody > tr:nth-child(1) > td:nth-child(3)
			Float newPrice = Float.valueOf(trElement.select("> td:nth-child(3)").text());
			// 涨跌额 body > div.wrap > div.table_wrap.bt0 > table > tbody > tr:nth-child(1) > td:nth-child(4)
			Float changeAmount = Float.valueOf(trElement.select("> td:nth-child(4)").text());
			// 涨跌幅 body > div.wrap > div.table_wrap.bt0 > table > tbody > tr:nth-child(1) > td.c_red.cur
			Float changePercent = Float.valueOf(trElement.select("> td:nth-child(5)").text().replace("%", ""));
			// 昨收 body > div.wrap > div.table_wrap.bt0 > table > tbody > tr:nth-child(1) > td:nth-child(6)
			Float lastClose = Float.valueOf(trElement.select(" > td:nth-child(6)").text());
			// 今开 body > div.wrap > div.table_wrap.bt0 > table > tbody > tr:nth-child(1) > td:nth-child(7)
			Float open = Float.valueOf(trElement.select("> td:nth-child(7)").text());
			// 最高 body > div.wrap > div.table_wrap.bt0 > table > tbody > tr:nth-child(1) > td:nth-child(8)
			Float high = Float.valueOf(trElement.select("> td:nth-child(8)").text());
			// 最低 body > div.wrap > div.table_wrap.bt0 > table > tbody > tr:nth-child(1) > td:nth-child(9)
			Float low = Float.valueOf(trElement.select(" > td:nth-child(9)").text());
			// 收盘价, 使用最新价.
			Float close = newPrice;
			// 成交量(万手) body > div.wrap > div.table_wrap.bt0 > table > tbody > tr:nth-child(1) > td:nth-child(10)
			Float volumn = Float.valueOf(trElement.select("> td:nth-child(10)").text());
			// 成交额(亿) body > div.wrap > div.table_wrap.bt0 > table > tbody > tr:nth-child(1) > td:nth-child(11)
			Float volumnAmount = Float.valueOf(trElement.select("> td:nth-child(11)").text())/10000.0f;
			
			Index index = new Index();
			index.setTradeDate(tradeDate);
			index.setIndexName(indexName);
			index.setIndexCode(indexCode);
			index.setNewPrice(newPrice);
			index.setChangeAmount(changeAmount);
			index.setChangePercent(changePercent);
			index.setLastClose(lastClose);
			index.setOpen(open);
			index.setHigh(high);
			index.setLow(low);
			index.setClose(close);
			// 振幅使用默认值
			index.setAmplitude(-99.0f);
			index.setVolumn(volumn);
			index.setVolumnAmount(volumnAmount);
			index.setTimestamp(timestamp);
			list.add(index);
		}
    	return list;
	}
	
	/**
	 * 获取概念、行业热点板块中列表的总页数.
	 * 概念: http://q.10jqka.com.cn/stock/gn/
	 * 同行顺行业: http://q.10jqka.com.cn/stock/thshy/
	 * @param html
	 * @param type
	 */
	public int getNotionIndustryHotTotalPages(File html, String type) {
		if(!html.exists()){
			LOGGER.info(html + " not exists, return now...");
			return 0;
		}
		Document doc = getNotionIndustryDoc(html);
		int page = 0;
		if("NOTION".equalsIgnoreCase(type)){
			// 3/4 body > div.wrap > div.page-main > div.clearfix > div > div > span.page_info
			page = Integer.valueOf(doc.select("body > div.wrap > div.page-main > div.clearfix > div > div > span.page_info").text().split("/")[1]);
		}else if("INDUSTRY".equalsIgnoreCase(type)){
			// 1/2 body > div.wrap > div.m_page.main_page > span.page_info
			page = Integer.valueOf(doc.select("body > div.wrap > div.m_page.main_page > span.page_info").text().split("/")[1]);
		}
		return page;
	}
	
	/**
	 * 从同花顺爱问财上下载xls: 2016-01-11 概念板块指数 区间涨跌幅 -> 2016-09-05  概念指数 区间涨跌幅 
	 * 转换成csv文件, 标题头: 指数代码,指数简称,涨跌幅(%)
	 * 解析该csv.
	 * 此方法可以需要手动下载xls.
	 * csv文件内容:
	 *  指数代码,指数简称,涨跌幅(%)
		,,2016.01.04
		885699.48,ST板块,-4.99
		883301.48,上证50样本股,-6.21
	 */
	public List<NotionHot> parseNotionHotFromCsv(File csvFile) {
		if(!csvFile.exists()){
			LOGGER.info(csvFile.getAbsolutePath() + " not exists, return now...");
			return null;
		}
		
		String tradeDateStr =csvFile.getParentFile().getParentFile().getParentFile().getName() + "-" 
				+ csvFile.getParentFile().getParentFile().getName() + "-"
				+ csvFile.getParentFile().getName();
		Timestamp tradeDate = Timestamp.valueOf(tradeDateStr + " 00:00:00");
		Timestamp timestamp = new Timestamp(Calendar.getInstance()
				.getTimeInMillis());
		Map<String, String> notionCodeNameMap = notionInfoService.queryNotionCodeNameMap(StockConstant.THS_FLAG);
		List<NotionHot> list = new ArrayList<NotionHot>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(csvFile));
			String line = reader.readLine();
			int lineNum = 0;
			int rank = 0;
			Float preChangePercent = 99.99f;
			while(!StringUtils.isEmpty(line)){
				lineNum++;
				String[] strArr = line.split(",");
				
				line = reader.readLine();
				if(strArr.length != 3){
					LOGGER.info("strArr length !=3 : " + strArr.length);
					return null;
				}
				if(lineNum == 1 && (!"指数代码".equals(strArr[0]) || !"指数简称".equals(strArr[1]) || !"涨跌幅(%)".equals(strArr[2]))){
					LOGGER.info("标题错误.");
					return null;
				}
				if(lineNum == 2 && !tradeDateStr.replaceAll("-", ".").equals(strArr[2])){
					LOGGER.info("tradeDateStr error: " + strArr[2]);
					return null;
				}
				if(lineNum < 3){
					continue;
				}
				
				rank++;
				String notionCode = strArr[0].split("\\.")[0];
				String notionName = notionCodeNameMap.get(notionCode);
				Float changePercent = Float.valueOf(strArr[2]);
				if(changePercent > preChangePercent){
					LOGGER.info(lineNum + " changePercent > preChangePercent, return now...");
					return null;
				}
				preChangePercent = changePercent;
				if(StringUtils.isEmpty(notionName)){
					notionName = strArr[1];
					// 查询 ST_NOTION_INFO 中是是否存在该条记录.
					NotionInfo existed = notionInfoService.queryNotionInfoByName(notionName, StockConstant.THS_NOTION_TYPE0, StockConstant.THS_FLAG);
					if(existed == null){
						// 新增
						NotionInfo newInfo = new NotionInfo();
						newInfo.setNotionUrl("http://q.10jqka.com.cn/stock/gn/" + StockUtils.getPinYinHeadChar(notionName) + "/");
						newInfo.setNotionName(notionName);
						newInfo.setNotionCode(notionCode);
						newInfo.setTimestamp(timestamp);
						newInfo.setSource(StockConstant.THS_FLAG);
						newInfo.setCorpsNum(0);
						notionInfoService.insertNotionInfo(newInfo);
					}else{
						// 更新 NOTION_CODE
						NotionInfo updateInfo = new NotionInfo();
						updateInfo.setNotionCode(notionCode);
						updateInfo.setNotionName(notionName);
						updateInfo.setSource(StockConstant.THS_FLAG);
						notionInfoService.updateNotionCodeByNotionName(updateInfo);
					}
				}
				
				NotionHot notionHot = new NotionHot();
				notionHot.setTradeDate(tradeDate);
	    		notionHot.setRank(rank);
	    		notionHot.setNotionName(notionName);
	    		notionHot.setChangePercent(changePercent);
	    		// 设置默认值
	    		notionHot.setTotalMarketCap(-1f);
	    		notionHot.setTurnoverRate(-1f);
	    		notionHot.setRiseStocksNum(-1);
	    		notionHot.setFallStocksNum(-1);
	    		notionHot.setRiseLeadStockName("爱问财");
	    		notionHot.setRiseLeadStockChangePercent(99.99f);
	    		notionHot.setTimestamp(timestamp);
	    		notionHot.setSource(StockConstant.THS_FLAG);
	    		notionHot.setAvgPrice(99999.99f);
	    		list.add(notionHot);
			}
		} catch (FileNotFoundException e) {
			LOGGER.error(e);
		} catch (IOException e) {
			LOGGER.error(e);
		} finally{
			try {
				if(reader != null){
					reader.close();
				}
			}catch (IOException e) {
				LOGGER.error(e);
			}
		}
		return list;
	}
	
	public List<IndustryHot> parseIndustryHotFromCsv(File csvFile) {
		if(!csvFile.exists()){
			LOGGER.info(csvFile.getAbsolutePath() + " not exists, return now...");
			return null;
		}
		
		String tradeDateStr =csvFile.getParentFile().getParentFile().getParentFile().getName() + "-" 
				+ csvFile.getParentFile().getParentFile().getName() + "-"
				+ csvFile.getParentFile().getName();
		Timestamp tradeDate = Timestamp.valueOf(tradeDateStr + " 00:00:00");
		Timestamp timestamp = new Timestamp(Calendar.getInstance()
				.getTimeInMillis());
		Map<String, String> industryCodeNameMap = industryInfoService.queryIndustryCodeNameMap(StockConstant.THS_FLAG);
		List<IndustryHot> list = new ArrayList<IndustryHot>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(csvFile));
			String line = reader.readLine();
			int lineNum = 0;
			int rank = 0;
			Float preChangePercent = 99.99f;
			while(!StringUtils.isEmpty(line)){
				lineNum++;
				String[] strArr = line.split(",");
				
				line = reader.readLine();
				if(strArr.length != 3){
					LOGGER.info("strArr length !=3 : " + strArr.length);
					return null;
				}
				if(lineNum == 1 && (!"指数代码".equals(strArr[0]) || !"指数简称".equals(strArr[1]) || !"涨跌幅(%)".equals(strArr[2]))){
					LOGGER.info("标题错误.");
					return null;
				}
				if(lineNum == 2 && !tradeDateStr.replaceAll("-", ".").equals(strArr[2])){
					LOGGER.info("tradeDateStr error: " + strArr[2]);
					return null;
				}
				if(lineNum < 3){
					continue;
				}
				
				rank++;
				String industryCode = strArr[0].split("\\.")[0];
				String industryName = industryCodeNameMap.get(industryCode);
				Float changePercent = Float.valueOf(strArr[2]);
				if(changePercent > preChangePercent){
					LOGGER.info(lineNum + " changePercent > preChangePercent, return now...");
					return null;
				}
				preChangePercent = changePercent;
				if(StringUtils.isEmpty(industryName)){
					industryName = strArr[1];
					// 查询 ST_INDUSTRY_INFO 中是是否存在该条记录.
					IndustryInfo existed = industryInfoService.queryIndustryInfoByName(industryName, StockConstant.THS_FLAG);
					if(existed == null){
						// 新增
						IndustryInfo newInfo = new IndustryInfo();
						newInfo.setIndustryUrl("http://q.10jqka.com.cn/stock/thshy/" + StockUtils.getPinYinHeadChar(industryName) + "/");
						newInfo.setIndustryName(industryName);
						newInfo.setIndustryCode(industryCode);
						newInfo.setTimestamp(timestamp);
						newInfo.setSource(StockConstant.THS_FLAG);
						newInfo.setCorpsNum(0);
						industryInfoService.insertIndustryInfo(newInfo);
					}else{
						// 更新 INDUSTRY_CODE
						IndustryInfo updateInfo = new IndustryInfo();
						updateInfo.setIndustryCode(industryCode);
						updateInfo.setIndustryName(industryName);
						updateInfo.setSource(StockConstant.THS_FLAG);
						industryInfoService.updateIndustryCodeByIndustryName(updateInfo);
					}
				}
				
				IndustryHot industryHot = new IndustryHot();
				industryHot.setTradeDate(tradeDate);
				industryHot.setRank(rank);
				industryHot.setIndustryName(industryName);
				industryHot.setChangePercent(changePercent);
	    		// 设置默认值
				industryHot.setTotalMarketCap(-1f);
				industryHot.setTurnoverRate(-1f);
				industryHot.setRiseStocksNum(-1);
				industryHot.setFallStocksNum(-1);
				industryHot.setRiseLeadStockName("爱问财");
				industryHot.setRiseLeadStockChangePercent(99.99f);
				industryHot.setTimestamp(timestamp);
				industryHot.setSource(StockConstant.THS_FLAG);
				industryHot.setAvgPrice(99999.99f);
	    		list.add(industryHot);
			}
		} catch (FileNotFoundException e) {
			LOGGER.error(e);
		} catch (IOException e) {
			LOGGER.error(e);
		} finally{
			try {
				if(reader != null){
					reader.close();
				}
			}catch (IOException e) {
				LOGGER.error(e);
			}
		}
		return list;
	}
	
	public void persistNotionHotHistFromCsv(File dir){
		com.wy.stock.utils.FileNameSelector selector = new com.wy.stock.utils.FileNameSelector(StockConstant.NOTION_HOT_IDENTIFIER, ".csv");
		List<File> csvFiles = new ArrayList<File>();
		StockUtils.listFilesRec(dir, selector, csvFiles);
		if(csvFiles == null || csvFiles.size() == 0){
			return;
		}
		
		int count = 0;
		for(File csvFile : csvFiles){
			LOGGER.info(++count + "processing file: " + csvFile.getAbsolutePath());
			
			String tradeDateStr =csvFile.getParentFile().getParentFile().getParentFile().getName() + "-" 
					+ csvFile.getParentFile().getParentFile().getName() + "-"
					+ csvFile.getParentFile().getName();
			List<NotionHot> existedList = notionHotService.queryNotionHotByDateStr(tradeDateStr, StockConstant.THS_FLAG);
			if(existedList != null && !existedList.isEmpty()){
				continue;
			}
			
			List<NotionHot> list = parseNotionHotFromCsv(csvFile);
			if(list != null){
				notionHotService.insertNotionHotBatch(list);
			}
		}
	}
	
	/**
	 * i问财:  2015-06-05 行业板块指数 区间涨跌幅
	 * 将查询结果页面按照指定的日期那列排序，并去掉涨跌幅、现价，导出excel,命名为industryHot.xls
	 */
	public void persistIndustryHotHistFromHtml(File dir) {
		com.wy.stock.utils.FileNameSelector selector = new com.wy.stock.utils.FileNameSelector(
				StockConstant.INDUSTRY_HOT_IDENTIFIER, ".xls");
		List<File> excelFiles = new ArrayList<File>();
		StockUtils.listFilesRec(dir, selector, excelFiles);
		if (excelFiles == null || excelFiles.size() == 0) {
			return;
		}
		
		int count = 0;
		Document doc = null;
		List<IndustryHot> industryHotList = new ArrayList<IndustryHot>();
		for (File excelFile : excelFiles) {
			LOGGER.info(++count + " processing file: " + excelFile.getAbsolutePath());
			Timestamp timestamp = new Timestamp(Calendar.getInstance()
					.getTimeInMillis());
			
			doc = Jsoup.parse(StockUtils.getFileContent(excelFile));
			if(doc == null){
				LOGGER.error("doc is null, return now...");
				return;
			}
			Iterator<Element> trIter = doc.select("body > table > tbody > tr").iterator();
			if(trIter == null){
				LOGGER.error("trIter is null, reutrn null now...");
				return;
			}
			
			Timestamp tradeDate = null;
			int rank = 0;
			while(trIter.hasNext()){
				Element trElement = trIter.next();
				
				String tradeDateStr = trElement.select("> td:nth-child(3)").text();
				if(tradeDateStr.contains(" ")){
					// 交易日期
					String tradeDateStrNew = tradeDateStr.split(" ")[1].replaceAll("\\.", "-");
					tradeDate = Timestamp.valueOf(tradeDateStrNew + " 00:00:00");
					continue;
				}
				
				// 指数代码: body > table > tbody > tr:nth-child(3) > td:nth-child(1)
				String industryCodeStr = trElement.select("> td:nth-child(1)").text();
				if ("指数代码".equals(industryCodeStr)){
					continue;
				}
				
				// 指数简称: body > table > tbody > tr:nth-child(3) > td:nth-child(2)
				String industryName = trElement.select("> td:nth-child(2)").text();
				// 涨跌幅: body > table > tbody > tr:nth-child(3) > td:nth-child(3)
				Float changePercent = Float.valueOf(trElement.select("> td:nth-child(3)").text());
				
				rank++;
				IndustryHot industryHot = new IndustryHot();
				industryHot.setTradeDate(tradeDate);
				industryHot.setRank(rank);
				industryHot.setIndustryName(industryName);
				industryHot.setChangePercent(changePercent);
	    		// 设置默认值
				industryHot.setTotalMarketCap(-1f);
				industryHot.setTurnoverRate(-1f);
				industryHot.setRiseStocksNum(-1);
				industryHot.setFallStocksNum(-1);
				industryHot.setRiseLeadStockName("爱问财");
				industryHot.setRiseLeadStockChangePercent(99.99f);
				industryHot.setTimestamp(timestamp);
				industryHot.setSource(StockConstant.THS_FLAG);
				industryHot.setAvgPrice(99999.99f);
				industryHotList.add(industryHot);
			}
		}
		if(industryHotList != null && !industryHotList.isEmpty()){
			industryHotService.insertIndustryHotBatch(industryHotList);
		}
	}
	
	/**
	 * 从同花顺爱问财上下载xls: 2016-01-04 区间涨跌幅 大飞机.   按照区间涨跌幅从高到低排序.
	 * 转换成csv文件, 标题头: 股票代码,股票简称,区间涨跌幅(%)
	 * 解析该csv.
	 * 此方法可以需要手动下载xls.
	 * csv文件内容:
		股票代码,股票简称,区间涨跌幅(%)
		,,2016.01.04
		600677.SH,航天通信,7.2
		000901.SZ,航天科技,0
		002530.SZ,丰东股份,0
	 */
	public List<NotionHotStocks> parseNotionHotStocksFromCsv(File csvFile) {
		if(!csvFile.exists()){
			LOGGER.info(csvFile.getAbsolutePath() + " not exists, return now...");
			return null;
		}
		
		String fileName = csvFile.getName();
		String[] fileArr = fileName.split("_");
		if(fileArr.length != 2){
			return null;
		}
		String notionName = fileArr[1].split("\\.")[0];
		
		String tradeDateStr =csvFile.getParentFile().getParentFile().getParentFile().getName() + "-" 
				+ csvFile.getParentFile().getParentFile().getName() + "-"
				+ csvFile.getParentFile().getName();
		Timestamp tradeDate = Timestamp.valueOf(tradeDateStr + " 00:00:00");
		Timestamp timestamp = new Timestamp(Calendar.getInstance()
				.getTimeInMillis());
		Map<String, String> codeNameMap = stockInfoService.queryStockCodeNameMap();
		List<NotionHotStocks> list = new ArrayList<NotionHotStocks>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(csvFile));
			String line = reader.readLine();
			int lineNum = 0;
			while(!StringUtils.isEmpty(line)){
				lineNum++;
				String[] strArr = line.split(",");
				
				line = reader.readLine();
				if(strArr.length != 3){
					LOGGER.info("strArr length !=3 : " + strArr.length);
					return null;
				}
				if(lineNum == 1 && (!"股票代码".equals(strArr[0]) || !"股票简称".equals(strArr[1]) ||
						((!"区间涨跌幅(%)".equals(strArr[2]) && !"区间涨跌幅:前复权(%)".equals(strArr[2]) && !"涨跌幅:前复权(%)".equals(strArr[2]))))){
					LOGGER.info("标题错误.");
					return null;
				}
				if(lineNum == 2 && !tradeDateStr.replaceAll("-", ".").equals(strArr[2])){
					LOGGER.info("tradeDateStr error: " + strArr[2]);
					return null;
				}
				if(lineNum < 3){
					continue;
				}
				
				String stockCode = strArr[0].split("\\.")[0];
				String stockName = codeNameMap.get(stockCode);
				if(StringUtils.isEmpty(stockName)){
					continue;
				}
				String changePercentStr = strArr[2];
				Float changePercent = 0.00f;
				if(NumberUtils.isNumber(changePercentStr)){
					changePercent = Float.valueOf(changePercentStr);
				}
				
				NotionHotStocks notionHotStocks = new NotionHotStocks();
				notionHotStocks.setTradeDate(tradeDate);
	    		notionHotStocks.setNotionName(notionName);
	    		notionHotStocks.setCode(stockCode);
	    		notionHotStocks.setStockName(stockName);
	    		notionHotStocks.setNewPrice(99999.99f);
	    		notionHotStocks.setChangePercent(changePercent);
	    		notionHotStocks.setTimestamp(timestamp);
	    		list.add(notionHotStocks);
			}
			
			// 将list按照changePercent排序, 然后增加rank值
			Collections.sort(list, StockUtils.descNotionHotStocksComparatorFloat);
			int rank = 0;
			for(NotionHotStocks notionHotStocks : list){
				rank++;
				notionHotStocks.setRank(rank);
			}
		} catch (FileNotFoundException e) {
			LOGGER.error(e);
		} catch (IOException e) {
			LOGGER.error(e);
		} finally{
			try {
				if(reader != null){
					reader.close();
				}
			}catch (IOException e) {
				LOGGER.error(e);
			}
		}
		return list;
	}

	/**
	 * 将JOB-T未开始之前的数据(2016-04-25)从爱问财上下载，并插入 ST_NOTION_HOT_STOCKS:
	 * 0, 注册足够多的同花顺账户，用于excel的下载.
	 * 1, 执行 StockParseToolTHSImpl.genTHSStocksChromeUrl() 产生爱问财的查询url，并执行.
	 * 2, 将查询结果页面按照指定的日期那列排序(不需要排序，由程序控制)，并去掉涨跌幅、现价，导出excel,命名为notion_[概念名称].xls(直接命名为[概念名称].xls即可, 前缀由batchRename.sh操作)
	 * 3, 执行 HtmlUtils.transTHSNotionHotStocksToCsv() 将excel(实际是html)转换为csv文件。
	 * 4, 执行 StockParseToolTHSImpl.persistNotionHotStocksFromCsv() 解析csv,并插入ST_NOTION_HOT_STOCKS.
	 * 
	 */
	public void persistNotionHotStocksHistFromCsv(File dir) {
		com.wy.stock.utils.FileNameSelector selector = new com.wy.stock.utils.FileNameSelector(StockConstant.NOTION_IDENTIFIER + "_", ".csv");
		List<File> csvFiles = new ArrayList<File>();
		StockUtils.listFilesRec(dir, selector, csvFiles);
		if(csvFiles == null || csvFiles.size() == 0){
			return;
		}
		
		int count = 0;
		for(File csvFile : csvFiles){
			LOGGER.info(++count + "processing file: " + csvFile.getAbsolutePath());
			
			String tradeDateStr =csvFile.getParentFile().getParentFile().getParentFile().getName() + "-" 
					+ csvFile.getParentFile().getParentFile().getName() + "-"
					+ csvFile.getParentFile().getName();
			String notionName = csvFile.getName().split("_")[1].split("\\.")[0];
			List<NotionHotStocks> existedList = notionHotStocksService.queryStocksByNotionNameDateStr(notionName, tradeDateStr);
			if(existedList != null && !existedList.isEmpty()){
				continue;
			}
			
			List<NotionHotStocks> list = parseNotionHotStocksFromCsv(csvFile);
			if(list != null){
				notionHotStocksService.insertNotionHotStocksBatch(list);
			}
		}
	}
	
	/**
	 *  构造chrome url, 方便一次打开多个, 需要确保 ST_NOTION_HOT中有数据.
	 * 按照关键词: 2016-04-01 区间涨跌幅 大数据    来查询i问财.
	 * opt/google/chrome/google-chrome "http://x.10jqka.com.cn/stockpick/search?typed=1&preParams=&ts=1&f=1&qs=result_rewrite&selfsectsn=&querytype=&searchfilter=&tid=stockpick&w=2016-04-01+%E5%8C%BA%E9%97%B4%E6%B6%A8%E8%B7%8C%E5%B9%85+%E4%BA%92%E8%81%94%E7%BD%91%E5%BD%A9%E7%A5%A8" 
	 *                                 "http://x.10jqka.com.cn/stockpick/search?typed=1&preParams=&ts=1&f=1&qs=result_rewrite&selfsectsn=&querytype=&searchfilter=&tid=stockpick&w=2016-04-01+%E5%8C%BA%E9%97%B4%E6%B6%A8%E8%B7%8C%E5%B9%85+%E4%B8%AD%E9%9F%A9%E8%87%AA%E8%B4%B8%E5%8C%BA"
	 *                                 "http://x.10jqka.com.cn/stockpick/search?typed=1&preParams=&ts=1&f=1&qs=result_rewrite&selfsectsn=&querytype=&searchfilter=&tid=stockpick&w=2016-04-01+%E5%8C%BA%E9%97%B4%E6%B6%A8%E8%B7%8C%E5%B9%85+%E9%AB%98%E6%A0%A1"
	 */                               
	public void genTHSStocksChromeUrl(String date) {
		// 根据 date 查询 ST_NOTION_HOT. 按照rank排序.
		List<NotionHot> notionHotList = notionHotService.queryNotionHotByDateStr(date, StockConstant.THS_FLAG);
		if(notionHotList == null || notionHotList.isEmpty()){
			LOGGER.info("notionHotList is null ,return now...");
			return;
		}
		Set<String> excludeNotionNames = excludeNotionNameForChromeUrl();
		
		StringBuilder sbRankBefore = new StringBuilder("/opt/google/chrome/google-chrome ");
		StringBuilder sbRankAfter = new StringBuilder("/opt/google/chrome/google-chrome ");
		String commonUrl = "http://x.10jqka.com.cn/stockpick/search?typed=1&preParams=&ts=1&f=1&qs=result_rewrite&selfsectsn=&querytype=&searchfilter=&tid=stockpick&w=";
		// 要有空格.
		String commonKey = date + " 区间涨跌幅 ";
//		String commonKey = date + " 涨跌幅 ";
		int firstLimit = 31;
		int rankAfter = notionHotList.size() - firstLimit;
		for(NotionHot notionHot : notionHotList){
			// 只取前后部分的.
			int rank = notionHot.getRank();
			if(rank > firstLimit && rank < rankAfter){
				continue;
			}
			// 排除掉部分notionName.
			String notionName = notionHot.getNotionName();
			if(excludeNotionNames.contains(notionName)){
				continue;
			}
			// 构造url.
			String toEncode = commonKey + notionName;
			String encodeUrl = null;
			try {
				encodeUrl = URLEncoder.encode(toEncode,"utf-8");
			} catch (UnsupportedEncodingException e) {
				LOGGER.info(e);
				return;
			}
			String url = commonUrl + encodeUrl;
			if(rank <= firstLimit){
				sbRankBefore.append("\"").append(url).append("\"").append(" ");
			}else if(rank >= rankAfter){
				sbRankAfter.append("\"").append(url).append("\"").append(" ");
			}
		}
		LOGGER.info(date + " rankBefore: " + sbRankBefore.toString());
		LOGGER.info(date + " rank after: " + sbRankAfter.toString()) ;
	}
	
	/**
	 * 解析从i问财上下载的历史index数据并插入数据， 查询关键词: 2016-01-04 上证指数 创业板指数 深圳成指 涨跌幅 成交金额
	 * 
	 * 将JOB-T未开始之前的数据(2016-04-25)从爱问财上下载，并插入 ST_INDEX, 只下载上证指数 创业板指数 深圳成指 3个.:
	 * 0, 注册足够多的同花顺账户，用于excel的下载.
	 * 1, 打开i问财页面，输入关键词: 2016-01-04 上证指数 创业板指数 深圳成指 涨跌幅 成交金额
	 * 2, 直接点击导出,命名为index.xls
	 * 3, 执行StockParseToolTHSImpl.persistIndexHistFromHtml(File)插入ST_INDEX.
	 * 
	 */
	public void persistIndexHistFromHtml(File dir) {
		com.wy.stock.utils.FileNameSelector selector = new com.wy.stock.utils.FileNameSelector(
				"index", ".xls");
		List<File> excelFiles = new ArrayList<File>();
		StockUtils.listFilesRec(dir, selector, excelFiles);
		if (excelFiles == null || excelFiles.size() == 0) {
			return;
		}
		
		int count = 0;
		Document doc = null;
		List<Index> indexList = new ArrayList<Index>();
		// 保留2位小数
		DecimalFormat df = new DecimalFormat("#######.00");
		// 匹配日期
		Pattern pattern = Pattern.compile("[0-9]{4}\\.[0-9]{2}\\.[0-9]{2}");
		for (File excelFile : excelFiles) {
			LOGGER.info(++count + " processing file: " + excelFile.getAbsolutePath());
			Timestamp timestamp = new Timestamp(Calendar.getInstance()
					.getTimeInMillis());
			
			doc = Jsoup.parse(StockUtils.getFileContent(excelFile));
			if(doc == null){
				LOGGER.error("doc is null, return now...");
				return;
			}
			Iterator<Element> trIter = doc.select("body > table > tbody > tr").iterator();
			if(trIter == null){
				LOGGER.error("trIter is null, reutrn null now...");
				return;
			}
			
			Timestamp tradeDate = null;
			while(trIter.hasNext()){
				Element trElement = trIter.next();
				// 指数代码: body > table > tbody > tr:nth-child(3) > td:nth-child(1)
				String indexCodeStr = trElement.select("> td:nth-child(1)").text();
				
				if(pattern.matcher(indexCodeStr).matches()){
					// 交易日期
					String tradeDateStr = trElement.select("> td:nth-child(2)").text().replaceAll("\\.", "-");
					tradeDate = Timestamp.valueOf(tradeDateStr + " 00:00:00");
					continue;
				}
				if(StringUtils.isEmpty(indexCodeStr) ||
						(!indexCodeStr.startsWith("1") && !indexCodeStr.startsWith("3"))){
					continue;
				}
				String indexCode = indexCodeStr.split("\\.")[0];
				// 指数简称: body > table > tbody > tr:nth-child(3) > td:nth-child(2)
				String indexName = trElement.select("> td:nth-child(2)").text();
				// 收盘价: body > table > tbody > tr:nth-child(3) > td:nth-child(11)
				Float close = Float.valueOf(trElement.select("> td:nth-child(11)").text());
				// 最新价
				Float newPrice = close;
				// 涨跌幅: body > table > tbody > tr:nth-child(3) > td:nth-child(4)
				Float changePercent = Float.valueOf(trElement.select("> td:nth-child(4)").text());
				// 开盘价: body > table > tbody > tr:nth-child(3) > td:nth-child(7)
				Float open = Float.valueOf(trElement.select("> td:nth-child(7)").text());
				// 最高价: body > table > tbody > tr:nth-child(3) > td:nth-child(8)
				Float high = Float.valueOf(trElement.select("> td:nth-child(8)").text());
				// 成交量(万手) body > table > tbody > tr:nth-child(3) > td:nth-child(10)
				String volumnStr = df.format(Float.valueOf(trElement.select("> td:nth-child(10)").text())/1000000.0f);
				Float volumn = Float.valueOf(volumnStr);
				// 成交额(亿) body > table > tbody > tr:nth-child(3) > td:nth-child(6)
				String volumnAmountStr = df.format(Float.valueOf(trElement.select("> td:nth-child(6)").text())/100000000.0f);
				Float volumnAmount = Float.valueOf(volumnAmountStr);
				
				Index index = new Index();
				index.setTradeDate(tradeDate);
				index.setIndexName(indexName);
				index.setIndexCode(indexCode);
				index.setNewPrice(newPrice);
				index.setChangePercent(changePercent);
				index.setOpen(open);
				index.setHigh(high);
				index.setClose(close);
				// 振幅使用默认值
				index.setChangeAmount(-99999.99f);
				index.setLastClose(-99999.99f);
				index.setAmplitude(-99.0f);
				index.setLow(-99999.99f);
				
				index.setVolumn(volumn);
				index.setVolumnAmount(volumnAmount);
				index.setTimestamp(timestamp);
				indexList.add(index);
			}
		}
		if(indexList != null && !indexList.isEmpty()){
			indexService.insertIndexBatch(indexList);
		}
	
	}
	
	private Set<String> excludeNotionNameForChromeUrl(){
		Set<String> result = new HashSet<String>();
		result.add("草甘霖");
		result.add("草甘膦");
		result.add("转融券标的");
		result.add("融资融券");
		result.add("沪深300样本股");
		result.add("上证380成分股");
		result.add("上证380成份股");
		result.add("上证180成分股");
		result.add("上证180成份股");
		result.add("中证500成份股");
		result.add("ST板块");
		result.add("禽流感");
		result.add("王亚伟");
		result.add("并购投资基金");
		result.add("参股新股");
		result.add("举牌");
		result.add("马云概念");
		result.add("证金持股");
		result.add("太阳能");
		result.add("参股新股");
		result.add("网络彩票");
		result.add("新三板");
		result.add("员工持股计划");
		result.add("风能");
		result.add("大健康产业");
		return result;
	}
	
	private Document getNotionIndustryDoc(File file){
		if(!file.exists()){
			LOGGER.error("file: " + file.getAbsolutePath() + " not exists.");
			return null;
		}
		
		Document doc = Jsoup.parse(StockUtils.getFileContent(file));
		
		if(doc == null){
			LOGGER.error("doc is null, return now...");
			return null;
		}
		return doc;
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

	public StockInfoService getStockInfoService() {
		return stockInfoService;
	}

	public void setStockInfoService(StockInfoService stockInfoService) {
		this.stockInfoService = stockInfoService;
	}

}
