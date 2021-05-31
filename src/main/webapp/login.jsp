<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String basePath = request.getScheme()+"://"+request.getServerName()+":"+
request.getServerPort()+request.getContextPath()+"/";
%>
<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
<meta charset="UTF-8">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
	<script>
		$(function () {

			//如果当前页面不是顶级窗口，则设置当前窗口为顶级窗口
			if ( window.top!=window){
				window.top.location=window.location;
			}

			//页面加载完毕后，将用户文本框中的内容清空
			$("#loginAct").val("");

			//页面加载完毕后，让用户的文本框自动获得焦点
			$("#loginAct").focus();

			//为登录按钮绑定事件，执行登录操作
			$("#submitBtn").click(function () {
				login();
			})

			//为当前登录页窗口绑定敲键盘事件
			//event可以获得我们敲的是哪个键
			$(window).keydown(function (event) {
				if (event.keyCode == 13){
					//执行登录操作
					login();
				}
			})
		})

		//普通的自定义function方法，一定要写在$(function (){})的外面
		function login() {
			//验证账号密码不为空
			//取得账号密码
			//将文本中的左右空格去掉,使用$.trim(文本)
			var loginAct = $.trim($("#loginAct").val());
			var loginPwd = $.trim($("#loginPwd").val());

			if ( loginAct == "" || loginPwd == ""){
				$("#msg").html("账号密码不能为空");

				//如果账号密码为空，则强行终止登录方法
				return false;
			}

			//去后台验证登录相关操作
			$.ajax({
				url:"login.do",
				data:{
					"loginAct" : loginAct,
					"loginPwd" : loginPwd
				},
				type:"post",
				dataType:"json",
				success:function (data) {

					/*
					 	data
					 		{"success":true/false,"msg":"哪错了"}
					*/

					//如果登录成功
					if ( data.success){

						//跳转到工作台的初始页
						window.location.href="workbench/index.jsp";

						//如果登录失败
					}else {

						$("#msg").html(data.msg);

					}
				}
				
			})
		}
	</script>
</head>
<body>
	<div style="position: absolute; top: 0px; left: 0px; width: 60%;">
		<img src="image/IMG_7114.JPG" style="width: 100%; height: 90%; position: relative; top: 50px;">
	</div>
	<div id="top" style="height: 50px; background-color: #3C3C3C; width: 100%;">
		<div style="position: absolute; top: 5px; left: 0px; font-size: 30px; font-weight: 400; color: white; font-family: 'times new roman'">CRM &nbsp;<span style="font-size: 12px;">&copy;2021曾大仙</span></div>
	</div>
	
	<div style="position: absolute; top: 120px; right: 100px;width:450px;height:400px;border:1px solid #D5D5D5">
		<div style="position: absolute; top: 0px; right: 60px;">
			<div class="page-header">
				<h1>登录</h1>
			</div>
			<!--workbench/index.jsp-->
			<form action="workbench/index.jsp" class="form-horizontal" role="form" method="get">
				<div class="form-group form-group-lg">
					<div style="width: 350px;">
						<input class="form-control" type="text" placeholder="用户名" id="loginAct" >
					</div>
					<div style="width: 350px; position: relative;top: 20px;">
						<input class="form-control" type="password" placeholder="密码" id="loginPwd" >
					</div>
					<div class="checkbox"  style="position: relative;top: 30px; left: 10px;">
						
							<span id="msg" style="color: red"></span>
						
					</div>
					<!--
						注意：让按钮写在form表单中，默认的行为就是提交表单
						一定要将按钮的类型设置为button
						按钮所触发的行为应该是由我们自己手动写js代码来决定
					-->
					<button type="button" id="submitBtn" class="btn btn-primary btn-lg btn-block"  style="width: 350px; position: relative;top: 45px;">登录</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>