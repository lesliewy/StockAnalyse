/**
 * 
 */
package com.wy.stock.service.impl;

import java.sql.Timestamp;
import java.util.List;

import com.wy.stock.dao.StockHistoryDao;
import com.wy.stock.domain.StockHistory;
import com.wy.stock.domain.ExchangeInfo;
import com.wy.stock.service.StockHistoryService;

/**
 * @author leslie
 *
 */
public class StockHistoryServiceImpl implements StockHistoryService {

	private StockHistoryDao stockHistoryDao;

	public void insertStockHistory(StockHistory stockHistory) {
		stockHistoryDao.insertStockHistory(stockHistory);
	}

	public void insertStockHistoryBatch(List<StockHistory> stockHistoryList) {
		stockHistoryDao.insertStockHistoryBatch(stockHistoryList);
	}

	public void insertStockHistoryAfterDelete(StockHistory stockHistory) {
		stockHistoryDao.insertStockHistoryAfterDelete(stockHistory);
	}

	public void deleteHistoryByKey(StockHistory stockHistory) {
		stockHistoryDao.deleteHistoryByKey(stockHistory);
	}
	
	public List<ExchangeInfo> queryAllHistStock() {
		return stockHistoryDao.queryAllHistStock();
	}

	public List<StockHistory> queryHistListByKey(String code, String exchange, String type) {
		StockHistory query = new StockHistory();
		query.setCode(code);
		query.setExchange(exchange);
		query.setType(type);
		return stockHistoryDao.queryHistListByKey(query);
	}
	
	public List<StockHistory> queryHistListByKeyRange(String code,
			String exchange, String type, Timestamp tradeDate) {
		StockHistory query = new StockHistory();
		query.setCode(code);
		query.setExchange(exchange);
		query.setType(type);
		query.setTradeDate(tradeDate);
		return stockHistoryDao.queryHistListByKeyRange(query);
	}
	
	public List<StockHistory> queryAllHistStockMaxDate() {
		return stockHistoryDao.queryAllHistStockMaxDate();
	}
	
	public StockHistoryDao getStockHistoryDao() {
		return stockHistoryDao;
	}

	public void setStockHistoryDao(StockHistoryDao stockHistoryDao) {
		this.stockHistoryDao = stockHistoryDao;
	}

}
