/**
 * 
 */
package com.wy.stock.dao;

import java.sql.Timestamp;
import java.util.List;

import com.wy.stock.domain.IndustryHot;

/**
 * @author leslie
 *
 */
public interface IndustryHotDao {
	
	void insertIndustryHot(IndustryHot industryHot);
	
	void insertIndustryHotBatch(List<IndustryHot> industryHotList);
	
	void deleteIndustryHotByTradeDateStr(String tradeDateStr, String source);
	
	List<IndustryHot> queryIndustryHotByDate(Timestamp tradeDate);
	
	List<IndustryHot> queryIndustryHotInfoByDate(IndustryHot industryHot);
	
	List<IndustryHot> queryIndustryHotForCsv(int year);
	
	List<IndustryHot> queryIndustryHotByDateStr(String tradeDateStr, String source);
	
	List<IndustryHot> queryIndustryHotByDateStrBetween(String lowTradeDateStr, String highTradeDateStr);
	
}
