/**
 * 
 */
package com.wy.stock.service.impl;

import java.util.List;

import com.wy.stock.dao.StockFiveChangeDao;
import com.wy.stock.domain.StockFiveChange;
import com.wy.stock.service.StockFiveChangeService;

/**
 * @author leslie
 *
 */
public class StockFiveChangeServiceImpl implements StockFiveChangeService {

	private StockFiveChangeDao stockFiveChangeDao;
	
	public void insertStockFiveChange(StockFiveChange stockFiveChange) {
		stockFiveChangeDao.insertStockFiveChange(stockFiveChange);
	}

	public void insertStockFiveChangeBatch(List<StockFiveChange> stockFiveChangeList) {
		stockFiveChangeDao.insertStockFiveChangeBatch(stockFiveChangeList);
	}

	public void deleteStockFiveChange() {
		stockFiveChangeDao.deleteStockFiveChange();
	}

	public List<StockFiveChange> queryStockFiveChange() {
		return stockFiveChangeDao.queryStockFiveChange();
	}

	public StockFiveChangeDao getStockFiveChangeDao() {
		return stockFiveChangeDao;
	}

	public void setStockFiveChangeDao(StockFiveChangeDao stockFiveChangeDao) {
		this.stockFiveChangeDao = stockFiveChangeDao;
	}

}
