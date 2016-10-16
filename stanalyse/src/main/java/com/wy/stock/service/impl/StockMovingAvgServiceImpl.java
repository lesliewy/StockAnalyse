/**
 * 
 */
package com.wy.stock.service.impl;

import java.util.List;

import com.wy.stock.dao.StockMovingAvgDao;
import com.wy.stock.domain.StockMovingAvg;
import com.wy.stock.service.StockMovingAvgService;

/**
 * @author leslie
 *
 */
public class StockMovingAvgServiceImpl implements StockMovingAvgService {

	private StockMovingAvgDao maDao;

	public void insertStockMovingAvg(StockMovingAvg ma) {
		maDao.insertStockMovingAvg(ma);
	}

	public void insertStockMovingAvgBatch(List<StockMovingAvg> maList) {
		maDao.insertStockMovingAvgBatch(maList);
	}
	
	public void deleteByCodePeriod(String code, String exchange, String type,
			int period) {
		StockMovingAvg deleted = new StockMovingAvg();
		deleted.setCode(code);
		deleted.setExchange(exchange);
		deleted.setType(type);
		deleted.setPeriod(period);
		maDao.deleteByCodePeriod(deleted);
	}
	
	public List<StockMovingAvg> queryAllDistinct() {
		return maDao.queryAllDistinct();
	}
	
	public List<StockMovingAvg> queryAllByCode(String code, String exchange,
			String type) {
		StockMovingAvg ma = new StockMovingAvg();
		ma.setCode(code);
		ma.setExchange(exchange);
		ma.setType(type);
		return maDao.queryAllByCode(ma);
	}


	public StockMovingAvgDao getMaDao() {
		return maDao;
	}

	public void setMaDao(StockMovingAvgDao maDao) {
		this.maDao = maDao;
	}

}
