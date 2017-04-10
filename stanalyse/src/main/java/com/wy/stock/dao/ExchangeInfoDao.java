/**
 * 
 */
package com.wy.stock.dao;

import java.util.List;

import com.wy.stock.domain.ExchangeInfo;

/**
 * @author leslie
 *
 */
public interface ExchangeInfoDao {
	
	void insertExchangeInfo(ExchangeInfo exchangeInfo);
	
	void insertExchangeInfoBatch(List<ExchangeInfo> infoList);
	
	void insertExchangeInfoAfterDelete(ExchangeInfo exchangeInfo);
	
	void deleteByExchangeType(ExchangeInfo exchangeInfo);
	
	void deleteByExchange(String exchange);
	
	void deleteByKey(ExchangeInfo exchangeInfo);
	
	List<ExchangeInfo> queryAllExchangeInfo();
	
	List<ExchangeInfo> queryExchangeInfoByExchange(String exchange);
	
	List<ExchangeInfo> queryExchangeInfoByType(String type);
}
