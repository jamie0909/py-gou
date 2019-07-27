app.controller('orderController',function($scope,orderService){

    //查询订单详情
    $scope.findOne=function(id){
        orderService.findOne(id).success(
            function(response){
                $scope.orderList=response;
            }
        )
    }

});