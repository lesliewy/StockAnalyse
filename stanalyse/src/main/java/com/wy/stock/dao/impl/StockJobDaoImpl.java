/**
 * 
 */
package com.wy.stock.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.wy.stock.dao.StockJobDao;
import com.wy.stock.domain.StockJob;

/**
 * @author leslie
 *
 */
public class StockJobDaoImpl extends SqlMapClientDaoSupport implements StockJobDao {

	private static Logger LOGGER = Logger.getLogger(StockJobDaoImpl.class
				.getName());
	
	public void insertStockJob(StockJob job) {
		if(job == null){
			LOGGER.info("job is null, return now...");
			return;
		}
		try{
			getSqlMapClientTemplate().insert("insertStockJob", job);
		}catch(Exception e){
			LOGGER.info("insertStockJob: " + e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<StockJob> queryStockJobByDateStatus(StockJob query) {
		if(query == null){
			LOGGER.info("query is null, return now...");
			return null;
		}
		return getSqlMapClientTemplate().queryForList("queryStockJobByDateStatus", query);
	}

	public void updateR2D(StockJob job) {
		if(job == null){
			LOGGER.info("job is null, return now...");
			return;
		}
		getSqlMapClientTemplate().update("updateR2D", job);
	}

	public void updateRunningJob(StockJob job) {
		if(job == null){
			LOGGER.info("job is null, return now...");
			return;
		}
		getSqlMapClientTemplate().update("updateRunningJob", job);
	}

	@SuppressWarnings("unchecked")
	public List<StockJob> queryStockJobByDate(StockJob job) {
		if(job == null){
			LOGGER.info("job is null, return now...");
			return null;
		}
		return getSqlMapClientTemplate().queryForList("queryStockJobByDate", job);
	}

	public void deleteByDateType(StockJob job) {
		if(StringUtils.isBlank(job.getDate()) || StringUtils.isBlank(job.getJobType())){
			LOGGER.info("date or jobType is null, return now...");
			return;
		}
		getSqlMapClientTemplate().delete("deleteByDateType", job);
	}
	
	public void deleteByDateTypeNoStatus(StockJob job) {
		if(StringUtils.isBlank(job.getDate()) || StringUtils.isBlank(job.getJobType())){
			LOGGER.info("date or jobType is null, return now...");
			return;
		}
		getSqlMapClientTemplate().delete("deleteByDateTypeNoStatus", job); 
	}

	public StockJob queryLastStockJob(StockJob job) {
		return (StockJob) getSqlMapClientTemplate().queryForObject("queryLastStockJob", job);
	}

}
