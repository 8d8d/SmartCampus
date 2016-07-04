/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.web.leave;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
import com.thinkgem.jeesite.modules.school.entity.leaderclass.SchoolLeaderClass;
import com.thinkgem.jeesite.modules.school.entity.leave.SchoolLeave;
import com.thinkgem.jeesite.modules.school.entity.lessonstudent.SchoolLessonStudent;
import com.thinkgem.jeesite.modules.school.entity.onclass.SchoolOnclass;
import com.thinkgem.jeesite.modules.school.service.ctable.SchoolCtableService;
import com.thinkgem.jeesite.modules.school.service.currentweek.SchoolCurrentWeekService;
import com.thinkgem.jeesite.modules.school.service.leaderclass.SchoolLeaderClassService;
import com.thinkgem.jeesite.modules.school.service.leave.SchoolLeaveService;
import com.thinkgem.jeesite.modules.school.service.lessonstudent.SchoolLessonStudentService;
import com.thinkgem.jeesite.modules.school.service.onclass.SchoolOnclassService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 请假审批Controller
 * @author 王超然
 * @version 2016-05-12
 */
@Controller
@RequestMapping(value = "${adminPath}/school/leave/schoolLeave")
public class SchoolLeaveController extends BaseController {

	@Autowired
	private SchoolLeaveService schoolLeaveService;
	
	@Autowired
	private SchoolCtableService schoolCtableService;
	
	@Autowired
	private SchoolOnclassService schoolOnclassService;
	
	@Autowired
	private SchoolLessonStudentService schoolLessonStudentService;
	
	@Autowired
	private SchoolCurrentWeekService schoolCurrentWeekService;
	
	@Autowired
	private SchoolLeaderClassService schoolLeaderClassService;
	
