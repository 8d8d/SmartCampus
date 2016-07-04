/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.web.cardrecharge;

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
import com.thinkgem.jeesite.modules.school.entity.card.SchoolCard;
import com.thinkgem.jeesite.modules.school.entity.cardconsum.SchoolCardConsum;
import com.thinkgem.jeesite.modules.school.entity.cardrecharge.SchoolCardRecharge;
import com.thinkgem.jeesite.modules.school.service.card.SchoolCardService;
import com.thinkgem.jeesite.modules.school.service.cardrecharge.SchoolCardRechargeService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 一卡通充值记录Controller
 * @author 何伟杰
 * @version 2016-06-16
 */
@Controller
@RequestMapping(value = "${adminPath}/school/cardrecharge/schoolCardRecharge")
public class SchoolCardRechargeController extends BaseController {

	@Autowired
	private SchoolCardRechargeService schoolCardRechargeService;
	
	@Autowired
	private SchoolCardService schoolCardService;
	
	@ModelAttribute
	public SchoolCardRecharge get(@RequestParam(required=false) String id) {
		SchoolCardRecharge entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = schoolCardRechargeService.get(id);
		}
		if (entity == null){
			entity = new SchoolCardRecharge();
		}
		return entity;
	}
	
	@RequiresPermissions("school:cardrecharge:schoolCardRecharge:view")
	@RequestMapping(value = {"list", ""})
	public String list(SchoolCardRecharge schoolCardRecharge, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SchoolCardRecharge> page = schoolCardRechargeService.findPage(new Page<SchoolCardRecharge>(request, response), schoolCardRecharge); 
		model.addAttribute("page", page);
		return "modules/school/cardrecharge/schoolCardRechargeList";
	}

	@RequiresPermissions("school:cardrecharge:schoolCardRecharge:view")
	@RequestMapping(value = "form")
	public String form(SchoolCardRecharge schoolCardRecharge, Model model) {
		model.addAttribute("schoolCardRecharge", schoolCardRecharge);
		return "modules/school/cardrecharge/schoolCardRechargeForm";
	}

	@RequiresPermissions("school:cardrecharge:schoolCardRecharge:edit")
	@RequestMapping(value = "save")
	public String save(SchoolCardRecharge schoolCardRecharge, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, schoolCardRecharge)){
			return form(schoolCardRecharge, model);
		}
		if(schoolCardRecharge.getCardId()==null){
			addMessage(redirectAttributes, "保存失败，卡号不能为空");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		SchoolCard schoolCard=new SchoolCard();
		schoolCard.setCardId(schoolCardRecharge.getCardId());
		List<SchoolCard> list=schoolCardService.findList(schoolCard);
		SchoolCard schoolCard2=list.get(0);
		Float balance=Float.parseFloat(schoolCard2.getBalance());
		Float recharge=Float.parseFloat(schoolCardRecharge.getRechargeMoney());
		schoolCard2.setBalance(balance+recharge+"");
		schoolCardRecharge.setBalance(balance+recharge+"");
		schoolCardService.save(schoolCard2);
		schoolCardRechargeService.save(schoolCardRecharge);
		addMessage(redirectAttributes, "保存一卡通充值记录成功");

		return "redirect:"+Global.getAdminPath()+"/school/cardrecharge/schoolCardRecharge/?repage";
	}
	
	@RequiresPermissions("school:cardrecharge:schoolCardRecharge:edit")
	@RequestMapping(value = "delete")
	public String delete(SchoolCardRecharge schoolCardRecharge, RedirectAttributes redirectAttributes) {
		schoolCardRechargeService.delete(schoolCardRecharge);
		addMessage(redirectAttributes, "删除一卡通充值记录成功");
		return "redirect:"+Global.getAdminPath()+"/school/cardrecharge/schoolCardRecharge/?repage";
	}

	/**
	 * 获取一卡通最近一年充值记录数接口
	 * @param 
	 * @return
	 */
	@RequiresPermissions("school:cardrecharge:schoolCardRecharge:view")
	@RequestMapping(value = "getCardRecharge",method=RequestMethod.GET)
	@ResponseBody
	public String getCardRecharge(@RequestParam(required=true) int index,@RequestParam(required=true) int pageSize) {
		Map<String, Object> map1 = Maps.newHashMap();
		User user=UserUtils.getUser();
		SchoolCard schoolCard=new SchoolCard();
		schoolCard.setUser(user);
		List<SchoolCard> list=schoolCardService.findList(schoolCard);
		
		if(list==null||list.size()==0){
			map1.put("code", 1);
			map1.put("message", "success");
			map1.put("data",Maps.newHashMap());
		}
		else{
			SchoolCard schoolCard2=list.get(0);
			SchoolCardRecharge schoolCardRecharge=new SchoolCardRecharge();
			schoolCardRecharge.setCardId(schoolCard2.getCardId());
			
			Page<SchoolCardRecharge> page=new Page<SchoolCardRecharge>(index,pageSize);
			page.setOrderBy("a.update_date desc");
			List<SchoolCardRecharge> rechargeList=schoolCardRechargeService.findPage(page,schoolCardRecharge).getList();
			for(int i=rechargeList.size()-1;i>=0;i--){
				Date dNow = new Date(); //当前时间
				Date dBefore = new Date();
				Calendar calendar = Calendar.getInstance(); //得到日历
				calendar.setTime(dNow);//把当前时间赋给日历
				calendar.add(calendar.YEAR, -1); //设置为前一年
				dBefore = calendar.getTime(); //得到前一年的时间
				if(rechargeList.get(i).getUpdateDate().before(dBefore)){
					rechargeList.remove(i);
				}
			}
			map1.put("page", page.isLastPage()?0:page.getNext());
			map1.put("code", 1);
			map1.put("message", "success");
			map1.put("data",rechargeList);
		}
		return 	JsonMapper.getInstance().toJson(map1);
	}
}