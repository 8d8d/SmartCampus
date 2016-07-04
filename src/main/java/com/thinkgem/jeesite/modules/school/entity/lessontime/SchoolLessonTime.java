/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.entity.lessontime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.TreeEntity;

/**
 * 上课时间表Entity
 * @author 王超然
 * @version 2016-05-12
 */
public class SchoolLessonTime extends TreeEntity<SchoolLessonTime> {
	
	private static final long serialVersionUID = 1L;
	private SchoolLessonTime parent;		// 上级
	private String parentIds;		// 所有上级
	private String count;		// 序号
	private String name;		// 名称
	private String start;		// 开始时间
	private String end;		// 结束时间
	private Integer sort;		// 排序
	
	public SchoolLessonTime() {
		super();
	}

	public SchoolLessonTime(String id){
		super(id);
	}

	@JsonBackReference
	public SchoolLessonTime getParent() {
		return parent;
	}

	public void setParent(SchoolLessonTime parent) {
		this.parent = parent;
	}
	
	@Length(min=0, max=255, message="所有上级长度必须介于 0 和 255 之间")
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	
	@Length(min=0, max=64, message="序号长度必须介于 0 和 64 之间")
	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}
	
	@Length(min=0, max=64, message="名称长度必须介于 0 和 64 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}
	
	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}
	
	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}
}