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
public class IndustryInfo {
	
	private String industryName;
	
	private String industryUrl;
	
	private Timestamp timestamp;
	
	private String source;
	
	private String industryCode;
	
	private Integer corpsNum;
	
	public String getIndustryName() {
		return industryName;
	}

	public void setIndustryName(String industryName) {
		this.industryName = industryName;
	}

	public String getIndustryUrl() {
		return industryUrl;
	}

	public void setIndustryUrl(String industryUrl) {
		this.industryUrl = industryUrl;
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

	public String getIndustryCode() {
		return industryCode;
	}

	public void setIndustryCode(String industryCode) {
		this.industryCode = industryCode;
	}

	public Integer getCorpsNum() {
		return corpsNum;
	}

	public void setCorpsNum(Integer corpsNum) {
		this.corpsNum = corpsNum;
	}

}
