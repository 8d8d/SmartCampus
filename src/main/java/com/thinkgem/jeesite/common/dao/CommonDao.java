/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.common.dao;

import java.util.List;
import java.util.Map;

import com.thinkgem.jeesite.common.persistence.BaseDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;

/**
 * 商家DAO接口
 * @author LVH
 * @version 2015-06-04
 */
@MyBatisDao("commonDao")
public interface CommonDao extends BaseDao {
	
	public List<Map<String, Object>> findListBySQL(String sql);
	
	public int updateBySQL(String sql);
	
	public int insertBySQL(String sql);
	
	public int deleteBySQL(String sql);
}