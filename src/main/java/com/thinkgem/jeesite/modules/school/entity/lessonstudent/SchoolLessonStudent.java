/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.entity.lessonstudent;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 学生选课Entity
 * @author 何伟杰
 * @version 2016-06-07
 */
public class SchoolLessonStudent extends DataEntity<SchoolLessonStudent> {
	
	private static final long serialVersionUID = 1L;
	private String studentid;		// studentid
	private String lessonid;		// lessonid
	
	public SchoolLessonStudent() {
		super();
	}

	public SchoolLessonStudent(String id){
		super(id);
	}

	@Length(min=0, max=255, message="studentid长度必须介于 0 和 255 之间")
	public String getStudentid() {
		return studentid;
	}

	public void setStudentid(String studentid) {
		this.studentid = studentid;
	}
	
	@Length(min=0, max=255, message="lessonid长度必须介于 0 和 255 之间")
	public String getLessonid() {
		return lessonid;
	}

	public void setLessonid(String lessonid) {
		this.lessonid = lessonid;
	}
	
}