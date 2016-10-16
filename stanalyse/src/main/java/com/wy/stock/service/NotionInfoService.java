/**
 * 
 */
package com.wy.stock.service;

import java.util.List;
import java.util.Map;

import com.wy.stock.domain.NotionInfo;

/**
 * @author leslie
 *
 */
public interface NotionInfoService {

	void insertNotionInfo(NotionInfo notionInfo);
	
	void insertNotionInfoBatch(List<NotionInfo> notionInfoList);
	
	void deleteAllNotionInfo();
	
	void deleteNotionInfoBySource(String source);
	
	void updateByNotionName(NotionInfo notionInfo);
	
	void updateNotionCodeByNotionName(NotionInfo notionInfo);
	
	void updateCorpsNumByNotionName(NotionInfo notionInfo);
	
	List<NotionInfo> queryAllNotionInfo(String source);
	
	Map<String, String> queryNotionNameUrlMap(String source);
	
	Map<String, String> queryNotionUrlNameMap(String source);
	
	NotionInfo queryNotionInfoByName(String notionName, String source);
	
	Map<String, String> queryNotionInfoMap(String source);
	
	Map<String, Integer> queryNotionNameNumMap(String source);
	
	Map<String, String> queryNotionCodeNameMap(String source);
	
	Map<String, Integer> queryNotionCorpsNumMap(String source);
}
