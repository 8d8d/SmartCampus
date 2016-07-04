/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.entity.ctable;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;

import com.thinkgem.jeesite.modules.school.dao.calendar.SchoolCalendarDao;
import com.thinkgem.jeesite.modules.school.entity.calendar.SchoolCalendar;
import com.thinkgem.jeesite.modules.school.service.calendar.SchoolCalendarService;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;
import com.thinkgem.jeesite.common.utils.DateUtils;

/**
 * 课程表Entity
 * @author 王超然
 * @version 2016-05-12
 */
public class SchoolCtable extends DataEntity<SchoolCtable> {
	
	private static final long serialVersionUID = 1L;
	private String courseId;		// 课程名称
	private String weekday;		// 星期
	private String weekNum;		// 第几周
	private String courseOrder;		// 课节
	private String start;		// 上课时间
	private String classroomId;		// 教室
	private Office office;		// 班级
	private Date courseDate;		// 日期
	private String teacherId;		// 任课教师
	
	
	public SchoolCtable() {
		super();
	}

	public SchoolCtable(String id){
		super(id);
	}

	@Length(min=0, max=64, message="课程名称长度必须介于 0 和 64 之间")
	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	
	@Length(min=0, max=1, message="星期长度必须介于 0 和 1 之间")
	public String getWeekday() {
		//return DateUtils.getWeekOfDate(this.getCourseDate());
		return weekday;
	}

	public void setWeekday(String weekday) {
		this.weekday = weekday;
	}
	
	@Length(min=0, max=3, message="第几周长度必须介于 0 和 3 之间")
	public String getWeekNum() {
		return weekNum;
		/*if(this.getCourseDate()!=null){
			return String.valueOf(DateUtils.getDistanceOfTwoDate(this.getCourseDate(),this.getCourseDate())/7);
		}else{
			return null;
		}*/
		//(Date)schoolCalendarDao.get("1").getStart()
	}

	public void setWeekNum(String weekNum) {
		this.weekNum = weekNum;
	}
	
	@Length(min=0, max=3, message="课节长度必须介于 0 和 3 之间")
	public String getCourseOrder() {
		return courseOrder;
	}

	public void setCourseOrder(String courseOrder) {
		this.courseOrder = courseOrder;
	}
	
	@Length(min=0, max=64, message="上课时间长度必须介于 0 和 64 之间")
	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}
	
	@Length(min=0, max=64, message="教室长度必须介于 0 和 64 之间")
	public String getClassroomId() {
		return classroomId;
	}

	public void setClassroomId(String classroomId) {
		this.classroomId = classroomId;
	}
	
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office) {
		this.office = office;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCourseDate() {
		return courseDate;
	}

	public void setCourseDate(Date courseDate) {
		this.courseDate = courseDate;
	}
	
	@Length(min=0, max=64, message="任课教师长度必须介于 0 和 64 之间")
	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	
}