<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
<script src="<%=request.getContextPath()%>/assets/libs/jquery/1.11.1/jquery.min.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/assets/js/upload.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>菜谱文件上传</title>
</head>
<body>

		URL：<input type="text" size="100" name="fileDownUrl" /><br>
		文件名（默认使用原文件名）：<input type="text" name="fileName" /><br> 
		<TEXTAREA NAME="resp" readonly="readonly" ROWS="30" COLS="120" id="resp">  </TEXTAREA></br>
		<a href="javascript:void(0)"
			id="uploadBtn" title="Upload" data-submit-url="/recipevideo/upload.json" data-query-url="/recipevideo/query.json"> Confirm</a>

</body>
</html>