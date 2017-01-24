/**
 * 
 */
package com.wy.stock.service;

import java.util.List;
import java.util.Map;

import com.wy.stock.domain.IndustryStock;

/**
 * @author leslie
 *
 */
public interface IndustryStockService {
	
	void insertIndustryStock(IndustryStock industryStock);
	
	void insertIndustryStockBatch(List<IndustryStock> industryStockList);
	
	void deleteAllIndustryStock(String source);
	
	void deleteIndustryStockByIndustryCode(String industryCode);
	
	List<IndustryStock> queryIndustryStockByIndustryCode(String industryCode, String source);
	
	List<IndustryStock> queryIndustryStockByIndustryName(String industryName, String source);
	
	List<String> queryCodeByIndustryCode(String industryCode, String source);
	
	List<String> queryCodeByIndustryName(String industryName, String source);
	
	List<IndustryStock> queryIndustryStockByCode(String code);
	
	List<IndustryStock> queryAllIndustryStock(String source);
	
	Map<String, String> queryCodeIndustryNameMap(String source);
}
