/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.entity.classroom;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.TreeEntity;

/**
 * 教室名称Entity
 * @author 王
 * @version 2016-05-10
 */
public class SchoolClassroom extends TreeEntity<SchoolClassroom> {
	
	private static final long serialVersionUID = 1L;
	private SchoolClassroom parent;		// 上级菜单
	private String parentIds;		// 所有上级菜单
	private String name;		// 教室名称
	private Integer sort;		// 排序
	
	public SchoolClassroom() {
		super();
	}

	public SchoolClassroom(String id){
		super(id);
	}

	@JsonBackReference
	@NotNull(message="上级菜单不能为空")
	public SchoolClassroom getParent() {
		return parent;
	}

	public void setParent(SchoolClassroom parent) {
		this.parent = parent;
	}
	
	@Length(min=1, max=255, message="所有上级菜单长度必须介于 1 和 255 之间")
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	
	@Length(min=1, max=64, message="教室名称长度必须介于 1 和 64 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@NotNull(message="排序不能为空")
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