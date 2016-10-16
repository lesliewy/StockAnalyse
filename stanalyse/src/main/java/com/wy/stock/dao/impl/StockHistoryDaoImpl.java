/**
 * 
 */
package com.wy.stock.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.wy.stock.dao.StockHistoryDao;
import com.wy.stock.domain.StockHistory;
import com.wy.stock.domain.StockInfo;

/**
 * @author leslie
 *
 */
public class StockHistoryDaoImpl extends SqlMapClientDaoSupport implements StockHistoryDao {

	private static Logger LOGGER = Logger.getLogger(StockHistoryDaoImpl.class
			.getName());
	
	public void insertStockHistory(StockHistory stockHistory) {
		if(stockHistory == null){
			LOGGER.info("stockHistory is null, return now...");
			return;
		}
		try{
			getSqlMapClientTemplate().insert("insertStockHistory", stockHistory);
		}catch(Exception e){
			LOGGER.info("insertStockHistory: " + e);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void insertStockHistoryBatch(final List<StockHistory> stockHistoryList) {
		if(stockHistoryList == null){
			LOGGER.info("stockHistoryList is null, return now...");
			return;
		}
		/*
		for(StockHistory stockHistory : stockHistoryList){
			insertStockHistoryAfterDelete(stockHistory);
		}
		*/
		
		// 批量方式
		try{
			this.getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
				public Object doInSqlMapClient(SqlMapExecutor executor)
						throws SQLException {
					executor.startBatch();
					// 每次提交最大条数
					final int batchSize = 2000;
					int count = 0;
					for (StockHistory stockHistory : stockHistoryList) {
						executor.insert("insertStockHistory", stockHistory);
						// 每1000条数据提交一次
						if (++count % batchSize == 0) {
							executor.executeBatch();
						}
					}
					// 提交剩余的数据
					executor.executeBatch();
					return null;
				}
			});
		}catch (Exception e){
			LOGGER.error("insertStockHistoryBatch: " + e);
		}
	}

	public void insertStockHistoryAfterDelete(StockHistory stockHistory) {
		if(stockHistory == null){
			LOGGER.info("stockHistory is null, return now...");
			return;
		}
		// 先删除
		deleteHistoryByKey(stockHistory);
		insertStockHistory(stockHistory);
	}
	
	public void deleteHistoryByKey(StockHistory stockHistory){
		if(stockHistory == null){
			LOGGER.info("stockHistory is null, return now...");
			return;
		}
		getSqlMapClientTemplate().delete("deleteHistoryByKey", stockHistory);
	}

	@SuppressWarnings("unchecked")
	public List<StockInfo> queryAllHistStock() {
		return getSqlMapClientTemplate().queryForList("queryAllHistStock");
	}

	@SuppressWarnings("unchecked")
	public List<StockHistory> queryHistListByKey(StockHistory query) {
		if(query == null){
			LOGGER.info("query is null, return null now...");
			return null;
		}
		return getSqlMapClientTemplate().queryForList("queryHistListByKey", query);
	}

	@SuppressWarnings("unchecked")
	public List<StockHistory> queryHistListByKeyRange(StockHistory query) {
		if(query == null){
			LOGGER.info("query is null, return null now...");
			return null;
		}
		return getSqlMapClientTemplate().queryForList("queryHistListByKeyRange", query);
	}

	@SuppressWarnings("unchecked")
	public List<StockHistory> queryAllHistStockMaxDate() {
		return getSqlMapClientTemplate().queryForList("queryAllHistStockMaxDate");
	}

}
