/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.service.agenda;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.school.entity.agenda.SchoolAgenda;
import com.thinkgem.jeesite.modules.school.dao.agenda.SchoolAgendaDao;

/**
 * 日程安排Service
 * @author 何伟杰
 * @version 2016-06-20
 */
@Service
@Transactional(readOnly = true)
public class SchoolAgendaService extends CrudService<SchoolAgendaDao, SchoolAgenda> {

	public SchoolAgenda get(String id) {
		return super.get(id);
	}
	
	public List<SchoolAgenda> findList(SchoolAgenda schoolAgenda) {
		//实现权限分割
		schoolAgenda.getSqlMap().put("dsf", dataScopeFilter(schoolAgenda.getCurrentUser(), "o", "u"));
		return super.findList(schoolAgenda);
	}
	
	public Page<SchoolAgenda> findPage(Page<SchoolAgenda> page, SchoolAgenda schoolAgenda) {
		//实现权限分割
		schoolAgenda.getSqlMap().put("dsf", dataScopeFilter(schoolAgenda.getCurrentUser(), "o", "u"));
		schoolAgenda.setPage(page);
		page.setList(super.findList(schoolAgenda));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(SchoolAgenda schoolAgenda) {
		super.save(schoolAgenda);
	}
	
	@Transactional(readOnly = false)
	public void delete(SchoolAgenda schoolAgenda) {
		super.delete(schoolAgenda);
	}
	
}