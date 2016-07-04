/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.web.agenda;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.thinkgem.jeesite.modules.school.entity.agenda.SchoolAgenda;
import com.thinkgem.jeesite.modules.school.entity.news.SchoolNews;
import com.thinkgem.jeesite.modules.school.service.agenda.SchoolAgendaService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 日程安排Controller
 * @author 何伟杰
 * @version 2016-06-20
 */
@Controller
@RequestMapping(value = "${adminPath}/school/agenda/schoolAgenda")
public class SchoolAgendaController extends BaseController {

	@Autowired
	private SchoolAgendaService schoolAgendaService;
	
	@ModelAttribute
	public SchoolAgenda get(@RequestParam(required=false) String id) {
		SchoolAgenda entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = schoolAgendaService.get(id);
		}
		if (entity == null){
			entity = new SchoolAgenda();
		}
		return entity;
	}
	
	@RequiresPermissions("school:agenda:schoolAgenda:view")
	@RequestMapping(value = {"list", ""})
	public String list(SchoolAgenda schoolAgenda, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SchoolAgenda> page = schoolAgendaService.findPage(new Page<SchoolAgenda>(request, response), schoolAgenda); 
		model.addAttribute("page", page);
		return "modules/school/agenda/schoolAgendaList";
	}

	@RequiresPermissions("school:agenda:schoolAgenda:view")
	@RequestMapping(value = "form")
	public String form(SchoolAgenda schoolAgenda, Model model) {
		model.addAttribute("schoolAgenda", schoolAgenda);
		return "modules/school/agenda/schoolAgendaForm";
	}

	@RequiresPermissions("school:agenda:schoolAgenda:edit")
	@RequestMapping(value = "save")
	public String save(SchoolAgenda schoolAgenda, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, schoolAgenda)){
			return form(schoolAgenda, model);
		}
		if(schoolAgenda.getUser()==null){
			addMessage(redirectAttributes, "保存失败，用户不能为空");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		
		if(schoolAgenda.getRepeats()==null){
			addMessage(redirectAttributes, "保存失败，重复方式不能为空");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		
		if(schoolAgenda.getRemind()==null){
			addMessage(redirectAttributes, "保存失败，提醒方式不能为空");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		
		User user=UserUtils.getUser();
		if(!user.getId().equals(schoolAgenda.getUser().getId())){
			addMessage(redirectAttributes, "保存失败，不能选择其他用户");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		schoolAgendaService.save(schoolAgenda);
		addMessage(redirectAttributes, "保存日程安排成功");
		return "redirect:"+Global.getAdminPath()+"/school/agenda/schoolAgenda/?repage";
	}
	
	@RequiresPermissions("school:agenda:schoolAgenda:edit")
	@RequestMapping(value = "delete")
	public String delete(SchoolAgenda schoolAgenda, RedirectAttributes redirectAttributes) {
		schoolAgendaService.delete(schoolAgenda);
		addMessage(redirectAttributes, "删除日程安排成功");
		return "redirect:"+Global.getAdminPath()+"/school/agenda/schoolAgenda/?repage";
	}
	
	/**
	 * 分页获取日程
	 * @param index 当前页
	 * @param pageSize 每页条数
	 * @return
	 */
	@RequiresPermissions("school:agenda:schoolAgenda:view")
	@RequestMapping(value = "getAgenda",method=RequestMethod.GET)
	@ResponseBody
	public String getAgenda(@RequestParam(required=true) int index,@RequestParam(required=true) int pageSize) {
		User user=UserUtils.getUser();
		SchoolAgenda schoolAgenda=new SchoolAgenda();
		schoolAgenda.setUser(user);
		Page<SchoolAgenda> page=new Page<SchoolAgenda>(index,pageSize);
		page.setOrderBy("a.update_date desc");
		List<SchoolAgenda> list=schoolAgendaService.findPage(page,schoolAgenda).getList();
		Map<String, Object> map1 = Maps.newHashMap();
		map1.put("page", page.isLastPage()?0:page.getNext());
		map1.put("code", 1);
		map1.put("message", "success");
		map1.put("data", list);
		return 	JsonMapper.getInstance().toJson(map1);
	}

	/**
	 * 删除日程
	 * @return
	 */
	@RequiresPermissions("school:agenda:schoolAgenda:edit")
	@RequestMapping(value = "deleteAgenda",method=RequestMethod.POST)
	@ResponseBody
	public String deleteAgenda(@RequestParam(required=true) String id) {
		SchoolAgenda schoolAgenda=schoolAgendaService.get(id);
		schoolAgendaService.delete(schoolAgenda);
		Map<String, Object> map1 = Maps.newHashMap();
		map1.put("code", 1);
		map1.put("message", "success");
		map1.put("data", Maps.newHashMap());
		return 	JsonMapper.getInstance().toJson(map1);
	}
	
	/**
	 * 添加日程
	 * @return
	 */
	@RequiresPermissions("school:agenda:schoolAgenda:edit")
	@RequestMapping(value = "createAgenda",method=RequestMethod.POST)
	@ResponseBody
	public String createAgenda(@RequestParam(required=true) String content,@RequestParam(required=true) String start
			,@RequestParam(required=true) String end,@RequestParam(required=true) String remind,@RequestParam(required=true) String repeats) {
		User user=UserUtils.getUser();
		SchoolAgenda schoolAgenda=new SchoolAgenda();
		schoolAgenda.setContent(content);
		schoolAgenda.setRemind(remind);
		schoolAgenda.setRepeats(repeats);
		schoolAgenda.setUser(user);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			schoolAgenda.setEnd(sdf.parse(end));
			schoolAgenda.setStart(sdf.parse(start));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		schoolAgendaService.save(schoolAgenda);
		Map<String, Object> map1 = Maps.newHashMap();
		map1.put("code", 1);
		map1.put("message", "success");
		map1.put("data", Maps.newHashMap());
		return 	JsonMapper.getInstance().toJson(map1);
	}
	
	/**
	 * 修改日程
	 * @return
	 */
	@RequiresPermissions("school:agenda:schoolAgenda:edit")
	@RequestMapping(value = "updateAgenda", method = RequestMethod.POST)
	@ResponseBody
	public String updateAgenda(@RequestParam(required = true) String id, @RequestParam(required = true) String content,
			@RequestParam(required = true) String start, @RequestParam(required = true) String end,
			@RequestParam(required = true) String remind, @RequestParam(required = true) String repeats) {
		User user = UserUtils.getUser();
		SchoolAgenda schoolAgenda = schoolAgendaService.get(id);
		if (schoolAgenda == null) {
			Map<String, Object> map1 = Maps.newHashMap();
			map1.put("code", 0);
			map1.put("message", "查无此日程");
			map1.put("data", Maps.newHashMap());
			return JsonMapper.getInstance().toJson(map1);
		} else {
			schoolAgenda.setContent(content);
			schoolAgenda.setRemind(remind);
			schoolAgenda.setRepeats(repeats);
			schoolAgenda.setUser(user);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			try {
				schoolAgenda.setEnd(sdf.parse(end));
				schoolAgenda.setStart(sdf.parse(start));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			schoolAgendaService.save(schoolAgenda);
			Map<String, Object> map1 = Maps.newHashMap();
			map1.put("code", 1);
			map1.put("message", "success");
			map1.put("data", Maps.newHashMap());
			return JsonMapper.getInstance().toJson(map1);
		}
	}
}