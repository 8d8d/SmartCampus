/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.web.lessonstudent;

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
import com.thinkgem.jeesite.common.utils.tran.TransLateCondition;
import com.thinkgem.jeesite.common.utils.tran.TranslateUtil;
import com.thinkgem.jeesite.modules.school.entity.classtype.SchoolCType;
import com.thinkgem.jeesite.modules.school.entity.ctable.SchoolCtable;
import com.thinkgem.jeesite.modules.school.entity.lessonstudent.RollCallEntity;
import com.thinkgem.jeesite.modules.school.entity.lessonstudent.RollCallListForm;
import com.thinkgem.jeesite.modules.school.entity.lessonstudent.RollCallSet;
import com.thinkgem.jeesite.modules.school.entity.lessonstudent.SchoolLessonStudent;
import com.thinkgem.jeesite.modules.school.entity.onclass.SchoolOnclass;
import com.thinkgem.jeesite.modules.school.service.classtype.SchoolCTypeService;
import com.thinkgem.jeesite.modules.school.service.ctable.SchoolCtableService;
import com.thinkgem.jeesite.modules.school.service.lessonstudent.SchoolLessonStudentService;
import com.thinkgem.jeesite.modules.school.service.onclass.SchoolOnclassService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 学生选课Controller
 * @author 何伟杰
 * @version 2016-06-07
 */
@Controller
@RequestMapping(value = "${adminPath}/school/lessonstudent/schoolLessonStudent")
public class SchoolLessonStudentController extends BaseController {

	@Autowired
	private SchoolLessonStudentService schoolLessonStudentService;
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private SchoolCtableService schoolCtableService;
	
	@Autowired
	private SchoolCTypeService schoolCTypeService;
	
	@Autowired
	private SchoolOnclassService schoolOnclassService;
	
