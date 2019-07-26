//控制层
app.controller('orderController' ,function($scope,$controller,$http,orderService) {

    // AngularJS中的继承:伪继承
    $controller('baseController',{$scope:$scope});

    //读取列表数据绑定到表单中
    $scope.findAll=function(){
        orderService.findAll().success(
            function(response){
                $scope.list=response;
            }
        );
    }

    // 分页查询
    $scope.findByPage = function(page,rows){
        // 向后台发送请求获取数据:
        orderService.findByPage(page,rows).success(function(response){
            $scope.paginationConf.totalItems = response.total;
            $scope.list = response.rows;
        });
    }
    //显示订单状态
    $scope.status = ["","未付款","已付款","未发货","已发货","交易成功","交易关闭","待评价","退货申请","退货完成"];

    //支付类型
    $scope.payType = ["","在线支付","货到付款"];

    //订单来源
    $scope.orderSource = ["","app端","pc端","M端","微信端","手机qq端"];


    // 删除选中:
    $scope.dele = function(){
        orderService.dele($scope.selectIds).success(function(response){
            // 判断保存是否成功:
            if(response.flag==true){
                // 删除成功
                // alert(response.message);
                $scope.reloadList();
                $scope.selectIds = [];
            }else{
                // 删除失败
                alert(response.message);
            }
        });
    }

    $scope.deleOne=function (id) {
        orderService.deleOne(id).success(function (response) {
            if(response.flag==true){
                // 删除成功
                // alert(response.message);
                $scope.reloadList();
            }else{
                // 删除失败
                alert(response.message);
            }

        })
    }

    //查询实体
    $scope.findOne=function(id){
        orderService.findOne(id).success(
            function(response){
                $scope.entity= response;
            }
        );
    }


    // 审核的方法:
    $scope.updateStatus = function(orderId,status){
        orderService.updateStatus(orderId,status).success(function(response){
            if(response.flag){
                $scope.reloadList();//刷新列表
            }else{
                alert(response.message);
            }
        });
    }


    $scope.searchEntity={};

    // 假设定义一个查询的实体：searchEntity
    $scope.search = function(page,rows){
        // 向后台发送请求获取数据:
        orderService.search(page,rows,$scope.searchEntity).success(function(response){
            $scope.paginationConf.totalItems = response.total;
            $scope.list = response.rows;
        });
    }


    //excel报表导出
    $scope.excelOperate = function(){
        alert($scope.searchEntity);
        orderService.excelOperate($scope.searchEntity).success(function(response){
            alert("11111111111");
            if(response.flag){
                alert(response.message);
            }else{
                alert(response.message);
            }
        });
    }


});