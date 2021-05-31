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
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

	<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css">
	<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
	<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>

	<script type="text/javascript">

	$(function(){
		$(".time").datetimepicker({
			minView: "month",
			language: "zh-CN",
			format: "yyyy-mm-dd",
			autoclose: true,
			todayBtn: true,
			pickerPosition: "bottom-left"
		});

		//为创建按钮绑定事件，打开创建市场活动界面
		$("#addBtn").click(function () {

			/*$(".time").datetimepicker({
				minView: "month",
				language: "zh-CN",
				format: "yyyy-mm-dd",
				autoclose: true,
				todayBtn: true,
				pickerPosition: "bottom-left"
			});*/

			//走后台，为下拉列表获取用户的名字
			$.ajax({
				url:"workbench/activity/add.do",
				dataType:"json",
				success:function (data) {
					var html = "<option></option>";

					$.each(data,function (i,obj) {

						html += "<option value='"+obj.id+"'>"+obj.name+"</option>"

					})
					//在字符串拼接完毕后，在外面用html函数把拼好的字符串当做子标签添加进来
					$("#create-marketActivityOwner").html(html);

					//获取登录用户的id，注意：在js代码中，EL表达式要写在双引号中
					var id = "${user.id}";
					//让value=id的标签为默认选中项
					$("#create-marketActivityOwner").val(id);
				}
			})


			$("#createActivityModal").modal("show");

		})

		//为保存按钮绑定事件，执行添加操作
		$("#saveBtn").click(function () {
			$.ajax({
				url:"workbench/activity/save.do",
				data:{
					"owner":$.trim($("#create-marketActivityOwner").val()),
					"name":$.trim($("#create-marketActivityName").val()),
					"startDate":$.trim($("#create-startTime").val()),
					"endDate":$.trim($("#create-endTime").val()),
					"cost":$.trim($("#create-cost").val()),
					"description":$.trim($("#create-describe").val())
				},
				type:"post",
				dataType: "json",
				success:function (data) {

					if( data.success){

						//对于表单的jquery对象没有提供reset方法，所以要转换成dom对象，原生js提供了reset方法
						$("#activityAddForm")[0].reset();
						//关闭模态窗口
						$("#createActivityModal").modal("hide");

					}else {
						alert("创建失败");
					}

				}
			})
		})

		//页面加载完毕后触发的一个方法
		//默认展开列表第一页，每页两条记录
		pageList(1,2);

		//为查询按钮绑定事件，将查询出来的市场活动列表展现出来
		$("#searchBtn").click(function () {
		    /*
		        点击查询按钮时，我们应该将搜索框中的信息保存到隐藏域中
		    */

		    $("#hidden-name").val($.trim($("#search-name").val()));
            $("#hidden-owner").val($.trim($("#search-owner").val()));
            $("#hidden-startDate").val($.trim($("#search-startTime").val()));
            $("#hidden-endDate").val($.trim($("#search-endTime").val()));

			pageList(1,2);
		})

		//为全选的复选框绑定事件，触发全选操作
		$("#qx").click(function () {

			$("input[name=xz]").prop("checked",this.checked);
		})

		//动态生成的标签要用on的形式来绑定事件
		//是给动态生成的标签的外面一层的标签来绑定
		$("#activityBody").on("click",$("input[name=xz]"),function () {

			$("#qx").prop("checked",$("input[name=xz]").length==$("input[name=xz]:checked").length)
		})

		//为删除按钮绑定事件，执行市场活动删除操作
		$("#deleteBtn").click(function () {

			var $xz = $("input[name=xz]:checked");

			if( $xz.length == 0){

				alert("请选择要删除的活动")

			}else {

				//给用户提示，问他是否删除
				if( confirm("确定删除所选中的记录吗？")){

					//workbench/delete.do?id=xxx&xxx...
					//拼接参数
					var param = "";

					for ( var i = 0;i<$xz.length;i++){

						param += "id="+$($xz[i]).val();

						//如果不是最后一个id的话，加上&符号
						if (i<$xz.length-1){
							param += "&";
						}

					}

					$.ajax({
						url:"workbench/activity/delete.do",
						data:param,
						dataType:"json",
						success:function (data) {

							if( data.success){

								pageList(1,2);

							}else {

								alert("删除市场活动失败");

							}
						}
					})
				}



			}
		})

		//为修改按钮绑定事件，打开修改界面，同时走后台展示要修改的市场活动的数据
		$("#editBtn").click(function () {

			//获取被选中的市场活动jquery对象
			var $xz = $("input[name=xz]:checked");

			if ($xz.length != 1){
				alert("请选中一项市场活动进行修改");
			}else {
				$.ajax({
					url:"workbench/activity/edit.do",
					data:{
						"id":$xz.val()
					},
					dataType:"json",
					success:function (data) {


						var html = "";
						$.each(data.list,function (i,n) {

							if (data.activity.owner==n.id){

								html += "<option value='"+n.id+"' selected>"+n.name+"</option>";
							}else {

								html += "<option value='"+n.id+"'>"+n.name+"</option>";
							}

						})

						$("#edit-marketActivityOwner").html(html);

						//处理单条activity
						$("#edit-id").val(data.activity.id);
						$("#edit-marketActivityName").val(data.activity.name);
						$("#edit-startTime").val(data.activity.startDate);
						$("#edit-endTime").val(data.activity.endDate);
						$("#edit-cost").val(data.activity.cost);
						$("#edit-describe").val(data.activity.description);

						//所有的值填好后，打开修改界面
						$("#editActivityModal").modal("show");
					}
				})

			}
		})
		
		$("#updateBtn").click(function () {

			$.ajax({
				url:"workbench/activity/update.do",
				data:{
					"id":$("#edit-id").val(),
					"owner":$.trim($("#edit-marketActivityOwner").val()),
					"name":$.trim($("#edit-marketActivityName").val()),
					"startDate":$.trim($("#edit-startTime").val()),
					"endDate":$.trim($("#edit-endTime").val()),
					"cost":$.trim($("#edit-cost").val()),
					"description":$.trim($("#edit-describe").val())
				},
				type:"post",
				success:function (data) {

					if (data.success){

						pageList(1,2);
						$("#editActivityModal").modal("hide");
					}else {
						alert("修改市场活动失败");
					}


				}
			})


		})

	});

	//展示市场活动列表函数
	function pageList(pageNo,pageSize) {
		//将全选的复选框的√去掉
		$("#qx").prop("checked",false);

	    //查询前，将隐藏域中保存的信息提取出来，重新赋予到搜索框
        $("#search-name").val($.trim($("#hidden-name").val()));
        $("#search-owner").val($.trim($("#hidden-owner").val()));
        $("#search-startDate").val($.trim($("#hidden-startTime").val()));
        $("#search-endDate").val($.trim($("#hidden-endTime").val()));

		$.ajax({
			url:"workbench/activity/pageList.do",
			data: {
				"pageNo":pageNo,
				"pageSize":pageSize,
				"name":$.trim($("#search-name").val()),
				"owner":$.trim($("#search-owner").val()),
				"startDate":$.trim($("#search-startTime").val()),
				"endDate":$.trim($("#search-endTime").val())
			},
			type: "get",
			dataType:"json",
			success:function (data) {

				var html="";

				/*
					{"total":100,"dataList":[{市场活动1},{市场活动2}{3}]}
				 */

				//每一个n都是一个市场活动对象
				$.each(data.dataList,function (i,n) {
					html += '<tr class="active">';
					html += '<td><input type="checkbox" name="xz" value="'+n.id+'" /></td>';
					html += '<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href=\'workbench/activity/detail.jsp\';">'+n.name+'</a></td>';
					html += '<td>'+n.owner+'</td>';
					html += '<td>'+n.startDate+'</td>';
					html += '<td>'+n.endDate+'</td>';
					html += '</tr>';
				})

				$("#activityBody").html(html);

				var totalPages = data.total%pageSize==0?data.total/pageSize:parseInt(data.total/pageSize)+1;

				//数据处理完毕后，结合分页查询，对前端展现分页信息
				$("#activityPage").bs_pagination({
					currentPage: pageNo, // 页码
					rowsPerPage: pageSize, // 每页显示的记录条数
					maxRowsPerPage: 20, // 每页最多显示的记录条数
					totalPages: totalPages, // 总页数
					totalRows: data.total, // 总记录条数

					visiblePageLinks: 3, // 显示几个卡片

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					//在点击分页组件时触发该函数
					onChangePage : function(event, data){
						pageList(data.currentPage , data.rowsPerPage);
					}
				});

			}

		})
	}

	
