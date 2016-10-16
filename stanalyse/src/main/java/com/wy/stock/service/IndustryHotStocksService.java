/**
 * 
 */
package com.wy.stock.service;

import java.util.List;
import java.util.Map;

import com.wy.stock.domain.IndustryHotStocks;

/**
 * @author leslie
 *
 */
public interface IndustryHotStocksService {
	
	void insertIndustryHotStocks(IndustryHotStocks industryHotStocks);
	
	void insertIndustryHotStocksBatch(List<IndustryHotStocks> industryHotStocksList);
	
	void deleteIndustryHotStocksByIndustryName(String industryName);
	
	List<IndustryHotStocks> queryIndustryHotStocksByIndustryName(String industryName);
	
	List<IndustryHotStocks> queryIndustryHotStocksForCsv(int year);
	
	List<IndustryHotStocks> queryIndustryHotStocksByDateStrBetween(String lowTradeDateStr, String highTradeDateStr);
	
	List<IndustryHotStocks> queryStocksIndustriesBetween(String lowTradeDateStr, String highTradeDateStr);
	
	Map<String, String> queryStocksIndustriesBetweenMap(String lowTradeDateStr, String highTradeDateStr);
}
