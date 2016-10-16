/**
 * 
 */
package com.wy.stock.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.wy.stock.dao.NotionInfoDao;
import com.wy.stock.domain.NotionInfo;

/**
 * @author leslie
 *
 */
public class NotionInfoDaoImpl extends SqlMapClientDaoSupport implements NotionInfoDao {

	private static Logger LOGGER = Logger.getLogger(NotionInfoDaoImpl.class
			.getName());

	public void insertNotionInfo(NotionInfo notionInfo) {
		if(notionInfo == null){
			LOGGER.info("notionInfo is null, return now...");
			return;
		}
		try{
			getSqlMapClientTemplate().insert("insertNotionInfo", notionInfo);
		}catch(Exception e){
			LOGGER.info("insertNotionInfo: " + e);
		}
	}

	public void insertNotionInfoBatch(List<NotionInfo> notionInfoList) {
		if(notionInfoList == null){
			LOGGER.info("notionInfoList is null, return now...");
			return;
		}
		for(NotionInfo notionInfo : notionInfoList){
			insertNotionInfo(notionInfo);
		}
	}

	public void deleteAllNotionInfo() {
		getSqlMapClientTemplate().delete("deleteAllNotionInfo");
	}

	@SuppressWarnings("unchecked")
	public List<NotionInfo> queryAllNotionInfo(String source) {
		if(StringUtils.isBlank(source)){
			LOGGER.info("source is blank, return now...");
			return null;
		}
		return getSqlMapClientTemplate().queryForList("queryAllNotionInfo", source);
	}

	public void deleteNotionInfoBySource(String source) {
		if(StringUtils.isBlank(source)){
			LOGGER.info("source is null, return now...");
			return;
		}
		getSqlMapClientTemplate().delete("deleteNotionInfoBySource", source);
	}

	public void updateByNotionName(NotionInfo notionInfo) {
		if(notionInfo == null){
			LOGGER.info("notionInfo is null, return now...");
			return;
		}
		if(StringUtils.isBlank(notionInfo.getSource())){
			LOGGER.info("source is null, return now...");
			return;
		}
		getSqlMapClientTemplate().update("updateByNotionName", notionInfo);
	}

	public void updateNotionCodeByNotionName(NotionInfo notionInfo) {
		if(notionInfo == null){
			LOGGER.info("notionInfo is null, return now...");
			return;
		}
		if(StringUtils.isBlank(notionInfo.getSource())){
			LOGGER.info("source is null, return now...");
			return;
		}
		getSqlMapClientTemplate().update("updateNotionCodeByNotionName", notionInfo);
	}

	public NotionInfo queryNotionInfoByName(String notionName, String source) {
		NotionInfo query = new NotionInfo();
		query.setNotionName(notionName);
		query.setSource(source);
		return (NotionInfo) getSqlMapClientTemplate().queryForObject("queryNotionInfoByName", query);
	}

	public void updateCorpsNumByNotionName(NotionInfo notionInfo) {
		getSqlMapClientTemplate().update("updateCorpsNumByNotionName", notionInfo);
	}

}
