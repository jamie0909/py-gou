package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbOrder;
import com.pinyougou.order.service.OrderService;
import com.pinyougou.pojo.TbSalesreturn;
import com.pinyougou.pojo.group.Order;
import entity.PageResult;
import entity.Result;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.ExcelOperateUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    @RequestMapping("/findAll")
    public List<TbOrder> findAll() {
        List<TbOrder> orderList = orderService.findAll();
        System.out.println(orderList);
        return orderList;
    }

    /**
     * 返回全部列表
     *
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(int page, int rows) {
        return orderService.findPage(page, rows);
    }

    /**
     * 条件分页查询
     *
     * @param order
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(@RequestBody TbOrder order, int page, int rows) {
        return orderService.findByPage(order, page, rows);
    }

    /**
     * 勾选删除
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Long[] ids) {
        try {
            orderService.delete(ids);
            return new Result(true, "删除成功!");

        } catch (Exception e) {

            e.printStackTrace();
            return new Result(false, "删除失败!");
        }
    }

    /**
     * 单点删除
     *
     * @param id
     * @return
     */
    @RequestMapping("/deleteOne")
    public Result deleteOne(Long id) {
        try {
            orderService.deleteOne(id);
            return new Result(true, "删除成功!");

        } catch (Exception e) {

            e.printStackTrace();
            return new Result(false, "删除失败!");
        }

    }

    /**
     * 查询组合实体  wjk
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public Order findOne(Long id) {
        System.out.println(id);
        return orderService.findOne(id);
    }


    /**
     * 更新状态     wjk
     *
     * @param id
     * @param status
     * @return
     */
    @RequestMapping("/updateStatus")
    public Result updateStatus(Long id, String status) {
        try {
            orderService.updateStatus(id, (Integer.parseInt(status) + 1) + "");
            return new Result(true, "订单状态修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "订单状态修改失败");
        }
    }

    /**
     * 导出excel报表
     *
     * @param order
     * @return
     */
    @RequestMapping("/excel222")//不用的接口
    public Result excel(@RequestBody TbOrder order) {

        try {


            orderService.excel(order);


            return new Result(true, "导出Excel成功");
        } catch (Exception e) {

            e.printStackTrace();
            return new Result(false, "导出Excel失败");
        }
    }


    /**
     * 查询退货商品详情   wjk
     *
     * @param id
     * @return
     */
    @RequestMapping("/findReturnOne")
    public TbSalesreturn findReturnOne(String id) {

        return orderService.findReturnOne(id);
    }


    /**
     * 导出excel(下载到客户端本地) whk
     *
     * @param receiver
     * @param request
     * @param response
     */
    @RequestMapping("/excel")
    public void exportExcel(String receiver, String receiverMobile, Long orderId, String status, String sourceType, HttpServletRequest request, HttpServletResponse response) {

        //根据条件查询出订单集合
        //将查询的条件封装到order中
        List<TbOrder> list = null;
        try {
            String ecode_receiver = URLDecoder.decode(receiver, "UTF-8");
            TbOrder order = new TbOrder();
            order.setReceiver(ecode_receiver);
            order.setReceiverMobile(receiverMobile);
            order.setOrderId(orderId);
            order.setStatus(status);
            order.setSourceType(sourceType);
            list = orderService.excel(order);

        } catch (UnsupportedEncodingException e) {
            System.out.println("解码异常,重试");
        }

        //excel标题
        String[] title = {"订单编号", "用户账号", "收货人", "手机号", "订单金额", "支付方式", "订单来源", "订单状态"};
        //excel文件名
        String fileName = "订单信息表" + System.currentTimeMillis() + ".xls";
        //sheet名
        String sheetName = "订单信息";
        //二维数组用于装要导出的数据
        String[][] content = new String[list.size()][title.length];
        for (int i = 0; i < list.size(); i++) {
            TbOrder obj = list.get(i);
            content[i][0] = obj.getOrderId() + "";
            content[i][1] = obj.getUserId() + "";
            content[i][2] = obj.getReceiver() + "";
            content[i][3] = obj.getReceiverMobile() + "";
            content[i][4] = obj.getPayment() + "";
            if ("1".equals(obj.getPaymentType() + "")) {
                content[i][5] = "在线支付";
            } else if ("2".equals(obj.getPaymentType() + "")) {
                content[i][5] = "货到付款";
            } else {
                content[i][5] = "其他";
            }

            if ("2".equals(obj.getSourceType() + "")) {
                content[i][6] = "pc端";
            } else {
                content[i][6] = "其他";
            }

            if ("1".equals(obj.getStatus() + "")) {
                content[i][7] = "未付款";
            } else if ("2".equals(obj.getStatus() + "")) {
                content[i][7] = "已付款";
            } else if ("3".equals(obj.getStatus() + "")) {
                content[i][7] = "未发货";
            } else if ("4".equals(obj.getStatus() + "")) {
                content[i][7] = "已发货";
            } else if ("5".equals(obj.getStatus() + "")) {
                content[i][7] = "交易成功";
            } else if ("6".equals(obj.getStatus() + "")) {
                content[i][7] = "交易关闭";
            } else if ("7".equals(obj.getStatus() + "")) {
                content[i][7] = "待评价";
            } else {
                content[i][7] = "其他";
            }
        }
        //创建HSSFWorkbook
        HSSFWorkbook wb = ExcelOperateUtil.getHSSFWorkbook(sheetName, title, content, null);

        //响应到客户端
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //发送响应流方法
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(), "ISO8859-1");
            } catch (Exception e) {
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
