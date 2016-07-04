/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.service.lessontime;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.TreeService;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.school.entity.lessontime.SchoolLessonTime;
import com.thinkgem.jeesite.modules.school.dao.lessontime.SchoolLessonTimeDao;

/**
 * 上课时间表Service
 * @author 王超然
 * @version 2016-05-12
 */
@Service
@Transactional(readOnly = true)
public class SchoolLessonTimeService extends TreeService<SchoolLessonTimeDao, SchoolLessonTime> {

	public SchoolLessonTime get(String id) {
		return super.get(id);
	}
	
	public List<SchoolLessonTime> findList(SchoolLessonTime schoolLessonTime) {
		//实现权限分割
		schoolLessonTime.getSqlMap().put("dsf", dataScopeFilter(schoolLessonTime.getCurrentUser(), "o", "u"));
		if (StringUtils.isNotBlank(schoolLessonTime.getParentIds())){
			schoolLessonTime.setParentIds(","+schoolLessonTime.getParentIds()+",");
		}
		return super.findList(schoolLessonTime);
	}
	
	@Transactional(readOnly = false)
	public void save(SchoolLessonTime schoolLessonTime) {
		super.save(schoolLessonTime);
	}
	
	@Transactional(readOnly = false)
	public void delete(SchoolLessonTime schoolLessonTime) {
		super.delete(schoolLessonTime);
	}
	
}