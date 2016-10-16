/**
 * 
 */
package com.wy.stock.dao.impl;

import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.wy.stock.dao.IndustryHotDao;
import com.wy.stock.domain.IndustryHot;
import com.wy.stock.utils.StockConstant;

/**
 * @author leslie
 *
 */
public class IndustryHotDaoImpl extends SqlMapClientDaoSupport implements IndustryHotDao {

	private static Logger LOGGER = Logger.getLogger(IndustryHotDaoImpl.class
			.getName());

	public void insertIndustryHot(IndustryHot industryHot) {
		if(industryHot == null){
			LOGGER.info("industryHot is null, return now...");
			return;
		}
		try{
			getSqlMapClientTemplate().insert("insertIndustryHot", industryHot);
		}catch(Exception e){
			LOGGER.info("insertIndustryHot: " + e);
		}
		
	}

	public void insertIndustryHotBatch(List<IndustryHot> industryHotList) {
		if(industryHotList == null){
			LOGGER.info("industryHotList is null, return now...");
			return;
		}
		for(IndustryHot industryHot : industryHotList){
			insertIndustryHot(industryHot);
		}
	}
	
	public void deleteIndustryHotByTradeDateStr(String tradeDateStr, String source) {
		if(tradeDateStr == null){
			LOGGER.info("tradeDate is null, return now...");
			return;
		}
		IndustryHot query = new IndustryHot();
		query.setTradeDateStr(tradeDateStr);
		query.setSource(source);
		getSqlMapClientTemplate().delete("deleteIndustryHotByTradeDateStr", query);
	}

	@SuppressWarnings("unchecked")
	public List<IndustryHot> queryIndustryHotByDate(Timestamp tradeDate) {
		if(tradeDate == null){
			LOGGER.info("tradeDate is null, return null now...");
			return null;
		}
		return getSqlMapClientTemplate().queryForList("queryIndustryHotByDate", tradeDate);
	}

	@SuppressWarnings("unchecked")
	public List<IndustryHot> queryIndustryHotInfoByDate(IndustryHot industryHot) {
		if(StringUtils.isBlank(industryHot.getSource()) || StringUtils.isBlank(industryHot.getTradeDateStr())){
			LOGGER.info("tradeDateStr or source is null, return null now...");
			return null;
		}
		return getSqlMapClientTemplate().queryForList("queryIndustryHotInfoByDateStr", industryHot);
	}
	

	@SuppressWarnings("unchecked")
	public List<IndustryHot> queryIndustryHotForCsv(int year) {
		String lowTradeDateStr = year + "-01-01";
		String highTradeDateStr = year + "-12-31";
		IndustryHot query = new IndustryHot();
		query.setLowTradeDateStr(lowTradeDateStr);
		query.setHighTradeDateStr(highTradeDateStr);
		return getSqlMapClientTemplate().queryForList("queryIndustryHotForCsv", query);
	}

	@SuppressWarnings("unchecked")
	public List<IndustryHot> queryIndustryHotByDateStr(String tradeDateStr,
			String source) {
		IndustryHot query = new IndustryHot();
		query.setTradeDateStr(tradeDateStr);
		query.setSource(source);
		return getSqlMapClientTemplate().queryForList("queryIndustryHotByDateStr", query);
	}
	
	@SuppressWarnings("unchecked")
	public List<IndustryHot> queryIndustryHotByDateStrBetween(
			String lowTradeDateStr, String highTradeDateStr) {
		IndustryHot query = new IndustryHot();
		query.setLowTradeDateStr(lowTradeDateStr);
		query.setHighTradeDateStr(highTradeDateStr);
		query.setSource(StockConstant.THS_FLAG);
		return getSqlMapClientTemplate().queryForList("queryIndustryHotByDateStrBetween", query);
	}

}
