package com.pinyougou.user.controller;
import java.util.List;

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
        //user.setUsername(name);
        System.out.println(user.getProvinces());
        System.out.println(user.getUsername());
        System.out.println(user.getBirthday());
        System.out.println(user.getJob());


        List<TbUser> userList = userService.findOne(name);
        TbUser user1 = userList.get(0);
        user1.setNickName(user.getNickName());
        user1.setSex(user.getSex());
        user1.setProvinces(user.getProvinces());
        user1.setCitiy(user.getCitiy());
        user1.setArea(user.getArea());
        user1.setBirthday(user.getBirthday());
        user1.setJob(user.getJob());

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
	public List<TbUser> findOne(){

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println(name);
        return userService.findOne(name);
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
		
		if(!PhoneFormatCheckUtils.isPhoneLegal(phone)){
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
	
}
