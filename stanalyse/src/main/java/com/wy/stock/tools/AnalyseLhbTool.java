/**
 * 
 */
package com.wy.stock.tools;

import java.util.List;
import java.util.Map;

/**
 * @author leslie
 *
 */
public interface AnalyseLhbTool {

	String queryAggregateString(String lowTradeDateStr, String highTradeDateStr);

	Map<String, Map<String, String>> queryAggregate(String lowTradeDateStr,
			String highTradeDateStr);

	List<Map<String, String>> queryLhbStatics(String lowTradeDateStr,
			String highTradeDateStr);
}
