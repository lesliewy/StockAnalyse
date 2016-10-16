/**
 * 
 */
package com.wy.stock.dao;

import java.util.List;

import com.wy.stock.domain.IndustryHotStocks;

/**
 * @author leslie
 *
 */
public interface IndustryHotStocksDao {
	
	void insertIndustryHotStocks(IndustryHotStocks industryHotStocks);
	
	void insertIndustryHotStocksBatch(List<IndustryHotStocks> industryHotStocksList);
	
	void deleteIndustryHotStocksByIndustryName(String industryName);
	
	List<IndustryHotStocks> queryIndustryHotStocksByIndustryName(String industryName);
	
	List<IndustryHotStocks> queryIndustryHotStocksForCsv(int year);
	
	List<IndustryHotStocks> queryIndustryHotStocksByDateStrBetween(String lowTradeDateStr, String highTradeDateStr);
	
	List<IndustryHotStocks> queryStocksIndustriesBetween(String lowTradeDateStr, String highTradeDateStr);
	
}
