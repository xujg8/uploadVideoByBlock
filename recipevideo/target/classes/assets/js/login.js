$(function(){
    $('#loginBtn').click(function(){
        var postData = {};
        postData['account'] = $('input[name="account"]').val();
        postData['passwd']=$("input[name=passwd]").val();
        $.each(postData,function(index,value){
            postData[index] = $.trim(postData[index]);
        });	
        $.ajax({
            type:'POST',
            url:$('#loginBtn').data('submit-url'),
            cache:false,
            data:postData,
            success: function(data){
                if(data.code==0){
//                    alert("Login Success!")
                    location.href=$('#loginBtn').data('redirect-url');
                }
                else{
                    var msg=data.msg;
                    if(msg){
                        msg=msg.replace(";",";\r\n")
                    }
                    alert(msg);
                }
            },
            error:function(){
	            	layer.close(sublayer);
	            	alert('Network error!');
	            }
        });
	})

});