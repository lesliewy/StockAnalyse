/**
 * 
 */
package com.wy.stock.domain;

import java.sql.Timestamp;


/**
 * ST_STOCK_NOTION  暂时不用.
 * @author leslie
 *
 */
public class StockNotion {
	
	private String code;
	
	private String stockName;
	
	private String notionCodes;
	
	private String notionNames;
	
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

	public String getNotionCodes() {
		return notionCodes;
	}

	public void setNotionCodes(String notionCodes) {
		this.notionCodes = notionCodes;
	}

	public String getNotionNames() {
		return notionNames;
	}

	public void setNotionNames(String notionNames) {
		this.notionNames = notionNames;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

}
