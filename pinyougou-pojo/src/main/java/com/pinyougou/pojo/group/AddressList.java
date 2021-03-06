package com.pinyougou.pojo.group;

import com.pinyougou.pojo.TbAddress;
import com.pinyougou.pojo.TbAreas;
import com.pinyougou.pojo.TbCities;
import com.pinyougou.pojo.TbProvinces;

import java.io.Serializable;
import java.util.List;

/**
 * @program: pinyougou-parent
 * @description:地址与城市的组合实体类
 * @author: Mr.Cai
 * @create: 2019-07-25 19:27
 */
public class AddressList implements Serializable {

    private TbAddress address;//地址对象

    private TbProvinces provinces;//省

    private TbCities cities;//城市

    private TbAreas areas;//区域

    public TbAddress getAddress() {
        return address;
    }

    public void setAddress(TbAddress address) {
        this.address = address;
    }

    public TbProvinces getProvinces() {
        return provinces;
    }

    public void setProvinces(TbProvinces provinces) {
        this.provinces = provinces;
    }

    public TbCities getCities() {
        return cities;
    }

    public void setCities(TbCities cities) {
        this.cities = cities;
    }

    public TbAreas getAreas() {
        return areas;
    }

    public void setAreas(TbAreas areas) {
        this.areas = areas;
    }
}
