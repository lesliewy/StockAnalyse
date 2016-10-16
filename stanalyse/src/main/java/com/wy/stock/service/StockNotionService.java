/**
 * 
 */
package com.wy.stock.service;

import java.util.List;

import com.wy.stock.domain.StockNotion;

/**
 * @author leslie
 *
 */
public interface StockNotionService {
	
	void insertStockNotion(StockNotion stockNotion);
	
	void insertStockNotionBatch(List<StockNotion> stockNotionList);
	
	void deleteAllStockNotion();
	
	void deleteStockNotionByCode(String code);
	
	List<StockNotion> queryAllStockNotion();
	
	StockNotion queryStockNotionByCode(String code);
	
}
