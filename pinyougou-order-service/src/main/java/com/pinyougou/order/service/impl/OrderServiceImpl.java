package com.pinyougou.order.service.impl;


import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import com.pinyougou.mapper.TbSalesreturnMapper;
import com.pinyougou.pojo.*;
import com.pinyougou.pojo.group.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbOrderItemMapper;
import com.pinyougou.mapper.TbOrderMapper;
import com.pinyougou.mapper.TbPayLogMapper;
import com.pinyougou.pojo.TbOrderExample.Criteria;
import com.pinyougou.pojo.group.Cart;
import com.pinyougou.order.service.OrderService;

import entity.PageResult;
import util.ExcelOperateUtil;
import util.IdWorker;



/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service(timeout = 1200000)
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private TbOrderMapper orderMapper;
	
	@Autowired
	private TbPayLogMapper payLogMapper;

	@Autowired
    private TbSalesreturnMapper salesreturnMapper;


	/**
	 * 查询全部
	 */
	@Override
	public List<TbOrder> findAll() {
		return orderMapper.selectByExample(null);
	}

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbOrder> page = (Page<TbOrder>) orderMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private TbOrderItemMapper orderItemMapper;

    /**
     * 增加
     */
    @Override
    public void add(TbOrder order) {

        //1.从redis中提取购物车列表
        List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(order.getUserId());

        List<String> orderIdList = new ArrayList();//订单ID集合
        double total_money = 0;//总金额
        //2.循环购物车列表添加订单
        for (Cart cart : cartList) {
            TbOrder tbOrder = new TbOrder();
            long orderId = idWorker.nextId();    //获取ID
            tbOrder.setOrderId(orderId);
            tbOrder.setPaymentType(order.getPaymentType());//支付类型
            tbOrder.setStatus("1");//未付款
            tbOrder.setCreateTime(new Date());//下单时间
            tbOrder.setUpdateTime(new Date());//更新时间
            tbOrder.setUserId(order.getUserId());//当前用户
            tbOrder.setReceiverAreaName(order.getReceiverAreaName());//收货人地址
            tbOrder.setReceiverMobile(order.getReceiverMobile());//收货人电话
            tbOrder.setReceiver(order.getReceiver());//收货人
            tbOrder.setSourceType(order.getSourceType());//订单来源
            tbOrder.setSellerId(order.getSellerId());//商家ID

            double money = 0;//合计数
            //循环购物车中每条明细记录
            for (TbOrderItem orderItem : cart.getOrderItemList()) {
                orderItem.setId(idWorker.nextId());//主键
                orderItem.setOrderId(orderId);//订单编号
                orderItem.setSellerId(cart.getSellerId());//商家ID
                orderItemMapper.insert(orderItem);
                money += orderItem.getTotalFee().doubleValue();
            }

            tbOrder.setPayment(new BigDecimal(money));//合计


            orderMapper.insert(tbOrder);

            orderIdList.add(orderId + "");
            total_money += money;
        }

        //添加支付日志
        if ("1".equals(order.getPaymentType())) {
            TbPayLog payLog = new TbPayLog();

            payLog.setOutTradeNo(idWorker.nextId() + "");//支付订单号
            payLog.setCreateTime(new Date());
            payLog.setUserId(order.getUserId());//用户ID
            payLog.setOrderList(orderIdList.toString().replace("[", "").replace("]", ""));//订单ID串
            payLog.setTotalFee((long) (total_money * 100));//金额（分）
            payLog.setTradeState("0");//交易状态
            payLog.setPayType("1");//微信
            payLogMapper.insert(payLog);

            redisTemplate.boundHashOps("payLog").put(order.getUserId(), payLog);//放入缓存
        }

		if ("3".equals(order.getPaymentType())) {
			TbPayLog payLog = new TbPayLog();

			payLog.setOutTradeNo(idWorker.nextId() + "");//支付订单号
			payLog.setCreateTime(new Date());
			payLog.setUserId(order.getUserId());//用户ID
			payLog.setOrderList(orderIdList.toString().replace("[", "").replace("]", ""));//订单ID串
			payLog.setTotalFee((long) (total_money * 100));//金额（分）
			payLog.setTradeState("0");//交易状态
			payLog.setPayType("3");//支付宝
			payLogMapper.insert(payLog);

			redisTemplate.boundHashOps("payLog").put(order.getUserId(), payLog);//放入缓存
		}

        //3.清除redis中的购物车
        redisTemplate.boundHashOps("cartList").delete(order.getUserId());
    }

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbOrder order){
		orderMapper.updateByPrimaryKey(order);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public Order findOne(Long id){
		Order order=new Order();
		order.setTbOrder(orderMapper.selectByPrimaryKey(id));
		order.setTbOrderItem(orderItemMapper.selectByPrimaryKey(id));

		return order;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			orderMapper.deleteByPrimaryKey(id);
		}
	}
	
	
		@Override
	public PageResult findPage(TbOrder order, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbOrderExample example=new TbOrderExample();
		Criteria criteria = example.createCriteria();
		
		if(order!=null){			
						if(order.getPaymentType()!=null && order.getPaymentType().length()>0){
				criteria.andPaymentTypeLike("%"+order.getPaymentType()+"%");
			}
			if(order.getPostFee()!=null && order.getPostFee().length()>0){
				criteria.andPostFeeLike("%"+order.getPostFee()+"%");
			}
			if(order.getStatus()!=null && order.getStatus().length()>0){
				criteria.andStatusLike("%"+order.getStatus()+"%");
			}
			if(order.getShippingName()!=null && order.getShippingName().length()>0){
				criteria.andShippingNameLike("%"+order.getShippingName()+"%");
			}
			if(order.getShippingCode()!=null && order.getShippingCode().length()>0){
				criteria.andShippingCodeLike("%"+order.getShippingCode()+"%");
			}
			if(order.getUserId()!=null && order.getUserId().length()>0){
				criteria.andUserIdLike("%"+order.getUserId()+"%");
			}
			if(order.getBuyerMessage()!=null && order.getBuyerMessage().length()>0){
				criteria.andBuyerMessageLike("%"+order.getBuyerMessage()+"%");
			}
			if(order.getBuyerNick()!=null && order.getBuyerNick().length()>0){
				criteria.andBuyerNickLike("%"+order.getBuyerNick()+"%");
			}
			if(order.getBuyerRate()!=null && order.getBuyerRate().length()>0){
				criteria.andBuyerRateLike("%"+order.getBuyerRate()+"%");
			}
			if(order.getReceiverAreaName()!=null && order.getReceiverAreaName().length()>0){
				criteria.andReceiverAreaNameLike("%"+order.getReceiverAreaName()+"%");
			}
			if(order.getReceiverMobile()!=null && order.getReceiverMobile().length()>0){
				criteria.andReceiverMobileLike("%"+order.getReceiverMobile()+"%");
			}
			if(order.getReceiverZipCode()!=null && order.getReceiverZipCode().length()>0){
				criteria.andReceiverZipCodeLike("%"+order.getReceiverZipCode()+"%");
			}
			if(order.getReceiver()!=null && order.getReceiver().length()>0){
				criteria.andReceiverLike("%"+order.getReceiver()+"%");
			}
			if(order.getInvoiceType()!=null && order.getInvoiceType().length()>0){
				criteria.andInvoiceTypeLike("%"+order.getInvoiceType()+"%");
			}
			if(order.getSourceType()!=null && order.getSourceType().length()>0){
				criteria.andSourceTypeLike("%"+order.getSourceType()+"%");
			}
			if(order.getSellerId()!=null && order.getSellerId().length()>0){
				criteria.andSellerIdLike("%"+order.getSellerId()+"%");
			}
	
		}
		
		Page<TbOrder> page= (Page<TbOrder>)orderMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}


    @Override
    public TbPayLog searchPayLogFromRedis(String userId) {
        return (TbPayLog) redisTemplate.boundHashOps("payLog").get(userId);
    }

	@Override
	public void updateOrderStatus(String out_trade_no, String transaction_id) {
		//1.修改支付日志的状态及相关字段
		TbPayLog payLog = payLogMapper.selectByPrimaryKey(out_trade_no);
		payLog.setPayTime(new Date());//支付时间
		payLog.setTradeState("1");//交易成功
		payLog.setStatus("2");
		payLog.setTransactionId(transaction_id);//微信的交易流水号
		
		payLogMapper.updateByPrimaryKey(payLog);//修改
		//2.修改订单表的状态
		String orderList = payLog.getOrderList();// 订单ID 串
		String[] orderIds = orderList.split(",");
		
		for(String orderId:orderIds){
			TbOrder order = orderMapper.selectByPrimaryKey(Long.valueOf(orderId));
			order.setStatus("2");//已付款状态
			order.setPaymentTime(new Date());//支付时间
			orderMapper.updateByPrimaryKey(order);			
		}
		
		//3.清除缓存中的payLog
		redisTemplate.boundHashOps("payLog").delete(payLog.getUserId());
		
	}

	@Override
	public void deleteOne(Long id) {
		orderMapper.deleteByPrimaryKey(id);
	}


	@Override
	public PageResult findByPage(TbOrder order, int pageNum, int pageSize) {

		// 使用分页插件:
		PageHelper.startPage(pageNum, pageSize);
		// 进行条件查询:
		TbOrderExample example = new TbOrderExample();
		Criteria criteria = example.createCriteria();
		// 设置条件:
		if(order!=null) {
			//通过电话或者收件人查询
			if (order.getReceiver()!= null && order.getReceiver().length()>0) {

				try {
					new BigInteger(order.getReceiver());//能安全转换  说明纯数字

					criteria.andReceiverMobileEqualTo(order.getReceiver());
				} catch (NumberFormatException e) {//报错说明不是纯数字

					criteria.andReceiverLike("%" + order.getReceiver() + "%");
				}

			}

			//通过订单编号查询
			if (order.getOrderId()!= null && !"".equals(order.getOrderId())) {
				criteria.andOrderIdEqualTo(order.getOrderId());
			}

			//通过订单状态查询
			if (order.getStatus()!= null &&order.getStatus().length()>0) {
				criteria.andStatusEqualTo(order.getStatus());
			}

			//通过订单来源查询
			if (order.getSourceType() != null && order.getSourceType().length()>0) {
				criteria.andSourceTypeEqualTo(order.getSourceType());
			}

		}

		Page<TbOrder> page = (Page<TbOrder>) orderMapper.selectByExample(example);

		return new PageResult(page.getTotal(),page.getResult());


	}


    @Override
    public void updateStatus(Long id, String status) {

            TbOrder order = orderMapper.selectByPrimaryKey(id);

            order.setStatus(status);

            orderMapper.updateByPrimaryKey(order);

    }

	@Override
	public List<TbOrder> excel(TbOrder order) {

		// 进行条件查询:
		TbOrderExample example = new TbOrderExample();
		Criteria criteria = example.createCriteria();
		// 设置条件:
		if(order!=null) {
			//通过电话或者收件人查询
			if (order.getReceiver()!= null && order.getReceiver().length()>0) {

				try {
					new BigInteger(order.getReceiver());//能安全转换  说明纯数字

					criteria.andReceiverMobileEqualTo(order.getReceiver());
				} catch (NumberFormatException e) {//报错说明不是纯数字

					criteria.andReceiverLike("%" + order.getReceiver() + "%");
				}

			}

			//通过订单编号查询
			if (order.getOrderId()!= null && !"".equals(order.getOrderId())) {
				criteria.andOrderIdEqualTo(order.getOrderId());
			}

			//通过订单状态查询
			if (order.getStatus()!= null &&order.getStatus().length()>0) {
				criteria.andStatusEqualTo(order.getStatus());
			}

			//通过订单来源查询
			if (order.getSourceType() != null && order.getSourceType().length()>0) {
				criteria.andSourceTypeEqualTo(order.getSourceType());
			}

		}

		List<TbOrder> orders = orderMapper.selectByExample(example);

		System.out.println(orders);
		return orders;
	}

    @Override
    public TbSalesreturn findReturnOne(String id) {

        return salesreturnMapper.selectByPrimaryKey(id);
    }

	/**
	 * 查询每种订单状态的数量
	 * @return
	 */
	@Override
	public Map<String, Integer> findCountOfEveryStatus() {
		Map<String, Integer> map = new HashMap<>(); //定义一个map集合封装每种状态的订单数

		map.put("待付款",countOfEveryStatus("1"));
		map.put("待发货",countOfEveryStatus("3"));
		map.put("已发货",countOfEveryStatus("4"));
		map.put("已完成",countOfEveryStatus("5"));
		map.put("已关闭",countOfEveryStatus("6"));

		return map;
	}

	/**
	 * 封装example查询条件
	 * @param status
	 * @return
	 */
	private Integer countOfEveryStatus(String status) {
		TbOrderExample orderExample = new TbOrderExample();
		Criteria criteria = orderExample.createCriteria();
		criteria.andStatusEqualTo(status);
		return orderMapper.countByExample(orderExample);

	}

}
