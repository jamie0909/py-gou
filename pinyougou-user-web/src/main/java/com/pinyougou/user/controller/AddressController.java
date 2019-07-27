package com.pinyougou.user.controller;

import com.alibaba.dubbo.config.annotation.Reference;
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

import java.util.Date;
import java.util.List;

/**
 * @program: pinyougou-parent
 * @description:地址管理控制层
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
     * 查询所有省份列表
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

    /**
     * 新增地址方法
     * @param address
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody TbAddress address){

        if(address==null){
            return  new Result(false,"地址输入无效或为空，请重新输入");
        }

        try {
            String userId = SecurityContextHolder.getContext().getAuthentication().getName();
            address.setUserId(userId);
            address.setCreateDate(new Date());//创建时间
            address.setIsDefault("0");//设置默认状态
            List<TbAddress> addList = addressService.findAll();
            String username = address.getContact();
            for (TbAddress tbAddress : addList) {
                String contact = tbAddress.getContact();
               if(contact.equals(username)||username==null||username.equals("")){
                   return  new Result(false,"收货人已存在或无效，请重新输入");
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

    /**
     *根据id删除地址
     * @param ids
     * @return
     */
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

    /**
     * 修改地址
     * @param tbAddress
     * @return
     */
    @RequestMapping("/update")
    public Result update(@RequestBody TbAddress tbAddress){
        try {
            addressService.update(tbAddress);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

    /**
     * 根据id查询实体类(回显)
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public TbAddress findOne(Long id){
        return addressService.findOne(id);
    }

    /**
     * 修改默认状态
     * @param id
     * @param isDefault
     */
    @RequestMapping("/updateStatus")
    public Result updateStatus(long id, String isDefault){
        try {
            addressService.updateStatus(id,isDefault);
            return new Result(true, "设置成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "设置失败");
        }
    }
}
