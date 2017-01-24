/**
 * 
 */
package com.wy.stock.domain;

import java.sql.Timestamp;

/**
 * ST_INDUSTRY_HOT
 * @author leslie
 *
 */
public class IndustryHot {
	
	private Timestamp tradeDate;
	
	private int rank;
	
	private String industryName;
	
	private Float changePercent;
	
	private Float totalMarketCap;
	
	private Float turnoverRate;
	
	private int riseStocksNum;
	
	private int fallStocksNum;
	
	private String riseLeadStockName;
	
	private Float riseLeadStockChangePercent;
	
	private Timestamp timestamp;
	
	private String source;
	
	private Float avgPrice;
	
	private String industryUrl;

	private String tradeDateStr;
	
	private int corpsNum;
	
	private String industryCode;
	
	private String lowTradeDateStr;
	
	private String highTradeDateStr;
	
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

	public Float getChangePercent() {
		return changePercent;
	}

	public void setChangePercent(Float changePercent) {
		this.changePercent = changePercent;
	}

	public Float getTotalMarketCap() {
		return totalMarketCap;
	}

	public void setTotalMarketCap(Float totalMarketCap) {
		this.totalMarketCap = totalMarketCap;
	}

	public Float getTurnoverRate() {
		return turnoverRate;
	}

	public void setTurnoverRate(Float turnoverRate) {
		this.turnoverRate = turnoverRate;
	}

	public int getRiseStocksNum() {
		return riseStocksNum;
	}

	public void setRiseStocksNum(int riseStocksNum) {
		this.riseStocksNum = riseStocksNum;
	}

	public int getFallStocksNum() {
		return fallStocksNum;
	}

	public void setFallStocksNum(int fallStocksNum) {
		this.fallStocksNum = fallStocksNum;
	}

	public String getRiseLeadStockName() {
		return riseLeadStockName;
	}

	public void setRiseLeadStockName(String riseLeadStockName) {
		this.riseLeadStockName = riseLeadStockName;
	}

	public Float getRiseLeadStockChangePercent() {
		return riseLeadStockChangePercent;
	}

	public void setRiseLeadStockChangePercent(Float riseLeadStockChangePercent) {
		this.riseLeadStockChangePercent = riseLeadStockChangePercent;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getIndustryUrl() {
		return industryUrl;
	}

	public void setIndustryUrl(String industryUrl) {
		this.industryUrl = industryUrl;
	}

	public Float getAvgPrice() {
		return avgPrice;
	}

	public void setAvgPrice(Float avgPrice) {
		this.avgPrice = avgPrice;
	}

	public String getTradeDateStr() {
		return tradeDateStr;
	}

	public void setTradeDateStr(String tradeDateStr) {
		this.tradeDateStr = tradeDateStr;
	}

	public int getCorpsNum() {
		return corpsNum;
	}

	public void setCorpsNum(int corpsNum) {
		this.corpsNum = corpsNum;
	}

	public String getIndustryCode() {
		return industryCode;
	}

	public void setIndustryCode(String industryCode) {
		this.industryCode = industryCode;
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
	
}
