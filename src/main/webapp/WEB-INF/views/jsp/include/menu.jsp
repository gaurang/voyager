<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

            <aside class="left-side sidebar-offcanvas">

                <section class="sidebar">
                    <!-- Sidebar user panel -->
                    <div class="user-panel">
                        <div class="pull-left image">
                            <img src="${pageContext.request.contextPath}/resources/core/img/avatar3.png" class="img-circle" alt="User Image" />
                        </div>
                        <div class="pull-left info">
                           <p>Hello, <sec:authentication property="principal.userDetails.fname"/> </p>

                            <a href="#"><i class="fa fa-circle text-success"></i> Online</a>
                        </div>
                    </div>
                    <!-- search form -->
                    <!-- <form action="#" method="get" class="sidebar-form">
                        <div class="input-group">
                            <input type="text" name="q" class="form-control" placeholder="Search..."/>
                            <span class="input-group-btn">
                                <button type='submit' name='seach' id='search-btn' class="btn btn-flat"><i class="fa fa-search"></i></button>
                            </span>
                        </div>
                    </form> -->
                    <!-- /.search form -->
                    <!-- sidebar menu: : style can be found in sidebar.less -->
                    <ul class="sidebar-menu">
                        <li class="active">
                            <a href="dashboard">
                                <i class="fa fa-dashboard"></i> <span>Dashboard</span>
                            </a>
                        </li>
                        <!--<li>
                            <a href="wf/widgets.html">
                                <i class="fa fa-th"></i> <span>Widgets</span> <small class="badge pull-right bg-green">new</small>
                            </a>
                        </li>-->
                        <li class="treeview">
                            <a href="#">
                                <i class="fa fa-group"></i>
                                <span>CSE</span>
                                <i class="fa fa-angle-left pull-right"></i>
                            </a>
                            <ul class="treeview-menu">
                                <li><a href="addDriver"><i class="fa fa-angle-double-right"></i> Register CSE</a></li>
                                <li><a href="listDrivers"><i class="fa fa-angle-double-right"></i> Edit CSE</a></li>
                                
                            </ul>
                        </li>
                        <li class="treeview">
                            <a href="#">
                                <i class="fa fa-user"></i>
                                <span>Corporate Customer</span>
                                <i class="fa fa-angle-left pull-right"></i>
                            </a>
                            <ul class="treeview-menu">
                                <li><a href="addCustomer"><i class="fa fa-angle-double-right"></i>Register Corporate</a></li>
								<li><a href="listCustomers"><i class="fa fa-angle-double-right"></i> Edit Corporate</a></li>
<!-- 								<li><a href="addPayment"><i class="fa fa-angle-double-right"></i> Add Payment</a></li>
 --><!-- 								<li><a href="emp"><i class="fa fa-angle-double-right"></i> Add Employee</a></li>
 -->
								<!-- <li><a href="updateCustomer"><i class="fa fa-angle-double-right"></i> Edit Individual</a></li> -->
                                
                            </ul>
                        </li>
                        
                        <li class="treeview">
                            <a href="#">
                                <i class="fa fa-mobile"></i>
                                <span>Asset</span>
                                <i class="fa fa-angle-left pull-right"></i>
                            </a>
                            <ul class="treeview-menu">
                                <li><a href="insertAsset"><i class="fa fa-angle-double-right"></i>Insert Asset</a></li>
								<li><a href="listAsset"><i class="fa fa-angle-double-right"></i>Modify Asset</a></li>
								<li><a href="manageAssets"><i class="fa fa-angle-double-right"></i>Manage Asset</a></li>
								<!-- <li><a href="updateCustomer"><i class="fa fa-angle-double-right"></i> Edit Individual</a></li> -->
                                
                            </ul>
                        </li>
        <!--                 <li class="treeview">
                            <a href="#">
                                <i class="fa fa-mobile"></i>
                                <span>Customer</span>
                                <i class="fa fa-angle-left pull-right"></i>
                            </a>
                            <ul class="treeview-menu">
                                <li><a href="insertAsset"><i class="fa fa-angle-double-right"></i>Search Customer</a></li>
                                
                            </ul>
                        </li>
         -->                
                        
<!--
                        <li class="treeview">
                            <a href="#">
                                <i class="fa fa-user"></i> <span>Users</span>
                                <i class="fa fa-angle-left pull-right"></i>
                            </a>
                            <ul class="treeview-menu">
                                <li><a href="pages/forms/general.html"><i class="fa fa-angle-double-right"></i> Add User</a></li>
                                <li><a href="pages/forms/advanced.html"><i class="fa fa-angle-double-right"></i> Edit User</a></li>
                                
                            </ul>
                        </li>
	                     <li class="treeview">
                            <a href="#">
                                <i class="fa fa-table"></i> <span>Configuration</span>
                                <i class="fa fa-angle-left pull-right"></i>
                            </a>
                            <ul class="treeview-menu">
                                <li><a href="pages/tables/simple.html"><i class="fa fa-angle-double-right"></i> Profile</a></li>
                                <li><a href="pages/tables/data.html"><i class="fa fa-angle-double-right"></i> Data tables</a></li>
                            </ul>
                        </li>
 -->
                     </ul>
                </section>
            </aside>
                