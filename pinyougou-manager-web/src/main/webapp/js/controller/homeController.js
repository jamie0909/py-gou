//控制层
app.controller('homeController' ,function($scope,$controller,$http,homeService) {


    $scope.findByTime=function(){
        alert("111111")
        alert($scope.entity)
        alert("22222")
        homeService.findByTime($scope.entity).success(
            function(response){
                $scope.list=response;
            }
        );
    }



});