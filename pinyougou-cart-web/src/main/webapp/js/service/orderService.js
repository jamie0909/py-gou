app.service('orderService',function($http){

    //查询订单详情
    this.findOne=function(id){
        return $http.get('order/findOne.do?id='+id);
    }

});