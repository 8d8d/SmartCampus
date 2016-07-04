/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.web.onclass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.cmd.GetStartFormCmd;
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
import com.mysql.jdbc.TimeUtil;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.TimeUtils;
import com.thinkgem.jeesite.common.utils.tran.TransLateCondition;
import com.thinkgem.jeesite.common.utils.tran.TranslateUtil;
import com.thinkgem.jeesite.modules.school.entity.classroom.SchoolClassroom;
import com.thinkgem.jeesite.modules.school.entity.classtype.SchoolCType;
import com.thinkgem.jeesite.modules.school.entity.ctable.SchoolCtable;
import com.thinkgem.jeesite.modules.school.entity.currentweek.SchoolCurrentWeek;
import com.thinkgem.jeesite.modules.school.entity.lessonstudent.SchoolLessonStudent;
import com.thinkgem.jeesite.modules.school.entity.lessontime.SchoolLessonTime;
import com.thinkgem.jeesite.modules.school.entity.onclass.SchoolOnclass;
import com.thinkgem.jeesite.modules.school.service.classroom.SchoolClassroomService;
import com.thinkgem.jeesite.modules.school.service.classtype.SchoolCTypeService;
import com.thinkgem.jeesite.modules.school.service.ctable.SchoolCtableService;
import com.thinkgem.jeesite.modules.school.service.currentweek.SchoolCurrentWeekService;
import com.thinkgem.jeesite.modules.school.service.lessonstudent.SchoolLessonStudentService;
import com.thinkgem.jeesite.modules.school.service.lessontime.SchoolLessonTimeService;
import com.thinkgem.jeesite.modules.school.service.onclass.SchoolOnclassService;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.DictService;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 考勤Controller
 * @author 王超然
 * @version 2016-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/school/onclass/schoolOnclass")
public class SchoolOnclassController extends BaseController {

	@Autowired
	private DictService dictService;
	
	@Autowired
	private SchoolOnclassService schoolOnclassService;
	
	@Autowired
	private SchoolClassroomService schoolClassroomService;
	
	@Autowired
	private SchoolCurrentWeekService schoolCurrentWeekService;
	
	@Autowired
	private SchoolLessonTimeService schoolLessonTimeService;
	
	@Autowired
	private SchoolCtableService schoolCtableService;
	
	@Autowired
	private SchoolCTypeService schoolCTypeService;
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private SchoolLessonStudentService lessonStudentService;
	