	@ModelAttribute
	public SchoolLessonStudent get(@RequestParam(required=false) String id) {
		SchoolLessonStudent entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = schoolLessonStudentService.get(id);
		}
		if (entity == null){
			entity = new SchoolLessonStudent();
		}
		return entity;
	}
	
	@RequiresPermissions("school:lessonstudent:schoolLessonStudent:view")
	@RequestMapping(value = {"list", ""})
	public String list(SchoolLessonStudent schoolLessonStudent, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SchoolLessonStudent> page = schoolLessonStudentService.findPage(new Page<SchoolLessonStudent>(request, response), schoolLessonStudent); 
		java.util.List<SchoolLessonStudent> list=page.getList();
		User user = UserUtils.getUser();
		if (user.getRoleNames().contains("老师"))
			for (int i = list.size() - 1; i >= 0; i--) {
				SchoolCtable schoolCtable = schoolCtableService.get(list.get(i).getLessonid());
				if (!user.getId().equals(schoolCtable.getTeacherId()))
					list.remove(i);
			}
		list = TranslateUtil.t.translateList(list,new TransLateCondition("lessonid","school_ctable a","a.course_id as lessonid","id",""));
		list = TranslateUtil.t.translateList(list,new TransLateCondition("lessonid","school_c_type a","a.name as lessonid","id",""));
		list = TranslateUtil.t.translateList(list,new TransLateCondition("studentid","sys_user a","a.name as studentid","id",""));
		model.addAttribute("page", page);
		return "modules/school/lessonstudent/schoolLessonStudentList";
	}

	@RequiresPermissions("school:lessonstudent:schoolLessonStudent:view")
	@RequestMapping(value = "form")
	public String form(SchoolLessonStudent schoolLessonStudent, Model model) {
		if (schoolLessonStudent.getStudentid() != null) {
			User user = UserUtils.get(schoolLessonStudent.getStudentid() );
			SchoolCtable schoolCtable = schoolCtableService.get(schoolLessonStudent.getLessonid());
			SchoolCType schoolCType = schoolCTypeService.get(schoolCtable.getCourseId());
			model.addAttribute("student", user.getName());
			model.addAttribute("lesson", schoolCType.getName() + ",星期" + schoolCtable.getWeekday() + ",第"
					+ schoolCtable.getCourseOrder() + "节");
		}
		model.addAttribute("schoolLessonStudent", schoolLessonStudent);
		return "modules/school/lessonstudent/schoolLessonStudentForm";
	}

	@RequiresPermissions("school:lessonstudent:schoolLessonStudent:edit")
	@RequestMapping(value = "save")
	public String save(SchoolLessonStudent schoolLessonStudent, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, schoolLessonStudent)){
			return form(schoolLessonStudent, model);
		}
		if(schoolLessonStudent.getLessonid()==null){
			addMessage(redirectAttributes, "保存失败，课程不能为空");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		
		if(schoolLessonStudent.getStudentid()==null){
			addMessage(redirectAttributes, "保存失败，学生不能为空");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		
		schoolLessonStudentService.save(schoolLessonStudent);
		addMessage(redirectAttributes, "保存学生选课成功");
		return "redirect:"+Global.getAdminPath()+"/school/lessonstudent/schoolLessonStudent/?repage";
	}
	
	@RequiresPermissions("school:lessonstudent:schoolLessonStudent:edit")
	@RequestMapping(value = "delete")
	public String delete(SchoolLessonStudent schoolLessonStudent, RedirectAttributes redirectAttributes) {
		schoolLessonStudentService.delete(schoolLessonStudent);
		addMessage(redirectAttributes, "删除学生选课成功");
		return "redirect:"+Global.getAdminPath()+"/school/lessonstudent/schoolLessonStudent/?repage";
	}

	
	/**
	 * 获取某一节课程的所有学生接口
	 * @param 
	 * @return
	 */
	@RequiresPermissions("school:lessonstudent:schoolLessonStudent:view")
	@RequestMapping(value = "getLessonStudent",method=RequestMethod.GET)
	@ResponseBody
	public String getLessonStudent(String weekNum,String lessonId,Model model) {
		SchoolLessonStudent schoolLessonStudent=new SchoolLessonStudent();
		schoolLessonStudent.setLessonid(lessonId);
		java.util.List<SchoolLessonStudent> list=schoolLessonStudentService.findList(schoolLessonStudent);
		ArrayList<Map<String, Object>> mapList=new ArrayList<Map<String,Object>>();
		for(int i=0;i<list.size();i++){
			User user=systemService.getUser(list.get(i).getStudentid());
			SchoolOnclass schoolOnclass=new SchoolOnclass();
			schoolOnclass.setStudentId(user.getId());
			schoolOnclass.setLessonId(lessonId);
			schoolOnclass.setWeek(weekNum);
			List<SchoolOnclass> OnclassList=schoolOnclassService.findList(schoolOnclass);

			Map<String, Object> map2 = Maps.newHashMap();
			map2.put("id", user.getId());
			map2.put("name", user.getName());
			map2.put("studentNo", user.getNo());
			map2.put("photo", user.getPhoto());
			if(OnclassList.size()==0){
				map2.put("rollCall",0);
			}
			else
			{
				map2.put("rollCall",RollCallSet.get(OnclassList.get(0).getResult()));
			}
			mapList.add(map2);
			}
	
		Map<String, Object> map1 = Maps.newHashMap();
		map1.put("code", 1);
		map1.put("message", "success");
		map1.put("data", mapList);
		return 	JsonMapper.getInstance().toJson(map1);
	}
	
	/**
	 * 学生点名接口
	 * @param 
	 * @return
	 */
	@RequiresPermissions("school:lessonstudent:schoolLessonStudent:view")
	@RequestMapping(value = "RollCall",method=RequestMethod.POST)
	@ResponseBody
	public String RollCall(String week,String lessonid,String rollCallListForm,Model model) {

		System.out.println(rollCallListForm);
		rollCallListForm=rollCallListForm.replace("&quot;", "\"");
		RollCallListForm rollCallListForm2=(RollCallListForm) JsonMapper.fromJsonString(rollCallListForm, RollCallListForm.class);
		List<RollCallEntity> list=rollCallListForm2.getRollCallEntities();
		for(RollCallEntity rollCallEntity:list){
	    	RollCallSet rollCallSet=new RollCallSet();
	    	SchoolOnclass schoolOnclass=new SchoolOnclass();
	    	schoolOnclass.setWeek(week);
	    	schoolOnclass.setLessonId(lessonid);
	    	schoolOnclass.setStudentId(rollCallEntity.getId());
	    	schoolOnclass.setResult(rollCallSet.Set(rollCallEntity.getRollCall()));
	    	schoolOnclassService.save(schoolOnclass);
		}
		Map<String, Object> map1 = Maps.newHashMap();
		map1.put("code", 1);
		map1.put("message", "success");
		map1.put("data",Maps.newHashMap());
		return 	JsonMapper.getInstance().toJson(map1);
	}
}