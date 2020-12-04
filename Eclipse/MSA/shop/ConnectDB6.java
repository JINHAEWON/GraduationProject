package com.db;
//import java.sql.Statement;
import java.sql.*;

public class ConnectDB6 {
    private static ConnectDB6 instance = new ConnectDB6();

    public static ConnectDB6 getInstance() {
        return instance;
    }
    public ConnectDB6() {  }
    
    Connection conn = null;
    PreparedStatement pstmt = null;
    PreparedStatement pstmt2 = null;
    ResultSet rs = null;
    ResultSet rs2 = null;
    Statement stmt = null;

    String sql;
    String sql2;
    String returns = "왜안되냐고짜정나진짜";
    String pidurl;
    String pidprice;
    String ret=null;

    
    public String connectionDB(String jid,String pid_list) {
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

            sql ="SELECT * FROM pinfo WHERE id='"+jid+"'";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            rs.next();
            
            String dbpid = rs.getString(2);
            String dbpids[] = dbpid.split(",");
            int dbpidnum = dbpids.length;
            

            String pids[] = pid_list.split(",");
            System.out.println("하나하나확인한다:"+pids.length);
            int pidnum = pids.length;
            
            ret=null;
            
            for(int i=0; i<dbpidnum; i++)
            {
               
               for(int j=0;j<pidnum;j++)
               {
                    
                  if(dbpids[i].contentEquals(pids[j]))
                  {
                     dbpids[i]="x";
                     pids[j]="x";
                  }
               }
                
            }
            
            for(int i=0; i<dbpidnum;i++)
            {
               if(dbpids[i]!="x")
               {
                  if(ret==null) {ret=dbpids[i];}
                    else {ret = ret + "," + dbpids[i];}
               }
            }
            
            
            System.out.println(ret);

            sql ="UPDATE pinfo SET PID = '"+ret+"' WHERE ID ='"+jid+"'";
           pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            rs.next();

            returns = "ok";
            System.out.println(returns);
           

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