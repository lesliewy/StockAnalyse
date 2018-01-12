/**
 * 
 */
package com.wy.stock.service;

import java.util.List;

import com.wy.stock.domain.StockJob;

/**
 * @author leslie
 *
 */
public interface StockJobService {
	
	void insertStockJob(StockJob job);
	
	List<StockJob> queryStockJobByDateStatus(String date, String jobType, String status);
	
	List<StockJob> queryStockJobByDate(String date, String jobType);
	
	void cleanLongTimeJob(int delRTime, String jobFlag);
	
	void updateRunningJob(StockJob job);
	
	void deleteByDateType(String date, String jobType);
	
	void deleteByDateTypeNoStatus(String date, String jobType);
	
	StockJob queryLastStockJob(String jobType, String status);
}
