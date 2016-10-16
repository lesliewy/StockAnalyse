/**
 * 
 */
package com.wy.stock.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wy.stock.dao.IndustryInfoDao;
import com.wy.stock.domain.IndustryInfo;
import com.wy.stock.service.IndustryInfoService;

/**
 * @author leslie
 *
 */
public class IndustryInfoServiceImpl implements IndustryInfoService {

	private IndustryInfoDao industryInfoDao;

	public void insertIndustryInfo(IndustryInfo industryInfo) {
		industryInfoDao.insertIndustryInfo(industryInfo);
	}

	public void insertIndustryInfoBatch(List<IndustryInfo> industryInfoList) {
		industryInfoDao.insertIndustryInfoBatch(industryInfoList);
	}

	public void deleteAllIndustryInfo() {
		industryInfoDao.deleteAllIndustryInfo();
	}
	
	public void deleteIndustryInfoBySource(String source) {
		industryInfoDao.deleteIndustryInfoBySource(source);
	}
	
	public void updateByIndustryName(IndustryInfo industryInfo) {
		industryInfoDao.updateByIndustryName(industryInfo);
	}
	
	public void updateIndustryCodeByIndustryName(IndustryInfo industryInfo) {
		industryInfoDao.updateIndustryCodeByIndustryName(industryInfo);
	}
	
	public void updateCorpsNumByIndustryName(IndustryInfo industryInfo) {
		industryInfoDao.updateCorpsNumByIndustryName(industryInfo);
	}

	public List<IndustryInfo> queryAllIndustryInfo(String source) {
		return industryInfoDao.queryAllIndustryInfo(source);
	}
	
	public Map<String, String> queryIndustryNameUrlMap(String source) {
		Map<String, String> map = new HashMap<String, String>();
		List<IndustryInfo> industryInfoList = queryAllIndustryInfo(source);
		if(industryInfoList != null){
			for(IndustryInfo industryInfo : industryInfoList){
				map.put(industryInfo.getIndustryName(), industryInfo.getIndustryUrl());
			}
		}
		return map;
	}
	
	public Map<String, String> queryIndustryUrlNameMap(String source) {
		Map<String, String> map = new HashMap<String, String>();
		List<IndustryInfo> industryInfoList = queryAllIndustryInfo(source);
		if(industryInfoList != null){
			for(IndustryInfo industryInfo : industryInfoList){
				map.put(industryInfo.getIndustryUrl(), industryInfo.getIndustryName());
			}
		}
		return map;
	}
	
	public IndustryInfo queryIndustryInfoByName(String industryName,
			String source) {
		return industryInfoDao.queryIndustryInfoByName(industryName, source);
	}
	
	public Map<String, String> queryIndustryInfoMap(String source) {
		List<IndustryInfo> allIndustryInfo = industryInfoDao.queryAllIndustryInfo(source);
		if(allIndustryInfo == null){
			return null;
		}
		Map<String , String> result = new HashMap<String, String>();
		for(IndustryInfo info : allIndustryInfo){
			String hyCode = info.getIndustryUrl().split("/")[5];
			result.put(hyCode, info.getIndustryName());
		}
		return result;
	}

	public Map<String, String> queryIndustryCodeNameMap(String source) {
		List<IndustryInfo> list = industryInfoDao.queryAllIndustryInfo(source);
		if(list == null){
			return null;
		}
		Map<String, String> result = new HashMap<String, String>();
		for(IndustryInfo info : list){
			result.put(info.getIndustryCode(), info.getIndustryName());
		}
		return result;
	}

	public Map<String, Integer> queryIndustryCorpsNumMap(String source) {
		List<IndustryInfo> list = industryInfoDao.queryAllIndustryInfo(source);
		if(list == null){
			return null;
		}
		Map<String, Integer> result = new HashMap<String, Integer>();
		for(IndustryInfo info : list){
			result.put(info.getIndustryName(), info.getCorpsNum());
		}
		return result;
	}
	
	public IndustryInfoDao getIndustryInfoDao() {
		return industryInfoDao;
	}

	public void setIndustryInfoDao(IndustryInfoDao industryInfoDao) {
		this.industryInfoDao = industryInfoDao;
	}

}
