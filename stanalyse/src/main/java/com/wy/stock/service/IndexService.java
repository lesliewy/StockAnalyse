/**
 * 
 */
package com.wy.stock.service;

import java.util.List;
import java.util.Map;

import com.wy.stock.domain.Index;

/**
 * @author leslie
 *
 */
public interface IndexService {
	
	void insertIndex(Index index);
	
	void insertIndexBatch(List<Index> indexList);
	
	void deleteIndexByDate(String date);
	
	List<Index> queryIndexByDate(String date);
	
	List<Index> queryAllIndex();
	
	Map<String, Float> queryAllChangePctAndVolumnAmnt();
	
	List<Index> queryTotalChangePctBetween(String lowTradeDateStr,
			String highTradeDateStr);
	
	Map<String, Float> queryTotalChangePctBetweenMap(String lowTradeDateStr,
			String highTradeDateStr);
	
}
