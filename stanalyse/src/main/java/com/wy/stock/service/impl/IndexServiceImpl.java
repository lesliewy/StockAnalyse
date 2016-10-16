/**
 * 
 */
package com.wy.stock.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wy.stock.dao.IndexDao;
import com.wy.stock.domain.Index;
import com.wy.stock.service.IndexService;

/**
 * @author leslie
 *
 */
public class IndexServiceImpl implements IndexService {

	private IndexDao indexDao;
	
	public void insertIndex(Index index) {
		indexDao.insertIndex(index);
	}

	public void insertIndexBatch(List<Index> indexList) {
		indexDao.insertIndexBatch(indexList);
	}

	public void deleteIndexByDate(String date) {
		indexDao.deleteIndexByDate(date);
	}

	public List<Index> queryIndexByDate(String date) {
		return indexDao.queryIndexByDate(date);
	}
	
	public List<Index> queryAllIndex() {
		return indexDao.queryAllIndex();
	}
	
	public Map<String, Float> queryAllChangePctAndVolumnAmnt() {
		List<Index> allIndexes = queryAllIndex();
		if(allIndexes == null || allIndexes.isEmpty()){
			return null;
		}
		
		// [key: 2016-06-17_上证指数_涨跌幅 value: 1.45]   [key: 2016-06-17_上证指数_成交金额  value: 1943.22]   
		Map<String, Float> result = new HashMap<String, Float>();
		for(Index index : allIndexes){
			String indexName = index.getIndexName();
			if(!"上证指数".equalsIgnoreCase(indexName) && !"深证成指".equalsIgnoreCase(indexName) && !"创业板指".equalsIgnoreCase(indexName)){
				continue;
			}
			Timestamp tradeDate = index.getTradeDate();
			String tradeDateStr = new SimpleDateFormat("YYYY-MM-dd").format(tradeDate);
			result.put(tradeDateStr + "_" + indexName + "_" + "涨跌幅", index.getChangePercent());
			result.put(tradeDateStr + "_" + indexName + "_" + "成交金额", index.getVolumnAmount());
		}
		return result;
	}
	
	public List<Index> queryTotalChangePctBetween(String lowTradeDateStr,
			String highTradeDateStr) {
		return indexDao.queryTotalChangePctBetween(lowTradeDateStr, highTradeDateStr);
	}

	public Map<String, Float> queryTotalChangePctBetweenMap(
			String lowTradeDateStr, String highTradeDateStr) {
		List<Index> list = indexDao.queryTotalChangePctBetween(lowTradeDateStr, highTradeDateStr);
		if(list == null){
			return null;
		}
		Map<String, Float> result = new HashMap<String, Float>();
		for(Index index : list){
			result.put(index.getIndexName(), index.getTotalChangePercent());
		}
		return result;
	}

	public IndexDao getIndexDao() {
		return indexDao;
	}

	public void setIndexDao(IndexDao indexDao) {
		this.indexDao = indexDao;
	}

}
