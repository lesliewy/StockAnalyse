/**
 * 
 */
package com.wy.stock.service;

import java.util.List;
import java.util.Map;

import com.wy.stock.domain.IndustryInfo;

/**
 * @author leslie
 *
 */
public interface IndustryInfoService {

	void insertIndustryInfo(IndustryInfo industryInfo);
	
	void insertIndustryInfoBatch(List<IndustryInfo> industryInfoList);
	
	void deleteAllIndustryInfo();
	
	void deleteIndustryInfoBySource(String source);
	
	void updateByIndustryName(IndustryInfo industryInfo);
	
	void updateIndustryCodeByIndustryName(IndustryInfo industryInfo);
	
	void updateCorpsNumByIndustryName(IndustryInfo industryInfo);
	
	List<IndustryInfo> queryAllIndustryInfo(String source);
	
	Map<String, String> queryIndustryNameUrlMap(String source);
	
	Map<String, String> queryIndustryUrlNameMap(String source);
	
	IndustryInfo queryIndustryInfoByName(String industryName, String source);
	
	Map<String, String> queryIndustryInfoMap(String source);
	
	Map<String, String> queryIndustryCodeNameMap(String source);
	
	Map<String, Integer> queryIndustryCorpsNumMap(String source);
}
