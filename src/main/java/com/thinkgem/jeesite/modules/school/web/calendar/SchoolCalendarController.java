/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.web.calendar;

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
import com.thinkgem.jeesite.modules.school.service.calendar.SchoolCalendarService;

/**
 * 校历Controller
 * @author 王
 * @version 2016-05-31
 */
@Controller
@RequestMapping(value = "${adminPath}/school/calendar/schoolCalendar")
public class SchoolCalendarController extends BaseController {

	@Autowired
	private SchoolCalendarService schoolCalendarService;
	
	@ModelAttribute
	public SchoolCalendar get(@RequestParam(required=false) String id) {
		SchoolCalendar entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = schoolCalendarService.get(id);
		}
		if (entity == null){
			entity = new SchoolCalendar();
		}
		return entity;
	}
	
	@RequiresPermissions("school:calendar:schoolCalendar:view")
	@RequestMapping(value = {"list", ""})
	public String list(SchoolCalendar schoolCalendar, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SchoolCalendar> page = schoolCalendarService.findPage(new Page<SchoolCalendar>(request, response), schoolCalendar); 
		model.addAttribute("page", page);
		return "modules/school/calendar/schoolCalendarList";
	}

	@RequiresPermissions("school:calendar:schoolCalendar:view")
	@RequestMapping(value = "form")
	public String form(SchoolCalendar schoolCalendar, Model model) {
		model.addAttribute("schoolCalendar", schoolCalendar);
		return "modules/school/calendar/schoolCalendarForm";
	}

	@RequiresPermissions("school:calendar:schoolCalendar:edit")
	@RequestMapping(value = "save")
	public String save(SchoolCalendar schoolCalendar, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, schoolCalendar)){
			return form(schoolCalendar, model);
		}
		schoolCalendarService.save(schoolCalendar);
		addMessage(redirectAttributes, "保存校历成功");
		return "redirect:"+Global.getAdminPath()+"/school/calendar/schoolCalendar/?repage";
	}
	
	@RequiresPermissions("school:calendar:schoolCalendar:edit")
	@RequestMapping(value = "delete")
	public String delete(SchoolCalendar schoolCalendar, RedirectAttributes redirectAttributes) {
		schoolCalendarService.delete(schoolCalendar);
		addMessage(redirectAttributes, "删除校历成功");
		return "redirect:"+Global.getAdminPath()+"/school/calendar/schoolCalendar/?repage";
	}
	
	/**
	 * 请求校历接口
	 * @param 
	 * @return
	 */
	@RequiresPermissions("school:calendar:schoolCalendar:view")
	@RequestMapping(value = "getCalendar",method=RequestMethod.GET)
	@ResponseBody
	public String getCalendar() {
		List<SchoolCalendar> list=schoolCalendarService.findList(new SchoolCalendar());
		Map<String, Object> map1 = Maps.newHashMap();
		map1.put("code", 1);
		map1.put("message", "success");
		map1.put("data", list);
		return 	JsonMapper.getInstance().toJson(map1);
	}

}