package net.faxuan.sale;

import net.faxuan.interfaceframework.util.JsonHelper;
import org.apache.poi.xssf.usermodel.*;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import static net.faxuan.interfaceframework.core.Http.*;

/**
 * 前后台交互
 * Created by song on 2018/4/17.
 */
public class Interactive extends Init {
    Product product1 = new Product();
    Product product2 = new Product();
    //发货单ID
    String invoiceID = "";
    //到账ID
    String billID = "";
    //发票单位name
    String invoiceDomainName = "聂荣县司法局" + dateaNumber ;
    //发票ID
    String FPID = "";
    //发票和到账的匹配关系序号
    String RELATED_INDEX = "";
    //汇款人名称
    String payment = "迪丽热巴" + dateaNumber;
    //汇款人手机号
    String telPhone = "18513111927";
    //首次申购 申购单ID
    String firstSubscribeId = "";
    //获取验证码
    String identifyCode = "";

    @BeforeClass
    public void beforeClass() {
        product1.setName("水浒传");
        product1.setBookCode("shz");
        product1.setIsbnCode("ISNSHZ");
        product1.setStorage("1101");
        product1.setUnitPrice(100);

        product2.setName("三国演义");
        product2.setBookCode("sgyy");
        product2.setIsbnCode("ISNSGYY");
        product2.setStorage("1102");
        product2.setUnitPrice(100);
        identifyCode = getIdentifyCode();
    }


    String firstPurchaseSubscribeCode = "";

