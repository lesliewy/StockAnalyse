/**
 * 
 */
package com.wy.stock.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.wy.stock.dao.StockCapFlowDao;
import com.wy.stock.domain.AnalyseCondition;
import com.wy.stock.domain.StockCapFlow;
import com.wy.stock.service.StockCapFlowService;

/**
 * @author leslie
 *
 */
public class StockCapFlowServiceImpl implements StockCapFlowService {

	private StockCapFlowDao stockCapFlowDao;

	public void insertStockCapFlow(StockCapFlow stockCapFlow) {
		stockCapFlowDao.insertStockCapFlow(stockCapFlow);
	}

	public void insertStockCapFlowBatch(List<StockCapFlow> stockCapFlowList) {
		stockCapFlowDao.insertStockCapFlowBatch(stockCapFlowList);
	}

	public void insertStockCapFlowAfterDelete(StockCapFlow stockCapFlow) {
		stockCapFlowDao.insertStockCapFlowAfterDelete(stockCapFlow);
	}

	public void deleteCapFlowByKey(StockCapFlow stockCapFlow) {
		stockCapFlowDao.deleteCapFlowByKey(stockCapFlow);
	}
	
	public void deleteCapFlowByDate(Timestamp tradeDate) {
		stockCapFlowDao.deleteCapFlowByDate(tradeDate);
	}
	
	public List<StockCapFlow> queryCapFlowByCode(String code) {
		return stockCapFlowDao.queryCapFlowByCode(code);
	}
	
	public List<Timestamp> queryCapFlowDateListByCode(String code) {
		List<StockCapFlow> list = queryCapFlowByCode(code);
		if(list == null || list.isEmpty()){
			return null;
		}
		List<Timestamp> result = new ArrayList<Timestamp>();
		for(StockCapFlow capFlow : list){
			result.add(capFlow.getTradeDate());
		}
		return result;
	}
	
	public List<StockCapFlow> queryCapFlowByDate(Timestamp tradeDate) {
		return stockCapFlowDao.queryCapFlowByDate(tradeDate);
	}
	
	public List<StockCapFlow> analyseFall(AnalyseCondition condition) {
		return stockCapFlowDao.analyseFall(condition);
	}
	
	public List<StockCapFlow> queryCapFlowBefore(Timestamp tradeDate,
			String code) {
		return stockCapFlowDao.queryCapFlowBefore(tradeDate, code);
	}
	
	public List<Timestamp> queryTradeDateBefore(Timestamp tradeDate, String code) {
		List<StockCapFlow> capFlowList = queryCapFlowBefore(tradeDate, code);
		if(capFlowList == null){
			return null;
		}
		List<Timestamp> result = new ArrayList<Timestamp>();
		for(StockCapFlow capFlow : capFlowList){
			result.add(capFlow.getTradeDate());
		}
		return result;
	}
	
	public List<StockCapFlow> queryAllAccumuChangePct(Timestamp tradeDateLower,
			Timestamp tradeDateUpper) {
		return stockCapFlowDao.queryAllAccumuChangePct(tradeDateLower, tradeDateUpper);
	}
	
	public List<StockCapFlow> queryCapFlowByDateRange(Timestamp tradeDateLower,
			Timestamp tradeDateUpper, String code) {
		return stockCapFlowDao.queryCapFlowByDateRange(tradeDateLower, tradeDateUpper, code);
	}
	
	public StockCapFlowDao getStockCapFlowDao() {
		return stockCapFlowDao;
	}

	public void setStockCapFlowDao(StockCapFlowDao stockCapFlowDao) {
		this.stockCapFlowDao = stockCapFlowDao;
	}

}
