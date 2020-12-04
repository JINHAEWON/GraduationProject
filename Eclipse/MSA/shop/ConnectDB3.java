package com.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.sql.*;

public class ConnectDB3 {
    private static ConnectDB3 instance = new ConnectDB3();

    public static void main(String args[]) {
       
    }
    
    public static ConnectDB3 getInstance() {
        return instance;
    }
    public ConnectDB3() {  }
    
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    Statement stmt = null;

    String sql;
    String returns = System.getenv("nodeport");
   
    
    String itemNumber=null;
    
    public String connectionDB(String jid, String item) {
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
            //sql ="SELECT * FROM LOGIN WHERE ID=?";
            sql ="SELECT * FROM pinfo WHERE id='"+jid+"'";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            if(rs.next()) {
               //String CheckID = rs.getString(1);
               itemNumber = rs.getString(2); // p001 �̷��� �����°Ű� 
               
               ///////////////////////////////////
               //���⿡�� DB�� ���� itemNumber������ UPDATE �ϴ°�!! 
               //itemNumber.concat(",");
               //itemNumber.concat(item);
               itemNumber=itemNumber+","+item;
               System.out.println(itemNumber);
               
               Statement stat = conn.createStatement();
               //String sql2 ="UPDATE INTO PINFO VALUES('"+jid+"','"+itemNumber+"')";
               
               String sql2 ="UPDATE pinfo SET pid='"+itemNumber+"' WHERE id='"+jid+"'";
               int check = stat.executeUpdate(sql2);
               
               //�̰͵� Ȯ���� ���� ������ �����ؼ� Ȯ��
               if(check>0) {
                  System.out.println("��ٱ��ϰ� �����ϴ� ��� update ����");}
               else {
                  System.out.println("��ٱ��ϰ� �����ϴ� ��� update ����,,");
               }
               ///////////////////////////////////
               
            }
            else if(!Objects.equals(jid, null)) {
               //��ٱ��Ͽ� ���� ���� �ϳ��� ���ٴ� �ǹ̴ϱ� 
               //���⿡�� DB���� INSERT ���ִ°� //////
               //sql="INSERT~ item ";
               Statement stat = conn.createStatement();
               String sql3 = "INSERT INTO pinfo VALUES('"+jid+"','"+item+"')";
               int check2 = stat.executeUpdate(sql3);
               
               //������Ʈ�Ǿ����� Ȯ��
               if(check2>0) {
                  System.out.println("��ٱ��� ���� ��� insert����");}
               else {
                  System.out.println("��ٱ��� ���� ��� insert ����,,");
               }
            }
            
            returns="Success (Put the Item)"+item+"(In the PINFO)";
            
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