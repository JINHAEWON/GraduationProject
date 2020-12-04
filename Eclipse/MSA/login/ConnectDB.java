package com.db;
import java.sql.*;

public class ConnectDB {
    private static ConnectDB instance = new ConnectDB();

    public static ConnectDB getInstance() {
        return instance;
    }
    public ConnectDB() {  }
    
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    Statement stmt = null;

    String sql;
    String returns = System.getenv("nodeport");

    public String connectionDB(String java_id, String pwd) {
        try {
           // 1. mysql db에 접속
           String node = System.getenv("nodeport");
           System.out.println("환경변수 : "+node);
           String addr = "jdbc:mysql://192.168.8.171:"+node+"/?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            String jdbcUrl = addr;
            String userId = "root";
            String userPw = "root";
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(jdbcUrl, userId, userPw);
            
            // 1-1) 접속됬는지 CHECK 
            returns = "Access Database";
            
            // 2. mysql의 id마다 database 만들기 
            stmt = conn.createStatement();
            sql ="create database "+java_id;
            stmt.execute(sql);
            
            // 3. 만들어진 database 사용 
            stmt = conn.createStatement();
            sql ="use "+java_id;
            stmt.execute(sql);

            // 4. 비교하기 위해 select문  보내기 
            sql ="SELECT * FROM cinfo WHERE id='"+java_id+"'";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            // 5. 받아온 resultset 값에서 하나씩 읽어서 login success 인지 확인하기 
            if (rs.next()) {
               String CheckID=rs.getString(1);
               System.out.println("조회된 ID는 :"+CheckID);
               String CheckPW=rs.getString(2);
               
               if(CheckPW.equals(pwd)&& CheckID.equals(java_id))
                  returns="loginSuccess";
               else
                  returns="LoginFail";
               
            } 
            else {
                returns = "LoginFail";
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