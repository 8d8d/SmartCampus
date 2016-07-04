/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.entity.homework;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 作业Entity
 * @author 王超然
 * @version 2016-05-16
 */
public class SchoolHomework extends DataEntity<SchoolHomework> {
	
	private static final long serialVersionUID = 1L;
	private String lessonId;		// 课节
	private String content;		// 作业
	
	public SchoolHomework() {
		super();
	}

	public SchoolHomework(String id){
		super(id);
	}

	@Length(min=0, max=64, message="课节长度必须介于 0 和 64 之间")
	public String getLessonId() {
		return lessonId;
	}

	public void setLessonId(String lessonId) {
		this.lessonId = lessonId;
	}
	
	@Length(min=0, max=255, message="作业长度必须介于 0 和 255 之间")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}