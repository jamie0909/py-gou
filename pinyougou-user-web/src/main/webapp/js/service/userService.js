//服务层
app.service('userService',function($http){
	    	
	//读取列表数据绑定到表单中
	this.findAll=function(){
		return $http.get('../user/findAll.do');		
	};
	//分页 
	this.findPage=function(page,rows){
		return $http.get('../user/findPage.do?page='+page+'&rows='+rows);
	};
	//查询实体
	this.findOne=function(id){
		return $http.get('../user/findOne.do?id='+id);
	};
	//增加 
	this.add=function(entity,smscode){
		return  $http.post('../user/add.do?smscode='+smscode,entity );
	};
	//修改 
	this.update=function(entity){
		return  $http.post('../user/update.do',entity );
	};
	//删除
	this.dele=function(ids){
		return $http.get('../user/delete.do?ids='+ids);
	};
	//搜索
	this.search=function(page,rows,searchEntity){
		return $http.post('../user/search.do?page='+page+"&rows="+rows, searchEntity);
	};



	//发送验证码
	this.sendCode=function(phone,code){
		return $http.get('../user/sendCode.do?phone='+phone);
	}


	/***
	* @Description: 修改用户的登陆密码
	* @Param:
	* @return:
	* @Author: WangRui
	* @Date: 2019/7/25
	*/

    this.updatePassword=function(entity){
        return  $http.post('../user/updatePassword.do',entity );
    }



    this.findUser=function(){
        return  $http.post('../user/findUser.do');
    }


    this.checkCode=function (code,phone) {
        return $http.get('../user/checkCode.do?code='+code+'&phone='+phone);
    }

    //查询所有省市区
    this.findPrv=function () {
        return $http.get('../user/findProvince.do');
    }
	//查询所有城市
    this.findCity=function (provinceId) {
        return $http.get('../user/findCity.do?provinceId='+provinceId);
    }
	//查询所有区
    this.findArea=function (cityId) {
        return $http.get('../user/findArea.do?cityId='+cityId);
    }

	
});
