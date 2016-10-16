/**
 * 
 */
package com.wy.stock.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.wy.stock.dao.NotionStockDao;
import com.wy.stock.domain.NotionStock;
import com.wy.stock.service.NotionStockService;

/**
 * @author leslie
 *
 */
public class NotionStockServiceImpl implements NotionStockService {

	private NotionStockDao notionStockDao;

	public void insertNotionStock(NotionStock notionStock) {
		notionStockDao.insertNotionStock(notionStock);
	}

	public void insertNotionStockBatch(List<NotionStock> notionStockList) {
		notionStockDao.insertNotionStockBatch(notionStockList);
	}

	public void deleteAllNotionStock(String source) {
		notionStockDao.deleteAllNotionStock(source);
	}
	
	public void deleteNotionStockByNotionCode(String notionCode) {
		notionStockDao.deleteNotionStockByNotionCode(notionCode);
	}

	public List<NotionStock> queryNotionStockByNotionCode(String notionCode, String source) {
		NotionStock query = new NotionStock();
		query.setNotionCode(notionCode);
		query.setSource(source);
		return notionStockDao.queryNotionStockByNotionCode(query);
	}
	
	public List<String> queryCodeByNotionCode(String notionCode, String source) {
		List<NotionStock> list = queryNotionStockByNotionCode(notionCode, source);
		List<String> result = new ArrayList<String>();
		if(list != null && !list.isEmpty()){
			for(NotionStock notionStock : list){
				result.add(notionStock.getCode());
			}
		}
		return result;
	}

	public List<NotionStock> queryNotionStockByCode(String code) {
		return notionStockDao.queryNotionStockByCode(code);
	}
	
	public List<NotionStock> queryAllCodeInNotionStock() {
		return notionStockDao.queryAllCodeInNotionStock();
	}
	
	public List<NotionStock> queryNotionCodesNames(String source) {
		return notionStockDao.queryNotionCodesNames(source);
	}

	public NotionStockDao getNotionStockDao() {
		return notionStockDao;
	}

	public void setNotionStockDao(NotionStockDao notionStockDao) {
		this.notionStockDao = notionStockDao;
	}

}
