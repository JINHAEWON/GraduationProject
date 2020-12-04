<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>

<head>
<link href="https://fonts.googleapis.com/css2?family=Big+Shoulders+Stencil+Display:wght@800&family=Black+Han+Sans&display=swap" rel="stylesheet">
<meta charset="EUC-KR">
<style>
#btn1 button{
border:1px solid #F9E05D;
background-color:rgba(0,0,0,0);
color:#F9E05D;
padding:5px;
border-top-right-radius:5px;
border-bottom-right-radius:5px;
}
#btn1 button:hover{
color:white;
background-color:#F9E05D;}
html,
body{
background-color: #F5F6F7;
height:100%;
}
.container {
display: flex;
flex-direction: column;
width:100%;
height:100%;
align-items:center;
justify-content:center;
}
p{
font-family:"Big Shoulders Stencil Display",sans-serif;
font-size: 60px;
}
p.a{
font-weight:300;
}
</style>
<title>업로드 완료</title>
</head>
<body>
<div class="container">
<p class="a"><font color="#F9E05D">S-Basket</font></p>
<font color="#272727">업로드가 완료되었습니다.</font>
<div id="btn1"><Button id="btn1" onClick="location='uploadDB.jsp'">제품 등록 화면으로</button></div>
</div>

</body>
</html>