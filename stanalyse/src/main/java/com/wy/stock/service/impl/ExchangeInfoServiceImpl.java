/**
 * 
 */
package com.wy.stock.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wy.stock.dao.ExchangeInfoDao;
import com.wy.stock.domain.ExchangeInfo;
import com.wy.stock.service.ExchangeInfoService;

/**
 * @author leslie
 *
 */
public class ExchangeInfoServiceImpl implements ExchangeInfoService {

	private ExchangeInfoDao exchangeInfoDao;
	
	public void insertExchangeInfo(ExchangeInfo info) {
		exchangeInfoDao.insertExchangeInfo(info);
	}

	public void insertExchangeInfoBatch(List<ExchangeInfo> infoList) {
		exchangeInfoDao.insertExchangeInfoBatch(infoList);
	}
	
	public void deleteByExchangeType(String exchange, String type) {
		ExchangeInfo query = new ExchangeInfo();
		query.setExchange(exchange);
		query.setType(type);
		exchangeInfoDao.deleteByExchangeType(query);
	}

	public void deleteByExchange(String exchange) {
		exchangeInfoDao.deleteByExchange(exchange);
	}
	
	public List<ExchangeInfo> queryAllExchangeInfo() {
		return exchangeInfoDao.queryAllExchangeInfo();
	}
	
	public List<ExchangeInfo> queryExchangeInfoByExchange(String exchange) {
		return exchangeInfoDao.queryExchangeInfoByExchange(exchange);
	}
	
	public List<ExchangeInfo> queryExchangeInfoByType(String type) {
		return exchangeInfoDao.queryExchangeInfoByType(type);
	}
	
	public Map<String, String> queryStockCodeNameMap() {
		List<ExchangeInfo> allInfo = exchangeInfoDao.queryAllExchangeInfo();
		if(allInfo == null || allInfo.isEmpty()){
			return null;
		}
		Map<String, String> result = new HashMap<String, String>();
		for(ExchangeInfo info : allInfo){
			result.put(info.getCode(), info.getName());
		}
		return result;
	}
	
	public Map<String, String> queryStockNameCodeMap() {
		List<ExchangeInfo> allInfo = exchangeInfoDao.queryAllExchangeInfo();
		if(allInfo == null || allInfo.isEmpty()){
			return null;
		}
		Map<String, String> result = new HashMap<String, String>();
		for(ExchangeInfo info : allInfo){
			result.put(info.getName(), info.getCode());
		}
		return result;
	}

	public ExchangeInfoDao getExchangeInfoDao() {
		return exchangeInfoDao;
	}

	public void setExchangeInfoDao(ExchangeInfoDao exchangeInfoDao) {
		this.exchangeInfoDao = exchangeInfoDao;
	}

}
