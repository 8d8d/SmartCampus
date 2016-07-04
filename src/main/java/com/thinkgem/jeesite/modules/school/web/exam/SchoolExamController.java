/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.web.exam;

import java.io.UnsupportedEncodingException;
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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.tran.TransLateCondition;
import com.thinkgem.jeesite.common.utils.tran.TranslateUtil;
import com.thinkgem.jeesite.modules.school.entity.calendar.SchoolCalendar;
import com.thinkgem.jeesite.modules.school.entity.classtype.SchoolCType;
import com.thinkgem.jeesite.modules.school.entity.ctable.SchoolCtable;
import com.thinkgem.jeesite.modules.school.entity.exam.SchoolExam;
import com.thinkgem.jeesite.modules.school.entity.examtime.SchoolExamTime;
import com.thinkgem.jeesite.modules.school.entity.lessonstudent.SchoolLessonStudent;
import com.thinkgem.jeesite.modules.school.service.classtype.SchoolCTypeService;
import com.thinkgem.jeesite.modules.school.service.ctable.SchoolCtableService;
import com.thinkgem.jeesite.modules.school.service.exam.SchoolExamService;
import com.thinkgem.jeesite.modules.school.service.examtime.SchoolExamTimeService;
import com.thinkgem.jeesite.modules.school.service.lessonstudent.SchoolLessonStudentService;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.DictService;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 成绩查询Controller
 * @author 王超然
 * @version 2016-05-16
 */
@Controller
@RequestMapping(value = "${adminPath}/school/exam/schoolExam")
public class SchoolExamController extends BaseController {

	@Autowired
	private SchoolExamService schoolExamService;
	
	@Autowired
	private DictService dictService;
	
	@Autowired
	private SchoolExamTimeService schoolExamTimeService;
	
	@Autowired
	private SchoolCTypeService schoolCTypeService;
	
	@Autowired
	private OfficeService officeService;
	
	@Autowired
	private SchoolCtableService schoolCtableService;
	
	@Autowired
	private SchoolLessonStudentService lessonStudentService;
	