    /**
     * 前端的
     */
    @Test(description = "首次申购",priority = 1)
    public void firstPurchase() {
        post("http://salemh.t.faxuan.net/saless/fservice/frontSubscribeService!doSaveFSubscribe.do","areaCode=542424;" +
                "domainName=" + invoiceDomainName + ";payment=" + payment + ";adminAccount=;telPhone=" + telPhone + ";email=songrenkun@faxuan.net;" +
                "payMoney=10000;peopleNum=10;payYear=5;isMinus=1;minusAccount=;credentials=20180418_a37e758c090943b9a7173a0d80c0cb12.jpg'semicolon';" +
                "remark=首次申购备注;chooseInvoiceType=0;filesPath=;validateCode=" + identifyCode + ";rid=" + rid).body("code=200;msg=操作成功");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ResultSet resultSet = mysql.selectSQL("SELECT SUBSCRIBE_CODE,ID FROM u_subscribe WHERE DOMAIN_NAME='" + invoiceDomainName + "' AND PAYMENT='" + payment + "' AND TEL_PHONE='" + telPhone + "';");
        try {
            while (resultSet.next()) {
                firstPurchaseSubscribeCode = resultSet.getString("SUBSCRIBE_CODE");
                firstSubscribeId = resultSet.getString("ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(getDBInfoCount("SELECT SUBSCRIBE_CODE FROM u_subscribe WHERE DOMAIN_NAME='" + invoiceDomainName + "' AND PAYMENT='" + payment + "' AND TEL_PHONE='" + telPhone + "';"),1);
    }

    @Test(description = "非首次申购",priority = 2)
    public void notFirstPurchase() {
        post("http://salemh.t.faxuan.net/saless/fservice/frontSubscribeService!doSaveFSubscribe.do","areaCode=371003;" +
                "domainName=;payment=" + payment + ";adminAccount=guanliyuan;telPhone=" + telPhone + ";email=songrenkun@faxuan.net;payMoney=10000;" +
                "peopleNum=10;payYear=3;isMinus=1;minusAccount=;credentials=20180417_07dbe1503d0441ca8e1d5ef29e254460.jpg'semicolon';" +
                "remark=非首次申购备注;chooseInvoiceType=0;invoiceList={'invoiceType':'0','domainName':'北京法宣在线科技有限公司" + dateaNumber + "'," +
                "'taxpayersCode':'NSRSBH" + dateaNumber + "','address':'北京市海淀区北三环西路32号恒润国际大厦','telPhone':'0102365245'," +
                "'bankName':'中国工商银行','bankCode':'622765216585','price':'1000'" +
                ",'number':'10','total':'10000','mobilePhone':'" + telPhone + "','contactName':'','mailAddress':'','email':'songrenkun@faxuan.net'};" +
                "filesPath=;validateCode=" + identifyCode + ";rid=" + rid).body("code=200;msg=操作成功");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(getDBInfoCount("SELECT SUBSCRIBE_CODE FROM u_subscribe WHERE DOMAIN_NAME='' AND PAYMENT='" + payment + "' AND TEL_PHONE='" + telPhone + "';") >= 1,true);
    }

    @Test(description = "申购查询",priority = 3)
    public void purchaseSearch() {
        post("http://salemh.t.faxuan.net/saless/fservice/frontSubscribeService!doGetFSubscribeList.do","payment=" + payment + ";telPhone=" + telPhone + ";validateCode=" + identifyCode + ";rid=" + rid)
                .body("code=200");
    }

    @Test(description = "补开发票",priority = 4)
    public void complementInvoicing() {
        post("http://salemh.t.faxuan.net/saless/fservice/frontSubscribeService!doSaveFInvoice.do",
                "subscribeCode=" + firstPurchaseSubscribeCode + ";chooseInvoiceType=0;invoiceList={'invoiceType':'0'," +
                        "'domainName':'" + invoiceDomainName + "','taxpayersCode':'NSRSBH" + dateaNumber + "'," +
                        "'address':'北京市海淀区北三环西路32号恒润国际大厦','telPhone':'0102365245','bankName':'中国工商银行'," +
                        "'bankCode':'622765216585','price':'1000','number':'10','total':'10000','mobilePhone':'18513111927'," +
                        "'contactName':'','mailAddress':'','email':'songrenkun@faxuan.net'};" +
                        "filesPath=;validateCode=" + identifyCode + ";rid=" + rid).body("code=200;msg=操作成功");
    }


    /**
     * 后台流程1
     */
    @Test(description = "测试登录",priority = 101)
    public void signin() {
        get("http://salegl.t.faxuan.net/saless/service/adminService!doAdminLogin.do","adminAccount=songrenkun;adminPassword=888888;code=;rid=f33041280298acc79549285da148b598;key=0;login=0").body("code=200;msg=登录成功");
    }

    @Test(description = "添加商品",priority = 102)
    public void addProduct() {
        get("http://salegl.t.faxuan.net/saless/service/goodsService!doAdd.do","goodsName=" + product1.getName() + "&bookCode=" + product1.getBookCode() + "&isbnCode=" + product1.getIsbnCode() + "&storage=" + product1.getStorage() + "&unitPrice=" + product1.getUnitPrice() + "&operator=" + saleUserInfo.getOperator()).body("code=200;msg=添加成功");
        get("http://salegl.t.faxuan.net/saless/service/goodsService!doAdd.do","goodsName=" + product2.getName() + "&bookCode=" + product2.getBookCode() + "&isbnCode=" + product2.getIsbnCode() + "&storage=" + product2.getStorage() + "&unitPrice=" + product2.getUnitPrice() + "&operator=" + saleUserInfo.getOperator()).body("code=200;msg=添加成功");
        //获取新加商品的ID
        product1.setId(JsonHelper.getValue(get("http://salegl.t.faxuan.net/saless/service/goodsService!doGetDetail.do","isbnCode=" + product1.getIsbnCode()).getBody(),"id").toString());
        product2.setId(JsonHelper.getValue(get("http://salegl.t.faxuan.net/saless/service/goodsService!doGetDetail.do","isbnCode=" + product2.getIsbnCode()).getBody(),"id").toString());
    }

    @Test(description = "商品入库",priority = 103)
    public void stockAdd() {
        post("http://salegl.t.faxuan.net/saless/service/stockService!doAdd.do","stockParam={'stockGood':[{'isbnCode':'" + product1.getIsbnCode() + "','costPrice':'50','number':'30','remark':'入库备注1'},{'isbnCode':'" + product2.getIsbnCode() + "','costPrice':'50','number':'30','remark':'入库备注2'}]};operator=" + saleUserInfo.getOperator()).body("code=200;msg=成功");
    }

    @Test(description = "添加发货单",priority = 104)
    public void addDelivery() {
        Double book1mayang = (double) product1.getUnitPrice()*6;
        Double book2mayang = (double)product2.getUnitPrice()*8;

        post("http://salegl.t.faxuan.net/saless/service/deliveryService!addAndEditDelivery.do",
                "deliveryParam={'delivery':{'id':'','number':'自动化添加发货单" + dateNow + "','purchaseDomain':'法宣在线'," +
                        "'makingData':'" + dateNow + ":835','areaCode':'542424','address':'西藏那曲地区聂荣县地址'," +
                        "'contact':'宋仁坤','telephone':'18513111927','postcode':'','billing':'1_1','opeanType':0," +
                        "'createOperator':'songrenkun','deliveryWays':'6_1','version':'','remark':''}," +
                        "'deliveryGood':[{'isbnCode':'" + product1.getIsbnCode() + "','number':'6','mayang':'" + book1mayang + "','goodId':'" + product1.getId() + "','shiyang':'" + (book1mayang-32) + "'," +
                        "'discount':'" + roundingValue(((book1mayang-32)/book1mayang),3) + "','remark':'备注1'},{'isbnCode':'" + product2.getIsbnCode() + "','number':'8','mayang':'" + book2mayang + "'," +
                        "'goodId':'" + product2.getId() + "','shiyang':'" + (book2mayang-45) + "','discount':'" + roundingValue(((book2mayang-45)/book2mayang),3) + "','remark':'备注2'}]}").body("code=200;msg=成功");

        //数据库获取发货单ID
        List<String> invoiceIDs = new ArrayList<String>();
        ResultSet resultSet = mysql.selectSQL("SELECT ID FROM `g_delivery` WHERE NUMBER LIKE '自动化添加发货单%';");
        try {
            while (resultSet.next()) {
                invoiceIDs.add(resultSet.getString("ID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        invoiceID = invoiceIDs.get(invoiceIDs.size()-1);
    }

    @Test(description = "审核发货单--不通过",priority = 105)
    public void invoiceReviewNotPassed() {
        get("http://salegl.t.faxuan.net/saless/service/deliveryService!doAdopt.do","id=" + invoiceID + ";approceSuggest=;approceStauts=2;version=0;approceExaminer=" + saleUserInfo.getName()).body("code=200;msg=审核成功");
    }

    @Test(description = "修改发货单",priority = 106)
    public void editInvoice() {
        Double book1mayang = (double) product1.getUnitPrice()*6;
        Double book2mayang = (double)product2.getUnitPrice()*8;

        post("http://salegl.t.faxuan.net/saless/service/deliveryService!addAndEditDelivery.do",
                "deliveryParam={'delivery':{'id':'" + invoiceID + "','number':'自动化添加发货单" + dateNow + "','purchaseDomain':'法宣在线'," +
                        "'makingData':'" + dateNow + ":835','areaCode':'542424','address':'西藏那曲地区聂荣县地址'," +
                        "'contact':'宋仁坤','telephone':'18513111927','postcode':'','billing':'1_1','opeanType':1," +
                        "'createOperator':'songrenkun','deliveryWays':'6_1','version':'1','remark':''}," +
                        "'deliveryGood':[{'isbnCode':'" + product1.getIsbnCode() + "','number':'6','mayang':'" + book1mayang + "','goodId':'" + product1.getId() + "','shiyang':'" + (book1mayang-32) + "'," +
                        "'discount':'" + roundingValue(((book1mayang-32)/book1mayang),3) + "','remark':'备注1'},{'isbnCode':'" + product2.getIsbnCode() + "','number':'8','mayang':'" + book2mayang + "'," +
                        "'goodId':'" + product2.getId() + "','shiyang':'" + (book2mayang-45) + "','discount':'" + roundingValue(((book2mayang-45)/book2mayang),3) + "','remark':'备注2'}]}").body("code=200;msg=成功");
    }

    @Test(description = "审核发货单--通过",priority = 107)
    public void invoiceReviewPassed() {
        get("http://salegl.t.faxuan.net/saless/service/deliveryService!doAdopt.do","id=" + invoiceID + ";approceSuggest=;approceStauts=3;version=2;approceExaminer=" + saleUserInfo.getName()).body("code=200;msg=审核成功");
    }

    @Test(description = "删除发货单,清理测试数据",priority = 108)
    public void delInvoice() {
        mysql.insertSQL("DELETE FROM g_delivery WHERE NUMBER LIKE '自动化添加发货单%';");
        mysql.insertSQL("DELETE FROM m_approve_record WHERE MODULE_ID=" + invoiceID + ";");
    }

    @Test(description = "删除商品",priority = 109)
    public void delGoods() {
        post("http://salegl.t.faxuan.net/saless/service/goodsService!doDelete.do","isbnCode=" + product1.getIsbnCode()).body("code=200;msg=删除成功!");
        post("http://salegl.t.faxuan.net/saless/service/goodsService!doDelete.do","isbnCode=" + product2.getIsbnCode()).body("code=200;msg=删除成功!");
    }

    @Test(description = "添加到账",priority = 110)
    public void addBill() {
        post("http://salegl.t.faxuan.net/saless/service/billService!doAdd.do","payment=" + payment + ";" +
                "accountType=0_1;money=9999;accountDateStart=" +
                new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ";remark=到账备注;" +
                "operator=" + saleUserInfo.getOperator()).body("code=200;msg=添加成功！");
    }

    @Test(description = "完善到账",priority = 111)
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
                "areaCode=542424;domainName=" + payment + ";telPhone=" + telPhone + ";email=songrenkun@faxuan.net;remark=到账备注;businessType=3_0;operator="+ saleUserInfo.getOperator())
                .body("code=200;msg=操作成功！");
    }

    @Test(description = "匹配到账",priority = 112)
    public void matchingBill() {
        post("http://salegl.t.faxuan.net/saless/service/subscribeService!doMatchingBill.do","subscribeId=" + firstSubscribeId +
                ";areaCode=542424;ids=" + billID + ";operator=" + saleUserInfo.getOperator()).body("code=200;msg=操作成功！");
    }

    @Test(description = "申请发票",priority = 113)
    public void applyInvoice() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        post("http://salegl.t.faxuan.net/saless/service/billService!doApplyInvoice.do","id=" +
                billID + ";domainName=" + invoiceDomainName + ";" +
                "subscribeId=" + firstSubscribeId + ";claimAdmin=songrenkun").body("code=200;msg=操作成功！");
    }

    @Test(description = "导出发票为excel，修改excel并添加发票号、开票日期、开票单位；导入excel",priority = 114)
    public void doExportAndInport() {
        /**
         * 导出excel文件到当前类所在文件夹
         */
        String dir = getDirPath(this.getClass());
        exportURLFile("http://salegl.t.faxuan.net/saless/service/invoiceService!doExport.do?" +
                "areaCode=000000&domainName=" + invoiceDomainName + "&roleId=1&start=0&length=6000&loadingId=invoiceexport6406434621801345",
                dir,invoiceDomainName + ".xlsx");

        /*
         * 读取excel文件，并在指定列写入数据
         */
        String filePath = dir + "\\" + invoiceDomainName + ".xlsx";
        File file = new File(filePath);
        XSSFWorkbook xWorkbook = null;
        try {
            xWorkbook = new XSSFWorkbook(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            System.err.println("未找到文件:" + filePath);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("文件:" + filePath + ",读取失败。");
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
                cell.setCellValue("SG4G" + dateaNumber);
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
        params.put("Content-Disposition: form-data; name='invoiceUploadFile'; filename=' " + invoiceDomainName + ".xlsx'\n" +
                "Content-Type: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet","<file>");
        post("http://salegl.t.faxuan.net/saless/service/invoiceService!doUpload.do?classify=1","invoiceUploadFile",file,params).body("code=200");

        /**
         * 清除导出文件
         */
        if (file.exists()) {
            file.delete();
        }
    }

    @Test(description = "发票认领",priority = 115)
    public void claimInvoice() {
        //获取发票ID
        ResultSet resultSet = mysql.selectSQL("SELECT ID FROM `f_invoice` WHERE PAYMENT='" + payment + "';");
        try {
            while (resultSet.next()) {
                FPID = resultSet.getString("ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        post("http://salegl.t.faxuan.net/saless/service/invoiceService!doClaimInvoice.do","id=" + FPID + ";operator=" + saleUserInfo.getOperator())
            .body("code=200;msg=操作成功！");

        //等待4秒钟，再去数据库查询发票和到账的匹配关系序号
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //获取发票和到账的匹配关系序号
        resultSet = mysql.selectSQL("SELECT RELATED_INDEX FROM `f_invoice` WHERE PAYMENT='" + payment + "';");
        try {
            while (resultSet.next()) {
                RELATED_INDEX = resultSet.getString("RELATED_INDEX");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test(description = "销账不成功触发审核",priority = 116)
    public void writeOffNo() {
        post("http://salegl.t.faxuan.net/saless/service/invoiceService!writeOff.do",
                "id=" + FPID + ";claimAdmin=" + saleUserInfo.getOperator())
                .body("code=200;msg=该信息已经提交审核！");
    }

    @Test(description = "销账审批不通过",priority = 117)
    public void approveCharge() {
        post("http://salegl.t.faxuan.net/saless/service/chargeManageService!approveCharge.do",
                "version=1;flag=2;approceSuggest=autoTest" + dateaNumber + ";userAccount=" +
                        saleUserInfo.getOperator() + ";relatedIndex=" + RELATED_INDEX).body("code=200;msg=审核成功");
    }

    @Test(description = "增加发票折扣",priority = 118)
    public void updateDiscount() {
        post("http://salegl.t.faxuan.net/saless/service/invoiceService!updateDiscount.do",
                "operator=" + saleUserInfo.getOperator() + ";id=" + FPID + ";discount=1")
                .body("code=200;msg=操作成功！");
    }

    @Test(description = "销账成功",priority = 119)
    public void writeOffYes() {
        post("http://salegl.t.faxuan.net/saless/service/invoiceService!writeOff.do",
                "id=" + FPID + ";claimAdmin=" + saleUserInfo.getOperator())
                .body("code=200;msg=操作成功！");
    }

    class Product{
        //商品ID
        private String id;
        //商品名称
        private String name;
        //书代号
        private String bookCode;
        //ISBN号
        private String isbnCode;
        //库位
        private String storage;
        //单价
        private int unitPrice;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getBookCode() {
            return bookCode;
        }

        public void setBookCode(String bookCode) {
            this.bookCode = bookCode;
        }

        public String getIsbnCode() {
            return isbnCode;
        }

        public void setIsbnCode(String isbnCode) {
            this.isbnCode = isbnCode;
        }

        public String getStorage() {
            return storage;
        }

        public void setStorage(String storage) {
            this.storage = storage;
        }

        public int getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(int unitPrice) {
            this.unitPrice = unitPrice;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    /**
     * 舍去小数点后的值（不做四舍五入）
     * @param doubleVlue 原始数据
     * @param reservedDigits 精确到小数点后多少位
     * @return 返回修改后的值
     */
    public Double roundingValue (Double doubleVlue,int reservedDigits) {
        String[] value = String.valueOf(doubleVlue).split("\\.");
        value[1] = value[1].substring(0,reservedDigits);
        return Double.valueOf(value[0] + "." + value[1]);
    }

    /**
     * 获取验证码
     * @return 验证码
     */
    public String getIdentifyCode() {
        setHeader("Cookie","rid=" + rid);
        get("http://salemh.t.faxuan.net/service/gc.html");
        String body = get("http://salemh.t.faxuan.net/getIdentifyCode","rid=" + rid).getBody();
        return body.split("\\r")[0];
    }


}
