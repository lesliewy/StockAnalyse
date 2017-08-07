/**
 * 
 */
package com.wy.stock.dao;

import java.util.List;

import com.wy.stock.domain.StocksInfo;

/**
 * @author leslie
 *
 */
public interface StocksInfoDao {
	
	void insertStocksInfo(StocksInfo stocksInfo);
	
	void insertStocksInfoBatch(List<StocksInfo> infoList);
	
	List<StocksInfo> queryStocksInfoByDate(String tradeDate);
	
	void deleteStocksInfoByDate(String tradeDate);
	
}
