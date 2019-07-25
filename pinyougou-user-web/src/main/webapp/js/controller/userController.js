 //控制层 
app.controller('userController' ,function($scope,$controller   ,userService){	
	
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
                       location.href="login"
                   }else{
                       alert(response.message);
                   }
               }
           );
        }

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



    $scope.sendCode2=function(){
        var code=$("#msgcode").val();
        var phone=$("#inputphone").val();
        alert(phone);

        if(phone==null || phone==""){
            alert("请填写手机号码");
            return ;
        }

        userService.sendCode(phone,code).success(
            function(response){
                alert(response.message);
            }
        );
    };


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
        );
    };






});	
