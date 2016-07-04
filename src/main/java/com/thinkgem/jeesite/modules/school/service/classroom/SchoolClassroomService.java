/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.service.classroom;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.TreeService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.school.entity.classroom.SchoolClassroom;
import com.thinkgem.jeesite.modules.school.dao.classroom.SchoolClassroomDao;

/**
 * 教室名称Service
 * @author 王
 * @version 2016-05-10
 */
@Service
@Transactional(readOnly = true)
public class SchoolClassroomService extends TreeService<SchoolClassroomDao, SchoolClassroom> {

	public SchoolClassroom get(String id) {
		return super.get(id);
	}
	
	public List<SchoolClassroom> findList(SchoolClassroom schoolClassroom) {
		//实现权限分割
		schoolClassroom.getSqlMap().put("dsf", dataScopeFilter(schoolClassroom.getCurrentUser(), "o", "u"));
		if (StringUtils.isNotBlank(schoolClassroom.getParentIds())){
			schoolClassroom.setParentIds(","+schoolClassroom.getParentIds()+",");
		}
		return super.findList(schoolClassroom);
	}
	
	@Transactional(readOnly = false)
	public void save(SchoolClassroom schoolClassroom) {
		super.save(schoolClassroom);
	}
	
	@Transactional(readOnly = false)
	public void delete(SchoolClassroom schoolClassroom) {
		super.delete(schoolClassroom);
	}
	
}