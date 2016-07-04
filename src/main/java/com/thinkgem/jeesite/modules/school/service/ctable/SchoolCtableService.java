/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.service.ctable;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.school.entity.ctable.SchoolCtable;
import com.thinkgem.jeesite.modules.school.dao.ctable.SchoolCtableDao;

/**
 * 课程表Service
 * @author 王超然
 * @version 2016-05-12
 */
@Service
@Transactional(readOnly = true)
public class SchoolCtableService extends CrudService<SchoolCtableDao, SchoolCtable> {

	public SchoolCtable get(String id) {
		return super.get(id);
	}
	
	public List<SchoolCtable> findList(SchoolCtable schoolCtable) {
		//实现权限分割
		schoolCtable.getSqlMap().put("dsf", dataScopeFilter(schoolCtable.getCurrentUser(), "o", "u"));
		return super.findList(schoolCtable);
	}
	
	public Page<SchoolCtable> findPage(Page<SchoolCtable> page, SchoolCtable schoolCtable) {
		//实现权限分割
		schoolCtable.getSqlMap().put("dsf", dataScopeFilter(schoolCtable.getCurrentUser(), "o", "u"));
		schoolCtable.setPage(page);
		page.setList(super.findList(schoolCtable));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(SchoolCtable schoolCtable) {
		super.save(schoolCtable);
	}
	
	@Transactional(readOnly = false)
	public void delete(SchoolCtable schoolCtable) {
		super.delete(schoolCtable);
	}
	
}