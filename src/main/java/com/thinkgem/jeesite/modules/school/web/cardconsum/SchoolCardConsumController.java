/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.school.web.cardconsum;

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
import com.thinkgem.jeesite.modules.school.entity.news.SchoolNews;
import com.thinkgem.jeesite.modules.school.service.card.SchoolCardService;
import com.thinkgem.jeesite.modules.school.service.cardconsum.SchoolCardConsumService;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;

/**
 * 一卡通消费记录Controller
 * @author 何伟杰
 * @version 2016-06-16
 */
@Controller
@RequestMapping(value = "${adminPath}/school/cardconsum/schoolCardConsum")
public class SchoolCardConsumController extends BaseController {

	@Autowired
	private SchoolCardConsumService schoolCardConsumService;
	
	@Autowired
	private SchoolCardService schoolCardService;
	
	@ModelAttribute
	public SchoolCardConsum get(@RequestParam(required=false) String id) {
		SchoolCardConsum entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = schoolCardConsumService.get(id);
		}
		if (entity == null){
			entity = new SchoolCardConsum();
		}
		return entity;
	}
	
	@RequiresPermissions("school:cardconsum:schoolCardConsum:view")
	@RequestMapping(value = {"list", ""})
	public String list(SchoolCardConsum schoolCardConsum, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SchoolCardConsum> page = schoolCardConsumService.findPage(new Page<SchoolCardConsum>(request, response), schoolCardConsum); 
		model.addAttribute("page", page);
		return "modules/school/cardconsum/schoolCardConsumList";
	}

	@RequiresPermissions("school:cardconsum:schoolCardConsum:view")
	@RequestMapping(value = "form")
	public String form(SchoolCardConsum schoolCardConsum, Model model) {
		model.addAttribute("schoolCardConsum", schoolCardConsum);
		return "modules/school/cardconsum/schoolCardConsumForm";
	}

	@RequiresPermissions("school:cardconsum:schoolCardConsum:edit")
	@RequestMapping(value = "save")
	public String save(SchoolCardConsum schoolCardConsum, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, schoolCardConsum)){
			return form(schoolCardConsum, model);
		}
		if(schoolCardConsum.getCardId()==null){
			addMessage(redirectAttributes, "保存失败，卡号不能为空");
			return "redirect:"+Global.getAdminPath()+"/school/onclass/schoolOnclass/?repage";
		}
		SchoolCard schoolCard=new SchoolCard();
		schoolCard.setCardId(schoolCardConsum.getCardId());
		List<SchoolCard> list=schoolCardService.findList(schoolCard);
		SchoolCard schoolCard2=list.get(0);
		Float balance=Float.parseFloat(schoolCard2.getBalance());
		Float consum=Float.parseFloat(schoolCardConsum.getConsumMoney());
		if(balance<consum)
			addMessage(redirectAttributes, "消费金额"+consum+"元不能大于余额"+balance+"元");
		else{
			schoolCard2.setBalance(balance-consum+"");
			schoolCardConsum.setBalance(balance-consum+"");
			schoolCardService.save(schoolCard2);
			schoolCardConsumService.save(schoolCardConsum);
			addMessage(redirectAttributes, "保存一卡通消费记录成功");
		}
		return "redirect:"+Global.getAdminPath()+"/school/cardconsum/schoolCardConsum/?repage";
	}
	
	@RequiresPermissions("school:cardconsum:schoolCardConsum:edit")
	@RequestMapping(value = "delete")
	public String delete(SchoolCardConsum schoolCardConsum, RedirectAttributes redirectAttributes) {
		schoolCardConsumService.delete(schoolCardConsum);
		addMessage(redirectAttributes, "删除一卡通消费记录成功");
		return "redirect:"+Global.getAdminPath()+"/school/cardconsum/schoolCardConsum/?repage";
	}

	/**
	 * 获取一卡通余额，消费记录，充值记录数接口
	 * @param 
	 * @return
	 */
	@RequiresPermissions("school:cardconsum:schoolCardConsum:view")
	@RequestMapping(value = "getCardConsum",method=RequestMethod.GET)
	@ResponseBody
	public String getCardConsum(@RequestParam(required=true) int index,@RequestParam(required=true) int pageSize) {
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
			
			Page<SchoolCardConsum> page=new Page<SchoolCardConsum>(index,pageSize);
			page.setOrderBy("a.update_date desc");
			List<SchoolCardConsum> consumList=schoolCardConsumService.findPage(page,schoolCardConsum).getList();
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
			map1.put("page", page.isLastPage()?0:page.getNext());
			map1.put("code", 1);
			map1.put("message", "success");
			map1.put("data", consumList);
		}
		return 	JsonMapper.getInstance().toJson(map1);
	}
}