/**
 * 
 */
package com.wy.stock.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @author leslie
 *
 */
public class StockHistory {

	private String code;
	
	private String exchange;
	
	private String type;
	
	private Timestamp tradeDate;
	
	private Float open;
	
	private Float high;
	
	private Float low;
	
	private Float close;
	
	// 添加了 typeHandler 后插入数据库没有问题，但是查询时报错， 现修改为BigDecimal
	private BigDecimal volumn;
	
	// 调整后的价格 今天的收盘价当做加权价格
	private Float adjClose;
	
	private Timestamp timestamp;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Timestamp getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(Timestamp tradeDate) {
		this.tradeDate = tradeDate;
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

	public BigDecimal getVolumn() {
		return volumn;
	}

	public void setVolumn(BigDecimal volumn) {
		this.volumn = volumn;
	}

	public Float getAdjClose() {
		return adjClose;
	}

	public void setAdjClose(Float adjClose) {
		this.adjClose = adjClose;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
}
