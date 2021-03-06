package net.faxuan.interfaceframework.util;

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
 * Excel操作工具类
 */
public class ExcelUtil {
    /**
     * 将excel表中数据导入到数据库中
     * @param excelPath Excel文件路径
     * @param ip   数据库地址
     * @param port 端口
     * @param databaseName 数据库名
     * @param userName  数据库用户名
     * @param passWord  数据库密码
     * @return 是否导入成功
     *
     * 调用示例：importExcelData("d:/uc数据导入.xslx","127.0.0.1","3306","uc","root","123456")
     */
    public static boolean importExcelData(String excelPath,String ip,String port,String databaseName,String userName,String passWord) {
        Mysql sql = new Mysql();
        String url = "jdbc:mysql://" + ip + ":" + port + "/" + databaseName + "?useSSL=true&characterEncoding=UTF-8";
        sql.setUrl(url);
        sql.setUserName(userName);
        sql.setPassWord(passWord);
        sql.connSQL();
        boolean insertResult = false;
        StringBuilder employeeInfoBuilder = null;
        try {
            FileInputStream excelFileInputStream = new FileInputStream(excelPath);
            XSSFWorkbook workbook = new XSSFWorkbook(excelFileInputStream);
            excelFileInputStream.close();
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                employeeInfoBuilder = new StringBuilder();
                XSSFRow row = sheet.getRow(rowIndex);
                if (row == null) {
                    continue;
                }
                XSSFCell systemIDCell = row.getCell(0); // 系统ID
//                XSSFCell methodNameCell = row.getCell(1); // 方法名
                XSSFCell preconditionCell = row.getCell(1); // 前置条件
                XSSFCell URLCell = row.getCell(2); // 测试url
                XSSFCell paramCell = row.getCell(3); // 参数
                XSSFCell expectedResultCell = row.getCell(4); // 预期结果
                XSSFCell descriptionCell = row.getCell(5); // 测试功能描述
                //拼接已获取数据
                employeeInfoBuilder.append("测试数据 --> ")
                        .append("系统ID : ").append(systemIDCell.getNumericCellValue())
                        .append(" , 测试url : ").append(URLCell.getStringCellValue())
                        .append(" , 参数 : ").append(paramCell.getStringCellValue());
                boolean preconditionIsNull = false;
                boolean descriptionIsNull = false;
                try {
                    preconditionCell.toString();
                    //拼接已获取数据
                    employeeInfoBuilder.append(" , 前置条件 : ").append(preconditionCell.getStringCellValue());
                } catch (NullPointerException npe) {
                    preconditionIsNull = true;
                    //拼接已获取数据
                    employeeInfoBuilder.append(" , 前置条件 : ").append("无");
                }

                employeeInfoBuilder.append(" , 预期结果 : ").append(expectedResultCell.getStringCellValue());
                try {
                    descriptionCell.toString();
                    employeeInfoBuilder.append(" , 测试功能描述 : ").append(descriptionCell.getStringCellValue());

                } catch (NullPointerException npe) {
                    descriptionIsNull = true;
                    employeeInfoBuilder.append(" , 测试功能描述 : ").append("无");
                }


                //执行数据库插入
                int caseID = 0;
                ResultSet rs1 = sql.selectSQL("SELECT id FROM cases WHERE `sys_id`='" + systemIDCell + "';");
                while (rs1.next()) {
                    caseID = rs1.getInt("id");
                }
//                rs1.last();
//                if (rs1.getRow() <= 0) {
//                    sql.insertSQL("INSERT INTO cases(name) VALUES('" + classNameCell + "');");
//                }
//                rs1 = sql.selectSQL("SELECT id FROM methods WHERE `case_id`=(SELECT id FROM cases WHERE `name`='" + classNameCell + "') AND method_name='" + methodNameCell + "';");
//                rs1.last();
//                if (rs1.getRow() <= 0) {
//                    sql.insertSQL("INSERT INTO methods(case_id,method_name) VALUES((SELECT id FROM cases WHERE name='" + classNameCell + "'),'" + methodNameCell + "');");
//                }
                rs1 = sql.selectSQL("SELECT id FROM datas WHERE `case_id`=" + caseID + " AND url='" + URLCell + "' AND params='" + paramCell + "' AND description='" + descriptionCell + "';");
                rs1.last();
                if (rs1.getRow() <= 0) {
                    if (preconditionIsNull) {
                        sql.insertSQL("INSERT INTO datas(case_id,url,params,expected_results,description) VALUES(" + caseID + ",'" + URLCell + "','" + paramCell +"','" + expectedResultCell +"','" + descriptionCell +"');");
                        System.out.println(employeeInfoBuilder.toString()+ "----->导入成功");
                    } else {
                        sql.insertSQL("INSERT INTO datas(case_id,url,params,expected_results,description,precondition) VALUES(" + caseID + ",'" + URLCell + "','" + paramCell +"','" + expectedResultCell +"','" + descriptionCell +"','" + preconditionCell +"');");
                        System.out.println(employeeInfoBuilder.toString()+ "----->导入成功");
                    }
                } else System.err.println(employeeInfoBuilder.toString()+ "----->数据已存在");
            }
            workbook.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            insertResult = true;
            sql.deconnSQL();
        }

        return insertResult;
    }
}
