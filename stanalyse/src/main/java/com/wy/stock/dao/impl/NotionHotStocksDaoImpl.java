/**
 * 
 */
package com.wy.stock.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.wy.stock.dao.NotionHotStocksDao;
import com.wy.stock.domain.NotionHotStocks;

/**
 * @author leslie
 *
 */
public class NotionHotStocksDaoImpl extends SqlMapClientDaoSupport implements NotionHotStocksDao {

	private static Logger LOGGER = Logger.getLogger(NotionHotStocksDaoImpl.class
			.getName());

	public void insertNotionHotStocks(NotionHotStocks notionHotStocks) {
		if(notionHotStocks == null){
			LOGGER.info("notionHotStocks is null, return now...");
			return;
		}
		try{
			getSqlMapClientTemplate().insert("insertNotionHotStocks", notionHotStocks);
		}catch(Exception e){
			LOGGER.info("insertNotionHotStocks: " + e);
		}
	}

	public void insertNotionHotStocksBatch(
			List<NotionHotStocks> notionHotStocksList) {
		if(notionHotStocksList == null){
			LOGGER.info("notionHotStocksList is null, return now...");
			return;
		}
		for(NotionHotStocks notionHotStocks : notionHotStocksList){
			insertNotionHotStocks(notionHotStocks);
		}
	}

	public void deleteNotionHotStocksByNotionName(String notionName) {
		if(StringUtils.isBlank(notionName)){
			LOGGER.info("notionName is null, return now...");
			return;
		}
		getSqlMapClientTemplate().delete("deleteNotionHotStocksByNotionName", notionName);
	}

	@SuppressWarnings("unchecked")
	public List<NotionHotStocks> queryNotionHotStocksByNotionName(
			String notionName) {
		if(StringUtils.isBlank(notionName)){
			LOGGER.info("notionName is null, return null now...");
			return null;
		}
		return getSqlMapClientTemplate().queryForList("queryNotionHotStocksByNotionName", notionName);
	}

	@SuppressWarnings("unchecked")
	public List<NotionHotStocks> queryNotionHotStocksForCsv(int year) {
		String lowTradeDateStr = year + "-01-01";
		String highTradeDateStr = year + "-12-31";
		NotionHotStocks query = new NotionHotStocks();
		query.setLowTradeDateStr(lowTradeDateStr);
		query.setHighTradeDateStr(highTradeDateStr);
		return getSqlMapClientTemplate().queryForList("queryNotionHotStocksForCsv", query);
	}

	@SuppressWarnings("unchecked")
	public List<NotionHotStocks> queryStocksByNotionNameDateStr(
			String notionName, String tradeDateStr) {
		NotionHotStocks query = new NotionHotStocks();
		query.setNotionName(notionName);
		query.setTradeDateStr(tradeDateStr);
		return getSqlMapClientTemplate().queryForList("queryStocksByNotionNameDateStr", query);
	}

	@SuppressWarnings("unchecked")
	public List<NotionHotStocks> querySumChangePctByDateStr(
			String lowTradeDateStr, String highTradeDateStr) {
		NotionHotStocks query = new NotionHotStocks();
		query.setLowTradeDateStr(lowTradeDateStr);
		query.setHighTradeDateStr(highTradeDateStr);
		return getSqlMapClientTemplate().queryForList("querySumChangePctByDateStr", query);
	}

	@SuppressWarnings("unchecked")
	public List<NotionHotStocks> queryNotionHotStocksByDateStrBetween(
			String lowTradeDateStr, String highTradeDateStr) {
		NotionHotStocks query = new NotionHotStocks();
		query.setLowTradeDateStr(lowTradeDateStr);
		query.setHighTradeDateStr(highTradeDateStr);
		return getSqlMapClientTemplate().queryForList("queryNotionHotStocksByDateStrBetween", query);
	}

	@SuppressWarnings("unchecked")
	public List<NotionHotStocks> queryStocksNotionsBetween(
			String lowTradeDateStr, String highTradeDateStr) {
		NotionHotStocks query = new NotionHotStocks();
		query.setLowTradeDateStr(lowTradeDateStr);
		query.setHighTradeDateStr(highTradeDateStr);
		return getSqlMapClientTemplate().queryForList("queryStocksNotionsBetween", query);
	}

	@SuppressWarnings("unchecked")
	public List<NotionHotStocks> queryStocksChangePctBetween(
			String lowTradeDateStr, String highTradeDateStr) {
		NotionHotStocks query = new NotionHotStocks();
		query.setLowTradeDateStr(lowTradeDateStr);
		query.setHighTradeDateStr(highTradeDateStr);
		return getSqlMapClientTemplate().queryForList("queryStocksChangePctBetween", query);
	}

}
