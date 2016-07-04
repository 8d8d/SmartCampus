package com.thinkgem.jeesite.common.utils;


import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.mvel2.DataConversion;
import org.mvel2.MVEL;
import org.mvel2.util.PropertyTools;

import com.thinkgem.jeesite.common.utils.mvel.DateConversion;
import com.thinkgem.jeesite.common.utils.mvel.TimeStampConversion;

public final class BeanUtil
{
	
static
  {
    DataConversion.addConversionHandler(Date.class, new DateConversion());
    DataConversion.addConversionHandler(Timestamp.class, new TimeStampConversion());
  }
	
  /**
   * 集合对象转化为集合MAP
   * @param list
   * @return
   */
  public static List<Map> listBean2listMap(List list)
  {
    List rs = new ArrayList();
    for (Iterator localIterator = list.iterator(); localIterator.hasNext(); ) { Object obj = localIterator.next();
      rs.add(bean2map(obj));
    }
    return rs;
  }
  
  /**
   * 集合对象转化为集合MAP
   * @param list
   * @return
   */
  public static List<Map> listMap2listBean(List list,Class c)
  {
    List rs = new ArrayList();
    for (int i = 0 ; i < list.size() ; i++) { 
      Object obj = list.get(i);
      Object o = BeanUtil.map2bean((Map)obj,c);
      if(o!=null){
    	  System.out.println(o);
    	  rs.add(o);
      }
    }
    return rs;
  }
  
  /**
   * 集合对象转化为集合MAP
   * @param list
   * @return
   */
  public static List<Map> listObjectName2listJavaName(List list)
  {
    List rs = new ArrayList();
    for (Iterator localIterator = list.iterator(); localIterator.hasNext(); ) { Object obj = localIterator.next();
      rs.add(objectNameToJavaName(obj));
    }
    return rs;
  }

  /**
   * 对象转化为map
   * @param bean
   * @return
   */
  public static Map bean2map(Object bean)
  {
    if (bean == null) {
      return null;
    }
    if ((bean instanceof Map)) {
      return (Map)bean;
    }

    Map m = new HashMap();
    Field[] fields = bean.getClass().getDeclaredFields();
    for (int i = 0; i < fields.length; i++) {
      if (PropertyTools.hasGetter(fields[i])) {
        m.put(fields[i].getName(), MVEL.getProperty(fields[i].getName(), bean));
      }
    }
    return m;
  }
  
  /**
   * map 转化为 对象
   * @param <T>
   * @param properties
   * @param c
   * @return
   */
  public static <T> T map2bean(Map properties, Class<T> c)
  {
    if (properties == null) {
      return null;
    }
    T bean = null ;
    try
    {
      bean = c.newInstance();
      Iterator iterator = properties.entrySet().iterator();
      while (iterator.hasNext()) {
        Map.Entry entry = (Map.Entry)iterator.next();
        setProperty(bean, (String)entry.getKey(), entry.getValue());
      }
      
    } catch (Exception e) {
    	e.printStackTrace();  
    }
      return bean; 
  }

  
  public static Object getProperty(Object bean, String expression)
  {
    if (bean == null) {
      return null;
    }
    if (PropertyTools.getSetter(bean.getClass(), expression) != null) {
      return MVEL.getProperty(expression, bean);
    }

    return null;
  }

  public static void setProperty(Object bean, String expression, Object properties)
  {
    if (PropertyTools.getSetter(bean.getClass(), expression) != null)
    {
      Class paramType = PropertyTools.getSetter(bean.getClass(), expression).getParameterTypes()[0];
      if(properties != null){
	      if ((properties.getClass().isArray()) && (!paramType.isArray())) {
	        Object[] param = (Object[])properties;
	        properties = param[0];
	      }
	      else if ((!properties.getClass().isArray()) && (paramType.isArray())) {
	        Object[] param = { properties };
	        properties = param;
	      }
	      MVEL.setProperty(bean, expression, properties);
      }
    }
  }

  public static String firstUpper(String s)
  {
    char[] ch = s.toLowerCase().toCharArray();
    ch[0] = (char)(ch[0] - ' ');
    String result = new String(ch);
    return result;
  }

  public static String tableNameToClassName(String tablename) {
    String[] sa = tablename.split("[_]");
    StringBuffer sb = new StringBuffer();

    for (int i = 0; i < sa.length; i++) {
      sb.append(firstUpper(sa[i]));
    }

    return sb.toString();
  }

  public static String filedNameToJavaName(String fileName) {
    String t = tableNameToClassName(fileName);
    return t.substring(0, 1).toLowerCase() + t.substring(1);
  }
  
  /**
   *  把MAP的KEY转化为大小写
   * @param obj
   * @return
   */
  public static Object objectNameToJavaName(Object obj) {
    if ((obj != null) && ((obj instanceof Map))) {
        Map newMap = new HashMap();
        Map oldMap = (Map)obj;
        Iterator iterator = oldMap.entrySet().iterator();
        while (iterator.hasNext()) {
          Map.Entry entry = (Map.Entry)iterator.next();
          newMap.put(BeanUtil.filedNameToJavaName(entry.getKey().toString()), entry.getValue());
        }
        return newMap;
     }else if(obj instanceof List || obj instanceof ArrayList){
    	  List oldlist = (List) obj;
    	  List newlist = new ArrayList();
    	  for(int i = 0 ; i < oldlist.size() ; i++){
    		  newlist.add(objectNameToJavaName(oldlist.get(i)));
    	  }
    	  return newlist;
     }else
    	  return obj;
  }
  /**
   * 把LIST里面的值转化为String
   * @param list
   * @param fileName
   * @return
   */
  public static String getListPropertyToJavaString(List list ,String fileName) {
	  String result = "'-1'";
	  for(int i = 0 ; i < list.size() ; i++){
		  Object o = list.get(i);
		  if(getProperty(o, fileName) != null)
			  result = result + ",'" + getProperty(o, fileName)+"'";
	  }
	  return result;
  }
  
}