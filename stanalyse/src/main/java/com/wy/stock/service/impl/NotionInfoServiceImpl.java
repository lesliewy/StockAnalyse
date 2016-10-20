/**
 * 
 */
package com.wy.stock.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wy.stock.dao.NotionInfoDao;
import com.wy.stock.domain.NotionInfo;
import com.wy.stock.service.NotionInfoService;
import com.wy.stock.utils.StockConstant;

/**
 * @author leslie
 *
 */
public class NotionInfoServiceImpl implements NotionInfoService {

	private NotionInfoDao notionInfoDao;

	public void insertNotionInfo(NotionInfo notionInfo) {
		notionInfoDao.insertNotionInfo(notionInfo);
	}

	public void insertNotionInfoBatch(List<NotionInfo> notionInfoList) {
		notionInfoDao.insertNotionInfoBatch(notionInfoList);
	}

	public void deleteAllNotionInfo() {
		notionInfoDao.deleteAllNotionInfo();
	}
	
	public void deleteNotionInfoBySource(String source) {
		notionInfoDao.deleteNotionInfoBySource(source);
	}
	
	public void deleteNotionInfoByType(String type, String source) {
		notionInfoDao.deleteNotionInfoByType(type, source);
	}
	
	public void updateByNotionName(NotionInfo notionInfo) {
		notionInfoDao.updateByNotionName(notionInfo);
	}
	
	public void updateNotionCodeByNotionName(NotionInfo notionInfo) {
		notionInfoDao.updateNotionCodeByNotionName(notionInfo);
	}
	
	public void updateCorpsNumByNotionName(NotionInfo notionInfo) {
		notionInfoDao.updateCorpsNumByNotionName(notionInfo);
	}

	public List<NotionInfo> queryAllNotionInfo(String source) {
		return notionInfoDao.queryAllNotionInfo(source);
	}
	
	public Map<String, String> queryNotionNameUrlMap(String source) {
		Map<String, String> map = new HashMap<String, String>();
		List<NotionInfo> notionInfoList = queryAllNotionInfo(source);
		if(notionInfoList != null){
			for(NotionInfo notionInfo : notionInfoList){
				map.put(notionInfo.getNotionName(), notionInfo.getNotionUrl());
			}
		}
		return map;
	}
	
	public Map<String, String> queryNotionUrlNameMap(String source) {
		Map<String, String> map = new HashMap<String, String>();
		List<NotionInfo> notionInfoList = queryAllNotionInfo(source);
		if(notionInfoList != null){
			for(NotionInfo notionInfo : notionInfoList){
				map.put(notionInfo.getNotionUrl(), notionInfo.getNotionName());
			}
		}
		return map;
	}
	
	public NotionInfo queryNotionInfoByName(String notionName, String type, String source) {
		return notionInfoDao.queryNotionInfoByName(notionName,  type, source);
	}
	
	public Map<String, String> queryNotionInfoMap(String source) {
		List<NotionInfo> allNotionInfo = notionInfoDao.queryAllNotionInfo(source);
		if(allNotionInfo == null){
			return null;
		}
		Map<String , String> result = new HashMap<String, String>();
		for(NotionInfo info : allNotionInfo){
			String hyCode = info.getNotionUrl().split("/")[5];
			result.put(hyCode, info.getNotionName());
		}
		return result;
	}
	
	public Map<String, Integer> queryNotionNameNumMap(String source) {
		List<NotionInfo> list = notionInfoDao.queryAllNotionInfo(StockConstant.THS_FLAG);
		if(list == null){
			return null;
		}
		Map<String, Integer> result = new HashMap<String, Integer>();
		for(NotionInfo info : list){
			result.put(info.getNotionName(), info.getCorpsNum());
		}
		return result;
	}
	
	public Map<String, String> queryNotionCodeNameMap(String source) {
		List<NotionInfo> list = notionInfoDao.queryAllNotionInfo(source);
		if(list == null){
			return null;
		}
		Map<String, String> result = new HashMap<String, String>();
		for(NotionInfo info : list){
			result.put(info.getNotionCode(), info.getNotionName());
		}
		return result;
	}
	
	public Map<String, Integer> queryNotionCorpsNumMap(String source) {
		List<NotionInfo> list = notionInfoDao.queryAllNotionInfo(source);
		if(list == null){
			return null;
		}
		Map<String, Integer> result = new HashMap<String, Integer>();
		for(NotionInfo info : list){
			result.put(info.getNotionName(), info.getCorpsNum());
		}
		return result;
	}
	
	public NotionInfoDao getNotionInfoDao() {
		return notionInfoDao;
	}

	public void setNotionInfoDao(NotionInfoDao notionInfoDao) {
		this.notionInfoDao = notionInfoDao;
	}
}
