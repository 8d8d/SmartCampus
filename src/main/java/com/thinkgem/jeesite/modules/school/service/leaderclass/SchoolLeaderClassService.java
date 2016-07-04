/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.service.leaderclass;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.school.entity.leaderclass.SchoolLeaderClass;
import com.thinkgem.jeesite.modules.school.dao.leaderclass.SchoolLeaderClassDao;

/**
 * 辅导员任教Service
 * @author 王超然
 * @version 2016-05-31
 */
@Service
@Transactional(readOnly = true)
public class SchoolLeaderClassService extends CrudService<SchoolLeaderClassDao, SchoolLeaderClass> {

	public SchoolLeaderClass get(String id) {
		return super.get(id);
	}
	
	public List<SchoolLeaderClass> findList(SchoolLeaderClass schoolLeaderClass) {
		//实现权限分割
		schoolLeaderClass.getSqlMap().put("dsf", dataScopeFilter(schoolLeaderClass.getCurrentUser(), "o", "u"));
		return super.findList(schoolLeaderClass);
	}
	
	public Page<SchoolLeaderClass> findPage(Page<SchoolLeaderClass> page, SchoolLeaderClass schoolLeaderClass) {
		//实现权限分割
		schoolLeaderClass.getSqlMap().put("dsf", dataScopeFilter(schoolLeaderClass.getCurrentUser(), "o", "u"));
		schoolLeaderClass.setPage(page);
		page.setList(super.findList(schoolLeaderClass));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(SchoolLeaderClass schoolLeaderClass) {
		super.save(schoolLeaderClass);
	}
	
	@Transactional(readOnly = false)
	public void delete(SchoolLeaderClass schoolLeaderClass) {
		super.delete(schoolLeaderClass);
	}
	
}