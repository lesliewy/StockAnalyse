/**
 * 
 */
package com.wy.stock.dao;

import java.util.List;

import com.wy.stock.domain.Config;


/**
 * 配置参数DAO.
 * @author leslie
 *
 */
public interface ConfigDao {
	
	List<Config> queryAllConfig();
	
}
