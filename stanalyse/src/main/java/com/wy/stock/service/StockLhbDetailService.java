/**
 * 
 */
package com.wy.stock.service;

import java.util.List;

import com.wy.stock.domain.StockLhbDetail;

/**
 * @author leslie
 *
 */
public interface StockLhbDetailService {

	void insertStockLhbDetail(StockLhbDetail stockLhbDetail);

	void insertBatch(List<StockLhbDetail> stockLhbDetailList);

	void deleteByDate(String tradeDate);

	List<StockLhbDetail> query(StockLhbDetail stockLhbDetail);
}
