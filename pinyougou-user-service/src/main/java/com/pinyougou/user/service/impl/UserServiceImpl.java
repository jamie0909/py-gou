package com.pinyougou.user.service.impl;
import java.util.*;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;


import com.pinyougou.mapper.TbAreasMapper;
import com.pinyougou.mapper.TbCitiesMapper;
import com.pinyougou.mapper.TbProvincesMapper;
import com.pinyougou.pojo.*;
import entity.Result;
import com.pinyougou.mapper.TbOrderMapper;
import com.pinyougou.mapper.TbSellerMapper;
import com.pinyougou.pojo.TbOrder;
import com.pinyougou.pojo.TbSeller;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbUserMapper;
import com.pinyougou.pojo.TbUserExample.Criteria;
import com.pinyougou.user.service.UserService;

import entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
public class UserServiceImpl implements UserService {


	@Autowired
	private TbUserMapper userMapper;


	@Autowired
	private TbOrderMapper orderMapper;

	@Autowired
	TbSellerMapper sellerMapper;

	@Override
	public TbUser findUser(String name) {
		TbUserExample example = new TbUserExample();
        Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(name);
		List<TbUser> users = userMapper.selectByExample(example);
		return users.get(0);
	}


    @Autowired
    private TbProvincesMapper provincesMapper;

    @Autowired
    private TbCitiesMapper citiesMapper;

    @Autowired
    private TbAreasMapper areasMapper;

    /**
     * 查询所有省份
     *
     * @return
     */
    @Override
    public List<TbProvinces> findProvince() {

        return provincesMapper.selectByExample(null);
    }

    /**
     * 查询所有市
     *
     * @param
     * @return
     */
    @Override
    public List<TbCities> findCity(String provinceId) {
        TbCitiesExample example=new TbCitiesExample();
        TbCitiesExample.Criteria criteria = example.createCriteria();
        criteria.andProvinceidEqualTo(provinceId);
        return citiesMapper.selectByExample(example);
    }
    /**
     * 查询所有区县
     *
     * @param
     * @return
     */
    @Override
    public List<TbAreas> findArea(String cityId) {
        TbAreasExample example = new TbAreasExample();
        TbAreasExample.Criteria criteria = example.createCriteria();
        criteria.andCityidEqualTo(cityId);
        return areasMapper.selectByExample(example);
    }
    /***
	 * @Description: 修改用户的密码
	 * @Param: [user]
	 * @return: void
	 * @Author: WangRui
	 * @Date: 2019/7/25
	 */

	@Override
	public Result updatePassword(TbUser user, String username) {


  		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		System.out.println("username"+username);

		List<TbUser> tbUsers = userMapper.selectByExample(example);

		String ordPassword = tbUsers.get(0).getPassword();
		System.out.println("旧密码:"+ordPassword);
		System.out.println("未加密的新密码"+user.getPassword());

		String newPassword = DigestUtils.md5Hex(user.getPassword());
		System.out.println("newPassword:"+newPassword);
        System.out.println(!ordPassword.equals(newPassword));
		if (!ordPassword.equals(newPassword)){
			tbUsers.get(0).setPassword(newPassword);
			tbUsers.get(0).setUpdated(new Date());
			userMapper.updateByPrimaryKey(tbUsers.get(0));
            System.out.println("前后的密码不相同");
			return new Result(true,"密码修改成功");
		}else{
			return new Result(false,"新旧密码相同,请重新输入");
		}


	}



	/***
	* @Description: 修改手机的号码
	* @Param: [username, phone]
	* @return: void
	* @Author: WangRui
	* @Date: 2019/7/25
	*/
	@Override
	public void updatePhone(String username, String phone) {
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		System.out.println("username"+username);

		List<TbUser> tbUsers = userMapper.selectByExample(example);
		TbUser user = tbUsers.get(0);
		user.setPhone(phone);
		userMapper.updateByPrimaryKey(user);

	}


	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbUser> findAll() {
		return userMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbUser> page=   (Page<TbUser>) userMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbUser user) {
		
		user.setCreated(new Date());//用户注册时间
		user.setUpdated(new Date());//修改时间
		user.setSourceType("1");//注册来源
		user.setPassword( DigestUtils.md5Hex(user.getPassword()));//密码加密
		
		userMapper.insert(user);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbUser user){

	    userMapper.updateByPrimaryKey(user);
	}

/*	@Override
	public List<TbUser> findOne(String name) {
		return null;*/
/*
	public TbUser findOne(Long id) {
		return userMapper.selectByPrimaryKey(id);
	}
*/

