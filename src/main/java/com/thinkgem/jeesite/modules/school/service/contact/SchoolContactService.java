/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.service.contact;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.TreeService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.school.entity.contact.SchoolContact;
import com.thinkgem.jeesite.modules.school.dao.contact.SchoolContactDao;

/**
 * 联系人设置Service
 * @author 何伟杰
 * @version 2016-06-22
 */
@Service
@Transactional(readOnly = true)
public class SchoolContactService extends TreeService<SchoolContactDao, SchoolContact> {

	public SchoolContact get(String id) {
		return super.get(id);
	}
	
	public List<SchoolContact> findList(SchoolContact schoolContact) {
		schoolContact.getSqlMap().put("dsf", dataScopeFilter(schoolContact.getCurrentUser(), "o", "u"));
		if (StringUtils.isNotBlank(schoolContact.getParentIds())){
			schoolContact.setParentIds(","+schoolContact.getParentIds()+",");
		}
		return super.findList(schoolContact);
	}
	
	@Transactional(readOnly = false)
	public void save(SchoolContact schoolContact) {
		super.save(schoolContact);
	}
	
	@Transactional(readOnly = false)
	public void delete(SchoolContact schoolContact) {
		super.delete(schoolContact);
	}
	
}