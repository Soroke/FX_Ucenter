package net.faxuan;

import com.interfacetest.core.TestCase;
import com.interfacetest.util.ExcelUtil;
import com.interfacetest.util.Mysql;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Created by song on 2018/3/5.
 */
public class Test{
//    @org.testng.annotations.Test
//    public void t() {
//
//        StringBuilder employeeInfoBuilder = new StringBuilder();
//        Mysql mysql = new Mysql();
//        mysql.setUrl("jdbc:mysql://127.0.0.1:3306/uc?characterEncoding=UTF-8");
//        mysql.setUserName("root");
//        mysql.setPassWord("123123");
//        mysql.connSQL();
//        try {
//            FileInputStream excelFileInputStream = new FileInputStream("D:/uc数据导入.xlsx");
//            XSSFWorkbook workbook = new XSSFWorkbook(excelFileInputStream);
//            excelFileInputStream.close();
//            XSSFSheet sheet = workbook.getSheetAt(0);
//            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
//                XSSFRow row = sheet.getRow(rowIndex);
//                if (row == null) {
//                    continue;
//                }
//                XSSFCell classNameCell = row.getCell(0); // 类名列
//                XSSFCell descriptionCell = row.getCell(1); // 测试功能描述列
//                XSSFCell resultCell = row.getCell(2); // 预期结果列
//                XSSFCell urlCell = row.getCell(3); // 测试地址列
//                XSSFCell paramCell = row.getCell(4); // 测试参数列
//                ResultSet rs1 = mysql.selectSQL("SELECT id FROM cases WHERE `name`='" + classNameCell + "';");
//                rs1.last();
//                if (rs1.getRow() <= 0) {
//                    mysql.insertSQL("INSERT INTO cases(name,function) VALUES('" + classNameCell + "','" + descriptionCell + "');");
//                }
//                mysql.insertSQL("INSERT INTO groups(case_id,result_correct) VALUES((SELECT id FROM cases WHERE name='" + classNameCell + "'),'" + resultCell + "');");
//                mysql.insertSQL("INSERT INTO datas(group_id,step,data) VALUES((SELECT id FROM groups WHERE case_id=(SELECT id FROM cases WHERE name='" + classNameCell + "') AND result_correct='" + resultCell + "'),1,'" + urlCell +"');");
//                mysql.insertSQL("INSERT INTO datas(group_id,step,data) VALUES((SELECT id FROM groups WHERE case_id=(SELECT id FROM cases WHERE name='" + classNameCell + "') AND result_correct='" + resultCell + "'),2,'" + paramCell +"');");
//
//                employeeInfoBuilder.append("测试数据 --> ")
//                        .append("类名 : ").append(classNameCell.getStringCellValue())
//                        .append(" , 测试功能描述 : ").append(descriptionCell.getStringCellValue())
//                        .append(" , 预期结果 : ").append(resultCell.getStringCellValue())
//                        .append(" , 测试地址 : ").append(urlCell.getStringCellValue())
//                        .append(" , 测试参数 : ").append(paramCell.getStringCellValue());
//                System.out.println(employeeInfoBuilder.toString());
//            }
//            workbook.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            mysql.deconnSQL();
//        }
//
//
//    }

    @org.testng.annotations.Test
    public void insertData() {
        ExcelUtil.importExcelData("D:/uc数据导入模板.xlsx","127.0.0.1","3306","uc","root","123123");
    }
}
