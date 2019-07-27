package com.pinyougou.user.service;
import java.util.List;

import com.pinyougou.pojo.TbAreas;
import com.pinyougou.pojo.TbCities;
import com.pinyougou.pojo.TbProvinces;
import com.pinyougou.pojo.TbUser;

import entity.PageResult;
import entity.Result;

/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface UserService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbUser> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbUser user);
	
	
	/**
	 * 修改
	 */
	public void update(TbUser user);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbUser findOne(Long id);

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
	public PageResult findPage(TbUser user, int pageNum,int pageSize);
	
	
	/**
	 * 发送短信验证码
	 * @param phone
	 */
	public void createSmsCode(String phone);
	
	/**
	 * 校验验证码
	 * @param phone
	 * @param code
	 * @return
	 */
	public boolean checkSmsCode(String phone,String code);



 
	/***
	* @Description: 修改密码
	* @Param: [user, username]
	* @return: void
	* @Author: WangRui
	* @Date: 2019/7/25
	*/ 
	public Result updatePassword(TbUser user,String username);
	
	
	/***
	* @Description: 修改手机的号码
	* @Param: [username, phone]
	* @return: void
	* @Author: WangRui
	* @Date: 2019/7/25
	*/
	public void updatePhone(String username,String phone);

	
	
    /***
    * @Description: 登陆的用户
    * @Param: [name]
    * @return: com.pinyougou.pojo.TbUser
    * @Author: WangRui
    * @Date: 2019/7/25
    */

	TbUser findUser(String name);

	/**
	 * 查询所有省份
	 * @return
	 */
	public List<TbProvinces> findProvince();

	/**
	 * 查询所有市
	 * @return
	 */
	public List<TbCities> findCity(String pid);

	/**
	 * 查询所有区县
	 * @return
	 */
	public List<TbAreas> findArea(String cid);

	/**
	 *
	 * @param orderId
	 * @return
	 */

	String getEmailFromOrderId(String orderId);
}
