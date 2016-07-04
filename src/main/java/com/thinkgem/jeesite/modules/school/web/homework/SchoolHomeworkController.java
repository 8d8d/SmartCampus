/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.web.homework;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.TimeUtils;
import com.thinkgem.jeesite.common.utils.tran.TransLateCondition;
import com.thinkgem.jeesite.common.utils.tran.TranslateUtil;
import com.thinkgem.jeesite.modules.school.entity.classtype.SchoolCType;
import com.thinkgem.jeesite.modules.school.entity.ctable.SchoolCtable;
import com.thinkgem.jeesite.modules.school.entity.currentweek.SchoolCurrentWeek;
import com.thinkgem.jeesite.modules.school.entity.homework.SchoolHomework;
import com.thinkgem.jeesite.modules.school.entity.lessontime.SchoolLessonTime;
import com.thinkgem.jeesite.modules.school.entity.onclass.SchoolOnclass;
import com.thinkgem.jeesite.modules.school.entity.tips.SchoolTips;
import com.thinkgem.jeesite.modules.school.service.classtype.SchoolCTypeService;
import com.thinkgem.jeesite.modules.school.service.ctable.SchoolCtableService;
import com.thinkgem.jeesite.modules.school.service.currentweek.SchoolCurrentWeekService;
import com.thinkgem.jeesite.modules.school.service.homework.SchoolHomeworkService;
import com.thinkgem.jeesite.modules.school.service.lessontime.SchoolLessonTimeService;
import com.thinkgem.jeesite.modules.school.service.onclass.SchoolOnclassService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 作业Controller
 * @author 王超然
 * @version 2016-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/school/homework/schoolHomework")
public class SchoolHomeworkController extends BaseController {

	@Autowired
	private SchoolHomeworkService schoolHomeworkService;

	
	@Autowired
	private SchoolCurrentWeekService schoolCurrentWeekService;
	
	@Autowired
	private SchoolLessonTimeService schoolLessonTimeService;
	
	@Autowired
	private SchoolCtableService schoolCtableService;
	
	@Autowired
	private SchoolCTypeService schoolCTypeService;
	