	@ModelAttribute
	public SchoolOnclass get(@RequestParam(required=false) String id) {
		SchoolOnclass entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = schoolOnclassService.get(id);
		}
		if (entity == null){
			entity = new SchoolOnclass();
		}
		return entity;
	}
	
	@RequiresPermissions("school:onclass:schoolOnclass:view")
	@RequestMapping(value = {"list", ""})
	public String list(SchoolOnclass schoolOnclass, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SchoolOnclass> page = schoolOnclassService.findPage(new Page<SchoolOnclass>(request, response), schoolOnclass); 
		List<SchoolOnclass> list = page.getList();
		User user = UserUtils.getUser();
		if (user.getRoleNames().contains("老师"))
			for (int i = list.size() - 1; i >= 0; i--) {
				SchoolCtable schoolCtable = schoolCtableService.get(list.get(i).getLessonId());
				if (!user.getId().equals(schoolCtable.getTeacherId()))
					list.remove(i);
			}
		list = TranslateUtil.t.translateList(list,new TransLateCondition("studentId","sys_user a","a.name as studentId","id",""));
		list = TranslateUtil.t.translateList(list,new TransLateCondition("lessonId","school_ctable a","a.course_id as lessonId","id",""));
		list = TranslateUtil.t.translateList(list,new TransLateCondition("lessonId","school_c_type a","a.name as lessonId","id",""));
		model.addAttribute("page", page);
		return "modules/school/onclass/schoolOnclassList";
	}

	@RequiresPermissions("school:onclass:schoolOnclass:view")
	@RequestMapping(value = "form")
	public String form(SchoolOnclass schoolOnclass, Model model) {

		if (schoolOnclass.getStudentId() != null) {
			User user = UserUtils.get(schoolOnclass.getStudentId());
			SchoolCtable schoolCtable = schoolCtableService.get(schoolOnclass.getLessonId());
			SchoolCType schoolCType = schoolCTypeService.get(schoolCtable.getCourseId());
			model.addAttribute("student", user.getName());
			model.addAttribute("lesson", schoolCType.getName() + ",星期" + schoolCtable.getWeekday() + ",第"
					+ schoolCtable.getCourseOrder() + "节");
		}
//		ArrayList<String> onclassList=Lists.newArrayList();
//		onclassList.add("出勤");
//		onclassList.add("迟到");
//		onclassList.add("缺课");
//		onclassList.add("早退");
//		onclassList.add("睡觉");
//		onclassList.add("玩手机");
//		onclassList.add("无着校服");
//		onclassList.add("聊天");
//		onclassList.add("吃东西");
//		onclassList.add("看课外书");
//
//		model.addAttribute("onclassList",onclassList);
		model.addAttribute("schoolOnclass", schoolOnclass);
		return "modules/school/onclass/schoolOnclassForm";
	}

	@RequiresPermissions("school:onclass:schoolOnclass:edit")
	@RequestMapping(value = "save")
	public String save(SchoolOnclass schoolOnclass, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, schoolOnclass)){
			return form(schoolOnclass, model);
		}
		
		if(schoolOnclass.getLessonId()==null){
			addMessage(redirectAttributes, "保存失败，课程不能为空");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		
		if(schoolOnclass.getStudentId()==null){
			addMessage(redirectAttributes, "保存失败，学生不能为空");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		
		SchoolLessonStudent schoolLessonStudent=new SchoolLessonStudent();
		schoolLessonStudent.setLessonid(schoolOnclass.getLessonId());
		schoolLessonStudent.setStudentid(schoolOnclass.getStudentId());
		if(lessonStudentService.findList(schoolLessonStudent).size()==0){
			addMessage(redirectAttributes, "保存失败，该学生没有选该课程");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		
		SchoolCtable schoolCtable=schoolCtableService.get(schoolOnclass.getLessonId());
		String[] week=schoolCtable.getWeekNum().split(",");
		if(Integer.parseInt(week[0])>Integer.parseInt(schoolOnclass.getWeek())||Integer.parseInt(week[1])<Integer.parseInt(schoolOnclass.getWeek())){
			addMessage(redirectAttributes, "保存失败，该课程第"+schoolOnclass.getWeek()+"周没课");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		String result="";
		if(schoolOnclass.getResult()!=null){
			String[] temp=schoolOnclass.getResult().split(",");
			Dict dict = new Dict();
			dict.setType("studentOnclass");
			List<Dict> listDict=dictService.findList(dict);
			for(int i=0;i<temp.length;i++){
				for(Dict dict2:listDict){
					if(dict2.getValue().equals(temp[i]))
						result=result+dict2.getLabel()+",";
				}
			}
		}
		schoolOnclass.setResult(result);
		schoolOnclassService.save(schoolOnclass);
		addMessage(redirectAttributes, "保存考勤成功");
		return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
	}
	
	@RequiresPermissions("school:onclass:schoolOnclass:edit")
	@RequestMapping(value = "delete")
	public String delete(SchoolOnclass schoolOnclass, RedirectAttributes redirectAttributes) {
		schoolOnclassService.delete(schoolOnclass);
		addMessage(redirectAttributes, "删除考勤成功");
		return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
	}
	
	
	/**
	 * 获取老师所有课程的考勤
	 * @param 
	 * @return
	 */
	@RequiresPermissions("school:onclass:schoolOnclass:view")
	@RequestMapping(value = "getLessonOnclass",method=RequestMethod.GET)
	@ResponseBody
	public String getLessonOnclass(String weekNum,Model model) {
		User user = UserUtils.getUser();
		System.out.println(user.getId());
		SchoolCtable schoolCtable=new SchoolCtable();
		schoolCtable.setTeacherId(user.getId());
		List<SchoolCtable> listCtable=schoolCtableService.findList(schoolCtable);
		for(int i=0;i<listCtable.size();i++){
			String[] weeks=listCtable.get(i).getWeekNum().split(",");
			if(Integer.parseInt(weekNum)<Integer.parseInt(weeks[0])||Integer.parseInt(weekNum)>Integer.parseInt(weeks[1]))
			{
				listCtable.remove(listCtable.get(i));
				i--;
			}
		}
		listCtable = TranslateUtil.t.translateList(listCtable,new TransLateCondition("courseId","school_c_type a","a.name as courseId","id",""));

		listCtable = TranslateUtil.t.translateList(listCtable,new TransLateCondition("classroomId","school_classroom a","a.name as classroomId","id",""));
		listCtable = TranslateUtil.t.translateList(listCtable,new TransLateCondition("teacherId","sys_user a","a.name as teacherId","id",""));
		

		SchoolOnclass schoolOnclass=new SchoolOnclass();
		schoolOnclass.setWeek(weekNum);
		List<SchoolOnclass> listOnclass=schoolOnclassService.findList(schoolOnclass);
	
		String week="1";
		try{
			week=schoolCurrentWeekService.findList(new SchoolCurrentWeek()).get(0).getWeek();
		}
		catch(Exception e){
			week="1";
		}

		ArrayList<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		for(int i=0;i<listCtable.size();i++){
			int count=0;
			Map<String, Object> map2=Maps.newHashMap();
			for(int j=0;j<listOnclass.size();j++){
				if(listCtable.get(i).getId().equals(listOnclass.get(j).getLessonId())){
					count++;
				}
			}
			if(count!=0){ 
				map2.put("id",listCtable.get(i).getId());
				map2.put("lesson", listCtable.get(i).getCourseId());
				map2.put("week", listCtable.get(i).getWeekday());
				SchoolLessonTime schoolLessonTime=schoolLessonTimeService.get(listCtable.get(i).getStart());
				map2.put("start",schoolLessonTime.getStart());
				map2.put("end", schoolLessonTime.getEnd());
				map2.put("count", count);
				String timeOfDay=TimeUtils.WeekOfDate(Integer.parseInt(weekNum)-Integer.parseInt(week), Integer.parseInt(listCtable.get(i).getWeekday()));
				map2.put("time", timeOfDay);
				mapList.add(map2);
			}
		}
		
		Map<String, Object> map1 = Maps.newHashMap();
		map1.put("code", 1);
		map1.put("message", "success");
		map1.put("data", mapList);
		return 	JsonMapper.getInstance().toJson(map1);
	}

	
	/**
	 * 获取某一节课的考勤情况
	 * @param 
	 * @return
	 */
	@RequiresPermissions("school:onclass:schoolOnclass:view")
	@RequestMapping(value = "getOnclassInfo",method=RequestMethod.GET)
	@ResponseBody
	public String getOnclassInfo(String weekNum,String lessonId,Model model) {
		

		SchoolOnclass schoolOnclass=new SchoolOnclass();
		schoolOnclass.setWeek(weekNum);
		schoolOnclass.setLessonId(lessonId);
		List<SchoolOnclass> listOnclass=schoolOnclassService.findList(schoolOnclass);
	
		listOnclass = TranslateUtil.t.translateList(listOnclass,new TransLateCondition("courseId","school_c_type a","a.name as courseId","id",""));
		
		ArrayList<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		for(SchoolOnclass schoolOnclass2:listOnclass){
			Map<String, Object> map2=Maps.newHashMap();

			User user=systemService.getUser(schoolOnclass2.getStudentId());
			System.out.println(user.getId());
			Office office=officeService.get(user.getOffice());
			map2.put("name", user.getName());
			map2.put("studentNo", user.getNo());
			map2.put("className",office!=null?office.getName():"");
			map2.put("reason", schoolOnclass2.getResult());
			mapList.add(map2);
		}
		
		Map<String, Object> map1 = Maps.newHashMap();
		map1.put("code", 1);
		map1.put("message", "success");
		map1.put("data", mapList);
		return 	JsonMapper.getInstance().toJson(map1);
	}
	

	/**
	 * 学生考勤首页接口
	 * @param start 开始周数
	 * @param end 结束周数
	 * @return
	 */
	@RequestMapping(value = "getStudentOnclass",method=RequestMethod.POST)
	@ResponseBody
	public String getStudentOnclass(@RequestParam(required=true)int start,@RequestParam(required=true)int end) {
		System.out.println(start+""+end);
		User user=UserUtils.getUser();
		Map<String, Object> map2 = Maps.newHashMap();
		
		SchoolOnclass schoolOnclass=new SchoolOnclass();
		schoolOnclass.setStudentId(user.getId());;
		List<SchoolOnclass> list=schoolOnclassService.findList(schoolOnclass);
		int normalCount=0;
		int lateCount=0;
		int budengCount=0;
		int leaveCount=0;
		for(int i=list.size()-1;i>=0;i--){
			if(Integer.parseInt(list.get(i).getWeek())<start||Integer.parseInt(list.get(i).getWeek())>end){
				list.remove(i);
				continue;
			}
			if(list.get(i).getResult().contains("出勤")) normalCount++;
			if(list.get(i).getResult().contains("迟到")) lateCount++;
			if(list.get(i).getResult().contains("缺课")) budengCount++;
			if(list.get(i).getResult().contains("请假")) leaveCount++;
		}
		map2.put("normal", normalCount);
		map2.put("late",lateCount);
		map2.put("absent",budengCount);
		map2.put("leave",leaveCount);
		
		Map<String, Object> map1 = Maps.newHashMap();
		map1.put("code", 1);
		map1.put("message", "success");
		map1.put("data", map2);
		return 	JsonMapper.getInstance().toJson(map1);
	}

	/**
	 * 学生考勤详情接口
	 * @param start 开始周数
	 * @param end 结束周数
	 * @param state 考勤状态
	 * @return
	 */
	@RequestMapping(value = "getStudentOnclassInfo",method=RequestMethod.POST)
	@ResponseBody
	public String getStudentOnclassInfo(@RequestParam(required=true)int start,@RequestParam(required=true)int end,@RequestParam(required=true)String state) {
		User user=UserUtils.getUser();
		if(state.equals("0")) state="出勤";
		if(state.equals("1")) state="请假";
		if(state.equals("2")) state="迟到";
		if(state.equals("3")) state="缺课";
		SchoolOnclass schoolOnclass=new SchoolOnclass();
		schoolOnclass.setStudentId(user.getId());;
		List<SchoolOnclass> list=schoolOnclassService.findList(schoolOnclass);
		ArrayList<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		String week="1";
		try{
			week=schoolCurrentWeekService.findList(new SchoolCurrentWeek()).get(0).getWeek();
		}
		catch(Exception e){
			week="1";
		}
		for(int i=list.size()-1;i>=0;i--){
			if(Integer.parseInt(list.get(i).getWeek())<start||Integer.parseInt(list.get(i).getWeek())>end){
				list.remove(i);
				continue;
			}
			Map<String, Object> map2=Maps.newHashMap();
		    
			if(!list.get(i).getResult().contains(state)){
				list.remove(i);
				continue;
			}
			SchoolCtable schoolCtable=schoolCtableService.get(list.get(i).getLessonId());
			SchoolCType schoolCType=schoolCTypeService.get(schoolCtable.getCourseId());
			SchoolClassroom schoolClassroom=schoolClassroomService.get(schoolCtable.getClassroomId());
			map2.put("classroom",schoolClassroom.getName());
			map2.put("name",UserUtils.get(schoolCtable.getTeacherId()).getName());
			map2.put("className",schoolCType.getName());
			String timeOfDay=TimeUtils.WeekOfDate(Integer.parseInt(list.get(i).getWeek())-Integer.parseInt(week), Integer.parseInt(schoolCtable.getWeekday()));
			map2.put("time", timeOfDay);
			map2.put("courseOrder",schoolCtable.getCourseOrder());
			mapList.add(map2);
		}
		
		Map<String, Object> map1 = Maps.newHashMap();
		map1.put("code", 1);
		map1.put("message", "success");
		map1.put("data", mapList);
		return 	JsonMapper.getInstance().toJson(map1);  
	}
}