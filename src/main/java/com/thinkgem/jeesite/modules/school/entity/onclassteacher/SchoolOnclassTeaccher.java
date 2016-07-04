/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.entity.onclassteacher;

import org.hibernate.validator.constraints.Length;
import com.thinkgem.jeesite.modules.sys.entity.User;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 教师考勤Entity
 * @author 何伟杰
 * @version 2016-06-20
 */
public class SchoolOnclassTeaccher extends DataEntity<SchoolOnclassTeaccher> {
	
	private static final long serialVersionUID = 1L;
	private String state;		// 考勤状态
	private String lessonId;		// 课程
	private String week;		// 第几周
	private User user;		// 用户
	
	public SchoolOnclassTeaccher() {
		super();
	}

	public SchoolOnclassTeaccher(String id){
		super(id);
	}

	@Length(min=0, max=255, message="考勤状态长度必须介于 0 和 255 之间")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	@Length(min=0, max=255, message="课程长度必须介于 0 和 255 之间")
	public String getLessonId() {
		return lessonId;
	}

	public void setLessonId(String lessonId) {
		this.lessonId = lessonId;
	}
	
	@Length(min=0, max=255, message="第几周长度必须介于 0 和 255 之间")
	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}