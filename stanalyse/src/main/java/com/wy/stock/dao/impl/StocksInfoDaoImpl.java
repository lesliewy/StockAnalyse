/**
 * 
 */
package com.wy.stock.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.wy.stock.dao.StocksInfoDao;
import com.wy.stock.domain.StocksInfo;

/**
 * @author leslie
 *
 */
public class StocksInfoDaoImpl extends SqlMapClientDaoSupport implements StocksInfoDao {

	private static Logger LOGGER = Logger.getLogger(StocksInfoDaoImpl.class
			.getName());
	
	public void insertStocksInfo(StocksInfo info) {
		if(info == null){
			LOGGER.info("info is null, return now...");
			return;
		}
		try{
			getSqlMapClientTemplate().insert("insertStocksInfo", info);
		}catch(Exception e){
			LOGGER.info("insertStocksInfo: " + e);
		}
	}

	public void insertStocksInfoBatch(List<StocksInfo> infoList) {
		if(infoList == null){
			LOGGER.info("infoList is null, return now...");
			return;
		}
		for(StocksInfo info : infoList){
			insertStocksInfo(info);
		}
	}

	@SuppressWarnings("unchecked")
	public List<StocksInfo> queryStocksInfoByDate(String tradeDate) {
		return getSqlMapClientTemplate().queryForList("queryStocksInfoByDate", tradeDate);
	}

	public void deleteStocksInfoByDate(String tradeDate) {
		getSqlMapClientTemplate().delete("deleteStocksInfoByDate", tradeDate);
	}
}
