//地址管理服务层
app.service("addressService",function ($http) {
    //1.查询所有地址列表
    this.findAllAddress=function () {
        return $http.get("address/findAllAddress.do");
    };

    //2.查询所有省份列表
    this.findAllProvinces=function(){
        return $http.get("address/findAllProvinces.do");
    };

    //3.根据省份id查询城市列表
    this.findCityListByProvinceId=function(provinceId){
        return $http.get("address/findCityListByProvinceId.do?provinceId="+provinceId);
    };
    //4.根据城市id查询区域列表
    this.findAreaListByCityId=function (cityId) {
        return $http.get("address/findAreaListByCityId.do?cityId="+cityId);
    };

    //5.新增地址
    this.add=function (addressList) {
        return $http.post("address/add.do",addressList);
    };

    //6.删除地址
    this.delete=function (ids) {
        return $http.get("address/delete.do?ids="+ids);
    };

    //7.根据id查询（ 修改回显）
    this.findOne=function (id) {
        return $http.get("address/findOne.do?id="+id);
    };

    //8.修改地址
    this.update=function (address) {
        return $http.post("address/update.do",address);
    };

    //9.修改默认状态
    this.updateStatus=function (id,isDefault) {
        return $http.get("address/updateStatus.do?id="+id+"&isDefault="+isDefault);
    }
});