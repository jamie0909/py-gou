package com.pinyougou.cart.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.pinyougou.pay.service.AliPayService;
import com.pinyougou.user.service.PayLogService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pay.service.WeixinPayService;
import com.pinyougou.pojo.TbPayLog;

import entity.Result;
import util.IdWorker;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/pay")
public class PayController {
	
	@Reference
	private WeixinPayService weixinPayService;

	@Reference
	private AliPayService aliPayService;

	@Reference
	private PayLogService payLogService;


	@Reference
	private OrderService orderService;
	
	@RequestMapping("/createNative")
	public Map createNative(String out_trade_no){
		if (out_trade_no!=null){
			String totalFeeByOutTradeNo = payLogService.findTotalFeeByOutTradeNo(out_trade_no);
			return weixinPayService.createNative(out_trade_no,totalFeeByOutTradeNo);
		}

		//1.获取当前登录用户
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		//2.提取支付日志（从缓存 ）
		TbPayLog payLog = orderService.searchPayLogFromRedis(username);

		//3.调用微信支付接口
		if(payLog!=null){
			return weixinPayService.createNative(payLog.getOutTradeNo(), payLog.getTotalFee()+"");		
		}else{
			return new HashMap<>();
		}		
	}

	@RequestMapping("/createAliPayNative")
	public Map createAliPayNative(String out_trade_no){
		if (out_trade_no!=null){
			String totalFeeByOutTradeNo = payLogService.findTotalFeeByOutTradeNo(out_trade_no);
			return weixinPayService.createNative(out_trade_no,totalFeeByOutTradeNo);
		}
		//1.获取当前登录用户
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		//2.提取支付日志（从缓存 ）
		TbPayLog payLog = orderService.searchPayLogFromRedis(username);

		//3.调用微信支付接口
		//System.out.println(payLog);
		if(payLog!=null){
			return aliPayService.createNative(payLog.getOutTradeNo(), payLog.getTotalFee()+"");
		}else{
			return new HashMap<>();
		}
	}

	
	@RequestMapping("/queryPayStatus")
	public Result queryPayStatus(String out_trade_no,String paymentType){
		Result result=null;
		int x=0;
		while(true){
			Map<String,String> map=null;
			if ("1".equals(paymentType)){
				 map = weixinPayService.queryPayStatus(out_trade_no);//微信调用查询
				System.out.println("paymentType=1,微信查询支付状态成功!");
			}
			if ("3".equals(paymentType)) {
				 map = aliPayService.queryPayStatus(out_trade_no);//支付宝调用查询
				System.out.println("paymentType=3,支付宝查询支付状态成功!");
			}
			System.out.println("map:"+map);
			if(map==null){
				result=new Result(false, "支付发生错误");
				break;
			}
			if(("SUCCESS").equals(map.get("trade_state"))||("TRADE_SUCCESS").equals(map.get("trade_state"))){//支付成功
				result=new Result(true, "支付成功");				
				orderService.updateOrderStatus(out_trade_no, map.get("transaction_id"));//修改订单状态
				break;
			}
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			x++;
			if(x>=100){				
				result=new Result(false, "二维码超时");
				break;				
			}
			
		}
		return result;
	}

	//支付宝回调地址:http://jamie.natapp1.cc/pay/alipayCallBack.do

	@RequestMapping("/alipayCallBack")
	public String alipayCallBack(HttpServletRequest request) {
		System.out.println("===================");
		System.out.println(request.getParameterMap());
		Map<String,String> params = new HashMap();
		Map requestParams = request.getParameterMap();
		for(Iterator iter = requestParams.keySet().iterator(); iter.hasNext();){
			String name = (String)iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for(int i = 0 ; i <values.length;i++){

				valueStr = (i == values.length -1)?valueStr + values[i]:valueStr + values[i]+",";
			}
			params.put(name,valueStr);
		}
		if("TRADE_SUCCESS".equals(params.get("trade_status"))) {
			orderService.updateOrderStatus(params.get("out_trade_no")+"", params.get("trade_no"));//修改订单状态
		}
		return "success";
	}
}
