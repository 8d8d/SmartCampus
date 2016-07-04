/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.service.exam;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.school.entity.exam.SchoolExam;
import com.thinkgem.jeesite.modules.school.dao.exam.SchoolExamDao;

/**
 * 成绩查询Service
 * @author 王超然
 * @version 2016-05-16
 */
@Service
@Transactional(readOnly = true)
public class SchoolExamService extends CrudService<SchoolExamDao, SchoolExam> {

	public SchoolExam get(String id) {
		return super.get(id);
	}
	
	public List<SchoolExam> findList(SchoolExam schoolExam) {
		schoolExam.getSqlMap().put("dsf", dataScopeFilter(schoolExam.getCurrentUser(), "o", "u"));
		//实现权限分割
		return super.findList(schoolExam);
	}
	
	public Page<SchoolExam> findPage(Page<SchoolExam> page, SchoolExam schoolExam) {
		schoolExam.getSqlMap().put("dsf", dataScopeFilter(schoolExam.getCurrentUser(), "o", "u"));
		//实现权限分割
		schoolExam.setPage(page);
		page.setList(super.findList(schoolExam));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(SchoolExam schoolExam) {
		super.save(schoolExam);
	}
	
	@Transactional(readOnly = false)
	public void delete(SchoolExam schoolExam) {
		super.delete(schoolExam);
	}
	
}