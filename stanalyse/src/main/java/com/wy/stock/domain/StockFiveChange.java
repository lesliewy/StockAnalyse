/**
 * 
 */
package com.wy.stock.domain;

import java.sql.Timestamp;

/**
 * ST_STOCK_FIVE_CHANGE
 * @author leslie
 *
 */
public class StockFiveChange {
	
	private String code;
	
	private String stockName;
	
	// 涨跌 %
	private Float changePercent;
	
	// 5分钟涨跌 %
	private Float fiveChangePercent;
	
	private Timestamp timestamp;

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

	public Float getChangePercent() {
		return changePercent;
	}

	public void setChangePercent(Float changePercent) {
		this.changePercent = changePercent;
	}

	public Float getFiveChangePercent() {
		return fiveChangePercent;
	}

	public void setFiveChangePercent(Float fiveChangePercent) {
		this.fiveChangePercent = fiveChangePercent;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

}
