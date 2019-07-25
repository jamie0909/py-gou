app.service('payService',function($http){
	//本地支付

	this.createNative=function(paymentType){
		if (paymentType=='1'){
            return $http.get('pay/createNative.do');
		}
		if (paymentType=='3'){
            return $http.get('pay/createAliPayNative.do');
		}
	}
	
	//查询支付状态
	this.queryPayStatus=function(out_trade_no){
		return $http.get('pay/queryPayStatus.do?out_trade_no='+out_trade_no);
	}
});