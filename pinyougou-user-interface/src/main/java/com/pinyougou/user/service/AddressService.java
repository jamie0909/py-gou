package com.pinyougou.user.service;
import java.util.List;
import com.pinyougou.pojo.TbAddress;

import com.pinyougou.pojo.TbAreas;
import com.pinyougou.pojo.TbCities;
import com.pinyougou.pojo.TbProvinces;
import com.pinyougou.pojo.group.AddressList;
import entity.PageResult;
/**
 * 地址管理服务层接口
 * @author Administrator
 *
 */
public interface AddressService {


	public List<TbAddress> findListByUserId(String userId);
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbAddress address, int pageNum,int pageSize);

	//===============用户地址管理功能开始位置================
	//===============功能包括查询用户所有地址列表=============
	//===============新增收货地址(省市区三级联动)=============
	//===============修改，删除，设置默认地址等===============
	/**
	 * 返回全部地址列表
	 * @return
	 */
	public List<TbAddress> findAll();

	/**
	 * 增加
	*/
	public void add(TbAddress address);

	/**
	 * 查询所有省份列表
	 * @return
	 */
	public List<TbProvinces> findAllProvinces();

	/**
	 * 根据省id查询城市列表
	 * @param provinceId
	 * @return
	 */
	public List<TbCities> findCityListByProvinceId(String provinceId);

	/**
	 * 根据城市id查询区域列表
	 * @param cityId
	 * @return
	 */
	public List<TbAreas> findAreaListByCityId(String cityId);

	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long [] ids);

	/**
	 * 根据ID获取实体(修改回显)
	 * @param id
	 * @return
	 */
	public TbAddress findOne(Long id);

	/**
	 * 修改
	 * @param address
	 */
	public void update(TbAddress address);

	/**
	 * 设置默认地址
	 * @param id
	 * @param isDefault
	 */
	public void updateStatus(long id,String isDefault);

	//===============用户地址管理功能结束位置===============
}
