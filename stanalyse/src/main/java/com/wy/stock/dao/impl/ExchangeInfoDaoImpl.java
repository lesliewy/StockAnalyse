/**
 * 
 */
package com.wy.stock.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.wy.stock.dao.ExchangeInfoDao;
import com.wy.stock.domain.ExchangeInfo;

/**
 * @author leslie
 *
 */
public class ExchangeInfoDaoImpl extends SqlMapClientDaoSupport implements ExchangeInfoDao {

	private static Logger LOGGER = Logger.getLogger(ExchangeInfoDaoImpl.class
			.getName());
	
	public void insertExchangeInfo(ExchangeInfo info) {
		if(info == null){
			LOGGER.info("info is null, return now...");
			return;
		}
		try{
			getSqlMapClientTemplate().insert("insertExchangeInfo", info);
		}catch(Exception e){
			LOGGER.info("insertStockInfo: " + e);
		}
	}

	public void insertExchangeInfoBatch(List<ExchangeInfo> infoList) {
		if(infoList == null){
			LOGGER.info("infoList is null, return now...");
			return;
		}
		for(ExchangeInfo info : infoList){
			insertExchangeInfoAfterDelete(info);
		}
	}

	public void deleteByExchangeType(ExchangeInfo exchangeInfo) {
		if(StringUtils.isBlank(exchangeInfo.getExchange()) || StringUtils.isBlank(exchangeInfo.getType())){
			LOGGER.info("exchange or type is blank, return now...");
			return;
		}
		getSqlMapClientTemplate().delete("deleteByExchangeType", exchangeInfo);
	}

	public void deleteByExchange(String exchange) {
		if(StringUtils.isBlank(exchange)){
			LOGGER.info("exchange is blank, return now...");
			return;
		}
		getSqlMapClientTemplate().delete("deleteByExchange", exchange);
	}

	public void deleteByKey(ExchangeInfo exchangeInfo) {
		getSqlMapClientTemplate().delete("deleteByKey", exchangeInfo);
	}

	public void insertExchangeInfoAfterDelete(ExchangeInfo info) {
		if(info == null){
			LOGGER.info("info is null, return now...");
			return;
		}
		// 先删除
		deleteByKey(info);
		try{
			getSqlMapClientTemplate().insert("insertExchangeInfo", info);
		}catch(Exception e){
			LOGGER.info("insertExchangeInfo: " + e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<ExchangeInfo> queryAllExchangeInfo() {
		return getSqlMapClientTemplate().queryForList("queryAllExchangeInfo");
	}

	@SuppressWarnings("unchecked")
	public List<ExchangeInfo> queryExchangeInfoByExchange(String exchange) {
		return getSqlMapClientTemplate().queryForList("queryExchangeInfoByExchange", exchange);
	}

	@SuppressWarnings("unchecked")
	public List<ExchangeInfo> queryExchangeInfoByType(String type) {
		return getSqlMapClientTemplate().queryForList("queryExchangeInfoByType", type);
	}
}
