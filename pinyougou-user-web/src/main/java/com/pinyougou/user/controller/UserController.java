package com.pinyougou.user.controller;
import java.util.List;


import com.pinyougou.pojo.TbAreas;
import com.pinyougou.pojo.TbCities;
import com.pinyougou.pojo.TbProvinces;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbUser;
import com.pinyougou.user.service.UserService;

import entity.PageResult;
import entity.Result;
import util.PhoneFormatCheckUtils;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Reference
	private UserService userService;

	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbUser> findAll(){			
		return userService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return userService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param user
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbUser user,String smscode){
		
		//校验验证码是否正确
		boolean checkSmsCode = userService.checkSmsCode(user.getPhone(), smscode);
		if(!checkSmsCode){
			return new Result(false, "验证码不正确！");
		}
		
		
		try {
			userService.add(user);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param user
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbUser user){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        List<TbUser> users = userService.findOne(name);
        TbUser user1= users.get(0);
        user1.setJob(user.getJob());
        user1.setBirthday(user.getBirthday());
        user1.setArea(user.getArea());
        user1.setCitiy(user.getCitiy());
        user1.setProvinces(user.getProvinces());
        user1.setSex(user.getSex());
        user1.setNickName(user.getNickName());
        user1.setHeadPic(user.getHeadPic());
        try {
			userService.update(user1);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param
	 * @return
	 */
	@RequestMapping("/findOne")
	public TbUser findOne(){
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
        TbUser user = userService.findUser(name);
        return user;
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			userService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbUser user, int page, int rows  ){
		return userService.findPage(user, page, rows);		
	}
	
	@RequestMapping("/sendCode")
	public Result sendCode(String phone){
		System.out.println("phone的格式"+phone);
		if(!PhoneFormatCheckUtils.isPhoneLegal(phone)){
			System.out.println("phonede根式"+phone);
			return new Result(false, "手机格式不正确");
		}
		
		try {
			userService.createSmsCode(phone);
			return new Result(true, "验证码发送成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "验证码发送失败");
		}
	}



	/***
	* @Description: 修改用户的密码
	* @Param: [user]
	* @return: entity.Result
	* @Author: WangRui
	* @Date: 2019/7/25
	*/
	@RequestMapping("/updatePassword")
	public Result updatePassword(@RequestBody TbUser user ){
		//获取到登录名
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		System.out.println("username"+username);
		if(!username.equals(user.getUsername())){
			return new Result(false, "用户名输入不正确");
		}
		try {
			userService.updatePassword(user,username);
			return new Result(true, "密码修改成功,请重新登陆");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}


	/***
	* @Description: 修改手机号码
	* @Param: [phone]
	* @return: entity.Result
	* @Author: WangRui
	* @Date: 2019/7/25
	*/ 
	@RequestMapping("/updatePhone")
	public  Result updatePhone(String phone){
		//获取到登录名
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		try {
			userService.updatePhone(username,phone);
			return new Result(true, "手机号修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "手机号修改失败");
		}
	}


	/***
	* @Description: 登陆额用户
	* @Param: []
	* @return: User
	* @Author: WangRui
	* @Date: 2019/7/25
	*/
	@RequestMapping("/findUser.do")
	public TbUser findUser(){
		String username= SecurityContextHolder.getContext().getAuthentication().getName();
		return userService.findUser(username);
	}



	/***
	* @Description: 检查验证码功能
	* @Param: [code, phone]
	* @return: entity.Result
	* @Author: WangRui
	* @Date: 2019/7/25
	*/ 
	@RequestMapping("/checkCode.do")
	public Result checkCode(String phone,String code){
		System.out.println("code"+code+"phone"+phone);
		boolean isTrue = userService.checkSmsCode(phone,code);
		System.out.println("bolean"+isTrue);
		if(isTrue) {
			return this.updatePhone(phone);
		}else {
			return new Result(false, "输入错误,请重新验证");
		}
	}

    /**
     * ==================================================================
     * 查询省市区三级联动↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↑↑↑↑↑↑↑↑
     */
	@RequestMapping("/findProvince.do")
	public List<TbProvinces> findProvince(){
	    return userService.findProvince();
    }

    @RequestMapping("/findCity.do")
    public List<TbCities> findCity(String provinceId){
	    return userService.findCity(provinceId);
    }

    @RequestMapping("/findArea.do")
    public List<TbAreas> findArea(String cityId){
        return userService.findArea(cityId);
    }
    /**
     * ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
     * ==================================================================
     */
}
