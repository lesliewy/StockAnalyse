/**
 * 
 */
package com.wy.stock.service;

import java.util.List;
import java.util.Map;

import com.wy.stock.domain.StockInfo;

/**
 * @author leslie
 *
 */
public interface StockInfoService {

	void insertStockInfo(StockInfo info);
	
	void insertStockInfoBatch(List<StockInfo> infoList);
	
	void deleteByExchangeType(String exchange, String type);
	
	void deleteByExchange(String exchange);
	
	List<StockInfo> queryAllStockInfo();
	
	List<StockInfo> queryStockInfoByExchange(String exchange);
	
	List<StockInfo> queryStockInfoByType(String type);
	
	Map<String, String> queryStockCodeNameMap();
	
	Map<String, String> queryStockNameCodeMap();
}
