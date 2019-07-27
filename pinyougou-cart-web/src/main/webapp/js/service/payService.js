app.service('payService',function($http){
	//本地支付

	this.createNative=function(paymentType){
		if (paymentType=='1'){
            return $http.get('pay/createNative.do');
		}
		else if (paymentType=='3'){
            return $http.get('pay/createAliPayNative.do');
		}
	}
	
	//查询支付状态
	this.queryPayStatus=function(out_trade_no,paymentType){
		//alert('paymentType:'+paymentType);
		return $http.post('pay/queryPayStatus.do?out_trade_no='+out_trade_no+'&paymentType='+paymentType);
	}
});