/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.dao.classroom;

import com.thinkgem.jeesite.common.persistence.TreeDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.school.entity.classroom.SchoolClassroom;

/**
 * 教室名称DAO接口
 * @author 王
 * @version 2016-05-10
 */
@MyBatisDao
public interface SchoolClassroomDao extends TreeDao<SchoolClassroom> {
	
}