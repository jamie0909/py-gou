 //控制层 
app.controller('userController' ,function($scope,$controller , $filter,userService,$interval,$timeout,$location,uploadService){
	
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
                       location.href="logout/cas"
                   }else{
                       alert(response.message);

                   }
               }
           );
        };






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

        userService.findUser().success(
            function(response){
                $scope.entity=response;


                if (phone==$scope.entity.phone){
                    alert("手机号码不能与原手机号码一样")
        }else{
                    userService.checkCode(code,phone).success(
                        function (response) {

                            if(response.success){
                                location.href="home-setting-address-complete.html";
                            }else{
                                alert("执行了")
                                alert(response.message);
                            }
                        })
                }
            });

          /* userService.checkCode(code,phone).success(
            function (response) {

                if(response.success){
                    location.href="home-setting-address-complete.html";
                }else{
                    alert("执行了")
                    alert(response.message);
                }
            })*/



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


              });
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


                    userService.sendCode($scope.entity.phone).success(
                        function(response){
                            alert(response.message);
                        }
                    );

            };




    //第二个验证码发送
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
        var phone=$("#inputphone").val();

        if(phone==null || phone==""){
            alert("请填写手机号码");
            return ;
        }


        userService.sendCode(phone).success(
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

    $scope.prvList={};
    $scope.cityList={}
    $scope.areaList={};


    // 查询省级列表
    $scope.findProvince = function(){
        userService.findPrv().success(function(response){
            $scope.prvList = response;
        });
    }

    // 查询市列表
    $scope.$watch("entity.provinces",function(newValue,oldValue){
        userService.findCity(newValue).success(function(response){
            $scope.cityList = response;
            $scope.areaList={};

        });
    });

    // 查询区域列表
    $scope.$watch("entity.citiy",function(newValue,oldValue){
        userService.findArea(newValue).success(function(response){
            $scope.areaList = response;
        });
    });

    /**
     * 查询用户信息，用于回显
     * by：马
     */
    $scope.findOne=function(){
        userService.findOne().success(
            function(response){
                $scope.entity= response;
                if ($scope.entity.birthday!=null){
                    $scope.birthday=$scope.entity.birthday.split(" ")[0]+"";
                }
            }
        );
    }
    /**
     * 修改个人信息
     * by:马
     */
    $scope.updateInfo=function () {
        if($scope.birthday!=null){
            $scope.entity.birthday=$scope.birthday;
            $scope.today = new  Date();
            $scope.timeString = $filter('date')($scope.today, 'yyyyMMdd');
            var now=parseInt($scope.timeString);
            $scope.date =$scope.birthday.split("-")[0]+
                $scope.birthday.split("-")[1]+
                $scope.birthday.split("-")[2];
            var time=parseInt($scope.date);
            if(time>now){
                alert("您的出生日期不合法哦~")
                return;
            }

            userService.update($scope.entity).success(
                function (response) {
                    alert(response.message);
                });
        }else {
            alert("生日不能为空");
        }

    }

    /**
     * 创建一个职业数组
     * @type {string[]}
     */
    $scope.names = ["程序员", "测试", "UI设计","架构师"];

    $scope.uploadFile = function(){
        // 调用uploadService的方法完成文件的上传
        uploadService.uploadFile().success(function(response){
            if(response.flag){
                // 获得url
                $scope.entity.headPic =  response.message;
            }else{
                alert(response.message);
            }
        });
    }




});
