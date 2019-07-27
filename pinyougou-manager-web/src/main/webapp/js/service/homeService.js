//服务层
app.service("homeService",function($http){
    this.findByTime = function(entity){
        return $http.post("../home/findBuTime.do",entity);
    }


});