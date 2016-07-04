/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.dao.lessonteacher;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.school.entity.lessonteacher.SchoolLessonTeacher;

/**
 * 教师任课管理DAO接口
 * @author 王超然
 * @version 2016-05-31
 */
@MyBatisDao
public interface SchoolLessonTeacherDao extends CrudDao<SchoolLessonTeacher> {
	
}