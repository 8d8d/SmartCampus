/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.web.ctable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.tagplugins.jstl.core.Out;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sun.jmx.snmp.internal.SnmpSecuritySubSystem;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.TimeUtils;
import com.thinkgem.jeesite.common.utils.tran.TransLateCondition;
import com.thinkgem.jeesite.common.utils.tran.TranslateUtil;
import com.thinkgem.jeesite.modules.school.entity.calendar.SchoolCalendar;
import com.thinkgem.jeesite.modules.school.entity.classroom.SchoolClassroom;
import com.thinkgem.jeesite.modules.school.entity.ctable.SchoolCtable;
import com.thinkgem.jeesite.modules.school.entity.currentweek.SchoolCurrentWeek;
import com.thinkgem.jeesite.modules.school.entity.lessonstudent.SchoolLessonStudent;
import com.thinkgem.jeesite.modules.school.entity.lessontime.SchoolLessonTime;
import com.thinkgem.jeesite.modules.school.service.classroom.SchoolClassroomService;
import com.thinkgem.jeesite.modules.school.service.classtype.SchoolCTypeService;
import com.thinkgem.jeesite.modules.school.service.ctable.SchoolCtableService;
import com.thinkgem.jeesite.modules.school.service.currentweek.SchoolCurrentWeekService;
import com.thinkgem.jeesite.modules.school.service.lessonstudent.SchoolLessonStudentService;
import com.thinkgem.jeesite.modules.school.service.lessontime.SchoolLessonTimeService;
import com.thinkgem.jeesite.modules.sys.entity.Role;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 课程表Controller
 * @author 王超然
 * @version 2016-05-12
 */
@Controller
@RequestMapping(value = "${adminPath}/school/ctable/schoolCtable")
public class SchoolCtableController extends BaseController {

	@Autowired
	private SchoolCurrentWeekService schoolCurrentWeekService;
	
	@Autowired
	private SchoolCtableService schoolCtableService;
	
	@Autowired
	private SchoolClassroomService schoolClassroomService;
	
	@Autowired
	private SchoolCTypeService schoolCTypeService;
	
	
	@Autowired
	private SchoolLessonTimeService schoolLessonTimeService;
	
	@Autowired
	private SchoolLessonStudentService schoolLessonStudentService;
	
