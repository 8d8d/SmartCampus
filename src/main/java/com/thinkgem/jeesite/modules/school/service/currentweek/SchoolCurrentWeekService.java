/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.service.currentweek;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.school.entity.currentweek.SchoolCurrentWeek;
import com.thinkgem.jeesite.modules.school.dao.currentweek.SchoolCurrentWeekDao;

/**
 * 设置当前周Service
 * @author 何伟杰
 * @version 2016-06-06
 */
@Service
@Transactional(readOnly = true)
public class SchoolCurrentWeekService extends CrudService<SchoolCurrentWeekDao, SchoolCurrentWeek> {

	public SchoolCurrentWeek get(String id) {
		return super.get(id);
	}
	
	public List<SchoolCurrentWeek> findList(SchoolCurrentWeek schoolCurrentWeek) {
		//实现权限分割
		schoolCurrentWeek.getSqlMap().put("dsf", dataScopeFilter(schoolCurrentWeek.getCurrentUser(), "o", "u"));
		return super.findList(schoolCurrentWeek);
	}
	
	public Page<SchoolCurrentWeek> findPage(Page<SchoolCurrentWeek> page, SchoolCurrentWeek schoolCurrentWeek) {
		//实现权限分割
		schoolCurrentWeek.getSqlMap().put("dsf", dataScopeFilter(schoolCurrentWeek.getCurrentUser(), "o", "u"));
		schoolCurrentWeek.setPage(page);
		page.setList(super.findList(schoolCurrentWeek));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(SchoolCurrentWeek schoolCurrentWeek) {
		super.save(schoolCurrentWeek);
	}
	
	@Transactional(readOnly = false)
	public void delete(SchoolCurrentWeek schoolCurrentWeek) {
		super.delete(schoolCurrentWeek);
	}
	
}