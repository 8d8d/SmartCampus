/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.entity.onclass;

import java.util.ArrayList;

import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 考勤Entity
 * @author 王超然
 * @version 2016-06-08
 */
public class SchoolOnclass extends DataEntity<SchoolOnclass> {
	
	private static final long serialVersionUID = 1L;
	private String studentId;		// 学生姓名
	private String result;		// 考勤结果
	private String week;		// week
	private String lessonId;		// 课程名称
	
	public SchoolOnclass() {
		super();
	}

	public SchoolOnclass(String id){
		super(id);
	}

	@Length(min=0, max=64, message="学生姓名长度必须介于 0 和 64 之间")
	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	
	@Length(min=0, max=64, message="考勤结果长度必须介于 0 和 64 之间")
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	@Length(min=0, max=255, message="week长度必须介于 0 和 255 之间")
	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}
	
	@Length(min=0, max=64, message="课程名称长度必须介于 0 和 64 之间")
	public String getLessonId() {
		return lessonId;
	}

	public void setLessonId(String lessonId) {
		this.lessonId = lessonId;
	}
	
	public ArrayList<String> getResultList() {
		ArrayList<String> resultList=Lists.newArrayList();
		if(result==null||result.equals("")){
			return resultList;
		}
		String[] temp=result.split(",");

		for(int i=0;i<temp.length;i++){
			resultList.add(temp[i]);
		}
		return resultList;
	}

	public void setResultList(ArrayList<String> resultList) {
		 this.result="";
	     for(String r:resultList){
	    	 this.result=this.result+r+",";
	     }
	}
	
}