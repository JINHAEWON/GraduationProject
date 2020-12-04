
package com.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.sql.Statement;
import java.util.Objects;
import com.mysql.*;

public class ConnectDB4 {
    private static ConnectDB4 instance = new ConnectDB4();

    public static ConnectDB4 getInstance() {
        return instance;
    }
    public ConnectDB4() {  }
    
    Connection conn = null;
    PreparedStatement pstmt = null;
    PreparedStatement pstmt2 = null;
    ResultSet rs = null;
    ResultSet rs2 = null;
    Statement stmt = null;

    String sql;
    String sql2;
    String pidAll=null;
    String pidlist[];
    String imgurlAll=null;
    int num = 0;
    String returns = System.getenv("nodeport");
    		
    public String connectionDB(String jid) {
        try {
           String pidlist[]=null;           
           pidAll=null;
           num = 0;
           imgurlAll=null;

        
           
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
            
            //sql문
            sql ="SELECT * FROM pinfo WHERE id='"+jid+"'";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            if(rs.next()) 
            {
             
               pidAll = rs.getString(2);
                pidlist = pidAll.split(",");
                num = pidlist.length;
                
            }
            
            //확인
            System.out.println(pidAll);
            System.out.println(pidlist);
            System.out.println(num);
            
            for(int i=0; i<num; i++)
            {
               System.out.println(pidlist[i]);
               sql2 ="SELECT * FROM binfo WHERE pid='"+pidlist[i]+"'";
               pstmt2 = conn.prepareStatement(sql2);
                rs2 = pstmt2.executeQuery();
                rs2.next();
                
                if(!Objects.equals(pidlist[i],"null"))
                {
                   System.out.println("ConnectDB4에서 확인");
                    
                    if(i==0) {imgurlAll=rs2.getString(2);}
                    else {imgurlAll = imgurlAll + "," + rs2.getString(2);}
                }
                              
                else {
                   
                   sql ="delete from pinfo where pid='null'";
                   pstmt = conn.prepareStatement(sql);
                    rs = pstmt.executeQuery();
                    rs.next();
                    
                    imgurlAll=null;
                }
                  
            }   
            
            returns = pidAll + "-" + imgurlAll;

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