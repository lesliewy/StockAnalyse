/**
 * 
 */
package com.wy.stock.service;

import java.util.List;
import java.util.Map;

import com.wy.stock.domain.NotionHotStocks;

/**
 * @author leslie
 *
 */
public interface NotionHotStocksService {

	void insertNotionHotStocks(NotionHotStocks notionHotStocks);
	
	void insertNotionHotStocksBatch(List<NotionHotStocks> notionHotStocksList);
	
	void deleteNotionHotStocksByNotionName(String notionName);
	
	List<NotionHotStocks> queryNotionHotStocksByNotionName(String notionName);
	
	List<NotionHotStocks> queryNotionHotStocksForCsv(int year);
	
	List<NotionHotStocks> queryStocksByNotionNameDateStr(String notionName, String tradeDateStr);
	
	List<NotionHotStocks> queryNotionHotStocksByDateStrBetween(String lowTradeDateStr, String highTradeDateStr);
	
	List<NotionHotStocks> queryStocksNotionsBetween(String lowTradeDateStr, String highTradeDateStr);
	
	Map<String, String> queryStocksNotionsBetweenMap(String lowTradeDateStr, String highTradeDateStr);
	
	List<NotionHotStocks> queryStocksChangePctBetween(String lowTradeDateStr, String highTradeDateStr);
	
	Map<String, Float> queryStocksChangePctBetweenMap(String lowTradeDateStr, String highTradeDateStr);
}
