/**
 * 
 */
package com.wy.stock.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.wy.stock.dao.StockInfoDao;
import com.wy.stock.domain.StockInfo;

/**
 * @author leslie
 *
 */
public class StockInfoDaoImpl extends SqlMapClientDaoSupport implements StockInfoDao {

	private static Logger LOGGER = Logger.getLogger(StockInfoDaoImpl.class
			.getName());
	
	public void insertStockInfo(StockInfo info) {
		if(info == null){
			LOGGER.info("info is null, return now...");
			return;
		}
		try{
			getSqlMapClientTemplate().insert("insertStockInfo", info);
		}catch(Exception e){
			LOGGER.info("insertStockInfo: " + e);
		}
	}

	public void insertStockInfoBatch(List<StockInfo> infoList) {
		if(infoList == null){
			LOGGER.info("infoList is null, return now...");
			return;
		}
		for(StockInfo info : infoList){
			insertStockInfoAfterDelete(info);
		}
	}

	public void deleteByExchangeType(StockInfo stockInfo) {
		if(StringUtils.isBlank(stockInfo.getExchange()) || StringUtils.isBlank(stockInfo.getType())){
			LOGGER.info("exchange or type is blank, return now...");
			return;
		}
		getSqlMapClientTemplate().delete("deleteByExchangeType", stockInfo);
	}

	public void deleteByExchange(String exchange) {
		if(StringUtils.isBlank(exchange)){
			LOGGER.info("exchange is blank, return now...");
			return;
		}
		getSqlMapClientTemplate().delete("deleteByExchange", exchange);
	}

	public void deleteByKey(StockInfo stockInfo) {
		getSqlMapClientTemplate().delete("deleteByKey", stockInfo);
	}

	public void insertStockInfoAfterDelete(StockInfo info) {
		if(info == null){
			LOGGER.info("info is null, return now...");
			return;
		}
		// 先删除
		deleteByKey(info);
		try{
			getSqlMapClientTemplate().insert("insertStockInfo", info);
		}catch(Exception e){
			LOGGER.info("insertStockInfo: " + e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<StockInfo> queryAllStockInfo() {
		return getSqlMapClientTemplate().queryForList("queryAllStockInfo");
	}

	@SuppressWarnings("unchecked")
	public List<StockInfo> queryStockInfoByExchange(String exchange) {
		return getSqlMapClientTemplate().queryForList("queryStockInfoByExchange", exchange);
	}

	@SuppressWarnings("unchecked")
	public List<StockInfo> queryStockInfoByType(String type) {
		return getSqlMapClientTemplate().queryForList("queryStockInfoByType", type);
	}
}
