//地址管理控制层
app.controller("addressController",function ($scope,addressService,$location) {

    //1.查询所有地址列表
    $scope.findAllAddress=function(){
        addressService.findAllAddress().success(
            function(response){
                $scope.address=response;

            }
        )
    };

    //2.查询所有省份列表

    //2.1初始化集合(当选择省的时候，市和区置为空)
    $scope.provinceList={};
    $scope.cityList={};
    $scope.areaList={};


    $scope.findAllProvinces=function () {
        addressService.findAllProvinces().success(
            function(response){
                $scope.provinceList=response;
                $scope.cityList={};
                $scope.areaList={};

            }
        )
    };
   // var pro1=$scope.entity.provinces.provinceid;
   // alert(pro1);
    //3.查询城市下拉列表(根据省份id)
    $scope.$watch('entity.provinceId',function (newValue,oldValue) {
        addressService.findCityListByProvinceId(newValue).success(
            function (response) {
                $scope.cityList=response;
                $scope.areaList={};
            }
        )
    });

    //4.查询区域下拉列表(根据城市id)
    $scope.$watch('entity.cityId',function (newValue,oldValue) {
       if('entity.provinces.provinceid')
            addressService.findAreaListByCityId(newValue).success(
                function (response) {
                    $scope.areaList=response;
                }
            )
    });

    //5.新增与修改地址
    $scope.add=function () {
        var object;
        if($scope.Add.id!=null){
            if(confirm("您确认修改地址吗?")){
                object=addressService.update($scope.Add);//修改操作
            }
        }else {
            if(confirm("您确认新增地址吗?")){

              // alert($scope.entity.provinces.provinceid);
              //5.1 重新封装表单中的数据(因为三级联动(省市区)省市区
              // 的id返回到前端是是json对象，而后端需要的是
              // 大键的值的键值对，所以需要前端传值的时候，手动封装键值对)
              // {provinces:{provinceid:"110000",contact:"小强"}}
              // var pro=$scope.entity.provinces.provinceid;
              // var cit=$scope.entity.cities.city;
              // var area=$scope.entity.areas.area;
              // var contact=$scope.entity.contact;
              // var address=$scope.entity.address;
              // var mobile=$scope.entity.mobile;
              // var email=$scope.entity.email;
              // var alias=$scope.entity.alias;
              // alert(pro);
              // alert(cit);
              // alert(area);
              // 5.2 封装Json对象并赋值
              // $scope.entity={provinceId:pro,cityId:cit,areaId:area,address:address,
              //        mobile:mobile,email:email,alias:alias,contact:contact};

              if($scope.address.length<10){
                  object= addressService.add($scope.entity)//添加操作
              }else {
                  alert("地址已达到上限，请删除不常用地址")
              }
            }
        }
         object.success(
             function (response){
                 if(response.success){
                     //成功，查询地址列表
                     $scope.findAllAddress();
                 }else {
                     alert(response.message);
                 }
             }
         )
     };

     //6.删除地址
    $scope.delete=function (id) {
        if(confirm("您确认删除吗?")){
            addressService.delete(id).success(
                function (response) {
                    if(response.success){
                        $scope.findAllAddress();
                    }else {
                        alert(response.message);
                    }
                }
            )
        }
    };

    //7.根据id查询（ 修改回显）
    $scope.findOne=function (id) {
        addressService.findOne(id).success(
            function (response) {
                $scope.Add=response;
            }
        )
    };

    //8.修改回显三级联动
    // 查询城市下拉列表(根据省份id)


    $scope.$watch('Add.provinceId',function (newValue,oldValue) {
        addressService.findCityListByProvinceId(newValue).success(
            function (response) {
                $scope.cityList=response;
                $scope.areaList={};
            }
        )
    });
    // 查询区域下拉列表(根据城市id)
    $scope.$watch('Add.cityId',function (newValue,oldValue) {
        addressService.findAreaListByCityId(newValue).success(
            function (response) {
                $scope.areaList=response;
            }
        )
    });

    //9.修改默认状态
    $scope.updateStatus=function (id,isDefault) {
        addressService.updateStatus(id,isDefault).success(
            function (response) {
                if(response.success){
                    $scope.findAllAddress();
                }else {
                   alert(response.message);
                }
            }
        )
    };
    //10.增加地址时修改地址别名方法
    $scope.entity={alias:''};
    $scope.updateAlias=function (address) {
        $scope.entity.alias=address;
    };

    //11.修改地址时修改地址别名方法
    $scope.entity={alias:''};
    $scope.update_Alias=function (address) {
        $scope.Add.alias=address;
    }
});