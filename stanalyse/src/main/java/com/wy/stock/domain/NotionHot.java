/**
 * 
 */
package com.wy.stock.domain;

import java.sql.Timestamp;

/**
 * ST_NOTION_HOT
 * @author leslie
 *
 */
public class NotionHot {
	
	private Timestamp tradeDate;
	
	private int rank;
	
	private String notionName;
	
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
	
	private String notionUrl;
	
	private String tradeDateStr;
	
	private String lowTradeDateStr;
	
	private String highTradeDateStr;
	
	private int corpsNum;
	
	private String notionCode;
	
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

	public String getNotionName() {
		return notionName;
	}

	public void setNotionName(String notionName) {
		this.notionName = notionName;
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

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getNotionUrl() {
		return notionUrl;
	}

	public void setNotionUrl(String notionUrl) {
		this.notionUrl = notionUrl;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
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

	public String getNotionCode() {
		return notionCode;
	}

	public void setNotionCode(String notionCode) {
		this.notionCode = notionCode;
	}

}
