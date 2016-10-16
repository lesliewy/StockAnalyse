/**
 * 
 */
package com.wy.stock.dao;

import java.util.List;

import com.wy.stock.domain.IndustryStock;

/**
 * @author leslie
 *
 */
public interface IndustryStockDao {
	
	void insertIndustryStock(IndustryStock industryStock);
	
	void insertIndustryStockBatch(List<IndustryStock> industryStockList);
	
	void deleteAllIndustryStock(String source);
	
	void deleteIndustryStockByIndustryCode(String industryCode);
	
	List<IndustryStock> queryIndustryStockByIndustryCode(IndustryStock industryStock);
	
	List<IndustryStock> queryIndustryStockByCode(String code);
	
	List<IndustryStock> queryAllIndustryStock(String source);
	
}
