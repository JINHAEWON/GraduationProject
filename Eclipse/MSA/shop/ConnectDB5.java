package com.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Statement;
import java.sql.Statement;
import java.sql.*;

public class ConnectDB5 {
    private static ConnectDB5 instance = new ConnectDB5();

    public static ConnectDB5 getInstance() {
        return instance;
    }
    public ConnectDB5() {  }
    
    Connection conn = null;
    PreparedStatement pstmt = null;
    PreparedStatement pstmt2 = null;
    ResultSet rs = null;
    ResultSet rs2 = null;
    Statement stmt = null;

    String sql;
    String returns = "****NOTHING IS HAPPENED****";
    String pids[];
    String pidurl;
    String pidprice;

    
    public String connectionDB(String pid_list) {
        try {
           // oracle 계정

           String node = System.getenv("nodeport");
           System.out.println("환경변수 : "+node);
           String addr = "jdbc:mysql://192.168.8.171:"+node+"/?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            String jdbcUrl = addr;
            String userId = "root";
            String userPw = "root";
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(jdbcUrl, userId, userPw);
            
            stmt = conn.createStatement();
            sql ="use mysqldatabase";
            stmt.execute(sql);
            
            pids = pid_list.split(",");
            int pidnum = pids.length;
            
           
            for(int i=0; i<pidnum; i++)
            {
               sql ="SELECT * FROM binfo WHERE pid='"+pids[i]+"'";
                pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery();
                rs.next();
                
                System.out.println(rs.getString(2));
                
                if(i==0) {pidurl=rs.getString(2);}
                else {pidurl = pidurl + "," + rs.getString(2);}
            }

            for(int i=0; i<pidnum; i++)
            {
               sql ="SELECT * FROM binfo WHERE pid='"+pids[i]+"'";
                pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery();
                rs.next();
                
                System.out.println(rs.getString(3));
                
                if(i==0) {pidprice = rs.getString(3);}
                else {pidprice = pidprice + "," + rs.getString(3);}
            }
            
            returns = pidurl + "-" + pidprice;
            System.out.println("ConnectDB5"+returns);

        } 
        catch (Exception e) {
            e.printStackTrace();
        } 
        finally {
            if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {System.out.println(ex);}
            if (conn != null)try {conn.close();} catch (SQLException ex) {System.out.println(ex);}
        }
        return returns;
    }
}