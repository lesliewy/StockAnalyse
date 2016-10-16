/**
 * 
 */
package com.wy.stock.dao;

import java.util.List;

import com.wy.stock.domain.StockInfo;

/**
 * @author leslie
 *
 */
public interface StockInfoDao {
	
	void insertStockInfo(StockInfo info);
	
	void insertStockInfoBatch(List<StockInfo> infoList);
	
	void insertStockInfoAfterDelete(StockInfo info);
	
	void deleteByExchangeType(StockInfo stockInfo);
	
	void deleteByExchange(String exchange);
	
	void deleteByKey(StockInfo stockInfo);
	
	List<StockInfo> queryAllStockInfo();
	
	List<StockInfo> queryStockInfoByExchange(String exchange);
	
	List<StockInfo> queryStockInfoByType(String type);
}
