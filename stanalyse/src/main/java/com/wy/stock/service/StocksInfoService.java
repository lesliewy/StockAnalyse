/**
 * 
 */
package com.wy.stock.service;

import java.util.List;
import java.util.Set;

import com.wy.stock.domain.StocksInfo;

/**
 * @author leslie
 *
 */
public interface StocksInfoService {

	void insertStocksInfo(StocksInfo stocksInfo);
	
	void insertStocksInfoBatch(List<StocksInfo> infoList);
	
	List<StocksInfo> queryStocksInfoByDate(String tradeDate);
	
	Set<String> queryStocksInfoCodeByDate(String tradeDate);
	
	void deleteStocksInfoByDate(String tradeDate);
}
