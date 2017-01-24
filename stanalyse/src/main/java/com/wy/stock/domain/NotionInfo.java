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
public class NotionInfo {
	
	private String type;
	
	private String notionName;
	
	private String notionUrl;
	
	private Timestamp timestamp;
	
	private String source;
	
	private String notionCode;
	
	private Integer corpsNum;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNotionName() {
		return notionName;
	}

	public void setNotionName(String notionName) {
		this.notionName = notionName;
	}

	public String getNotionUrl() {
		return notionUrl;
	}

	public void setNotionUrl(String notionUrl) {
		this.notionUrl = notionUrl;
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

	public String getNotionCode() {
		return notionCode;
	}

	public void setNotionCode(String notionCode) {
		this.notionCode = notionCode;
	}

	public Integer getCorpsNum() {
		return corpsNum;
	}

	public void setCorpsNum(Integer corpsNum) {
		this.corpsNum = corpsNum;
	}
	
}
