/**
 * 
 */
package com.wy.stock.service;

import java.util.List;

import com.wy.stock.domain.StockFiveChange;

/**
 * @author leslie
 *
 */
public interface StockFiveChangeService {
	
	void insertStockFiveChange(StockFiveChange stockFiveChange);
	
	void insertStockFiveChangeBatch(List<StockFiveChange> stockFiveChangeList);
	
	void deleteStockFiveChange();
	
	List<StockFiveChange> queryStockFiveChange();
	
}
