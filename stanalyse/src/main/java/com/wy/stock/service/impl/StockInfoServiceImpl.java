/**
 * 
 */
package com.wy.stock.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wy.stock.dao.StockInfoDao;
import com.wy.stock.domain.StockInfo;
import com.wy.stock.service.StockInfoService;

/**
 * @author leslie
 *
 */
public class StockInfoServiceImpl implements StockInfoService {

	private StockInfoDao stockInfoDao;
	
	public void insertStockInfo(StockInfo info) {
		stockInfoDao.insertStockInfo(info);
	}

	public void insertStockInfoBatch(List<StockInfo> infoList) {
		stockInfoDao.insertStockInfoBatch(infoList);
	}
	
	public void deleteByExchangeType(String exchange, String type) {
		StockInfo query = new StockInfo();
		query.setExchange(exchange);
		query.setType(type);
		stockInfoDao.deleteByExchangeType(query);
	}

	public void deleteByExchange(String exchange) {
		stockInfoDao.deleteByExchange(exchange);
	}
	
	public List<StockInfo> queryAllStockInfo() {
		return stockInfoDao.queryAllStockInfo();
	}
	
	public List<StockInfo> queryStockInfoByExchange(String exchange) {
		return stockInfoDao.queryStockInfoByExchange(exchange);
	}
	
	public List<StockInfo> queryStockInfoByType(String type) {
		return stockInfoDao.queryStockInfoByType(type);
	}
	
	public Map<String, String> queryStockCodeNameMap() {
		List<StockInfo> allInfo = stockInfoDao.queryAllStockInfo();
		if(allInfo == null || allInfo.isEmpty()){
			return null;
		}
		Map<String, String> result = new HashMap<String, String>();
		for(StockInfo info : allInfo){
			result.put(info.getCode(), info.getName());
		}
		return result;
	}
	
	public Map<String, String> queryStockNameCodeMap() {
		List<StockInfo> allInfo = stockInfoDao.queryAllStockInfo();
		if(allInfo == null || allInfo.isEmpty()){
			return null;
		}
		Map<String, String> result = new HashMap<String, String>();
		for(StockInfo info : allInfo){
			result.put(info.getName(), info.getCode());
		}
		return result;
	}

	public StockInfoDao getStockInfoDao() {
		return stockInfoDao;
	}

	public void setStockInfoDao(StockInfoDao stockInfoDao) {
		this.stockInfoDao = stockInfoDao;
	}

}
