/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.service.leave;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.school.entity.leave.SchoolLeave;
import com.thinkgem.jeesite.modules.school.dao.leave.SchoolLeaveDao;

/**
 * 请假审批Service
 * @author 王超然
 * @version 2016-05-12
 */
@Service
@Transactional(readOnly = true)
public class SchoolLeaveService extends CrudService<SchoolLeaveDao, SchoolLeave> {

	public SchoolLeave get(String id) {
		return super.get(id);
	}
	
	public List<SchoolLeave> findList(SchoolLeave schoolLeave) {
		//实现权限分割
		schoolLeave.getSqlMap().put("dsf", dataScopeFilter(schoolLeave.getCurrentUser(), "o", "u"));
		return super.findList(schoolLeave);
	}
	
	public Page<SchoolLeave> findPage(Page<SchoolLeave> page, SchoolLeave schoolLeave) {
		//实现权限分割
		schoolLeave.getSqlMap().put("dsf", dataScopeFilter(schoolLeave.getCurrentUser(), "o", "u"));
		schoolLeave.setPage(page);
		page.setList(super.findList(schoolLeave));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(SchoolLeave schoolLeave) {
		super.save(schoolLeave);
	}
	
	@Transactional(readOnly = false)
	public void delete(SchoolLeave schoolLeave) {
		super.delete(schoolLeave);
	}
	
}