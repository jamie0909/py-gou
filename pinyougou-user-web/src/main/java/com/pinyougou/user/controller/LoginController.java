package com.pinyougou.user.controller;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.user.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {
	@Reference
	private UserService userService;

	@RequestMapping("/name")
	public Map showName(){
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		Map map=new HashMap();
		map.put("loginName", name);
		return map;		
	}


	
}