	@ModelAttribute
	public SchoolExam get(@RequestParam(required=false) String id) {
		SchoolExam entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = schoolExamService.get(id);
		}
		if (entity == null){
			entity = new SchoolExam();
		}
		return entity;
	}
	
	@RequiresPermissions("school:exam:schoolExam:view")
	@RequestMapping(value = {"list", ""})
	public String list(SchoolExam schoolExam, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SchoolExam> page = schoolExamService.findPage(new Page<SchoolExam>(request, response), schoolExam); 
		List<SchoolExam> list = page.getList();
		for(SchoolExam schoolExam2:list){
			schoolExam2.setExamId(findExamTime(schoolExam2));
		}
		User user = UserUtils.getUser();
		if (user.getRoleNames().contains("老师"))
			for (int i = list.size() - 1; i >= 0; i--) {
				SchoolCtable schoolCtable = schoolCtableService.get(list.get(i).getCourseId());
				if (!user.getId().equals(schoolCtable.getTeacherId()))
					list.remove(i);
			}
		list = TranslateUtil.t.translateList(list,new TransLateCondition("courseId","school_ctable a","a.course_id as courseId","id",""));
		list = TranslateUtil.t.translateList(list,new TransLateCondition("courseId","school_c_type a","a.name as courseId","id",""));
		list = TranslateUtil.t.translateList(list,new TransLateCondition("studentId","sys_user a","a.name as studentId","id",""));
		model.addAttribute("page", page);
		return "modules/school/exam/schoolExamList";
	}

	@RequiresPermissions("school:exam:schoolExam:view")
	@RequestMapping(value = "form")
	public String form(SchoolExam schoolExam, Model model) {
		model.addAttribute("schoolExam", schoolExam);

		if (schoolExam.getStudentId() != null) {
			User user = UserUtils.get(schoolExam.getStudentId());
			SchoolCtable schoolCtable = schoolCtableService.get(schoolExam.getCourseId());
			SchoolCType schoolCType = schoolCTypeService.get(schoolCtable.getCourseId());
			model.addAttribute("student", user.getName());
			model.addAttribute("exam",findExamTime(schoolExam));
			model.addAttribute("lesson", schoolCType.getName() + ",星期" + schoolCtable.getWeekday() + ",第"
					+ schoolCtable.getCourseOrder() + "节");
		}
		return "modules/school/exam/schoolExamForm";
	}

	@RequiresPermissions("school:exam:schoolExam:edit")
	@RequestMapping(value = "save")
	public String save(SchoolExam schoolExam, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, schoolExam)){
			return form(schoolExam, model);
		}
		
		if(schoolExam.getCourseId()==null){
			addMessage(redirectAttributes, "保存失败，课程不能为空");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		
		if(schoolExam.getStudentId()==null){
			addMessage(redirectAttributes, "保存失败，学生不能为空");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		
		if(schoolExam.getExamId()==null){
			addMessage(redirectAttributes, "保存失败，考试名称不能为空");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		
		SchoolLessonStudent schoolLessonStudent=new SchoolLessonStudent();
		schoolLessonStudent.setLessonid(schoolExam.getCourseId());
		schoolLessonStudent.setStudentid(schoolExam.getStudentId());
		if(lessonStudentService.findList(schoolLessonStudent).size()==0){
			addMessage(redirectAttributes, "保存失败，该学生没有选该课程");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		
		schoolExamService.save(schoolExam);
		addMessage(redirectAttributes, "保存成绩查询成功");
		return "redirect:"+Global.getAdminPath()+"/school/exam/schoolExam/?repage";
	}
	
	@RequiresPermissions("school:exam:schoolExam:edit")
	@RequestMapping(value = "delete")
	public String delete(SchoolExam schoolExam, RedirectAttributes redirectAttributes) {
		schoolExamService.delete(schoolExam);
		addMessage(redirectAttributes, "删除成绩查询成功");
		return "redirect:"+Global.getAdminPath()+"/school/exam/schoolExam/?repage";
	}

	private String findExamTime(SchoolExam schoolExam ) {
		if(schoolExam.getExamId()==null) return null;
		SchoolExamTime schoolExamTime=schoolExamTimeService.get(schoolExam.getExamId());
		System.out.println(schoolExam.getExamId());
		Dict dict = new Dict();
		dict.setType("exam_year");
		List<Dict> listDict1 = dictService.findList(dict);
		dict.setType("exam_term");
		List<Dict> listDict2 = dictService.findList(dict);
		dict.setType("exam_type");
		List<Dict> listDict3 = dictService.findList(dict);
		String temp=findDict(listDict1,schoolExamTime.getYear())+" "+findDict(listDict2,schoolExamTime.getTerm())+" "+findDict(listDict3,schoolExamTime.getType());			

		return temp;
	}
	
	private String findDict(List<Dict> listDict,String data){
		for(Dict d:listDict){
			if(d.getValue().equals(data)){
				return d.getLabel();
			}
		}
		return "";
		
	}
	
	
	/**
	 * 考试成绩查询接口
	 * @param 
	 * @return
	 */
	@RequiresPermissions("school:exam:schoolExam:view")
	@RequestMapping(value = "queryResults",method=RequestMethod.POST)
	@ResponseBody
	public String queryResults(@RequestParam(required=false) String subject,@RequestParam(required=false) String year,@RequestParam(required=false) String term,@RequestParam(required=false) String type,@RequestParam(required=false) String className) {

		List<SchoolExam> list=schoolExamService.findList(new SchoolExam());
		User user = UserUtils.getUser();
		for(int i=list.size()-1;i>=0;i--){
			SchoolCtable schoolCtable =schoolCtableService.get(list.get(i).getCourseId());
			if(!user.getId().equals(schoolCtable.getTeacherId()))
				list.remove(i);
		}
		
		list = TranslateUtil.t.translateList(list,new TransLateCondition("courseId","school_ctable a","a.course_id as courseId","id",""));
		list = TranslateUtil.t.translateList(list,new TransLateCondition("courseId","school_c_type a","a.id as courseId","id",""));
//		for(SchoolExam schoolExam2:list){
//			findExamTime(schoolExam2);
//		}
		list = TranslateUtil.t.translateList(list,new TransLateCondition("studentId","sys_user a","a.name as studentId,a.no as teacherId,a.office_id as classId","id",""));
//		list = TranslateUtil.t.translateList(list,new TransLateCondition("classId","sys_office a","a.name as classId","id",""));
		System.out.println(subject);
		System.out.println(className);
		if(subject==null&&year==null&&year==term&&year==type&&className==null){
			Map<String, Object> map1 = Maps.newHashMap();
			map1.put("code", 1);
			map1.put("message", "success");
			map1.put("data",Maps.newHashMap());

			return 	JsonMapper.getInstance().toJson(map1);
		}
		System.out.println(list.size());
		if(subject!=null&&!subject.equals("")){
			for(int i=0;i<list.size();i++){
				if(!list.get(i).getCourseId().equals(subject)){
					list.remove(i);
					i=i-1;
				}
			}
		}
		System.out.println(list.size());
		if(!year.equals("")&&!term.equals("")&&!type.equals("")){

			for(int i=0;i<list.size();i++){
				SchoolExamTime schoolExamTime=schoolExamTimeService.get(list.get(i).getExamId());
				if(!schoolExamTime.getYear().equals(year)||!schoolExamTime.getTerm().equals(term)||!schoolExamTime.getType().equals(type)){
					list.remove(i);
					i=i-1;
				}
			}
		}
		System.out.println(list.size());
		if(className!=null&&!className.equals("")){
			for(int i=0;i<list.size();i++){
				if(!list.get(i).getClassId().equals(className)){
					list.remove(i);
					i=i-1;
				}
			}
		}
		System.out.println(list.size());
		ArrayList<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		for(int i=0;i<list.size();i++){
			Map<String, Object> map2 = Maps.newHashMap();
			map2.put("name", list.get(i).getStudentId());
			map2.put("studentNo",list.get(i).getTeacherId());
			map2.put("score", list.get(i).getScore());
			mapList.add(map2);
		}
		Map<String, Object> map1 = Maps.newHashMap();
		map1.put("code", 1);
		map1.put("message", "success");
		map1.put("data", mapList);

		return 	JsonMapper.getInstance().toJson(map1);
	}
	
	/**
	 * 获取成绩查询条件接口
	 * @param 
	 * @return
	 */
	@RequiresPermissions("school:exam:schoolExam:view")
	@RequestMapping(value = "queryCondition",method=RequestMethod.GET)
	@ResponseBody
	public String queryCondition() {

		List<SchoolCType> listSubject = schoolCTypeService.findList(new SchoolCType());
		for(int i=0;i<listSubject.size();i++){
			if(listSubject.get(i).getParentId().equals("0"))
				listSubject.remove(i);
		}
		
		Dict dict = new Dict();
		dict.setType("exam_year");
		List<Dict> listYear=dictService.findList(dict);
		
		dict.setType("exam_term");
		List<Dict> listTerm=dictService.findList(dict);
		
		dict.setType("exam_type");
		List<Dict> listType = dictService.findList(dict);

		Map<String, Object> map3 = Maps.newHashMap();
		map3.put("year",listYear);
		map3.put("term", listTerm);
		map3.put("type", listType);

		List<Office> listClass = officeService.findAllStudents(new Office());
		
		Map<String, Object> map1 = Maps.newHashMap();
		Map<String, Object> map2 = Maps.newHashMap();
		map1.put("code", 1);
		map1.put("message", "success");
		map2.put("subject",listSubject);
		map2.put("exam",map3);
		map2.put("className",listClass);
		map1.put("data", map2);

		return 	JsonMapper.getInstance().toJson(map1);
	}
}