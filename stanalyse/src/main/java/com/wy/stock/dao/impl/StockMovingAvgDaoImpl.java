/**
 * 
 */
package com.wy.stock.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.wy.stock.dao.StockMovingAvgDao;
import com.wy.stock.domain.StockMovingAvg;

/**
 * @author leslie
 *
 */
public class StockMovingAvgDaoImpl extends SqlMapClientDaoSupport implements StockMovingAvgDao {

	private static Logger LOGGER = Logger.getLogger(StockMovingAvgDaoImpl.class
			.getName());
	
	public void insertStockMovingAvg(StockMovingAvg ma) {
		if(ma == null){
			LOGGER.info("ma is null, return now...");
			return;
		}
		try{
			getSqlMapClientTemplate().insert("insertStockMovingAvg", ma);
		}catch(Exception e){
			LOGGER.info("insertStockHistory: " + e);
		}
	}

	public void insertStockMovingAvgBatch(List<StockMovingAvg> maList) {
		if(maList == null){
			LOGGER.info("stockHistoryList is null, return now...");
			return;
		}
		for(StockMovingAvg ma : maList){
			insertStockMovingAvg(ma);
		}
	}

	public void deleteByCodePeriod(StockMovingAvg ma) {
		if(ma == null){
			LOGGER.info("ma is null, return now...");
			return;
		}
		getSqlMapClientTemplate().delete("deleteByCodePeriod", ma);
	}

	@SuppressWarnings("unchecked")
	public List<StockMovingAvg> queryAllDistinct() {
		return getSqlMapClientTemplate().queryForList("queryAllDistinct");
	}

	@SuppressWarnings("unchecked")
	public List<StockMovingAvg> queryAllByCode(StockMovingAvg ma) {
		if(ma == null){
			LOGGER.info("ma is null, return now...");
			return null;
		}
		return getSqlMapClientTemplate().queryForList("queryAllByCode", ma);
	}

}
