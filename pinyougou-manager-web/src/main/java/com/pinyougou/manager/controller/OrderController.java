package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbOrder;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pojo.group.Order;
import entity.PageResult;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    @RequestMapping("/findAll")
    public List<TbOrder> findAll(){
        List<TbOrder> orderList = orderService.findAll();
        System.out.println(orderList);
        return orderList;
    }

    /**
     * 返回全部列表
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(int page, int rows){
        return orderService.findPage(page, rows);
    }

    /**
     * 条件分页查询
     * @param order
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(@RequestBody TbOrder order, int page, int rows){
        return orderService.findByPage(order, page, rows);
    }

    /**
     * 勾选删除
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Long[] ids){
        try{
            orderService.delete(ids);
            return new Result(true,"删除成功!");

        }catch(Exception e){

            e.printStackTrace();
            return new Result(false,"删除失败!");
        }
    }

    @RequestMapping("/deleteOne")
    public Result deleteOne(Long id){
        try{
            orderService.deleteOne(id);
            return new Result(true,"删除成功!");

        }catch(Exception e){

            e.printStackTrace();
            return new Result(false,"删除失败!");
        }

    }
    @RequestMapping("/findOne")
    public Order findOne(Long id){
        System.out.println(id);
        return orderService.findOne(id);
    }


    @RequestMapping("/updateStatus")
    public Result updateStatus(Long id,String status){
        try {
            orderService.updateStatus(id,"4");
            return new Result(true,"订单状态修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"订单状态修改失败");
        }
    }


}
