/**
 * 
 */
package com.wy.stock.tools;

import java.io.File;
import java.util.List;

import org.jsoup.nodes.Document;

import com.wy.stock.domain.IndustryHot;
import com.wy.stock.domain.IndustryHotStocks;
import com.wy.stock.domain.NotionHot;
import com.wy.stock.domain.NotionHotStocks;
import com.wy.stock.domain.NotionInfo;

/**
 * @author leslie
 *
 */
public interface StockParseToolTHS {

	
	List<NotionInfo> parseNotionInfoFromDoc(Document industryInfoDoc);
	
	void  persistNotionHotFromHtml(File html);
	
	List<IndustryHot> parseIndustryHotFromHtml(File jsonfile);
	
	List<NotionHotStocks> parseNotionHotStocksFromHtml(File html);
	
	List<NotionHot> parseNotionHotStocksFromStocksHtml(File html);
	
	List<IndustryHotStocks> parseIndustryHotStocksFromHtml(File html);
	
	void persistNotionIndustryHotFromStocksHtml(File htmlDir, String tradeDate);
	
	void persistNotionIndustryHotStocksFromhtml(String date, String type);
	
	void persistNotionIndustryHotStocksFromJson(String date, String type);
	
	void persistNotionIndustryInfo(File html, String type);
	
	void persistNotionIndustryHot(File html, String type);
	
	void persistNotionIndustryHot(File saveDir, int totalPages, String type);
	
	void persistIndexFromHtml(File html);
	
	int getNotionIndustryHotTotalPages(File html, String type);
	
	List<NotionHot> parseNotionHotFromCsv(File csvFile);
	
	List<IndustryHot> parseIndustryHotFromCsv(File csvFile);
	
	void persistNotionHotHistFromCsv(File dir);
	
	void persistIndustryHotHistFromHtml(File dir);
	
	List<NotionHotStocks> parseNotionHotStocksFromCsv(File csvFile);
	
	void persistNotionHotStocksHistFromCsv(File dir);
	
	void genTHSStocksChromeUrl(String date);
	
	void persistIndexHistFromHtml(File dir);
}
