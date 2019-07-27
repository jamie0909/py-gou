app.controller('orderController', function ($scope, orderService, $interval) {

    //定义一个订单状态数组,索引与状态码一致
    $scope.orderStatus = ['无', '提交订单未付款', '付款成功', '未发货', '已发货', '交易成功确认收货', '交易关闭', '待评价', '评价晒单'];

    //查询订单详情
    $scope.findOrder = function (id) {
        orderService.findOrder(id).success(
            function (response) {
                $scope.orderList = response;
                //$scope.goodsList=response;
                //$scope.orderCreateTime=[];
                $scope.orderCreateTime = $scope.orderList.order.createTime.split(" ");

                //$scope.orderPayTime=[];
                if ($scope.orderList.order.paymentTime != null) {
                    $scope.orderPayTime = $scope.orderList.order.paymentTime.split(" ");
                }


                if($scope.orderList.order.consignTime != null){
                    //$scope.consignTime=[];
                    $scope.consignTime = $scope.orderList.order.consignTime.split(" ");

                }

                if($scope.orderList.order.endTime !=null){
                    //$scope.endTime=[];
                    $scope.endTime = $scope.orderList.order.endTime.split(" ");
                }


                if($scope.orderList.order.closeTime != null){
                    //$scope.closeTime=[];
                    $scope.closeTime = $scope.orderList.order.closeTime.split(" ");
                }

                $scope.orderList.order.statusNum = parseInt(response.order.status);

                //alert(response.order.statusNum);
                //alert($scope.order[0])
                //倒计时开始
                //获取从结束时间到当前日期的秒数
                //allsecond= Math.floor( (new Date($scope.entity.endTime).getTime()- new Date().getTime())/1000);

                allsecond = 60 * 60 * 24 * 6;
                time = $interval(function () {
                    allsecond = allsecond - 1;
                    $scope.timeString = converTimeString(allsecond);

                    if (allsecond <= 0) {
                        $interval.cancel(time);
                    }
                }, 1000);
            }
        )
    }


    // 转换秒为 天小时格式
    converTimeString = function (allsecond) {
        var days = Math.floor(allsecond / (60 * 60 * 24));// 天数
        var hours = Math.floor((allsecond - days * 60 * 60 * 24) / (60 * 60));// 小时数
        var minutes = Math.floor((allsecond - days * 60 * 60 * 24 - hours * 60 * 60) / 60);// 分钟数
        var seconds = allsecond - days * 60 * 60 * 24 - hours * 60 * 60 - minutes * 60; // 秒数
        var timeString = "";
        if (days > 0) {
            timeString = days + "天 ";
        }
        return timeString + hours + "小时" + minutes + "分" + seconds + "秒";
    }

});