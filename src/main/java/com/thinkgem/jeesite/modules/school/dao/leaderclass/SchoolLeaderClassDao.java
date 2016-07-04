/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.dao.leaderclass;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.school.entity.leaderclass.SchoolLeaderClass;

/**
 * 辅导员任教DAO接口
 * @author 王超然
 * @version 2016-05-31
 */
@MyBatisDao
public interface SchoolLeaderClassDao extends CrudDao<SchoolLeaderClass> {
	
}