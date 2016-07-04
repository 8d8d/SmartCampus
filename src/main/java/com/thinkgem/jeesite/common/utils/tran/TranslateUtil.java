package com.thinkgem.jeesite.common.utils.tran;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.formula.functions.T;

import com.thinkgem.jeesite.common.dao.CommonDao;
import com.thinkgem.jeesite.common.utils.BeanUtil;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.CommonUtil;
import com.thinkgem.jeesite.common.utils.ObjectUtils;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
import com.thinkgem.jeesite.common.utils.StringUtils;

public class TranslateUtil {

	public static TranslateUtil t = new TranslateUtil() ;
	public List translateList(List list,TransLateCondition c){
		CommonDao commonService = (CommonDao) SpringContextHolder.getBean("commonDao");
		String colName = c.getColName();
		String key = c.getKey();
		String table = c.getTable();
		String tableKey = c.getTableKey();
		String cols[] = colName.split(",");
		String otherParam[] = c.getOtherParam();
		String values = CommonUtil.listToStringRemoveDuplicate(list, key).toString();
		if(values.length()==0){
			return new ArrayList();
		}
		StringBuffer sql = new StringBuffer("select ").append(colName).append(","+tableKey).append(" from "+table+" where 1=1 ")
							.append(" and "+CommonUtil.getInSqlContainQuotes(tableKey,values));
		if(otherParam!=null){
			for(String s:otherParam){
				if(!StringUtils.isEmpty(s)){
					sql.append(" and "+s);
				}
			}
		}
		
		List<Map<String, Object>> dataList = (List<Map<String, Object>>) CacheUtils.get(sql.toString());
		if(dataList == null || dataList.size() == 0){
			dataList = commonService.findListBySQL(sql.toString());
			CacheUtils.put(sql.toString(), dataList);
		}
		for(Object m:list){
			String cd = ObjectUtils.toString(BeanUtil.getProperty(m, key),null);
			if(StringUtils.isEmpty((String)cd)){
				continue;
			}
			for(Map data:dataList){
				String cd1 = ObjectUtils.toString(data.get(tableKey),"");
				if(cd.equals(cd1)){
					for(String field:cols){
						field = toJavaAttributeName(field);
						BeanUtil.setProperty(m, field, data.get(field));
					}
					break;
				}
			}
		}
		return list;
	}
	
	public Object translateListT(Object o,TransLateCondition c){
		CommonDao commonService = (CommonDao) SpringContextHolder.getBean("commonDao");
		String colName = c.getColName();
		String key = c.getKey();
		String table = c.getTable();
		String tableKey = c.getTableKey();
		String cols[] = colName.split(",");
		String otherParam[] = c.getOtherParam();
		String cd = ObjectUtils.toString(BeanUtil.getProperty(o, key),null);
		if(cd == null || cd.length()==0){
			return o;
		}
		StringBuffer sql = new StringBuffer("select ").append(colName).append(","+tableKey).append(" from "+table+" where 1=1 ")
							.append(" and " + tableKey +" = '" + cd +"'");
		if(otherParam!=null){
			for(String s:otherParam){
				if(!StringUtils.isEmpty(s)){
					sql.append(" and "+s);
				}
			}
		}
		List<Map<String, Object>> dataList = commonService.findListBySQL(sql.toString());
		
		for(Map data:dataList){
			String cd1 = ObjectUtils.toString(data.get(tableKey),"");
			if(cd.equals(cd1)){
				for(String field:cols){
					field = toJavaAttributeName(field);
					BeanUtil.setProperty(o, field, data.get(field));
				}
				break;
			}
		}
		return o;
	}
	
	public static void translateList(List<Map> list,String key,String table,String colName,String tableKey){
		CommonDao commonService = (CommonDao) SpringContextHolder.getBean("commonDao");
		String cols[] = colName.split(",");

		String values = CommonUtil.getLongString(list, key).toString();
		if(values.length()==0){
			return;
		}
		String sql = "select "+colName+","+tableKey+" from "+table+" where 1=1  and "+CommonUtil.getInSqlContainQuotes(tableKey,values);
		List<Map<String, Object>> dataList = commonService.findListBySQL(sql);
		for(Map m:list){
			Object cd = (Object)m.get(key);
			for(Map data:dataList){
				Object cd1 = data.get(toJavaAttributeName(tableKey));
				if(cd.equals(cd1)){
					for(String field:cols){
						field = toJavaAttributeName(field);
						m.put(field,data.get(field));
					}
					break;
				}
			}
		}
	}
	
	public static String toJavaAttributeName(String dbColumnName) {
		if(dbColumnName.indexOf(" as ")>0){
			dbColumnName = dbColumnName.substring(dbColumnName.indexOf(" as ")+4,dbColumnName.length());
		}
		if(dbColumnName.indexOf(".")>0){
			dbColumnName = dbColumnName.substring(dbColumnName.indexOf(".")+1,dbColumnName.length()).trim();
		}
//		char ch[] = dbColumnName.toCharArray();
//		for (int i = 0; i < ch.length; i++) {
//			if (i == 0) {
//				ch[i] = Character.toLowerCase(ch[i]);
//			}
//			if ((i + 1) < ch.length) {
//				if (ch[i] == '_') {
//					ch[i + 1] = Character.toUpperCase(ch[i + 1]);
//				} else {
//					ch[i + 1] = Character.toLowerCase(ch[i + 1]);
//				}
//			}
//		}
		return dbColumnName;
	}
}
