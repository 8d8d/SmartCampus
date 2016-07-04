/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.service.lessonstudent;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.school.entity.lessonstudent.SchoolLessonStudent;
import com.thinkgem.jeesite.modules.school.dao.lessonstudent.SchoolLessonStudentDao;

/**
 * 学生选课Service
 * @author 何伟杰
 * @version 2016-06-07
 */
@Service
@Transactional(readOnly = true)
public class SchoolLessonStudentService extends CrudService<SchoolLessonStudentDao, SchoolLessonStudent> {

	public SchoolLessonStudent get(String id) {
		return super.get(id);
	}
	
	public List<SchoolLessonStudent> findList(SchoolLessonStudent schoolLessonStudent) {
		//实现权限分割
		schoolLessonStudent.getSqlMap().put("dsf", dataScopeFilter(schoolLessonStudent.getCurrentUser(), "o", "u"));
		return super.findList(schoolLessonStudent);
	}
	
	public Page<SchoolLessonStudent> findPage(Page<SchoolLessonStudent> page, SchoolLessonStudent schoolLessonStudent) {
		//实现权限分割
		schoolLessonStudent.getSqlMap().put("dsf", dataScopeFilter(schoolLessonStudent.getCurrentUser(), "o", "u"));
		schoolLessonStudent.setPage(page);
		page.setList(super.findList(schoolLessonStudent));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(SchoolLessonStudent schoolLessonStudent) {
		super.save(schoolLessonStudent);
	}
	
	@Transactional(readOnly = false)
	public void delete(SchoolLessonStudent schoolLessonStudent) {
		super.delete(schoolLessonStudent);
	}
	
}