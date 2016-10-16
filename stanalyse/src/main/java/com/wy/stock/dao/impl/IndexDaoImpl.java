/**
 * 
 */
package com.wy.stock.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.wy.stock.dao.IndexDao;
import com.wy.stock.domain.Index;

/**
 * @author leslie
 *
 */
public class IndexDaoImpl extends SqlMapClientDaoSupport implements IndexDao {

	private static Logger LOGGER = Logger.getLogger(IndexDaoImpl.class
			.getName());

	public void insertIndex(Index index) {
		if(index == null){
			LOGGER.info("index is null, return now...");
			return;
		}
		try{
			getSqlMapClientTemplate().insert("insertIndex", index);
		}catch(Exception e){
			LOGGER.info("insertIndex: " + e);
		}
	}

	public void insertIndexBatch(List<Index> indexList) {
		if(indexList == null){
			LOGGER.info("indexList is null, return now...");
			return;
		}
		for(Index index : indexList){
			insertIndex(index);
		}
	}

	public void deleteIndexByDate(String date) {
		if(StringUtils.isBlank(date)){
			LOGGER.info("date is null, return now...");
			return;
		}
		getSqlMapClientTemplate().delete("deleteIndexByDate", date);
	}

	@SuppressWarnings("unchecked")
	public List<Index> queryIndexByDate(String date) {
		if(StringUtils.isBlank(date)){
			LOGGER.info("date is null, return null now...");
			return null;
		}
		return getSqlMapClientTemplate().queryForList("queryIndexByDate", date);
	}

	@SuppressWarnings("unchecked")
	public List<Index> queryAllIndex() {
		return getSqlMapClientTemplate().queryForList("queryAllIndex");
	}

	@SuppressWarnings("unchecked")
	public List<Index> queryTotalChangePctBetween(String lowTradeDateStr,
			String highTradeDateStr) {
		Index query = new Index();
		query.setLowTradeDateStr(lowTradeDateStr);
		query.setHighTradeDateStr(highTradeDateStr);
		return getSqlMapClientTemplate().queryForList("queryTotalChangePctBetween", query);
	}

}
