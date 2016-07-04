/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.service.calendar;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.school.entity.calendar.SchoolCalendar;
import com.thinkgem.jeesite.modules.school.dao.calendar.SchoolCalendarDao;

/**
 * 校历Service
 * @author 王
 * @version 2016-05-31
 */
@Service
@Transactional(readOnly = true)
public class SchoolCalendarService extends CrudService<SchoolCalendarDao, SchoolCalendar> {

	public SchoolCalendar get(String id) {
		return super.get(id);
	}
	
	public List<SchoolCalendar> findList(SchoolCalendar schoolCalendar) {
		//实现权限分割
		schoolCalendar.getSqlMap().put("dsf", dataScopeFilter(schoolCalendar.getCurrentUser(), "o", "u"));
		return super.findList(schoolCalendar);
	}
	
	public Page<SchoolCalendar> findPage(Page<SchoolCalendar> page, SchoolCalendar schoolCalendar) {
		//实现权限分割
		schoolCalendar.getSqlMap().put("dsf", dataScopeFilter(schoolCalendar.getCurrentUser(), "o", "u"));
		schoolCalendar.setPage(page);
		page.setList(super.findList(schoolCalendar));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(SchoolCalendar schoolCalendar) {
		super.save(schoolCalendar);
	}
	
	@Transactional(readOnly = false)
	public void delete(SchoolCalendar schoolCalendar) {
		super.delete(schoolCalendar);
	}
	
}