	@ModelAttribute
	public SchoolCtable get(@RequestParam(required=false) String id) {
		SchoolCtable entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = schoolCtableService.get(id);
		}
		if (entity == null){
			entity = new SchoolCtable();
		}
		return entity;
	}
	
	@RequiresPermissions("school:ctable:schoolCtable:view")
	@RequestMapping(value = {"list", ""})
	public String list(SchoolCtable schoolCtable, HttpServletRequest request, HttpServletResponse response, Model model) {
		String week = "1";
		try{
			if(schoolCtable.getWeekNum()!=null){
				week=schoolCtable.getWeekNum();
			}
			else 
				week=schoolCurrentWeekService.findList(new SchoolCurrentWeek()).get(0).getWeek();
		}
		catch(Exception e){

		}


		User user = UserUtils.getUser();
		schoolCtable.setTeacherId(user.getId());
		schoolCtable.setWeekNum(null);
		
		Page<SchoolCtable> page = schoolCtableService.findPage(new Page<SchoolCtable>(request, response), schoolCtable); 
		List<SchoolCtable> list = page.getList();
		for(int i=0;i<list.size();i++){
			System.out.println(list.size());
			String[] weeks=list.get(i).getWeekNum().split(",");
			if(Integer.parseInt(week)<Integer.parseInt(weeks[0])||Integer.parseInt(week)>Integer.parseInt(weeks[1]))
				list.remove(list.get(i));
			System.out.println(list.size());
		}
		list = TranslateUtil.t.translateList(list,new TransLateCondition("courseId","school_c_type a","a.name as courseId","id",""));
		list = TranslateUtil.t.translateList(list,new TransLateCondition("start","school_lesson_time a","a.name as start","id",""));
		list = TranslateUtil.t.translateList(list,new TransLateCondition("classroomId","school_classroom a","a.name as classroomId","id",""));
		list = TranslateUtil.t.translateList(list,new TransLateCondition("teacherId","sys_user a","a.name as teacherId","id",""));
		model.addAttribute("page", page);
		return "modules/school/ctable/schoolCtableList";
	}

	@RequiresPermissions("school:ctable:schoolCtable:view")
	@RequestMapping(value = "form")
	public String form(SchoolCtable schoolCtable, Model model) {
		schoolCtable = (SchoolCtable) TranslateUtil.t.translateListT(schoolCtable,new TransLateCondition("courseId","school_c_type a","a.name as courseId","id",""));
		schoolCtable = (SchoolCtable) TranslateUtil.t.translateListT(schoolCtable,new TransLateCondition("start","school_lesson_time a","a.name as start","id",""));
		schoolCtable = (SchoolCtable) TranslateUtil.t.translateListT(schoolCtable,new TransLateCondition("classroomId","school_classroom a","a.name as classroomId","id",""));
		schoolCtable = (SchoolCtable) TranslateUtil.t.translateListT(schoolCtable,new TransLateCondition("teacherId","sys_user a","a.name as teacherId","id",""));
		model.addAttribute("schoolCtable", schoolCtable);
		return "modules/school/ctable/schoolCtableForm";
	}

	@RequiresPermissions("school:ctable:schoolCtable:edit")
	@RequestMapping(value = "save")
	public String save(SchoolCtable schoolCtable, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, schoolCtable)){
			return form(schoolCtable, model);
		}
		if(schoolCtable.getCourseId()==null){
			addMessage(redirectAttributes, "保存失败，课程不能为空");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		
		if(schoolCtable.getClassroomId()==null){
			addMessage(redirectAttributes, "保存失败，教室不能为空");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		
		if(schoolCtable.getTeacherId()==null){
			addMessage(redirectAttributes, "保存失败，课程教师不能为空");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		
		if(schoolCtable.getStart()==null){
			addMessage(redirectAttributes, "保存失败，上课时间不能为空");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		
		if(Integer.parseInt(schoolCtable.getWeekday())>7){
			addMessage(redirectAttributes, "保存失败，星期不能超过7");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		
		try {
			String[] temp=schoolCtable.getWeekNum().split(",");
			if(temp.length!=2){
				addMessage(redirectAttributes, "保存失败，起始周格式错误");
				return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
			}
			Integer.parseInt(temp[0]);
			Integer.parseInt(temp[1]);
		} catch (Exception e) {
			System.out.println(e.toString());
			addMessage(redirectAttributes, "保存失败，起始周格式错误");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
			// TODO: handle exception
		}
		schoolCtable = (SchoolCtable) TranslateUtil.t.translateListT(schoolCtable,new TransLateCondition("courseId","school_c_type a","a.id as courseId","name",""));
		schoolCtable = (SchoolCtable) TranslateUtil.t.translateListT(schoolCtable,new TransLateCondition("start","school_lesson_time a","a.id as start","name",""));
		schoolCtable = (SchoolCtable) TranslateUtil.t.translateListT(schoolCtable,new TransLateCondition("classroomId","school_classroom a","a.id as classroomId","name",""));
		schoolCtable = (SchoolCtable) TranslateUtil.t.translateListT(schoolCtable,new TransLateCondition("teacherId","sys_user a","a.id as teacherId","name",""));
		schoolCtableService.save(schoolCtable);
		addMessage(redirectAttributes, "保存课程表成功");
		return "redirect:"+Global.getAdminPath()+"/school/ctable/schoolCtable/?repage";
	}
	
	@RequiresPermissions("school:ctable:schoolCtable:edit")
	@RequestMapping(value = "delete")
	public String delete(SchoolCtable schoolCtable, RedirectAttributes redirectAttributes) {
		schoolCtableService.delete(schoolCtable);
		addMessage(redirectAttributes, "删除课程表成功");
		return "redirect:"+Global.getAdminPath()+"/school/ctable/schoolCtable/?repage";
	}

	
	/**
	 * 请求课表接口
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "getCtable", method = RequestMethod.GET)
	@ResponseBody
	public String getCtable(String weekNum, Model model) {
		User user = UserUtils.getUser();
		List<SchoolCtable> list = new ArrayList<SchoolCtable>();
		if (user.getRoleNames().contains("老师")) {
			SchoolCtable schoolCtable = new SchoolCtable();
			schoolCtable.setTeacherId(user.getId());
			list = schoolCtableService.findList(schoolCtable);

		}
		if (user.getRoleNames().contains("学生")) {
			SchoolLessonStudent schoolLessonStudent = new SchoolLessonStudent();
			schoolLessonStudent.setStudentid(user.getId());
			List<SchoolLessonStudent> list2 = schoolLessonStudentService.findList(schoolLessonStudent);
			for (SchoolLessonStudent schoolLessonStudent2 : list2) {
				SchoolCtable schoolCtable = schoolCtableService.get(schoolLessonStudent2.getLessonid());
				list.add(schoolCtable);
			}
		}

		for (int i = 0; i < list.size(); i++) {
			String[] weeks = list.get(i).getWeekNum().split(",");
			if (Integer.parseInt(weekNum) < Integer.parseInt(weeks[0])
					|| Integer.parseInt(weekNum) > Integer.parseInt(weeks[1])) {
				list.remove(i);
				i--;
			}
		}
		list = TranslateUtil.t.translateList(list,
				new TransLateCondition("courseId", "school_c_type a", "a.name as courseId", "id", ""));
		list = TranslateUtil.t.translateList(list,
				new TransLateCondition("start", "school_lesson_time a", "a.start as start,a.end as end", "id", ""));
		list = TranslateUtil.t.translateList(list,
				new TransLateCondition("classroomId", "school_classroom a", "a.name as classroomId", "id", ""));
		list = TranslateUtil.t.translateList(list,
				new TransLateCondition("teacherId", "sys_user a", "a.name as teacherId", "id", ""));

		Map<String, Object> map1 = Maps.newHashMap();
		map1.put("code", 1);
		map1.put("message", "success");
		map1.put("data", list);
		return JsonMapper.getInstance().toJson(map1);
	}
	
	/**
	 * 获取所有课程
	 * @param officeId
	 * @param response
	 * @return
	 */
	@RequiresPermissions("school:ctable:schoolCtable:view")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();

	
		List<SchoolCtable> list=schoolCtableService.findList(new SchoolCtable());
		list = TranslateUtil.t.translateList(list,new TransLateCondition("courseId","school_c_type a","a.name as courseId","id",""));
		list = TranslateUtil.t.translateList(list,new TransLateCondition("start","school_lesson_time a","a.start as start,a.end as end","id",""));
		list = TranslateUtil.t.translateList(list,new TransLateCondition("classroomId","school_classroom a","a.name as classroomId","id",""));
		list = TranslateUtil.t.translateList(list,new TransLateCondition("teacherId","sys_user a","a.name as teacherId","id",""));
		for (SchoolCtable e : list) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", 0);
			map.put("name", e.getCourseId()+","+e.getTeacherId()+",星期"+e.getWeekday()+",第"+e.getCourseOrder()+"节");
			mapList.add(map);	
			
//			SchoolLessonStudent schoolLessonStudent=new SchoolLessonStudent();
//			schoolLessonStudent.setLessonid(e.getId());
//			List<SchoolLessonStudent> lessonList=schoolLessonStudentService.findList(schoolLessonStudent);
//			for(SchoolLessonStudent schoolLessonStudent2:lessonList){
//				Map<String, Object> map1 = Maps.newHashMap();
//				map1.put("id",schoolLessonStudent2.getId());
//				map1.put("pId", e.getId());
//				map1.put("name",UserUtils.get(schoolLessonStudent2.getStudentid()).getName());
//				mapList.add(map1);	
//			}
		}
		
		return mapList;
	}
	
	/**
	 * 获取所有课程的学生
	 * @param officeId
	 * @param response
	 * @return
	 */
	@RequiresPermissions("school:ctable:schoolCtable:view")
	@ResponseBody
	@RequestMapping(value = "treeDataStudent")
	public List<Map<String, Object>> treeDataStudent(HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();

	
		List<SchoolCtable> list=schoolCtableService.findList(new SchoolCtable());
		list = TranslateUtil.t.translateList(list,new TransLateCondition("courseId","school_c_type a","a.name as courseId","id",""));
		list = TranslateUtil.t.translateList(list,new TransLateCondition("start","school_lesson_time a","a.start as start,a.end as end","id",""));
		list = TranslateUtil.t.translateList(list,new TransLateCondition("classroomId","school_classroom a","a.name as classroomId","id",""));
		list = TranslateUtil.t.translateList(list,new TransLateCondition("teacherId","sys_user a","a.name as teacherId","id",""));
		for (SchoolCtable e : list) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", 0);
			map.put("name", e.getCourseId()+","+e.getTeacherId()+",星期"+e.getWeekday()+",第"+e.getCourseOrder()+"节");

			
			SchoolLessonStudent schoolLessonStudent=new SchoolLessonStudent();
			schoolLessonStudent.setLessonid(e.getId());
			List<SchoolLessonStudent> lessonList=schoolLessonStudentService.findList(schoolLessonStudent);
			int count=0;
			for(SchoolLessonStudent schoolLessonStudent2:lessonList){
				Map<String, Object> map1 = Maps.newHashMap();
				map1.put("id",schoolLessonStudent2.getStudentid());
				map1.put("pId", e.getId());
				map1.put("name",UserUtils.get(schoolLessonStudent2.getStudentid()).getName());
				mapList.add(map1);	
				count++;
			}
			if(count!=0){
				mapList.add(map);	
			}
		}
		
		return mapList;
	}
	
	/**
	 * 请求明天课表
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "getTomorrowCtable", method = RequestMethod.GET)
	@ResponseBody
	public String getTomorrowCtable(Model model) {
		User user = UserUtils.getUser();
		String week="1";
		try{
			week=schoolCurrentWeekService.findList(new SchoolCurrentWeek()).get(0).getWeek();
		}
		catch(Exception e){
			week="1";
		}
	
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE,1);
		
		int weekNum=TimeUtils.WeekOfNumber(Integer.parseInt(week),cal.getTime());
		int weekDay=TimeUtils.dayOfWeek(cal.getTime());
		System.out.println(weekNum+""+weekDay);
		SchoolLessonStudent schoolLessonStudent=new SchoolLessonStudent();
		schoolLessonStudent.setStudentid(user.getId());
		List<SchoolLessonStudent> lessonStudents=schoolLessonStudentService.findList(schoolLessonStudent);
		
		ArrayList<Map<String, Object>> maps=new ArrayList<Map<String,Object>>();
		for(SchoolLessonStudent schoolLessonStudent2:lessonStudents){
			SchoolCtable schoolCtable=schoolCtableService.get(schoolLessonStudent2.getLessonid());
			if(Integer.parseInt(schoolCtable.getWeekday())==weekDay){
				String[] temp=schoolCtable.getWeekNum().split(",");
				if(weekNum>Integer.parseInt(temp[0])&&weekNum<Integer.parseInt(temp[1])){
					Map<String, Object> map2 = Maps.newHashMap();
					TranslateUtil.t.translateListT(schoolCtable,new TransLateCondition("courseId","school_c_type a","a.name as courseId","id",""));
					TranslateUtil.t.translateListT(schoolCtable,new TransLateCondition("start","school_lesson_time a","a.start as start,a.end as weekday","id",""));
					TranslateUtil.t.translateListT(schoolCtable,new TransLateCondition("classroomId","school_classroom a","a.name as classroomId","id",""));
					
					map2.put("classRoom",schoolCtable.getClassroomId());
					map2.put("courseName",schoolCtable.getCourseId());
					map2.put("start",schoolCtable.getStart());
					map2.put("end",schoolCtable.getWeekday());
					maps.add(map2);
				}
			}
		}


		Map<String, Object> map1 = Maps.newHashMap();
		map1.put("code", 1);
		map1.put("message", "success");
		map1.put("data", maps);
		return JsonMapper.getInstance().toJson(map1);
	}
	
}