/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.entity.contact;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.validator.constraints.Length;
import com.thinkgem.jeesite.modules.sys.entity.User;

import com.thinkgem.jeesite.common.persistence.TreeEntity;

/**
 * 联系人设置Entity
 * @author 何伟杰
 * @version 2016-06-22
 */
public class SchoolContact extends TreeEntity<SchoolContact> {
	
	private static final long serialVersionUID = 1L;
	private SchoolContact parent;		// parent_id
	private String parentIds;		// parent_ids
	private User user;		// 用户
	private String name;		// name
	private Integer sort;		// sort
	
	public SchoolContact() {
		super();
	}

	public SchoolContact(String id){
		super(id);
	}

	@JsonBackReference
	public SchoolContact getParent() {
		return parent;
	}

	public void setParent(SchoolContact parent) {
		this.parent = parent;
	}
	
	@Length(min=0, max=255, message="parent_ids长度必须介于 0 和 255 之间")
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Length(min=0, max=64, message="name长度必须介于 0 和 64 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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