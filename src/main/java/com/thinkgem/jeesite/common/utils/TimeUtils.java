/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 时间计算工具类
 * @author ThinkGem
 * @version 2013-11-03
 */
public class TimeUtils {
	
	public static String toTimeString(long time) {
		TimeUtils t = new TimeUtils(time);
		int day = t.get(TimeUtils.DAY);
		int hour = t.get(TimeUtils.HOUR);
		int minute = t.get(TimeUtils.MINUTE);
		int second = t.get(TimeUtils.SECOND);
		StringBuilder sb = new StringBuilder();
		if (day > 0){
			sb.append(day).append("天");
		}
		if (hour > 0){
			sb.append(hour).append("时");
		}
		if (minute > 0){
			sb.append(minute).append("分");
		}
		if (second > 0){
			sb.append(second).append("秒");
		}
		return sb.toString();
	}
	
    /**
     * 时间字段常量，表示“秒”
     */
    public final static int SECOND = 0;

    /**
     * 时间字段常量，表示“分”
     */
    public final static int MINUTE = 1;

    /**
     * 时间字段常量，表示“时”
     */
    public final static int HOUR = 2;

    /**
     * 时间字段常量，表示“天”
     */
    public final static int DAY = 3;

    /**
     * 各常量允许的最大值
     */
    private final int[] maxFields = { 59, 59, 23, Integer.MAX_VALUE - 1 };

    /**
     * 各常量允许的最小值
     */
    private final int[] minFields = { 0, 0, 0, Integer.MIN_VALUE };

    /**
     * 默认的字符串格式时间分隔符
     */
    private String timeSeparator = ":";

    /**
     * 时间数据容器
     */
    private int[] fields = new int[4];

    /**
     * 无参构造，将各字段置为 0
     */
    public TimeUtils() {
        this(0, 0, 0, 0);
    }

    /**
     * 使用时、分构造一个时间
     * @param hour      小时
     * @param minute    分钟
     */
    public TimeUtils(int hour, int minute) {
        this(0, hour, minute, 0);
    }

    /**
     * 使用时、分、秒构造一个时间
     * @param hour      小时
     * @param minute    分钟
     * @param second    秒
     */
    public TimeUtils(int hour, int minute, int second) {
        this(0, hour, minute, second);
    }

    /**
     * 使用一个字符串构造时间<br/>
     * Time time = new Time("14:22:23");
     * @param time      字符串格式的时间，默认采用“:”作为分隔符
     */
    public TimeUtils(String time) {
        this(time, null);
//    	System.out.println(time);
    }

    /**
     * 使用时间毫秒构建时间
     * @param time
     */
    public TimeUtils(long time){
    	this(new Date(time));
    }
    
    /**
     * 使用日期对象构造时间
     * @param date
     */
    public TimeUtils(Date date){
    	this(DateFormatUtils.formatUTC(date, "HH:mm:ss"));
    }

    /**
     * 使用天、时、分、秒构造时间，进行全字符的构造
     * @param day       天
     * @param hour      时
     * @param minute    分
     * @param second    秒
     */
    public TimeUtils(int day, int hour, int minute, int second) {
        initialize(day, hour, minute, second);
    }

    /**
     * 使用一个字符串构造时间，指定分隔符<br/>
     * Time time = new Time("14-22-23", "-");
     * @param time      字符串格式的时间
     */
    public TimeUtils(String time, String timeSeparator) {
        if(timeSeparator != null) {
            setTimeSeparator(timeSeparator);
        }
        parseTime(time);
    }

    /**
     * 设置时间字段的值
     * @param field     时间字段常量
     * @param value     时间字段的值
     */
    public void set(int field, int value) {
        if(value < minFields[field]) {
            throw new IllegalArgumentException(value + ", time value must be positive.");
        }
        fields[field] = value % (maxFields[field] + 1);
        // 进行进位计算
        int carry = value / (maxFields[field] + 1);
        if(carry > 0) {
            int upFieldValue = get(field + 1);
            set(field + 1, upFieldValue + carry);
        }
    }

