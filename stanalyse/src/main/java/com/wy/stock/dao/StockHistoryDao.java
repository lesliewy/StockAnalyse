/**
 * 
 */
package com.wy.stock.dao;

import java.util.List;

import com.wy.stock.domain.StockHistory;
import com.wy.stock.domain.StockInfo;

/**
 * @author leslie
 *
 */
public interface StockHistoryDao {
	
	void insertStockHistory(StockHistory stockHistory);
	
	void insertStockHistoryBatch(List<StockHistory> stockHistoryList);
	
	void insertStockHistoryAfterDelete(StockHistory stockHistory);
	
	void deleteHistoryByKey(StockHistory stockHistory);
	
	List<StockInfo> queryAllHistStock();
	
	List<StockHistory> queryHistListByKey(StockHistory query);
	
	List<StockHistory> queryHistListByKeyRange(StockHistory query);
	
	List<StockHistory> queryAllHistStockMaxDate();
	
}
