/**
 * 
 */
package com.wy.stock.domain;

import java.sql.Timestamp;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.json.JSONObject;

/**
 * @author leslie
 *
 */
public class StockLhbDetail {

	private String tradeDate;

	private String code;

	private String stockName;

	private Float close;

	private Float changePercent;

	// 买入营业部、机构，金额等信息
	private JSONObject buyIn;

	// 卖出营业部、机构,金额等信息
	private JSONObject sellOut;

	// 龙虎榜的成交金额 = 合计买入 + 合计卖出;
	private Float volumnAmount;

	// 龙虎榜的买入金额
	private Float volumnAmountIn;

	// 龙虎榜的卖出金额
	private Float volumnAmountOut;

	// 龙虎榜的净金额
	private Float volumnAmountNet;

	// 上榜类型 取值: 1日， 3日
	private String lhType;

	// 上榜类型的详细信息;
	private String lhTypeDesc;

	private Timestamp timestamp;

	public String getTradeDate() {
		return tradeDate;
	}

	public String getCode() {
		return code;
	}

	public String getStockName() {
		return stockName;
	}

	public Float getClose() {
		return close;
	}

	public Float getChangePercent() {
		return changePercent;
	}

	public JSONObject getBuyIn() {
		return buyIn;
	}

	public JSONObject getSellOut() {
		return sellOut;
	}

	public Float getVolumnAmount() {
		return volumnAmount;
	}

	public Float getVolumnAmountIn() {
		return volumnAmountIn;
	}

	public Float getVolumnAmountOut() {
		return volumnAmountOut;
	}

	public Float getVolumnAmountNet() {
		return volumnAmountNet;
	}

	public String getLhType() {
		return lhType;
	}

	public String getLhTypeDesc() {
		return lhTypeDesc;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setStockName(String stockName) {
		this.stockName = stockName;
	}

	public void setClose(Float close) {
		this.close = close;
	}

	public void setChangePercent(Float changePercent) {
		this.changePercent = changePercent;
	}

	public void setBuyIn(JSONObject buyIn) {
		this.buyIn = buyIn;
	}

	public void setSellOut(JSONObject sellOut) {
		this.sellOut = sellOut;
	}

	public void setVolumnAmount(Float volumnAmount) {
		this.volumnAmount = volumnAmount;
	}

	public void setVolumnAmountIn(Float volumnAmountIn) {
		this.volumnAmountIn = volumnAmountIn;
	}

	public void setVolumnAmountOut(Float volumnAmountOut) {
		this.volumnAmountOut = volumnAmountOut;
	}

	public void setVolumnAmountNet(Float volumnAmountNet) {
		this.volumnAmountNet = volumnAmountNet;
	}

	public void setLhType(String lhType) {
		this.lhType = lhType;
	}

	public void setLhTypeDesc(String lhTypeDesc) {
		this.lhTypeDesc = lhTypeDesc;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
