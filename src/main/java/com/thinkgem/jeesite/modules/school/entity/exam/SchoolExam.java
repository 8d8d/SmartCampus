/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.entity.exam;

import org.hibernate.validator.constraints.Length;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;

/**
 * 成绩查询Entity
 * @author 王超然
 * @version 2016-05-16
 */
public class SchoolExam extends DataEntity<SchoolExam> {
	
	private static final long serialVersionUID = 1L;
	private String courseId;		// 课程名称
	private String examId;		// 考试名称
	private String classId;		// 班级名称
	private String teacherId;		// 教师姓名
	private String studentId;		// 学生姓名
	private String score;		// 分数
	private Date updateTime;		// 更新日期
	
	public SchoolExam() {
		super();
	}

	public SchoolExam(String id){
		super(id);
	}

	@Length(min=0, max=64, message="课程名称长度必须介于 0 和 64 之间")
	public String getCourseId() {
		return courseId;
	}

	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}
	
	@Length(min=0, max=64, message="考试名称长度必须介于 0 和 64 之间")
	public String getExamId() {
		return examId;
	}

	public void setExamId(String examId) {
		this.examId = examId;
	}
	
	@Length(min=0, max=64, message="班级名称长度必须介于 0 和 64 之间")
	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}
	
	@Length(min=0, max=64, message="教师姓名长度必须介于 0 和 64 之间")
	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	
	@Length(min=0, max=64, message="学生姓名长度必须介于 0 和 64 之间")
	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	
	@Length(min=0, max=64, message="分数长度必须介于 0 和 64 之间")
	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}