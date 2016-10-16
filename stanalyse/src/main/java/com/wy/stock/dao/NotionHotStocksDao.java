/**
 * 
 */
package com.wy.stock.dao;

import java.util.List;

import com.wy.stock.domain.NotionHotStocks;

/**
 * @author leslie
 *
 */
public interface NotionHotStocksDao {
	
	void insertNotionHotStocks(NotionHotStocks notionHotStocks);
	
	void insertNotionHotStocksBatch(List<NotionHotStocks> notionHotStocksList);
	
	void deleteNotionHotStocksByNotionName(String notionName);
	
	List<NotionHotStocks> queryNotionHotStocksByNotionName(String notionName);
	
	List<NotionHotStocks> queryNotionHotStocksForCsv(int year);
	
	List<NotionHotStocks> queryStocksByNotionNameDateStr(String notionName, String tradeDateStr);
	
	List<NotionHotStocks> querySumChangePctByDateStr(String lowTradeDateStr, String highTradeDateStr);
	
	List<NotionHotStocks> queryNotionHotStocksByDateStrBetween(String lowTradeDateStr, String highTradeDateStr);
	
	List<NotionHotStocks> queryStocksNotionsBetween(String lowTradeDateStr, String highTradeDateStr);
	
	List<NotionHotStocks> queryStocksChangePctBetween(String lowTradeDateStr, String highTradeDateStr);
	
}
