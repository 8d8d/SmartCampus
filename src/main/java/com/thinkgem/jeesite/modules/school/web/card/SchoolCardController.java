/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.web.card;

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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.mapper.JsonMapper;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.common.utils.tran.TransLateCondition;
import com.thinkgem.jeesite.common.utils.tran.TranslateUtil;
import com.thinkgem.jeesite.modules.school.entity.calendar.SchoolCalendar;
import com.thinkgem.jeesite.modules.school.entity.card.SchoolCard;
import com.thinkgem.jeesite.modules.school.entity.cardconsum.SchoolCardConsum;
import com.thinkgem.jeesite.modules.school.entity.cardrecharge.SchoolCardRecharge;
import com.thinkgem.jeesite.modules.school.entity.ctable.SchoolCtable;
import com.thinkgem.jeesite.modules.school.entity.exam.SchoolExam;
import com.thinkgem.jeesite.modules.school.service.card.SchoolCardService;
import com.thinkgem.jeesite.modules.school.service.cardconsum.SchoolCardConsumService;
import com.thinkgem.jeesite.modules.school.service.cardrecharge.SchoolCardRechargeService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 一卡通信息Controller
 * @author 何伟杰
 * @version 2016-06-16
 */
@Controller
@RequestMapping(value = "${adminPath}/school/card/schoolCard")
public class SchoolCardController extends BaseController {

	@Autowired
	private SchoolCardService schoolCardService;
	
	@Autowired
	private SchoolCardRechargeService schoolCardRechargeService;
	
	@Autowired
	private SchoolCardConsumService schoolCardConsumService;
	
	@ModelAttribute
	public SchoolCard get(@RequestParam(required=false) String id) {
		SchoolCard entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = schoolCardService.get(id);
		}
		if (entity == null){
			entity = new SchoolCard();
		}
		return entity;
	}
	
	@RequiresPermissions("school:card:schoolCard:view")
	@RequestMapping(value = {"list", ""})
	public String list(SchoolCard schoolCard, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SchoolCard> page = schoolCardService.findPage(new Page<SchoolCard>(request, response), schoolCard); 
		List<SchoolCard> list = page.getList();
		User user = UserUtils.getUser();
		if (UserUtils.getRoleList().get(0).getEnname().equals("teacher"))
			for (int i = list.size() - 1; i >= 0; i--) {
				if (!user.getId().equals(list.get(i).getUser().getId()))
					list.remove(i);
			}
		model.addAttribute("page", page);
		return "modules/school/card/schoolCardList";
	}

	@RequiresPermissions("school:card:schoolCard:view")
	@RequestMapping(value = "form")
	public String form(SchoolCard schoolCard, Model model) {
		model.addAttribute("schoolCard", schoolCard);
		return "modules/school/card/schoolCardForm";
	}

	@RequiresPermissions("school:card:schoolCard:edit")
	@RequestMapping(value = "save")
	public String save(SchoolCard schoolCard, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, schoolCard)){
			return form(schoolCard, model);
		}
		if(schoolCard.getUser()==null){
			addMessage(redirectAttributes, "保存失败，学生不能为空");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		schoolCardService.save(schoolCard);
		addMessage(redirectAttributes, "保存一卡通信息设置成功");
		return "redirect:"+Global.getAdminPath()+"/school/card/schoolCard/?repage";
	}
	
	@RequiresPermissions("school:card:schoolCard:edit")
	@RequestMapping(value = "delete")
	public String delete(SchoolCard schoolCard, RedirectAttributes redirectAttributes) {
		schoolCardService.delete(schoolCard);
		addMessage(redirectAttributes, "删除一卡通信息设置成功");
		return "redirect:"+Global.getAdminPath()+"/school/card/schoolCard/?repage";
	}

	
	/**
	 * 获取所有卡号
	 * @param officeId
	 * @param response
	 * @return
	 */
	@RequiresPermissions("school:card:schoolCard:view")
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
	
		List<SchoolCard> list=schoolCardService.findList(new SchoolCard());
		for (SchoolCard e : list) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getCardId());
			map.put("pId", 0);
			map.put("name",e.getCardId());
			mapList.add(map);			
		}
		return mapList;
	}
	
	/**
	 * 获取一卡通余额，消费记录，充值记录数接口
	 * @param 
	 * @return
	 */
	@RequiresPermissions("school:card:schoolCard:view")
	@RequestMapping(value = "getCard",method=RequestMethod.GET)
	@ResponseBody
	public String getCard() {
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
			
			Map<String, Object> map2 = Maps.newHashMap();
			map2.put("balance", schoolCard2.getBalance());
			SchoolCardConsum schoolCardConsum=new SchoolCardConsum();
			schoolCardConsum.setCardId(schoolCard2.getCardId());
			List<SchoolCardConsum> consumList=schoolCardConsumService.findList(new SchoolCardConsum());
			for(int i=consumList.size()-1;i>=0;i--){
				Date dNow = new Date(); //当前时间
				Date dBefore = new Date();
				Calendar calendar = Calendar.getInstance(); //得到日历
				calendar.setTime(dNow);//把当前时间赋给日历
				calendar.add(calendar.MONTH, -3); //设置为前3月
				dBefore = calendar.getTime(); //得到前3月的时间
				if(consumList.get(i).getUpdateDate().before(dBefore)){
					consumList.remove(i);
				}
			}
			map2.put("consum", consumList.size());
			
			SchoolCardRecharge schoolCardRecharge=new SchoolCardRecharge();
			schoolCardRecharge.setCardId(schoolCard2.getCardId());
			List<SchoolCardRecharge> rechargeList=schoolCardRechargeService.findList(new SchoolCardRecharge());
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
			map2.put("reharge", rechargeList.size());
			map1.put("code", 1);
			map1.put("message", "success");
			map1.put("data", map2);
		}
		return 	JsonMapper.getInstance().toJson(map1);
	}
	
	/**
	 * 获取一卡通余额，消费记录，充值记录数接口
	 * @param 
	 * @return
	 */
	@RequiresPermissions("school:card:schoolCard:view")
	@RequestMapping(value = "getCardInfo",method=RequestMethod.GET)
	@ResponseBody
	public String getCardInfo() {
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
			
			Map<String, Object> map2 = Maps.newHashMap();
			map2.put("cardId", schoolCard2.getCardId());
			map2.put("balance", schoolCard2.getBalance());
			map2.put("state", schoolCard2.getState());
			map2.put("name", user.getName());
			map2.put("remark", schoolCard2.getRemarks());
			if(!StringUtils.isEmpty(user.getSex()))
				map2.put("sex",user.getSex().equals("0")?"男":"女");
			else
				map2.put("sex","");
			
			map1.put("code", 1);
			map1.put("message", "success");
			map1.put("data", map2);
		}
		return 	JsonMapper.getInstance().toJson(map1);
	}
}