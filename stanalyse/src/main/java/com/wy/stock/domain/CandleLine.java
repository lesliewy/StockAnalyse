/**
 * 
 */
package com.wy.stock.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * K 线
 * @author leslie
 *
 */
public class CandleLine {
	
	private Float open;
	
	private Float close;
	
	private Float high;
	
	private Float low;
	
	// 上影线
	private Float upperLine;
	
	// 下影线
	private Float lowerLine;
	
	// 实体部分
	private Float entity;
	
	// 类型: 阳线(close > open): A   阴线(close < open): D   点(close=open):E
	private String type;
	
	// 成交量
	private BigDecimal volumn;
	
	// 周期(日:D 周:W 月:M)
	private String period;
	
	// 周期是日的情况下， 记录日期.
	private Timestamp periodValue;

	public Float getOpen() {
		return open;
	}

	public void setOpen(Float open) {
		this.open = open;
	}

	public Float getClose() {
		return close;
	}

	public void setClose(Float close) {
		this.close = close;
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

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public Timestamp getPeriodValue() {
		return periodValue;
	}

	public void setPeriodValue(Timestamp periodValue) {
		this.periodValue = periodValue;
	}

	public Float getUpperLine() {
		upperLine = Math.abs(high - open);
		return upperLine;
	}

	public Float getLowerLine() {
		lowerLine = Math.abs(close - low);
		return lowerLine;
	}

	public Float getEntity() {
		entity = Math.abs(open - close);
		return entity;
	}

	public String getType() {
		if(open > close){
			type = "D";
		}else if(open < close){
			type = "A";
		}else if(open.floatValue() == close.floatValue()){
			type = "E";
		}
		return type;
	}

	public BigDecimal getVolumn() {
		return volumn;
	}

	public void setVolumn(BigDecimal volumn) {
		this.volumn = volumn;
	}

}
