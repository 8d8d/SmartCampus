/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.entity.cardrecharge;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 一卡通充值记录Entity
 * @author 何伟杰
 * @version 2016-06-16
 */
public class SchoolCardRecharge extends DataEntity<SchoolCardRecharge> {
	
	private static final long serialVersionUID = 1L;
	private String cardId;		// 卡号
	private String rechargeMoney;		// 充值金额
	private String rechargeName;		// 充值机器名
	private String wallet;		// 充值钱包
	private String rechargeType;		// 充值类型
	private String balance;		// 余额
	
	public SchoolCardRecharge() {
		super();
	}

	public SchoolCardRecharge(String id){
		super(id);
	}

	@Length(min=0, max=255, message="卡号长度必须介于 0 和 255 之间")
	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	
	@Length(min=0, max=255, message="充值金额长度必须介于 0 和 255 之间")
	public String getRechargeMoney() {
		return rechargeMoney;
	}

	public void setRechargeMoney(String rechargeMoney) {
		this.rechargeMoney = rechargeMoney;
	}
	
	@Length(min=0, max=255, message="充值机器名长度必须介于 0 和 255 之间")
	public String getRechargeName() {
		return rechargeName;
	}

	public void setRechargeName(String rechargeName) {
		this.rechargeName = rechargeName;
	}
	
	@Length(min=0, max=255, message="充值钱包长度必须介于 0 和 255 之间")
	public String getWallet() {
		return wallet;
	}

	public void setWallet(String wallet) {
		this.wallet = wallet;
	}
	
	@Length(min=0, max=255, message="充值类型长度必须介于 0 和 255 之间")
	public String getRechargeType() {
		return rechargeType;
	}

	public void setRechargeType(String rechargeType) {
		this.rechargeType = rechargeType;
	}

	@Length(min=0, max=255, message="消费类型长度必须介于 0 和 255 之间")
	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}
}