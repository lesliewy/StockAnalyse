/**
 * 
 */
package com.wy.stock.domain;

import java.sql.Timestamp;

/**
 * ST_INDUSTRY_HOT_STOCKS
 * @author leslie
 *
 */
public class IndustryHotStocks {
	
	private Timestamp tradeDate;
	
	private int rank;
	
	private String industryName;
	
	private String code;
	
	private String stockName;
	
	private Float newPrice;
	
	private Float changePercent;
	
	private Timestamp timestamp;
	
	private String lowTradeDateStr;
	
	private String highTradeDateStr;
	
	private int industryRank;
	
	private Float industryChangePercent;
	
	private String industryNames;

	public Timestamp getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(Timestamp tradeDate) {
		this.tradeDate = tradeDate;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getIndustryName() {
		return industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStockName() {
		return stockName;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public Float getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(Float newPrice) {
		this.newPrice = newPrice;
	}

	public Float getChangePercent() {
		return changePercent;
	}

	public void setChangePercent(Float changePercent) {
		this.changePercent = changePercent;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getLowTradeDateStr() {
		return lowTradeDateStr;
	}

	public void setLowTradeDateStr(String lowTradeDateStr) {
		this.lowTradeDateStr = lowTradeDateStr;
	}

	public String getHighTradeDateStr() {
		return highTradeDateStr;
	}

	public void setHighTradeDateStr(String highTradeDateStr) {
		this.highTradeDateStr = highTradeDateStr;
	}

	public int getIndustryRank() {
		return industryRank;
	}

	public void setIndustryRank(int industryRank) {
		this.industryRank = industryRank;
	}

	public Float getIndustryChangePercent() {
		return industryChangePercent;
	}

	public void setIndustryChangePercent(Float industryChangePercent) {
		this.industryChangePercent = industryChangePercent;
	}

	public String getIndustryNames() {
		return industryNames;
	}

	public void setIndustryNames(String industryNames) {
		this.industryNames = industryNames;
	}
	
}
