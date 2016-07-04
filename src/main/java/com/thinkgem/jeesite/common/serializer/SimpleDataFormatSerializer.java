package com.thinkgem.jeesite.common.serializer;


import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

public class SimpleDataFormatSerializer
  implements ObjectSerializer
{
  private final String pattern;
  private final ThreadLocal<SimpleDateFormat> formatLocal = new ThreadLocal();

  public SimpleDataFormatSerializer(String pattern) {
    this.pattern = pattern;
  }

  public String getPattern() {
    return this.pattern;
  }

  public void write(JSONSerializer serializer, Object object) throws IOException {
    Date date = (Date)object;
    SimpleDateFormat format = (SimpleDateFormat)this.formatLocal.get();

    if (format == null) {
      format = new SimpleDateFormat(this.pattern);
      this.formatLocal.set(format);
    }

    String text = format.format(date);
    serializer.write(text);
  }

	public void write(JSONSerializer serializer, Object object, Object arg2, Type arg3)
			throws IOException {
		// TODO Auto-generated method stub
		  Date date = (Date)object;
		    SimpleDateFormat format = (SimpleDateFormat)this.formatLocal.get();
	
		    if (format == null) {
		      format = new SimpleDateFormat(this.pattern);
		      this.formatLocal.set(format);
		    }
	
		    String text = format.format(date);
		    serializer.write(text);
	}
}