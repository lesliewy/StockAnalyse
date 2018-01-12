/**
 * Project Name:stanalyse  
 * File Name:StockLhbDetailDaoImpl.java  
 * Package Name:com.wy.stock.dao.impl  
 * Date:Dec 22, 2017 9:50:33 PM  
 * Copyright (c) 2017, wy All Rights Reserved.  
 *  
 */
package com.wy.stock.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.wy.stock.dao.StockLhbDetailDao;
import com.wy.stock.domain.StockLhbDetail;

/**
 * 
 * date: Dec 22, 2017 9:50:33 PM <br/>  
 *  
 * @author leslie  
 * @version   
 * @since version 1.0  
 */
public class StockLhbDetailDaoImpl extends SqlMapClientDaoSupport implements
		StockLhbDetailDao {
	private static Logger LOGGER = Logger.getLogger(StockLhbDetailDaoImpl.class
			.getName());

	public void insertStockLhbDetail(StockLhbDetail stockLhbDetail) {
		if (stockLhbDetail == null) {
			LOGGER.info("stockLhbDetail is null, return now...");
			return;
		}
		try {
			getSqlMapClientTemplate().insert("insertStockLhbDetail",
					stockLhbDetail);
		} catch (Exception e) {
			LOGGER.info("insertStockLhbDetail: " + e);
		}
	}

	public void insertBatch(List<StockLhbDetail> stockLhbDetailList) {
		if (stockLhbDetailList == null) {
			LOGGER.info("stockLhbDetailList is null, return now...");
			return;
		}
		for (StockLhbDetail stockLhbDetail : stockLhbDetailList) {
			insertStockLhbDetail(stockLhbDetail);
		}
	}

	public void deleteByDate(String tradeDate) {
		if (StringUtils.isBlank(tradeDate)) {
			LOGGER.info("tradeDate is null, return now...");
			return;
		}
		try {
			getSqlMapClientTemplate().delete("deleteByDate", tradeDate);
		} catch (Exception e) {
			LOGGER.info("deleteByDate: " + e);
		}

	}
	
	@SuppressWarnings("unchecked")
	public List<StockLhbDetail> query(StockLhbDetail stockLhbDetail) {
		return getSqlMapClientTemplate().queryForList("query", stockLhbDetail);
	}
}
