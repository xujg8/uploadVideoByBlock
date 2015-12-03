<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
<script src="<%=request.getContextPath()%>/assets/libs/jquery/1.11.1/jquery.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/assets/js/login.js" type="text/javascript"></script>
</head>
<body>
<!-- <form action="login"  method="POST"> -->
		账号：<input type = "text"  name = "account" /><br>
		密码：<input type = "password" name = "passwd" /> <br>
		<a href="javascript:void(0)" id="loginBtn" title="Login" data-submit-url="/recipevideo/login.json" data-redirect-url="/recipevideo/upload.do" >Confirm</a>
<!-- 	</form> -->
</body> 
</html>