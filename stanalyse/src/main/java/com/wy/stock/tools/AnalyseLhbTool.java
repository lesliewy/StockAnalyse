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

	List<Map<String, String>> queryLhbStatics(String lowTradeDateStr,
			String highTradeDateStr);
}
