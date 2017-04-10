/**
 * 
 */
package com.wy.stock.service;

import java.util.List;
import java.util.Map;

import com.wy.stock.domain.ExchangeInfo;

/**
 * @author leslie
 *
 */
public interface ExchangeInfoService {

	void insertExchangeInfo(ExchangeInfo info);
	
	void insertExchangeInfoBatch(List<ExchangeInfo> infoList);
	
	void deleteByExchangeType(String exchange, String type);
	
	void deleteByExchange(String exchange);
	
	List<ExchangeInfo> queryAllExchangeInfo();
	
	List<ExchangeInfo> queryExchangeInfoByExchange(String exchange);
	
	List<ExchangeInfo> queryExchangeInfoByType(String type);
	
	Map<String, String> queryStockCodeNameMap();
	
	Map<String, String> queryStockNameCodeMap();
}
