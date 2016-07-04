package com.thinkgem.jeesite.common.utils.mvel;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.mvel2.ConversionHandler;

public class TimeStampConversion
  implements ConversionHandler
{
  
  private static String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";
	
  public Object convertFrom(Object in)
  {
    if ((in == null) || ("".equals(in)))
      return null;
    try
    {
      String t = (String)in;
      if ((t.length() == 10) && (dateTimeFormat.length() == 19)) {
        in = in + " 00:00:00";
      }
      Date date = new SimpleDateFormat(dateTimeFormat).parse((String)in);
      return new Timestamp(date.getTime()); } catch (ParseException e) {
    }catch(Exception e){
    	throw new RuntimeException(e);	
    }
	return null;
  }

  public boolean canConvertFrom(Class cls)
  {
    return true;
  }
}