app.controller('indexController',function($scope,loginService){

    //获取商家登陆人姓名
    $scope.showLoginName = function () {

        loginService.loginName().success(

            function (response) {

                $scope.sellerLoginName = response.loginName;
            }
        );
    }

});