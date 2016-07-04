/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.service.examtime;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.school.entity.examtime.SchoolExamTime;
import com.thinkgem.jeesite.modules.school.dao.examtime.SchoolExamTimeDao;

/**
 * 考试时间设置Service
 * @author 何伟杰
 * @version 2016-06-12
 */
@Service
@Transactional(readOnly = true)
public class SchoolExamTimeService extends CrudService<SchoolExamTimeDao, SchoolExamTime> {

	public SchoolExamTime get(String id) {
		return super.get(id);
	}
	
	public List<SchoolExamTime> findList(SchoolExamTime schoolExamTime) {
		//实现权限分割
		schoolExamTime.getSqlMap().put("dsf", dataScopeFilter(schoolExamTime.getCurrentUser(), "o", "u"));
		return super.findList(schoolExamTime);
	}
	
	public Page<SchoolExamTime> findPage(Page<SchoolExamTime> page, SchoolExamTime schoolExamTime) {
		//实现权限分割
		schoolExamTime.getSqlMap().put("dsf", dataScopeFilter(schoolExamTime.getCurrentUser(), "o", "u"));
		schoolExamTime.setPage(page);
		page.setList(super.findList(schoolExamTime));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(SchoolExamTime schoolExamTime) {
		super.save(schoolExamTime);
	}
	
	@Transactional(readOnly = false)
	public void delete(SchoolExamTime schoolExamTime) {
		super.delete(schoolExamTime);
	}
	
}