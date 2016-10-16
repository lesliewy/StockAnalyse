/**
 * 
 */
package com.wy.stock.service.impl;

import java.sql.Timestamp;
import java.util.List;

import com.wy.stock.dao.IndustryHotDao;
import com.wy.stock.domain.IndustryHot;
import com.wy.stock.service.IndustryHotService;

/**
 * @author leslie
 *
 */
public class IndustryHotServiceImpl implements IndustryHotService {

	private IndustryHotDao industryHotDao;

	public void insertIndustryHot(IndustryHot industryHot) {
		industryHotDao.insertIndustryHot(industryHot);
	}

	public void insertIndustryHotBatch(List<IndustryHot> industryHotList) {
		industryHotDao.insertIndustryHotBatch(industryHotList);
	}

	public void deleteIndustryHotByTradeDateStr(String tradeDateStr, String source) {
		industryHotDao.deleteIndustryHotByTradeDateStr(tradeDateStr, source);
	}

	public List<IndustryHot> queryIndustryHotByDate(Timestamp tradeDate) {
		return industryHotDao.queryIndustryHotByDate(tradeDate);
	}
	
	public List<IndustryHot> queryIndustryHotInfoByDate(String tradeDateStr, String source) {
		IndustryHot query = new IndustryHot();
		query.setTradeDateStr(tradeDateStr);
		query.setSource(source);
		return industryHotDao.queryIndustryHotInfoByDate(query);
	}
	
	public List<IndustryHot> queryIndustryHotByDateStr(String tradeDateStr,
			String source) {
		return industryHotDao.queryIndustryHotByDateStr(tradeDateStr, source);
	}

	public List<IndustryHot> queryIndustryHotByDateStrBetween(
			String lowTradeDateStr, String highTradeDateStr) {
		return industryHotDao.queryIndustryHotByDateStrBetween(lowTradeDateStr, highTradeDateStr);
	}
	
	public List<IndustryHot> queryIndustryHotForCsv(int year) {
		return industryHotDao.queryIndustryHotForCsv(year);
	}

	public IndustryHotDao getIndustryHotDao() {
		return industryHotDao;
	}

	public void setIndustryHotDao(IndustryHotDao industryHotDao) {
		this.industryHotDao = industryHotDao;
	}

}
