/**
 * 
 */
package com.wy.stock.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wy.stock.dao.NotionHotStocksDao;
import com.wy.stock.domain.NotionHotStocks;
import com.wy.stock.service.NotionHotStocksService;

/**
 * @author leslie
 *
 */
public class NotionHotStocksServiceImpl implements NotionHotStocksService {

	private NotionHotStocksDao notionHotStocksDao;
	
	public void insertNotionHotStocks(NotionHotStocks notionHotStocks) {
		notionHotStocksDao.insertNotionHotStocks(notionHotStocks);
	}

	public void insertNotionHotStocksBatch(List<NotionHotStocks> notionHotStocksList) {
		notionHotStocksDao.insertNotionHotStocksBatch(notionHotStocksList);
	}

	public void deleteNotionHotStocksByNotionName(String notionName) {
		notionHotStocksDao.deleteNotionHotStocksByNotionName(notionName);
	}

	public List<NotionHotStocks> queryNotionHotStocksByNotionName(String notionName) {
		return notionHotStocksDao.queryNotionHotStocksByNotionName(notionName);
	}
	
	public List<NotionHotStocks> queryNotionHotStocksForCsv(int year) {
		return notionHotStocksDao.queryNotionHotStocksForCsv(year);
	}
	
	public List<NotionHotStocks> queryStocksByNotionNameDateStr(
			String notionName, String tradeDateStr) {
		return notionHotStocksDao.queryStocksByNotionNameDateStr(notionName, tradeDateStr);
	}
	
	public List<NotionHotStocks> queryNotionHotStocksByDateStrBetween(
			String lowTradeDateStr, String highTradeDateStr) {
		return notionHotStocksDao.queryNotionHotStocksByDateStrBetween(lowTradeDateStr, highTradeDateStr);
	}
	
	public List<NotionHotStocks> queryStocksNotionsBetween(
			String lowTradeDateStr, String highTradeDateStr) {
		return notionHotStocksDao.queryStocksNotionsBetween(lowTradeDateStr, highTradeDateStr);
	}
	
	public Map<String, String> queryStocksNotionsBetweenMap(
			String lowTradeDateStr, String highTradeDateStr) {
		List<NotionHotStocks> list = queryStocksNotionsBetween(lowTradeDateStr, highTradeDateStr);
		if(list == null || list.isEmpty()){
			return null;
		}
		Map<String, String> result = new HashMap<String, String>();
		for(NotionHotStocks notionHotStocks : list){
			result.put(notionHotStocks.getStockName(), notionHotStocks.getNotionNames());
		}
		return result;
	}
	
	public List<NotionHotStocks> queryStocksChangePctBetween(
			String lowTradeDateStr, String highTradeDateStr) {
		return notionHotStocksDao.queryStocksChangePctBetween(lowTradeDateStr, highTradeDateStr);
	}

	public Map<String, Float> queryStocksChangePctBetweenMap(
			String lowTradeDateStr, String highTradeDateStr) {
		List<NotionHotStocks> list = queryStocksChangePctBetween(lowTradeDateStr, highTradeDateStr);
		if(list == null || list.isEmpty()){
			return null;
		}
		Map<String, Float> result = new HashMap<String, Float>();
		for(NotionHotStocks notionHotStocks : list){
			result.put(notionHotStocks.getStockName(), notionHotStocks.getTotalChangePercent());
		}
		return result;
	}

	public NotionHotStocksDao getNotionHotStocksDao() {
		return notionHotStocksDao;
	}

	public void setNotionHotStocksDao(NotionHotStocksDao notionHotStocksDao) {
		this.notionHotStocksDao = notionHotStocksDao;
	}

}
