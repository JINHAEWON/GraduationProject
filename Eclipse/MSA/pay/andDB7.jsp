<%@page import="com.db.ConnectDB7"%>
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
ConnectDB7 connectDB = ConnectDB7.getInstance();

//����ڰ� �Է��� id�� pw�� http ������� ������ �� �ִ� ��� 
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