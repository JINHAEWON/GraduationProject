package com.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.sql.*;

public class ConnectDB2 {
    private static ConnectDB2 instance = new ConnectDB2();

    public static ConnectDB2 getInstance() {
        return instance;
    }
    public ConnectDB2() {  }
    
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    Statement stmt = null;

    String sql;
    String returns = "ConnectDB2";

    public String connectionDB(String jid, String jpw, String jname, String jemail, String jphone) {
        try {
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
            
            //sql문
            //sql ="SELECT * FROM LOGIN WHERE ID=?";
            sql ="SELECT * FROM cinfo WHERE id='"+jid+"'";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            if(rs.next()) {
               String CheckID=rs.getString(1);
               System.out.println("중복 ID :"+CheckID);
               returns="fail";
            }
            else if(Objects.equals(jid, null) && Objects.equals(jpw, null)&& Objects.equals(jname, null)&& Objects.equals(jemail, null)&& Objects.equals(jphone, null))
            {
               System.out.println("다 채워야 함");
               returns="fail";
            }
            else {
               returns="success";
               
               Statement stat = conn.createStatement();
               String sql2 ="INSERT INTO cinfo VALUES('"+jid+"','"+jpw+"','"+jname+"','"+jemail+"','"+jphone+"')";
               int check = stat.executeUpdate(sql2);
               if(check>0) {
                  System.out.println("업데이트 되긴함~~");}
               else {
                  System.out.println("실패,,");
               }

            }
            
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