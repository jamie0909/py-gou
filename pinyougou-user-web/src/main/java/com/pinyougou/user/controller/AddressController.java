package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pinyougou.pojo.TbAddress;
import com.pinyougou.pojo.TbAreas;
import com.pinyougou.pojo.TbCities;
import com.pinyougou.pojo.TbProvinces;
import com.pinyougou.user.service.AddressService;
import entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: pinyougou-parent
 * @description:
 * @author: Mr.Cai
 * @create: 2019-07-25 14:19
 */
@RestController
@RequestMapping("/address")
public class AddressController {
    @Reference
    private AddressService addressService;

    /**
     * 返回全部地址列表
     * @return
     */
    @RequestMapping("/findAllAddress")
    public List<TbAddress> findAllAddress(){
        return addressService.findAll();
    }

    /**
     * 查询所有省份信息
     * @return
     */
    @RequestMapping("/findAllProvinces")
    public List<TbProvinces> findAllProvinces(){
        return addressService.findAllProvinces();
    }

    /**
     * 根据省份id查询城市列表
     * @param provinceId
     * @return
     */
    @RequestMapping("/findCityListByProvinceId")
    public List<TbCities> findCityListByProvinceId(String provinceId){
        return addressService.findCityListByProvinceId(provinceId);
    }

    /**
     * 根据城市id查询区域列表
     * @param cityId
     * @return
     */
    @RequestMapping("/findAreaListByCityId")
    public List<TbAreas> findAreaListByCityId(String cityId){
        return addressService.findAreaListByCityId(cityId);
    }

    @RequestMapping("/add")
    public Result add(@RequestBody TbAddress address){



        String string = JSON.toJSONString(address);
        System.out.println(string);
        String contact1 = address.getContact();


        try {
            String userId = SecurityContextHolder.getContext().getAuthentication().getName();
            address.setUserId(userId);
            address.setIsDefault("0");
            List<TbAddress> addList = addressService.findAll();
            String username = address.getContact();
            for (TbAddress tbAddress : addList) {
                String contact = tbAddress.getContact();
               if(username.equals(contact)||username==null||username.equals("")){

                   return  new Result(false,"用户名已存在或无效");
               }
            }

            if(!address.getContact().equals(null)&&!address.getContact().equals("")) {
                addressService.add(address);
            }
            return new Result(true,"添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"添加失败");
        }
    }
    @RequestMapping("/updateStatus")
    public void updateStatus(long id, String isDefault){

        addressService.updateStatus(id,isDefault);
    }

    //删除
    @RequestMapping("/delete")
    public Result delete(Long [] ids){
        try {
            addressService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

    @RequestMapping("/findOne")
    public TbAddress findOne(Long id){
        return addressService.findOne(id);
    }
}
