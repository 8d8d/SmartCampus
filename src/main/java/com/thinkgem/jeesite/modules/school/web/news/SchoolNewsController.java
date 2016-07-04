/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.web.news;

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
import com.thinkgem.jeesite.modules.school.entity.news.SchoolNews;
import com.thinkgem.jeesite.modules.school.service.news.SchoolNewsService;

/**
 * 校园新闻Controller
 * @author 王超然
 * @version 2016-05-31
 */
@Controller
@RequestMapping(value = "${adminPath}/school/news/schoolNews")
public class SchoolNewsController extends BaseController {

	@Autowired
	private SchoolNewsService schoolNewsService;
	
	@ModelAttribute
	public SchoolNews get(@RequestParam(required=false) String id) {
		SchoolNews entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = schoolNewsService.get(id);
		}
		if (entity == null){
			entity = new SchoolNews();
		}
		return entity;
	}
	
	@RequiresPermissions("school:news:schoolNews:view")
	@RequestMapping(value = {"list", ""})
	public String list(SchoolNews schoolNews, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SchoolNews> page = schoolNewsService.findPage(new Page<SchoolNews>(request, response), schoolNews); 
		model.addAttribute("page", page);
		return "modules/school/news/schoolNewsList";
	}

	@RequiresPermissions("school:news:schoolNews:view")
	@RequestMapping(value = "form")
	public String form(SchoolNews schoolNews, Model model) {
		model.addAttribute("schoolNews", schoolNews);
		return "modules/school/news/schoolNewsForm";
	}

	@RequiresPermissions("school:news:schoolNews:edit")
	@RequestMapping(value = "save")
	public String save(SchoolNews schoolNews, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, schoolNews)){
			return form(schoolNews, model);
		}
		schoolNewsService.save(schoolNews);
		addMessage(redirectAttributes, "保存校园新闻成功");
		return "redirect:"+Global.getAdminPath()+"/school/news/schoolNews/?repage";
	}
	
	@RequiresPermissions("school:news:schoolNews:edit")
	@RequestMapping(value = "delete")
	public String delete(SchoolNews schoolNews, RedirectAttributes redirectAttributes) {
		schoolNewsService.delete(schoolNews);
		addMessage(redirectAttributes, "删除校园新闻成功");
		return "redirect:"+Global.getAdminPath()+"/school/news/schoolNews/?repage";
	}

	/**
	 * 分页获取新闻
	 * @param index 当前页
	 * @param pageSize 每页条数
	 * @return
	 */
	@RequiresPermissions("school:news:schoolNews:view")
	@RequestMapping(value = "getNews",method=RequestMethod.GET)
	@ResponseBody
	public String getNews(@RequestParam(required=true) int index,@RequestParam(required=true) int pageSize) {
		
		Page<SchoolNews> page=new Page<SchoolNews>(index,pageSize);
		page.setOrderBy("a.update_date desc");
		List<SchoolNews> list=schoolNewsService.findPage(page,new SchoolNews()).getList();
		Map<String, Object> map1 = Maps.newHashMap();
		map1.put("page", page.isLastPage()?0:page.getNext());
		map1.put("code", 1);
		map1.put("message", "success");
		map1.put("data", list);
		return 	JsonMapper.getInstance().toJson(map1);
	}
}