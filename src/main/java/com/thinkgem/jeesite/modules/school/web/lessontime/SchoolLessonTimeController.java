/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.web.lessontime;

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
import com.thinkgem.jeesite.modules.school.entity.lessontime.SchoolLessonTime;
import com.thinkgem.jeesite.modules.school.service.lessontime.SchoolLessonTimeService;

/**
 * 上课时间表Controller
 * @author 王超然
 * @version 2016-05-12
 */
@Controller
@RequestMapping(value = "${adminPath}/school/lessontime/schoolLessonTime")
public class SchoolLessonTimeController extends BaseController {

	@Autowired
	private SchoolLessonTimeService schoolLessonTimeService;
	
	@ModelAttribute
	public SchoolLessonTime get(@RequestParam(required=false) String id) {
		SchoolLessonTime entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = schoolLessonTimeService.get(id);
		}
		if (entity == null){
			entity = new SchoolLessonTime();
		}
		return entity;
	}
	
	@RequiresPermissions("school:lessontime:schoolLessonTime:view")
	@RequestMapping(value = {"list", ""})
	public String list(SchoolLessonTime schoolLessonTime, HttpServletRequest request, HttpServletResponse response, Model model) {
		List<SchoolLessonTime> list = schoolLessonTimeService.findList(schoolLessonTime); 
		model.addAttribute("list", list);
		return "modules/school/lessontime/schoolLessonTimeList";
	}

	@RequiresPermissions("school:lessontime:schoolLessonTime:view")
	@RequestMapping(value = "form")
	public String form(SchoolLessonTime schoolLessonTime, Model model) {
		if (schoolLessonTime.getParent()!=null && StringUtils.isNotBlank(schoolLessonTime.getParent().getId())){
			schoolLessonTime.setParent(schoolLessonTimeService.get(schoolLessonTime.getParent().getId()));
			// 获取排序号，最末节点排序号+30
			if (StringUtils.isBlank(schoolLessonTime.getId())){
				SchoolLessonTime schoolLessonTimeChild = new SchoolLessonTime();
				schoolLessonTimeChild.setParent(new SchoolLessonTime(schoolLessonTime.getParent().getId()));
				List<SchoolLessonTime> list = schoolLessonTimeService.findList(schoolLessonTime); 
				if (list.size() > 0){
					schoolLessonTime.setSort(list.get(list.size()-1).getSort());
					if (schoolLessonTime.getSort() != null){
						schoolLessonTime.setSort(schoolLessonTime.getSort() + 30);
					}
				}
			}
		}
		if (schoolLessonTime.getSort() == null){
			schoolLessonTime.setSort(30);
		}
		model.addAttribute("schoolLessonTime", schoolLessonTime);
		return "modules/school/lessontime/schoolLessonTimeForm";
	}

	@RequiresPermissions("school:lessontime:schoolLessonTime:edit")
	@RequestMapping(value = "save")
	public String save(SchoolLessonTime schoolLessonTime, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, schoolLessonTime)){
			return form(schoolLessonTime, model);
		}
		schoolLessonTimeService.save(schoolLessonTime);
		addMessage(redirectAttributes, "保存上课时间表成功");
		return "redirect:"+Global.getAdminPath()+"/school/lessontime/schoolLessonTime/?repage";
	}
	
	@RequiresPermissions("school:lessontime:schoolLessonTime:edit")
	@RequestMapping(value = "delete")
	public String delete(SchoolLessonTime schoolLessonTime, RedirectAttributes redirectAttributes) {
		schoolLessonTimeService.delete(schoolLessonTime);
		addMessage(redirectAttributes, "删除上课时间表成功");
		return "redirect:"+Global.getAdminPath()+"/school/lessontime/schoolLessonTime/?repage";
	}

	@RequiresPermissions("user")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<SchoolLessonTime> list = schoolLessonTimeService.findList(new SchoolLessonTime());
		for (int i=0; i<list.size(); i++){
			SchoolLessonTime e = list.get(i);
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
	 * 获取每节课的上课时间接口
	 * @param 
	 * @return
	 */
	@RequiresPermissions("school:lessontime:schoolLessonTime:view")
	@RequestMapping(value = "getLessonTime",method=RequestMethod.GET)
	@ResponseBody
	public String getLessonTime() {
		List<SchoolLessonTime> list=schoolLessonTimeService.findList(new SchoolLessonTime());
		for(int i=list.size()-1;i>=0;i--){
			if(list.get(i).getParentId().equals("0"))
				list.remove(i);
		}
		Map<String, Object> map1 = Maps.newHashMap();
		map1.put("code", 1);
		map1.put("message", "success");
		map1.put("data", list);
		return 	JsonMapper.getInstance().toJson(map1);
	}
}