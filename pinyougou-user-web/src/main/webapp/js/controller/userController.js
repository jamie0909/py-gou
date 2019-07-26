 //控制层 
app.controller('userController' ,function($scope,$controller ,userService,$interval,$timeout){
	
	//注册用户
	$scope.reg=function(){
		
		//比较两次输入的密码是否一致
		if($scope.password!=$scope.entity.password){
			alert("两次输入密码不一致，请重新输入");
			$scope.entity.password="";
			$scope.password="";
			return ;			
		}
		//新增
		userService.add($scope.entity,$scope.smscode).success(
			function(response){
				alert(response.message);
			}		
		);
	}
	//发送验证码
	$scope.sendCode=function(){
		alert("执行了");
		if($scope.entity.phone==null || $scope.entity.phone==""){
			alert("请填写手机号码");
			return ;
		}
		
		userService.sendCode($scope.entity.phone).success(
			function(response){
				alert(response.message);
			}
		);		
	}


   /***
   * @Description: 修改用户的登陆密码
   * @Param:
   * @return:
   * @Author: WangRui
   * @Date: 2019/7/25
   */
    $scope.updatePassword=function(){

    //比较两次输入的密码是否一致
            if($scope.password!=$scope.entity.password){
                alert("两次输入密码不一致，请重新输入");
                $scope.entity.password="";
                $scope.password="";
                return ;
            }


           userService.updatePassword( $scope.entity ).success(
               function(response){
                   if(response.success){
                       //重新查询
                      // $scope.reloadList();


                       alert(response.message);
                       location.href="home-index.html"
                   }else{
                       alert(response.message);
                   }
               }
           );
        }





    //第一个下一步
    $scope.nextTOPhone=function (entity) {
        var code=$("#msgcode").val();
        var phone=$scope.entity.phone;
        userService.checkCode(code,phone).success(function (response) {
            if(response.success){
                location.href="home-setting-address-phone.html";

            }else{
                alert(response.message);
            }
        })
    }

    //第二个下一步

    $scope.endTOPhone=function () {
        var code=$("#msgcode").val();
        var phone=$("#inputphone").val();
        userService.checkCode(code,phone).success(function (response) {
            if(response.success){
                location.href="home-setting-address-complete.html";
            }else{
                alert(response.message);
            }
        })
    }
    /***
    * @Description: 登陆的用户
    * @Param: 
    * @return: 
    * @Author: WangRui
    * @Date: 2019/7/25
    */ 
      $scope.findUser=function(){
        userService.findUser().success(
            function(response){
                $scope.entity=response;
            }
        ) ;
     };




            $scope.timer = false;
            $scope.timeout = 60000;
            $scope.timerCount = $scope.timeout / 1000;
            $scope.text = "获取验证码";


              //第一个验证码发送
            $scope.sendCode = function(){

                $scope.showTimer = true;
                $scope.timer = true;
                $scope.text = "秒后重新获取";
                var counter = $interval(function(){
                    $scope.timerCount = $scope.timerCount - 1;
                }, 1000);
                $timeout(function(){
                    $scope.text = "获取验证码";
                    $scope.timer = false;
                    $interval.cancel(counter);
                    $scope.showTimer = false;
                    $scope.timerCount = $scope.timeout / 1000;
                }, $scope.timeout);
                //发送验证码


                    if($scope.entity.phone==null || $scope.entity.phone==""){
                        alert("请填写手机号码");
                        return ;
                    }

                    userService.sendCode($scope.entity.phone).success(
                        function(response){
                            alert(response.message);
                        }
                    );

            };




    //第一个验证码发送
    $scope.sendCode2 = function(){

        $scope.showTimer = true;
        $scope.timer = true;
        $scope.text = "秒后重新获取";
        var counter = $interval(function(){
            $scope.timerCount = $scope.timerCount - 1;
        }, 1000);
        $timeout(function(){
            $scope.text = "获取验证码";
            $scope.timer = false;
            $interval.cancel(counter);
            $scope.showTimer = false;
            $scope.timerCount = $scope.timeout / 1000;
        }, $scope.timeout);
        //发送验证码


        if($scope.entity.phone==null || $scope.entity.phone==""){
            alert("请填写手机号码");
            return ;
        }

        userService.sendCode($scope.entity.phone).success(
            function(response){
                alert(response.message);
            }
        );

    };



    
    /***
    * @Description: 
    * @Param: 
    * @return: 
    * @Author: WangRui
    * @Date: 2019/7/26
    */ 
    $scope.checkPassword = function(password){
  var lowTest1 = /^\d{1,6}$/; //纯数字
        var lowTest2 = /^[a-zA-Z]{1,6}$/; //纯字母
        var halfTest = /^[A-Za-z0-9]{1,6}$/;
        var halfTest2 = /^[A-Za-z0-9]{6,8}$/;
        var highTest = /^[A-Za-z0-9]{6,16}$/;
        if(lowTest1.test(password)|lowTest2.test(password)|halfTest.test(password)){
            $scope.safeMsg = "安全强度：*低";
            $scope.teColor = "red";
        }else if(halfTest2.test(password)){
            $scope.safeMsg = "安全强度：***中";
            $scope.teColor = "yellow";
        }else if(highTest.test(password)){

            $scope.safeMsg = "安全强度：*****高";
            $scope.teColor = "green";
            $scope.repassDis = "false";
        }else{
            $scope.safeMsg = "密码不符合要求";
            $scope.teColor = "red";
        }

    };







});	
