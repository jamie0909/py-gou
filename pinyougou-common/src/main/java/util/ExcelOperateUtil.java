package util;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import com.pinyougou.pojo.TbOrder;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ExcelOperateUtil {

    //此工具类有待改进,也不算工具类,适应性不强 = _= wjk
     public static void createExcel(List<TbOrder> list) {
        // 创建一个Excel文件
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 创建一个工作表
        HSSFSheet sheet = workbook.createSheet("订单信息表");
        // 添加表头行
        HSSFRow hssfRow = sheet.createRow(0);
        // 设置单元格格式居中
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);

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
            cell.setCellValue(order.getPaymentType());
            cell.setCellStyle(cellStyle);

            cell = hssfRow.createCell(6);
            cell.setCellValue(order.getSourceType());
            cell.setCellStyle(cellStyle);

            cell = hssfRow.createCell(7);
            cell.setCellValue(order.getStatus());
            cell.setCellStyle(cellStyle);

        }

        // 保存Excel文件
        try {
            OutputStream outputStream = new FileOutputStream("D:/orderList.xls");
            workbook.write(outputStream);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
