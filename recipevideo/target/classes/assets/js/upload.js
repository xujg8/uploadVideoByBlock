$(function() {
	var interval; //定义一个定时器
	
	$('#uploadBtn').click(function() {
		var postData = {};
		postData['fileDownUrl'] = $('input[name="fileDownUrl"]').val();
		postData['fileName'] = $("input[name=fileName]").val();
		$.each(postData, function(index, value) {
			postData[index] = $.trim(postData[index]);
		});
		run();
		$.ajax({
			type : 'POST',
			url : $('#uploadBtn').data('submit-url'),
			cache : false,
			data : postData,
			success : function(data) {
				clearTimeout(interval);
				if (data.code == 0) {
					$("#resp").val(data.data+"\r\n");
				} else {
					var msg = data.msg;
					if (msg) {
						msg = msg.replace(";", ";\r\n")
					}
					$("#resp").val(msg);
				}
			},
			error : function() {
				layer.close(sublayer);
				alert('Network error!');
			}
		});
	})

	
	function run() {
		interval = setInterval(getrate, "1000"); //定时的设置
	}
	function getrate(){
		var postData = {};
		//alert('get rate start');
		$.ajax({
			type : 'POST',
			url : $('#uploadBtn').data('query-url'),
			cache : false,
			data : postData,
			success : function(data) {
				if (data.code == 0) {
//					var oldmsg = $("#resp").val() + "\r\n";
					$("#resp").val(data.data+"\r\n");
				} else {
					var msg = data.msg;
					if (msg) {
						msg = msg.replace(";", ";\r\n")
					}
					$("#resp").val(msg+"\r\n");
				}
			},
			error : function() {
				layer.close(sublayer);
				alert('Network error!');
			}
		});
	}
	
	$('#testbtn').click(function() {
		var postData = {};
		$.ajax({
			type : 'POST',
			url : $('#testbtn').data('query-url'),
			cache : false,
			data : postData,
			success : function(data) {
				alert(data.code);
				if (data.code == 0) {
					alert('suc');
					if($.trim(data.data)==""){
						
					}else{
						//var oldmsg = $("#resp").val() + "\r\n";
						$("#resp").val("\r\n"+data.data);
					}
					
				} else {
					var msg = data.msg;
					if (msg) {
						msg = msg.replace(";", ";\r\n")
					}
					//var oldmsg = $("#resp").val() + "\r\n";
					$("#resp").val(msg+"\r\n");
				}
			},
			error : function() {
				layer.close(sublayer);
				alert('Network error!');
			}
		});
	})
});