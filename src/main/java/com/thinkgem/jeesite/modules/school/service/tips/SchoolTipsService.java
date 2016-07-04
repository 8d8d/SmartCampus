/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.service.tips;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.school.entity.tips.SchoolTips;
import com.thinkgem.jeesite.modules.school.dao.tips.SchoolTipsDao;

/**
 * 意见反馈Service
 * @author 王超然
 * @version 2016-05-12
 */
@Service
@Transactional(readOnly = true)
public class SchoolTipsService extends CrudService<SchoolTipsDao, SchoolTips> {

	public SchoolTips get(String id) {
		return super.get(id);
	}
	
	public List<SchoolTips> findList(SchoolTips schoolTips) {
		//实现权限分割
		schoolTips.getSqlMap().put("dsf", dataScopeFilter(schoolTips.getCurrentUser(), "o", "u"));
		return super.findList(schoolTips);
	}
	
	public Page<SchoolTips> findPage(Page<SchoolTips> page, SchoolTips schoolTips) {
		//实现权限分割
		schoolTips.getSqlMap().put("dsf", dataScopeFilter(schoolTips.getCurrentUser(), "o", "u"));
		schoolTips.setPage(page);
		page.setList(super.findList(schoolTips));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(SchoolTips schoolTips) {
		super.save(schoolTips);
	}
	
	@Transactional(readOnly = false)
	public void delete(SchoolTips schoolTips) {
		super.delete(schoolTips);
	}
	
}