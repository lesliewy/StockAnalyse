/**
 * 
 */
package com.wy.stock.dao;

import java.sql.Timestamp;
import java.util.List;

import com.wy.stock.domain.AnalyseCondition;
import com.wy.stock.domain.StockCapFlow;

/**
 * @author leslie
 *
 */
public interface StockCapFlowDao {
	
	void insertStockCapFlow(StockCapFlow stockCapFlow);
	
	void insertStockCapFlowBatch(List<StockCapFlow> stockCapFlowList);
	
	void insertStockCapFlowAfterDelete(StockCapFlow stockCapFlow);
	
	void deleteCapFlowByKey(StockCapFlow stockCapFlow);
	
	void deleteCapFlowByDate(Timestamp tradeDate);
	
	List<StockCapFlow> queryCapFlowByCode(String code);
	
	List<StockCapFlow> queryCapFlowByDate(Timestamp tradeDate);
	
	List<StockCapFlow> analyseFall(AnalyseCondition condition);
	
	List<StockCapFlow> queryCapFlowBefore(Timestamp tradeDate, String code);
	
	List<StockCapFlow> queryAllAccumuChangePct(Timestamp tradeDateLower, Timestamp tradeDateUpper);
	
	List<StockCapFlow> queryCapFlowByDateRange(Timestamp tradeDateLower, Timestamp tradeDateUpper, String code);
}
