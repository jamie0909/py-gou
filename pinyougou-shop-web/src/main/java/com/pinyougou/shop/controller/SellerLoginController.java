package com.pinyougou.shop.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取登陆商家名
 * @author whk
 */
@RestController
@RequestMapping("/login")
public class SellerLoginController {

    @RequestMapping("/name")
    public Map name(){
        Map map = new HashMap();
        String loginName = SecurityContextHolder.getContext().getAuthentication().getName();
        map.put("loginName",loginName);
        return map;
    }

}
