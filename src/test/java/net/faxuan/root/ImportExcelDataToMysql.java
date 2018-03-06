package net.faxuan.root;

import net.faxuan.interfaceframework.util.ExcelUtil;
import org.testng.annotations.Test;

/**
 * 将指定Excel文件中的测试数据导入到mysql中
 * Created by song on 2018/3/6.
 */
public class ImportExcelDataToMysql {
    @Test
    public void insertData() {
        ExcelUtil.importExcelData("D:/uc.xlsx","127.0.0.1","3306","uuu","root","123123");
    }
}
