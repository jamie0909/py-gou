package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.order.service.OrderItemService;
import com.pinyougou.order.service.OrderItemServiceHu;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.order.service.OrderServiceHu;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbOrder;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.sellergoods.service.ItemServiceHu;
import com.pinyougou.user.service.PayLogService;
import entity.OrderEntity;
import entity.PageResult;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/order")
public class OrderController {

	@Reference(timeout = 60000)
	private OrderServiceHu orderService;

	@Reference(timeout = 60000)
	private OrderItemServiceHu orderItemService;

	@Reference(timeout = 60000)
	private ItemServiceHu itemService;

	@Reference(timeout = 60000)
	private PayLogService payLogService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbOrder> findAll(){
		return orderService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public Map findPage(int page, int rows, String status){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Map map = new HashMap();

		List<OrderEntity> orderEntityList = new ArrayList<>();
		PageResult page1 = orderService.findPage(page, rows, userId,status);

		map.put("currentPage",page);
		map.put("total",page1.getTotal());
		map.put("totalPages",page1.getTotal()%rows==0?page1.getTotal()/rows:page1.getTotal()/rows+1);

		List<TbOrder> orderList = page1.getRows();
		for (TbOrder order : orderList) {
			OrderEntity orderEntity = new OrderEntity();
			orderEntity.setOrder(order);
			List<TbOrderItem> orderItems = orderItemService.findOrderItemByOrderId(order.getOrderId());
			for (TbOrderItem orderItem : orderItems) {
				TbItem one = itemService.findOne(orderItem.getItemId());
				orderItem.setSpec(one.getSpec());
			}
			orderEntity.setOrderItem(orderItems);
			orderEntityList.add(orderEntity);
		}
		map.put("orderEntityList",orderEntityList);

		return map;
	}
	
	/**
	 * 增加
	 * @param order
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbOrder order){
		try {
			orderService.add(order);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param order
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbOrder order){
		try {
			orderService.update(order);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public TbOrder findOne(Long id){
		System.out.println(id);
		return orderService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long[] ids){
		try {
			orderService.delete(ids);
			return new Result(true, "删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbOrder order, int page, int rows  ){
		return orderService.findPage(order, page, rows);		
	}
	@RequestMapping("/findOrderItemsByOrderId")
	public List<TbOrderItem> findOrderItemsByOrderId(Long orderId  ){
		return orderItemService.findOrderItemByOrderId(orderId);
	}

	@RequestMapping("/findOrderListfromPayLog")
	public Map findOrderListfromPayLog(int pageNum, int pageSize, String status){
		String userId = SecurityContextHolder.getContext().getAuthentication().getName();
		return payLogService.findOrderListfromPayLog(pageNum,pageSize,status,userId);
	}



	@RequestMapping("/updateStatus")
	public Result updateStatus(String out_order_no, String status){
		try {
			orderService.updateStatus(out_order_no,status);
			return new Result(true,"成功！");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"失败了，请重试！");
		}

	}

	@RequestMapping("/findOrderListByOutOrderNo")
	public OrderEntity findOrderListByOutOrderNo(String outTradeNo){
		OrderEntity orderListByOutTradeNo = payLogService.findOrderListByOutTradeNo(outTradeNo);
		return orderListByOutTradeNo;
	}

	
}
