/**
 * 
 */
package com.wy.stock.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.wy.stock.dao.IndustryHotStocksDao;
import com.wy.stock.domain.IndustryHotStocks;

/**
 * @author leslie
 *
 */
public class IndustryHotStocksDaoImpl extends SqlMapClientDaoSupport implements IndustryHotStocksDao {

	private static Logger LOGGER = Logger.getLogger(IndustryHotStocksDaoImpl.class
			.getName());

	public void insertIndustryHotStocks(IndustryHotStocks industryHotStocks) {
		if(industryHotStocks == null){
			LOGGER.info("industryHotStocks is null, return now...");
			return;
		}
		try{
			getSqlMapClientTemplate().insert("insertIndustryHotStocks", industryHotStocks);
		}catch(Exception e){
			LOGGER.info("insertIndustryHotStocks: " + e);
		}
	}

	public void insertIndustryHotStocksBatch(
			List<IndustryHotStocks> industryHotStocksList) {
		if(industryHotStocksList == null){
			LOGGER.info("industryHotStocksList is null, return now...");
			return;
		}
		for(IndustryHotStocks industryHotStocks : industryHotStocksList){
			insertIndustryHotStocks(industryHotStocks);
		}
	}

	public void deleteIndustryHotStocksByIndustryName(String industryName) {
		if(StringUtils.isBlank(industryName)){
			LOGGER.info("industryName is null, return now...");
			return;
		}
		getSqlMapClientTemplate().delete("deleteIndustryHotStocksByIndustryName", industryName);
	}

	@SuppressWarnings("unchecked")
	public List<IndustryHotStocks> queryIndustryHotStocksByIndustryName(
			String industryName) {
		if(StringUtils.isBlank(industryName)){
			LOGGER.info("industryName is null, return null now...");
			return null;
		}
		return getSqlMapClientTemplate().queryForList("queryIndustryHotStocksByIndustryName", industryName);
	}
	
	@SuppressWarnings("unchecked")
	public List<IndustryHotStocks> queryIndustryHotStocksForCsv(int year) {
		String lowTradeDateStr = year + "-01-01";
		String highTradeDateStr = year + "-12-31";
		IndustryHotStocks query = new IndustryHotStocks();
		query.setLowTradeDateStr(lowTradeDateStr);
		query.setHighTradeDateStr(highTradeDateStr);
		return getSqlMapClientTemplate().queryForList("queryIndustryHotStocksForCsv", query);
	}

	@SuppressWarnings("unchecked")
	public List<IndustryHotStocks> queryIndustryHotStocksByDateStrBetween(
			String lowTradeDateStr, String highTradeDateStr) {
		IndustryHotStocks query = new IndustryHotStocks();
		query.setLowTradeDateStr(lowTradeDateStr);
		query.setHighTradeDateStr(highTradeDateStr);
		return getSqlMapClientTemplate().queryForList("queryIndustryHotStocksByDateStrBetween", query);
	}
	
	@SuppressWarnings("unchecked")
	public List<IndustryHotStocks> queryStocksIndustriesBetween(
			String lowTradeDateStr, String highTradeDateStr) {
		IndustryHotStocks query = new IndustryHotStocks();
		query.setLowTradeDateStr(lowTradeDateStr);
		query.setHighTradeDateStr(highTradeDateStr);
		return getSqlMapClientTemplate().queryForList("queryStocksIndustriesBetween", query);
	}
}
