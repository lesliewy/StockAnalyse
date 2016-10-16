/**
 * 
 */
package com.wy.stock.dao.impl;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.wy.stock.dao.ConfigDao;
import com.wy.stock.domain.Config;


/**
 * @author leslie
 *
 */
public class ConfigDaoImpl extends SqlMapClientDaoSupport implements ConfigDao {

	@SuppressWarnings("unchecked")
	public List<Config> queryAllConfig() {
		return getSqlMapClientTemplate().queryForList("queryAllConfig");
	}

}
