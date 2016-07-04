/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.web.notice;

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
import com.thinkgem.jeesite.modules.school.entity.news.SchoolNews;
import com.thinkgem.jeesite.modules.school.entity.notice.SchoolNotice;
import com.thinkgem.jeesite.modules.school.service.notice.SchoolNoticeService;

/**
 * 校园公告Controller
 * 
 * @author 王超然
 * @version 2016-05-12
 */
@Controller
@RequestMapping(value = "${adminPath}/school/notice/schoolNotice")
public class SchoolNoticeController extends BaseController {

	@Autowired
	private SchoolNoticeService schoolNoticeService;

	@ModelAttribute
	public SchoolNotice get(@RequestParam(required = false) String id) {
		SchoolNotice entity = null;
		if (StringUtils.isNotBlank(id)) {
			entity = schoolNoticeService.get(id);
		}
		if (entity == null) {
			entity = new SchoolNotice();
		}
		return entity;
	}

	@RequiresPermissions("school:notice:schoolNotice:view")
	@RequestMapping(value = { "list", "" })
	public String list(SchoolNotice schoolNotice, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<SchoolNotice> page = schoolNoticeService.findPage(new Page<SchoolNotice>(request, response), schoolNotice);
		model.addAttribute("page", page);
		if (1 == 1) {
			List list = page.getList();
			String json = JsonMapper.getInstance().toJson(list);
			Map<String, Object> map = Maps.newHashMap();
			map.put("code", 1);
			map.put("message", "success");
			map.put("data", list);
			json = JsonMapper.getInstance().toJson(map);
			System.out.println(json);
		}
		return "modules/school/notice/schoolNoticeList";
	}

	@RequestMapping(value = { "listMobile" })
	public String listMobile(SchoolNotice schoolNotice, HttpServletRequest request, HttpServletResponse response,
			Model model) {
		Page<SchoolNotice> page = schoolNoticeService.findPage(new Page<SchoolNotice>(request, response), schoolNotice);
		List list = page.getList();
		String json = JsonMapper.getInstance().toJson(list);
		Map<String, Object> map = Maps.newHashMap();
		map.put("code", 1);
		map.put("message", "success");
		map.put("data", list);
		json = JsonMapper.getInstance().toJson(map);
		System.out.println(json);
		return json;
	}

	@RequiresPermissions("school:notice:schoolNotice:view")
	@RequestMapping(value = "form")
	public String form(SchoolNotice schoolNotice, Model model) {
		model.addAttribute("schoolNotice", schoolNotice);
		return "modules/school/notice/schoolNoticeForm";
	}

	@RequiresPermissions("school:notice:schoolNotice:edit")
	@RequestMapping(value = "save")
	public String save(SchoolNotice schoolNotice, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, schoolNotice)) {
			return form(schoolNotice, model);
		}
		schoolNoticeService.save(schoolNotice);
		addMessage(redirectAttributes, "保存校园公告成功");
		return "redirect:" + Global.getAdminPath() + "/school/notice/schoolNotice/?repage";
	}

	@RequiresPermissions("school:notice:schoolNotice:edit")
	@RequestMapping(value = "delete")
	public String delete(SchoolNotice schoolNotice, RedirectAttributes redirectAttributes) {
		schoolNoticeService.delete(schoolNotice);
		addMessage(redirectAttributes, "删除校园公告成功");
		return "redirect:" + Global.getAdminPath() + "/school/notice/schoolNotice/?repage";
	}

	/**
	 * 分页获取公告
	 * @param index 当前页
	 * @param pageSize 每页条数
	 * @return
	 */
	@RequiresPermissions("school:notice:schoolNotice:view")
	@RequestMapping(value = "getNotice",method=RequestMethod.GET)
	@ResponseBody
	public String getNotice(@RequestParam(required=true) int index,@RequestParam(required=true) int pageSize) {
		
		Page<SchoolNotice> page=new Page<SchoolNotice>(index,pageSize);
		page.setOrderBy("a.update_date desc");
		List<SchoolNotice> list=schoolNoticeService.findPage(page,new SchoolNotice()).getList();
		if(page.isEndPage()) 
			list=new ArrayList<SchoolNotice>();
		Map<String, Object> map1 = Maps.newHashMap();
		map1.put("page", page.isLastPage()?0:page.getNext());
		map1.put("code", 1);
		map1.put("message", "success");
		map1.put("data", list);
		return 	JsonMapper.getInstance().toJson(map1);
	}
}