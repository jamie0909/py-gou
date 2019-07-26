//服务层
app.service("orderService",function($http){
    this.findAll = function(){
        return $http.get("../order/findAll.do");
    }

    this.findByPage = function(page,rows){
        return $http.get("../order/findByPage.do?page="+page+"&rows="+rows);
    }

    this.search = function(page,rows,searchEntity){
        return $http.post("../order/search.do?page="+page+"&rows="+rows,searchEntity);
    }

    this.dele = function(ids){
        return $http.get("../order/delete.do?ids="+ids);
    }

    this.deleOne = function(id){
        return $http.get("../order/deleteOne.do?id="+id);
    }

    //查询实体
    this.findOne=function(id){
        return $http.get('../order/findOne.do?id='+id);
    }


    this.updateStatus = function(id,status){
        return $http.get('../order/updateStatus.do?id='+id+"&status="+status);
    }
    //excel
    this.excelOperate = function(searchEntity){
        return $http.post("../order/excel.do",searchEntity);
    }

});