	@ModelAttribute
	public SchoolHomework get(@RequestParam(required=false) String id) {
		SchoolHomework entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = schoolHomeworkService.get(id);
		}
		if (entity == null){
			entity = new SchoolHomework();
		}
		return entity;
	}
	
	@RequiresPermissions("school:homework:schoolHomework:view")
	@RequestMapping(value = {"list", ""})
	public String list(SchoolHomework schoolHomework, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SchoolHomework> page = schoolHomeworkService.findPage(new Page<SchoolHomework>(request, response), schoolHomework); 
		List<SchoolHomework> list = page.getList();
		User user = UserUtils.getUser();
		if (user.getRoleNames().contains("老师"))
			for (int i = list.size() - 1; i >= 0; i--) {
				SchoolCtable schoolCtable = schoolCtableService.get(list.get(i).getLessonId());
				if (!user.getId().equals(schoolCtable.getTeacherId()))
					list.remove(i);
			}
		list = TranslateUtil.t.translateList(list,new TransLateCondition("lessonId","school_ctable a","a.course_id as lessonId","id",""));
		list = TranslateUtil.t.translateList(list,new TransLateCondition("lessonId","school_c_type a","a.name as lessonId","id",""));
		model.addAttribute("page", page);
		return "modules/school/homework/schoolHomeworkList";
	}

	@RequiresPermissions("school:homework:schoolHomework:view")
	@RequestMapping(value = "form")
	public String form(SchoolHomework schoolHomework, Model model) {
		if (schoolHomework.getLessonId() != null) {
			SchoolCtable schoolCtable = schoolCtableService.get(schoolHomework.getLessonId());
			SchoolCType schoolCType = schoolCTypeService.get(schoolCtable.getCourseId());
			model.addAttribute("courseName", schoolCType.getName() + ",星期" + schoolCtable.getWeekday() + ",第"
					+ schoolCtable.getCourseOrder() + "节");
		}
		model.addAttribute("schoolHomework", schoolHomework);
		return "modules/school/homework/schoolHomeworkForm";
	}

	@RequiresPermissions("school:homework:schoolHomework:edit")
	@RequestMapping(value = "save")
	public String save(SchoolHomework schoolHomework, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, schoolHomework)){
			return form(schoolHomework, model);
		}
		if(schoolHomework.getLessonId()==null){
			addMessage(redirectAttributes, "保存失败，课程不能为空");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		
		if(schoolHomework.getId()==null){
			SchoolHomework schoolHomework2=schoolHomeworkService.findList(schoolHomework).get(0);
			if(schoolHomework2==null){
				schoolHomeworkService.save(schoolHomework);
			}
			else{
				schoolHomework2.setContent(schoolHomework.getContent());
				schoolHomeworkService.save(schoolHomework2);
			}
			addMessage(redirectAttributes, "保存作业成功");
			return "redirect:"+Global.getAdminPath()+"/school/homework/schoolHomework/?repage";
		}

		schoolHomeworkService.save(schoolHomework);
		addMessage(redirectAttributes, "保存作业成功");
		return "redirect:"+Global.getAdminPath()+"/school/homework/schoolHomework/?repage";
	}
	
	@RequiresPermissions("school:homework:schoolHomework:edit")
	@RequestMapping(value = "delete")
	public String delete(SchoolHomework schoolHomework, RedirectAttributes redirectAttributes) {
		schoolHomeworkService.delete(schoolHomework);
		addMessage(redirectAttributes, "删除作业成功");
		return "redirect:"+Global.getAdminPath()+"/school/homework/schoolHomework/?repage";
	}

	/**
	 * 获取老师一周的所有课程
	 * @param 
	 * @return
	 */
	@RequiresPermissions("school:homework:schoolHomework:view")
	@RequestMapping(value = "getAllLesson",method=RequestMethod.GET)
	@ResponseBody
	public String getAllLesson(@RequestParam(required=true) String weekNum,Model model) {
		User user = UserUtils.getUser();
		SchoolCtable schoolCtable=new SchoolCtable();
		schoolCtable.setTeacherId(user.getId());
		List<SchoolCtable> listCtable=schoolCtableService.findList(schoolCtable);
		for(int i=listCtable.size()-1;i>=0;i--){
			String[] weeks=listCtable.get(i).getWeekNum().split(",");
			if(Integer.parseInt(weekNum)<Integer.parseInt(weeks[0])||Integer.parseInt(weekNum)>Integer.parseInt(weeks[1]))
			{
				listCtable.remove(i);
			}
		}
		listCtable = TranslateUtil.t.translateList(listCtable,new TransLateCondition("courseId","school_c_type a","a.name as courseId","id",""));

		listCtable = TranslateUtil.t.translateList(listCtable,new TransLateCondition("classroomId","school_classroom a","a.name as classroomId","id",""));
		listCtable = TranslateUtil.t.translateList(listCtable,new TransLateCondition("teacherId","sys_user a","a.name as teacherId","id",""));
	
		String week="1";
		try{
			week=schoolCurrentWeekService.findList(new SchoolCurrentWeek()).get(0).getWeek();
		}
		catch(Exception e){
			week="1";
		}

		ArrayList<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		for(int i=0;i<listCtable.size();i++){
			Map<String, Object> map2=Maps.newHashMap();
				map2.put("id",listCtable.get(i).getId());
				map2.put("lesson", listCtable.get(i).getCourseId());
				map2.put("week", listCtable.get(i).getWeekday());
				SchoolLessonTime schoolLessonTime=schoolLessonTimeService.get(listCtable.get(i).getStart());
				map2.put("start",schoolLessonTime.getStart());
				map2.put("end", schoolLessonTime.getEnd());
				String timeOfDay=TimeUtils.WeekOfDate(Integer.parseInt(weekNum)-Integer.parseInt(week), Integer.parseInt(listCtable.get(i).getWeekday()));
				map2.put("time", timeOfDay);
				
				SchoolHomework schoolHomework=new SchoolHomework();
				schoolHomework.setLessonId(listCtable.get(i).getId());
				
				List<SchoolHomework> homeworkList=schoolHomeworkService.findList(schoolHomework);
				SchoolHomework schoolHomework2=null;
				if(homeworkList.size()!=0)
					schoolHomework2=homeworkList.get(0);
				
				map2.put("content", schoolHomework2==null?"":schoolHomework2.getContent());
				mapList.add(map2);
		}
		
		Map<String, Object> map1 = Maps.newHashMap();
		map1.put("code", 1);
		map1.put("message", "success");
		map1.put("data", mapList);
		return 	JsonMapper.getInstance().toJson(map1);
	}
	
	/**
	 * 布置作业
	 * @param content 作业内容
	 * @return
	 */
	@RequiresPermissions("school:homework:schoolHomework:edit")
	@RequestMapping(value = "saveHomework",method=RequestMethod.POST)
	@ResponseBody
	public String saveHomework(@RequestParam(required=true) String courseId,@RequestParam(required=true) String content) {

		SchoolHomework schoolHomework=new SchoolHomework();
		schoolHomework.setLessonId(courseId);
		List<SchoolHomework> homeworkList=schoolHomeworkService.findList(schoolHomework);
		SchoolHomework schoolHomework2=null;
		
		if(homeworkList.size()!=0)
			schoolHomework2=homeworkList.get(0);
		
		if(schoolHomework2==null){
			schoolHomework.setContent(content);
		}
		else{
			schoolHomework=schoolHomework2;
			schoolHomework.setContent(content);
		}
		schoolHomeworkService.save(schoolHomework);
		
		Map<String, Object> map1 = Maps.newHashMap();
		Map<String, Object> map2 = Maps.newHashMap();
		map1.put("code", 1);
		map1.put("message", "success");
		map1.put("data",map2);
		return 	JsonMapper.getInstance().toJson(map1);
	}
}