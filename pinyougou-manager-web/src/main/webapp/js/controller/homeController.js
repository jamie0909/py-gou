//控制层
app.controller('homeController' ,function($scope,$controller,$http,homeService) {

    $scope.findByTime=function(){
         //alert($scope.startTime);
        // alert($scope.entity.startTime);
        homeService.findByTime($scope.entity).success(
            function(response){
                $scope.list=response;
            }
        );
    }
});