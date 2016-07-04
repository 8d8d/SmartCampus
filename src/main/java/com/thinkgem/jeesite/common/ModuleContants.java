package com.thinkgem.jeesite.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinkgem.jeesite.common.model.SMap;
import com.thinkgem.jeesite.modules.sys.entity.Area;

public class ModuleContants {

	//区分团购商铺 和 普通商品
	//1	团购商品  T_SHOPS 字段 status
	public static String SP_TG = "1";
	//0	普通商品
	public static String SP_PT = "0";
	
	//首页商铺推荐
	public static String HOME_SP = "1";
	//区分团购和优惠卷
	//1	团购  T_SHOPS 字段  C_LIB
	public static String YHJ = "0";
	//0	团购
	public static String TG = "1";
	//活动 T_NEWS 表 C_TYPE字段
	public static String HD_ID = "hd";
	
	//启用
	public static String QY = "1";
	//回收
	public static String HS = "255";
	//积分兑换
	public static String TALK_DUIHUAN = "duihuan";
	
	//商家默认
	public static String SJ_MR = "0";
	//商家用户发布待审核
	public static String SJ_DSH = "9";
	//商家已删
	public static String SJ_YS = "10";
	//商家联盟商家
	public static String SJ_LMSJ = "11";
	//商家已删
	public static String SJ_SQ = "12";
	
	//省
	public static String SHENG = "2";
	//市
	public static String SHI = "3";
	//县
	public static String XIAN = "4";
	//商圈
	public static String SHANGQ = "5";
	//首页团购
	public static String SY_TG = "listtg";
	//每日图购
	public static String MR_TG = "tgjx";
	//首页优惠卷
	public static String SY_YHJ = "listjuan";
	//每日优惠卷
	public static String MR_YHJ = "jx";
	
	//商品已删
	public static String TG_YS = "9";
	public static String  TG_JX = "tgjx";//热门商品
	public static String  TG_SY = "listtg";//商品精选
	//商品
	public static String TURNTOU_DINGDAN = "tuanding";
	//点评
	public static String DIANPING_PINGLUN = "dingping";
	//会员
	public static String MEMBER_STATUS = "member";
	//付款
	public static String TTX_STATUS = "ttx";
	
	
	//未认领商家
	public static String WRLRJ = "0";
	//已认领商家
	public static String YRLRJ = "1";
	//商品商家
	public static String TGRJ = "2"	;
	
	public static int SH_Y = 1;//已经审核
	public static int SH_W = 2;//未审核
	public static int SH_S = 3;//回收站商家\
	
	
	//等待成功
	public static String M_DDFK = "1";
	//正在付款
	public static String M_ZZFK = "2";
	//付款失败
	public static String M_FKSB = "3";
	//付款成功
	public static String M_FKCG = "4";
	//货到付款
	public static String M_HDFK = "9";
	
	//已经结算
	public static String DD_YJS = "1";
	//未结算
	public static String DD_WJS = "2";
	
	//提现商家
	public static String TX_SJ = "1";
	//提现个人
	public static String TX_GR = "2";
	
	//人员删除
	public static String RY_DEL = "9";
	//人员正常
	public static String RY_ZC = "0";
	
	//登录ADMIN
	public static String DL_ADMIN = "0"; 
	//登录商户
	public static String DL_SHOP = "1";
	public static String SP_YH = "1";
	//登录用户
	public static String DL_YH = "2";
	public static String PT_YH = "0";
	//登录购物
	public static String DL_GW = "3";
	//登录
	public static String LOGIN_FLAG = "1";
	//注册
	public static String REGIST_FLAG = "2";
	//密码修改
	public static String MODIFY_FLAG = "3";
	//忘记修改
	public static String FORGET_FLAG = "4";
	//商品订单
	//订单完成
	public static String TG_WC = "5";
	//显示
	public static String T_SHOW = "1";
	//隐藏
	public static String T_HIDE = "0";
	
	//使用优惠券
	public static String YHJ_SY = "1";
	
	
	/**
	 * 付款状态
	 * @return
	 */
	public static List getTtxStatusList(){
		List ttxStatusList = new ArrayList();
		ttxStatusList.add(new SMap("id","1","name","等待付款"));
		ttxStatusList.add(new SMap("id","2","name","正在付款"));
		ttxStatusList.add(new SMap("id","3","name","付款失败"));
		ttxStatusList.add(new SMap("id","4","name","付款成功"));
		ttxStatusList.add(new SMap("id","9","name","货到付款"));
		return ttxStatusList;
	}
	
	//商品
	public static String TURNTOU_DINGDAN_TGFH = "8";
	//团购货物状态
	public static List getHWStatusList(){
		List tuanStatusList = new ArrayList();
		tuanStatusList.add(new SMap("id","0","name","等待发货"));
		tuanStatusList.add(new SMap("id","3","name","已发货"));
		tuanStatusList.add(new SMap("id","6","name","未发货"));
		tuanStatusList.add(new SMap("id","9","name","确认收货"));
		tuanStatusList.add(new SMap("id","8","name","等待退货申请"));
		tuanStatusList.add(new SMap("id","7","name","退货成功"));
		tuanStatusList.add(new SMap("id","5","name","退货失败"));
		return tuanStatusList;
	}
	
