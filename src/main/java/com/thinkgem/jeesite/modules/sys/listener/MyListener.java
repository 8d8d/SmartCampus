package com.thinkgem.jeesite.modules.sys.listener;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;  
import javax.servlet.ServletContextListener;  
  
public class MyListener implements ServletContextListener {  
  
    private java.util.Timer timer = null ;  
    public void contextDestroyed(ServletContextEvent event) {  
        // TODO Auto-generated method stub  
  
    }  
  
    public void contextInitialized(ServletContextEvent event) {  
  
//        Timer timer = new Timer();  
//        timer.schedule(new TimerTask() {  
//            public void run() {  
//                System.out.println("-------设定要指定任务--------");  
//
//            }  
//        }, 2000,2000);// 设定指定的时间time,此处为2000毫秒  
          
    }  
  
}  