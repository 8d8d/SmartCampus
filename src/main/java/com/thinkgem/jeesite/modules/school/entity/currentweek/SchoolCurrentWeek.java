/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.entity.currentweek;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 设置当前周Entity
 * @author 何伟杰
 * @version 2016-06-06
 */
public class SchoolCurrentWeek extends DataEntity<SchoolCurrentWeek> {
	
	private static final long serialVersionUID = 1L;
	private String week;		// week
	
	public SchoolCurrentWeek() {
		super();
	}

	public SchoolCurrentWeek(String id){
		super(id);
	}

	@Length(min=0, max=255, message="week长度必须介于 0 和 255 之间")
	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}
	
}