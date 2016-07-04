/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.dao.news;

import com.thinkgem.jeesite.common.persistence.CrudDao;
import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.school.entity.news.SchoolNews;

/**
 * 校园新闻DAO接口
 * @author 王超然
 * @version 2016-05-31
 */
@MyBatisDao
public interface SchoolNewsDao extends CrudDao<SchoolNews> {
	
}