<%@page import="com.db.ConnectDB2"%>
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
ConnectDB2 connectDB = ConnectDB2.getInstance();

//����ڰ� �Է��� id�� pw�� http ������� ������ �� �ִ� ��� 
String Jid=request.getParameter("et_id");
String Jpw=request.getParameter("et_pw");
String Jname=request.getParameter("et_name");
String Jemail=request.getParameter("et_email");
String Jphone=request.getParameter("et_phone");


//public String connectionDB(String id, String pwd) �̰� �Լ� ���
String returns = connectDB.connectionDB(Jid,Jpw,Jname,Jemail,Jphone);

//eclipse �ֿܼ� returns �� ���� 
System.out.println(returns);

//�ȵ���̵忡 ���� (XML ���Ϸ� ��ȯ�ؾ���)
out.println(returns);
%>
</body>
</html>