/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.service.cardrecharge;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.school.entity.cardrecharge.SchoolCardRecharge;
import com.thinkgem.jeesite.modules.school.dao.cardrecharge.SchoolCardRechargeDao;

/**
 * 一卡通充值记录Service
 * @author 何伟杰
 * @version 2016-06-16
 */
@Service
@Transactional(readOnly = true)
public class SchoolCardRechargeService extends CrudService<SchoolCardRechargeDao, SchoolCardRecharge> {

	public SchoolCardRecharge get(String id) {
		return super.get(id);
	}
	
	public List<SchoolCardRecharge> findList(SchoolCardRecharge schoolCardRecharge) {
		//实现权限分割
		schoolCardRecharge.getSqlMap().put("dsf", dataScopeFilter(schoolCardRecharge.getCurrentUser(), "o", "u"));
		return super.findList(schoolCardRecharge);
	}
	
	public Page<SchoolCardRecharge> findPage(Page<SchoolCardRecharge> page, SchoolCardRecharge schoolCardRecharge) {
		//实现权限分割
		schoolCardRecharge.getSqlMap().put("dsf", dataScopeFilter(schoolCardRecharge.getCurrentUser(), "o", "u"));
		schoolCardRecharge.setPage(page);
		page.setList(super.findList(schoolCardRecharge));
		return page;
	}
	
	@Transactional(readOnly = false)
	public void save(SchoolCardRecharge schoolCardRecharge) {
		super.save(schoolCardRecharge);
	}
	
	@Transactional(readOnly = false)
	public void delete(SchoolCardRecharge schoolCardRecharge) {
		super.delete(schoolCardRecharge);
	}
	
}