    /**
     * 获得时间字段的值
     * @param field     时间字段常量
     * @return          该时间字段的值
     */
    public int get(int field) {
        if(field < 0 || field > fields.length - 1) {
            throw new IllegalArgumentException(field + ", field value is error.");
        }
        return fields[field];
    }

    /**
     * 将时间进行“加”运算，即加上一个时间
     * @param time      需要加的时间
     * @return          运算后的时间
     */
    public TimeUtils addTime(TimeUtils time) {
    	TimeUtils result = new TimeUtils();
        int up = 0;     // 进位标志
        for (int i = 0; i < fields.length; i++) {
            int sum = fields[i] + time.fields[i] + up;
            up = sum / (maxFields[i] + 1);
            result.fields[i] = sum % (maxFields[i] + 1);
        }
        return result;
    }

    /**
     * 将时间进行“减”运算，即减去一个时间
     * @param time      需要减的时间
     * @return          运算后的时间
     */
    public TimeUtils subtractTime(TimeUtils time) {
    	TimeUtils result = new TimeUtils();
        int down = 0;       // 退位标志
        for (int i = 0, k = fields.length - 1; i < k; i++) {
            int difference = fields[i] + down;
            if (difference >= time.fields[i]) {
                difference -= time.fields[i];
                down = 0;
            } else {
                difference += maxFields[i] + 1 - time.fields[i];
                down = -1;
            }
            result.fields[i] = difference;
        }
        result.fields[DAY] = fields[DAY] - time.fields[DAY] + down;
        return result;
    }

    /**
     * 获得时间字段的分隔符
     * @return
     */
    public String getTimeSeparator() {
        return timeSeparator;
    }

    /**
     * 设置时间字段的分隔符（用于字符串格式的时间）
     * @param timeSeparator     分隔符字符串
     */
    public void setTimeSeparator(String timeSeparator) {
        this.timeSeparator = timeSeparator;
    }

    private void initialize(int day, int hour, int minute, int second) {
        set(DAY, day);
        set(HOUR, hour);
        set(MINUTE, minute);
        set(SECOND, second);
    }

    private void parseTime(String time) {
        if(time == null) {
            initialize(0, 0, 0, 0);
            return;
        }
        String t = time;
        int field = DAY;
        set(field--, 0);
        int p = -1;
        while((p = t.indexOf(timeSeparator)) > -1) {
            parseTimeField(time, t.substring(0, p), field--);
            t = t.substring(p + timeSeparator.length());
        }
        parseTimeField(time, t, field--);
    }

    private void parseTimeField(String time, String t, int field) {
        if(field < SECOND || t.length() < 1) {
            parseTimeException(time);
        }
        char[] chs = t.toCharArray();
        int n = 0;
        for(int i = 0; i < chs.length; i++) {
            if(chs[i] <= ' ') {
                continue;
            }
            if(chs[i] >= '0' && chs[i] <= '9') {
                n = n * 10 + chs[i] - '0';
                continue;
            }
            parseTimeException(time);
        }
        set(field, n);
    }

    private void parseTimeException(String time) {
        throw new IllegalArgumentException(time + ", time format error, HH"
                + this.timeSeparator + "mm" + this.timeSeparator + "ss");
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(16);
        sb.append(fields[DAY]).append(',').append(' ');
        buildString(sb, HOUR).append(timeSeparator);
        buildString(sb, MINUTE).append(timeSeparator);
        buildString(sb, SECOND);
        return sb.toString();
    }

