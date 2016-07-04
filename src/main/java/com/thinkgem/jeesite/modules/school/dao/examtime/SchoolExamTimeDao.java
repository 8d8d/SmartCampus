/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.dao.examtime;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.school.entity.examtime.SchoolExamTime;

/**
 * 考试时间设置DAO接口
 * @author 何伟杰
 * @version 2016-06-12
 */
@MyBatisDao
public interface SchoolExamTimeDao extends CrudDao<SchoolExamTime> {
	
}