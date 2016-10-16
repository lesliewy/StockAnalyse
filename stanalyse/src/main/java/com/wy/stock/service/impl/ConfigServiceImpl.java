/**
 * 
 */
package com.wy.stock.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wy.stock.dao.ConfigDao;
import com.wy.stock.domain.Config;
import com.wy.stock.service.ConfigService;


/**
 * 配置参数 service
 * 
 * @author leslie
 *
 */
public class ConfigServiceImpl implements ConfigService {

	private ConfigDao configDao;

	public List<Config> queryAllConfig() {
		return configDao.queryAllConfig();
	}
	
	public Map<String, String> queryAllConfigInMap() {
		List<Config> configList = queryAllConfig();
		if(configList == null){
			return null;
		}
		Map<String, String> result = new HashMap<String, String>();
		for(Config config : configList){
			result.put(config.getName(), config.getValue());
		}
		return result;
	}

	public ConfigDao getConfigDao() {
		return configDao;
	}

	public void setConfigDao(ConfigDao configDao) {
		this.configDao = configDao;
	}

}
