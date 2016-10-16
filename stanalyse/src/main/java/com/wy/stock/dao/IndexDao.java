/**
 * 
 */
package com.wy.stock.dao;

import java.util.List;

import com.wy.stock.domain.Index;

/**
 * @author leslie
 *
 */
public interface IndexDao {
	
	void insertIndex(Index index);
	
	void insertIndexBatch(List<Index> indexList);
	
	void deleteIndexByDate(String date);
	
	List<Index> queryIndexByDate(String date);
	
	List<Index> queryAllIndex();
	
	List<Index> queryTotalChangePctBetween(String lowTradeDateStr, String highTradeDateStr);
}
