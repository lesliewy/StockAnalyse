/**
 * 
 */
package com.wy.stock.dao.impl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.wy.stock.dao.StockCapFlowDao;
import com.wy.stock.domain.AnalyseCondition;
import com.wy.stock.domain.StockCapFlow;

/**
 * @author leslie
 *
 */
public class StockCapFlowDaoImpl extends SqlMapClientDaoSupport implements StockCapFlowDao {

	private static Logger LOGGER = Logger.getLogger(StockCapFlowDaoImpl.class
			.getName());
	
	public void insertStockCapFlow(StockCapFlow stockCapFlow) {
		if(stockCapFlow == null){
			LOGGER.info("stockCapFlow is null, return now...");
			return;
		}
		try{
			getSqlMapClientTemplate().insert("insertStockCapFlow", stockCapFlow);
		}catch(Exception e){
			LOGGER.info("insertStockCapFlow: " + e);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void insertStockCapFlowBatch(final List<StockCapFlow> stockCapFlowList) {
		if(stockCapFlowList == null){
			LOGGER.info("stockCapFlowList is null, return now...");
			return;
		}
		
		// 批量方式
		try{
			this.getSqlMapClientTemplate().execute(new SqlMapClientCallback() {
				public Object doInSqlMapClient(SqlMapExecutor executor)
						throws SQLException {
					executor.startBatch();
					// 每次提交最大条数
					final int batchSize = 2000;
					int count = 0;
					for (StockCapFlow stockCapFlow : stockCapFlowList) {
						executor.insert("insertStockCapFlow", stockCapFlow);
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
			LOGGER.error("insertStockCapFlowBatch: " + e);
		}
	}

	public void insertStockCapFlowAfterDelete(StockCapFlow stockCapFlow) {
		if(stockCapFlow == null){
			LOGGER.info("stockCapFlow is null, return now...");
			return;
		}
		// 先删除
		deleteCapFlowByKey(stockCapFlow);
		insertStockCapFlow(stockCapFlow);
	}
	
	public void deleteCapFlowByKey(StockCapFlow stockCapFlow){
		if(stockCapFlow == null){
			LOGGER.info("stockCapFlow is null, return now...");
			return;
		}
		getSqlMapClientTemplate().delete("deleteCapFlowByKey", stockCapFlow);
	}

	public void deleteCapFlowByDate(Timestamp tradeDate) {
		if(tradeDate == null){
			LOGGER.info("tradeDate is null, return now...");
			return;
		}
		getSqlMapClientTemplate().delete("deleteCapFlowByDate", tradeDate);
	}

	@SuppressWarnings("unchecked")
	public List<StockCapFlow> queryCapFlowByCode(String code) {
		return getSqlMapClientTemplate().queryForList("queryCapFlowByCode", code);
	}

	@SuppressWarnings("unchecked")
	public List<StockCapFlow> queryCapFlowByDate(Timestamp tradeDate) {
		return getSqlMapClientTemplate().queryForList("queryCapFlowByDate", tradeDate);
	}

	@SuppressWarnings("unchecked")
	public List<StockCapFlow> analyseFall(AnalyseCondition condition) {
		return getSqlMapClientTemplate().queryForList("analyseFall", condition);
	}

	@SuppressWarnings("unchecked")
	public List<StockCapFlow> queryCapFlowBefore(Timestamp tradeDate, String code) {
		StockCapFlow capFLow = new StockCapFlow();
		capFLow.setTradeDate(tradeDate);
		capFLow.setCode(code);
		return getSqlMapClientTemplate().queryForList("queryCapFlowBefore", capFLow);
	}

	@SuppressWarnings("unchecked")
	public List<StockCapFlow> queryAllAccumuChangePct(Timestamp tradeDateLower,
			Timestamp tradeDateUpper) {
		StockCapFlow capFlow = new StockCapFlow();
		capFlow.setTradeDateLower(tradeDateLower);
		capFlow.setTradeDateUpper(tradeDateUpper);
		return getSqlMapClientTemplate().queryForList("queryAllAccumuChangePct", capFlow);
	}

	@SuppressWarnings("unchecked")
	public List<StockCapFlow> queryCapFlowByDateRange(Timestamp tradeDateLower,
			Timestamp tradeDateUpper, String code) {
		StockCapFlow capFlow = new StockCapFlow();
		capFlow.setTradeDateLower(tradeDateLower);
		capFlow.setTradeDateUpper(tradeDateUpper);
		capFlow.setCode(code);
		return getSqlMapClientTemplate().queryForList("queryCapFlowByDateRange", capFlow);
	}

}
