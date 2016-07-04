/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.web.classroom;

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
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.school.entity.classroom.SchoolClassroom;
import com.thinkgem.jeesite.modules.school.service.classroom.SchoolClassroomService;

/**
 * 教室名称Controller
 * @author 王
 * @version 2016-05-10
 */
@Controller
@RequestMapping(value = "${adminPath}/school/classroom/schoolClassroom")
public class SchoolClassroomController extends BaseController {

	@Autowired
	private SchoolClassroomService schoolClassroomService;
	
	@ModelAttribute
	public SchoolClassroom get(@RequestParam(required=false) String id) {
		SchoolClassroom entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = schoolClassroomService.get(id);
		}
		if (entity == null){
			entity = new SchoolClassroom();
		}
		return entity;
	}
	
	@RequiresPermissions("school:classroom:schoolClassroom:view")
	@RequestMapping(value = {"list", ""})
	public String list(SchoolClassroom schoolClassroom, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<SchoolClassroom> list = schoolClassroomService.findList(schoolClassroom); 
		model.addAttribute("list", list);
		return "modules/school/classroom/schoolClassroomList";
	}

	@RequiresPermissions("school:classroom:schoolClassroom:view")
	@RequestMapping(value = "form")
	public String form(SchoolClassroom schoolClassroom, Model model) {
		if (schoolClassroom.getParent()!=null && StringUtils.isNotBlank(schoolClassroom.getParent().getId())){
			schoolClassroom.setParent(schoolClassroomService.get(schoolClassroom.getParent().getId()));
			// 获取排序号，最末节点排序号+30
			if (StringUtils.isBlank(schoolClassroom.getId())){
				SchoolClassroom schoolClassroomChild = new SchoolClassroom();
				schoolClassroomChild.setParent(new SchoolClassroom(schoolClassroom.getParent().getId()));
				List<SchoolClassroom> list = schoolClassroomService.findList(schoolClassroom); 
				if (list.size() > 0){
					schoolClassroom.setSort(list.get(list.size()-1).getSort());
					if (schoolClassroom.getSort() != null){
						schoolClassroom.setSort(schoolClassroom.getSort() + 30);
					}
				}
			}
		}
		if (schoolClassroom.getSort() == null){
			schoolClassroom.setSort(30);
		}
		model.addAttribute("schoolClassroom", schoolClassroom);
		return "modules/school/classroom/schoolClassroomForm";
	}

	@RequiresPermissions("school:classroom:schoolClassroom:edit")
	@RequestMapping(value = "save")
	public String save(SchoolClassroom schoolClassroom, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, schoolClassroom)){
			return form(schoolClassroom, model);
		}
		schoolClassroomService.save(schoolClassroom);
		addMessage(redirectAttributes, "保存教室名称成功");
		return "redirect:"+Global.getAdminPath()+"/school/classroom/schoolClassroom/?repage";
	}
	
	@RequiresPermissions("school:classroom:schoolClassroom:edit")
	@RequestMapping(value = "delete")
	public String delete(SchoolClassroom schoolClassroom, RedirectAttributes redirectAttributes) {
		schoolClassroomService.delete(schoolClassroom);
		addMessage(redirectAttributes, "删除教室名称成功");
		return "redirect:"+Global.getAdminPath()+"/school/classroom/schoolClassroom/?repage";
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<SchoolClassroom> list = schoolClassroomService.findList(new SchoolClassroom());
		for (int i=0; i<list.size(); i++){
			SchoolClassroom e = list.get(i);
			if (StringUtils.isBlank(extId) || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
	
}