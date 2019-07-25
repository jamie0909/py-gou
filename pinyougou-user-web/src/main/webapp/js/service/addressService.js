//地址管理服务层
app.service("addressService",function ($http) {
    this.findAllAddress=function () {
        return $http.get("address/findAllAddress.do");
    };

    this.findAllProvinces=function(){
        return $http.get("address/findAllProvinces.do");
    };

    this.findCityListByProvinceId=function(provinceId){
        return $http.get("address/findCityListByProvinceId.do?provinceId="+provinceId);
    };
    this.findAreaListByCityId=function (cityId) {
        return $http.get("address/findAreaListByCityId.do?cityId="+cityId);
    };

    //新增地址
    this.add=function (address) {
        return $http.post("address/add.do",address);
    };
    //删除地址
    this.delete=function (ids) {
        return $http.get("address/delete.do?ids="+ids);
    };
    //根据id查询（ 修改回显）
    this.findOne=function (id) {
        return $http.get("address/findOne.do?id="+id);
    }
});