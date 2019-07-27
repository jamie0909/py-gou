 //控制层 
app.controller('orderController' ,function($scope,$controller ,$location  ,orderService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		orderService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	};

	$scope.orderEntityList = [{"orderList":[{"orderItems":[]}]}];
    // $scope.orderEntityList.orderList = [];
    // $scope.orderEntityList.orderList.orderItems = [];

	//分页
	$scope.findPage=function(status){
		if($scope.pageNo == undefined || $scope.pageNo==null ){
			$scope.pageNo =1;
		}
        $scope.currentChoose = status;
		orderService.findOrderListfromPayLog($scope.pageNo,5,status).success(
			function(response){
                console.log(response);

				$scope.orderEntityList=response.orderEntityList;
				$scope.totalPages=response.totalPages;
                $scope.pageNo=response.currentPage;
                window.scroll(0,0);
                buildPageLabel();
                window.scroll(0,0);
                $scope.showName();

			}			
		);

	};
	
	//查询实体 
	$scope.findOne=function(id){				
		orderService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=orderService.update( $scope.entity ); //修改  
		}else{
			serviceObject=orderService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		orderService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		orderService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}



    //构建分页栏
    buildPageLabel=function(){
        //构建分页栏
        $scope.pageLabel=[];
        var firstPage=1;//开始页码
        var lastPage=$scope.totalPages;//截止页码
        $scope.firstDot=true;//前面有点
        $scope.lastDot=true;//后边有点

        if($scope.totalPages>3){  //如果页码数量大于5

            if($scope.pageNo<=2){//如果当前页码小于等于3 ，显示前5页
                lastPage=3;
                $scope.firstDot=false;//前面没点
            }else if( $scope.pageNo>= $scope.totalPages-1 ){//显示后5页
                firstPage=$scope.totalPages-2;
                $scope.lastDot=false;//后边没点
            }else{  //显示以当前页为中心的5页
                firstPage=$scope.pageNo-1;
                lastPage=$scope.pageNo+1;
            }
        }else{
            $scope.firstDot=false;//前面无点
            $scope.lastDot=false;//后边无点
        }


        //构建页码
        for(var i=firstPage;i<=lastPage;i++){
            $scope.pageLabel.push(i);
        }
    };

    //分页查询
    $scope.queryByPage=function(pageNo,statusStr){
        if(pageNo<1 ){
            return 1;
        }

        if( pageNo>$scope.totalPages){
            return totalPages;
        }


        $scope.pageNo=pageNo;
        $scope.findPage(statusStr)//查询

    };

    //判断当前页是否为第一页
    $scope.isTopPage=function(){
        if($scope.pageNo==1){
            return true;
        }else{
            return false;
        }
    }

    //判断当前页是否为最后一页
    $scope.isEndPage=function(){
        if($scope.pageNo==$scope.totalPages){
            return true;
        }else{
            return false;
        }
    };

    $scope.isCurrentPage = function(page){
        if($scope.pageNo == page){
            return true;
        }
        return false;
    }

    $scope.orderItems = [];

    //定义一个订单状态数组,索引与状态码一致
    $scope.orderStatus = ['无', '提交订单','付款成功', '已发货','确认收货','交易关闭','评价晒单'];

   // $scope.orderStatusStr = ["未付款","已付款","未发货","已发货","交易成功","交易关闭","待评价"];

    $scope.waitStatusStr = ["等待买家付款","买家已付款","买家已付款","物流指派中","交易成功","交易关闭","待评价","已申请退货","退货成功"];

    $scope.detailStatus = ["无","提交订单","付款成功","已发货","确认收货","交易关闭","评价晒单"];

    $scope.paymentTypeStr = ["","微信支付","货到付款","支付宝支付"];

    $scope.findOrder = function () {
		orderService.findOne($location.search()['orderId']).success(
			function (response) {
				$scope.order = response;
            }
		);
        $scope.findOrderItemByOrderId();
    };


    $scope.findOrderItemByOrderId = function () {
        orderService.findOrderItemByOrderId($location.search()['orderId']).success(
            function (response) {
                $scope.orderItems = response;
            }
        )
    };



    $scope.isCurrent = function (status,comparedStatus) {
        if($scope.detailStatus[status] == comparedStatus){
            return "current";
        }
        return "todo";
    }



    $scope.updateStatusStr = function(out_order_no,status){
        orderService.updateStatusStr(out_order_no,status).success(
            function (response) {
                alert(response.message);
                $scope.findPage($scope.currentChoose);
                
            }
        )
    };
    


    $scope.showName=function(){
        orderService.showName().success(
            function(response){
                $scope.loginName=response.loginName;
            }
        );
    };

    $scope.getNumberOfDays = function(date1){
        //date1：开始日期，date2结束日期
        var a1 = Date.parse(new Date(date1));
        var a2 = Date.parse(new Date());

        var hours = parseInt((a2-a1)/ (1000 * 60 * 60 )+15*24);//核心：时间戳相减，然后得到小时数
        if(hours < 0 || hours >15){
            return "";
        }
        var timeStr ="还剩"+ hours%24==0?Math.floor(hours/24)+'天':Math.floor(hours/24)+'天'+hours%24+'小时自动确认收货';
        return timeStr;
    };

   // alert( $scope.getNumberOfDays(new Date(),new Date("2019/7/20")));

    $scope.sendEmail = function (orderId) {
        orderService.sendEmail(orderId).success(
            function (response) {
                if (response.success){
                    alert("已经提醒卖家尽快发货")
                }else{
                    alert("出错了，亲稍后再试")
                }
            }
        )
    }

    $scope.findOrderListByOutOrderNo = function () {
        orderService.findOrderListByOutOrderNo($location.search()['outTradeNo']).success(
            function (response) {
                $scope.entity = response;


                //切割时间
                $scope.orderCreateTime = $scope.entity.orderList[0].createTime.split(" ");

                //$scope.orderPayTime=[];
                if ($scope.entity.orderList[0].paymentTime != null) {
                    $scope.orderPayTime = $scope.entity.orderList[0].paymentTime.split(" ");
                }


                if($scope.entity.orderList[0].consignTime != null){
                    //$scope.consignTime=[];
                    $scope.consignTime = $scope.entity.orderList[0].consignTime.split(" ");

                }

                if($scope.entity.orderList[0].endTime !=null){
                    //$scope.endTime=[];
                    $scope.endTime = $scope.entity.orderList[0].endTime.split(" ");
                }


                if($scope.entity.orderList[0].closeTime != null){
                    //$scope.closeTime=[];
                    $scope.closeTime = $scope.entity.orderList[0].closeTime.split(" ");
                }

                $scope.statusNum = parseInt($scope.entity.payLog.status);


            }
        )
    }












    $scope.isShow = function (orderEntity) {
        if(orderEntity.payLog.status == '6' ){
            return false;
        }else if (orderEntity.payLog.status == '1' ){
            return false;
        }else if (orderEntity.payLog.status == '8' ){
            return false;
        }
        return true;

    }

    $scope.checkNum = function () {
        if($scope.pageNo < 0 ){
            $scope.pageNo =0;
        }
        if($scope.pageNo >  $scope.totalPages ){
            $scope.pageNo =$scope.totalPages;
        }
    }




});	
