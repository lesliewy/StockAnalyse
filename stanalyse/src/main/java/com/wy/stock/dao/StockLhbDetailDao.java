/**
 * 
 */
package com.wy.stock.dao;

import java.util.List;

import com.wy.stock.domain.StockLhbDetail;

/**
 * @author leslie
 *
 */
public interface StockLhbDetailDao {

	void insertStockLhbDetail(StockLhbDetail stockLhbDetail);

	void insertBatch(List<StockLhbDetail> stockLhbDetailList);

	void deleteByDate(String tradeDate);

	List<StockLhbDetail> query(StockLhbDetail stockLhbDetail);
}
