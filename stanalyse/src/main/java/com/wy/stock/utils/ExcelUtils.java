/*
 * FileName: ExcelUtils.java
 * Author:   leslie
 * Date:     Jun 16, 2016 4:59:27 PM
 * Description: //模块目的、功能描述      
 */

package com.wy.stock.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.log4j.Logger;

/**
 * 
 * @author leslie
 */

public class ExcelUtils {
	
	private static Logger LOGGER = Logger.getLogger(ExcelUtils.class
			.getName());

	/**
	 * 读取excel文件，并将其转为文本格式(csv).
	 * 从i问财上下载的excel文件不能用jxl来读取，下载的xls格式的实际上是html文件.
	 *
	 * @param dir
	 */
	public static void transTHSNotionHotStocksToCsv(File dir) {
		jxl.Workbook readwb = null;
		InputStream instream = null;
		String CSV_SPLIT = ",";
		try {
			com.wy.stock.utils.FileNameSelector selector = new com.wy.stock.utils.FileNameSelector(
					StockConstant.NOTION_IDENTIFIER + "_", ".xls");
			List<File> excelFiles = new ArrayList<File>();
			StockUtils.listFilesRec(dir, selector, excelFiles);
			if (excelFiles == null || excelFiles.size() == 0) {
				return;
			}

			// 构建Workbook对象, 只读Workbook对象
			String excelName = "";
			int count = 0;
			StringBuilder sb = null;
			for (File excelFile : excelFiles) {
				LOGGER.info(++count + " processing file: " + excelFile.getAbsolutePath());
				
				instream = new FileInputStream(excelFile);
				readwb = Workbook.getWorkbook(instream);
				excelName = excelFile.getName();
				String csvFileStr = excelFile.getParent() + "/" + excelName.replace(".xls", ".csv");
				File csvFile = new File(csvFileStr);
				// 删除已存在的.
				if(csvFile.exists()){
					csvFile.delete();
				}
				
				// 获取第一张Sheet表 Sheet的下标是从0开始
				Sheet readsheet = readwb.getSheet(0);
				// 获取Sheet表中所包含的总行数
				int rsRows = readsheet.getRows();
				sb = new StringBuilder("");
				// 获取指定单元格的对象引用
				for (int i = 0; i < rsRows; i++) {
					for (int j = 0; j < 3; j++) {
						Cell cell = readsheet.getCell(j, i);
						String content = cell.getContents();
						sb.append(content).append(CSV_SPLIT);
					}
					// 去掉最后一个","
					sb.deleteCharAt(sb.length() - 1);
					sb.append("\n");
				}

				// 生成 csv 文件.
				StockUtils.writeToFile(csvFileStr, sb, "UTF-8");
			}
		} catch (Exception e) {
			LOGGER.info(e);
		} finally {
			if(readwb != null){
				readwb.close();
			}
			try{
				if(instream != null){
					instream.close();
				}
			} catch (Exception e){
				LOGGER.info(e);
			}
		}
	}

}
