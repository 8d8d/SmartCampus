/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.dao.onclassteacher;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.school.entity.onclassteacher.SchoolOnclassTeaccher;

/**
 * 教师考勤DAO接口
 * @author 何伟杰
 * @version 2016-06-20
 */
@MyBatisDao
public interface SchoolOnclassTeaccherDao extends CrudDao<SchoolOnclassTeaccher> {
	
}