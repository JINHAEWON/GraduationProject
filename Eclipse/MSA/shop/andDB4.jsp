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

//model number �޾ƿ��� �� 
String Jid=request.getParameter("et_id");


//public String connectionDB(String id, String pwd) �̰� �Լ� ���
String returns = connectDB.connectionDB(Jid);

//eclipse �ֿܼ� returns �� ���� 
System.out.println(returns);

//�ȵ���̵忡 ���� (XML ���Ϸ� ��ȯ�ؾ���)
out.println(returns);
%>
</body>
</html>