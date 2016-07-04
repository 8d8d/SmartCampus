/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.dao.calendar;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.school.entity.calendar.SchoolCalendar;

/**
 * 校历DAO接口
 * @author 王
 * @version 2016-05-31
 */
@MyBatisDao
public interface SchoolCalendarDao extends CrudDao<SchoolCalendar> {
	
}