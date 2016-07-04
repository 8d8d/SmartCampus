/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.dao.card;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.school.entity.card.SchoolCard;

/**
 * 一卡通信息DAO接口
 * @author 何伟杰
 * @version 2016-06-16
 */
@MyBatisDao
public interface SchoolCardDao extends CrudDao<SchoolCard> {
	
}