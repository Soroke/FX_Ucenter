package com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;

/**
 * Created by song on 18/3/26.
 */
public class ExcelImportDB extends JFrame {

    JTextField excelLocation = new JTextField(20);
    JTextField dbURL = new JTextField(20);
    JTextField dbPort = new JTextField(20);
    JTextField dbName = new JTextField(20);
    JTextField dbUser = new JTextField(15);
    JTextField dbPassword = new JTextField(15);
    JTextField systemID = new JTextField(5);
    JTextField jtFiddler = new JTextField(20);
    JTextField jtExcel = new JTextField(20);
    JLabel info = new JLabel();
    JLabel dbConnectTestInfo = new JLabel();
    JLabel delInfo = new JLabel();
    JButton button1 = new JButton("导入数据");
    JButton chooseButton = new JButton("选择Excel文件");
    JButton delButton = new JButton("删除数据");
    JButton connectTest = new JButton("连接测试");
    JButton fiddlerButton = new JButton(" 选择fiddler导出文件  ");
    JButton excelButton = new JButton("选择导出excel文件夹");
    JButton convert = new JButton("转换");

    /**
     * 默认信息
     */
    String excel = "C:\\Users\\song\\Desktop\\uc数据导入模板.xlsx";
    String url = "27.221.58.101";
    String port = "3308";
    String name = "qa_apiframetest";
    String user = "root";
    String pw = "xfDB_xntest.223";
    String delSystemID = "2";

    public void CreatJFrame() {
        JFrame jf = new JFrame("导入或删除数据库的测试数据");
        GridLayout gy = new GridLayout(21, 0);
        jf.setSize(400, 700);
        jf.setLocation(500, 20);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setLayout(gy);
        jf.setResizable(false);

        ImageIcon icon = new ImageIcon(this.getClass().getResource("/ex.png")); //图片和项目同一路径，故不用图片的路径
        jf.setIconImage(icon.getImage());

        button1.addActionListener(new ButtonHandler1());
        connectTest.addActionListener(new ConnectTestHandler());
        delButton.addActionListener(new DelDataHandler());
        chooseButton.addActionListener(new ChooseButtonListener());
        JLabel jl_firstline = new JLabel("====================配置数据库连接====================");
        JLabel jl_excelLocation = new JLabel("  Excel本地位置:");
        JLabel jl_dbURL = new JLabel("数据库地址:");
        JLabel jl_dbPort = new JLabel("端    口    号:");
        JLabel jl_dbName = new JLabel("数据库名称:");
        JLabel jl_dbUser = new JLabel("用户名:");
        JLabel jl_dbPassword = new JLabel("密    码:");
        JLabel jl_insline = new JLabel("======================数据导入======================");
        JLabel jl_delline = new JLabel("======================数据删除======================");
        JLabel jl_fiddlerline = new JLabel("========解析fiddler导出文件并生成为指定的excel========");
        JLabel jl_delSystemId = new JLabel("需要删除测试数据的系统ID");
        JLabel warning = new JLabel("谨慎操作，删除前请确认信息，操作不可逆!!!!");


        JPanel firstLine = new JPanel();
        firstLine.add(jl_firstline);

        JPanel jPanel1 = new JPanel();
        jPanel1.add(jl_dbURL);
        jPanel1.add(dbURL);
        dbURL.setForeground(Color.GRAY);
        dbURL.setText(url);
        dbURL.addFocusListener(new MyFocusListener(url, dbURL));

        JPanel jPanel2 = new JPanel();
        jPanel2.add(jl_dbPort);
        jPanel2.add(dbPort);
        dbPort.setForeground(Color.GRAY);
        dbPort.setText(port);
        dbPort.addFocusListener(new MyFocusListener(port, dbPort));

        JPanel jPanel3 = new JPanel();
        jPanel3.add(jl_dbName);
        jPanel3.add(dbName);
        dbName.setForeground(Color.GRAY);
        dbName.setText(name);
        dbName.addFocusListener(new MyFocusListener(name, dbName));

        JPanel jPanel4 = new JPanel();
        jPanel4.add(jl_dbUser);
        jPanel4.add(dbUser);
        dbUser.setForeground(Color.GRAY);
        dbUser.setText(user);
        dbUser.addFocusListener(new MyFocusListener(user, dbUser));

        JPanel jPanel5 = new JPanel();
        jPanel5.add(jl_dbPassword);
        jPanel5.add(dbPassword);
        dbPassword.setForeground(Color.GRAY);
        dbPassword.setText(pw);
        dbPassword.addFocusListener(new MyFocusListener(pw, dbPassword));

        JPanel dbConnTestButton = new JPanel();
        dbConnTestButton.add(connectTest);

        JPanel dbConnInfo = new JPanel();
        dbConnInfo.add(dbConnectTestInfo);

        JPanel insLine = new JPanel();
        insLine.add(jl_insline);

        JPanel jPanelexcel = new JPanel();
        jPanelexcel.add(chooseButton);
//        jPanelexcel.add(jl_excelLocation);
        excelLocation.setEditable(false);
        jPanelexcel.add(excelLocation);
//        excelLocation.setForeground(Color.GRAY);
//        excelLocation.setText(excel);
//        excelLocation.addFocusListener(new MyFocusListener(excel, excelLocation));

        JPanel jPanel6 = new JPanel();
        jPanel6.add(button1);

        JPanel jPanel7 = new JPanel();
        info.setForeground(Color.red);
        jPanel7.add(info);

        JPanel jPanel8 = new JPanel();
        jPanel8.add(jl_delline);

        JPanel jPanel9 = new JPanel();
        jPanel9.add(jl_delSystemId);
        jPanel9.add(systemID);
        systemID.setForeground(Color.GRAY);
        systemID.setText(delSystemID);
        systemID.addFocusListener(new MyFocusListener(delSystemID, systemID));

        JPanel jPanel10 = new JPanel();
        warning.setForeground(Color.red);
        jPanel10.add(warning);

        JPanel jPanel11 = new JPanel();
        jPanel11.add(delButton);

        JPanel delectInfo = new JPanel();
        delectInfo.add(delInfo);

        JPanel fiddlerLine = new JPanel();
        fiddlerLine.add(jl_fiddlerline);

        JPanel fiddlerChoose = new JPanel();
        fiddlerChoose.add(fiddlerButton);
        jtFiddler.setEditable(false);
        fiddlerChoose.add(jtFiddler);

        JPanel excelChoose = new JPanel();
        excelChoose.add(excelButton);
        jtExcel.setEditable(false);
        excelChoose.add(jtExcel);

        JPanel jpconvert = new JPanel();
        jpconvert.add(convert);


        jf.add(firstLine);
        jf.add(jPanel1);
        jf.add(jPanel2);
        jf.add(jPanel3);
        jf.add(jPanel4);
        jf.add(jPanel5);
        jf.add(dbConnTestButton);
        jf.add(dbConnInfo);
        jf.add(insLine);
        jf.add(jPanelexcel);
        jf.add(jPanel6);
        jf.add(jPanel7);
        jf.add(jPanel8);
        jf.add(jPanel9);
        jf.add(jPanel10);
        jf.add(jPanel11);
        jf.add(delectInfo);
        jf.add(fiddlerLine);
        jf.add(fiddlerChoose);
        jf.add(excelChoose);
        jf.add(jpconvert);

        jf.setVisible(true);
    }

