package dbtesting;
 
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import io.kubernetes.client.openapi.ApiException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.*;
 
 
@WebServlet("/fileUpload")
public class Excel extends HttpServlet {
	public String id ="null";
	public int node= 0;
 
    private static final String CHARSET = "utf-8";
    private static final String ATTACHES_DIR = "C:\\Users\\user\\Desktop\\db";
    //private static final String ATTACHES_DIR = "http://192.168.8.169 @root";
    private static final int LIMIT_SIZE_BYTES = 1024 * 1024;
    
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    String fname=null;
    Statement stmt = null;
    String fvalue;
    String sql;
 
 
    @Override
    protected void doPost(HttpServletRequest request,  HttpServletResponse response)
            throws ServletException, IOException {
 
 
        response.setContentType("text/html; charset=UTF-8");
        request.setCharacterEncoding(CHARSET);
        PrintWriter out = response.getWriter();
 
 
        File attachesDir = new File(ATTACHES_DIR);
 
 
        DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();
        fileItemFactory.setRepository(attachesDir);
        fileItemFactory.setSizeThreshold(LIMIT_SIZE_BYTES);
        ServletFileUpload fileUpload = new ServletFileUpload(fileItemFactory);
 
 
        try {
            List<FileItem> items = fileUpload.parseRequest(request);
            for (FileItem item : items) {
                if (item.isFormField()) {
                    System.out.printf("파라미터 명 : %s, 파라미터 값 :  %s \n", item.getFieldName(), item.getString(CHARSET));
                    if(item.getFieldName().equals("input_id"))
                    id = item.getString(CHARSET);
                } else {
                    System.out.printf("파라미터 명 : %s, 파일 명 : %s,  파일 크기 : %s bytes \n", item.getFieldName(),
                            item.getName(), item.getSize());
                    if (item.getSize() > 0) {
                        String separator = File.separator;
                        int index =  item.getName().lastIndexOf(separator);
                        String fileName = item.getName().substring(index  + 1);
                        fname=fileName;
                        File uploadFile = new File(ATTACHES_DIR +  separator + fileName);
                        item.write(uploadFile);
                    }
                }
            }
 
            
            out.println("<h1>파일 업로드 완료</h1>");
            request.getRequestDispatcher("/fileUpload.jsp").include(request, response);
            request.getRequestDispatcher("/fileUpload.jsp").forward(request, response);

 
 
        } catch (Exception e) {
            // 파일 업로드 처리 중 오류가 발생하는 경우
            e.printStackTrace();
            out.println("<h1>파일 업로드 중 오류가  발생하였습니다.</h1>");
        }
        
    // 할 일 : id마다 db컨테이너 올려주고 그 주소를 받아오는 것  create로 각 테이블들 만들어주기      
               
    try {
    	
    	//1. database pod 생성하기 
    	kuberDB kubernetes_database = new kuberDB();
    	node = kubernetes_database.kubernetes(id);
       
    	Thread.sleep(120000);
    	
    	
       // 생성된 포드에 
        System.out.println("환경변수 : "+node);
        String addr = "jdbc:mysql://192.168.8.171:"+node+"/?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        System.out.println(addr); 
        String jdbcUrl = addr;
        String userId = "root";
        String userPw = "root";
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(jdbcUrl, userId, userPw);
        
        //sql문
        stmt = conn.createStatement();
        sql ="create database mysqldatabase";
        stmt.execute(sql);
        
        stmt = conn.createStatement();
        sql ="use mysqldatabase";
        stmt.execute(sql);
        
        //create table 할 애들
        
        stmt = conn.createStatement();
        sql ="create table cinfo("
              + "id varchar(20) primary key,"
              + "password varchar(20) not null,"
              + "name varchar(20) not null,"
              + "email varchar(30) not null,"
              + "phone varchar(20) not null"
              + ")";
        stmt.execute(sql);
        
        stmt = conn.createStatement();
        sql ="create table pinfo("
              + "id varchar(20) primary key,"
              + "pid varchar(100) not null"
              + ")";
        stmt.execute(sql);
        
        stmt = conn.createStatement();
        sql ="create table binfo("
              + "pid varchar(20) primary key ,"
              + "imgurl varchar(512) not null,"
              + "price int not null"
              + ")";
        stmt.execute(sql);
        
               
        //파일 읽어오기
        
        FileInputStream file = new FileInputStream("C:\\Users\\user\\Desktop\\db\\"+fname);
        XSSFWorkbook workbook = new XSSFWorkbook(file);

        int rowindex=0;
        int columnindex=0;
        //시트 수 (첫번째에만 존재하므로 0을 준다)
        //만약 각 시트를 읽기위해서는 FOR문을 한번더 돌려준다
        XSSFSheet sheet=workbook.getSheetAt(0);
        //행의 수
        int rows=sheet.getPhysicalNumberOfRows();
        for(rowindex=0;rowindex<rows;rowindex++){
            //행을읽는다
            XSSFRow row=sheet.getRow(rowindex);
            if(row !=null){
                //셀의 수
                int cells=row.getPhysicalNumberOfCells();
                for(columnindex=0; columnindex<=cells; columnindex++){
                    //셀값을 읽는다
                    XSSFCell cell=row.getCell(columnindex);
                    String value="";
                    //셀이 빈값일경우를 위한 널체크
                    if(cell==null){
                        continue;
                    }else{
                        //타입별로 내용 읽기
                        switch (cell.getCellType()){
                        case XSSFCell.CELL_TYPE_FORMULA:
                            value=cell.getCellFormula();
                            break;
                        case XSSFCell.CELL_TYPE_NUMERIC:
                            value=cell.getNumericCellValue()+"";
                            break;
                        case XSSFCell.CELL_TYPE_STRING:
                            value=cell.getStringCellValue()+"";
                            break;
                        case XSSFCell.CELL_TYPE_BLANK:
                            value=cell.getBooleanCellValue()+"";
                            break;
                        case XSSFCell.CELL_TYPE_ERROR:
                            value=cell.getErrorCellValue()+"";
                            break;
                        }
                    }
                    System.out.println(rowindex+"번 행 : "+columnindex+"번 열 값은: "+value);
                    if(columnindex==0)
                       fvalue="'"+value+"'";
                    else
                       fvalue = fvalue +","+ "'"+value+"'";
                
                }
                
                System.out.println(rowindex+"번 행 : "+fvalue);
                
                Statement stat = conn.createStatement();
                sql="INSERT INTO binfo VALUES("+fvalue+")";
                int check= stat.executeUpdate(sql);
                
                fvalue=null;

            }
        }
        
        out.println("<h1>파일 읽어서 DB에 넣기 완료</h1>");
        
    }catch(Exception e) {
        e.printStackTrace();
    }
    
    
    kuber kubernetes_deployment = new kuber();
    try {
		kubernetes_deployment.kubernetes(id,String.valueOf(node));
	} catch (ApiException e) {
		e.printStackTrace();
	}
   
}
    
}