package com;




import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by song on 18/3/10.
 */
public class ExcelImportDB extends JFrame{

    JTextField excelLocation = new JTextField(20);
    JTextField dbURL = new JTextField(20);
    JTextField dbPort = new JTextField(20);
    JTextField dbName = new JTextField(20);
    JTextField dbUser = new JTextField(15);
    JTextField dbPassword = new JTextField(15);
    JLabel info = new JLabel();
    JButton button1 = new JButton("导入");
    JButton button2 = new JButton("退出");

    public void CreatJFrame(){
        JFrame jf=new JFrame("Excel数据导入数据库");
        GridLayout gy =new GridLayout(8,0);
        jf.setSize(400,400);
        jf.setLocation(450,180);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setLayout(gy);
        jf.setResizable(false);

        ImageIcon icon=new ImageIcon(this.getClass().getResource("/ex.png")); //图片和项目同一路径，故不用图片的路径
        jf.setIconImage(icon.getImage());

        button1.addActionListener(new ButtonHandler1());
        button2.addActionListener(new ButtonHandler2());

        JLabel jl_excelLocation = new JLabel("  Excel位置:");
        JLabel jl_dbURL = new JLabel("数据库地址:");
        JLabel jl_dbPort = new JLabel("端   口   号:");
        JLabel jl_dbName = new JLabel("数据库名称:");
        JLabel jl_dbUser = new JLabel("用户名:");
        JLabel jl_dbPassword = new JLabel("密    码:");

        JPanel jPanelexcel = new JPanel();
        jPanelexcel.add(jl_excelLocation);
        jPanelexcel.add(excelLocation);

        JPanel jPanel1 = new JPanel();
        jPanel1.add(jl_dbURL);
        jPanel1.add(dbURL);

        JPanel jPanel2 = new JPanel();
        jPanel2.add(jl_dbPort);
        jPanel2.add(dbPort);

        JPanel jPanel3 = new JPanel();
        jPanel3.add(jl_dbName);
        jPanel3.add(dbName);

        JPanel jPanel4 = new JPanel();
        jPanel4.add(jl_dbUser);
        jPanel4.add(dbUser);

        JPanel jPanel5 = new JPanel();
        jPanel5.add(jl_dbPassword);
        jPanel5.add(dbPassword);

        JPanel jPanel6 = new JPanel();
        jPanel6.add(button1);
        jPanel6.add(button2);

        JPanel jPanel7 = new JPanel();
        info.setForeground(Color.red);
        jPanel7.add(info);

        jf.add(jPanelexcel);
        jf.add(jPanel1);
        jf.add(jPanel2);
        jf.add(jPanel3);
        jf.add(jPanel4);
        jf.add(jPanel5);
        jf.add(jPanel6);
        jf.add(jPanel7);


        jf.setVisible(true);
    }

    public static void main(String[] args) {
        new ExcelImportDB().CreatJFrame();
    }

    class ButtonHandler1 implements ActionListener
    {

        public void actionPerformed(ActionEvent e)
        {
            //事件处理器

            if (excelLocation.getText().trim().length() > 0  && dbURL.getText().trim().length() > 0  && dbPort.getText().trim().length() > 0 && dbName.getText().trim().length() > 0 && dbUser.getText().trim().length() > 0 && dbPassword.getText().trim().length() > 0) {

                 int i = ExcelUtil.importExcelData(excelLocation.getText().trim(),dbURL.getText().trim(),dbPort.getText().trim(),dbName.getText().trim(),dbUser.getText().trim(),dbPassword.getText().trim());
                 switch (i) {
                     case 0:
                         info.setText("导入成功");
                         info.setForeground(Color.green);
                         break;
                     case 1:
                         info.setText("数据库连接信息输入错误");
                         info.setForeground(Color.red);
                         break;
                     case 2:
                         info.setText("Excel文件地址输入错误");
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

    class ButtonHandler2 implements ActionListener
    {

        public void actionPerformed(ActionEvent e)
        {
            //事件处理器
            System.exit(0);
        }
    }

}
