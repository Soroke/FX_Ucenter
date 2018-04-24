package net.faxuan.sale;

import org.apache.poi.xssf.usermodel.*;
import org.testng.annotations.Test;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static net.faxuan.interfaceframework.core.Http.post;

/**
 * 后台单独流程
 * Created by song on 2018/4/24.
 */
public class Backstage extends Init{
    /**
     * 后台流程2
     */
    String domainName = "墨脱县司法局" + dateaNumber;
    String payment = "宋仁坤" + dateaNumber;
    //正发票ID
    String invoiceIDz = "";
    //正发票编号
    String invoiceIndex = "";
    //负发票ID
    String invoiceIDf = "";
    //正发票和到账的匹配关系序号
    String ZFP_RELATED_INDEX = "";
    //到账ID
    String billID = "";

    @Test(description = "添加正发票",priority = 201)
    public void addInvoice() {
        post("http://salegl.t.faxuan.net/saless/service/invoiceService!doAdd.do",
                "areaCode=542624;domainName=" + domainName + ";payment=" + payment + ";remark=发票备注;" +
                        "invoiceIndex=;invoiceType=2;domainNameInfo=" + domainName + ";taxpayersCode=" +
                        dateaNumber + ";address=西藏自治区林芝地区墨脱县东波路71号;telPhone=0894652316;" +
                        "bankName=招商银行;bankCode=622776848223;price=3000;number=10;total=30000;" +
                        "titleType=4_0;mailAddress=西藏自治区林芝地区墨脱县东波路71号;contactName=宋仁坤;" +
                        "mobilePhone=18513111927;email=;operator=" + saleUserInfo.getOperator()).body("code=200;msg=添加成功！");
        //获取发票ID
        ResultSet resultSet = mysql.selectSQL("SELECT ID,INVOICE_INDEX FROM `f_invoice` WHERE DOMAIN_NAME='" + domainName + "' AND MONEY=30000;");
        try {
            while (resultSet.next()) {
                invoiceIDz = resultSet.getString("ID");
                invoiceIndex = resultSet.getString("INVOICE_INDEX");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test(description = "添加负发票",priority = 202)
    public void addNegativeInvoice() {
        post("http://salegl.t.faxuan.net/saless/service/invoiceService!doAdd.do",
                "areaCode=542624;domainName=" + domainName + ";payment=" + payment + ";remark=发票备注;" +
                        "invoiceIndex=;invoiceType=2;domainNameInfo=" + domainName + ";taxpayersCode=" +
                        dateaNumber + ";address=西藏自治区林芝地区墨脱县东波路71号;telPhone=0894652316;" +
                        "bankName=招商银行;bankCode=622776848223;price=3000;number=10;total=-30000;" +
                        "titleType=4_0;mailAddress=西藏自治区林芝地区墨脱县东波路71号;contactName=宋仁坤;" +
                        "mobilePhone=18513111927;email=;operator=" + saleUserInfo.getOperator()).body("code=200;msg=添加成功！");
        //获取发票ID
        ResultSet resultSet = mysql.selectSQL("SELECT ID FROM `f_invoice` WHERE DOMAIN_NAME='" + domainName + "' AND MONEY=-30000;");
        try {
            while (resultSet.next()) {
                invoiceIDf = resultSet.getString("ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test(description = "导出新建的发票并修改excel文件后再导入",priority = 203)
    public void exportInvoice() {
        /**
         * 导出excel文件到当前类所在文件夹
         */
        String dir = getDirPath(this.getClass());
        exportURLFile("http://salegl.t.faxuan.net/saless/service/invoiceService!doExport.do?" +
                        "areaCode=000000&domainName=" + domainName + "&roleId=1&start=0&length=6000&loadingId=invoiceexport6406434621801345",
                dir,domainName + ".xlsx");

        /*
         * 读取excel文件，并在指定列写入数据
         */
        String filePath = dir + "\\" + domainName + ".xlsx";
        File file = new File(filePath);
        XSSFWorkbook xWorkbook = null;
        try {
            xWorkbook = new XSSFWorkbook(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            System.out.println("未找到文件:" + filePath);
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("文件:" + filePath + ",读取失败。");
            e.printStackTrace();
        }

        FileOutputStream out = null;
        XSSFSheet sheet = xWorkbook.getSheet("Sheet1");
        //创建一个为文本的样式
        XSSFCellStyle contextstyle =xWorkbook.createCellStyle();
        XSSFDataFormat format = xWorkbook.createDataFormat();
        contextstyle.setDataFormat(format.getFormat("@"));
        try {
            // 获得表头行对象
            XSSFRow titleRow = sheet.getRow(0);
            if(titleRow!=null){
                XSSFRow newRow=sheet.getRow(3);
                XSSFCell cell = newRow.createCell(5);
                cell.setCellStyle(contextstyle);
                cell.setCellValue("FPHA" + dateaNumber);
                cell = newRow.createCell(6);
                cell.setCellStyle(contextstyle);
                cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(date));
                cell = newRow.createCell(29);
                cell.setCellValue("法宣");

                newRow=sheet.getRow(4);
                cell = newRow.createCell(5);
                cell.setCellStyle(contextstyle);
                cell.setCellValue("FPHA" + dateaNumber);
                cell = newRow.createCell(6);
                cell.setCellStyle(contextstyle);
                cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(date));
                cell = newRow.createCell(29);
                cell.setCellValue("法宣");
            }
            out = new FileOutputStream(filePath);
            xWorkbook.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * 导入excel文件
         */
        Map<Object,Object> params = new HashMap<>();
        params.put("Content-Disposition: form-data; name='invoiceUploadFile'; filename=' " + domainName + ".xlsx'\n" +
                "Content-Type: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet","<file>");
        post("http://salegl.t.faxuan.net/saless/service/invoiceService!doUpload.do?classify=1","invoiceUploadFile",file,params).body("code=200");

        /**
         * 删除导出文件
         */
        if (file.exists()) {
            file.delete();
        }
    }

    @Test(description = "认领正发票",priority = 204)
    public void claimInvoice() {

        post("http://salegl.t.faxuan.net/saless/service/invoiceService!doClaimInvoice.do","id=" + invoiceIDz + ";operator=" + saleUserInfo.getOperator())
                .body("code=200;msg=操作成功！");

        //等待4秒钟，再去数据库查询发票和到账的匹配关系序号
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //获取正发票和到账的匹配关系序号
        ResultSet resultSet = mysql.selectSQL("SELECT RELATED_INDEX FROM `f_invoice` WHERE DOMAIN_NAME='" + domainName + "' AND MONEY=30000;");
        try {
            while (resultSet.next()) {
                ZFP_RELATED_INDEX = resultSet.getString("RELATED_INDEX");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test(description = "添加到账",priority = 205)
    public void addBill() {
        post("http://salegl.t.faxuan.net/saless/service/billService!doAdd.do",
                "payment=" + payment + ";accountType=0_0;money=30000;accountDateStart=" +
                        new SimpleDateFormat("yyyy-MM-dd").format(date) +";" +
                        "remark=autoTest后台流程2-到账备注;operator=" + saleUserInfo.getOperator())
            .body("code=200;msg=添加成功！");
    }

    @Test(description = "完善到账",priority = 206)
    public void updateBill() {

        ResultSet resultSet = mysql.selectSQL("SELECT ID FROM `f_bill` WHERE PAYMENT='" + payment + "';");
        try {
            while (resultSet.next()) {
                billID = resultSet.getString("ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String subscribeCode = post("http://salegl.t.faxuan.net/saless/service/billService!doSubId.do","id=" + billID + ";operator=" + saleUserInfo.getOperator()).getBodyValue("subId");
        post("http://salegl.t.faxuan.net/saless/service/billService!doUpdateSub.do","id=" + billID + ";subscribeId=" + subscribeCode + ";" +
                "areaCode=542624;domainName=" + payment + ";telPhone=18513111927;email=songrenkun@faxuan.net;remark=autoTest后台流程2-到账备注;businessType=3_0;operator="+ saleUserInfo.getOperator())
                .body("code=200;msg=操作成功！");
    }

    @Test(description = "认领到账",priority = 207)
    public void claimBill() {
        post("http://salegl.t.faxuan.net/saless/service/billService!claimBill.do",
                "id=" + billID + ";operator=" + saleUserInfo.getOperator()).body("code=200;msg=操作成功！");
    }

    @Test(description = "发票匹配到账",priority = 208)
    public void relatedBill() {
        post("http://salegl.t.faxuan.net/saless/service/invoiceService!doRelatedBill.do",
                "id=" + invoiceIDz + ";ids=" + billID + ",;claimAdmin=" + saleUserInfo.getOperator())
                .body("code=200;msg=操作成功！");
    }

    @Test(description = "成功销账",priority = 209)
    public void writeOff() {
        post("http://salegl.t.faxuan.net/saless/service/invoiceService!writeOff.do",
                "id=" + invoiceIDz + ";claimAdmin" + saleUserInfo.getOperator()).body("code=200;msg=操作成功！");
    }

    @Test(description = "取消销账",priority = 210)
    public void cancleCharge() {
        post("http://salegl.t.faxuan.net/saless/service/chargeManageService!cancleCharge.do",
                "id=" + billID + ";userAccount=" + saleUserInfo.getAccount()).body("code=200;msg=取消成功");
    }

    @Test(description = "取消发票和销账的匹配",priority = 211)
    public void removeRelated() {
        post("http://salegl.t.faxuan.net/saless/service/invoiceService!removeRelated.do",
                "id=" + invoiceIDz + ";claimAdmin=" + saleUserInfo.getAccount()).body("code=200;msg=操作成功！");
    }

    @Test(description = "取消发票认领",priority = 212)
    public void cancelClaim() {
        post("http://salegl.t.faxuan.net/saless/service/invoiceService!cancelClaim.do",
                "invoiceIndex=" + invoiceIndex + ";claimAdmin=" + saleUserInfo.getAccount()).body("code=200;msg=操作成功！");
    }

    @Test(description = "发票邮寄",priority = 213)
    public void invoiceMailing() {
        post("http://salegl.t.faxuan.net/saless/service/invoiceService!doPost.do",
                "contactName=宋仁坤;expressCode=107998935964;expressCompany=2_5;" +
                        "ids=" + invoiceIDz + ";mailAddress=西藏自治区林芝地区墨脱县东波路71号;" +
                        "mobilePhone=18513111927;operator=" + saleUserInfo.getOperator() + ";" +
                        "remark=autoTest后台流程2发票邮寄备注").body("code=200;msg=操作成功！");
    }

    @Test(description = "发票作废",priority = 214)
    public void toVoidInvoice() {
        post("http://salegl.t.faxuan.net/saless/service/invoiceService!doInvoiceStatus.do",
                "id=" + invoiceIDz + ";problemId=" + invoiceIDf + ";operator=" + saleUserInfo.getOperator())
                .body("code=200;msg=操作成功！");
    }

    @Test(description = "发票恢复正常",priority = 215)
    public void recoveryInvoice() {
        post("http://salegl.t.faxuan.net/saless/service/problemInvoiceService!doRestoreInvoice.do",
                "id=" + invoiceIDz + ";operator=" + saleUserInfo.getOperator()).body("code=200;msg=操作成功！");
    }

    @Test(description = "到账取消认领",priority = 216)
    public void cancelClaimBill() {
        post("http://salegl.t.faxuan.net/saless/service/billService!cancelClaim.do",
                "id=" + billID + ";claimAdmin=" + saleUserInfo.getOperator()).body("code=200;msg=操作成功！");
    }

    @Test(description = "到账退款",priority = 217)
    public void returnBill() {
        post("http://salegl.t.faxuan.net/saless/service/billService!returnBill.do",
                "id=" + billID + ";operator=" + saleUserInfo.getOperator()).body("code=200;msg=操作成功！");
    }

}
