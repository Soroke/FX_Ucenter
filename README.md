# FX_Ucenter

测试数据驱动方法
---
 

  1、新建数据库并将数据库文件uc.sql导入

  2、按照‘uc数据导入模板.xslx’文件编辑测试数据

  3、使用工具[excelImport.jar](https://github.com/Soroke/FX_Ucenter/releases)将编辑完成的测数据导入到数据库中

  4、修改testNG.xml文件中的数据库链接方式

  5、运行maven命令执行测试
  ```
  mvn clean install
  ```

  6、同样使用工具[excelImport.jar](https://github.com/Soroke/FX_Ucenter/releases)，输入系统ID将数据库中的测试数据删除


手动编写
---
  1、静态导入Http类，使用import static net.faxuan.interfaceframework.core.Http.*<br>
  -----------
  2、直接编写get或post请求的代码（暂时只支持get和post）<br>
  -----------
    实例：<br>
```Java

public void test() {

  get("http://www.baidu.com/request.do","a=1;b=2;c=3;d=4");

}
```
3、验证返回（暂时只支持验证返回为标准json，后续扩展）
-----------
如果接口返回json如下
```Json
{

  "lotto":{

   "lottoId":5,

   "winning-numbers":[2,45,34,23,7,5,3],

   "winners":[{

     "winnerId":23,

     "numbers":[2,45,34,23,3,5]

   },{

     "winnerId":54,

     "numbers":[52,3,12,11,18,22]

   }]

  }

}
```
验证单个数值
```Java
get(URL,Params).body("lotto.lottoId",5);

```
验证多个值
```Java
get(URL,Params).body("lotto.lottoId=5;lotto.winners[0].winnerId=23");

```
