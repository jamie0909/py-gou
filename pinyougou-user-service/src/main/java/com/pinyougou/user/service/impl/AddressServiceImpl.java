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
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	private TbAddressMapper addressMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbAddress> findAll() {
		List<TbAddress> addresses = addressMapper.selectByExample(null);
		for (TbAddress address : addresses) {
			String mobile = address.getMobile();
			String phoneNumber = mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
			address.setMobile(phoneNumber);
			addressMapper.updateByPrimaryKey(address);
		}

		return addressMapper.selectByExample(null);
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

	/**
	 * 增加
	 */
	@Override
	public void add(TbAddress address) {
		TbProvinces provinces=new TbProvinces();
		TbCities cities=new TbCities();
		String provinceId = address.getProvinceId();
		String cityId = address.getCityId();
		System.out.println(provinceId);
		System.out.println(cityId);
		provinces.setProvince(provinceId);
		cities.setCity(cityId);
		addressMapper.insert(address);		
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
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			addressMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
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
			if(address.getTownId()!=null && address.getTownId().length()>0){
				criteria.andTownIdLike("%"+address.getTownId()+"%");
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
	@Autowired
	private TbProvincesMapper provincesMapper;

	@Autowired
	private TbCitiesMapper citiesMapper;

	@Autowired
	private TbAreasMapper areasMapper;


	@Override
	public List<TbProvinces> findAllProvinces() {

		return provincesMapper.selectByExample(null);
	}

	@Override
	public List<TbCities> findCityListByProvinceId(String provinceId) {


		TbCitiesExample example=new TbCitiesExample();

		TbCitiesExample.Criteria criteria = example.createCriteria();

		criteria.andProvinceidEqualTo(provinceId);

		return citiesMapper.selectByExample(example);
	}

	@Override
	public List<TbAreas> findAreaListByCityId(String cityId) {


		TbAreasExample example=new TbAreasExample();

		TbAreasExample.Criteria criteria = example.createCriteria();

		criteria.andCityidEqualTo(cityId);

		return areasMapper.selectByExample(example);
	}

	@Override
	public List<AddressList> findAddressAndCountry() {

		List<TbAddress> address = findAll();//查询所有地址



		return null;
	}

	@Override
	public void updateStatus(long id, String isDefault) {
		TbAddress address=new TbAddress();
		address.setId(id);
		address.setIsDefault(isDefault);

		System.out.println(address.getContact());
		System.out.println(address.getIsDefault());

		addressMapper.updateByPrimaryKeySelective(address);
	}


}
