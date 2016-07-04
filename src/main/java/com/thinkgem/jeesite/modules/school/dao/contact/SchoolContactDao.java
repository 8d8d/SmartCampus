/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.dao.contact;

import com.thinkgem.jeesite.common.persistence.TreeDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.school.entity.contact.SchoolContact;

/**
 * 联系人设置DAO接口
 * @author 何伟杰
 * @version 2016-06-22
 */
@MyBatisDao
public interface SchoolContactDao extends TreeDao<SchoolContact> {
	
}