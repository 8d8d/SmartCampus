/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.dao.cardconsum;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.school.entity.cardconsum.SchoolCardConsum;

/**
 * 一卡通消费记录DAO接口
 * @author 何伟杰
 * @version 2016-06-16
 */
@MyBatisDao
public interface SchoolCardConsumDao extends CrudDao<SchoolCardConsum> {
	
}