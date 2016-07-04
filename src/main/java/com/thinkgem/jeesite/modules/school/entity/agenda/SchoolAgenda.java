/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.entity.agenda;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.thinkgem.jeesite.modules.sys.entity.User;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 日程安排Entity
 * @author 何伟杰
 * @version 2016-06-20
 */
public class SchoolAgenda extends DataEntity<SchoolAgenda> {
	
	private static final long serialVersionUID = 1L;
	private String repeats;		// 重复方式
	private Date start;		// 开始时间
	private Date end;		// 结束时间
	private String remind;		// 提醒方式
	private String content;		// 内容
	private User user;		// 用户
	
	public SchoolAgenda() {
		super();
	}

	public SchoolAgenda(String id){
		super(id);
	}

	@Length(min=0, max=255, message="重复方式长度必须介于 0 和 255 之间")
	public String getRepeats() {
		return repeats;
	}

	public void setRepeats(String repeats) {
		this.repeats = repeats;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
	
	@Length(min=0, max=255, message="提醒方式长度必须介于 0 和 255 之间")
	public String getRemind() {
		return remind;
	}

	public void setRemind(String remind) {
		this.remind = remind;
	}
	
	@Length(min=0, max=255, message="内容长度必须介于 0 和 255 之间")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}