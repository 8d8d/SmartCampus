/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.service.news;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.school.entity.news.SchoolNews;
import com.thinkgem.jeesite.modules.school.dao.news.SchoolNewsDao;

/**
 * 校园新闻Service
 * @author 王超然
 * @version 2016-05-31
 */
@Service
@Transactional(readOnly = true)
public class SchoolNewsService extends CrudService<SchoolNewsDao, SchoolNews> {

	public SchoolNews get(String id) {
		return super.get(id);
	}
	
	public List<SchoolNews> findList(SchoolNews schoolNews) {
		schoolNews.getSqlMap().put("dsf", dataScopeFilter(schoolNews.getCurrentUser(), "o", "u"));
		//实现权限分割
		return super.findList(schoolNews);
	}
	
	public Page<SchoolNews> findPage(Page<SchoolNews> page, SchoolNews schoolNews) {
		schoolNews.getSqlMap().put("dsf", dataScopeFilter(schoolNews.getCurrentUser(), "o", "u"));
		//实现权限分割
		schoolNews.setPage(page);
		page.setList(super.findList(schoolNews));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(SchoolNews schoolNews) {
		super.save(schoolNews);
	}
	
	@Transactional(readOnly = false)
	public void delete(SchoolNews schoolNews) {
		super.delete(schoolNews);
	}
	
}