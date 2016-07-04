/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.web.leaderclass;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.tran.TransLateCondition;
import com.thinkgem.jeesite.common.utils.tran.TranslateUtil;
import com.thinkgem.jeesite.modules.school.entity.leaderclass.SchoolLeaderClass;
import com.thinkgem.jeesite.modules.school.service.leaderclass.SchoolLeaderClassService;
import com.thinkgem.jeesite.modules.sys.entity.Office;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.service.OfficeService;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 辅导员任教Controller
 * @author 王超然
 * @version 2016-05-31
 */
@Controller
@RequestMapping(value = "${adminPath}/school/leaderclass/schoolLeaderClass")
public class SchoolLeaderClassController extends BaseController {

	@Autowired
	private SchoolLeaderClassService schoolLeaderClassService;
	
	@Autowired
	private OfficeService officeService;
	
	@ModelAttribute
	public SchoolLeaderClass get(@RequestParam(required=false) String id) {
		SchoolLeaderClass entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = schoolLeaderClassService.get(id);
		}
		if (entity == null){
			entity = new SchoolLeaderClass();
		}
		return entity;
	}
	
	@RequiresPermissions("school:leaderclass:schoolLeaderClass:view")
	@RequestMapping(value = {"list", ""})
	public String list(SchoolLeaderClass schoolLeaderClass, HttpServletRequest request, HttpServletResponse response, Model model) {
		System.out.println("权限过滤语句：" + UserUtils.getLC(UserUtils.getUser()));
		Page<SchoolLeaderClass> page = schoolLeaderClassService.findPage(new Page<SchoolLeaderClass>(request, response), schoolLeaderClass); 
		List list=page.getList();
		list = TranslateUtil.t.translateList(list,new TransLateCondition("leaderId","sys_user a","a.name as leaderId","id",""));
		list = TranslateUtil.t.translateList(list,new TransLateCondition("classId","sys_office a","a.name as classId","id",""));
		model.addAttribute("page", page);
		return "modules/school/leaderclass/schoolLeaderClassList";
	}

	@RequiresPermissions("school:leaderclass:schoolLeaderClass:view")
	@RequestMapping(value = "form")
	public String form(SchoolLeaderClass schoolLeaderClass, Model model) {
		if (schoolLeaderClass.getId() != null) {
			User user1 = UserUtils.get(schoolLeaderClass.getLeaderId());
			model.addAttribute("leader",user1.getName() );
			
			Office office=officeService.get(schoolLeaderClass.getClassId());
			model.addAttribute("className",office.getName());
		}
		model.addAttribute("schoolLeaderClass", schoolLeaderClass);
		return "modules/school/leaderclass/schoolLeaderClassForm";
	}

	@RequiresPermissions("school:leaderclass:schoolLeaderClass:edit")
	@RequestMapping(value = "save")
	public String save(SchoolLeaderClass schoolLeaderClass, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, schoolLeaderClass)){
			return form(schoolLeaderClass, model);
		}
		if(schoolLeaderClass.getLeaderId()==null){
			addMessage(redirectAttributes, "保存失败，辅导员不能为空");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		
		if(schoolLeaderClass.getClassId()==null){
			addMessage(redirectAttributes, "保存失败，班级不能为空");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		schoolLeaderClassService.save(schoolLeaderClass);
		addMessage(redirectAttributes, "保存辅导员任教成功");
		return "redirect:"+Global.getAdminPath()+"/school/leaderclass/schoolLeaderClass/?repage";
	}
	
	@RequiresPermissions("school:leaderclass:schoolLeaderClass:edit")
	@RequestMapping(value = "delete")
	public String delete(SchoolLeaderClass schoolLeaderClass, RedirectAttributes redirectAttributes) {
		schoolLeaderClassService.delete(schoolLeaderClass);
		addMessage(redirectAttributes, "删除辅导员任教成功");
		return "redirect:"+Global.getAdminPath()+"/school/leaderclass/schoolLeaderClass/?repage";
	}

}