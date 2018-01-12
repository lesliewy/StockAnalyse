/**
 * 
 */
package com.wy.stock.service.impl;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import com.wy.stock.dao.StockJobDao;
import com.wy.stock.domain.StockJob;
import com.wy.stock.service.StockJobService;

/**
 * @author leslie
 *
 */
public class StockJobServiceImpl implements StockJobService {

	private StockJobDao stockJobDao;
	public void insertStockJob(StockJob job) {
		stockJobDao.insertStockJob(job);
	}

	public List<StockJob> queryStockJobByDateStatus(String date,
			String jobType, String status) {
		StockJob query = new StockJob();
		query.setDate(date);
		query.setJobType(jobType);
		query.setStatus(status);
		return stockJobDao.queryStockJobByDateStatus(query);
	}

	public void cleanLongTimeJob(int delRTime, String jobFlag) {
		StockJob job = new StockJob();
		job.setDelRUpperLimit(delRTime);
		job.setJobFlag(jobFlag);
		job.setRemark("kill long time job");
		job.setTimestamp(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		stockJobDao.updateR2D(job);
	}

	public void updateRunningJob(StockJob job) {
		stockJobDao.updateRunningJob(job);
	}

	public List<StockJob> queryStockJobByDate(String date, String jobType) {
		StockJob job = new StockJob();
		job.setJobDate(date);
		job.setJobType(jobType);
		return stockJobDao.queryStockJobByDate(job);
	}
	
	public void deleteByDateType(String date, String jobType) {
		StockJob job = new StockJob();
		job.setDate(date);
		job.setJobType(jobType);
		stockJobDao.deleteByDateType(job);
	}
	
	public void deleteByDateTypeNoStatus(String date, String jobType) {
		StockJob job = new StockJob();
		job.setDate(date);
		job.setJobType(jobType);
		stockJobDao.deleteByDateTypeNoStatus(job);
	}
	
	public StockJob queryLastStockJob(String jobType, String status) {
		StockJob job = new StockJob();
		job.setStatus(status);
		job.setJobType(jobType);
		return stockJobDao.queryLastStockJob(job);
	}
	
	public StockJobDao getStockJobDao() {
		return stockJobDao;
	}

	public void setStockJobDao(StockJobDao stockJobDao) {
		this.stockJobDao = stockJobDao;
	}

}
