//地址管理控制层
app.controller("addressController",function ($scope,addressService,$location) {
    $scope.findAllAddress=function(){
        addressService.findAllAddress().success(
            function(response){
                $scope.address=response;

            }
        )
    };

    $scope.status=["设为默认","默认地址"];

    // $scope.isDefault=function(status){
    //     if(status==$scope.status[1]){
    //         return true
    //     }else {
    //         return false;
    //     }
    // };

    //查询省份信息
   // $scope.entity={};
    $scope.findAllProvinces=function () {
        addressService.findAllProvinces().success(
            function(response){
                $scope.provinceList=response;
            }
        )
    };

    // 查询城市下拉列表(根据省份id)
    $scope.$watch('entity.provinces.provinceid',function (newValue,oldValue) {
        addressService.findCityListByProvinceId(newValue).success(
            function (response) {
                $scope.cityList=response;

            }
        )
    });
    // 查询区域下拉列表(根据城市id)
    $scope.$watch('entity.cities.city',function (newValue,oldValue) {
        addressService.findAreaListByCityId(newValue).success(
            function (response) {
                $scope.areaList=response;

            }
        )
    });

    //新增地址


    $scope.add=function () {

         addressService.add($scope.entity).success(
             function (response){
                 if(response.success){
                     $scope.findAllAddress();
                 }else {
                     alert(response.message);
                 }
             }
         )
     };


     //删除地址
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

    //根据id查询（ 修改回显）
    $scope.findOne=function (id) {
        addressService.findOne(id).success(
            function (response) {

                $scope.Add=response;

            }
        )
    }
});