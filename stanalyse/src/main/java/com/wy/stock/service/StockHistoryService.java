/**
 * 
 */
package com.wy.stock.service;

import java.sql.Timestamp;
import java.util.List;

import com.wy.stock.domain.StockHistory;
import com.wy.stock.domain.StockInfo;

/**
 * @author leslie
 *
 */
public interface StockHistoryService {
	
	void insertStockHistory(StockHistory stockHistory);
	
	void insertStockHistoryBatch(List<StockHistory> stockHistoryList);
	
	void insertStockHistoryAfterDelete(StockHistory stockHistory);
	
	void deleteHistoryByKey(StockHistory stockHistory);
	
	List<StockInfo> queryAllHistStock();
	
	List<StockHistory> queryHistListByKey(String code, String exchange, String type);
	
	List<StockHistory> queryHistListByKeyRange(String code, String exchange, String type, Timestamp tradeDate);
	
	List<StockHistory> queryAllHistStockMaxDate();
}
