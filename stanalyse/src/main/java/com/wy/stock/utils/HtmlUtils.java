      
/*
 * FileName: HtmlUtils.java
 * Author:   leslie
 * Date:     Jun 16, 2016 6:07:58 PM
 * Description: //模块目的、功能描述      
 */
package com.wy.stock.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


/**
 *
 * @author leslie
 */
public class HtmlUtils {

	
	private static Logger LOGGER = Logger.getLogger(HtmlUtils.class
			.getName());
	
	/**
	 * 将从i问财上下载的文件转为文本格式(csv).
	 * 后缀名虽然是xls, 实际上是html文件. 
	 * 下载后的xls，不要再打开，覆盖成了真正的excel文件了.
	 *
	 * @param dir
	 */
	public static void transTHSNotionHotStocksToCsv(File dir){
		com.wy.stock.utils.FileNameSelector selector = new com.wy.stock.utils.FileNameSelector(
				StockConstant.NOTION_IDENTIFIER + "_", ".xls");
		List<File> excelFiles = new ArrayList<File>();
		StockUtils.listFilesRec(dir, selector, excelFiles);
		if (excelFiles == null || excelFiles.size() == 0) {
			return;
		}
		
		String CSV_SPLIT = ",";
		int count = 0;
		Document doc = null;
		StringBuilder sb = null;
		for (File excelFile : excelFiles) {
			LOGGER.info(++count + " processing file: " + excelFile.getAbsolutePath());
			String excelName = excelFile.getName();
			String csvFileStr = excelFile.getParent() + "/" + excelName.replace(".xls", ".csv");
			File csvFile = new File(csvFileStr);
			// 已存在的跳过
			if(csvFile.exists()){
				continue;
			}
			
			doc = Jsoup.parse(StockUtils.getFileContent(excelFile));
			if(doc == null){
				LOGGER.error("doc is null, return now...");
				return;
			}
			Iterator<Element> trIter = doc.select("body > table > tbody > tr").iterator();
			if(trIter == null){
				LOGGER.error("trIter is null, reutrn null now...");
				return;
			}
			
			sb = new StringBuilder("");
			int lineNum = 0;
			while(trIter.hasNext()){
				Element trElement = trIter.next();
				// 第一列.  股票代码: body > table > tbody > tr:nth-child(1) > td:nth-child(1)
				String code = trElement.select("> td:nth-child(1)").text();
				if(lineNum != 0 && !code.endsWith(".SZ") && !code.endsWith(".SH")){
					continue;
				}
				sb.append(code);
				sb.append(CSV_SPLIT);
				// 第二列  股票简称: body > table > tbody > tr:nth-child(1) > td:nth-child(2)
				sb.append(trElement.select("> td:nth-child(2)").text());
				sb.append(CSV_SPLIT);
				// 第三列  区间涨跌幅: body > table > tbody > tr:nth-child(1) > td:nth-child(3)
				// 区间涨跌幅(%) 2016.03.08
				String str3 = trElement.select("> td:nth-child(3)").text();
				if(lineNum == 0){
					String[] strArr = str3.split(" ");
					str3 = strArr[0] + "\n" + CSV_SPLIT + CSV_SPLIT + strArr[1];
				}
				sb.append(str3);
				
				sb.append("\n");
				lineNum++;
			}
			
			// 生成 csv 文件.
			StockUtils.writeToFile(csvFileStr, sb, "UTF-8");
		}
	}
	
}

   