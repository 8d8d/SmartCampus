package com.thinkgem.jeesite.common.model;


import java.io.Serializable;

public class ResultSure
  implements Serializable
{
   private boolean isSuccess = false;
   private Object resultObject = null;

  public ResultSure(boolean isSuccess, Object resultObject) {
     this.isSuccess = isSuccess;
     this.resultObject = resultObject;
  }

  public boolean isSuccess() {
     return this.isSuccess;
  }

  public Object getResultObject() {
     return this.resultObject;
  }
}
