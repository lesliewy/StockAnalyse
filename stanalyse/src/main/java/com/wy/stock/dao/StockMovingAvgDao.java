/**
 * 
 */
package com.wy.stock.dao;

import java.util.List;

import com.wy.stock.domain.StockMovingAvg;

/**
 * @author leslie
 *
 */
public interface StockMovingAvgDao {
	
	void insertStockMovingAvg(StockMovingAvg ma);
	
	void insertStockMovingAvgBatch(List<StockMovingAvg> maList);
	
	void deleteByCodePeriod(StockMovingAvg ma);
	
	List<StockMovingAvg> queryAllDistinct();
	
	List<StockMovingAvg> queryAllByCode(StockMovingAvg ma);
	
}
