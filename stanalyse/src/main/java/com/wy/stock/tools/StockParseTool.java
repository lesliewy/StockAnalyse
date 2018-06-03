/**
 * 
 */
package com.wy.stock.tools;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;

import com.wy.stock.domain.Index;
import com.wy.stock.domain.IndustryHot;
import com.wy.stock.domain.IndustryHotStocks;
import com.wy.stock.domain.NotionHot;
import com.wy.stock.domain.NotionHotStocks;
import com.wy.stock.domain.NotionInfo;
import com.wy.stock.domain.StockCapFlow;
import com.wy.stock.domain.ExchangeInfo;

/**
 * @author leslie
 *
 */
public interface StockParseTool {

	void getStockInfo();
	
	void parseHistCsv(File file, ExchangeInfo stockInfo, Map<String, String> maxDateMap) throws IOException;
	
	void parseAllHistCsv(File dir);
	
	List<NotionInfo> parseNotionInfoFromDoc(Document industryInfoDoc, String source);
	
	void downloadParseBoardCode(String source);
	
	List<NotionHot> parseNotionHotFromJson(File jsonFile);
	
	List<IndustryHot> parseIndustryHotFromJson(File jsonfile);
	
	List<NotionHotStocks> parseNotionHotStocksFromJson(File jsonFile);
	
	List<IndustryHotStocks> parseIndustryHotStocksFromJson(File jsonFile);
	
	List<Index> parseIndexFromJson(File jsonFile);
	
	List<StockCapFlow> parseStockCapFlow(File capFlowFile);
	
	List<StockCapFlow> parseStockCapFlowHist(File capFlowHtmlFile);
	
	void persistNotionHotStocksFromJson(String date);
	
	void persistIndustryHotStocksFromJson(String date);
	
	void persistIndexFromJson(File jsonFile);
	
	void persistNotionIndustryStockFromJson(File dir, String type);
	
	void persistStockFiveChange(File dir);
	
}
