/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.service.classtype;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.TreeService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.school.entity.classtype.SchoolCType;
import com.thinkgem.jeesite.modules.school.dao.classtype.SchoolCTypeDao;

/**
 * 课程类型Service
 * @author 王
 * @version 2016-05-10
 */
@Service
@Transactional(readOnly = true)
public class SchoolCTypeService extends TreeService<SchoolCTypeDao, SchoolCType> {

	public SchoolCType get(String id) {
		return super.get(id);
	}
	
	public List<SchoolCType> findList(SchoolCType schoolCType) {
		//实现权限分割
		schoolCType.getSqlMap().put("dsf", dataScopeFilter(schoolCType.getCurrentUser(), "o", "u"));
		if (StringUtils.isNotBlank(schoolCType.getParentIds())){
			schoolCType.setParentIds(","+schoolCType.getParentIds()+",");
		}
		return super.findList(schoolCType);
	}
	
	@Transactional(readOnly = false)
	public void save(SchoolCType schoolCType) {
		super.save(schoolCType);
	}
	
	@Transactional(readOnly = false)
	public void delete(SchoolCType schoolCType) {
		super.delete(schoolCType);
	}
	
}