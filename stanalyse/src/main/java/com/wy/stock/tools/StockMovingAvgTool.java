/**
 * 
 */
package com.wy.stock.tools;

/**
 * @author leslie
 *
 */
public interface StockMovingAvgTool {
	
	void calcAllMovingAvg();
	
	void calcMovingAvg(String code, String exchange, String type, int period);
	
	void analyseMovingAvgSequence(String code, String exchange, String type, Integer[] periods, String seqType, int days);
	
}
