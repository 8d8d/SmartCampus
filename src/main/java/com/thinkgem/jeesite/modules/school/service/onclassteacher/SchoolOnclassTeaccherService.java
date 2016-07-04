/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.service.onclassteacher;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.school.entity.onclassteacher.SchoolOnclassTeaccher;
import com.thinkgem.jeesite.modules.school.dao.onclassteacher.SchoolOnclassTeaccherDao;

/**
 * 教师考勤Service
 * @author 何伟杰
 * @version 2016-06-20
 */
@Service
@Transactional(readOnly = true)
public class SchoolOnclassTeaccherService extends CrudService<SchoolOnclassTeaccherDao, SchoolOnclassTeaccher> {

	public SchoolOnclassTeaccher get(String id) {
		return super.get(id);
	}
	
	public List<SchoolOnclassTeaccher> findList(SchoolOnclassTeaccher schoolOnclassTeaccher) {
		//实现权限分割
		schoolOnclassTeaccher.getSqlMap().put("dsf", dataScopeFilter(schoolOnclassTeaccher.getCurrentUser(), "o", "u"));
		return super.findList(schoolOnclassTeaccher);
	}
	
	public Page<SchoolOnclassTeaccher> findPage(Page<SchoolOnclassTeaccher> page, SchoolOnclassTeaccher schoolOnclassTeaccher) {
		//实现权限分割
		schoolOnclassTeaccher.getSqlMap().put("dsf", dataScopeFilter(schoolOnclassTeaccher.getCurrentUser(), "o", "u"));
		schoolOnclassTeaccher.setPage(page);
		page.setList(super.findList(schoolOnclassTeaccher));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(SchoolOnclassTeaccher schoolOnclassTeaccher) {
		super.save(schoolOnclassTeaccher);
	}
	
	@Transactional(readOnly = false)
	public void delete(SchoolOnclassTeaccher schoolOnclassTeaccher) {
		super.delete(schoolOnclassTeaccher);
	}
	
}