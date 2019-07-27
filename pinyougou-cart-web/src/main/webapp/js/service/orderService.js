app.service('orderService',function($http){

    //查询订单详情
    this.findOrder=function(id){
        return $http.get('order/findOrder.do?id='+id);
    }

});