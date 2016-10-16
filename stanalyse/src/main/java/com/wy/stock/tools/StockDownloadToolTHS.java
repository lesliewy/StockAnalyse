/**
 * 
 */
package com.wy.stock.tools;

import java.io.File;

/**
 * @author leslie
 *
 */
public interface StockDownloadToolTHS {
	
	void downloadBoardHotHtmlFiles(File savedDir, String type);
	
	void downloadBoardHotJsonFiles(File savedDir, int totalPages, String type);
	
	void downloadBoardHotStocksFiles(String date, String type);
	
	void downloadIndexFiles();
	
}