</script>
</head>
<body>

    <input type="hidden" id="hidden-name">
    <input type="hidden" id="hidden-owner">
    <input type="hidden" id="hidden-startDate">
    <input type="hidden" id="hidden-endDate">

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">

					<!--给表单赋值id属性，用来重置表单-->
					<form id="activityAddForm" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
								  <!--
									  这里在js代码中实现获取User的name
								  -->

								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startTime" readonly>
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endTime" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<!--data-dismiss="modal"表示关闭模态窗口-->
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="saveBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">

						<!--隐藏域保存修改市场活动的id-->
						<input type="hidden" id="edit-id">
					
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
								  <%--<option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>--%>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label ">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startTime" readonly >
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endTime" readonly >
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" >
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control time" type="text" id="search-startTime" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control time" type="text" id="search-endTime">
				    </div>
				  </div>
				  
				  <button type="button" id="searchBtn" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
					<!--
						以后不这样写，因为不能对按钮功能实现扩充，
						<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#createActivityModal"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  		<button type="button" class="btn btn-default" data-toggle="modal" data-target="#editActivityModal"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
						所以未来的实际开发中，不要写死在元素中，
						应该由我们在js代码中来操作
					-->
				  <button type="button" class="btn btn-primary" id="addBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx" /></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
						<%--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='workbench/activity/detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 30px;">

				<div id="activityPage"></div>


			</div>
			
		</div>
		
	</div>
</body>
</html>