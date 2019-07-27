package util;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

import com.pinyougou.pojo.TbOrder;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

public class ExcelOperateUtil {

    //此工具类有待改进,也不算工具类,适应性不强= = wjk
     public static void createExcel(List<TbOrder> list) throws UnsupportedEncodingException {

         HttpServletReq request=new HttpServletReq();
         request.setCharacterEncoding("UTF-8");
         HttpServletRes response=new HttpServletRes();
         response.setCharacterEncoding("UTF-8");
         response.setContentType("application/x-download");


         UUID uuid = UUID.randomUUID();
         String s = uuid.toString().replaceAll("-", "");
         String fileName = "订单查询"+uuid+".xlsx";
         fileName = URLEncoder.encode(fileName, "UTF-8");
         response.addHeader("Content-Disposition", "attachment;filename=" + fileName);


        // 创建一个Excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 创建一个工作表



         HSSFSheet sheet = workbook.createSheet("订单信息表");
         sheet.setDefaultRowHeight((short) (2 * 256));//设置行高
         sheet.setColumnWidth(0, 4000);//设置列宽
         sheet.setColumnWidth(1,5500);
         sheet.setColumnWidth(2,5500);
         sheet.setColumnWidth(3,5500);
         sheet.setColumnWidth(11,3500);
         sheet.setColumnWidth(12,4000);
         sheet.setColumnWidth(13,3000);
         sheet.setColumnWidth(13,3000);

         // 添加表头行
         HSSFRow hssfRow = sheet.createRow(0);
         // 设置单元格格式居中
         HSSFCellStyle cellStyle = workbook.createCellStyle();
         cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);


         HSSFFont font = workbook.createFont();
         font.setFontName("宋体");
         font.setFontHeightInPoints((short) 16);



        // 添加表头内容
        HSSFCell headCell = hssfRow.createCell(0);
        headCell.setCellValue("订单编号");
        headCell.setCellStyle(cellStyle);

        headCell = hssfRow.createCell(1);
        headCell.setCellValue("用户账号");
        headCell.setCellStyle(cellStyle);

        headCell = hssfRow.createCell(2);
        headCell.setCellValue("收货人");
        headCell.setCellStyle(cellStyle);

        headCell = hssfRow.createCell(3);
        headCell.setCellValue("手机号");
        headCell.setCellStyle(cellStyle);

        headCell = hssfRow.createCell(4);
        headCell.setCellValue("订单金额");
        headCell.setCellStyle(cellStyle);

        headCell = hssfRow.createCell(5);
        headCell.setCellValue("支付方式");
        headCell.setCellStyle(cellStyle);

        headCell = hssfRow.createCell(6);
        headCell.setCellValue("订单来源");
        headCell.setCellStyle(cellStyle);

        headCell = hssfRow.createCell(7);
        headCell.setCellValue("订单状态");
        headCell.setCellStyle(cellStyle);



        HSSFCell cell=null;

        // 添加数据内容
        for (int i = 0; i < list.size(); i++) {
            hssfRow = sheet.createRow((int) i + 1);
            TbOrder order = list.get(i);

            // 创建单元格，并设置值
            cell = hssfRow.createCell(0);
            cell.setCellValue(order.getOrderId());
            cell.setCellStyle(cellStyle);

            cell = hssfRow.createCell(1);
            cell.setCellValue(order.getUserId());
            cell.setCellStyle(cellStyle);

            cell = hssfRow.createCell(2);
            cell.setCellValue(order.getReceiver());
            cell.setCellStyle(cellStyle);

            cell = hssfRow.createCell(3);
            cell.setCellValue(order.getReceiverMobile());
            cell.setCellStyle(cellStyle);

            cell = hssfRow.createCell(4);
            cell.setCellValue(String.valueOf(order.getPayment()));
            cell.setCellStyle(cellStyle);

            cell = hssfRow.createCell(5);
            String paymentType = order.getPaymentType();
            if ("1".equals(paymentType)){
                paymentType="在线支付";
            }
            if ("2".equals(paymentType)){
                paymentType="货到付款";
            }
            if ("3".equals(paymentType)){
                paymentType="支付宝付款";
            }
            cell.setCellValue(paymentType);
            cell.setCellStyle(cellStyle);


            cell = hssfRow.createCell(6);
            String sourceType = order.getSourceType();
            if ("1".equals(sourceType)){
                sourceType="app端";
            }
            if ("2".equals(sourceType)){
                sourceType="pc端";
            }
            if ("3".equals(sourceType)){
                sourceType="M端";
            }
            if ("4".equals(sourceType)){
                sourceType="微信端";
            }
            if ("5".equals(sourceType)){
                sourceType="手机qq端";
            }
            cell.setCellValue(sourceType);
            cell.setCellStyle(cellStyle);

            
            cell = hssfRow.createCell(7);
            String status = order.getStatus();
            if ("1".equals(status)){
                status="未付款";
            }
            if ("2".equals(status)){
                status="已付款";
            }
            if ("3".equals(status)){
                status="未发货";
            }
            if ("4".equals(status)){
                status="已发货";
            }
            if ("5".equals(status)){
                status="交易成功";
            }
            if ("6".equals(status)){
                status="交易关闭";
            }
            if ("7".equals(status)){
                status="待评价";
            }
            if ("8".equals(status)){
                status="退货申请";
            }
            if ("9".equals(status)){
                status="退货完成";
            }
            cell.setCellValue(status);
            cell.setCellStyle(cellStyle);

        }


 /*        // 保存Excel文件
        try {
            OutputStream outputStream = response.getOutputStream();
            System.out.println(workbook.getBytes());
            workbook.write(outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

         // 保存Excel文件
         try {
             UUID uuid1 = UUID.randomUUID();
             String s1 = uuid1.toString().replaceAll("-", "");
             OutputStream outputStream = new FileOutputStream("D:/orderList"+s1+".xls");
             workbook.write(outputStream);
             outputStream.close();
         } catch (Exception e) {
             e.printStackTrace();
         }

    }

}
