<%@page import="com.db.ConnectDB4"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title>Insert title here</title>
</head>
<body>
<%
ConnectDB4 connectDB = ConnectDB4.getInstance();

//model number 받아오는 것 
String Jid=request.getParameter("et_id");


//public String connectionDB(String id, String pwd) 이거 함수 사용
String returns = connectDB.connectionDB(Jid);

//eclipse 콘솔에 returns 값 찍어내기 
System.out.println(returns);

//안드로이드에 전송 (XML 파일로 변환해야함)
out.println(returns);
%>
</body>
</html>