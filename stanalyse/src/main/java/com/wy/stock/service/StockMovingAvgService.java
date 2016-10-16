/**
 * 
 */
package com.wy.stock.service;

import java.util.List;

import com.wy.stock.domain.StockMovingAvg;

/**
 * @author leslie
 *
 */
public interface StockMovingAvgService {
	
	void insertStockMovingAvg(StockMovingAvg ma);
	
	void insertStockMovingAvgBatch(List<StockMovingAvg> maList);
	
	void deleteByCodePeriod(String code, String exchange, String type, int period);
	
	List<StockMovingAvg> queryAllDistinct();
	
	List<StockMovingAvg> queryAllByCode(String code, String exchange, String type);
}
