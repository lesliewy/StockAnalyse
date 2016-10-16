/**
 * 
 */
package com.wy.stock.tools;

import java.io.File;

/**
 * @author leslie
 *
 */
public interface StockDownloadTool {
	
	void downloadCsvFiles(File savedDir);
	
	void downloadBoardHotFiles(File savedDir, String type);
	
	void downloadBoardHotStocksFiles(String date, String type);
	
	void downloadIndexFiles();
	
	void downloadBoardHotFilesAll(File savedDir, String type);
	
	void downloadStockFiveChange(String type);
	
	File downloadStockCapFlow(File dir);
	
	File downloadStockCapFlowHist(File dir, String code);
	
	File downloadStockCapFlowDetail(File dir, String code);
}
