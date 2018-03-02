<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>

<header class="main-header">
    <!-- Logo -->
    <a href="dashboard.html" class="logo">
        <!-- mini logo for sidebar mini 50x50 pixels -->
        <span class="logo-mini">DMS</span>
        <!-- logo for regular state and mobile devices -->
        <span class="logo-lg"><b>Nilo</b>DMS</span>
    </a>
    <!-- Header Navbar: style can be found in header.less -->
    <nav class="navbar navbar-static-top">
        <!-- Sidebar toggle button-->
        <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </a>

        <div class="navbar-custom-menu">
            <ul class="nav navbar-nav">
                <!-- Messages: style can be found in dropdown.less-->
                
                <!-- Notifications: style can be found in dropdown.less -->
                <!-- Tasks: style can be found in dropdown.less -->
                
                <!-- User Account: style can be found in dropdown.less -->
                <li class="dropdown user user-menu">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <!-- <img src="../dist/img/user2-160x160.jpg" class="user-image" alt="User Image"> -->
                        <span class="hidden-xs">${sessionScope.userName}
                        </span>
                    </a>
                    <ul class="dropdown-menu">
                        <!-- User image -->
                        <%--
                        <li class="user-header">
                            <!-- <img src="../dist/img/user2-160x160.jpg" class="img-circle" alt="User Image"> -->

                            <p>
                                ${sessionScope.userName}- ${sessionScope.name}
                            </p>
                        </li>
                        --%>
                        <!-- Menu Body -->
                        <!-- <li class="user-body">
                            <div class="row">
                                <div class="col-xs-4 text-center">
                                    <a href="#">Followers</a>
                                </div>
                                <div class="col-xs-4 text-center">
                                    <a href="#">Sales</a>
                                </div>
                                <div class="col-xs-4 text-center">
                                    <a href="#">Friends</a>
                                </div>
                            </div>
                            /.row
                        </li> -->
                        <!-- Menu Footer-->
                        <li class="user-footer">
                            <div class="pull-left">
                                <a href="#" class="btn btn-default btn-flat change-password">Change Password</a>
                            </div>
                            <div class="pull-right">
                                <a href="#" class="btn btn-default btn-flat sign-out">Sign out</a>
                            </div>
                        </li>
                    </ul>
                </li>

            </ul>
        </div>
    </nav>
</header>

