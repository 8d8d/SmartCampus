/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.entity.leaderclass;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.modules.sys.entity.User;

/**
 * 辅导员任教Entity
 * @author 王超然
 * @version 2016-05-31
 */
public class SchoolLeaderClass extends DataEntity<SchoolLeaderClass> {
	
	private static final long serialVersionUID = 1L;
	private String leaderId;		// 辅导员
	private String classId;		// 班级
	private Date updateTime;		// 更新日期
	
	private User user;
	
	public SchoolLeaderClass() {
		super();
	}

	public SchoolLeaderClass(String id){
		super(id);
	}
	
	public SchoolLeaderClass(User user){
		this.leaderId = user.getId();
	}

	@Length(min=0, max=64, message="辅导员长度必须介于 0 和 64 之间")
	public String getLeaderId() {
		return leaderId;
	}

	public void setLeaderId(String leaderId) {
		this.leaderId = leaderId;
	}
	
	@Length(min=0, max=64, message="班级长度必须介于 0 和 64 之间")
	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}