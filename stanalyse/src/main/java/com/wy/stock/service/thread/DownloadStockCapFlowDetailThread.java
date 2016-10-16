/**
 * 
 */
package com.wy.stock.service.thread;

import java.io.File;

import org.apache.log4j.Logger;

import com.wy.stock.tools.StockDownloadTool;

/**
 * @author leslie
 *
 */
public class DownloadStockCapFlowDetailThread implements Runnable {

	private static Logger LOGGER = Logger.getLogger(DownloadStockCapFlowDetailThread.class
			.getName());
	
	private StockDownloadTool stockDownloadTool;
	
	private File dir;
	
	private String code;
	
	public void run() {
		stockDownloadTool.downloadStockCapFlowDetail(dir, code);
		LOGGER.info("download capFlowDetail success: " + code);
	}

	public StockDownloadTool getStockDownloadTool() {
		return stockDownloadTool;
	}

	public void setStockDownloadTool(StockDownloadTool stockDownloadTool) {
		this.stockDownloadTool = stockDownloadTool;
	}

	public File getDir() {
		return dir;
	}

	public void setDir(File dir) {
		this.dir = dir;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
