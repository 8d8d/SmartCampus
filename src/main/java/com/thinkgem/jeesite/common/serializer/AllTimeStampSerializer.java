package com.thinkgem.jeesite.common.serializer;


import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;

import oracle.sql.TIMESTAMP;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

public class AllTimeStampSerializer implements ObjectSerializer {
	
	public static final AllTimeStampSerializer instance = new AllTimeStampSerializer();
	
	public void write(JSONSerializer serializer, Object object) throws IOException {
		try {
			java.sql.Timestamp t;
			System.out.println(object.getClass());
			if(object instanceof TIMESTAMP){
			TIMESTAMP o = (TIMESTAMP)object;
				t = o.timestampValue();
			}
			else if(object instanceof Date){
				Date dateNow = (Date) object;
				t = new Timestamp(dateNow.getTime());
			}
			else if(object instanceof java.util.Date){
				java.util.Date dateNow = (java.util.Date) object;
				t = new Timestamp(dateNow.getTime());
			}
			else
			{
				t = (Timestamp) object;
			}
			Date date = new Date(t.getTime());
			SimpleDataFormatSerializer time = new SimpleDataFormatSerializer("yyyy-MM-dd HH:mm:ss");
			time.write(serializer, date);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}

	public void write(JSONSerializer serializer, Object object, Object arg2, Type arg3)
			throws IOException {
		// TODO Auto-generated method stub
		write(serializer,object);
	}
	
}
