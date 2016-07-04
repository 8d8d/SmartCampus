/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.entity.card;

import org.hibernate.validator.constraints.Length;
import com.thinkgem.jeesite.modules.sys.entity.User;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 一卡通信息Entity
 * @author 何伟杰
 * @version 2016-06-16
 */
public class SchoolCard extends DataEntity<SchoolCard> {
	
	private static final long serialVersionUID = 1L;
	private String state;		// 状态
	private String balance;		// 余额
	private User user;		// 用户
	private String cardId;		// 卡号
	
	public SchoolCard() {
		super();
	}

	public SchoolCard(String id){
		super(id);
	}

	@Length(min=0, max=255, message="状态长度必须介于 0 和 255 之间")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	@Length(min=0, max=255, message="余额长度必须介于 0 和 255 之间")
	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Length(min=0, max=255, message="卡号长度必须介于 0 和 255 之间")
	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	
}