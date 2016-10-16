/**
 * 
 */
package com.wy.stock.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.wy.stock.dao.IndustryStockDao;
import com.wy.stock.domain.IndustryStock;


/**
 * @author leslie
 *
 */
public class IndustryStockDaoImpl extends SqlMapClientDaoSupport implements IndustryStockDao {

	private static Logger LOGGER = Logger.getLogger(IndustryStockDaoImpl.class
			.getName());

	public void insertIndustryStock(IndustryStock industryStock) {
		if(industryStock == null){
			LOGGER.info("notionStock is null, return now...");
			return;
		}
		try{
			getSqlMapClientTemplate().insert("insertIndustryStock", industryStock);
		}catch(Exception e){
			LOGGER.info("insertIndustryStock: " + e);
		}		
	}

	public void insertIndustryStockBatch(List<IndustryStock> industryStockList) {
		if(industryStockList == null){
			LOGGER.info("industryStockList is null, return now...");
			return;
		}
		for(IndustryStock industryStock : industryStockList){
			insertIndustryStock(industryStock);
		}
	}

	public void deleteAllIndustryStock(String source) {
		if(StringUtils.isBlank(source)){
			LOGGER.info("source is blank, return now...");
			return;
		}
		getSqlMapClientTemplate().delete("deleteAllIndustryStock", source);
	}
	
	public void deleteIndustryStockByIndustryCode(String industryCode) {
		if(StringUtils.isBlank(industryCode)){
			LOGGER.info("industryCode is null, return now...");
			return;
		}
		getSqlMapClientTemplate().delete("deleteIndustryStockByIndustryCode", industryCode);
	}

	@SuppressWarnings("unchecked")
	public List<IndustryStock> queryIndustryStockByIndustryCode(IndustryStock industryStock) {
		if(StringUtils.isBlank(industryStock.getIndustryCode()) || StringUtils.isBlank(industryStock.getSource())){
			LOGGER.info("industryCode or source is null, return now...");
			return null;
		}
		return getSqlMapClientTemplate().queryForList("queryIndustryStockByIndustryCode", industryStock);
	}
	
	@SuppressWarnings("unchecked")
	public List<IndustryStock> queryIndustryStockByCode(String code) {
		if(StringUtils.isBlank(code)){
			LOGGER.info("code is null, return now...");
			return null;
		}
		return getSqlMapClientTemplate().queryForList("queryIndustryStockByCode", code);
	}

	@SuppressWarnings("unchecked")
	public List<IndustryStock> queryAllIndustryStock(String source) {
		if(StringUtils.isBlank(source)){
			LOGGER.info("source is null, return now...");
			return null;
		}
		return getSqlMapClientTemplate().queryForList("queryAllIndustryStock", source);
	}

}
