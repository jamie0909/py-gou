package com.pinyougou.order.service;
import java.util.List;
import java.util.Map;

import com.pinyougou.pojo.TbOrder;
import com.pinyougou.pojo.TbPayLog;

import com.pinyougou.pojo.TbSalesreturn;
import com.pinyougou.pojo.group.Order;
import entity.PageResult;



/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface OrderService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbOrder> findAll();


	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);


	/**
	 * 条件分页查询
	 * @param order
	 * @param page
	 * @param rows
	 * @return
	 */
	PageResult findByPage(TbOrder order, int page, int rows);
	
	
	/**
	 * 增加
	*/
	public void add(TbOrder order);
	
	
	/**
	 * 修改
	 */
	public void update(TbOrder order);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public Order findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long [] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbOrder order, int pageNum,int pageSize);
	
	/**
	 * 根据用户ID获取支付日志
	 * @param userId
	 * @return
	 */
	public TbPayLog searchPayLogFromRedis(String userId);
	
	
	/**
	 * 支付成功修改状态
	 * @param out_trade_no
	 * @param transaction_id
	 */
	public void updateOrderStatus(String out_trade_no,String transaction_id);


    void deleteOne(Long id);

	public void updateStatus(Long id,String status);

	/**
	 * poi报表导出   wjk
	 * @param order
	 * @return
	 */
    List<TbOrder> excel(TbOrder order);

	/**
	 * 返回退单个货商品详情   wjk
	 * @param id
	 * @return
	 */
	TbSalesreturn findReturnOne(String id);


	/**
	 * 查询每种状态的的数量
	 */
	public Map<String,Integer> findCountOfEveryStatus();



}
