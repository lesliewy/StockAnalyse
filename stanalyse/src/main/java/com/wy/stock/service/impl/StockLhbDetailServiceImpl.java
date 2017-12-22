/**
 * Project Name:stanalyse  
 * File Name:StockLhbDetailServiceImpl.java  
 * Package Name:com.wy.stock.service.impl  
 * Date:Dec 22, 2017 9:53:56 PM  
 * Copyright (c) 2017, wy All Rights Reserved.  
 *  
 */
package com.wy.stock.service.impl;

import com.wy.stock.dao.StockLhbDetailDao;
import com.wy.stock.domain.StockLhbDetail;
import com.wy.stock.service.StockLhbDetailService;

/**
 * date: Dec 22, 2017 9:53:56 PM <br/>  
 *  
 * @author leslie  
 * @version   
 * @since version 1.0  
 */
public class StockLhbDetailServiceImpl implements StockLhbDetailService {

	private StockLhbDetailDao stockLhbDetailDao;

	public void insertStockLhbDetail(StockLhbDetail stockLhbDetail) {
		stockLhbDetailDao.insertStockLhbDetail(stockLhbDetail);
	}

	public StockLhbDetailDao getStockLhbDetailDao() {
		return stockLhbDetailDao;
	}

	public void setStockLhbDetailDao(StockLhbDetailDao stockLhbDetailDao) {
		this.stockLhbDetailDao = stockLhbDetailDao;
	}

}
