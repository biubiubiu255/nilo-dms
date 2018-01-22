<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="common/header.jsp" %>
<link rel="stylesheet" href="${ctx}/dist/css/dm-notif-style.css">
<body class="hold-transition skin-blue sidebar-mini fixed">

<!--Top Nav-->
<%@ include file="common/nav.jsp" %>
<!--Left Sidebar-->
<%@ include file="common/sidebar.jsp" %>
<!-- Content Wrapper. Contains page content -->
<div class="content-wrapper" id="content-wrapper" style="min-height: 421px;">
    <!--bootstrap tab风格 多标签页-->
    <div class="content-tabs">
        <button class="roll-nav roll-left tabLeft" onclick="scrollTabLeft()">
            <i class="fa fa-backward"></i>
        </button>
        <nav class="page-tabs menuTabs tab-ui-menu" id="tab-menu">
            <div class="page-tabs-content" style="margin-left: 0px;">

            </div>
        </nav>
        <button class="roll-nav roll-right tabRight" onclick="scrollTabRight()">
            <i class="fa fa-forward" style="margin-left: 3px;"></i>
        </button>
        <div class="btn-group roll-nav roll-right">
            <button class="dropdown tabClose" data-toggle="dropdown">
                Tab <i class="fa fa-bars" style="padding-left: 3px;"></i>
            </button>
            <ul class="dropdown-menu dropdown-menu-right" style="min-width: 128px;">
                <li><a class="tabReload" href="javascript:refreshTab();">Reload</a></li>
                <li><a class="tabCloseCurrent" href="javascript:closeCurrentTab();">Close Current</a></li>
                <li><a class="tabCloseAll" href="javascript:closeOtherTabs(true);">Close All</a></li>
                <li><a class="tabCloseOther" href="javascript:closeOtherTabs();">Close Others</a></li>
            </ul>
        </div>
        <button class="roll-nav roll-right fullscreen" onclick="App.handleFullScreen()"><i
                class="fa fa-arrows-alt"></i></button>
    </div>
    <div class="content-iframe " style="background-color: #ffffff; ">

        <div class="tab-content " id="tab-content">
        </div>
    </div>
</div>
<div id="dm-notif"></div>
<!-- /.content-wrapper -->
<footer class="main-footer">
    <div class="pull-right hidden-xs">
        <b>Version</b> 1.0
    </div>
    <strong>Copyright &copy; 2017-2027 <a href="#">Nilo</a>.</strong> All rights reserved.
</footer>

<!--Bottom Footer-->
<%@ include file="common/footer.jsp" %>
<script src="${ctx}/dist/js/dm_notify.js"></script>

<script>

    $("input[name='menu_name']").keydown(function (event) {
        event = document.all ? window.event : event;
        if ((event.keyCode || event.which) == 13) {
            search_menu();
        }
    });

    /**
     * 本地搜索菜单
     */
    function search_menu() {
        //要搜索的值
        var text = $('input[name=menu_name]').val();
        if (!text) {
            return;
        }
        var $ul = $('.sidebar-menu');
        $ul.find("a.nav-link").each(function () {
            var $a = $(this).css("border", "");
            //判断是否含有要搜索的字符串
            if ($a.children("span.menu-text").text().indexOf(text) >= 0) {
                //如果a标签的父级是隐藏的就展开
                $ul = $a.parents("ul");
                if ($ul.is(":hidden")) {
                    $a.parents("ul").prev().click();
                }
                //点击该菜单
                $a.click();
            }
        });
    }

    $(function () {
        App.setbasePath("../");
        App.setGlobalImgPath("dist/img/");

        addTabs({
            id: '999999',
            title: 'Home',
            close: false,
            url: 'home.html',
            urlType: "relative"
        });

        App.fixIframeCotent();

        var menus = eval('${menu}');
        $('.sidebar-menu').sidebarMenu({data: menus});

        //修改密码
        $("a.change-password").click(function () {
            var title = "Change Password";
            var url = "admin/user/passwordView.html";
            $.ajax({
                url: url,
                type: 'POST',
                dataType: 'text',
                success: function (data) {
                    layer.open({
                        type: 1,
                        title: title,
                        area: ['700px', '550px'],
                        content: data
                    });
                }
            });
        });
        $("a.sign-out").click(function () {
            var url = "account/logout.html";
            window.location.href = url;
        });
        
    });
</script>

</body>
