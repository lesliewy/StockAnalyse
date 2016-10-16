/**
 * 
 */
package com.wy.stock.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.pinyin4j.PinyinHelper;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.wy.stock.domain.CandleLine;
import com.wy.stock.domain.NotionHotStocks;
import com.wy.stock.domain.StockHistory;

/**
 * @author leslie
 *
 */
public class StockUtils {
	
	private static Logger LOGGER = Logger.getLogger(StockUtils.class
			.getName());
	
	public static ValueComparatorInteger ascMapComparatorInteger = new ValueComparatorInteger(true);
	
	public static ValueComparatorInteger descMapComparatorInteger = new ValueComparatorInteger(false);
	
	public static ValueComparatorFloat ascMapComparatorFloat = new ValueComparatorFloat(true);
	
	public static ValueComparatorFloat descMapComparatorFloat = new ValueComparatorFloat(false);
	
	public static NotionHotStocksComparatorFloat descNotionHotStocksComparatorFloat = new NotionHotStocksComparatorFloat(false);
	
	private static class ValueComparatorInteger implements Comparator<Map.Entry<String, Integer>> {
		// 默认升序;
		private boolean asc = true;
		ValueComparatorInteger(boolean isAsc){
			asc = isAsc;
		}
		public int compare(Map.Entry<String, Integer> mp1,
				Map.Entry<String, Integer> mp2) {
			if(asc){
				return mp1.getValue() - mp2.getValue();
			}
			return mp2.getValue() - mp1.getValue();
		}
	}
	
	private static class ValueComparatorFloat implements Comparator<Map.Entry<String, Float>> {
		// 默认升序;
		private boolean asc = true;
		ValueComparatorFloat(boolean isAsc){
			asc = isAsc;
		}
		public int compare(Map.Entry<String, Float> mp1,
				Map.Entry<String, Float> mp2) {
			if(asc){
				return mp1.getValue() - mp2.getValue() > 0 ? 1 : -1;
			}
			return mp1.getValue() - mp2.getValue() > 0 ? -1 : 1;
		}
	}
	
	private static class NotionHotStocksComparatorFloat implements Comparator<NotionHotStocks> {
		// 默认升序;
		private boolean asc = true;
		NotionHotStocksComparatorFloat(boolean isAsc){
			asc = isAsc;
		}
		public int compare(NotionHotStocks stocks1,
				NotionHotStocks stocks2) {
			if(asc){
				return stocks1.getChangePercent() - stocks2.getChangePercent() > 0 ? 1 : -1;
			}
			float diff = stocks1.getChangePercent() - stocks2.getChangePercent();
			if(diff > 0){
				return -1;
			}else if (diff == 0){
				return stocks1.getNotionName().compareTo(stocks2.getNotionName());
			}else {
				return 1;
			}
		}
	}
	
	/**
	 * 将 StockHistory 转换成 CandleLine
	 * @param stockHistory
	 * @return
	 */
	public static CandleLine translateStock2CandleLine(StockHistory stockHistory){
		CandleLine candleLine = new CandleLine();
		candleLine.setOpen(stockHistory.getOpen());
		candleLine.setClose(stockHistory.getClose());
		candleLine.setHigh(stockHistory.getHigh());
		candleLine.setLow(stockHistory.getLow());
		candleLine.setPeriod("D");
		candleLine.setPeriodValue(stockHistory.getTradeDate());
		candleLine.setVolumn(stockHistory.getVolumn());
		return candleLine;
	}
	
