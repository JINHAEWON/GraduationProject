<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="dbtesting.Excel"%>
<!DOCTYPE html>
<html>
<head>
<link href="https://fonts.googleapis.com/css2?family=Black+Han+Sans&display=swap" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Big+Shoulders+Stencil+Display:wght@800&family=Black+Han+Sans&display=swap" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Nanum+Gothic+Coding&display=swap" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@500&display=swap" rel="stylesheet">
<style>
o{
font-family:"Noto Sans KR",san-serif;
font-size:20px;
}
o.a{font-weight:80;}
p{
font-family:"Big Shoulders Stencil Display",sans-serif;
font-size: 60px;
}
p.a{
font-weight:300;
}
t{
font-family:"Nanum Gothic Coding",sans-serif;
font-size: 20px;
}
t.a{
font-weight:200;
}
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
.input{
width:150px;
}
.input:focus{
animation-name:border-focus;
animation-duration:1s;
animation-fill-mode:forwards;
}
@keyframes border-focus{
from{
border-radius:0;
}
to{
border-radius:15px;
}
}
.filebox input[type="file"] {
position: absolute;
width: 0;
height: 0;
padding: 0;
overflow: hidden;
border: 0;
}
.filebox label {
display: inline-block;
padding: 10px 20px;
color: #424242;
vertical-align: middle;
background-color: #F9E05D;
cursor: pointer;
border: 1px solid #ebebeb;
border-radius: 5px;
font-weight:bold;
}
.filebox .upload-name {
display: inline-block;
  height: 35px;
  font-size:18px; 
  padding: 0 10px;
vertical-align: middle;
background-color: #f5f5f5;
  border: 1px solid #F9E05D;
  border-radius: 5px;

}
input[type="radio"]:not(old)+label{
display:inline-block;
background:#f5f5f5;

}
input[type="radio"]:not(old):checked+label{
display:inline-block;
background:#F9E05D;
}
</style>
</head>
<body>
      <form action="<%= application.getContextPath() %>/fileUpload"  method="post" enctype="multipart/form-data">            
             <div class="container">
             <p class="a"><font color="#F9E05D">S-Basket</font></p>
             <input class=input" type="text" placeholder="아이디" name="input_id" style="height:30px;width:300px;border: 1px solid #F9E05D;"/>
           <br>
            <input class=input" type="password" placeholder="비밀번호" name="input_pw" style="height:30px;width:300px;border: 1px solid #F9E05D;"/>
            <br>
            <o class="a"><div>
            <input type="radio" id="QR Code" name="Code" value="QR Code" >
            <label for="QR">QR Code</label>
           
            <input type="radio" id="BarCode" name="Code" value="BarCode" >
            <label for="BarCode">BarCode</label>
           
            <input type="radio" id="NFC" name="Code" value="NFC"  >
            <label for="NFC">NFC</label>
            </div></o><br>
            <o class="a"><div>
            <input type="radio" id="QR Code" name="Card" value="Credit">
            <label for="QR">카드 결제</label>
           
            <input type="radio" id="BarCode" name="Cash" value="Cash">
            <label for="BarCode">무통장 입금</label>
            </div></o><br>
            <t class="a">제품 목록 업로드(엑셀 파일)</t>
            <br>
            <div class="filebox">
            <label for="file">업로드</label>
            <input type="file" id="file" name="file1" onchange="javascript:document.getElementById('fileName').value=this.value">
            <input type="text" id="fileName" class="upload-name" value="파일 이름">
            </div>
            <br>
            <input type="submit" value="전송" style="background-color:#F9E15D;height:30px;width:50px;border:1px solid #F9E15D;">
            </div>
      </form>

</body>
</html>