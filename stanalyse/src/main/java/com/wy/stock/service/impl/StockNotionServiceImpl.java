/**
 * 
 */
package com.wy.stock.service.impl;

import java.util.List;

import com.wy.stock.dao.StockNotionDao;
import com.wy.stock.domain.StockNotion;
import com.wy.stock.service.StockNotionService;

/**
 * @author leslie
 *
 */
public class StockNotionServiceImpl implements StockNotionService {

	private StockNotionDao stockNotionDao;

	public void insertStockNotion(StockNotion stockNotion) {
		stockNotionDao.insertStockNotion(stockNotion);
	}

	public void insertStockNotionBatch(List<StockNotion> stockNotionList) {
		stockNotionDao.insertStockNotionBatch(stockNotionList);
	}

	public void deleteAllStockNotion() {
		stockNotionDao.deleteAllStockNotion();
	}
	
	public void deleteStockNotionByCode(String code) {
		stockNotionDao.deleteStockNotionByCode(code);
	}

	public List<StockNotion> queryAllStockNotion() {
		return stockNotionDao.queryAllStockNotion();
	}

	public StockNotion queryStockNotionByCode(String code) {
		return stockNotionDao.queryStockNotionByCode(code);
	}

	public StockNotionDao getStockNotionDao() {
		return stockNotionDao;
	}

	public void setStockNotionDao(StockNotionDao stockNotionDao) {
		this.stockNotionDao = stockNotionDao;
	}

}