    public static void main(String[] args) {
        new ExcelImportDB().CreatJFrame();
    }


    /**
     * 选择框按钮监听
     */
    class ChooseButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser jfc=new JFileChooser();
            jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            jfc.showDialog(new JLabel(), "选择");
            File file=jfc.getSelectedFile();
            if(file.isDirectory()){
                System.out.println("文件夹:"+file.getAbsolutePath());
            }else if(file.isFile()){
                String filePath = file.getAbsolutePath();
                String extension = filePath.split("\\.")[1];
                if (extension.equals("xlsx") || extension.equals("xls")) {
                    excelLocation.setForeground(Color.BLACK);
                    excelLocation.setText(filePath);
                    info.setText("");
                } else {
                    excelLocation.setForeground(Color.MAGENTA);
                    excelLocation.setText("请选择指定的EXCEL文件。");
                }
//System.out.println("选择文件:"+file.getAbsolutePath());
            }
        }
    }
    /**
     * 测试连接按钮
     */
    class ConnectTestHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            //事件处理器
            dbConnectTestInfo.setText("连接中请稍后。。。");
            Mysql sql = new Mysql();
            String url = "jdbc:mysql://" + dbURL.getText().trim() + ":" + dbPort.getText().trim() + "/" + dbName.getText().trim() + "?useSSL=true&characterEncoding=UTF-8";
            sql.setUrl(url);
            sql.setUserName(dbUser.getText().trim());
            sql.setPassWord(dbPassword.getText().trim());
            boolean b = sql.connSQL();
            if (b) {
                dbConnectTestInfo.setForeground(Color.green);
                dbConnectTestInfo.setText("数据库连接成功,可以开始导入或删除测试数据了。");
            } else {
                dbConnectTestInfo.setForeground(Color.red);
                dbConnectTestInfo.setText("数据库连接失败，请核对上述信息的正确性。");
            }
            sql.deconnSQL();

        }
    }

    /**
     * 导入数据按钮
     */
    class ButtonHandler1 implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            //事件处理器

            if (excelLocation.getText().trim().length() > 0 && dbURL.getText().trim().length() > 0 && dbPort.getText().trim().length() > 0 && dbName.getText().trim().length() > 0 && dbUser.getText().trim().length() > 0 && dbPassword.getText().trim().length() > 0) {

                int[] i = ExcelUtil.importExcelData(excelLocation.getText().trim(), dbURL.getText().trim(), dbPort.getText().trim(), dbName.getText().trim(), dbUser.getText().trim(), dbPassword.getText().trim());
                switch (i[1]) {
                    case 0:
                        info.setText("导入成功");
                        info.setForeground(Color.green);
                        break;
                    case 1:
                        info.setText("数据库连接失败，请核对数据库连接信息。");
                        info.setForeground(Color.red);
                        break;
                    case 2:
                        info.setText("Excel文件不存在，请核对excel地址是否正确。");
                        info.setForeground(Color.red);
                        break;
                    case 3:
                        info.setText("excel第" + i[0] + "行数据的系统ID不能为空");
                        info.setForeground(Color.red);
                        break;
                    case 4:
                        info.setText("excel第" + i[0] + "行数据的URL不能为空");
                        info.setForeground(Color.red);
                        break;
                    case 5:
                        info.setText("excel第" + i[0] + "行数据的测试参数不能为空");
                        info.setForeground(Color.red);
                        break;
                    case 6:
                        info.setText("excel第" + i[0] + "行数据的预期结果不能为空");
                        info.setForeground(Color.red);
                        break;
                    case 7:
                        info.setText("执行excel第" + i[0] + "行数据的数据库插入报错");
                        info.setForeground(Color.red);
                        break;
                }
            } else {
                info.setText("请输入正确的数据连接地址和文件地址");
                info.setForeground(Color.red);
            }
