/**
 * 
 */
package com.wy.stock.domain;

import java.sql.Timestamp;

/**
 * ST_STOCKS_INFO
 * @author leslie
 *
 */
public class StocksInfo {
	
	private Timestamp tradeDate;
	
	private String code;
	
	private float turnoverRate;
	
	private float volumnRate;
	
	private float amplitude;
	
	private float volumnAmount;
	
	private float tradableNum;
	
	private float tradableAmount;
	
	private float peRatio;
	
	private Timestamp timestamp;

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

	public float getTurnoverRate() {
		return turnoverRate;
	}

	public void setTurnoverRate(float turnoverRate) {
		this.turnoverRate = turnoverRate;
	}

	public float getVolumnRate() {
		return volumnRate;
	}

	public void setVolumnRate(float volumnRate) {
		this.volumnRate = volumnRate;
	}

	public float getAmplitude() {
		return amplitude;
	}

	public void setAmplitude(float amplitude) {
		this.amplitude = amplitude;
	}

	public float getVolumnAmount() {
		return volumnAmount;
	}

	public void setVolumnAmount(float volumnAmount) {
		this.volumnAmount = volumnAmount;
	}

	public float getTradableNum() {
		return tradableNum;
	}

	public void setTradableNum(float tradableNum) {
		this.tradableNum = tradableNum;
	}

	public float getTradableAmount() {
		return tradableAmount;
	}

	public void setTradableAmount(float tradableAmount) {
		this.tradableAmount = tradableAmount;
	}

	public float getPeRatio() {
		return peRatio;
	}

	public void setPeRatio(float peRatio) {
		this.peRatio = peRatio;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	
}
