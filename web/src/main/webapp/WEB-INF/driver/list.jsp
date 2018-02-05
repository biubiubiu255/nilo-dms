<%@ page import="org.apache.commons.lang.RandomStringUtils" %>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<html>
<%@ include file="../common/header.jsp" %>
<%
    request.setAttribute("id0", RandomStringUtils.randomAlphabetic(8));
%>
<body>
<div class="box-body">

    <div class="layui-form layui-row">
        <div class="layui-col-md4 layui-col-lg3">
            driverName：
            <div class="layui-inline">
                <input class="layui-input" id="searchVal" autocomplete="off">
            </div>
        </div>
        <div class="layui-col-md1">
            <shiro:hasPermission name="300021">
                <button class="layui-btn layui-btn-normal search">Search</button>
            </shiro:hasPermission>
        </div>
    </div>
    <hr>
    <div class="layui-row">
        <div class="layui-col-md3">
            <shiro:hasPermission name="300022">
                <button class="layui-btn layui-btn-normal add-user" id="10001">Add</button>
            </shiro:hasPermission>
        </div>
    </div>

    <table class="layui-table"
           lay-data="{ url:'/admin/driver/list.html',method:'post', page:true,limit:10, id:'${id0}'}"
           lay-filter="demo">
        <thead>
        <tr>
            <th lay-data="{fixed: 'left',field:'id', width:100, align:'center', templet: '<div>{{d.id}}</div>'}">No</th>
            <th lay-data="{field:'driverName', width:150, align:'center', templet: '<div>{{d.driverName}}</div>'}">driverName</th>
            <th lay-data="{field:'expressName', width:150, align:'center',templet: '<div>{{d.expressName}}</div>'}">expressName</th>
            <th lay-data="{field:'driverId', width:150, align:'center',templet: '<div>{{d.driverId}}</div>'}">driverId</th>
            <th lay-data="{field:'phone', width:150, align:'center',templet: '<div>{{d.phone}}</div>'}">phone</th>
            <th lay-data="{field:'CreatedTime', width:170, templet:'<div>{{ formatDate(d.createdTime) }}</div>'}">CreatedTime</th>
            <th lay-data="{title:'Opt',fixed: 'right', width:172, align:'center', toolbar: '#barDemo'}"></th>
        </tr>

        </thead>
    </table>

    <script type="text/html" id="barDemo">
        <shiro:hasPermission name="300023">
            <a class="layui-btn layui-btn-normal layui-btn-mini" lay-event="edit">Edit</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="300024">
            <a class="layui-btn layui-btn-danger layui-btn-mini" lay-event="delete">Delete</a>
        </shiro:hasPermission>
    </script>
</div>


<!--Bottom Footer-->
<%@ include file="../common/footer.jsp" %>
<script type="text/javascript">

    function formatUserStatus(status) {
        if (status == 1) {
            return "<Font color='##1E9FFF'>Normal</Font>";
        }
        if (status == 2) {
            return "<Font color='red'>Disabled</Font>";
        }
    }

    $(function () {

        var table;
        layui.use('table', function () {
            table = layui.table;
            //监听工具条
            table.on('tool(demo)', function (obj) {
                var data = obj.data;

                if (obj.event === 'edit') {
                    edit(data);
                }
                if (obj.event === 'delete') {
                    del(data);
                }
                if (obj.event === 'active') {
                    activeUser(data);
                }
                if (obj.event === 'reset') {
                    resetPassword(data);
                }
            });

            $(".search").on("click", function () {
                reloadTable();
            })


        });

        var reloadTable = function (item) {
            table.reload("${id0}", {
                where: {
                	driverName: $("#searchVal").val()
                }
            });
        };

        $("button.add-user").on("click", function () {
        	add();
        });

        function resetPassword(userId) {
            //询问框
            layer.confirm('Confirm to Reset Password?', {
                btn: ['OK', 'Cancel']
                //按钮
            }, function () {
                $.ajax({
                    type: "POST",
                    url: "/admin/express/resetPassword.html",
                    dataType: "json",
                    data: {
                        userId: userId
                    },
                    success: function (data) {
                        if (data.result) {
                            layer.msg('SUCCESS', {icon: 1, time: 2000}, function () {
                            });
                        } else {
                            layer.msg("Failed. " + data.msg, {icon: 2});
                        }
                    }
                });
            });
        }

        function del(d) {
            //询问框
            layer.confirm('Confirm to delete?', {
                btn: ['OK', 'Cancel']
                //按钮
            }, function () {
                $.ajax({
                    type: "POST",
                    url: "/admin/driver/dele.html",
                    dataType: "json",
					data : {
						id : d.id
					},
					success : function(data) {
						if (data.result) {
							layer.msg('SUCCESS', {
								icon : 1,
								time : 2000
							}, function() {
								reloadCurrentPage();
							});
						} else {
							layer.msg("Failed. " + data.msg, {
								icon : 2
							});
						}
					}
				});
			});
		}

		function activeUser(userId) {
			//询问框
			layer.confirm('Confirm to active?', {
				btn : [ 'OK', 'Cancel' ]
			//按钮
			}, function() {
				$.ajax({
					type : "POST",
					url : "/admin/driver/activeUser.html",
					dataType : "json",
					data : {
						userId : userId
					},
					success : function(data) {
						if (data.result) {
							layer.msg('SUCCESS', {
								icon : 1,
								time : 2000
							}, function() {
								reloadCurrentPage();
							});
						} else {
							layer.msg("Failed. " + data.msg, {
								icon : 2
							});
						}
					}
				});
			});
		}

		function edit(d) {
			var load = layer.load(2);
			var url = "/admin/driver/edit.html?id=" + d.id
					+ "&driverId=" + d.driverId + "&driverName="
					+ d.driverName + "&phone="  + d.phone 
					+ "&expressName=" + d.expressName;
			var title = "Edit Driver";
			$.ajax({
				url : url,
				type : 'GET',
				dataType : 'text',
				success : function(data) {
					parent.layer.open({
						type : 1,
						title : title,
						area : [ '1000px', '500' ],
						offset : [ '100px', '250px' ],
						content : data ,
						end : function () {
                            reloadTable();
						}
					});
				},
				complete: function () {
                    layer.close(load);
                }
			});

		}

		function add() {
			var url = "/admin/driver/add.html";
			var title = "Add Driver";
			$.ajax({
				url : url,
				type : 'GET',
				dataType : 'text',
				success : function(data) {
					parent.layer.open({
						type : 1,
						title : title,
						area : [ '800px' ],
						offset : [ '100px', '250px' ],
						content : data,
						end : function () {
                            reloadTable();
						}
					});
				}
			});

		}
	});
</script>
</body>
</html>