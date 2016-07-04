/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.entity.leave;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 请假审批Entity
 * @author 王超然
 * @version 2016-05-12
 */
public class SchoolLeave extends DataEntity<SchoolLeave> {
	
	private static final long serialVersionUID = 1L;
	private String applyId;		// 申请人
	private String acceptId;		// 审批人
	private String reason;		// 缘由
	private Date start;		// 开始时间
	private Date end;		// 结束时间
	private String dealId;		// 处理结果
	private String dealName;		// 处理内容
	private String dealReason;		// 处理缘由
	
	public SchoolLeave() {
		super();
	}

	public SchoolLeave(String id){
		super(id);
	}

	@Length(min=0, max=64, message="申请人长度必须介于 0 和 64 之间")
	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}
	
	@Length(min=0, max=64, message="审批人长度必须介于 0 和 64 之间")
	public String getAcceptId() {
		return acceptId;
	}

	public void setAcceptId(String acceptId) {
		this.acceptId = acceptId;
	}
	
	@Length(min=0, max=64, message="缘由长度必须介于 0 和 64 之间")
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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
	
	@Length(min=0, max=1, message="处理结果长度必须介于 0 和 1 之间")
	public String getDealId() {
		return dealId;
	}

	public void setDealId(String dealId) {
		this.dealId = dealId;
	}
	
	@Length(min=0, max=64, message="处理内容长度必须介于 0 和 64 之间")
	public String getDealName() {
		return dealName;
	}

	public void setDealName(String dealName) {
		this.dealName = dealName;
	}
	
	@Length(min=0, max=64, message="处理缘由长度必须介于 0 和 64 之间")
	public String getDealReason() {
		return dealReason;
	}

	public void setDealReason(String dealReason) {
		this.dealReason = dealReason;
	}
	
}