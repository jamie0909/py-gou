package com.pinyougou.user.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.mapper.TbOrderItemMapper;
import com.pinyougou.mapper.TbOrderMapper;
import com.pinyougou.mapper.TbPayLogMapper;
import com.pinyougou.pojo.*;
import com.pinyougou.pojo.TbPayLogExample.Criteria;
import com.pinyougou.user.service.PayLogService;
import entity.OrderEntity;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class PayLogServiceImpl implements PayLogService {

	@Autowired
	private TbPayLogMapper payLogMapper;

	@Autowired
	private TbOrderMapper orderMapper;


	/**
	 * 查询全部
	 */
	@Override
	public List<TbPayLog> findAll() {
		return payLogMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbPayLog> page=   (Page<TbPayLog>) payLogMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbPayLog payLog) {
		payLogMapper.insert(payLog);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbPayLog payLog){
		payLogMapper.updateByPrimaryKey(payLog);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbPayLog findOne(Long id){
		return payLogMapper.selectByPrimaryKey(id.toString());
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			payLogMapper.deleteByPrimaryKey(id.toString());
		}		
	}
	
	
		@Override
	public PageResult findPage(TbPayLog payLog, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbPayLogExample example=new TbPayLogExample();
		Criteria criteria = example.createCriteria();
		
		if(payLog!=null){			
						if(payLog.getOutTradeNo()!=null && payLog.getOutTradeNo().length()>0){
				criteria.andOutTradeNoLike("%"+payLog.getOutTradeNo()+"%");
			}
			if(payLog.getUserId()!=null && payLog.getUserId().length()>0){
				criteria.andUserIdLike("%"+payLog.getUserId()+"%");
			}
			if(payLog.getTransactionId()!=null && payLog.getTransactionId().length()>0){
				criteria.andTransactionIdLike("%"+payLog.getTransactionId()+"%");
			}
			if(payLog.getTradeState()!=null && payLog.getTradeState().length()>0){
				criteria.andTradeStateLike("%"+payLog.getTradeState()+"%");
			}
			if(payLog.getOrderList()!=null && payLog.getOrderList().length()>0){
				criteria.andOrderListLike("%"+payLog.getOrderList()+"%");
			}
			if(payLog.getPayType()!=null && payLog.getPayType().length()>0){
				criteria.andPayTypeLike("%"+payLog.getPayType()+"%");
			}
	
		}
		
		Page<TbPayLog> page= (Page<TbPayLog>)payLogMapper.selectByExample(example);
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Autowired
	private TbOrderItemMapper orderItemMapper;

	@Autowired
	private TbItemMapper itemMapper;

	/**
	 *
	 * @param pageNum
	 * @param pageSize
	 * @param status
	 *  status     1,待付款 2，待发货 3，待收货  4，待评价
	 * 数据库 状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭,7、待评价
	 * @return
	 */

	public Map findOrderListfromPayLog(int pageNum, int pageSize, String status, String userId){
		List<String> orderList = new ArrayList<>();

		Map map = new HashMap<>();
		List<OrderEntity> orderEntities = new ArrayList<>();
		PageHelper.startPage(pageNum,pageSize);
		TbPayLogExample example = new TbPayLogExample();
		Criteria criteria3 = example.createCriteria();
		if ("1".equals(status)){
			criteria3.andStatusEqualTo("1");
		}
		if ("2".equals(status)){
			List<String> list = new ArrayList<String>();
			list.add("2");
			list.add("3");
			criteria3.andStatusIn(list);
		}
		if ("3".equals(status)){
			criteria3.andStatusEqualTo("4");
		}
		if ("4".equals(status)){
			criteria3.andStatusEqualTo("7");
		}
		criteria3.andUserIdEqualTo(userId);
		Page<TbPayLog> page=   (Page<TbPayLog>) payLogMapper.selectByExample(example);
		List<TbPayLog> tbPayLogs = page.getResult();
		long totalNumbers = page.getTotal();
		long totalPages = totalNumbers%pageSize==0?totalNumbers/pageSize:totalNumbers/pageSize+1;
		map.put("totalPages",totalPages);
		map.put("currentPage",pageNum);
		for (TbPayLog tbPayLog : tbPayLogs) {
			String payLogOrderListIn = tbPayLog.getOrderList();
			orderList.add(payLogOrderListIn);
			String orderIdsFromPayLog = tbPayLog.getOrderList();
			if (orderIdsFromPayLog.contains(",")){
				OrderEntity orderEntity = new OrderEntity();
				List<TbOrder> orders = new ArrayList<>();
				String[] orderIdsFromPayLogList = orderIdsFromPayLog.split(", ");
				for (String orderIdSingle : orderIdsFromPayLogList) {

					//订单（一个paylog对应多个订单）
					TbOrder orders1 = orderMapper.selectByPrimaryKey(Long.parseLong(orderIdSingle));
					orders.add(orders1);

					//orderItems
					TbOrderItemExample orderItemExample = new TbOrderItemExample();
					TbOrderItemExample.Criteria criteria1 = orderItemExample.createCriteria();
					criteria1.andOrderIdEqualTo(Long.parseLong(orderIdSingle));
					List<TbOrderItem> orderItems = orderItemMapper.selectByExample(orderItemExample);
					orders1.setOrderItems(orderItems);

					//spec
					for (TbOrderItem orderItem : orderItems) {
						TbItem tbItem = itemMapper.selectByPrimaryKey(orderItem.getItemId());
						orderItem.setSpec(tbItem.getSpec());
					}
				}
				orderEntity.setOrderList(orders);
				orderEntity.setPayLog(tbPayLog);
				orderEntities.add(orderEntity);
			}else{

				OrderEntity orderEntity = new OrderEntity();
				List<TbOrder> orders = new ArrayList<>();
				TbOrder order = orderMapper.selectByPrimaryKey(Long.parseLong(orderIdsFromPayLog));

				//orderItems
				TbOrderItemExample orderItemExample = new TbOrderItemExample();
				TbOrderItemExample.Criteria criteria1 = orderItemExample.createCriteria();
				criteria1.andOrderIdEqualTo(Long.parseLong(orderIdsFromPayLog));
				List<TbOrderItem> orderItems = orderItemMapper.selectByExample(orderItemExample);
				order.setOrderItems(orderItems);
				orders.add(order);

				//spec
				for (TbOrderItem orderItem : orderItems) {
					TbItem tbItem = itemMapper.selectByPrimaryKey(orderItem.getItemId());
					orderItem.setSpec(tbItem.getSpec());
				}
				orderEntity.setOrderList(orders);
				orderEntity.setPayLog(tbPayLog);
				orderEntities.add(orderEntity);
			}
		}

		map.put("orderEntityList",orderEntities);
		return map;
	}

	@Override
	public OrderEntity findOrderListByOutTradeNo(String outTradeNo) {
		System.out.println("outTradeNo"+outTradeNo);
		TbPayLog tbPayLog = payLogMapper.selectByPrimaryKey(outTradeNo);
		String orderIdsFromPayLog = tbPayLog.getOrderList();
		OrderEntity orderEntity = new OrderEntity();
		if (orderIdsFromPayLog.contains(",")){
			List<TbOrder> orders = new ArrayList<>();
			String[] orderIdsFromPayLogList = orderIdsFromPayLog.split(", ");
			for (String orderIdSingle : orderIdsFromPayLogList) {

				//订单（一个paylog对应多个订单）
				TbOrderExample example2 = new TbOrderExample();
				TbOrderExample.Criteria criteria = example2.createCriteria();
				criteria.andOrderIdEqualTo(Long.valueOf(orderIdSingle));
				List<TbOrder> orders1 = orderMapper.selectByExample(example2);
				orders.add(orders1.get(0));
				//orderItems
				TbOrderItemExample orderItemExample = new TbOrderItemExample();
				TbOrderItemExample.Criteria criteria1 = orderItemExample.createCriteria();
				criteria1.andOrderIdEqualTo(Long.parseLong(orderIdSingle));
				List<TbOrderItem> orderItems = orderItemMapper.selectByExample(orderItemExample);
				orders1.get(0).setOrderItems(orderItems);

				//spec
				for (TbOrderItem orderItem : orderItems) {
					TbItemExample itemExample = new TbItemExample();
					TbItemExample.Criteria criteria2 = itemExample.createCriteria();
					criteria2.andIdEqualTo(orderItem.getItemId());
					List<TbItem> tbItems = itemMapper.selectByExample(itemExample);
					if (tbItems != null && tbItems.size() > 0){

						orderItem.setSpec(tbItems.get(0).getSpec());
					}
				}


			}
			orderEntity.setOrderList(orders);
			orderEntity.setPayLog(tbPayLog);
		}else{
			TbOrderExample example3 = new TbOrderExample();
			TbOrderExample.Criteria criteria = example3.createCriteria();
			criteria.andOrderIdEqualTo(Long.parseLong(orderIdsFromPayLog));
			List<TbOrder> orders1 = orderMapper.selectByExample(example3);

			//orderItems
			TbOrderItemExample orderItemExample = new TbOrderItemExample();
			TbOrderItemExample.Criteria criteria1 = orderItemExample.createCriteria();
			criteria1.andOrderIdEqualTo(Long.parseLong(orderIdsFromPayLog));
			List<TbOrderItem> orderItems = orderItemMapper.selectByExample(orderItemExample);
			orders1.get(0).setOrderItems(orderItems);

			//spec
			for (TbOrderItem orderItem : orderItems) {
				TbItemExample itemExample = new TbItemExample();
				TbItemExample.Criteria criteria2 = itemExample.createCriteria();
				criteria2.andIdEqualTo(orderItem.getItemId());
				List<TbItem> tbItems = itemMapper.selectByExample(itemExample);
				if (tbItems != null && tbItems.size() > 0){

					System.out.println("=================");
					orderItem.setSpec(tbItems.get(0).getSpec());
					System.out.println(tbItems.get(0).getSpec());
				}

			}
			orderEntity.setOrderList(orders1);
			orderEntity.setPayLog(tbPayLog);
			System.out.println("payLog的totalFee"+tbPayLog.getTotalFee());
		}
		return orderEntity;
	}




}
