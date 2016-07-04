/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.dao.exam;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.school.entity.exam.SchoolExam;

/**
 * 成绩查询DAO接口
 * @author 王超然
 * @version 2016-05-16
 */
@MyBatisDao
public interface SchoolExamDao extends CrudDao<SchoolExam> {
	
}