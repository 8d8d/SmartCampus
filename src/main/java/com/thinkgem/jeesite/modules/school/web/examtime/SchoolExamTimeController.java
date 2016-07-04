/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.web.examtime;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.tran.TransLateCondition;
import com.thinkgem.jeesite.common.utils.tran.TranslateUtil;
import com.thinkgem.jeesite.modules.school.entity.ctable.SchoolCtable;
import com.thinkgem.jeesite.modules.school.entity.examtime.SchoolExamTime;
import com.thinkgem.jeesite.modules.school.service.examtime.SchoolExamTimeService;
import com.thinkgem.jeesite.modules.sys.entity.Dict;
import com.thinkgem.jeesite.modules.sys.service.DictService;

/**
 * 考试时间设置Controller
 * @author 何伟杰
 * @version 2016-06-12
 */
@Controller
@RequestMapping(value = "${adminPath}/school/examtime/schoolExamTime")
public class SchoolExamTimeController extends BaseController {

	@Autowired
	private DictService dictService;
	
	@Autowired
	private SchoolExamTimeService schoolExamTimeService;
	
	@ModelAttribute
	public SchoolExamTime get(@RequestParam(required=false) String id) {
		SchoolExamTime entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = schoolExamTimeService.get(id);
		}
		if (entity == null){
			entity = new SchoolExamTime();
		}
		return entity;
	}
	
	@RequiresPermissions("school:examtime:schoolExamTime:view")
	@RequestMapping(value = {"list", ""})
	public String list(SchoolExamTime schoolExamTime, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SchoolExamTime> page = schoolExamTimeService.findPage(new Page<SchoolExamTime>(request, response), schoolExamTime); 
		model.addAttribute("page", page);
		return "modules/school/examtime/schoolExamTimeList";
	}

	@RequiresPermissions("school:examtime:schoolExamTime:view")
	@RequestMapping(value = "form")
	public String form(SchoolExamTime schoolExamTime, Model model) {
		model.addAttribute("schoolExamTime", schoolExamTime);
		return "modules/school/examtime/schoolExamTimeForm";
	}

	@RequiresPermissions("school:examtime:schoolExamTime:edit")
	@RequestMapping(value = "save")
	public String save(SchoolExamTime schoolExamTime, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, schoolExamTime)){
			return form(schoolExamTime, model);
		}
		if(schoolExamTime.getYear()==null){
			addMessage(redirectAttributes, "保存失败，年度不能为空");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		
		if(schoolExamTime.getTerm()==null){
			addMessage(redirectAttributes, "保存失败，学期不能为空");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		
		if(schoolExamTime.getType()==null){
			addMessage(redirectAttributes, "保存失败，类型不能为空");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		
		schoolExamTimeService.save(schoolExamTime);
		addMessage(redirectAttributes, "保存考试时间设置成功");
		return "redirect:"+Global.getAdminPath()+"/school/examtime/schoolExamTime/?repage";
	}
	
	@RequiresPermissions("school:examtime:schoolExamTime:edit")
	@RequestMapping(value = "delete")
	public String delete(SchoolExamTime schoolExamTime, RedirectAttributes redirectAttributes) {
		schoolExamTimeService.delete(schoolExamTime);
		addMessage(redirectAttributes, "删除考试时间设置成功");
		return "redirect:"+Global.getAdminPath()+"/school/examtime/schoolExamTime/?repage";
	}

	/**
	 * 获取所有考试
	 * @param officeId
	 * @param response
	 * @return
	 */
	@RequiresPermissions("school:examtime:schoolExamTime:view")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();

	
		List<SchoolExamTime> list=schoolExamTimeService.findList(new SchoolExamTime());
		Dict dict = new Dict();
		dict.setType("exam_year");
		List<Dict> listDict1 = dictService.findList(dict);
		dict.setType("exam_term");
		List<Dict> listDict2 = dictService.findList(dict);
		dict.setType("exam_type");
		List<Dict> listDict3 = dictService.findList(dict);
		for (SchoolExamTime e : list) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", 0);
			map.put("name", findDict(listDict1,e.getYear())+" "+findDict(listDict2,e.getTerm())+" "+findDict(listDict3,e.getType()));
			mapList.add(map);			
		}
		return mapList;
	}
	
	private String findDict(List<Dict> listDict,String data){
		for(Dict d:listDict){
			if(d.getValue().equals(data)){
				return d.getLabel();
			}
		}
		return "";
		
	}
}