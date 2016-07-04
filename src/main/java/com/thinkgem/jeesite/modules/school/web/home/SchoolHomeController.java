/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.web.home;

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
import com.thinkgem.jeesite.modules.school.entity.home.SchoolHome;
import com.thinkgem.jeesite.modules.school.entity.news.SchoolNews;
import com.thinkgem.jeesite.modules.school.entity.notice.SchoolNotice;
import com.thinkgem.jeesite.modules.school.service.home.SchoolHomeService;
import com.thinkgem.jeesite.modules.school.service.news.SchoolNewsService;
import com.thinkgem.jeesite.modules.school.service.notice.SchoolNoticeService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 首页Controller
 * @author 王超然
 * @version 2016-05-26
 */
@Controller
@RequestMapping(value = "${adminPath}/school/home/schoolHome")
public class SchoolHomeController extends BaseController {

	@Autowired
	private SchoolHomeService schoolHomeService;
	
	@Autowired
	private SchoolNewsService schoolNewsService;
	
	@Autowired
	private SchoolNoticeService schoolNoticeService;
	
	@ModelAttribute
	public SchoolHome get(@RequestParam(required=false) String id) {
		SchoolHome entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = schoolHomeService.get(id);
		}
		if (entity == null){
			entity = new SchoolHome();
		}
		return entity;
	}
	
	@RequiresPermissions("school:home:schoolHome:view")
	@RequestMapping(value = {"list", ""})
	public String list(SchoolHome schoolHome, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SchoolHome> page = schoolHomeService.findPage(new Page<SchoolHome>(request, response), schoolHome); 
		model.addAttribute("page", page);
		SchoolNotice schoolNotice = new SchoolNotice();
		SchoolNews schoolNews = new SchoolNews();
		if(1==2){
			Page<SchoolNotice> pageNotice = schoolNoticeService.findPage(new Page<SchoolNotice>(request, response), schoolNotice);
			Page<SchoolNews> pageNews = schoolNewsService.findPage(new Page<SchoolNews>(request, response), schoolNews);
			model.addAttribute("pageNotice", pageNotice);
			model.addAttribute("pageNews", pageNews);
		}
		return "modules/school/home/schoolHomeList";
	}

	@RequiresPermissions("school:home:schoolHome:view")
	@RequestMapping(value = "form")
	public String form(SchoolHome schoolHome, Model model) {
		model.addAttribute("schoolHome", schoolHome);
		return "modules/school/home/schoolHomeForm";
	}

	@RequiresPermissions("school:home:schoolHome:edit")
	@RequestMapping(value = "save")
	public String save(SchoolHome schoolHome, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, schoolHome)){
			return form(schoolHome, model);
		}
		schoolHomeService.save(schoolHome);
		addMessage(redirectAttributes, "保存首页成功");
		return "redirect:"+Global.getAdminPath()+"/school/home/schoolHome/?repage";
	}
	
	@RequiresPermissions("school:home:schoolHome:edit")
	@RequestMapping(value = "delete")
	public String delete(SchoolHome schoolHome, RedirectAttributes redirectAttributes) {
		schoolHomeService.delete(schoolHome);
		addMessage(redirectAttributes, "删除首页成功");
		return "redirect:"+Global.getAdminPath()+"/school/home/schoolHome/?repage";
	}

	
	/**
	 * 请求首页功能接口
	 * @param 
	 * @return
	 */
	@RequiresPermissions("school:home:schoolHome:view")
	@RequestMapping(value = "getHome",method=RequestMethod.GET)
	@ResponseBody
	public String getHome() {
		User user=UserUtils.getUser();
		System.out.println(user.getId());
		List<SchoolHome> list=schoolHomeService.findList(new SchoolHome());
		Map<String, Object> map1 = Maps.newHashMap();
		map1.put("code", 1);
		map1.put("message", "success");
		map1.put("data", list);
		return 	JsonMapper.getInstance().toJson(map1);
	}
}