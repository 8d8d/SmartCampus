/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.web.onclassteacher;

import java.util.ArrayList;
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
import com.thinkgem.jeesite.modules.school.entity.calendar.SchoolCalendar;
import com.thinkgem.jeesite.modules.school.entity.classtype.SchoolCType;
import com.thinkgem.jeesite.modules.school.entity.ctable.SchoolCtable;
import com.thinkgem.jeesite.modules.school.entity.currentweek.SchoolCurrentWeek;
import com.thinkgem.jeesite.modules.school.entity.lessonstudent.SchoolLessonStudent;
import com.thinkgem.jeesite.modules.school.entity.onclass.SchoolOnclass;
import com.thinkgem.jeesite.modules.school.entity.onclassteacher.SchoolOnclassTeaccher;
import com.thinkgem.jeesite.modules.school.service.classtype.SchoolCTypeService;
import com.thinkgem.jeesite.modules.school.service.ctable.SchoolCtableService;
import com.thinkgem.jeesite.modules.school.service.currentweek.SchoolCurrentWeekService;
import com.thinkgem.jeesite.modules.school.service.onclassteacher.SchoolOnclassTeaccherService;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 教师考勤Controller
 * @author 何伟杰
 * @version 2016-06-20
 */
@Controller
@RequestMapping(value = "${adminPath}/school/onclassteacher/schoolOnclassTeaccher")
public class SchoolOnclassTeaccherController extends BaseController {

	@Autowired
	private SchoolOnclassTeaccherService schoolOnclassTeaccherService;
	
	@Autowired
	private SchoolCurrentWeekService schoolCurrentWeekService;
	
	@Autowired
	private SchoolCtableService schoolCtableService;
	
	@Autowired
	private SchoolCTypeService schoolCTypeService;
	
