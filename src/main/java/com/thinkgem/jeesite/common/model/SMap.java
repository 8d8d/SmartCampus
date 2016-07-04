package com.thinkgem.jeesite.common.model;


import java.util.HashMap;

public class SMap extends HashMap<String, Object>
{
  public SMap()
  {
	  super();
  }
  
  public SMap(Object ... args){
      super();
      for(int i=1; i<args.length; i+=2){
          this.put(String.valueOf(args[i-1]), args[i]);
      }
  }

  public SMap add(String key, Object val)
  {
    put(key, val);
    return this;
  }
  
}