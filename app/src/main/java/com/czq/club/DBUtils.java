package com.czq.club;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {
    private static String driver = "com.mysql.jdbc.Driver";//mysql驱动
    private static String url = "jdbc:mysql://192.168.43.92:3306/club?useUnicode=true&characterEncoding=UTF-8";//mysql数据库连接url
    private static String user = "czq";
    private static String password ="123456";

    public static Connection getConnection(){
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url,user,password);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }


}