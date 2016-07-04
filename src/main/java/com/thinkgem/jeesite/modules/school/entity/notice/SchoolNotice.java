/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.entity.notice;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 校园公告Entity
 * @author 王超然
 * @version 2016-05-12
 */
public class SchoolNotice extends DataEntity<SchoolNotice> {
	
	private static final long serialVersionUID = 1L;
	private String noticeId;		// 公告ID
	private String title;		// 标题
	private String content;		// 内容
	private String address;		// 地址
	
	public SchoolNotice() {
		super();
	}

	public SchoolNotice(String id){
		super(id);
	}

	@Length(min=0, max=64, message="公告ID长度必须介于 0 和 64 之间")
	public String getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
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
	
	@Length(min=0, max=255, message="地址长度必须介于 0 和 255 之间")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
}