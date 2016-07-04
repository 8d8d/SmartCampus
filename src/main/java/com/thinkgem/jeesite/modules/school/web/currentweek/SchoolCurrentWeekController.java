/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.web.currentweek;

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
import com.thinkgem.jeesite.modules.school.entity.calendar.SchoolCalendar;
import com.thinkgem.jeesite.modules.school.entity.currentweek.SchoolCurrentWeek;
import com.thinkgem.jeesite.modules.school.service.currentweek.SchoolCurrentWeekService;

/**
 * 设置当前周Controller
 * @author 何伟杰
 * @version 2016-06-06
 */
@Controller
@RequestMapping(value = "${adminPath}/school/currentweek/schoolCurrentWeek")
public class SchoolCurrentWeekController extends BaseController {

	@Autowired
	private SchoolCurrentWeekService schoolCurrentWeekService;
	
	@ModelAttribute
	public SchoolCurrentWeek get(@RequestParam(required=false) String id) {
		SchoolCurrentWeek entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = schoolCurrentWeekService.get(id);
		}
		if (entity == null){
			entity = new SchoolCurrentWeek();
		}
		return entity;
	}
	
	@RequiresPermissions("school:currentweek:schoolCurrentWeek:view")
	@RequestMapping(value = {"list", ""})
	public String list(SchoolCurrentWeek schoolCurrentWeek, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SchoolCurrentWeek> page = schoolCurrentWeekService.findPage(new Page<SchoolCurrentWeek>(request, response), schoolCurrentWeek); 
		model.addAttribute("page", page);
		return "modules/school/currentweek/schoolCurrentWeekList";
	}

	@RequiresPermissions("school:currentweek:schoolCurrentWeek:view")
	@RequestMapping(value = "form")
	public String form(SchoolCurrentWeek schoolCurrentWeek, Model model) {
		model.addAttribute("schoolCurrentWeek", schoolCurrentWeek);
		return "modules/school/currentweek/schoolCurrentWeekForm";
	}

	@RequiresPermissions("school:currentweek:schoolCurrentWeek:edit")
	@RequestMapping(value = "save")
	public String save(SchoolCurrentWeek schoolCurrentWeek, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, schoolCurrentWeek)){
			return form(schoolCurrentWeek, model);
		}
		schoolCurrentWeekService.save(schoolCurrentWeek);
		addMessage(redirectAttributes, "保存设置当前周成功");
		return "redirect:"+Global.getAdminPath()+"/school/currentweek/schoolCurrentWeek/?repage";
	}
	
	@RequiresPermissions("school:currentweek:schoolCurrentWeek:edit")
	@RequestMapping(value = "delete")
	public String delete(SchoolCurrentWeek schoolCurrentWeek, RedirectAttributes redirectAttributes) {
		schoolCurrentWeekService.delete(schoolCurrentWeek);
		addMessage(redirectAttributes, "删除设置当前周成功");
		return "redirect:"+Global.getAdminPath()+"/school/currentweek/schoolCurrentWeek/?repage";
	}
	
	/**
	 * 请求当前周数接口
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "getWeek",method=RequestMethod.GET)
	@ResponseBody
	public String getCalendar() {
		String week="1";
		try{
			week=schoolCurrentWeekService.findList(new SchoolCurrentWeek()).get(0).getWeek();
		}
		catch(Exception e){
			week="1";
		}
		Map<String, Object> map1 = Maps.newHashMap();
		Map<String, Object> map2 = Maps.newHashMap();
		map2.put("week", week);
		map1.put("code", 1);
		map1.put("message", "success");
		map1.put("data", map2);
		return 	JsonMapper.getInstance().toJson(map1);
	}

}