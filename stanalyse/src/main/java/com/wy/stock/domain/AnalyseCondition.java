/**
 * 
 */
package com.wy.stock.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 用于储存分析股票的条件, 没有表.
 * @author leslie
 *
 */
public class AnalyseCondition {
	/*
	 * 交易日期，分析日期
	 */
	Timestamp tradeDate;
	
	/*
	 * 收盘价上下限
	 */
	Float closeUpper;
	
	Float closeLower;
	
	/*
	 * 储存资金净流入、净流出的上下限。
	 */
	BigDecimal capFlowUpper;
	
	BigDecimal capFlowLower;
	
	/*
	 * 股票的类型，即股票代码的第一位数. 6-上证； 0-深证；  3-创业板  
	 */
	List<Integer> types;
	
	/*
	 * 需要排除的板块名称，可以从ST_INDUSTRY_STOCK中查找.
	 */
	List<String> excludeIndustries;
	
	/*
	 * 需要排除的股票名称，例如ST, 则过滤掉名称中包含ST的.
	 */
	List<String> excludeName;
	
	/*
	 * 当日的涨跌幅上下限
	 */
	Float changePctUpper;
	
	Float changePctLower;
	
	/*
	 * 累计几日, 取tradeDate之前的(包含tradeDate).
	 */
	Integer accumuDays;
	
	/*
	 * 累计几日涨跌幅的上下限
	 */
	Float accumuDaysChangePctUpper;
	
	Float accumuDaysChangePctLower;
	
	/*
	 * 累计几日主力净流入净占比的上下限.
	 */
	Float accumuMainNetInPctUpper;
	
	Float accumuMainNetInPctLower;
	
	public Timestamp getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(Timestamp tradeDate) {
		this.tradeDate = tradeDate;
	}

	public Float getCloseUpper() {
		return closeUpper;
	}

	public void setCloseUpper(Float closeUpper) {
		this.closeUpper = closeUpper;
	}

	public Float getCloseLower() {
		return closeLower;
	}

	public void setCloseLower(Float closeLower) {
		this.closeLower = closeLower;
	}

	public BigDecimal getCapFlowUpper() {
		return capFlowUpper;
	}

	public void setCapFlowUpper(BigDecimal capFlowUpper) {
		this.capFlowUpper = capFlowUpper;
	}

	public BigDecimal getCapFlowLower() {
		return capFlowLower;
	}

	public void setCapFlowLower(BigDecimal capFlowLower) {
		this.capFlowLower = capFlowLower;
	}

	public List<Integer> getTypes() {
		return types;
	}

	public void setTypes(List<Integer> types) {
		this.types = types;
	}

	public List<String> getExcludeIndustries() {
		return excludeIndustries;
	}

	public void setExcludeIndustries(List<String> excludeIndustries) {
		this.excludeIndustries = excludeIndustries;
	}

	public Float getChangePctUpper() {
		return changePctUpper;
	}

	public void setChangePctUpper(Float changePctUpper) {
		this.changePctUpper = changePctUpper;
	}

	public Float getChangePctLower() {
		return changePctLower;
	}

	public void setChangePctLower(Float changePctLower) {
		this.changePctLower = changePctLower;
	}

	public Integer getAccumuDays() {
		return accumuDays;
	}

	public void setAccumuDays(Integer accumuDays) {
		this.accumuDays = accumuDays;
	}

	public Float getAccumuDaysChangePctUpper() {
		return accumuDaysChangePctUpper;
	}

	public void setAccumuDaysChangePctUpper(Float accumuDaysChangePctUpper) {
		this.accumuDaysChangePctUpper = accumuDaysChangePctUpper;
	}

	public Float getAccumuDaysChangePctLower() {
		return accumuDaysChangePctLower;
	}

	public void setAccumuDaysChangePctLower(Float accumuDaysChangePctLower) {
		this.accumuDaysChangePctLower = accumuDaysChangePctLower;
	}

	public Float getAccumuMainNetInPctUpper() {
		return accumuMainNetInPctUpper;
	}

	public void setAccumuMainNetInPctUpper(Float accumuMainNetInPctUpper) {
		this.accumuMainNetInPctUpper = accumuMainNetInPctUpper;
	}

	public Float getAccumuMainNetInPctLower() {
		return accumuMainNetInPctLower;
	}

	public void setAccumuMainNetInPctLower(Float accumuMainNetInPctLower) {
		this.accumuMainNetInPctLower = accumuMainNetInPctLower;
	}

	public List<String> getExcludeName() {
		return excludeName;
	}

	public void setExcludeName(List<String> excludeName) {
		this.excludeName = excludeName;
	}

	@Override
	public String toString(){
		return "[ tradeDate = " + tradeDate +
				", closeLimit: " + closeLower + " - " + closeUpper +
				", capFlowLimit = " + capFlowLower + " - " + capFlowUpper +
				", types = " + types +
				", excludeIndustries = " + excludeIndustries + ", excludeName: " + excludeName + 
				", changePctLimit = " + changePctLower + " - " + changePctUpper + 
				", accumuDays: " + accumuDays + 
				", accumuDaysChangePctLimit: " + accumuDaysChangePctLower + " - " + accumuDaysChangePctUpper +
				", accumuMainNetInPctLimit: " + accumuMainNetInPctLower + " - " + accumuMainNetInPctUpper
				+ " ]";
	}
}
