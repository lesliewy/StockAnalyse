/**
 * 
 */
package com.wy.stock.domain;

import java.sql.Timestamp;

/**
 * ST_NOTION_STOCK
 * @author leslie
 *
 */
public class NotionStock {
	
	private String notionCode;
	
	private String notionName;
	
	private String code;
	
	private String stockName;
	
	private String notionCodes;
	
	private String notionNames;
	
	private Timestamp timestamp;
	
	private String source;

	public String getNotionCode() {
		return notionCode;
	}

	public void setNotionCode(String notionCode) {
		this.notionCode = notionCode;
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

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
}
