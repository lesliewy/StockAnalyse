/**
 * 
 */
package com.wy.stock.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.wy.stock.dao.StockNotionDao;
import com.wy.stock.domain.StockNotion;

/**
 * @author leslie
 *
 */
public class StockNotionDaoImpl extends SqlMapClientDaoSupport implements StockNotionDao {

	private static Logger LOGGER = Logger.getLogger(StockNotionDaoImpl.class
			.getName());

	public void insertStockNotion(StockNotion stockNotion) {
		if(stockNotion == null){
			LOGGER.info("stockNotion is null, return now...");
			return;
		}
		try{
			getSqlMapClientTemplate().insert("insertStockNotion", stockNotion);
		}catch(Exception e){
			LOGGER.info("insertStockNotion: " + e);
		}		
	}

	public void insertStockNotionBatch(List<StockNotion> stockNotionList) {
		if(stockNotionList == null){
			LOGGER.info("stockNotionList is null, return now...");
			return;
		}
		for(StockNotion stockNotion : stockNotionList){
			insertStockNotion(stockNotion);
		}
	}

	public void deleteAllStockNotion() {
		getSqlMapClientTemplate().delete("deleteAllStockNotion");
	}
	
	public void deleteStockNotionByCode(String code) {
		if(StringUtils.isBlank(code)){
			LOGGER.info("code is null, return now...");
			return;
		}
		getSqlMapClientTemplate().delete("deleteStockNotionByCode", code);
	}

	@SuppressWarnings("unchecked")
	public List<StockNotion> queryAllStockNotion() {
		return getSqlMapClientTemplate().queryForList("queryAllStockNotion");
	}
	
	public StockNotion queryStockNotionByCode(String code) {
		if(StringUtils.isBlank(code)){
			LOGGER.info("code is null, return now...");
			return null;
		}
		return (StockNotion) getSqlMapClientTemplate().queryForObject("queryStockNotionByCode", code);
	}

}
