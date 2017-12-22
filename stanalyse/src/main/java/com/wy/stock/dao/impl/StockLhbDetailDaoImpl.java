/**
 * Project Name:stanalyse  
 * File Name:StockLhbDetailDaoImpl.java  
 * Package Name:com.wy.stock.dao.impl  
 * Date:Dec 22, 2017 9:50:33 PM  
 * Copyright (c) 2017, wy All Rights Reserved.  
 *  
 */
package com.wy.stock.dao.impl;

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

}
