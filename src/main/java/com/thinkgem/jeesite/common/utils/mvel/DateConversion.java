package com.thinkgem.jeesite.common.utils.mvel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.mvel2.ConversionHandler;

public class DateConversion
  implements ConversionHandler
{ 
  private static String dateFormat = "yyyy-MM-dd";
  
  public Object convertFrom(Object in)
  {
    if ((in == null) || ("".equals(in)))
      return null;
    try
    {
      return new SimpleDateFormat(dateFormat).parse((String)in); } catch (ParseException e) {
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