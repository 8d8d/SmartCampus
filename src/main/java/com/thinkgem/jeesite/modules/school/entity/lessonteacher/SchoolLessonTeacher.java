/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.entity.lessonteacher;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 教师任课管理Entity
 * @author 王超然
 * @version 2016-05-31
 */
public class SchoolLessonTeacher extends DataEntity<SchoolLessonTeacher> {
	
	private static final long serialVersionUID = 1L;
	private String teacherId;		// 教师
	private String classId;		// 班级
	private String lessonId;		// 科目
	
	private User user;
	
	public SchoolLessonTeacher() {
		super();
	}

	public SchoolLessonTeacher(String id){
		super(id);
	}
	
	public SchoolLessonTeacher(User user){
		this.teacherId = user.getId();
	}

	@Length(min=0, max=64, message="教师长度必须介于 0 和 64 之间")
	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	
	@Length(min=0, max=64, message="班级长度必须介于 0 和 64 之间")
	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}
	
	@Length(min=0, max=64, message="科目长度必须介于 0 和 64 之间")
	public String getLessonId() {
		return lessonId;
	}

	public void setLessonId(String lessonId) {
		this.lessonId = lessonId;
	}
	
}