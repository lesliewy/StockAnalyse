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
public class StockCapFlow {
	
	private Timestamp tradeDate;

	private String code;
	
	private Float close;
	
	// 涨跌 %
	private Float changePercent;
	
	// 主力净流入
	private BigDecimal mainNetIn;
	
	// 主力净占比， 净流入占当日成交量的比例.
	private Float mainNetInPercent;
	
	// 超大单
	private BigDecimal superLarge;
	
	private Float superLargePercent;
	
	// 大单
	private BigDecimal large;
	
	private Float largePercent;
	
	// 中单
	private BigDecimal middle;
	
	private Float middlePercent;
	
	// 小单
	private BigDecimal small;
	
	private Float smallPercent;
	
	private Timestamp timestamp;
	
	// 累计涨跌幅总和.
	private Float accumuChangePct;
	// 较早的分析日期
	private Timestamp tradeDateLower;
	// 较晚的分析日期
	private Timestamp tradeDateUpper;
	
	// 累计主力净占比.
	private Float accumuMainNetInPct;

	public Timestamp getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(Timestamp tradeDate) {
		this.tradeDate = tradeDate;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Float getClose() {
		return close;
	}

	public void setClose(Float close) {
		this.close = close;
	}

	public Float getChangePercent() {
		return changePercent;
	}

	public void setChangePercent(Float changePercent) {
		this.changePercent = changePercent;
	}

	public BigDecimal getMainNetIn() {
		return mainNetIn;
	}

	public void setMainNetIn(BigDecimal mainNetIn) {
		this.mainNetIn = mainNetIn;
	}

	public Float getMainNetInPercent() {
		return mainNetInPercent;
	}

	public void setMainNetInPercent(Float mainNetInPercent) {
		this.mainNetInPercent = mainNetInPercent;
	}

	public BigDecimal getSuperLarge() {
		return superLarge;
	}

	public void setSuperLarge(BigDecimal superLarge) {
		this.superLarge = superLarge;
	}

	public Float getSuperLargePercent() {
		return superLargePercent;
	}

	public void setSuperLargePercent(Float superLargePercent) {
		this.superLargePercent = superLargePercent;
	}

	public BigDecimal getLarge() {
		return large;
	}

	public void setLarge(BigDecimal large) {
		this.large = large;
	}

	public Float getLargePercent() {
		return largePercent;
	}

	public void setLargePercent(Float largePercent) {
		this.largePercent = largePercent;
	}

	public BigDecimal getMiddle() {
		return middle;
	}

	public void setMiddle(BigDecimal middle) {
		this.middle = middle;
	}

	public Float getMiddlePercent() {
		return middlePercent;
	}

	public void setMiddlePercent(Float middlePercent) {
		this.middlePercent = middlePercent;
	}

	public BigDecimal getSmall() {
		return small;
	}

	public void setSmall(BigDecimal small) {
		this.small = small;
	}

	public Float getSmallPercent() {
		return smallPercent;
	}

	public void setSmallPercent(Float smallPercent) {
		this.smallPercent = smallPercent;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public Float getAccumuChangePct() {
		return accumuChangePct;
	}

	public void setAccumuChangePct(Float accumuChangePct) {
		this.accumuChangePct = accumuChangePct;
	}

	public Timestamp getTradeDateLower() {
		return tradeDateLower;
	}

	public void setTradeDateLower(Timestamp tradeDateLower) {
		this.tradeDateLower = tradeDateLower;
	}

	public Timestamp getTradeDateUpper() {
		return tradeDateUpper;
	}

	public void setTradeDateUpper(Timestamp tradeDateUpper) {
		this.tradeDateUpper = tradeDateUpper;
	}

	public Float getAccumuMainNetInPct() {
		return accumuMainNetInPct;
	}

	public void setAccumuMainNetInPct(Float accumuMainNetInPct) {
		this.accumuMainNetInPct = accumuMainNetInPct;
	}

}
