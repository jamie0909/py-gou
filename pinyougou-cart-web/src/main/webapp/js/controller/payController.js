app.controller('payController' ,function($scope ,$location,payService){
	
	
	$scope.createNative=function(){
		var paymentType=$location.search()['type'];
		$scope.paymentType_str='微信';
		$scope.otherPaymentType_str='支付宝';
		if(paymentType=='3'){
			$scope.paymentType_str='支付宝';
            $scope.otherPaymentType_str='微信';
		}
		payService.createNative(paymentType).success(
			function(response){
				
				//显示订单号和金额
				$scope.money= (response.total_fee/100).toFixed(2);
				$scope.out_trade_no=response.out_trade_no;
				
				//生成二维码
				 var qr=new QRious({
					    element:document.getElementById('qrious'),
						size:250,
						value:response.code_url,
						level:'H'
			     });
				 $scope.paymentType=paymentType;
				 $scope.queryPayStatus();//调用查询
				
			}	
		);	
	}
	
	//调用查询
	$scope.queryPayStatus=function(paymentType){
		payService.queryPayStatus($scope.out_trade_no,$scope.paymentType).success(
			function(response){
				if($scope.paymentType=='1'){
					$scope.pay_str='微信';
				}
                if($scope.paymentType=='3'){
                    $scope.pay_str='支付宝';
                }
				if(response.success){
					location.href="paysuccess.html#?money="+$scope.money+"&pay_str="+$scope.pay_str;
				}else{
					if(response.message=='二维码超时'){
						$scope.createNative();//重新生成二维码
					}else{
						location.href="payfail.html";
					}
				}				
			}		
		);		
	}
	
	//获取金额
	$scope.getMoney=function(){
		return $location.search()['money'];
	}

	//获取支付方式
	$scope.getPayStr=function(){
		return $location.search()['pay_str'];
	}


	$scope.changeOtherPayType=function () {
		$scope.paymentType=$location.search()['type'];
		if ($scope.paymentType=='1'){
            location.href="pay-other.html#?type=3";
            return;
		}

         if($scope.paymentType=='3'){
             location.href="pay.html#?type=1";
             return;
        }

    }

    $scope.changeDeliveryPayType=function () {

		//alert('订单编号:'+$scope.out_trade_no);
		//$scope.out_trade_no=$location.search()['out_trade_type'];
		location.href="pay-deliverypay.html#?out_trade_no="+$scope.out_trade_no+"&money="+$scope.money;
    }

    $scope.getTradeNo=function () {
		$scope.out_trade_no=$location.search()['out_trade_no'];
    }
});