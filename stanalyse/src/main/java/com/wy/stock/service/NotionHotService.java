/**
 * 
 */
package com.wy.stock.service;

import java.sql.Timestamp;
import java.util.List;

import com.wy.stock.domain.NotionHot;

/**
 * @author leslie
 *
 */
public interface NotionHotService {

	void insertNotionHot(NotionHot notionHot);
	
	void insertNotionHotBatch(List<NotionHot> notionHotList);
	
	void deleteNotionHotByTradeDateStr(String tradeDateStr, String source);
	
	List<NotionHot> queryNotionHotByDate(Timestamp tradeDate);
	
	List<NotionHot> queryNotionHotByDateStr(String tradeDateStr, String source);
	
	List<NotionHot> queryNotionHotInfoByDateStr(String tradeDateStr, String source);
	
	List<NotionHot> queryNotionHotForCsv(int year);
	
	List<NotionHot> queryNotionHotByDateStrBetween(String lowTradeDateStr, String highTradeDateStr);
}
