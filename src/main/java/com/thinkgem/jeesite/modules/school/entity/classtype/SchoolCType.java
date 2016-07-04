/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.entity.classtype;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.TreeEntity;

/**
 * 课程类型Entity
 * @author 王
 * @version 2016-05-10
 */
public class SchoolCType extends TreeEntity<SchoolCType> {
	
	private static final long serialVersionUID = 1L;
	private SchoolCType parent;		// 上级菜单
	private String parentIds;		// 所有上级菜单
	private String name;		// 名称
	private Integer sort;		// 排序
	private Date updateTime;		// 更新时间
	
	public SchoolCType() {
		super();
	}

	public SchoolCType(String id){
		super(id);
	}

	@JsonBackReference
	@NotNull(message="上级菜单不能为空")
	public SchoolCType getParent() {
		return parent;
	}

	public void setParent(SchoolCType parent) {
		this.parent = parent;
	}
	
	@Length(min=1, max=255, message="所有上级菜单长度必须介于 1 和 255 之间")
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	
	@Length(min=1, max=64, message="名称长度必须介于 1 和 64 之间")
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
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@NotNull(message="更新时间不能为空")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	public String getParentId() {
		return parent != null && parent.getId() != null ? parent.getId() : "0";
	}
}