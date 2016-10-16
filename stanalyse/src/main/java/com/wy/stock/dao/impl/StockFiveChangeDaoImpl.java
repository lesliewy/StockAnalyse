/**
 * 
 */
package com.wy.stock.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.wy.stock.dao.StockFiveChangeDao;
import com.wy.stock.domain.StockFiveChange;

/**
 * @author leslie
 *
 */
public class StockFiveChangeDaoImpl extends SqlMapClientDaoSupport implements StockFiveChangeDao {

	private static Logger LOGGER = Logger.getLogger(StockFiveChangeDaoImpl.class
			.getName());

	public void insertStockFiveChange(StockFiveChange stockFiveChange) {
		if(stockFiveChange == null){
			LOGGER.info("stockFiveChange is null, return now...");
			return;
		}
		try{
			getSqlMapClientTemplate().insert("insertStockFiveChange", stockFiveChange);
		}catch(Exception e){
			LOGGER.info("insertStockFiveChange: " + e);
		}
	}

	public void insertStockFiveChangeBatch(List<StockFiveChange> stockFiveChangeList) {
		if(stockFiveChangeList == null){
			LOGGER.info("stockFiveChangeList is null, return now...");
			return;
		}
		for(StockFiveChange stockFiveChange : stockFiveChangeList){
			insertStockFiveChange(stockFiveChange);
		}
	}

	public void deleteStockFiveChange() {
		getSqlMapClientTemplate().delete("deleteStockFiveChange");
	}

	@SuppressWarnings("unchecked")
	public List<StockFiveChange> queryStockFiveChange() {
		return getSqlMapClientTemplate().queryForList("queryStockFiveChange");
	}

}