	@ModelAttribute
	public SchoolOnclassTeaccher get(@RequestParam(required=false) String id) {
		SchoolOnclassTeaccher entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = schoolOnclassTeaccherService.get(id);
		}
		if (entity == null){
			entity = new SchoolOnclassTeaccher();
		}
		return entity;
	}
	
	@RequiresPermissions("school:onclassteacher:schoolOnclassTeaccher:view")
	@RequestMapping(value = {"list", ""})
	public String list(SchoolOnclassTeaccher schoolOnclassTeaccher, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SchoolOnclassTeaccher> page = schoolOnclassTeaccherService.findPage(new Page<SchoolOnclassTeaccher>(request, response), schoolOnclassTeaccher); 
		java.util.List<SchoolOnclassTeaccher> list=page.getList();
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
		return "modules/school/onclassteacher/schoolOnclassTeaccherList";
	}

	@RequiresPermissions("school:onclassteacher:schoolOnclassTeaccher:view")
	@RequestMapping(value = "form")
	public String form(SchoolOnclassTeaccher schoolOnclassTeaccher, Model model) {
		if (schoolOnclassTeaccher.getUser()!= null) {
			SchoolCtable schoolCtable = schoolCtableService.get(schoolOnclassTeaccher.getLessonId());
			SchoolCType schoolCType = schoolCTypeService.get(schoolCtable.getCourseId());
			model.addAttribute("lesson", schoolCType.getName() + ",星期" + schoolCtable.getWeekday() + ",第"
					+ schoolCtable.getCourseOrder() + "节");
		}
		model.addAttribute("schoolOnclassTeaccher", schoolOnclassTeaccher);
		return "modules/school/onclassteacher/schoolOnclassTeaccherForm";
	}

	@RequiresPermissions("school:onclassteacher:schoolOnclassTeaccher:edit")
	@RequestMapping(value = "save")
	public String save(SchoolOnclassTeaccher schoolOnclassTeaccher, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, schoolOnclassTeaccher)){
			return form(schoolOnclassTeaccher, model);
		}
		if(schoolOnclassTeaccher.getLessonId()==null){
			addMessage(redirectAttributes, "保存失败，课程不能为空");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		
		if(schoolOnclassTeaccher.getUser()==null){
			addMessage(redirectAttributes, "保存失败，老师不能为空");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		
		if(schoolCtableService.get(schoolOnclassTeaccher.getLessonId())==null||!schoolCtableService.get(schoolOnclassTeaccher.getLessonId()).getTeacherId().equals(schoolOnclassTeaccher.getUser().getId())){
			addMessage(redirectAttributes, "保存失败，该老师没有教该课程");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		
		
		SchoolCtable schoolCtable=schoolCtableService.get(schoolOnclassTeaccher.getLessonId());
		String[] week=schoolCtable.getWeekNum().split(",");
		if(Integer.parseInt(week[0])>Integer.parseInt(schoolOnclassTeaccher.getWeek())||Integer.parseInt(week[1])<Integer.parseInt(schoolOnclassTeaccher.getWeek())){
			addMessage(redirectAttributes, "保存失败，该课程第"+schoolOnclassTeaccher.getWeek()+"周没课");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		schoolOnclassTeaccherService.save(schoolOnclassTeaccher);
		addMessage(redirectAttributes, "保存教师考勤成功");
		return "redirect:"+Global.getAdminPath()+"/school/onclassteacher/schoolOnclassTeaccher/?repage";
	}
	
	@RequiresPermissions("school:onclassteacher:schoolOnclassTeaccher:edit")
	@RequestMapping(value = "delete")
	public String delete(SchoolOnclassTeaccher schoolOnclassTeaccher, RedirectAttributes redirectAttributes) {
		schoolOnclassTeaccherService.delete(schoolOnclassTeaccher);
		addMessage(redirectAttributes, "删除教师考勤成功");
		return "redirect:"+Global.getAdminPath()+"/school/onclassteacher/schoolOnclassTeaccher/?repage";
	}
	
	
	/**
	 * 教师考勤首页接口
	 * @param start 开始周数
	 * @param end 结束周数
	 * @return
	 */
	@RequiresPermissions("school:onclassteacher:schoolOnclassTeaccher:view")
	@RequestMapping(value = "getTeacherOnclass",method=RequestMethod.POST)
	@ResponseBody
	public String getTeacherOnclass(@RequestParam(required=true)int start,@RequestParam(required=true)int end) {
		System.out.println(start+""+end);
		User user=UserUtils.getUser();
		Map<String, Object> map2 = Maps.newHashMap();
		map2.put("name",user.getName());
		if(!StringUtils.isEmpty(user.getSex()))
			map2.put("sex",user.getSex().equals("0")?"男":"女");
		else
			map2.put("sex","");
		map2.put("type",user.getRoleList().get(0).getName());
		map2.put("department",user.getOffice().getName());
		map2.put("photo",user.getPhoto());
		
		SchoolOnclassTeaccher schoolOnclassTeaccher=new SchoolOnclassTeaccher();
		schoolOnclassTeaccher.setUser(user);
		List<SchoolOnclassTeaccher> list=schoolOnclassTeaccherService.findList(schoolOnclassTeaccher);
		int normalCount=0;
		int lateCount=0;
		int budengCount=0;
		int leaveCount=0;
		for(int i=list.size()-1;i>=0;i--){
			if(Integer.parseInt(list.get(i).getWeek())<start||Integer.parseInt(list.get(i).getWeek())>end){
				list.remove(i);
				continue;
			}
			if(list.get(i).getState().equals("0")) normalCount++;
			if(list.get(i).getState().equals("1")) lateCount++;
			if(list.get(i).getState().equals("2")) budengCount++;
			if(list.get(i).getState().equals("3")) leaveCount++;
		}
		map2.put("normal", normalCount);
		map2.put("late",lateCount);
		map2.put("board",budengCount);
		map2.put("leave",leaveCount);
		
		Map<String, Object> map1 = Maps.newHashMap();
		map1.put("code", 1);
		map1.put("message", "success");
		map1.put("data", map2);
		return 	JsonMapper.getInstance().toJson(map1);
	}

	/**
	 * 教师考勤详情接口
	 * @param start 开始周数
	 * @param end 结束周数
	 * @param state 考勤状态
	 * @return
	 */
	@RequiresPermissions("school:onclassteacher:schoolOnclassTeaccher:view")
	@RequestMapping(value = "getTeacherOnclassInfo",method=RequestMethod.POST)
	@ResponseBody
	public String getTeacherOnclassInfo(@RequestParam(required=true)int start,@RequestParam(required=true)int end,@RequestParam(required=true)String state) {
		User user=UserUtils.getUser();
		
		SchoolOnclassTeaccher schoolOnclassTeaccher=new SchoolOnclassTeaccher();
		schoolOnclassTeaccher.setUser(user);
		List<SchoolOnclassTeaccher> list=schoolOnclassTeaccherService.findList(schoolOnclassTeaccher);
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
			if(!state.equals(list.get(i).getState())){
				list.remove(i);
				continue;
			}
			SchoolCtable schoolCtable=schoolCtableService.get(list.get(i).getLessonId());
			SchoolCType schoolCType=schoolCTypeService.get(schoolCtable.getCourseId());
			map2.put("className",schoolCType.getName());
			String timeOfDay=TimeUtils.WeekOfDate(Integer.parseInt(list.get(i).getWeek())-Integer.parseInt(week), Integer.parseInt(schoolCtable.getWeekday()));
			map2.put("time", timeOfDay);
			map2.put("weekday",schoolCtable.getWeekday());
			mapList.add(map2);
		}
		
		Map<String, Object> map1 = Maps.newHashMap();
		map1.put("code", 1);
		map1.put("message", "success");
		map1.put("data", mapList);
		return 	JsonMapper.getInstance().toJson(map1);
	}
}