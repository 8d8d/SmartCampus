/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.entity.home;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 首页Entity
 * @author 王超然
 * @version 2016-05-26
 */
public class SchoolHome extends DataEntity<SchoolHome> {
	
	private static final long serialVersionUID = 1L;
	private String name;		// 功能
	private String pic;		// 图片
	
	public SchoolHome() {
		super();
	}

	public SchoolHome(String id){
		super(id);
	}

	@Length(min=0, max=64, message="功能长度必须介于 0 和 64 之间")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Length(min=0, max=255, message="图片长度必须介于 0 和 255 之间")
	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}
	
}