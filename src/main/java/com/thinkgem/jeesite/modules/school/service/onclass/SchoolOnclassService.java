/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.service.onclass;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.school.entity.onclass.SchoolOnclass;
import com.thinkgem.jeesite.modules.school.dao.onclass.SchoolOnclassDao;

/**
 * 考勤Service
 * @author 王超然
 * @version 2016-05-16
 */
@Service
@Transactional(readOnly = true)
public class SchoolOnclassService extends CrudService<SchoolOnclassDao, SchoolOnclass> {

	public SchoolOnclass get(String id) {
		return super.get(id);
	}
	
	public List<SchoolOnclass> findList(SchoolOnclass schoolOnclass) {
		//实现权限分割
		schoolOnclass.getSqlMap().put("dsf", dataScopeFilter(schoolOnclass.getCurrentUser(), "o", "u"));
		return super.findList(schoolOnclass);
	}
	
	public Page<SchoolOnclass> findPage(Page<SchoolOnclass> page, SchoolOnclass schoolOnclass) {
		//实现权限分割
		schoolOnclass.getSqlMap().put("dsf", dataScopeFilter(schoolOnclass.getCurrentUser(), "o", "u"));
		schoolOnclass.setPage(page);
		page.setList(super.findList(schoolOnclass));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(SchoolOnclass schoolOnclass) {
		super.save(schoolOnclass);
	}
	
	@Transactional(readOnly = false)
	public void delete(SchoolOnclass schoolOnclass) {
		super.delete(schoolOnclass);
	}
	
}