	@ModelAttribute
	public SchoolLeave get(@RequestParam(required=false) String id) {
		SchoolLeave entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = schoolLeaveService.get(id);
		}
		if (entity == null){
			entity = new SchoolLeave();
		}
		return entity;
	}
	
	@RequiresPermissions("school:leave:schoolLeave:view")
	@RequestMapping(value = {"list", ""})
	public String list(SchoolLeave schoolLeave, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SchoolLeave> page = schoolLeaveService.findPage(new Page<SchoolLeave>(request, response), schoolLeave); 
		List<SchoolLeave> list=page.getList();
		User user = UserUtils.getUser();
		if (user.getRoleNames().contains("老师"))
			for (int i = list.size() - 1; i >= 0; i--) {
				if (!user.getId().equals(list.get(i).getAcceptId()))
					list.remove(i);
			}
		list = TranslateUtil.t.translateList(list,new TransLateCondition("applyId","sys_user a","a.name as applyId","id",""));
		list = TranslateUtil.t.translateList(list,new TransLateCondition("acceptId","sys_user a","a.name as acceptId","id",""));
		model.addAttribute("page", page);
		return "modules/school/leave/schoolLeaveList";
	}

	@RequiresPermissions("school:leave:schoolLeave:view")
	@RequestMapping(value = "form")
	public String form(SchoolLeave schoolLeave, Model model) {
		if (schoolLeave.getId() != null) {
			User user1 = UserUtils.get(schoolLeave.getAcceptId());
			User user2 = UserUtils.get(schoolLeave.getApplyId());
			model.addAttribute("student",user2.getName());
			model.addAttribute("teacher",user1.getName() );
		}
		model.addAttribute("schoolLeave", schoolLeave);
		return "modules/school/leave/schoolLeaveForm";
	}

	@RequiresPermissions("school:leave:schoolLeave:edit")
	@RequestMapping(value = "save")
	public String save(SchoolLeave schoolLeave, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, schoolLeave)){
			return form(schoolLeave, model);
		}
		if(schoolLeave.getApplyId()==null){
			addMessage(redirectAttributes, "保存失败，申请人不能为空");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		
		if(schoolLeave.getAcceptId()==null){
			addMessage(redirectAttributes, "保存失败，审批人不能为空");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		String week="1";
		try{
			week=schoolCurrentWeekService.findList(new SchoolCurrentWeek()).get(0).getWeek();
		}
		catch(Exception e){
			week="1";
		}
		
		int startWeek=TimeUtils.WeekOfNumber(Integer.parseInt(week), schoolLeave.getStart());
		int endtWeek=TimeUtils.WeekOfNumber(Integer.parseInt(week), schoolLeave.getEnd());
		int startSection=schoolLeave.getStart().getHours()>=12?5:1;
		int endSection=schoolLeave.getEnd().getHours()>=12?5:1;
		int startWeekDay=TimeUtils.dayOfWeek(schoolLeave.getStart());
		int endWeekDay=TimeUtils.dayOfWeek(schoolLeave.getEnd());
		SchoolLessonStudent schoolLessonStudent=new SchoolLessonStudent();
		schoolLessonStudent.setStudentid(schoolLeave.getApplyId());
		List<SchoolLessonStudent> lessonStudents=schoolLessonStudentService.findList(schoolLessonStudent);
		for(SchoolLessonStudent schoolLessonStudent2:lessonStudents){
			SchoolCtable schoolCtable=schoolCtableService.get(schoolLessonStudent2.getLessonid());
			for(int i=0;i<=endtWeek-startWeek;i++){
				SchoolOnclass schoolOnclass=new SchoolOnclass();
				if(startWeekDay<=endWeekDay&&startWeekDay<=Integer.parseInt(schoolCtable.getWeekday())){
					if(startSection<Integer.parseInt(schoolCtable.getCourseOrder())){
						schoolOnclass.setLessonId(schoolLessonStudent2.getLessonid());
						schoolOnclass.setWeek(startWeek+i+"");
						schoolOnclass.setStudentId(schoolLessonStudent2.getStudentid());
						schoolOnclass.setResult("请假");
					}
				}
				if(startWeekDay>endWeekDay&&endWeekDay>Integer.parseInt(schoolCtable.getWeekday())){
					if(startSection<Integer.parseInt(schoolCtable.getCourseOrder())){
						schoolOnclass.setLessonId(schoolLessonStudent2.getLessonid());
						schoolOnclass.setWeek(startWeek+i+""+1);
						schoolOnclass.setStudentId(schoolLessonStudent2.getStudentid());
						schoolOnclass.setResult("请假");
					}
				}
				schoolOnclassService.save(schoolOnclass);
			}
			
		}
		schoolLeaveService.save(schoolLeave);
		addMessage(redirectAttributes, "保存请假审批成功");
		return "redirect:"+Global.getAdminPath()+"/school/leave/schoolLeave/?repage";
	}
	
	@RequiresPermissions("school:leave:schoolLeave:edit")
	@RequestMapping(value = "delete")
	public String delete(SchoolLeave schoolLeave, RedirectAttributes redirectAttributes) {
		schoolLeaveService.delete(schoolLeave);
		addMessage(redirectAttributes, "删除请假审批成功");
		return "redirect:"+Global.getAdminPath()+"/school/leave/schoolLeave/?repage";
	}

	/**
	 * 请求未审批请假接口
	 * @param 
	 * @return
	 */
	@RequiresPermissions("school:leave:schoolLeave:view")
	@RequestMapping(value = "getLeaveFalse",method=RequestMethod.GET)
	@ResponseBody
	public String getLeaveFalse() {
		User user = UserUtils.getUser();
		SchoolLeave schoolLeave=new SchoolLeave();
		schoolLeave.setAcceptId(user.getId());
		ArrayList<Map<String, Object>> mapList =new  ArrayList<Map<String,Object>>();
		List<SchoolLeave> list=schoolLeaveService.findList(schoolLeave);
		for(SchoolLeave schoolLeave2:list){
			if(schoolLeave2.getDealId()!=null&&!schoolLeave2.getDealId().equals("")){
				continue;
			}
			User student=UserUtils.get(schoolLeave2.getApplyId());
			Map<String, Object> map2 = Maps.newHashMap();
			map2.put("id", schoolLeave2.getId());
			map2.put("name",student.getName());
			map2.put("photo", student.getPhoto());
			map2.put("start",schoolLeave2.getStart());
			map2.put("end", schoolLeave2.getEnd());
			map2.put("date", schoolLeave2.getUpdateDate());
			map2.put("reason", schoolLeave2.getReason());
			mapList.add(map2);
		}

		Map<String, Object> map1 = Maps.newHashMap();
		map1.put("code", 1);
		map1.put("message", "success");
		map1.put("data", mapList);
		return 	JsonMapper.getInstance().toJson(map1);
	}
	
	/**
	 * 请求已审批请假接口
	 * @param 
	 * @return
	 */
	@RequiresPermissions("school:leave:schoolLeave:view")
	@RequestMapping(value = "getLeaveTrue",method=RequestMethod.GET)
	@ResponseBody
	public String getLeaveTrue() {
		User user = UserUtils.getUser();
		SchoolLeave schoolLeave=new SchoolLeave();
		schoolLeave.setAcceptId(user.getId());
		ArrayList<Map<String, Object>> mapList =new  ArrayList<Map<String,Object>>();
		List<SchoolLeave> list=schoolLeaveService.findList(schoolLeave);
		for(SchoolLeave schoolLeave2:list){
			if(schoolLeave2.getDealId()==null||schoolLeave2.getDealId().equals("")){
				continue;
			}
			User student=UserUtils.get(schoolLeave2.getApplyId());
			Map<String, Object> map2 = Maps.newHashMap();
			map2.put("id", schoolLeave2.getId());
			map2.put("name",student.getName());
			map2.put("photo", student.getPhoto());
			map2.put("start",schoolLeave2.getStart());
			map2.put("end", schoolLeave2.getEnd());
			map2.put("date", schoolLeave2.getUpdateDate());
			map2.put("dealId", schoolLeave2.getDealId());
			map2.put("dealName", schoolLeave2.getDealName());
			map2.put("reason", schoolLeave2.getReason());
			mapList.add(map2);
		}

		Map<String, Object> map1 = Maps.newHashMap();
		map1.put("code", 1);
		map1.put("message", "success");
		map1.put("data", mapList);
		return 	JsonMapper.getInstance().toJson(map1);
	}
	
	/**
	 * 审批请假接口
	 * @param 
	 * @return
	 */
	@RequiresPermissions("school:leave:schoolLeave:edit")
	@RequestMapping(value = "LeaveApproval",method=RequestMethod.POST)
	@ResponseBody
	public String LeaveApproval(@RequestParam(required=true) String id,@RequestParam(required=true) String deal) {
		SchoolLeave schoolLeave=schoolLeaveService.get(id);
		schoolLeave.setDealId(deal);
		schoolLeave.setDealName(deal.equals("0")?"审批不通过":"审批通过");
		schoolLeaveService.save(schoolLeave);
		
		Map<String, Object> map1 = Maps.newHashMap();
		map1.put("code", 1);
		map1.put("message", "success");
		map1.put("data", Maps.newHashMap());
		return 	JsonMapper.getInstance().toJson(map1);
	}
	
	
	/**
	 * 请求学生假条
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "getLeave",method=RequestMethod.GET)
	@ResponseBody
	public String getLeave() {
		User user = UserUtils.getUser();
		SchoolLeave schoolLeave=new SchoolLeave();
		schoolLeave.setApplyId(user.getId());
		ArrayList<Map<String, Object>> mapList =new  ArrayList<Map<String,Object>>();
		List<SchoolLeave> list=schoolLeaveService.findList(schoolLeave);
		for(SchoolLeave schoolLeave2:list){
			Map<String, Object> map2 = Maps.newHashMap();
			map2.put("id", schoolLeave2.getId());
			if(schoolLeave2.getDealId()==null||schoolLeave2.getDealId().equals("")){
				map2.put("state", 0);//未审批
			}
			else{
				if(schoolLeave2.getDealId().equals("0")){
					map2.put("state", 1);//不通过
				}
				else{
					map2.put("state", 2);//通过
				}
			}
			map2.put("start",TimeUtils.timestamp2str(schoolLeave2.getStart(),"yyyy-MM-dd a"));
			map2.put("end", TimeUtils.timestamp2str(schoolLeave2.getEnd(),"yyyy-MM-dd a"));
			map2.put("reason", schoolLeave2.getReason());
			mapList.add(map2);
		}

		Map<String, Object> map1 = Maps.newHashMap();
		map1.put("code", 1);
		map1.put("message", "success");
		map1.put("data", mapList);
		return 	JsonMapper.getInstance().toJson(map1);
	}
	
	/**
	 * 学生请假
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "postLeave",method=RequestMethod.POST)
	@ResponseBody
	public String postLeave(@RequestParam(required=true) String content,@RequestParam(required=true) String start,@RequestParam(required=true) String end) {
		User user = UserUtils.getUser();
		SchoolLeaderClass schoolLeaderClass=new SchoolLeaderClass();
		schoolLeaderClass.setClassId(user.getOffice().getId());
		List<SchoolLeaderClass> list=schoolLeaderClassService.findList(schoolLeaderClass);
		Map<String, Object> map1 = Maps.newHashMap();
		if(list.size()==0){
			map1.put("code", 0);
			map1.put("message", "该班级无辅导员任教");
			map1.put("data", Maps.newHashMap());
			return 	JsonMapper.getInstance().toJson(map1);
		}

		SchoolLeave schoolLeave=new SchoolLeave();
		schoolLeave.setAcceptId(list.get(0).getLeaderId());
		schoolLeave.setApplyId(user.getId());
		schoolLeave.setReason(content);
		schoolLeave.setStart(TimeUtils.stringToDate(start, "yyyy-MM-dd a"));
		schoolLeave.setEnd(TimeUtils.stringToDate(end, "yyyy-MM-dd a"));
		
		schoolLeaveService.save(schoolLeave);
		map1.put("code", 1);
		map1.put("message", "success");
		map1.put("data",Maps.newHashMap());
		return 	JsonMapper.getInstance().toJson(map1);
	}
}