	/*	TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(name);
		List<TbUser> users = userMapper.selectByExample(example);
		return users;
	}*/
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */


	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			userMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbUser user, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbUserExample example=new TbUserExample();
		Criteria criteria = example.createCriteria();
		
		if(user!=null){			
						if(user.getUsername()!=null && user.getUsername().length()>0){
				criteria.andUsernameLike("%"+user.getUsername()+"%");
			}
			if(user.getPassword()!=null && user.getPassword().length()>0){
				criteria.andPasswordLike("%"+user.getPassword()+"%");
			}
			if(user.getPhone()!=null && user.getPhone().length()>0){
				criteria.andPhoneLike("%"+user.getPhone()+"%");
			}
			if(user.getEmail()!=null && user.getEmail().length()>0){
				criteria.andEmailLike("%"+user.getEmail()+"%");
			}
			if(user.getSourceType()!=null && user.getSourceType().length()>0){
				criteria.andSourceTypeLike("%"+user.getSourceType()+"%");
			}
			if(user.getNickName()!=null && user.getNickName().length()>0){
				criteria.andNickNameLike("%"+user.getNickName()+"%");
			}
			if(user.getName()!=null && user.getName().length()>0){
				criteria.andNameLike("%"+user.getName()+"%");
			}
			if(user.getStatus()!=null && user.getStatus().length()>0){
				criteria.andStatusLike("%"+user.getStatus()+"%");
			}
			if(user.getHeadPic()!=null && user.getHeadPic().length()>0){
				criteria.andHeadPicLike("%"+user.getHeadPic()+"%");
			}
			if(user.getQq()!=null && user.getQq().length()>0){
				criteria.andQqLike("%"+user.getQq()+"%");
			}
			if(user.getIsMobileCheck()!=null && user.getIsMobileCheck().length()>0){
				criteria.andIsMobileCheckLike("%"+user.getIsMobileCheck()+"%");
			}
			if(user.getIsEmailCheck()!=null && user.getIsEmailCheck().length()>0){
				criteria.andIsEmailCheckLike("%"+user.getIsEmailCheck()+"%");
			}
			if(user.getSex()!=null && user.getSex().length()>0){
				criteria.andSexLike("%"+user.getSex()+"%");
			}
	
		}
		
		Page<TbUser> page= (Page<TbUser>)userMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Autowired
	private RedisTemplate redisTemplate;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	
	@Autowired
	private Destination smsDestination;
	
	@Value("${template_code}")
	private String template_code;
	
	@Value("${sign_name}")
	private String sign_name;
	
	@Override
	public void createSmsCode(final String phone) {
		//1.生成一个6位随机数（验证码）
		final String smscode=  (long)(Math.random()*1000000)+"";
		System.out.println("验证码："+smscode);
		
		//2.将验证码放入redis
		redisTemplate.boundHashOps("smscode").put(phone, smscode);
		//3.将短信内容发送给activeMQ
		
		jmsTemplate.send(smsDestination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				MapMessage message = session.createMapMessage();
				message.setString("mobile", phone);//手机号
				message.setString("template_code", template_code);//验证码
				message.setString("sign_name", sign_name);//签名
				Map map=new HashMap();

				map.put("number", smscode);

				map.put("code", smscode);

				message.setString("param", JSON.toJSONString(map));
				return message;
			}
		});

		try {
			//验证码有效期为一分钟
			System.out.println("验证码调度器开始运行");
			final Timer timer = new Timer();
			timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    redisTemplate.boundHashOps("smscode").delete(phone);
					System.out.println("一分钟已经到了,验证码从缓存中被删除");
					timer.cancel();
                }
            },60*1000);
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	@Override
	public boolean checkSmsCode(String phone, String code) {

		System.out.println("你好");
		System.out.println("你你你"+phone+"code"+code);
		String systemcode= (String) redisTemplate.boundHashOps("smscode").get(phone);

		if(systemcode==null){
			return false;
		}
		if(!systemcode.equals(code)){
			return false;
		}
		
		return true;
	}

	@Override
	public String getEmailFromOrderId(String orderId) {
		TbOrder order = orderMapper.selectByPrimaryKey(Long.parseLong(orderId));
		String sellerId = order.getSellerId();
		TbSeller tbSeller = sellerMapper.selectByPrimaryKey(sellerId);
		return tbSeller.getEmail();
	}







	
}
