/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.service.homework;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.school.entity.homework.SchoolHomework;
import com.thinkgem.jeesite.modules.school.dao.homework.SchoolHomeworkDao;

/**
 * 作业Service
 * @author 王超然
 * @version 2016-05-16
 */
@Service
@Transactional(readOnly = true)
public class SchoolHomeworkService extends CrudService<SchoolHomeworkDao, SchoolHomework> {

	public SchoolHomework get(String id) {
		return super.get(id);
	}
	
	public List<SchoolHomework> findList(SchoolHomework schoolHomework) {
		//实现权限分割
		schoolHomework.getSqlMap().put("dsf", dataScopeFilter(schoolHomework.getCurrentUser(), "o", "u"));
		return super.findList(schoolHomework);
	}
	
	public Page<SchoolHomework> findPage(Page<SchoolHomework> page, SchoolHomework schoolHomework) {
		//实现权限分割
		schoolHomework.getSqlMap().put("dsf", dataScopeFilter(schoolHomework.getCurrentUser(), "o", "u"));
		schoolHomework.setPage(page);
		page.setList(super.findList(schoolHomework));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(SchoolHomework schoolHomework) {
		super.save(schoolHomework);
	}
	
	@Transactional(readOnly = false)
	public void delete(SchoolHomework schoolHomework) {
		super.delete(schoolHomework);
	}
	
}