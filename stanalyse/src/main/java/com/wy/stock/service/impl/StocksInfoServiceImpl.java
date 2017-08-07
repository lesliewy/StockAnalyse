/**
 * 
 */
package com.wy.stock.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.wy.stock.dao.StocksInfoDao;
import com.wy.stock.domain.StocksInfo;
import com.wy.stock.service.StocksInfoService;

/**
 * @author leslie
 *
 */
public class StocksInfoServiceImpl implements StocksInfoService {

	private StocksInfoDao stocksInfoDao;
	
	public void insertStocksInfo(StocksInfo stocksInfo) {
		stocksInfoDao.insertStocksInfo(stocksInfo);
	}

	public void insertStocksInfoBatch(List<StocksInfo> infoList) {
		stocksInfoDao.insertStocksInfoBatch(infoList);
	}

	public List<StocksInfo> queryStocksInfoByDate(String tradeDate) {
		return stocksInfoDao.queryStocksInfoByDate(tradeDate);
	}
	
	public Set<String> queryStocksInfoCodeByDate(String tradeDate) {
		List<StocksInfo> stocksInfoList = stocksInfoDao.queryStocksInfoByDate(tradeDate);
		Set<String> stocksInfoSet = new HashSet<String>();
		if(stocksInfoList != null){
			for(StocksInfo stocksInfo : stocksInfoList){
				stocksInfoSet.add(stocksInfo.getCode());
			}
		}
		
		return stocksInfoSet;
	}
	
	public void deleteStocksInfoByDate(String tradeDate) {
		stocksInfoDao.deleteStocksInfoByDate(tradeDate);
	}

	public StocksInfoDao getStocksInfoDao() {
		return stocksInfoDao;
	}

	public void setStocksInfoDao(StocksInfoDao stocksInfoDao) {
		this.stocksInfoDao = stocksInfoDao;
	}

}
