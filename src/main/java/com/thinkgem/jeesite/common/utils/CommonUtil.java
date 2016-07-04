package com.thinkgem.jeesite.common.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.reflection.stdclasses.BigDecimalCachedClass;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.thinkgem.jeesite.common.ModuleContants;
import com.thinkgem.jeesite.common.model.ResultSure;
import com.thinkgem.jeesite.common.serializer.AllTimeStampSerializer;
import com.thoughtworks.xstream.core.BaseException;

public class CommonUtil {

	private static final DecimalFormat df = new DecimalFormat("#####0.00");
	public static final DecimalFormat sdf = new DecimalFormat("#####0.0");
	// 日期格式化
	public static final SimpleDateFormat ymdhm = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");
	public static final SimpleDateFormat ymdhms = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat mdhm = new SimpleDateFormat(
			"MM-dd HH:mm");
	public static final SimpleDateFormat mdhms = new SimpleDateFormat(
			"MM-dd HH:mm:ss");
	public static final SimpleDateFormat ymd = new SimpleDateFormat(
			"yyyy-MM-dd");

	public static final int scale = 1;

	/**
	 * 
	 */
	public static String replaceAll(String strs, String xx) {
		strs = strs.substring(1, strs.length() - 1);
		String[] arr = strs.split(",");
		StringBuffer result = new StringBuffer("'-1'");
		for (String s : arr) {
			if (!s.replaceAll("'", "").equals(xx)
					&& !s.replaceAll("'", "").equals("-1")) {
				result.append("," + s);
			}
		}
		return "(" + result.toString() + ")";
	}

