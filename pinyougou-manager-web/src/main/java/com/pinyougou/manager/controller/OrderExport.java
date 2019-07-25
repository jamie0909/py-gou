package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pojo.TbOrder;
import entity.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author whk
 * <p>
 * 将订单导出为Excel
 */
@RestController
@RequestMapping("/export")
public class OrderExport {

    @Reference
    private OrderService orderService;


    /**
     * 导出订单
     */
    @RequestMapping("/orderToExcel")
    public Result orderToExcel(int page, int rows) {

        TbOrder order = new TbOrder();
        try {
            orderService.exportOrderToExcel(order, page, rows);
            return new Result(true, "导出成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "导出失败");
        }
    }

}
