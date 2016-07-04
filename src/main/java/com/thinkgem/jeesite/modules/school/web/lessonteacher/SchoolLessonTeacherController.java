/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.web.lessonteacher;

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
import com.thinkgem.jeesite.modules.school.entity.lessonteacher.SchoolLessonTeacher;
import com.thinkgem.jeesite.modules.school.entity.onclass.SchoolOnclass;
import com.thinkgem.jeesite.modules.school.service.classtype.SchoolCTypeService;
import com.thinkgem.jeesite.modules.school.service.lessonteacher.SchoolLessonTeacherService;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 教师任课管理Controller
 * @author 王超然
 * @version 2016-05-31
 */
@Controller
@RequestMapping(value = "${adminPath}/school/lessonteacher/schoolLessonTeacher")
public class SchoolLessonTeacherController extends BaseController {

	@Autowired
	private SchoolLessonTeacherService schoolLessonTeacherService;
	
	@Autowired
	private SchoolCTypeService schoolCTypeService;
	
	@Autowired
	private OfficeService officeService;
	
	@ModelAttribute
	public SchoolLessonTeacher get(@RequestParam(required=false) String id) {
		SchoolLessonTeacher entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = schoolLessonTeacherService.get(id);
		}
		if (entity == null){
			entity = new SchoolLessonTeacher();
		}
		return entity;
	}
	
	@RequiresPermissions("school:lessonteacher:schoolLessonTeacher:view")
	@RequestMapping(value = {"list", ""})
	public String list(SchoolLessonTeacher schoolLessonTeacher, HttpServletRequest request, HttpServletResponse response, Model model) {
		System.out.println("权限过滤语句：" + UserUtils.getTLC(UserUtils.getUser()));
		Page<SchoolLessonTeacher> page = schoolLessonTeacherService.findPage(new Page<SchoolLessonTeacher>(request, response), schoolLessonTeacher); 
		List<SchoolLessonTeacher> list = page.getList();
		User user = UserUtils.getUser();
		if (user.getRoleNames().contains("老师"))
			for (int i = list.size() - 1; i >= 0; i--) {
				if (!user.getId().equals(list.get(i).getTeacherId()))
					list.remove(i);
			}
		list = TranslateUtil.t.translateList(list,new TransLateCondition("teacherId","sys_user a","a.name as teacherId","id",""));
		list = TranslateUtil.t.translateList(list,new TransLateCondition("classId","sys_office a","a.name as classId","id",""));
		list = TranslateUtil.t.translateList(list,new TransLateCondition("lessonId","school_c_type a","a.name as lessonId","id",""));
		model.addAttribute("page", page);
		return "modules/school/lessonteacher/schoolLessonTeacherList";
	}

	@RequiresPermissions("school:lessonteacher:schoolLessonTeacher:view")
	@RequestMapping(value = "form")
	public String form(SchoolLessonTeacher schoolLessonTeacher, Model model) {
		if (schoolLessonTeacher.getId()!= null) {
			User user = UserUtils.get(schoolLessonTeacher.getTeacherId());
			SchoolCType schoolCType = schoolCTypeService.get(schoolLessonTeacher.getLessonId());
			Office office=officeService.get(schoolLessonTeacher.getClassId());
			model.addAttribute("teacher", user.getName());
			model.addAttribute("lesson",schoolCType!=null?schoolCType.getName():"");
			model.addAttribute("className",office!=null?office.getName():"");
		}
		model.addAttribute("schoolLessonTeacher", schoolLessonTeacher);
		return "modules/school/lessonteacher/schoolLessonTeacherForm";
	}

	@RequiresPermissions("school:lessonteacher:schoolLessonTeacher:edit")
	@RequestMapping(value = "save")
	public String save(SchoolLessonTeacher schoolLessonTeacher, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, schoolLessonTeacher)){
			return form(schoolLessonTeacher, model);
		}
		if(schoolLessonTeacher.getTeacherId()==null){
			addMessage(redirectAttributes, "保存失败，老师不能为空");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		
		if(schoolLessonTeacher.getLessonId()==null&&schoolLessonTeacher.getClassId()==null){
			addMessage(redirectAttributes, "保存失败，班级和课程不能都为空");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		schoolLessonTeacherService.save(schoolLessonTeacher);
		addMessage(redirectAttributes, "保存教师任课管理成功");
		return "redirect:"+Global.getAdminPath()+"/school/lessonteacher/schoolLessonTeacher/?repage";
	}
	
	@RequiresPermissions("school:lessonteacher:schoolLessonTeacher:edit")
	@RequestMapping(value = "delete")
	public String delete(SchoolLessonTeacher schoolLessonTeacher, RedirectAttributes redirectAttributes) {
		schoolLessonTeacherService.delete(schoolLessonTeacher);
		addMessage(redirectAttributes, "删除教师任课管理成功");
		return "redirect:"+Global.getAdminPath()+"/school/lessonteacher/schoolLessonTeacher/?repage";
	}

	/**
	 * 获取用户个人资料
	 * @param model
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "getUserInfo",method=RequestMethod.GET)
	public String getUserInfo() {
		User user = UserUtils.getUser();
		SchoolLessonTeacher schoolLessonTeacher=new SchoolLessonTeacher();
		schoolLessonTeacher.setTeacherId(user.getId());
		List<SchoolLessonTeacher> list = schoolLessonTeacherService.findList(schoolLessonTeacher);
		
		if (user.getRoleNames().contains("老师"))
			for (int i = list.size() - 1; i >= 0; i--) {
				if (!user.getId().equals(list.get(i).getTeacherId()))
					list.remove(i);
			}
		list = TranslateUtil.t.translateList(list,new TransLateCondition("teacherId","sys_user a","a.name as teacherId","id",""));
		list = TranslateUtil.t.translateList(list,new TransLateCondition("classId","sys_office a","a.name as classId","id",""));
		list = TranslateUtil.t.translateList(list,new TransLateCondition("lessonId","school_c_type a","a.name as lessonId","id",""));
		ArrayList<String> classList=new ArrayList<String>();
		ArrayList<String> lessonList=new ArrayList<String>();
		for(SchoolLessonTeacher schoolLessonTeacher2:list){
			if(schoolLessonTeacher2.getClassId()!=null&&!schoolLessonTeacher2.getClassId().equals("")){
				classList.add(schoolLessonTeacher2.getClassId());
			}
			if(schoolLessonTeacher2.getLessonId()!=null&&!schoolLessonTeacher2.getLessonId().equals("")){
				lessonList.add(schoolLessonTeacher2.getLessonId());
			}
		}
		Map<String, Object> map2 = Maps.newHashMap();
		map2.put("photo", user.getPhoto());
		map2.put("name", user.getName());
		map2.put("sex", user.getSex().equals("0")?"男":"女");
		Office office=user.getOffice();
		while(true){
			if(office.getParentId().equals("0"))
				break;
			else
				office=officeService.get(office.getParentId());
		}
		map2.put("id", user.getId());
		map2.put("unit", office.getName());//
		map2.put("phone", user.getPhone());
		map2.put("mail", user.getEmail());
		map2.put("office", user.getOffice().getName());
		map2.put("classList", classList);
		map2.put("lessonList",lessonList);
		
		Map<String, Object> map1 = Maps.newHashMap();
		map1.put("code", 1);
		map1.put("message", "success");
		map1.put("data", map2);
		return 	JsonMapper.getInstance().toJson(map1);
	}
	
	/**
	 * 获取学生个人资料
	 * @param model
	 * @return
	 */
	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "getStudentInfo",method=RequestMethod.GET)
	public String getStudentInfo() {
		User user = UserUtils.getUser();

		Map<String, Object> map2 = Maps.newHashMap();
		map2.put("photo", user.getPhoto());
		map2.put("name", user.getName());
		map2.put("sex", user.getSex().equals("0")?"男":"女");
		map2.put("id", user.getId());
		map2.put("studentNo",user.getNo());//
		map2.put("phone", user.getPhone());
		map2.put("office", user.getOffice().getName());
		
		Map<String, Object> map1 = Maps.newHashMap();
		map1.put("code", 1);
		map1.put("message", "success");
		map1.put("data", map2);
		return 	JsonMapper.getInstance().toJson(map1);
	}
}