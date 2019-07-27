package com.pinyougou.user.service.impl;
import java.util.List;

import com.pinyougou.mapper.TbAreasMapper;
import com.pinyougou.mapper.TbCitiesMapper;
import com.pinyougou.mapper.TbProvincesMapper;
import com.pinyougou.pojo.*;
import com.pinyougou.pojo.group.AddressList;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbAddressMapper;
import com.pinyougou.pojo.TbAddressExample.Criteria;
import com.pinyougou.user.service.AddressService;

import entity.PageResult;

/**
 * 地址管理服务实现层
 * @author Administrator
 *
 */
@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	private TbAddressMapper addressMapper;


	@Override
	public PageResult findPage(TbAddress address, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);

		TbAddressExample example=new TbAddressExample();
		Criteria criteria = example.createCriteria();

		if(address!=null){
			if(address.getUserId()!=null && address.getUserId().length()>0){
				criteria.andUserIdLike("%"+address.getUserId()+"%");
			}
			if(address.getProvinceId()!=null && address.getProvinceId().length()>0){
				criteria.andProvinceIdLike("%"+address.getProvinceId()+"%");
			}
			if(address.getCityId()!=null && address.getCityId().length()>0){
				criteria.andCityIdLike("%"+address.getCityId()+"%");
			}
			if(address.getAreaId()!=null && address.getAreaId().length()>0){
				criteria.andAreaIdLike("%"+address.getAreaId()+"%");
			}
			if(address.getMobile()!=null && address.getMobile().length()>0){
				criteria.andMobileLike("%"+address.getMobile()+"%");
			}
			if(address.getAddress()!=null && address.getAddress().length()>0){
				criteria.andAddressLike("%"+address.getAddress()+"%");
			}
			if(address.getContact()!=null && address.getContact().length()>0){
				criteria.andContactLike("%"+address.getContact()+"%");
			}
			if(address.getIsDefault()!=null && address.getIsDefault().length()>0){
				criteria.andIsDefaultLike("%"+address.getIsDefault()+"%");
			}
			if(address.getNotes()!=null && address.getNotes().length()>0){
				criteria.andNotesLike("%"+address.getNotes()+"%");
			}
			if(address.getAlias()!=null && address.getAlias().length()>0){
				criteria.andAliasLike("%"+address.getAlias()+"%");
			}

		}

		Page<TbAddress> page= (Page<TbAddress>)addressMapper.selectByExample(example);
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public List<TbAddress> findListByUserId(String userId) {

		TbAddressExample example=new TbAddressExample();
		Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		return addressMapper.selectByExample(example);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Page<TbAddress> page=   (Page<TbAddress>) addressMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	//===============用户地址管理功能开始位置================
	//===============功能包括查询用户所有地址列表=============
	//===============新增收货地址(省市区三级联动)=============
	//===============修改，删除，设置默认地址等===============

	/**
	 * 返回全部地址列表
	 */
	@Override
	public List<TbAddress> findAll() {
		List<TbAddress> addresses = addressMapper.selectByExample(null);
		for (TbAddress address : addresses) {
			String mobile = address.getMobile();
			String phoneNumber = mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
			address.setMobile(phoneNumber);
		}
		return addresses;
	}
	private void saveToRedis(){
		List<TbAddress> addresse = findAll();
	}

	/**
	 * 查询所有省份列表
	 * @return
	 */
	@Override
	public List<TbProvinces> findAllProvinces() {

		return provincesMapper.selectByExample(null);
	}

	@Autowired
	private TbProvincesMapper provincesMapper;

	@Autowired
	private TbCitiesMapper citiesMapper;

	@Autowired
	private TbAreasMapper areasMapper;
	/**
	 * 根据省id查询城市列表
	 * @param provinceId
	 * @return
	 */
	@Override
	public List<TbCities> findCityListByProvinceId(String provinceId) {

		TbCitiesExample example=new TbCitiesExample();

		TbCitiesExample.Criteria criteria = example.createCriteria();

		criteria.andProvinceidEqualTo(provinceId);

		return citiesMapper.selectByExample(example);
	}

	/**
	 * 根据城市id查询区域列表
	 * @param cityId
	 * @return
	 */
	@Override
	public List<TbAreas> findAreaListByCityId(String cityId) {


		TbAreasExample example=new TbAreasExample();

		TbAreasExample.Criteria criteria = example.createCriteria();

		criteria.andCityidEqualTo(cityId);

		return areasMapper.selectByExample(example);
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbAddress address) {
		addressMapper.insert(address);		
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			addressMapper.deleteByPrimaryKey(id);
		}
	}

	/**
	 * 修改
	 */
	@Override
	public void update(TbAddress address){
		addressMapper.updateByPrimaryKey(address);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbAddress findOne(Long id){
		return addressMapper.selectByPrimaryKey(id);
	}

	/**
	 * 设置默认地址
	 * @param id
	 * @param isDefault
	 */
	@Override
	public void updateStatus(long id, String isDefault) {
		TbAddress address=new TbAddress();
		address.setId(id);
		address.setIsDefault(isDefault);
		System.out.println(id);
		System.out.println(address.getIsDefault());

		addressMapper.updateByPrimaryKeySelective(address);
	}
	//===============用户地址管理功能结束位置===============

}
