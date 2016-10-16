/**
 * 
 */
package com.wy.stock.tools;

import com.wy.stock.domain.AnalyseCondition;


/**
 * @author leslie
 *
 */
public interface AnalyseStockTool {
	
	void analyseAccumuChangePct(AnalyseCondition condition);
	
	void analyseAccumuMainNetIn(AnalyseCondition condition);
	
	void genNotionHotCsv(int year);
	
	void genIndustryHotCsv(int year);
	
	void genIndustryHotStockCsv(int year);
	
	void genNotionHotStockCsv(int year);
	
	StringBuilder getNotionHotPhraseString(String lowTradeDateStr, String highTradeDateStr, boolean header);
	
	StringBuilder getIndustryHotPhraseString(String lowTradeDateStr, String highTradeDateStr, boolean header);
	
	StringBuilder getIndustryHotStocksPhraseString(String lowTradeDateStr, String highTradeDateStr, boolean header);
	
	StringBuilder getNotionHotStocksPhraseString(String lowTradeDateStr, String highTradeDateStr, boolean header);
}
