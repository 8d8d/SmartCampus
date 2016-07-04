/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.service.cardconsum;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.school.entity.cardconsum.SchoolCardConsum;
import com.thinkgem.jeesite.modules.school.dao.cardconsum.SchoolCardConsumDao;

/**
 * 一卡通消费记录Service
 * @author 何伟杰
 * @version 2016-06-16
 */
@Service
@Transactional(readOnly = true)
public class SchoolCardConsumService extends CrudService<SchoolCardConsumDao, SchoolCardConsum> {

	public SchoolCardConsum get(String id) {
		return super.get(id);
	}
	
	public List<SchoolCardConsum> findList(SchoolCardConsum schoolCardConsum) {
		//实现权限分割
		schoolCardConsum.getSqlMap().put("dsf", dataScopeFilter(schoolCardConsum.getCurrentUser(), "o", "u"));
		return super.findList(schoolCardConsum);
	}
	
	public Page<SchoolCardConsum> findPage(Page<SchoolCardConsum> page, SchoolCardConsum schoolCardConsum) {
		//实现权限分割
		schoolCardConsum.getSqlMap().put("dsf", dataScopeFilter(schoolCardConsum.getCurrentUser(), "o", "u"));
		schoolCardConsum.setPage(page);
		page.setList(super.findList(schoolCardConsum));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(SchoolCardConsum schoolCardConsum) {
		super.save(schoolCardConsum);
	}
	
	@Transactional(readOnly = false)
	public void delete(SchoolCardConsum schoolCardConsum) {
		super.delete(schoolCardConsum);
	}
	
}