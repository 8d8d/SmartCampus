/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.dao.classtype;

import com.thinkgem.jeesite.common.persistence.TreeDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.school.entity.classtype.SchoolCType;

/**
 * 课程类型DAO接口
 * @author 王
 * @version 2016-05-10
 */
@MyBatisDao
public interface SchoolCTypeDao extends TreeDao<SchoolCType> {
	
}