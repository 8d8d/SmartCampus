/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.web.tips;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import com.thinkgem.jeesite.modules.school.entity.notice.SchoolNotice;
import com.thinkgem.jeesite.modules.school.entity.tips.SchoolTips;
import com.thinkgem.jeesite.modules.school.service.tips.SchoolTipsService;
import com.thoughtworks.xstream.mapper.Mapper.Null;

/**
 * 意见反馈Controller
 * @author 王超然
 * @version 2016-05-12
 */
@Controller
@RequestMapping(value = "${adminPath}/school/tips/schoolTips")
public class SchoolTipsController extends BaseController {

	@Autowired
	private SchoolTipsService schoolTipsService;
	
	@ModelAttribute
	public SchoolTips get(@RequestParam(required=false) String id) {
		SchoolTips entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = schoolTipsService.get(id);
		}
		if (entity == null){
			entity = new SchoolTips();
		}
		return entity;
	}
	
	@RequiresPermissions("school:tips:schoolTips:view")
	@RequestMapping(value = {"list", ""})
	public String list(SchoolTips schoolTips, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SchoolTips> page = schoolTipsService.findPage(new Page<SchoolTips>(request, response), schoolTips); 
		model.addAttribute("page", page);
		return "modules/school/tips/schoolTipsList";
	}

	@RequiresPermissions("school:tips:schoolTips:view")
	@RequestMapping(value = "form")
	public String form(SchoolTips schoolTips, Model model) {
		model.addAttribute("schoolTips", schoolTips);
		return "modules/school/tips/schoolTipsForm";
	}

	@RequiresPermissions("school:tips:schoolTips:edit")
	@RequestMapping(value = "save")
	public String save(SchoolTips schoolTips, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, schoolTips)){
			return form(schoolTips, model);
		}
		schoolTipsService.save(schoolTips);
		addMessage(redirectAttributes, "保存意见反馈成功");
		return "redirect:"+Global.getAdminPath()+"/school/tips/schoolTips/?repage";
	}
	
	@RequiresPermissions("school:tips:schoolTips:edit")
	@RequestMapping(value = "delete")
	public String delete(SchoolTips schoolTips, RedirectAttributes redirectAttributes) {
		schoolTipsService.delete(schoolTips);
		addMessage(redirectAttributes, "删除意见反馈成功");
		return "redirect:"+Global.getAdminPath()+"/school/tips/schoolTips/?repage";
	}

	/**
	 * 保存意见反馈
	 * @param content 反馈内容
	 * @return
	 */
	@RequiresPermissions("school:tips:schoolTips:edit")
	@RequestMapping(value = "saveTips",method=RequestMethod.POST)
	@ResponseBody
	public String saveTips(@RequestParam(required=false) String content) {
		SchoolTips schoolTips=new SchoolTips();
		schoolTips.setContent(content);
		Calendar c=Calendar.getInstance();   
		schoolTips.setUpdateTime(c.getTime());
		schoolTipsService.save(schoolTips);

		Map<String, Object> map1 = Maps.newHashMap();
		Map<String, Object> map2 = Maps.newHashMap();
		map1.put("code", 1);
		map1.put("message", "success");
		map1.put("data",map2);
		return 	JsonMapper.getInstance().toJson(map1);
	}
}