	//团购付款状态
	public static List getFKStatusList(){
		List tuanStatusList = new ArrayList();
//		tuanStatusList.add(new SMap("id","0","name","未付款 "));
		tuanStatusList.add(new SMap("id","4","name","未付款"));
		tuanStatusList.add(new SMap("id","7","name","已付款"));
//		tuanStatusList.add(new SMap("id","5","name","订单已完成"));
//		tuanStatusList.add(new SMap("id","8","name","订单已删除"));
		return tuanStatusList;
	}
	
	//团购订单支付方式
	public static List getZFStatusList(){
		List tuanStatusList = new ArrayList();
		tuanStatusList.add(new SMap("id","1","name","货到付款"));
		tuanStatusList.add(new SMap("id","0","name","线上付款"));
		return tuanStatusList;
	}
	
	//团购退货状态
	public static List getTHStatusList(){
		List tuanStatusList = new ArrayList();
		tuanStatusList.add(new SMap("id","1","name","申请退货"));
		tuanStatusList.add(new SMap("id","2","name","退货成功"));
		tuanStatusList.add(new SMap("id","2","name","退货失败"));
		return tuanStatusList;
	}
	
	//团购用户状态
	public static List getTuanYHStatusList(){
		List tuanStatusList = new ArrayList();
		tuanStatusList.add(new SMap("id","0","name","未付款 "));
		tuanStatusList.add(new SMap("id","4","name","已下订单"));
		tuanStatusList.add(new SMap("id","2","name","等待发货"));
		tuanStatusList.add(new SMap("id","3","name","已发货"));
		tuanStatusList.add(new SMap("id","5","name","已完成"));
		tuanStatusList.add(new SMap("id","7","name","已付款"));
		return tuanStatusList;
	}
	
	//点评评论
	public static List getDianpingStatusList(){
		List dianpingStatusList = new ArrayList();
		dianpingStatusList.add(new SMap("id","0","name","未审核"));
		dianpingStatusList.add(new SMap("id","1","name","已审核"));
		dianpingStatusList.add(new SMap("id","2","name","精华"));
		dianpingStatusList.add(new SMap("id","9","name","已删除"));
		return dianpingStatusList;
	}
	
	//会员管理
	public static List getMemberStatusList(){
		List memberStatusList = new ArrayList();
		memberStatusList.add(new SMap("id","0","name","已发卡用户"));
		memberStatusList.add(new SMap("id","1","name","已认领商家"));
		memberStatusList.add(new SMap("id","2","name","商品商家"));
		memberStatusList.add(new SMap("id","9","name","已屏蔽"));
		return memberStatusList;
	}
	
	public static String getTalkListTitle(String blogId, Boolean isAdd){
		if("link".equals(blogId)){
			return "链接申请管理";
		}else if("bangding".equals(blogId)){
			return "申请认领管理";
		}else if("baoliao".equals(blogId)){
			return "网友优惠爆料";
		}else if("yijian".equals(blogId)){
			return "意见反馈管理";
		}else if("jb".equals(blogId)){
			return "举报信息管理";
		}else if("jiucuo".equals(blogId)){
			return "纠错信息管理";
		}else if("td".equals(blogId)){
			return "申请投递用户";
		}else if("fx".equals(blogId)){
			return "申请发行点";
		} 
		return "链接申请管理";
	}
	
	/**
	 * 折扣
	 * @return
	 */
	public static List getDiscountList(){
		List list = new ArrayList();
		for(int i = 1; i <= 100; i ++){
			Area m = new Area();
			m.setId(i+"");
			m.setName(i+"");
			list.add(m);
		}
		return list;
	}
	
	/**
	 * App调用之后操作成功的默认消息，默认显示success.
	 */
	public static final String APP_JSON_DEFAULT_SUCCESS_MSG = "success";

	/**
	 * App调用之后返回的JSON表示数据的Key，使用data来标识，通常在获取数据时该Key才会有对应的Value.
	 */
	public static final String APP_JSON_KEY_DATA = "data";

	/**
	 * App调用之后返回的JSON表示提示消息的Key，使用message来标识.
	 */
	public static final String APP_JSON_KEY_MESSAGE = "message";

	/**
	 * App调用之后返回的JSON表示成功操作的结果码.
	 */
	public static final int APP_RESULT_CODE_SUCCESS = 200;

	/**
	 * App调用之后返回的JSON表示异常操作的结果码.
	 */
	public static final int APP_RESULT_CODE_YC = 201;

	/**
	 * App调用之后返回的JSON表示未知异常操作的结果码.
	 */
	public static final int APP_RESULT_CODE_WZYC = 202;

	/**
	 * App调用之后返回的JSON表示参数异常的结果码.
	 */
	public static final int APP_RESULT_CODE_PARAMETER_INVALIDATE = 1;

	/**
	 * App调用之后返回的JSON表示经纪人未找到的结果码，经纪人相关业务的异常以500X来表示.
	 */
	public static final int APP_RESULT_CODE_BROKER_NOT_FOUND = 5001;

	/**
	 * App调用之后返回的JSON表示结果代号的Key，如果成功则使用0表示，失败则使用具体的代号.
	 */
	public static final String APP_JSON_KEY_RESULT_CODE = "errorCode";

	// 经纪人抢单，该单已经被人抢光
	public static final int ROB_ORDER_FAIL_RESULT_CODE = 5002;
}
