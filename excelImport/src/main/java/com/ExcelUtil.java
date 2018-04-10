package com;

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
 * Created by song on 18/3/26.
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
     * @return 返回状态码
     *          0：导入成功
     *          1：数据库链接失败
     *          2：excel文件地址不正确
     *          3：系统ID为空
     *          4：测试URL为空
     *          5：参数为空
     *          6：预期结果为空
     *          7: 操作数据库报错
     *          8：接口类型为空
     *
     * 调用示例：importExcelData("d:/uc数据导入.xslx","127.0.0.1","3306","uc","root","123456")
     */
    public static int[] importExcelData(String excelPath,String ip,String port,String databaseName,String userName,String passWord) {
        int[] i = new int[2];
        i[0] = 2;
        i[1] = 0;
        Mysql mysql = new Mysql();
        String url = "jdbc:mysql://" + ip + ":" + port + "/" + databaseName + "?useSSL=true&characterEncoding=UTF-8";
        mysql.setUrl(url);
        mysql.setUserName(userName);
        mysql.setPassWord(passWord);
        boolean b = mysql.connSQL();
        if (!b) {
            i[1] = 1;
            return i;
        }
        //读取数据拼接
        StringBuilder employeeInfoBuilder = null;

        try {
            FileInputStream excelFileInputStream = new FileInputStream(excelPath);
            XSSFWorkbook workbook = new XSSFWorkbook(excelFileInputStream);
            excelFileInputStream.close();
            XSSFSheet sheet = workbook.getSheetAt(0);
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                i[0] = rowIndex + 1;
                employeeInfoBuilder = new StringBuilder();
                XSSFRow row = sheet.getRow(rowIndex);
                if (row == null) {
                    continue;
                }
                XSSFCell systemIDCell = row.getCell(0); // 系统ID
                XSSFCell preconditionCell = row.getCell(1); // 前置条件
                XSSFCell URLCell = row.getCell(2); // 测试url
                XSSFCell paramCell = row.getCell(3); // 参数
                XSSFCell expectedResultCell = row.getCell(4); // 预期结果
                XSSFCell descriptionCell = row.getCell(5); // 测试功能描述
                XSSFCell apiTypeCell = row.getCell(6); //接口类型

                /**
                 * 检查获取数据是否为空
                 */
                boolean systemIDIsNull = false;
                boolean preconditionIsNull = false;
                boolean urlIsNull = false;
                boolean paramIsNull = false;
                boolean expectedResultIsNull = false;
                boolean descriptionIsNull = false;
                boolean apiTypeIsNull = false;
                employeeInfoBuilder.append("测试数据 --> ");
                //系统ID
                try {
                    systemIDCell.toString();
                    employeeInfoBuilder.append("系统ID : ").append(systemIDCell.getNumericCellValue());
                } catch (NullPointerException npe) {
                    systemIDIsNull = true;
                    i[1] = 3;
                    break;
                }
                //前置条件
                try {
                    preconditionCell.toString();
                    employeeInfoBuilder.append(" , 前置条件 : ").append(preconditionCell.getStringCellValue());
                } catch (NullPointerException npe) {
                    preconditionIsNull = true;
                    employeeInfoBuilder.append(" , 前置条件 : ").append("无");
                }
                //测试地址url
                try {
                    URLCell.toString();
                    employeeInfoBuilder.append(" , 测试url : ").append(URLCell.getStringCellValue());
                } catch (NullPointerException npe) {
                    urlIsNull = true;
                    i[1] = 4;
                    break;
                }
                //参数
                try {
                    paramCell.toString();
                    employeeInfoBuilder.append(" , 参数 : ").append(paramCell.getStringCellValue());
                } catch (NullPointerException npe) {
                    paramIsNull = true;
                    i[1] = 5;
                    break;
                }
                //预期结果
                try {
                    expectedResultCell.toString();
                    employeeInfoBuilder.append(" , 预期结果 : ").append(expectedResultCell.getStringCellValue());
                } catch (NullPointerException npe) {
                    expectedResultIsNull = true;
                    i[1] = 6;
                    break;
                }
                //描述
                try {
                    descriptionCell.toString();
                    employeeInfoBuilder.append(" , 测试功能描述 : ").append(descriptionCell.getStringCellValue());
                } catch (NullPointerException npe) {
                    descriptionIsNull = true;
                    employeeInfoBuilder.append(" , 测试功能描述 : ").append("无");
                }
                //接口类型
                try {
                    apiTypeCell.toString();
                    employeeInfoBuilder.append(" , 接口类型 : ").append(apiTypeCell.getStringCellValue());
                } catch (NullPointerException npe) {
                    apiTypeIsNull = true;
                    i[1] = 8;
                    break;
                }




                //执行数据库插入
                int caseID = 0;
                ResultSet rs1 = mysql.selectSQL("SELECT id FROM cases WHERE `sys_id`='" + systemIDCell + "';");
                while (rs1.next()) {
                    caseID = rs1.getInt("id");
                }
                rs1 = mysql.selectSQL("SELECT id FROM datas WHERE `case_id`=" + caseID + " AND url='" + URLCell + "' AND params='" + paramCell + "' AND expected_results='" + expectedResultCell + "';");
                rs1.last();
                String sql = "";
                if (rs1.getRow() <= 0) {
                    if (preconditionIsNull && descriptionIsNull) {
                        sql = "INSERT INTO datas(case_id,api_type,url,params,expected_results) VALUES(" + caseID + ",'" + apiTypeCell + "','" + URLCell + "','" + paramCell +"','" + expectedResultCell +"');";

                        System.out.println(employeeInfoBuilder.toString()+ "----->导入成功");
                    } else if (preconditionIsNull && !descriptionIsNull){
                        sql = "INSERT INTO datas(case_id,api_type,url,params,expected_results,description) VALUES(" + caseID + ",'" + apiTypeCell + "','" + URLCell + "','" + paramCell +"','" + expectedResultCell +"','" + descriptionCell +"');";

                        System.out.println(employeeInfoBuilder.toString()+ "----->导入成功");
                    }else if (!preconditionIsNull && descriptionIsNull){
                        sql = "INSERT INTO datas(case_id,api_type,url,params,expected_results,precondition) VALUES(" + caseID + ",'" + apiTypeCell + "','" + URLCell + "','" + paramCell +"','" + expectedResultCell +"','"  + preconditionCell +"');";

                        System.out.println(employeeInfoBuilder.toString()+ "----->导入成功");
                    }else if (!preconditionIsNull && !descriptionIsNull){
                        sql = "INSERT INTO datas(case_id,api_type,url,params,expected_results,description,precondition) VALUES(" + caseID + ",'" + apiTypeCell + "','" + URLCell + "','" + paramCell +"','" + expectedResultCell +"','" + descriptionCell +"','" + preconditionCell +"');";

                        System.out.println(employeeInfoBuilder.toString()+ "----->导入成功");
                    }
                } else System.err.println(employeeInfoBuilder.toString()+ "----->数据已存在");
                mysql.insertSQL(sql);
            }
            workbook.close();
        } catch (FileNotFoundException e) {
            i[1] = 2;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            i[1]=7;
            e.printStackTrace();
        } finally {
            mysql.deconnSQL();
        }

        return i;
    }
}
