# FX_Ucenter
使用模式
===
框架分为两种使用模式
一、测试数据驱动方法
---
  1、新建数据库并将数据库文件uc.sql导入
  2、按照‘uc数据导入模板.xslx’文件编辑测试数据
  3、使用net.faxuan.root.ImportExcelDataToMysql.java文件修改文件路径和数据库链接地址；将编辑完成的测数据导入到数据库中
  4、修改testNG.xml文件中的数据库链接方式
  5、运行testNG.xml文件执行测试
  
二、手动编写
---
  1、静态导入Http类，使用import static net.faxuan.interfaceframework.core.Http.*
  2、直接编写get或post请求的代码（暂时只支持get和post）
    实例：
    ` ``Java
        @Test(description = "注册，测试get")
    public void register() {
        get("http://ucms.test.faxuan.net/ucds/ucenter/registerUser.do","userAccount=lkassd8513;userPassword=ceshi123;userPhone=18860702903;sysCode=KF01").body("code",200).body("msg","操作成功！");
    }
