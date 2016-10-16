/**
 * 
 */
package com.wy.stock.domain;

import java.sql.Timestamp;

/**
 * ST_NOTION_HOT_STOCKS
 * @author leslie
 *
 */
public class NotionHotStocks {
	
	private Timestamp tradeDate;
	
	private int rank;
	
	private String notionName;
	
	private String code;
	
	private String stockName;
	
	private Float newPrice;
	
	private Float changePercent;
	
	private Timestamp timestamp;
	
	private int notionRank;
	
	private Float notionChangePercent;
	
	private String tradeDateStr;
	
	private String lowTradeDateStr;
	
	private String highTradeDateStr;
	
	private String notionNames;
	
	private Float totalChangePercent;

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

	public int getNotionRank() {
		return notionRank;
	}

	public void setNotionRank(int notionRank) {
		this.notionRank = notionRank;
	}

	public Float getNotionChangePercent() {
		return notionChangePercent;
	}

	public void setNotionChangePercent(Float notionChangePercent) {
		this.notionChangePercent = notionChangePercent;
	}

	public String getTradeDateStr() {
		return tradeDateStr;
	}

	public void setTradeDateStr(String tradeDateStr) {
		this.tradeDateStr = tradeDateStr;
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

	public String getNotionNames() {
		return notionNames;
	}

	public void setNotionNames(String notionNames) {
		this.notionNames = notionNames;
	}

	public Float getTotalChangePercent() {
		return totalChangePercent;
	}

	public void setTotalChangePercent(Float totalChangePercent) {
		this.totalChangePercent = totalChangePercent;
	}
	
}