	/**
	 * 获取历史数据csv文件本地存储目录.
	 * @param 保存的文件类型   H - 历史数据(csv文件)   B-概念板块、行业板块热点    C-资金流向数据(json格式)
	 * @return
	 */
	public static String getDailyStockSaveDir(String type){
		// 构造目录中的时间部分.
		String timeStr = String.valueOf(Calendar.getInstance().get(Calendar.YEAR)) +
				File.separatorChar + 
				StringUtils.leftPad(String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1), 2, "0") +
				File.separatorChar + 
				StringUtils.leftPad(String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)), 2, "0") + 
				File.separatorChar;
		
		if("H".equalsIgnoreCase(type)){
			return StockConstant.HISTORY_FILE_PATH +
					StringUtils.leftPad(String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1), 2, "0") +
					File.separatorChar + 
					StringUtils.leftPad(String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)), 2, "0") + 
					File.separatorChar;
		}else if("B".equalsIgnoreCase(type)){
			return StockConstant.BOARD_HOT_FILE_PATH + timeStr;
					
		}else if("C".equalsIgnoreCase(type)){
			return StockConstant.CAP_FLOW_FILE_PATH + timeStr;
		}
		return null;
	}
	
	/**
	 * 读取文件内容
	 * 
	 * @param file
	 * @return
	 */
	public static String getFileContent(File file) {
		if(!file.exists()){
			LOGGER.error("file: " + file.getAbsolutePath() + " is empty.");
			return null;
		}
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (FileNotFoundException e) {
			LOGGER.error(e);
		} catch (IOException e) {
			LOGGER.error(e);
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				LOGGER.error(e);
			}

		}
		return sb.toString();
	}
	
	/**
	 * 将str存入file.
	 * 
	 * @param file
	 * @param str
	 */
	public static void persistByStr(File file, String str, boolean append) {
		BufferedWriter bw;
		if(append){
			try {
				FileWriter writer = new FileWriter(file, true);     
				if (!StringUtils.isEmpty(str)) {
					writer.write(str);
				}
				writer.close();
				writer = null;
			} catch (IOException e) {
				LOGGER.error(e);
			}
		}else{
			try {
				bw = new BufferedWriter(new FileWriter(file));
				if (!StringUtils.isEmpty(str)) {
					bw.append(str);
					bw.flush();
				}
				bw.close();
				bw = null;
			} catch (IOException e) {
				LOGGER.error(e);
			}
		}
	}
	
	/**
	 * 将UTF编码转成汉字.
	 * @param utfString
	 * @return
	 */
	public static String convertUtfString(String utfString){
	    StringBuilder sb = new StringBuilder();  
	    int i = -1;  
	    int pos = 0;  
	      
	    while((i=utfString.indexOf("\\u", pos)) != -1){  
	        sb.append(utfString.substring(pos, i));  
	        if(i+5 < utfString.length()){  
	            pos = i+6;  
	            sb.append((char)Integer.parseInt(utfString.substring(i+2, i+6), 16));  
	        }  
	    }  
	    return sb.toString();  
	}
	
	public static Map<String, String> getIndexCodeMap(){
		Map<String, String> result = new HashMap<String, String>();
		result.put("创业板指", "399006");
		result.put("深证A指", "399107");
		result.put("深证综指", "399106");
		result.put("深证成指", "399001");
		result.put("中小板综", "399101");
		result.put("成份A指", "399002");
		result.put("新指数", "399100");
		result.put("上证380", "1B0009");
		result.put("B股指数", "1A0003");
		result.put("综合指数", "1B0006");
		result.put("上证指数", "1A0001");
		result.put("新综指", "999009");
		result.put("A股指数", "1A0002");
		result.put("沪深300", "1B0300");
		result.put("上证180", "1B0007");
		result.put("上证50", "1B0016");
		result.put("深证B指", "399108");
		result.put("成份B指", "399003");
		return result;
	}
	
	public static boolean isWeekend(Calendar cal){
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
	}
	
	public static List<File> listFilesRec(File dir, com.wy.stock.utils.FileNameSelector selector, List<File> result){
        if(dir!=null){
            if(dir.isDirectory()){
                File[] selectedFiles=dir.listFiles(selector);
                if(selectedFiles!= null && selectedFiles.length > 0){
                	result.addAll(Arrays.asList(selectedFiles));
                }
                
                File[] fileArray=dir.listFiles();
                if(fileArray!=null){
                    for (int i = 0; i < fileArray.length; i++) {
                        //递归调用
                    	listFilesRec(fileArray[i], selector, result);
                    }
                }
            }
        }
        return result;
    }
	
	 // 返回中文的首字母  
    public static String getPinYinHeadChar(String str) {  
        String convert = "";  
        for (int j = 0; j < str.length(); j++) {
            char word = str.charAt(j);  
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);  
            if (pinyinArray != null) {  
                convert += pinyinArray[0].charAt(0);  
            } else {  
                convert += word;  
            }  
        }
        return convert;  
    }
    
	public static void writeToFile(String file, StringBuilder sb, String encoding){
		BufferedWriter writer = null;
		/*
		 * 使用GB2312编码，方便在Windows中打开
		 */
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), encoding));
			writer.append(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			try{
				if(writer != null){
					writer.close();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	public static List<String> getTradeDateLimit(){
		List<String> list = new ArrayList<String>();
		list.add("2015-06-15_2015-07-08");
		list.add("2015-07-09_2015-08-19");
		list.add("2015-07-09_2015-07-24");
		list.add("2015-07-28_2015-08-17");
		list.add("2015-08-20_2015-08-26");
		list.add("2015-08-27_2015-09-30");
		list.add("2015-10-08_2015-11-26");
		list.add("2015-10-08_2015-11-09");
		list.add("2015-11-30_2015-12-22");
		list.add("2015-11-10_2015-12-31");
		list.add("2016-01-04_2016-01-28");
		list.add("2016-01-29_2016-03-11");
		list.add("2016-01-29_2016-02-22");
		list.add("2016-03-01_2016-03-11");
		list.add("2016-03-14_2016-04-14");
		list.add("2016-04-21_2016-05-05");
		list.add("2016-05-06_2016-05-11");
		list.add("2016-05-12_2016-05-30");
		list.add("2016-05-31_2016-06-03");
		list.add("2016-06-06_2016-06-14");
		list.add("2016-06-16_2016-06-23");
		list.add("2016-06-27_2016-07-14");
		list.add("2016-08-12_2016-08-19");
		list.add("2016-08-23_2016-09-08");
		return list;
	}
	
	public static List<String> getTradeDateLimitAdd(){
		List<String> list = new ArrayList<String>();
		list.add("2015-10-08_2015-10-20");
		list.add("2015-10-22_2015-11-03");
		list.add("2015-11-04_2015-11-09");
		list.add("2015-11-10_2015-11-26");
		
		list.add("2016-01-04_2016-01-13");
		list.add("2016-01-14_2016-01-25");
		list.add("2016-01-26_2016-01-28");
		
		list.add("2016-03-14_2016-03-21");
		list.add("2016-03-22_2016-03-29");
		list.add("2016-03-30_2016-04-06");
		list.add("2016-04-08_2016-04-14");
		list.add("2016-04-15_2016-04-21");
		
		list.add("2016-05-12_2016-05-16");
		list.add("2016-05-12_2016-05-18");
		list.add("2016-05-19_2016-05-23");
		list.add("2016-05-19_2016-05-26");
		
		list.add("2016-05-10_2016-05-11");
		
		list.add("2016-06-16_2016-06-23");
		
		list.add("2016-06-27_2016-07-01");
		list.add("2016-07-04_2016-07-07");
		
		list.add("2016-07-12_2016-07-26");
		list.add("2016-08-02_2016-08-11");
		list.add("2016-09-09_9999-99-99");
		return list;
	}
}
