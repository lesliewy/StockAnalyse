/**
 * 
 */
package com.wy.stock.dao.impl;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.wy.stock.dao.NotionHotDao;
import com.wy.stock.domain.NotionHot;
import com.wy.stock.utils.StockConstant;

/**
 * @author leslie
 *
 */
public class NotionHotDaoImpl extends SqlMapClientDaoSupport implements NotionHotDao {

	private static Logger LOGGER = Logger.getLogger(NotionHotDaoImpl.class
			.getName());

	public void insertNotionHot(NotionHot notionHot) {
		if(notionHot == null){
			LOGGER.info("notionHot is null, return now...");
			return;
		}
		try{
			getSqlMapClientTemplate().insert("insertNotionHot", notionHot);
		}catch(Exception e){
			LOGGER.info("insertNotionHot: " + e);
		}
	}

	public void insertNotionHotBatch(List<NotionHot> notionHotList) {
		if(notionHotList == null){
			LOGGER.info("notionHotList is null, return now...");
			return;
		}
		for(NotionHot notionHot : notionHotList){
			insertNotionHot(notionHot);
		}
	}

	public void deleteNotionHotByTradeDateStr(String tradeDateStr, String source) {
		if(tradeDateStr == null){
			LOGGER.info("tradeDate is null, return now...");
			return;
		}
		NotionHot query = new NotionHot();
		query.setTradeDateStr(tradeDateStr);
		query.setSource(source);
		getSqlMapClientTemplate().delete("deleteNotionHotByTradeDateStr", query);
	}

	@SuppressWarnings("unchecked")
	public List<NotionHot> queryNotionHotByDate(Timestamp tradeDate) {
		if(tradeDate == null){
			LOGGER.info("tradeDate is null, return null now...");
			return null;
		}
		return getSqlMapClientTemplate().queryForList("queryNotionHotByDate", tradeDate);
	}

	@SuppressWarnings("unchecked")
	public List<NotionHot> queryNotionHotInfoByDateStr(String tradeDateStr, String source) {
		NotionHot query = new NotionHot();
		query.setTradeDateStr(tradeDateStr);
		query.setSource(source);
		return getSqlMapClientTemplate().queryForList("queryNotionHotInfoByDateStr", query);
	}

	@SuppressWarnings("unchecked")
	public List<NotionHot> queryNotionHotForCsv(int year) {
		String lowTradeDateStr = year + "-01-01";
		String highTradeDateStr = year + "-12-31";
		NotionHot query = new NotionHot();
		query.setLowTradeDateStr(lowTradeDateStr);
		query.setHighTradeDateStr(highTradeDateStr);
		return getSqlMapClientTemplate().queryForList("queryNotionHotForCsv", query);
	}

	@SuppressWarnings("unchecked")
	public List<NotionHot> queryNotionHotByDateStr(String tradeDateStr,
			String source) {
		NotionHot query = new NotionHot();
		query.setTradeDateStr(tradeDateStr);
		query.setSource(source);
		return getSqlMapClientTemplate().queryForList("queryNotionHotByDateStr", query);
	}

	@SuppressWarnings("unchecked")
	public List<NotionHot> queryNotionHotByDateStrBetween(
			String lowTradeDateStr, String highTradeDateStr) {
		NotionHot query = new NotionHot();
		query.setLowTradeDateStr(lowTradeDateStr);
		query.setHighTradeDateStr(highTradeDateStr);
		query.setSource(StockConstant.THS_FLAG);
		return getSqlMapClientTemplate().queryForList("queryNotionHotByDateStrBetween", query);
	}

}
