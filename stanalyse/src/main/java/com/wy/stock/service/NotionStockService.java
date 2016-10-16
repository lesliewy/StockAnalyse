/**
 * 
 */
package com.wy.stock.service;

import java.util.List;

import com.wy.stock.domain.NotionStock;

/**
 * @author leslie
 *
 */
public interface NotionStockService {
	
	void insertNotionStock(NotionStock notionStock);
	
	void insertNotionStockBatch(List<NotionStock> notionStockList);
	
	void deleteAllNotionStock(String source);
	
	void deleteNotionStockByNotionCode(String notionCode);
	
	List<NotionStock> queryNotionStockByNotionCode(String notionCode, String source);
	
	List<String> queryCodeByNotionCode(String notionCode, String source);
	
	List<NotionStock> queryNotionStockByCode(String code);
	
	List<NotionStock> queryAllCodeInNotionStock();
	
	List<NotionStock> queryNotionCodesNames(String source);
	
}
