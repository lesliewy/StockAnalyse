/**
 * 
 */
package com.wy.stock.service.impl;

import java.sql.Timestamp;
import java.util.List;

import com.wy.stock.dao.NotionHotDao;
import com.wy.stock.domain.NotionHot;
import com.wy.stock.service.NotionHotService;

/**
 * @author leslie
 *
 */
public class NotionHotServiceImpl implements NotionHotService {

	private NotionHotDao notionHotDao;
	
	public void insertNotionHot(NotionHot notionHot) {
		notionHotDao.insertNotionHot(notionHot);
	}

	public void insertNotionHotBatch(List<NotionHot> notionHotList) {
		notionHotDao.insertNotionHotBatch(notionHotList);
	}

	public void deleteNotionHotByTradeDateStr(String tradeDateStr, String source) {
		notionHotDao.deleteNotionHotByTradeDateStr(tradeDateStr, source);
	}

	public List<NotionHot> queryNotionHotByDate(Timestamp tradeDate) {
		return notionHotDao.queryNotionHotByDate(tradeDate);
	}
	
	public List<NotionHot> queryNotionHotByDateStr(String tradeDateStr,
			String source) {
		return notionHotDao.queryNotionHotByDateStr(tradeDateStr, source);
	}
	
	public List<NotionHot> queryNotionHotInfoByDateStr(String tradeDateStr, String type, String source) {
		return notionHotDao.queryNotionHotInfoByDateStr(tradeDateStr, type, source);
	}
	
	public List<NotionHot> queryNotionHotForCsv(int year) {
		return notionHotDao.queryNotionHotForCsv(year);
	}
	
	public List<NotionHot> queryNotionHotByDateStrBetween(
			String lowTradeDateStr, String highTradeDateStr) {
		return notionHotDao.queryNotionHotByDateStrBetween(lowTradeDateStr, highTradeDateStr);
	}


	public NotionHotDao getNotionHotDao() {
		return notionHotDao;
	}

	public void setNotionHotDao(NotionHotDao notionHotDao) {
		this.notionHotDao = notionHotDao;
	}

}
