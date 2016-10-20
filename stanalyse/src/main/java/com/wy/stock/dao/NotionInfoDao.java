/**
 * 
 */
package com.wy.stock.dao;

import java.util.List;

import com.wy.stock.domain.NotionInfo;

/**
 * @author leslie
 *
 */
public interface NotionInfoDao {
	
	void insertNotionInfo(NotionInfo notionInfo);
	
	void insertNotionInfoBatch(List<NotionInfo> notionInfoList);
	
	void deleteAllNotionInfo();
	
	List<NotionInfo> queryAllNotionInfo(String source);
	
	void deleteNotionInfoBySource(String source);
	
	void deleteNotionInfoByType(String type, String source);
	
	void updateByNotionName(NotionInfo notionInfo);
	
	void updateNotionCodeByNotionName(NotionInfo notionInfo);
	
	NotionInfo queryNotionInfoByName(String notionName, String type,  String source);
	
	void updateCorpsNumByNotionName(NotionInfo notionInfo);
	
}
