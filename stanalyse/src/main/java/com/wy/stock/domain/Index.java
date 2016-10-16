/**
 * 
 */
package com.wy.stock.domain;

import java.sql.Timestamp;

/**
 * ST_INDEX
 * @author leslie
 *
 */
public class Index {
	
	private Timestamp tradeDate;
	
	private String indexCode;
	
	private String indexName;
	
	// 最新价
	private Float newPrice;
	
	// 涨跌幅度
	private Float changeAmount;
	
	// 涨跌 %
	private Float changePercent;
	
	private Float open;
	
	private Float high;
	
	private Float low;
	
	private Float close;
	
	private Float lastClose;
	
	// 振幅
	private Float amplitude;
	
	// 成交量(万手)
	private Float volumn;
	
	// 成交金额
	private Float volumnAmount;
	
	private Timestamp timestamp;
	
	private String lowTradeDateStr;
	
	private String highTradeDateStr;
	
	private Float totalChangePercent;

	public Timestamp getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(Timestamp tradeDate) {
		this.tradeDate = tradeDate;
	}

	public String getIndexCode() {
		return indexCode;
	}

	public void setIndexCode(String indexCode) {
		this.indexCode = indexCode;
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public Float getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(Float newPrice) {
		this.newPrice = newPrice;
	}

	public Float getChangeAmount() {
		return changeAmount;
	}

	public void setChangeAmount(Float changeAmount) {
		this.changeAmount = changeAmount;
	}

	public Float getChangePercent() {
		return changePercent;
	}

	public void setChangePercent(Float changePercent) {
		this.changePercent = changePercent;
	}

	public Float getOpen() {
		return open;
	}

	public void setOpen(Float open) {
		this.open = open;
	}

	public Float getHigh() {
		return high;
	}

	public void setHigh(Float high) {
		this.high = high;
	}

	public Float getLow() {
		return low;
	}

	public void setLow(Float low) {
		this.low = low;
	}

	public Float getClose() {
		return close;
	}

	public void setClose(Float close) {
		this.close = close;
	}

	public Float getLastClose() {
		return lastClose;
	}

	public void setLastClose(Float lastClose) {
		this.lastClose = lastClose;
	}

	public Float getAmplitude() {
		return amplitude;
	}

	public void setAmplitude(Float amplitude) {
		this.amplitude = amplitude;
	}

	public Float getVolumn() {
		return volumn;
	}

	public void setVolumn(Float volumn) {
		this.volumn = volumn;
	}

	public Float getVolumnAmount() {
		return volumnAmount;
	}

	public void setVolumnAmount(Float volumnAmount) {
		this.volumnAmount = volumnAmount;
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

	public Float getTotalChangePercent() {
		return totalChangePercent;
	}

	public void setTotalChangePercent(Float totalChangePercent) {
		this.totalChangePercent = totalChangePercent;
	}

}
