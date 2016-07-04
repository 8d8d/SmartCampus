/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.dao.lessonstudent;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.school.entity.lessonstudent.SchoolLessonStudent;

/**
 * 学生选课DAO接口
 * @author 何伟杰
 * @version 2016-06-07
 */
@MyBatisDao
public interface SchoolLessonStudentDao extends CrudDao<SchoolLessonStudent> {
	
}