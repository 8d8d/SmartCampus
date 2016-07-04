/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.entity.news;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 校园新闻Entity
 * @author 王超然
 * @version 2016-05-31
 */
public class SchoolNews extends DataEntity<SchoolNews> {
	
	private static final long serialVersionUID = 1L;
	private String newId;		// 新闻ID
	private String address;		// 地址
	private String title;		// 标题
	private String content;		// 内容
	private String cover;		// 封面图片地址
	
	public SchoolNews() {
		super();
	}

	public SchoolNews(String id){
		super(id);
	}

	@Length(min=0, max=64, message="新闻ID长度必须介于 0 和 64 之间")
	public String getNewId() {
		return newId;
	}

	public void setNewId(String newId) {
		this.newId = newId;
	}
	
	@Length(min=0, max=255, message="地址长度必须介于 0 和 255 之间")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@Length(min=0, max=64, message="标题长度必须介于 0 和 64 之间")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Length(min=0, max=255, message="内容长度必须介于 0 和 255 之间")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@Length(min=0, max=255, message="封面图片地址长度必须介于 0 和 255 之间")
	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}
	
}