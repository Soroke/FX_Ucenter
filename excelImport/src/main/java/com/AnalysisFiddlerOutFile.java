package com;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;

/**
 * 读取fiddler文件并解析，然后输出excel文件到指定目录
 * Created by song on 2018/3/30.
 */
public class AnalysisFiddlerOutFile {

    private static String[] excelTitle = {"系统ID","前置条件(是否需要登录;不需要请留空)","测试url","参数(多个参数用英文分号隔开)","预期结果(接口返回状态码)","测试功能描述"};

    private static HSSFWorkbook workbook = null;
    private static XSSFWorkbook xWorkbook = null;


    /**
     * 读取fiddler文件并解析，然后输出excel文件到指定目录
     * @param filePathName 输出文件excel
     * @param sheetName excel列名
     * @param sourceFile fiddler导出文件
     */
    public static void writeToExcel(String filePathName,String sheetName,String sourceFile) {
        writeToExcel(filePathName,sheetName,sourceFile);
    }

    /**
     * 写数据到excel文件
     * @param filePathName
     * @param sheetName
     * @param apiInfos
     */
    private static void writeToExcel(String filePathName,String sheetName,List<APIInfo> apiInfos )  throws Exception{
        createExcel(filePathName,sheetName,excelTitle);
        //获取数据数量
        int apiCount = apiInfos.size();

        //判断excel的文件类型
        String[] excelType = filePathName.split("\\.");
        if (excelType[1].equals("xlsx") || excelType.equals("XLSX")) {
            //创建workbook
            File file = new File(filePathName);
            try {
                xWorkbook = new XSSFWorkbook(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //流
            FileOutputStream out = null;
            XSSFSheet sheet = xWorkbook.getSheet(sheetName);
            // 获取表头的列数
            //int columnCount = sheet.getRow(0).getLastCellNum()+1;// 需要加一

            try {
                // 获得表头行对象
                XSSFRow titleRow = sheet.getRow(0);
                if(titleRow!=null){
                    for(int rowId=0;rowId<apiCount;rowId++){
                        APIInfo apiInfo = apiInfos.get(rowId);
                        //Map map = mapList.get(rowId);
                        XSSFRow newRow=sheet.createRow(rowId+1);
                        XSSFCell cell = newRow.createCell(2);
                        cell.setCellValue(apiInfo.getUrl());
                        cell = newRow.createCell(3);
                        cell.setCellValue(apiInfo.getParam());
                        cell = newRow.createCell(5);
                        cell.setCellValue(rowId+1);
                    }
                }
                out = new FileOutputStream(filePathName);
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

        } else {
            //创建workbook
            File file = new File(filePathName);
            try {
                workbook = new HSSFWorkbook(new FileInputStream(file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //流
            FileOutputStream out = null;
            HSSFSheet sheet = workbook.getSheet(sheetName);
            // 获取表头的列数
            int columnCount = sheet.getRow(0).getLastCellNum()+1;// 需要加一

            try {
                // 获得表头行对象
                HSSFRow titleRow = sheet.getRow(0);
                if(titleRow!=null){
                    for(int rowId=0;rowId<apiCount;rowId++){
                        APIInfo apiInfo = apiInfos.get(rowId);
                        //Map map = mapList.get(rowId);
                        HSSFRow newRow=sheet.createRow(rowId+1);
                        HSSFCell cell = newRow.createCell(2);
                        cell.setCellValue(apiInfo.getUrl());
                        cell = newRow.createCell(3);
                        cell.setCellValue(apiInfo.getParam());
                    }
                }
                out = new FileOutputStream(filePathName);
                workbook.write(out);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 创建新excel（xls或xlsx）.
     * @param filePathName  excel的路径
     * @param sheetName 要创建的表格索引
     * @param titleRow excel的第一行即表格头
     */
    private static void createExcel(String filePathName,String sheetName,String titleRow[]) throws Exception{
        //判断excel的文件类型
        String[] excelType = filePathName.split("\\.");
        if (excelType[1].equals("xls") || excelType.equals("XLS")) {
            //创建workbook
            workbook = new HSSFWorkbook();
            //添加Worksheet（不添加sheet时生成的xls文件打开时会报错)
            HSSFSheet sheet = workbook.createSheet(sheetName);
            //新建文件
            FileOutputStream out = null;
            try {
                //添加表头
                HSSFRow row = workbook.getSheet(sheetName).createRow(0);    //创建第一行
                for(short i = 0;i < titleRow.length;i++){
                    HSSFCell cell = row.createCell(i);
                    cell.setCellValue(titleRow[i]);
                }
                out = new FileOutputStream(filePathName);
                workbook.write(out);
            } catch (Exception e) {
                throw e;
            } finally {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            //创建workbook
            xWorkbook = new XSSFWorkbook();
            //添加Worksheet（不添加sheet时生成的xls文件打开时会报错)
            XSSFSheet sheet2 = xWorkbook.createSheet(sheetName);
            //新建文件
            FileOutputStream out = null;

            try {
                //添加表头
                XSSFRow row1 = xWorkbook.getSheet(sheetName).createRow(0);
                for(short i = 0;i < titleRow.length;i++){
                    XSSFCell cell1 = row1.createCell(i);
                    cell1.setCellValue(titleRow[i]);
                }
                out = new FileOutputStream(filePathName);
                xWorkbook.write(out);
            } catch (Exception e) {
                throw e;
            } finally {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    /**
     * 解析文件
     * @param filePath 文件path
     */
    private static List<APIInfo> analysis(String filePath) {
        List<APIInfo> apiInfoList = new ArrayList<APIInfo>();

        String content = codedConvert(filePath);
        String[] allApi = content.split("------------------------------------------------------------------");
        APIInfo apiInfo = null;
        for (String api:allApi) {
            if (api.equals("")) continue;

            String[] apiContent = api.split("\\n");
            apiInfo = new APIInfo();
            Boolean isCookies = false;
            int cookie = 0;
            for (String apiContentOneLine:apiContent) {

                if (apiContentOneLine.contains("POST") || apiContentOneLine.contains("GET")) {
                    String[] urlInfo = apiContentOneLine.split(" ");
                    try {
                        String[] up = urlInfo[1].split("\\?");
                        up[1].length();
                        String params = up[1].replaceAll("&",";");
                        apiInfo.setUrl(up[0]);
                        apiInfo.setParam(params);
                        break;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        //System.out.print(e);
                        apiInfo.setUrl(urlInfo[1]);
                    }
                }
                if (apiContentOneLine.contains("Cookie")) {
                    cookie ++;
                    isCookies = true;
                    continue;
                }
                if (isCookies && cookie != 2) {
                    cookie ++;
                } else if (isCookies && cookie == 2) {
                    String params = apiContentOneLine.replaceAll("&",";");
                    apiInfo.setParam(params);
                    isCookies = false;
                    cookie = 0;
                }
            }
            try {
                apiInfo.getUrl().equals("");
                apiInfoList.add(apiInfo);
                //System.out.println(apiInfo);
            } catch (NullPointerException e) {

            }

        }
        return deDuplication(apiInfoList);
    }

    /**
     *
     * 将fiddler导出文件进行转换，fiddler导出文件中文和特殊字符都进行了URL编码。
     * @param sourceFilePath
     * @return
     */
    private static String codedConvert(String sourceFilePath) {
        BufferedReader reader = null;

        StringBuilder content = new StringBuilder();
        String codeing = "UTF-8";
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFilePath)));
            String lineStr;
            while ((lineStr = reader.readLine()) != null) {
                try {
                    String converted = URLDecoder.decode(lineStr,codeing);
                    content.append(converted).append("\n");
                } catch (IllegalArgumentException e) {
                    content.append(lineStr).append("\n");
                    continue;
                }
            }
            reader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString();

    }


    /**
     * 去重
     *  list中相同的对象会被去重复
     * @param list
     * @return
     */
    private static List<APIInfo> deDuplication(List<APIInfo> list){
        for (int i=0;i<list.size();i++) {
            for (int j=list.size()-1;j>i;j--) {
                if (list.get(j).getUrl().equals(list.get(i).getUrl()) && list.get(j).getParam().equals(list.get(i).getParam())) {
                    list.remove(j);
                }
            }
        }
        return list;
    }

    /**
     * 内部类，暂存解析后的接口url和param
     */
    private static class APIInfo {
        private String url;
        private String param;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getParam() {
            return param;
        }

        public void setParam(String param) {
            this.param = param;
        }

        public String toString() {
            return "url:" + url + "\t参数：" + param ;
        }
    }
}
