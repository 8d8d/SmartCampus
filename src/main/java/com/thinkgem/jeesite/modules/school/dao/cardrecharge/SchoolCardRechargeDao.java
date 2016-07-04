/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.dao.cardrecharge;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.school.entity.cardrecharge.SchoolCardRecharge;

/**
 * 一卡通充值记录DAO接口
 * @author 何伟杰
 * @version 2016-06-16
 */
@MyBatisDao
public interface SchoolCardRechargeDao extends CrudDao<SchoolCardRecharge> {
	
}