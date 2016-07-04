/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.service.notice;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.school.entity.notice.SchoolNotice;
import com.thinkgem.jeesite.modules.school.dao.notice.SchoolNoticeDao;

/**
 * 校园公告Service
 * @author 王超然
 * @version 2016-05-12
 */
@Service
@Transactional(readOnly = true)
public class SchoolNoticeService extends CrudService<SchoolNoticeDao, SchoolNotice> {

	public SchoolNotice get(String id) {
		return super.get(id);
	}
	
	public List<SchoolNotice> findList(SchoolNotice schoolNotice) {
		//实现权限分割
		schoolNotice.getSqlMap().put("dsf", dataScopeFilter(schoolNotice.getCurrentUser(), "o", "u"));
		return super.findList(schoolNotice);
	}
	
	public Page<SchoolNotice> findPage(Page<SchoolNotice> page, SchoolNotice schoolNotice) {
		//实现权限分割
		schoolNotice.getSqlMap().put("dsf", dataScopeFilter(schoolNotice.getCurrentUser(), "o", "u"));
		schoolNotice.setPage(page);
		page.setList(super.findList(schoolNotice));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(SchoolNotice schoolNotice) {
		super.save(schoolNotice);
	}
	
	@Transactional(readOnly = false)
	public void delete(SchoolNotice schoolNotice) {
		super.delete(schoolNotice);
	}
	
}