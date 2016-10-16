/**
 * 
 */
package com.wy.stock.service;

import java.util.List;
import java.util.Map;

import com.wy.stock.domain.Config;


/**
 * 配置参数 Service
 * @author leslie
 *
 */
public interface ConfigService {

	List<Config> queryAllConfig();
	
	Map<String, String> queryAllConfigInMap();
	
}
