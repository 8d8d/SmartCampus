/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.web.classtype;

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
import com.thinkgem.jeesite.modules.school.entity.classtype.SchoolCType;
import com.thinkgem.jeesite.modules.school.service.classtype.SchoolCTypeService;

/**
 * 课程类型Controller
 * @author 王
 * @version 2016-05-10
 */
@Controller
@RequestMapping(value = "${adminPath}/school/classtype/schoolCType")
public class SchoolCTypeController extends BaseController {

	@Autowired
	private SchoolCTypeService schoolCTypeService;
	
	@ModelAttribute
	public SchoolCType get(@RequestParam(required=false) String id) {
		SchoolCType entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = schoolCTypeService.get(id);
		}
		if (entity == null){
			entity = new SchoolCType();
		}
		return entity;
	}
	
	@RequiresPermissions("school:classtype:schoolCType:view")
	@RequestMapping(value = {"list", ""})
	public String list(SchoolCType schoolCType, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<SchoolCType> list = schoolCTypeService.findList(schoolCType); 
		model.addAttribute("list", list);
		return "modules/school/classtype/schoolCTypeList";
	}

	@RequiresPermissions("school:classtype:schoolCType:view")
	@RequestMapping(value = "form")
	public String form(SchoolCType schoolCType, Model model) {
		if (schoolCType.getParent()!=null && StringUtils.isNotBlank(schoolCType.getParent().getId())){
			schoolCType.setParent(schoolCTypeService.get(schoolCType.getParent().getId()));
			// 获取排序号，最末节点排序号+30
			if (StringUtils.isBlank(schoolCType.getId())){
				SchoolCType schoolCTypeChild = new SchoolCType();
				schoolCTypeChild.setParent(new SchoolCType(schoolCType.getParent().getId()));
				List<SchoolCType> list = schoolCTypeService.findList(schoolCType); 
				if (list.size() > 0){
					schoolCType.setSort(list.get(list.size()-1).getSort());
					if (schoolCType.getSort() != null){
						schoolCType.setSort(schoolCType.getSort() + 30);
					}
				}
			}
		}
		if (schoolCType.getSort() == null){
			schoolCType.setSort(30);
		}
		model.addAttribute("schoolCType", schoolCType);
		return "modules/school/classtype/schoolCTypeForm";
	}

	@RequiresPermissions("school:classtype:schoolCType:edit")
	@RequestMapping(value = "save")
	public String save(SchoolCType schoolCType, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, schoolCType)){
			return form(schoolCType, model);
		}
		schoolCTypeService.save(schoolCType);
		addMessage(redirectAttributes, "保存课程类型成功");
		return "redirect:"+Global.getAdminPath()+"/school/classtype/schoolCType/?repage";
	}
	
	@RequiresPermissions("school:classtype:schoolCType:edit")
	@RequestMapping(value = "delete")
	public String delete(SchoolCType schoolCType, RedirectAttributes redirectAttributes) {
		schoolCTypeService.delete(schoolCType);
		addMessage(redirectAttributes, "删除课程类型成功");
		return "redirect:"+Global.getAdminPath()+"/school/classtype/schoolCType/?repage";
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<SchoolCType> list = schoolCTypeService.findList(new SchoolCType());
		for (int i=0; i<list.size(); i++){
			SchoolCType e = list.get(i);
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