/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.entity.cardconsum;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 一卡通消费记录Entity
 * @author 何伟杰
 * @version 2016-06-16
 */
public class SchoolCardConsum extends DataEntity<SchoolCardConsum> {
	
	private static final long serialVersionUID = 1L;
	private String consumMoney;		// 消费金额
	private String consumName;		// 消费名字
	private String consumType;		// 消费类型
	private String balance;		// 余额
	private String cardId;		// 卡号
	
	public SchoolCardConsum() {
		super();
	}

	public SchoolCardConsum(String id){
		super(id);
	}

	@Length(min=0, max=255, message="消费金额长度必须介于 0 和 255 之间")
	public String getConsumMoney() {
		return consumMoney;
	}

	public void setConsumMoney(String consumMoney) {
		this.consumMoney = consumMoney;
	}
	
	@Length(min=0, max=255, message="消费名字长度必须介于 0 和 255 之间")
	public String getConsumName() {
		return consumName;
	}

	public void setConsumName(String consumName) {
		this.consumName = consumName;
	}
	
	@Length(min=0, max=255, message="消费类型长度必须介于 0 和 255 之间")
	public String getConsumType() {
		return consumType;
	}

	public void setConsumType(String consumType) {
		this.consumType = consumType;
	}
	
	@Length(min=0, max=255, message="消费类型长度必须介于 0 和 255 之间")
	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}
	
	@Length(min=0, max=255, message="卡号长度必须介于 0 和 255 之间")
	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	
}