/**
 * 
 */
package com.wy.stock.service;

import java.sql.Timestamp;

/**
 * K线分析
 * @author leslie
 *
 */
public interface CandlestickService {
	
	StringBuilder morningStarAnalyse(Timestamp lowestTime);
	
	StringBuilder duskStarAnalyse(Timestamp lowestTime);
	
	StringBuilder crossStarAnalyse(Timestamp lowestTime);
	
	StringBuilder shootingStarAnalyse();
	
	StringBuilder pregnantLineAnalyse(Timestamp lowestTime);
	
	StringBuilder blackCloudCoverAnalyse();
}
