/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.service.card;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.school.entity.card.SchoolCard;
import com.thinkgem.jeesite.modules.school.dao.card.SchoolCardDao;

/**
 * 一卡通信息Service
 * @author 何伟杰
 * @version 2016-06-16
 */
@Service
@Transactional(readOnly = true)
public class SchoolCardService extends CrudService<SchoolCardDao, SchoolCard> {

	public SchoolCard get(String id) {
		return super.get(id);
	}
	
	public List<SchoolCard> findList(SchoolCard schoolCard) {
		//实现权限分割
		schoolCard.getSqlMap().put("dsf", dataScopeFilter(schoolCard.getCurrentUser(), "o", "u"));
		return super.findList(schoolCard);
	}
	
	public Page<SchoolCard> findPage(Page<SchoolCard> page, SchoolCard schoolCard) {
		//实现权限分割
		schoolCard.getSqlMap().put("dsf", dataScopeFilter(schoolCard.getCurrentUser(), "o", "u"));
		schoolCard.setPage(page);
		page.setList(super.findList(schoolCard));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(SchoolCard schoolCard) {
		super.save(schoolCard);
	}
	
	@Transactional(readOnly = false)
	public void delete(SchoolCard schoolCard) {
		super.delete(schoolCard);
	}
	
}