    private StringBuilder buildString(StringBuilder sb, int field) {
        if(fields[field] < 10) {
            sb.append('0');
        }
        return sb.append(fields[field]);
    }

    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + Arrays.hashCode(fields);
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final TimeUtils other = (TimeUtils) obj;
        if (!Arrays.equals(fields, other.fields)) {
            return false;
        }
        return true;
    }
    
    /**
     * 判断日期是第几周<br>
     * @param week，当前周<br>
     * @param date，要计算的日期<br>
     * @return returnList
     */
    public static int WeekOfNumber(int week,Date date){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
        Calendar cal = Calendar.getInstance();
        Calendar cal1 =Calendar.getInstance();
        cal1.setTime(date);
        int day1=cal.get(Calendar.DAY_OF_WEEK);
        int day2=cal1.get(Calendar.DAY_OF_WEEK);
        int temp=day2-day1;
        System.out.println(sdf.format(cal.getTime()));
        System.out.println(sdf.format(cal1.getTime()));
        System.out.println(temp);
        int count=week;
    	cal.add(Calendar.DATE, temp);
        while(true){

            System.out.println(sdf.format(cal.getTime()));
            System.out.println(sdf.format(cal1.getTime()));
        	if(cal.get(Calendar.MONTH)==cal1.get(Calendar.MONTH)&&cal.get(Calendar.DAY_OF_MONTH)==cal1.get(Calendar.DAY_OF_MONTH)){
                System.out.println(sdf.format(cal.getTime()));
                System.out.println(sdf.format(cal1.getTime()));
        		break;
        	}
        	else{
        		count++;
        		cal.add(Calendar.DATE, 7);
        	}
        }
        return count;
    }
    
    
    /**
     * 获取周一到周日的日期列表<br>
     * 1、获取当前日期<br>
     * 2、加上week*7得到第week周的日期<br>
     * 3、计算周一的日期，依次加一得到一整周的日期
     * @param week，要计算的是第几周<br>
     * @return returnList
     */
    public static String WeekOfDate(int week,int weekDay){//返回本周一到周日的日期
        SimpleDateFormat sdf=new SimpleDateFormat("MM-dd"); //设置时间格式
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, week*7);
        System.out.println("要计算日期为:" + sdf.format(cal.getTime())); //输出要计算日期

        //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        if(1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }

        cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一

        int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, weekDay-1);
        return sdf.format(cal.getTime());
    }
    
    /**
     * 把timestamp格式的时间转化为相应格式的时间 <br>
     * 1、通过pattern将timestamp转化为对应格式<br>
     * 2、如果pattern为空，使用默认的格式<br>
     * @param time，时间戳<br>
     * @param pattern，匹配格式<br>
     * @return string
     * @throws IllegalArgumentException 如果pattern不合法
     */
    public static String timestamp2str(Date time, String pattern) {
//        if (pattern != null && !"".equals(pattern)) {
//            if (!"yyyyMMddHHmmss".equals(pattern)
//                    && !"yyyy-MM-dd HH:mm:ss".equals(pattern)
//                    && !"yyyy-MM-dd".equals(pattern)
//                    && !"MM/dd/yyyy".equals(pattern)){
//                throw new IllegalArgumentException("Date format ["+pattern+"] is invalid");
//            }
//        }else{
//            pattern = "yyyy-MM-dd HH:mm:ss";
//        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(cal.getTime());
    }
    
    //判断日期是星期几
    public static int dayOfWeek(Date date){
    	  Calendar c = Calendar.getInstance();
    	  c.setTime(date);
    	  int dayForWeek = 0;
    	  if(c.get(Calendar.DAY_OF_WEEK) == 1){
    	   dayForWeek = 7;
    	  }else{
    	   dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
    	  }
    	  return dayForWeek;
    }
    
    public static Date stringToDate(String date,String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date2 = null;
        try {
        	date2=sdf.parse("2016-6-29 上午");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return date2;
  }
    
    public static void main(String[] args) { 
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd a");
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse("2016-6-29 上午"));
            cal.setFirstDayOfWeek(Calendar.MONDAY);
			System.out.println(cal.get(Calendar.DAY_OF_WEEK));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    } 
}