	/**
	 * 集合对象转化为集合MAP
	 * 
	 * @param list
	 * @return
	 */
	public static List listMap2listBean(List list, Class c) {
		List rs = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			Object o = BeanUtil.map2bean((Map) obj, c);
			if (o != null) {
				rs.add(o);
			}
		}
		return rs;
	}

	/**
	 * 把LIST中字符串对象拼接成字符串
	 */
	public static String crateString(List list) {
		String str = "'-1'";
		for (int i = 0; i < list.size(); i++) {
			str += ",'" + ObjectUtils.toString(list.get(i), "") + "'";
		}
		return str;
	}

	/**
	 * 根据对象的KEY取得对象的值
	 * 
	 * @param obj
	 *            对象
	 * @param code
	 *            对象KEY的值
	 */
	public static String getObjectByKey(Object obj, String code) {
		String orgCd = "";
		try {
			// 如果是MAP
			if (obj instanceof Map) {
				orgCd = ObjectUtils.toString(((Map) obj).get(code), "");
			}
			// 如果是HASHMAP
			else if (obj instanceof HashMap) {
				orgCd = ObjectUtils.toString(((HashMap) obj).get(code), "");
			}
			// 如果是对象
			else {
				Map map = BeanUtil.bean2map(obj);
				orgCd = ObjectUtils.toString(map.get(code), "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return orgCd;
		}
	}

	/**
	 * 年度
	 * 
	 * @param patten
	 * @return
	 */
	public static String getNowDateByFormat(String patten) {
		SimpleDateFormat sdf = new SimpleDateFormat(patten);
		String ly_time = sdf.format(new java.util.Date());
		return ly_time;
	}

	/**
	 * 补全编号
	 * 
	 * @param o
	 *            字符
	 * @param num
	 *            一共要多少位
	 * @return
	 */
	public static String getStringLength(String o, int num) {
		int k = num - o.length();
		for (int i = 0; i < k; i++) {
			o = o + " ";
		}
		return o;
	}

	public static List copyList(List list) {
		List listEnd = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			listEnd.add(list.get(i));
		}
		return listEnd;
	}

	public static List deepcopy(List src) throws IOException,
			ClassNotFoundException {
		ByteArrayOutputStream byteout = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(byteout);
		out.writeObject(src);
		ByteArrayInputStream bytein = new ByteArrayInputStream(
				byteout.toByteArray());
		ObjectInputStream in = new ObjectInputStream(bytein);
		List dest = (List) in.readObject();
		return dest;
	}

	private static final SerializerFeature[] features = {
			SerializerFeature.WriteMapNullValue,
			SerializerFeature.WriteNullListAsEmpty,
			SerializerFeature.WriteNullNumberAsZero,
			SerializerFeature.WriteNullBooleanAsFalse,
			SerializerFeature.WriteNullStringAsEmpty,
			SerializerFeature.UseISO8601DateFormat };

	/**
	 * 
	 * @param isSucess
	 * @param object
	 * @param response
	 * @throws BaseException
	 */
	public static void writeJson(boolean isSucess, Object object,
			HttpServletResponse response) {
		SerializeConfig
				.getGlobalInstance()
				.put(java.util.Date.class,
						com.thinkgem.jeesite.common.serializer.OracleTimeStampSerializer.instance); // 使用和json-lib兼容的日期输出格式
		SerializeConfig
				.getGlobalInstance()
				.put(java.sql.Timestamp.class,
						com.thinkgem.jeesite.common.serializer.OracleTimeStampSerializer.instance); // 使用和json-lib兼容的日期输出格式
		String jsonString = "";
		try {
			ResultSure result = new ResultSure(isSucess, object);
			jsonString = JSON.toJSONString(result, features);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			PrintWriter out = response.getWriter();
			out.print(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param isSucess
	 * @param object
	 * @param response
	 * @throws BaseException
	 */
	public static void writeJsonSuccess(boolean isSucess, Object object,
			HttpServletResponse response) {
		SerializeConfig
				.getGlobalInstance()
				.put(java.util.Date.class,
						com.thinkgem.jeesite.common.serializer.OracleTimeStampSerializer.instance); // 使用和json-lib兼容的日期输出格式
		SerializeConfig
				.getGlobalInstance()
				.put(java.sql.Timestamp.class,
						com.thinkgem.jeesite.common.serializer.OracleTimeStampSerializer.instance); // 使用和json-lib兼容的日期输出格式
		String jsonString = "";
		try {
			ResultSure result = new ResultSure(isSucess, object);
			jsonString = JSON.toJSONString(result, features);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			PrintWriter out = response.getWriter();
			out.print(jsonString);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param isSucess
	 * @param object
	 * @param response
	 * @throws BaseException
	 */
	public static String toJson(Object object) {
		SerializeConfig.getGlobalInstance().put(java.util.Date.class,
				AllTimeStampSerializer.instance); // 使用和json-lib兼容的日期输出格式
		SerializeConfig
				.getGlobalInstance()
				.put(java.sql.Timestamp.class,
						com.thinkgem.jeesite.common.serializer.AllTimeStampSerializer.instance); // 使用和json-lib兼容的日期输出格式
		String jsonString = "";
		try {
			Map map = new HashMap();
			map.put("resultObject", object);
			map.put("sucess", true);
			jsonString = JSON.toJSONString(map, features);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SerializeConfig
					.getGlobalInstance()
					.put(java.util.Date.class,
							com.thinkgem.jeesite.common.serializer.OracleTimeStampSerializer.instance); // 使用和json-lib兼容的日期输出格式
			SerializeConfig
					.getGlobalInstance()
					.put(java.sql.Timestamp.class,
							com.thinkgem.jeesite.common.serializer.OracleTimeStampSerializer.instance); // 使用和json-lib兼容的日期输出格式
		}
		return jsonString;
	}

	/**
	 * 
	 * @param isSucess
	 * @param object
	 * @param response
	 * @throws BaseException
	 */
	public static void writeString(boolean isSucess, String xml,
			HttpServletResponse response) {
		try {
			PrintWriter out = response.getWriter();
			out.print(xml);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 取得某个property的StringBuffer
	 * 
	 * @return
	 */
	public static StringBuffer getLongString(List list, String key) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("'-1'");
		for (int i = 0; i < list.size(); i++) {
			Object o = list.get(i);
			if (o instanceof Map) {
				if (((Map) o).get(key) != null) {
					buffer.append(",'");
					buffer.append(((Map) o).get(key).toString());
					buffer.append("'");
				}
			} else {
				Map p = BeanUtil.bean2map(o);
				if (((Map) p).get(key) != null) {
					buffer.append(",'");
					buffer.append(((Map) p).get(key).toString());
					buffer.append("'");
				}
			}
		}
		return buffer;
	}

	/**
	 * 根据LIST中对象集合起来 成为字符串
	 * 
	 * @return
	 */
	public static String getListString(List list) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("'1'");
		for (int i = 0; i < list.size(); i++) {
			buffer.append(",");
			buffer.append("'");
			buffer.append(ObjectUtils.toString(list.get(i), "1"));
			buffer.append("'");
		}
		return buffer.toString();
	}

	/**
	 * 把LIST中某个KEY的不重复的值拼接起来
	 * 
	 * @param list
	 *            集合
	 * @param key
	 *            key
	 * @return
	 */
	public static String listToStringRemoveDuplicate(List list, String key) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("'1'");
		for (int i = 0; i < list.size(); i++) {
			Object o = list.get(i);
			if (o == null) {
				continue;
			}
			Map map = new HashMap();
			if (o instanceof Map) {
				map = (Map) o;
			} else {
				map = BeanUtil.bean2map(o);
			}
			if (map.get(key) == null) {
				continue;
			}
			String code = map.get(key).toString();
			if (buffer.toString().indexOf(code) == -1) {
				buffer.append(",");
				buffer.append("'");
				buffer.append(code);
				buffer.append("'");
			}
		}
		return buffer.toString();
	}

	/**
	 * 根据某个KEY 把LIST转化为MAP
	 * 
	 * @return
	 */
	public static Map listToMap(List list, String key) {
		Map mapResult = new HashMap();
		for (int i = 0; i < list.size(); i++) {
			Object o = list.get(i);
			Map map = new HashMap();
			if (o instanceof Map) {
				map = (Map) o;
			} else {
				map = BeanUtil.bean2map(o);
			}
			mapResult.put(map.get(key), map);
		}
		return mapResult;
	}

	/**
	 * 根据某个KEY 把LIST转化为MAP
	 * 
	 * @return
	 */
	public static Map listToMapUpp(List list, String key) {
		Map mapResult = new HashMap();
		for (int i = 0; i < list.size(); i++) {
			Object o = list.get(i);
			Map map = new HashMap();
			if (o instanceof Map) {
				map = (Map) o;
			} else {
				map = BeanUtil.bean2map(o);
			}
			mapResult.put(ObjectUtils.toString(map.get(key), "").toUpperCase(),
					map);
		}
		return mapResult;
	}

	/**
	 * 根据某个KEY 把LIST转化为MAP
	 * 
	 * @return
	 */
	public static Map listToMap(List list, String key, String key1) {
		Map mapResult = new HashMap();
		for (int i = 0; i < list.size(); i++) {
			Object o = list.get(i);
			Map map = new HashMap();
			if (o instanceof Map) {
				map = (Map) o;
			} else {
				map = BeanUtil.bean2map(o);
			}
			String id = ObjectUtils.toString(map.get(key))
					+ ObjectUtils.toString(map.get(key1));
			mapResult.put(id, map);
		}
		return mapResult;
	}

	/**
	 * 根据某个KEY 把LIST转化为MAP
	 * 
	 * @return
	 */
	public static Map listToObject(List list, String key) {
		Map mapResult = new HashMap();
		for (int i = 0; i < list.size(); i++) {
			Object o = list.get(i);
			Map map = new HashMap();
			if (o instanceof Map) {
				map = (Map) o;
			} else {
				map = BeanUtil.bean2map(o);
			}
			mapResult.put(map.get(key), o);
		}
		return mapResult;
	}

	/**
	 * 深度拷贝object
	 * 
	 * @param src
	 * @return
	 */
	public static Object deepClone(Object src) {
		Object o = null;
		try {
			if (src != null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ObjectOutputStream oos = new ObjectOutputStream(baos);
				oos.writeObject(src);
				oos.close();
				ByteArrayInputStream bais = new ByteArrayInputStream(
						baos.toByteArray());
				ObjectInputStream ois = new ObjectInputStream(bais);
				o = ois.readObject();
				ois.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return o;
	}

	// 格式化数字对象
	private static DecimalFormat decimalFormat = new DecimalFormat();

	/**
	 * 转化类型
	 * 
	 * @param o
	 * @param end
	 * @return
	 */
	public static double toDouble(Object o, double end) {
		try {
			if(o == null){
				return end;
			}
			String s = formatNumber(Double.parseDouble(o.toString()), "0.00");
			return Double.parseDouble(s);
		} catch (Exception e) {
			return end;
		}
	}

	/**
	 * 格式化数字
	 * 
	 * @param value
	 *            数据
	 * @param pattern
	 *            格式化字符串 输入 "0.00" 格式化为两位小数
	 * @return
	 */
	public static String formatNumber(Object value, String pattern) {
		if (StringUtils.isBlank(pattern))
			pattern = "0.00";
		decimalFormat.applyPattern(pattern);
		if (value == null)
			return decimalFormat.format(new Integer("0"));
		else
			return decimalFormat.format(value);
	}

	/**
	 * 转化类型
	 * 
	 * @param o
	 * @param end
	 * @return
	 */
	public static int toInteger(Object o, int end) {
		try {
			String numStr = o.toString();
			if (numStr.indexOf(".") > 0) {
				numStr = numStr.substring(0, numStr.indexOf("."));
			}
			return Integer.parseInt(numStr);
		} catch (Exception e) {
			return end;
		}
	}

	/**
	 * 找出Map中的值
	 * 
	 * @param num
	 */
	public static Object getMapHave(Map map, String key) {
		if (!map.containsKey(key)) {
			return null;
		}
		Object o = map.get(key);
		if (o == null) {
			return null;
		}
		return o;
	}

	/**
	 * 根据LIST中某个KEY相等的对象集合起来 LIST转化为MAP
	 * 
	 * @return
	 */
	public static Map listToMapEqual(List list, String key) {
		Map mapResult = new HashMap();
		for (int i = 0; i < list.size(); i++) {
			Object o = list.get(i);
			Map map = new HashMap();
			if (o instanceof Map) {
				map = (Map) o;
			} else {
				map = BeanUtil.bean2map(o);
			}
			if (map.get(key) != null) {
				String code = map.get(key).toString();
				List listResult = new ArrayList();
				if (mapResult.containsKey(code)) {
					listResult = (List) mapResult.get(code);
					listResult.add(map);
					mapResult.put(code, listResult);
				} else {
					listResult.add(map);
					mapResult.put(code, listResult);
				}
			}
		}
		return mapResult;
	}

	/**
	 * 根据LIST中某个KEY相等的对象集合起来 并且根据MAP中的某个KEY的Value值合起来
	 * 
	 * @return
	 */
	public static Map listToMapEqualString(List list, String key,
			Object... args) {
		Map mapResult = new HashMap();
		for (int i = 0; i < list.size(); i++) {
			Object o = list.get(i);
			if (o == null) {
				continue;
			}
			Map map = new HashMap();
			if (o instanceof Map) {
				map = (Map) o;
			} else {
				map = BeanUtil.bean2map(o);
			}
			String code = map.get(key).toString();
			StringBuffer s = new StringBuffer();
			if (mapResult.containsKey(code)) {
				s = (StringBuffer) mapResult.get(code);
			}
			for (int j = 0; j < args.length; j++) {
				s.append(ObjectUtils.toString(map.get(args[j])));
				if (j < args.length - 1)
					s.append(ObjectUtils.toString("|"));
			}
			s.append(";");
			mapResult.put(code, s);
		}
		return mapResult;
	}

	/**
	 * 返回IN SQL
	 */
	public static String getInSql(String key, String value) {
		value = value.replaceAll("'", "");
		String[] sqlArray = value.split(",");
		StringBuffer buffer = new StringBuffer();
		int fj = 400;
		int n = 0;
		int m = sqlArray.length;
		if (m > fj) {
			while (m - n > 0) {
				int k = n + fj;
				if (k > m) {
					k = m;
				}

				if (n == 0) {
					buffer.append(" ( ");
				} else {
					buffer.append(" OR ");
				}
				buffer.append(key);
				buffer.append(" IN  (");
				for (int i = n; i < k; i++) {
					buffer.append("'" + ObjectUtils.toString(sqlArray[i], "1")
							+ "'");
					if (i < k - 1) {
						buffer.append(" , ");
					}
				}
				buffer.append(" )");
				n = n + fj;
			}
			buffer.append(" ) ");
			return buffer.toString();
		} else {
			for (int i = 0; i < m; i++) {
				buffer.append("'" + ObjectUtils.toString(sqlArray[i], "1")
						+ "'");
				if (i < m - 1) {
					buffer.append(" , ");
				}
			}
			return key + " IN (" + buffer.toString() + ")";

		}
	}

	/**
	 * 判断LIST中的某个Key是否存在
	 */
	public static boolean getListKeyIsHave(List list, String key, String value) {
		for (int i = 0; i < list.size(); i++) {
			Object o = list.get(i);
			if (o == null) {
				continue;
			}
			Map map = new HashMap();
			if (o instanceof Map) {
				map = (Map) o;
			} else {
				map = BeanUtil.bean2map(o);
			}
			if (value.equals(ObjectUtils.toString(map.get(key), ""))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断LIST中的某个Key是否存在
	 * 
	 * @param list
	 * @param args
	 *            参数和值 key1,val1,key2,val2
	 */
	public static boolean getListKeyIsHave(List list, String... args) {
		for (int i = 0; i < list.size(); i++) {
			Object o = list.get(i);
			Map map = new HashMap();
			if (o instanceof Map) {
				map = (Map) o;
			} else {
				map = BeanUtil.bean2map(o);
			}
			boolean isAllTrue = true;
			for (int j = 1; j < args.length; j += 2) {
				if (!map.get(String.valueOf(args[j - 1])).equals(args[j])) {
					isAllTrue = false;
					break;
				}
			}
			if (isAllTrue) {
				return true;
			}
		}
		return false;
	}

	public static List getListByKeys(List list, String... args) {
		List rtn = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			Object o = list.get(i);
			Map map = new HashMap();
			if (o instanceof Map) {
				map = (Map) o;
			} else {
				map = BeanUtil.bean2map(o);
			}
			boolean isAllTrue = true;
			for (int j = 1; j < args.length; j += 2) {
				if (!map.get(String.valueOf(args[j - 1])).equals(args[j])) {
					isAllTrue = false;
					break;
				}
			}
			if (isAllTrue) {
				rtn.add(map);
			}
		}
		return rtn;
	}

	/**
	 * 取得数据审核的下一个环节
	 */
	public static Map getNextNodeEk(String type, String node) {
		Map map = new HashMap();
		// String nowCode = "1";
		// String nextCode = "";
		// //环节现有数据
		// List PermissHaveQx =
		// ParamUtil.getLiuChengInformation(AuditDataContanst.COMMON_ORG_CD,
		// type, false);
		// if("".equals(node)){
		// if(PermissHaveQx.size() > 1)
		// nowCode = PermissHaveQx.get(1).toString();
		// else
		// nowCode = "";
		// }
		// else{
		// for(int i = 0 ; i < PermissHaveQx.size() ; i++){
		// String code = PermissHaveQx.get(i).toString();
		// if(code.lastIndexOf(node) > 0){
		// nowCode = code;
		// if(i == PermissHaveQx.size() -1 ) {
		// nextCode = "";
		// }else{
		// nextCode = PermissHaveQx.get(i + 1).toString();
		// }
		// break;
		// }
		// }
		// if(nowCode.equals("1")){
		// //所有环节
		// List PermissHaveQxAll =
		// ParamUtil.getLiuChengInformation(AuditDataContanst.COMMON_ORG_CD,
		// type, true);
		//
		// for(int j = 0 ; j < PermissHaveQxAll.size() ; j++){
		// String code = PermissHaveQxAll.get(j).toString();
		// if(code.lastIndexOf(node) > 0){
		// nowCode = code;
		// if(j == PermissHaveQxAll.size() -1 ) {
		// nextCode = "";
		// }else{
		// if(PermissHaveQx.contains(PermissHaveQxAll.get(j + 1).toString())){
		// nextCode = PermissHaveQxAll.get(j + 1).toString();
		// }else
		// nowCode = "";
		// }
		// }
		// }
		// }
		// }
		// map.put("nowCode", nowCode);
		// map.put("nextCode", nextCode);
		return map;
	}

	/**
	 * 返回IN SQL
	 */
	public static String getInSqlContainQuotes(String key, String value) {
		value = value.replaceAll("'", "");
		String[] sqlArray = value.split(",");
		StringBuffer buffer = new StringBuffer();
		int fj = 400;
		int n = 0;
		int m = sqlArray.length;
		if (m > fj) {
			while (m - n > 0) {
				int k = n + fj;
				if (k > m) {
					k = m;
				}

				if (n == 0) {
					buffer.append(" ( ");
				} else {
					buffer.append(" OR ");
				}
				buffer.append(key);
				buffer.append(" IN  (");
				for (int i = n; i < k; i++) {
					buffer.append("'" + ObjectUtils.toString(sqlArray[i], "1")
							+ "'");
					if (i < k - 1) {
						buffer.append(" , ");
					}
				}
				buffer.append(" )");
				n = n + fj;
			}
			buffer.append(" ) ");
			return buffer.toString();
		} else {
			for (int i = 0; i < m; i++) {
				buffer.append("'" + ObjectUtils.toString(sqlArray[i], "1")
						+ "'");
				if (i < m - 1) {
					buffer.append(" , ");
				}
			}
			return key + " IN (" + buffer.toString() + ")";

		}

	}

	/**
	 * 把dataId安逗号隔开的数据，全部LIJE起来
	 * 
	 * @param dataId
	 * @param sql
	 * @param arr
	 * @return
	 */
	public static void getLikeAndOr(String dataId, StringBuffer sql, List arr,
			String property) {
		if (!StringUtils.isEmpty(dataId)) {
			dataId = dataId.replaceAll("'", "");
			dataId = replaceDo(dataId);
			String[] projectTypeInArray = dataId.split(",");

			for (int i = 0; i < projectTypeInArray.length; i++) {
				if (i == 0) {
					sql.append(" and ( ");
					sql.append(property + " like ?||'%' ");
				} else {
					if (!StringUtils.isEmpty(projectTypeInArray[i]))
						sql.append(" or " + property + " like ?||'%' ");
				}

				if (i == projectTypeInArray.length - 1) {
					sql.append(" )");
				}
				if (!StringUtils.isEmpty(projectTypeInArray[i]))
					arr.add(projectTypeInArray[i]);
			}
		}
	}

	public static String replaceDo(String dataId) {
		if (dataId.startsWith(",")) {
			dataId = dataId.replace(",", "");
			replaceDo(dataId);
		}
		return dataId;

	}

	/**
	 * /** 获取所需字段的查询字符串
	 * 
	 * @param list
	 * @param key
	 * @return
	 */
	public static String formatStringKey(List list, String key) {
		String str = "";
		boolean bool = false;
		for (int index = 0; index < list.size(); index++) {
			Map m = (Map) list.get(index);
			if (bool) {
				str += ",";
			}
			str += "'" + m.get(key) + "'";
			bool = true;
		}
		return str;
	}

	/**
	 * 是否包含字串
	 * 
	 * @param list
	 * @param key
	 * @param str
	 * @return
	 */
	public static Boolean contentString(List list, String key, String str) {
		boolean flag = false;
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Map m = (Map) list.get(i);
				if (str.equals(m.get(key))) {
					flag = true;
				}
			}
		}
		return flag;
	}

	public static BigDecimal getBigDecimal(Object obj) {
		if (obj == null || "".equals(obj)) {
			return BigDecimal.ZERO;
		}
		return new BigDecimal(String.valueOf(obj));
	}

	/**
	 * 进行数字匹配查询
	 * 
	 * @param obj
	 * @return
	 */
	public static StringBuffer getSql(StringBuffer buffer, String property,
			String code, List array) {
		String[] codeArray = code.split(";");
		if (codeArray.length < 2) {
			return buffer;
		} else {
			String codeS = codeArray[0];
			String codeStart = codeArray[1];
			if ("1".equals(codeS)) {
				array.add(codeStart);
				return buffer.append(" AND " + property + ">= ?");
			} else if ("2".equals(codeS)) {
				array.add(codeStart);
				return buffer.append(" AND " + property + "= ?");
			} else if ("3".equals(codeS)) {
				array.add(codeStart);
				return buffer.append(" AND " + property + "<= ?");
			} else if ("4".equals(codeS)) {
				if (!StringUtils.isEmpty(codeStart)) {
					array.add(codeStart);
					buffer.append(" AND " + property + ">= ?");
				}
				if (codeArray.length > 2) {
					if (!StringUtils.isEmpty(codeArray[2])) {
						array.add(codeArray[2]);
						buffer.append(" AND " + property + "<= ?");
					}
				}
				return buffer;
			}
			return buffer;
		}
	}

	/**
	 * 截取ORDERBY意外的sql字串
	 * 
	 * @param sql
	 * @return
	 */
	public static String substrOrderBySql(String sql) {
		int index = sql.indexOf("order");
		return StringUtils.substring(sql, 0, index);
	}

	public static Object getValue(Map data, String key, String property) {
		if (data == null)
			return null;
		return data.get(property);
	}

	public static List bubbleSort(List list, String key) {// 冒泡排序算法
		for (int i = 0; i < list.size() - 1; i++) {
			Map map = (Map) list.get(i);
			String value = map.get(key).toString();
			if ("ALL".equals(value)) {
				value = "9999";
			}
			for (int j = i + 1; j < list.size(); j++) {
				Map mapj = (Map) list.get(j);
				String valuej = mapj.get(key).toString();
				if ("ALL".equals(valuej)) {
					valuej = "9999";
				}
				if (CommonUtil.toDouble(value, 0) > CommonUtil.toDouble(valuej,
						0)) {
					value = valuej;
					Map mapR = (Map) list.get(i);
					list.set(i, mapj);
					list.set(j, mapR);
				}
			}
		}
		Collections.reverse(list);
		return list;
	}

	public static String turnToString(String projectTypeCd) {
		String str = "";
		String strCd = projectTypeCd.replaceAll("'", "");
		String[] strArr = strCd.split(",");
		if (strArr.length > 0) {
			Boolean bool = false;
			for (String s : strArr) {
				if (bool) {
					str += ",";
				}
				str += "'" + s + "'";
				bool = true;
			}
		}
		return str;
	}

	public static void addFlashAttribute(HttpServletRequest request,
			String key, Object value) {
		request.getSession().setAttribute(key, ObjectUtils.toString(value, ""));
	}

	/**
	 * 翻译状态
	 * 
	 * @param enable
	 * @param module
	 * @return
	 */
	public static String getEnableStatus(String enable, String module) {
		String status = "";
		if (enable == null) {
			return status;
		}
		List list = null;
		if (ModuleContants.TURNTOU_DINGDAN.equals(module)) {
			list = ModuleContants.getFKStatusList();
		} 
		else if (ModuleContants.DIANPING_PINGLUN.equals(module)) {
			list = ModuleContants.getDianpingStatusList();
		}
		else if (ModuleContants.TURNTOU_DINGDAN_TGFH.equals(module)) {
			list = ModuleContants.getHWStatusList();
		} else if (ModuleContants.MEMBER_STATUS.equals(module)) {
			list = ModuleContants.getMemberStatusList();
		}else if(ModuleContants.TTX_STATUS.equals(module)){
			list = ModuleContants.getTtxStatusList();
		}
		for (int i = 0; i < list.size(); i++) {
			Map tuanMap = (Map) list.get(i);
			if (StringUtils.trim(enable).equals(tuanMap.get("id"))) {
				status = ObjectUtils.toString(tuanMap.get("name"));
				break;
			}
		}
		return status;
	}

	/**
	 * 日期格式化
	 * 
	 * @param dateStr
	 * @param type
	 * @return
	 */
	public static String formatDate(Object dateStr, String type,
			Boolean isFormat) {
		if (dateStr != null) {
			String str = "";
			if ("ymdhms".equals(type)) {
				str = ymdhms.format(dateStr);
			} else if ("ymdhm".equals(type)) {
				str = ymdhm.format(dateStr);
			} else if ("mdhm".equals(type)) {
				str = mdhm.format(dateStr);
			} else if ("ymd".equals(type)) {
				str = ymd.format(dateStr);
			} else if ("mdhms".equals(type)) {
				str = mdhms.format(dateStr);
			}
			if (isFormat) {
				str = StringUtils.replace(str, "-", "");
				str = StringUtils.replace(str, ":", "");
				str = StringUtils.replace(str, " ", "");
			}
			return str;
		} else {
			return "";
		}
	}

	/**
	 * 保留小数
	 * 
	 * @param obj
	 * @param decimal
	 * @return
	 */
	public static String getBigDecimal(Object obj, int decimal){
		try{
		if(obj == null || "".equals(obj)){
			return "0";
		}
		if(isNumeric(String.valueOf(obj))){
			obj = 0;
		}
		BigDecimal df = new BigDecimal(String.valueOf(obj));
		df = df.setScale(decimal,  BigDecimal.ROUND_HALF_UP);
		return df.toString();
		}catch(Exception e){
			return "0";
		}
	}

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}
	
	/**
	 * 获得指定位数的随机数字
	 * @param strLength
	 * @return
	 */
	public static String getRandomString(int strLength){
		Random rm=new Random();
		// 获得随机数  
	    double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);  
	    // 将获得的获得随机数转化为字符串  
	    String fixLenthString = String.valueOf(pross);  
	  
	    // 返回固定的长度的随机数  
	    return fixLenthString.substring(1, strLength + 1);  
	}
}
