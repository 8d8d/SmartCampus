/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.service.lessonteacher;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.school.entity.lessonteacher.SchoolLessonTeacher;
import com.thinkgem.jeesite.modules.school.dao.lessonteacher.SchoolLessonTeacherDao;

/**
 * 教师任课管理Service
 * @author 王超然
 * @version 2016-05-31
 */
@Service
@Transactional(readOnly = true)
public class SchoolLessonTeacherService extends CrudService<SchoolLessonTeacherDao, SchoolLessonTeacher> {

	public SchoolLessonTeacher get(String id) {
		return super.get(id);
	}
	
	public List<SchoolLessonTeacher> findList(SchoolLessonTeacher schoolLessonTeacher) {
		//实现权限分割
		schoolLessonTeacher.getSqlMap().put("dsf", dataScopeFilter(schoolLessonTeacher.getCurrentUser(), "o", "u"));
		return super.findList(schoolLessonTeacher);
	}
	
	public Page<SchoolLessonTeacher> findPage(Page<SchoolLessonTeacher> page, SchoolLessonTeacher schoolLessonTeacher) {
		//实现权限分割
		schoolLessonTeacher.getSqlMap().put("dsf", dataScopeFilter(schoolLessonTeacher.getCurrentUser(), "o", "u"));
		schoolLessonTeacher.setPage(page);
		page.setList(super.findList(schoolLessonTeacher));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(SchoolLessonTeacher schoolLessonTeacher) {
		super.save(schoolLessonTeacher);
	}
	
	@Transactional(readOnly = false)
	public void delete(SchoolLessonTeacher schoolLessonTeacher) {
		super.delete(schoolLessonTeacher);
	}
	
}