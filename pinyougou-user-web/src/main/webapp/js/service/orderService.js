//服务层
app.service('orderService',function($http) {

    //读取列表数据绑定到表单中
    this.findAll = function () {
        return $http.get('../order/findAll.do');
    };
    //分页
    this.findOrderListfromPayLog = function (pageNum, pageSize, statusStr) {
        return $http.post('../order/findOrderListfromPayLog.do?pageNum=' + pageNum + '&pageSize=' + pageSize + '&status=' + statusStr);
    };
    //查询实体
    this.findOne = function (id) {
        return $http.get('../order/findOne.do?id=' + id);
    };
    //增加
    this.add = function (entity) {
        return $http.post('../order/add.do', entity);
    };
    //修改
    this.update = function (entity) {
        return $http.post('../order/update.do', entity);
    };
    //删除
    this.dele = function (ids) {
        return $http.get('../order/delete.do?ids=' + ids);
    };
    //搜索
    this.search = function (page, rows, searchEntity) {
        return $http.post('../order/search.do?page=' + page + "&rows=" + rows, searchEntity);
    };
    this.findOrderItemByOrderId = function (orderId) {
        return $http.post('../order/findOrderItemsByOrderId.do?orderId=' + orderId);
    };
    this.updateStatusStr = function (out_order_no, status) {
        return $http.post('../order/updateStatus.do?out_order_no=' + out_order_no + '&status=' + status);
    };

    //读取列表数据绑定到表单中
    this.showName=function(){
        return $http.get('../login/name.do');
    }

    this.sendEmail=function(orderId){
        return $http.get('../user/sendEmail.do?orderId='+orderId);
    }


    //查询订单详情
    this.findOrderListByOutOrderNo=function(outTradeNo){
        return $http.get('../order/findOrderListByOutOrderNo.do?outTradeNo='+outTradeNo);
    }


});