/**
 * 
 */
package com.wy.stock.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wy.stock.dao.IndustryHotStocksDao;
import com.wy.stock.domain.IndustryHotStocks;
import com.wy.stock.service.IndustryHotStocksService;

/**
 * @author leslie
 *
 */
public class IndustryHotStocksServiceImpl implements IndustryHotStocksService {

	private IndustryHotStocksDao industryHotStocksDao;
	
	public void insertIndustryHotStocks(IndustryHotStocks industryHotStocks) {
		industryHotStocksDao.insertIndustryHotStocks(industryHotStocks);
	}

	public void insertIndustryHotStocksBatch(List<IndustryHotStocks> industryHotStocksList) {
		industryHotStocksDao.insertIndustryHotStocksBatch(industryHotStocksList);
	}

	public void deleteIndustryHotStocksByIndustryName(String industryName) {
		industryHotStocksDao.deleteIndustryHotStocksByIndustryName(industryName);
	}

	public List<IndustryHotStocks> queryIndustryHotStocksByIndustryName(String industryName) {
		return industryHotStocksDao.queryIndustryHotStocksByIndustryName(industryName);
	}
	
	public List<IndustryHotStocks> queryIndustryHotStocksForCsv(int year) {
		return industryHotStocksDao.queryIndustryHotStocksForCsv(year);
	}
	
	public List<IndustryHotStocks> queryIndustryHotStocksByDateStrBetween(
			String lowTradeDateStr, String highTradeDateStr) {
		return industryHotStocksDao.queryIndustryHotStocksByDateStrBetween(lowTradeDateStr, highTradeDateStr);
	}
	
	public List<IndustryHotStocks> queryStocksIndustriesBetween(
			String lowTradeDateStr, String highTradeDateStr) {
		return industryHotStocksDao.queryStocksIndustriesBetween(lowTradeDateStr, highTradeDateStr);
	}
	
	public Map<String, String> queryStocksIndustriesBetweenMap(
			String lowTradeDateStr, String highTradeDateStr) {
		List<IndustryHotStocks> list = queryStocksIndustriesBetween(lowTradeDateStr, highTradeDateStr);
		if(list == null || list.isEmpty()){
			return null;
		}
		Map<String, String> result = new HashMap<String, String>();
		for(IndustryHotStocks industryHotStocks : list){
			result.put(industryHotStocks.getStockName(), industryHotStocks.getIndustryNames());
		}
		return result;
	}

	public IndustryHotStocksDao getIndustryHotStocksDao() {
		return industryHotStocksDao;
	}

	public void setIndustryHotStocksDao(IndustryHotStocksDao industryHotStocksDao) {
		this.industryHotStocksDao = industryHotStocksDao;
	}

}
