/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.dao.home;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.school.entity.home.SchoolHome;

/**
 * 首页DAO接口
 * @author 王超然
 * @version 2016-05-26
 */
@MyBatisDao
public interface SchoolHomeDao extends CrudDao<SchoolHome> {
	
}