//System.out.println("Excel位置：" + excelLocation.getText().trim() + "\n数据库地址："  + dbURL.getText().trim() + "\n数据库端口号："  + dbPort.getText().trim() + "\n数据库名称："  + dbName.getText().trim() + "\n数据库用户名："  + dbUser.getText().trim() + "\n数据库用户密码："  + dbPassword.getText().trim());
        }
    }


    /**
     * 删除数据按钮
     */
    class DelDataHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            //事件处理器

            if (systemID.getText().trim().length() > 0 && dbURL.getText().trim().length() > 0 && dbPort.getText().trim().length() > 0 && dbName.getText().trim().length() > 0 && dbUser.getText().trim().length() > 0 && dbPassword.getText().trim().length() > 0) {

                int[] i = DelData.del(dbURL.getText().trim(), dbPort.getText().trim(), dbName.getText().trim(), dbUser.getText().trim(), dbPassword.getText().trim(),systemID.getText().trim());
                switch (i[0]) {
                    case 0:
                        delInfo.setText("系统ID：'" + systemID.getText().trim() + "'下的" + i[1] + "条数据删除成功");
                        delInfo.setForeground(Color.green);
                        break;
                    case 1:
                        delInfo.setText("数据库连接失败，请核对数据库连接信息。");
                        delInfo.setForeground(Color.red);
                        break;
                    case 2:
                        delInfo.setText("系统ID：" + systemID.getText().trim() + "，数据为空，不做任何执行。");
                        delInfo.setForeground(Color.green);
                        break;
                    case 3:
                        delInfo.setText("删除数据执行出错。。。");
                        delInfo.setForeground(Color.red);
                        break;
                }
            } else {
                delInfo.setText("请输入正确的系统ID");
                delInfo.setForeground(Color.red);
            }
        }
    }

    /**
     * 输入框默认文字监听
     */
    class MyFocusListener implements FocusListener {
        String info;
        JTextField jtf;

        public MyFocusListener(String info, JTextField jtf) {
            this.info = info;
            this.jtf = jtf;
        }

        public void focusGained(FocusEvent e) {//获得焦点的时候,清空提示文字
            String temp = jtf.getText();
            if (temp.equals(info)) {
                jtf.setText("");
                jtf.setForeground(Color.BLACK);
            }
        }

        public void focusLost(FocusEvent e) {//失去焦点的时候,判断如果为空,就显示提示文字
            String temp = jtf.getText();
            if (temp.equals("")) {
                jtf.setForeground(Color.GRAY);
                jtf.setText(info);
            }
        }
    }
}
