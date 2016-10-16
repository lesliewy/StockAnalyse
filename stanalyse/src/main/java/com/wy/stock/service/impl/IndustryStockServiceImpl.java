/**
 * 
 */
package com.wy.stock.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wy.stock.dao.IndustryStockDao;
import com.wy.stock.domain.IndustryStock;
import com.wy.stock.service.IndustryStockService;

/**
 * @author leslie
 *
 */
public class IndustryStockServiceImpl implements IndustryStockService {

	private IndustryStockDao industryStockDao;

	public void insertIndustryStock(IndustryStock industryStock) {
		industryStockDao.insertIndustryStock(industryStock);
	}

	public void insertIndustryStockBatch(List<IndustryStock> industryStockList) {
		industryStockDao.insertIndustryStockBatch(industryStockList);
	}

	public void deleteAllIndustryStock(String source) {
		industryStockDao.deleteAllIndustryStock(source);
	}
	
	public void deleteIndustryStockByIndustryCode(String industryCode) {
		industryStockDao.deleteIndustryStockByIndustryCode(industryCode);
	}

	public List<IndustryStock> queryIndustryStockByIndustryCode(String industryCode, String source) {
		IndustryStock query = new IndustryStock();
		query.setIndustryCode(industryCode);
		query.setSource(source);
		return industryStockDao.queryIndustryStockByIndustryCode(query);
	}
	
	public List<String> queryCodeByIndustryCode(String industryCode, String source) {
		List<IndustryStock> list = queryIndustryStockByIndustryCode(industryCode, source);
		List<String> result = new ArrayList<String>();
		if(list != null && !list.isEmpty()){
			for(IndustryStock industryStock : list){
				result.add(industryStock.getCode());
			}
		}
		return result;
	}

	public List<IndustryStock> queryIndustryStockByCode(String code) {
		return industryStockDao.queryIndustryStockByCode(code);
	}
	
	public List<IndustryStock> queryAllIndustryStock(String source) {
		return industryStockDao.queryAllIndustryStock(source);
	}
	
	public Map<String, String> queryCodeIndustryNameMap(String source) {
		List<IndustryStock> all = industryStockDao.queryAllIndustryStock(source);
		if(all == null){
			return null;
		}
		Map<String, String> result = new HashMap<String, String>();
		for(IndustryStock stock : all){
			result.put(stock.getCode(), stock.getIndustryName());
		}
		return result;
	}

	public IndustryStockDao getIndustryStockDao() {
		return industryStockDao;
	}

	public void setIndustryStockDao(IndustryStockDao industryStockDao) {
		this.industryStockDao = industryStockDao;
	}

}
