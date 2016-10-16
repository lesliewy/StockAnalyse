/**
 * 
 */
package com.wy.stock.dao;

import java.util.List;

import com.wy.stock.domain.IndustryInfo;

/**
 * @author leslie
 *
 */
public interface IndustryInfoDao {
	
	void insertIndustryInfo(IndustryInfo industryInfo);
	
	void insertIndustryInfoBatch(List<IndustryInfo> industryInfoList);
	
	void deleteAllIndustryInfo();
	
	void deleteIndustryInfoBySource(String source);
	
	List<IndustryInfo> queryAllIndustryInfo(String source);
	
	void updateByIndustryName(IndustryInfo industryInfo);
	
	void updateIndustryCodeByIndustryName(IndustryInfo industryInfo);
	
	IndustryInfo queryIndustryInfoByName(String industryName, String source);
	
	void updateCorpsNumByIndustryName(IndustryInfo industryInfo);
}
