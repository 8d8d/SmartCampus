/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.dao.homework;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.school.entity.homework.SchoolHomework;

/**
 * 作业DAO接口
 * @author 王超然
 * @version 2016-05-16
 */
@MyBatisDao
public interface SchoolHomeworkDao extends CrudDao<SchoolHomework> {
	
}