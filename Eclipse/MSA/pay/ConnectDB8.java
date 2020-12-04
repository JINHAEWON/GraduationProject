package com.db;
//import java.sql.Statement;
import com.db.ConnectDB7;
import java.sql.*;

public class ConnectDB8 {
    private static ConnectDB8 instance = new ConnectDB8();

    public static ConnectDB8 getInstance() {
        return instance;
    }
    public ConnectDB8() {  }
    
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    Statement stmt = null;

    String sql;
    String returns = "****IT'S ERROR IN ConnectDB8****";
    String phoneNo;
    
    public String connectionDB(String jid) {
        try {
           // oracle ����

           String node = System.getenv("nodeport");
           System.out.println("ȯ�溯�� : "+node);
           String addr = "jdbc:mysql://192.168.8.171:"+node+"/?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            String jdbcUrl = addr;
            String userId = "root";
            String userPw = "root";
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(jdbcUrl, userId, userPw);
            
            stmt = conn.createStatement();
            sql ="use mysqldatabase";
            stmt.execute(sql);
            
            //sql��
            sql ="SELECT * FROM cinfo WHERE id='"+jid+"'";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            if(rs.next()) 
            {
               phoneNo = rs.getString(4);                
            }
            
            System.out.println("��ȸ�� �̸��� : "+phoneNo);
          
            returns = phoneNo;

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