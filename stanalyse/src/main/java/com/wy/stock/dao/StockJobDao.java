/**
 * 
 */
package com.wy.stock.dao;

import java.util.List;

import com.wy.stock.domain.StockJob;

/**
 * @author leslie
 *
 */
public interface StockJobDao {
	
	void insertStockJob(StockJob job);
	
	List<StockJob> queryStockJobByDateStatus(StockJob query);
	
	List<StockJob> queryStockJobByDate(StockJob job);
	
	void updateR2D(StockJob job);
	
	void updateRunningJob(StockJob job);
	
	void deleteByDateType(StockJob job);
	
	StockJob queryLastStockJob(StockJob job);
}
