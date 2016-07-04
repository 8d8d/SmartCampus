/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.entity.calendar;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 校历Entity
 * @author 王
 * @version 2016-05-31
 */
public class SchoolCalendar extends DataEntity<SchoolCalendar> {
	
	private static final long serialVersionUID = 1L;
	private String title;		// 主题
	private Date start;		// 开始时间
	private Date end;		// 结束时间
	private String picture;		// 节日图标
	
	public SchoolCalendar() {
		super();
	}

	public SchoolCalendar(String id){
		super(id);
	}

	@Length(min=0, max=64, message="主题长度必须介于 0 和 64 之间")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
	
	@Length(min=0, max=255, message="节日图标长度必须介于 0 和 255 之间")
	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
	
}