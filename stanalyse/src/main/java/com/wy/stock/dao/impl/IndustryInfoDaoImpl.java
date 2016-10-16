/**
 * 
 */
package com.wy.stock.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.wy.stock.dao.IndustryInfoDao;
import com.wy.stock.domain.IndustryInfo;

/**
 * @author leslie
 *
 */
public class IndustryInfoDaoImpl extends SqlMapClientDaoSupport implements IndustryInfoDao {

	private static Logger LOGGER = Logger.getLogger(IndustryInfoDaoImpl.class
			.getName());

	public void insertIndustryInfo(IndustryInfo industryInfo) {
		if(industryInfo == null){
			LOGGER.info("industryInfo is null, return now...");
			return;
		}
		try{
			getSqlMapClientTemplate().insert("insertIndustryInfo", industryInfo);
		}catch(Exception e){
			LOGGER.info("insertIndustryInfo: " + e);
		}
	}

	public void insertIndustryInfoBatch(List<IndustryInfo> industryInfoList) {
		if(industryInfoList == null){
			LOGGER.info("industryInfoList is null, return now...");
			return;
		}
		for(IndustryInfo industryInfo : industryInfoList){
			insertIndustryInfo(industryInfo);
		}
	}

	public void deleteAllIndustryInfo() {
		getSqlMapClientTemplate().delete("deleteAllIndustryInfo");
	}

	@SuppressWarnings("unchecked")
	public List<IndustryInfo> queryAllIndustryInfo(String source) {
		if(StringUtils.isBlank(source)){
			LOGGER.info("source is blank, return now...");
			return null;
		}
		return getSqlMapClientTemplate().queryForList("queryAllIndustryInfo", source);
	}

	public void deleteIndustryInfoBySource(String source) {
		if(StringUtils.isBlank(source)){
			LOGGER.info("source is null, return now...");
			return;
		}
		getSqlMapClientTemplate().delete("deleteIndustryInfoBySource", source);
	}
	
	public void updateByIndustryName(IndustryInfo industryInfo) {
		if(industryInfo == null){
			LOGGER.info("industryInfo is null, return now...");
			return;
		}
		if(StringUtils.isBlank(industryInfo.getSource())){
			LOGGER.info("source is null, return now...");
			return;
		}
		getSqlMapClientTemplate().update("updateByIndustryName", industryInfo);
	}

	public void updateIndustryCodeByIndustryName(IndustryInfo industryInfo) {
		if(industryInfo == null){
			LOGGER.info("industryInfo is null, return now...");
			return;
		}
		if(StringUtils.isBlank(industryInfo.getSource())){
			LOGGER.info("source is null, return now...");
			return;
		}
		getSqlMapClientTemplate().update("updateIndustryCodeByIndustryName", industryInfo);
	}
	
	public void updateCorpsNumByIndustryName(IndustryInfo industryInfo) {
		getSqlMapClientTemplate().update("updateCorpsNumByIndustryName", industryInfo);
	}

	public IndustryInfo queryIndustryInfoByName(String industryName,
			String source) {
		IndustryInfo query = new IndustryInfo();
		query.setIndustryName(industryName);
		query.setSource(source);
		return (IndustryInfo)getSqlMapClientTemplate().queryForObject("queryIndustryInfoByName", query);
	}

}
