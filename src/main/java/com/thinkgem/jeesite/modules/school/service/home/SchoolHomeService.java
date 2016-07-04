/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.service.home;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.school.entity.home.SchoolHome;
import com.thinkgem.jeesite.modules.school.dao.home.SchoolHomeDao;

/**
 * 首页Service
 * @author 王超然
 * @version 2016-05-26
 */
@Service
@Transactional(readOnly = true)
public class SchoolHomeService extends CrudService<SchoolHomeDao, SchoolHome> {

	public SchoolHome get(String id) {
		return super.get(id);
	}
	
	public List<SchoolHome> findList(SchoolHome schoolHome) {
		//实现权限分割
		schoolHome.getSqlMap().put("dsf", dataScopeFilter(schoolHome.getCurrentUser(), "o", "u"));
		return super.findList(schoolHome);
	}
	
	public Page<SchoolHome> findPage(Page<SchoolHome> page, SchoolHome schoolHome) {
		//实现权限分割
		schoolHome.getSqlMap().put("dsf", dataScopeFilter(schoolHome.getCurrentUser(), "o", "u"));
		schoolHome.setPage(page);
		page.setList(super.findList(schoolHome));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(SchoolHome schoolHome) {
		super.save(schoolHome);
	}
	
	@Transactional(readOnly = false)
	public void delete(SchoolHome schoolHome) {
		super.delete(schoolHome);
	}
	
}