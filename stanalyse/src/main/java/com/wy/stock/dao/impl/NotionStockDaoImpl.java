/**
 * 
 */
package com.wy.stock.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.wy.stock.dao.NotionStockDao;
import com.wy.stock.domain.NotionStock;

/**
 * @author leslie
 *
 */
public class NotionStockDaoImpl extends SqlMapClientDaoSupport implements NotionStockDao {

	private static Logger LOGGER = Logger.getLogger(NotionStockDaoImpl.class
			.getName());

	public void insertNotionStock(NotionStock notionStock) {
		if(notionStock == null){
			LOGGER.info("notionStock is null, return now...");
			return;
		}
		try{
			getSqlMapClientTemplate().insert("insertNotionStock", notionStock);
		}catch(Exception e){
			LOGGER.info("insertNotionStock: " + e);
		}		
	}

	public void insertNotionStockBatch(List<NotionStock> notionStockList) {
		if(notionStockList == null){
			LOGGER.info("notionStockList is null, return now...");
			return;
		}
		for(NotionStock notionStock : notionStockList){
			insertNotionStock(notionStock);
		}
	}

	public void deleteAllNotionStock(String source) {
		if(StringUtils.isBlank(source)){
			LOGGER.info("source is blank, return now...");
			return;
		}
		getSqlMapClientTemplate().delete("deleteAllNotionStock", source);
	}
	
	public void deleteNotionStockByNotionCode(String notionCode) {
		if(StringUtils.isBlank(notionCode)){
			LOGGER.info("notionCode is null, return now...");
			return;
		}
		getSqlMapClientTemplate().delete("deleteNotionStockByNotionCode", notionCode);
	}

	@SuppressWarnings("unchecked")
	public List<NotionStock> queryNotionStockByNotionCode(NotionStock notionStock) {
		if(StringUtils.isBlank(notionStock.getNotionCode()) || StringUtils.isBlank(notionStock.getSource())){
			LOGGER.info("notionCode or source is null, return now...");
			return null;
		}
		return getSqlMapClientTemplate().queryForList("queryNotionStockByNotionCode", notionStock);
	}
	
	@SuppressWarnings("unchecked")
	public List<NotionStock> queryNotionStockByCode(String code) {
		if(StringUtils.isBlank(code)){
			LOGGER.info("code is null, return now...");
			return null;
		}
		return getSqlMapClientTemplate().queryForList("queryNotionStockByCode", code);
	}

	@SuppressWarnings("unchecked")
	public List<NotionStock> queryAllCodeInNotionStock() {
		return getSqlMapClientTemplate().queryForList("queryAllCodeInNotionStock");
	}

	@SuppressWarnings("unchecked")
	public List<NotionStock> queryNotionCodesNames(String source) {
		if(StringUtils.isBlank(source)){
			LOGGER.info("source is null, return now...");
			return null;
		}
		return getSqlMapClientTemplate().queryForList("queryNotionCodesNames", source);
	}

}
