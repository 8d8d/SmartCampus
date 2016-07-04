/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.web.contact;

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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.school.entity.calendar.SchoolCalendar;
import com.thinkgem.jeesite.modules.school.entity.contact.SchoolContact;
import com.thinkgem.jeesite.modules.school.service.contact.SchoolContactService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.SystemService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 联系人设置Controller
 * @author 何伟杰
 * @version 2016-06-22
 */
@Controller
@RequestMapping(value = "${adminPath}/school/contact/schoolContact")
public class SchoolContactController extends BaseController {

	@Autowired
	private SchoolContactService schoolContactService;
	
	@Autowired
	private SystemService systemService;
	
	@ModelAttribute
	public SchoolContact get(@RequestParam(required=false) String id) {
		SchoolContact entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = schoolContactService.get(id);
		}
		if (entity == null){
			entity = new SchoolContact();
		}
		return entity;
	}
	
	@RequiresPermissions("school:contact:schoolContact:view")
	@RequestMapping(value = {"list", ""})
	public String list(SchoolContact schoolContact, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<SchoolContact> list = schoolContactService.findList(schoolContact); 
		User user=UserUtils.getUser();
		for(int i=list.size()-1;i>=0;i--){
			if(!list.get(i).getCreateBy().getId().equals(user.getId()))
				list.remove(i);
		}
		model.addAttribute("list", list);
		return "modules/school/contact/schoolContactList";
	}

	@RequiresPermissions("school:contact:schoolContact:view")
	@RequestMapping(value = "form")
	public String form(SchoolContact schoolContact, Model model) {
		if (schoolContact.getParent()!=null && StringUtils.isNotBlank(schoolContact.getParent().getId())){
			schoolContact.setParent(schoolContactService.get(schoolContact.getParent().getId()));
			// 获取排序号，最末节点排序号+30
			if (StringUtils.isBlank(schoolContact.getId())){
				SchoolContact schoolContactChild = new SchoolContact();
				schoolContactChild.setParent(new SchoolContact(schoolContact.getParent().getId()));
				List<SchoolContact> list = schoolContactService.findList(schoolContact); 
				if (list.size() > 0){
					schoolContact.setSort(list.get(list.size()-1).getSort());
					if (schoolContact.getSort() != null){
						schoolContact.setSort(schoolContact.getSort() + 30);
					}
				}
			}
		}
		if (schoolContact.getSort() == null){
			schoolContact.setSort(30);
		}
		model.addAttribute("schoolContact", schoolContact);
		return "modules/school/contact/schoolContactForm";
	}

	@RequiresPermissions("school:contact:schoolContact:edit")
	@RequestMapping(value = "save")
	public String save(SchoolContact schoolContact, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, schoolContact)){
			return form(schoolContact, model);
		}
		schoolContactService.save(schoolContact);
		addMessage(redirectAttributes, "保存联系人设置成功");
		return "redirect:"+Global.getAdminPath()+"/school/contact/schoolContact/?repage";
	}
	
	@RequiresPermissions("school:contact:schoolContact:edit")
	@RequestMapping(value = "delete")
	public String delete(SchoolContact schoolContact, RedirectAttributes redirectAttributes) {
		schoolContactService.delete(schoolContact);
		addMessage(redirectAttributes, "删除联系人设置成功");
		return "redirect:"+Global.getAdminPath()+"/school/contact/schoolContact/?repage";
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<SchoolContact> list = schoolContactService.findList(new SchoolContact());
		for (int i=0; i<list.size(); i++){
			SchoolContact e = list.get(i);
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
	
	/**
	 * 获取联系人列表接口
	 * @param 
	 * @return
	 */
	@RequiresPermissions("school:contact:schoolContact:view")
	@RequestMapping(value = "getContact",method=RequestMethod.GET)
	@ResponseBody
	public String getContact() {
		User user=UserUtils.getUser();
		List<SchoolContact> list=schoolContactService.findList(new SchoolContact());
		for(int i=list.size()-1;i>=0;i--){
			if(!list.get(i).getCreateBy().getId().equals(user.getId()))
				list.remove(i);
		}
		
		ArrayList<Map<String, Object>> mapList=new ArrayList<Map<String,Object>>();
		for(SchoolContact schoolContact:list){
			if(schoolContact.getParentId().equals("0")){
				Map<String, Object> map2 = Maps.newHashMap();
				map2.put("groupid", schoolContact.getId());
				map2.put("name", schoolContact.getName());
				mapList.add(map2);
			}
		}
		
		for(Map map:mapList){
			int count=0;
			ArrayList<Map<String, Object>> mapList1=new ArrayList<Map<String,Object>>();
			for(SchoolContact schoolContact:list){
				if(schoolContact.getParentId().equals(map.get("groupid"))){
					Map<String, Object> map2 = Maps.newHashMap();
					User student=systemService.getUser(schoolContact.getUser().getId());
					map2.put("id", student.getId());
					map2.put("phone",student.getPhone());
					map2.put("username",student.getName());
					map2.put("photo",student.getPhoto());
					map2.put("loginName",student.getLoginName());
					mapList1.add(map2);
					count++;
				}
			}
			map.put("count", count);
			map.put("userList", mapList1);
		}
		
		Map<String, Object> map1 = Maps.newHashMap();
		map1.put("code", 1);
		map1.put("message", "success");
		map1.put("data", mapList);
		return 	JsonMapper.getInstance().toJson(map1);
	}
}