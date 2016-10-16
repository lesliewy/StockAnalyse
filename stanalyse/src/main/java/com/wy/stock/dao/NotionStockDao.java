/**
 * 
 */
package com.wy.stock.dao;

import java.util.List;

import com.wy.stock.domain.NotionStock;

/**
 * @author leslie
 *
 */
public interface NotionStockDao {
	
	void insertNotionStock(NotionStock notionStock);
	
	void insertNotionStockBatch(List<NotionStock> notionStockList);
	
	void deleteAllNotionStock(String source);
	
	void deleteNotionStockByNotionCode(String notionCode);
	
	List<NotionStock> queryNotionStockByNotionCode(NotionStock notionStock);
	
	List<NotionStock> queryNotionStockByCode(String code);
	
	List<NotionStock> queryAllCodeInNotionStock();
	
	List<NotionStock> queryNotionCodesNames(String source);
	
}
