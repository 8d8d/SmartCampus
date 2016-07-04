/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.entity.examtime;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 考试时间设置Entity
 * @author 何伟杰
 * @version 2016-06-12
 */
public class SchoolExamTime extends DataEntity<SchoolExamTime> {
	
	private static final long serialVersionUID = 1L;
	private String type;		// type
	private String term;		// term
	private String year;		// year
	
	public SchoolExamTime() {
		super();
	}

	public SchoolExamTime(String id){
		super(id);
	}

	@Length(min=0, max=255, message="type长度必须介于 0 和 255 之间")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Length(min=0, max=255, message="term长度必须介于 0 和 255 之间")
	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}
	
	@Length(min=0, max=255, message="year长度必须介于 0 和 255 之间")
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}
	
}