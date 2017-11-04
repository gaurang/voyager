<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>AdminLTE | Dashboard</title>
        <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>

       <jsp:include page="include/files.jsp"></jsp:include>
 
	</head>
    <body class="skin-black">
        <!-- header logo: style can be found in header.less -->
        <jsp:include page="include/header.jsp"></jsp:include>
        
        
        <div class="wrapper row-offcanvas row-offcanvas-left">
            <!-- Left side column. contains the logo and sidebar -->
            <aside class="left-side sidebar-offcanvas">
                <!-- sidebar: style can be found in sidebar.less -->
		        <jsp:include page="include/menu.jsp"></jsp:include>
                <!-- /.sidebar -->
            </aside>

            <!-- Right side column. Contains the navbar and content of the page -->
            <aside class="right-side">
                <!-- Content Header (Page header) -->
                <section class="content-header">
                    <h1>
                        Dashboard
                        <small>Control panel</small>
                    </h1>
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                        <li class="active">Dashboard</li>
                    </ol>
                </section>

                <!-- Main content -->
                <section class="content">

                    <!-- Small boxes (Stat box) -->
                    <div class="row">
                        <div class="col-lg-3 col-xs-6">
                            <!-- small box -->
                            <div class="small-box bg-aqua">
                                <div class="inner">
                                    <h3>
          	                             ${ridesDashboard['rideTotalAmt']!=null?ridesDashboard['rideTotalAmt']:0} $ 
                                    </h3>
                                    <p>
                                        From ${ridesDashboard['count']} Rides today
                                    </p>
                                </div>
                                <div class="icon">
                                    <i class="ion ion-bag"></i>
                                </div>
                                <a href="#" class="small-box-footer">
                                    More info <i class="fa fa-arrow-circle-right"></i>
                                </a>
                            </div>
                        </div><!-- ./col -->
                        <div class="col-lg-3 col-xs-6">
                            <!-- small box -->
                            <div class="small-box bg-yellow">
                                <div class="inner">
                                    <h3>
                                        ${regEmpCount}
                                    </h3>
                                    <p>
                                       REGISTERED EMPLOYEE
                                    </p>
                                </div>
                                <div class="icon">
                                    <i class="ion ion-person-add"></i>
                                </div>
                                <a href="#" class="small-box-footer">
                                    More info <i class="fa fa-arrow-circle-right"></i>
                                </a>
                            </div>
                        </div><!-- ./col -->
 <!--                       
						<div class="col-lg-3 col-xs-6">
                            <div class="small-box bg-red">
                                <div class="inner">
                                    <h3>
                                        $ 6,500 
                                    </h3>
                                    <p>
                                        Business Today
                                    </p>
                                </div>
                                <div class="icon">
                                    <i class="ion ion-pie-graph"></i>
                                </div>
                                <a href="#" class="small-box-footer">
                                    More info <i class="fa fa-arrow-circle-right"></i>
                                </a>
                            </div>
                        </div>
	-->					
						<!-- ./col -->
                    </div><!-- /.row -->

                    <!-- top row -->
                    <div class="row">
                        <div class="col-xs-12 connectedSortable">
                            
                        </div><!-- /.col -->
                    </div>
                    <!-- /.row -->

                    <!-- Main row -->
                    <div class="row">
                        <!-- Left col -->
                        <section class="col-lg-6 connectedSortable"> 
                            <!-- Box (with bar chart) -->
                            <div class="box box-danger" id="loading-example">
                                <div class="box-header">
                                    <!-- tools box -->
                                    <div class="pull-right box-tools">
                                        <button class="btn btn-danger btn-sm refresh-btn" data-toggle="tooltip" title="Reload"><i class="fa fa-refresh"></i></button>
                                        <button class="btn btn-danger btn-sm" data-widget='collapse' data-toggle="tooltip" title="Collapse"><i class="fa fa-minus"></i></button>
                                        <button class="btn btn-danger btn-sm" data-widget='remove' data-toggle="tooltip" title="Remove"><i class="fa fa-times"></i></button>
                                    </div><!-- /. tools -->
                                    <i class="fa"></i>

                                    <h3 class="box-title">Status</h3>
                                </div><!-- /.box-header -->
                                <div class="box-body no-padding">
                                    <div class="row">
                                        <div class="col-sm-7">
                                            <!-- bar chart -->
                                            <div class="chart" id="bar-chart" style="height: 250px;"></div>
                                        </div>
                                        <div class="col-sm-5">
                                            <div class="pad">
                                                <!-- Progress bars -->
                                                <div class="clearfix">
                                                    <span class="pull-left">Active Employee</span>
                                                    <small class="pull-right">${regEmpCount}/${corpCustEmpCount}</small>
                                                </div>
                                                <div class="progress xs">
                                                    <div class="progress-bar progress-bar-green" style="width: ${regEmpCount/corpCustEmpCount*100}%;"></div>
                                                </div>
<!-- 
                                                <div class="clearfix">
                                                    <span class="pull-left">Pending Approval</span>
                                                    <small class="pull-right">10/44 </small>
                                                </div> 
                                                <div class="progress xs">
                                                    <div class="progress-bar progress-bar-red" style="width: 70%;"></div>
                                                </div>
              -->                                  
                                                <div class="clearfix">
                                                    <span class="pull-left">Loyalty Points</span>
                                                    <small class="pull-right">${rewardPoints}</small>
                                                
                                                

                                                <!-- Buttons -->
                                                <!--<p>
                                                    <button class="btn btn-default btn-sm"><i class="fa fa-cloud-download"></i> Generate PDF</button>
                                                </p>-->
                                            </div><!-- /.pad -->
                                        </div><!-- /.col -->
                                    </div><!-- /.row - inside box -->
                                </div><!-- /.box-body -->
								
                                <!--
								<div class="box-footer">
                                    <div class="row">
                                        <div class="col-xs-4 text-center" style="border-right: 1px solid #f4f4f4">
                                            <input type="text" class="knob" data-readonly="true" value="80" data-width="60" data-height="60" data-fgColor="#f56954"/>
                                            <div class="knob-label">CPU</div>
                                        </div><!-- ./col -*->
                                        <div class="col-xs-4 text-center" style="border-right: 1px solid #f4f4f4">
                                            <input type="text" class="knob" data-readonly="true" value="50" data-width="60" data-height="60" data-fgColor="#00a65a"/>
                                            <div class="knob-label">Disk</div>
                                        </div><!-- ./col -*->
                                        <div class="col-xs-4 text-center">
                                            <input type="text" class="knob" data-readonly="true" value="30" data-width="60" data-height="60" data-fgColor="#3c8dbc"/>
                                            <div class="knob-label">RAM</div>
                                        </div><!-- ./col -*->
                                    </div><!-- /.row -*->
                                </div><!-- /.box-footer -->
                            </div><!-- /.box -->        
                            
                            <!-- Custom tabs (Charts with tabs)
                            <div class="nav-tabs-custom">
                                <!-- Tabs within a box 
                                <ul class="nav nav-tabs pull-right">
                                    <li class="active"><a href="#revenue-chart" data-toggle="tab">Area</a></li>
                                    <li><a href="#sales-chart" data-toggle="tab">Donut</a></li>
                                    <li class="pull-left header"><i class="fa fa-inbox"></i> Sales</li>
                                </ul>
                                <div class="tab-content no-padding">
                                    <!-- Morris chart - Sales 
                                    <div class="chart tab-pane active" id="revenue-chart" style="position: relative; height: 300px;"></div>
                                    <div class="chart tab-pane" id="sales-chart" style="position: relative; height: 300px;"></div>
                                </div>
                            </div> --><!-- /.nav-tabs-custom -->
                                                
                            <!-- Calendar -->
							
							<!--
                            <div class="box box-warning">
                                <div class="box-header">
                                    <i class="fa fa-calendar"></i>
                                    <div class="box-title">Calendar</div>
                                    
                                    <!-- tools box -***->
                                    <div class="pull-right box-tools">
                                        <!-- button with a dropdown -***->
                                        <div class="btn-group">
                                            <button class="btn btn-warning btn-sm dropdown-toggle" data-toggle="dropdown"><i class="fa fa-bars"></i></button>
                                            <ul class="dropdown-menu pull-right" role="menu">
                                                <li><a href="#">Add new event</a></li>
                                                <li><a href="#">Clear events</a></li>
                                                <li class="divider"></li>
                                                <li><a href="#">View calendar</a></li>
                                            </ul>
                                        </div>
                                    </div><!-- /. tools -***->                                    
                                </div><!-- /.box-header -***->
                                <div class="box-body no-padding">
                                    <!--The calendar -***->
                                    <div id="calendar"></div>
                                </div><!-- /.box-body -***->
                            </div><!-- /.box -***->
							-->

                        </section><!-- /.Left col -->
                        <!-- right col (We are only adding the ID to make the widgets sortable)-->
                        <section class="col-lg-6 connectedSortable">
                            <!-- Map box -->
                            <div class="box box-primary">
                                <div class="box-header">
                                    <!-- tools box -->
                                    <div class="pull-right box-tools">                                        
                                        <button class="btn btn-primary btn-sm daterange pull-right" data-toggle="tooltip" title="Date range"><i class="fa fa-calendar"></i></button>
                                        <button class="btn btn-primary btn-sm pull-right" data-widget='collapse' data-toggle="tooltip" title="Collapse" style="margin-right: 5px;"><i class="fa fa-minus"></i></button>
                                    </div><!-- /. tools -->

                                    <i class="fa fa-map-marker"></i>
                                    <h3 class="box-title">
                                        Trip Status
                                    </h3>
                                </div>
                                <div class="box-body no-padding">
                                <form action="dashboard">
                                  <div class="s3" style='display:inline-block'>
								       Month<select name="Month">
								        <option value="01">Jan</option>
								        <option value="02">Feb</option>
								        <option value="03">Mar</option>
								        <option value="04">Apr</option>
								        <option value="05">May</option>
								        <option value="06">Jun</option>
								        <option value="07">Jul</option>
								        <option value="08">Aug</option>
								        <option value="09">Sep</option>
								        <option value="10">Oct</option>
								        <option value="11">Nov</option>
								        <option value="12">Dec</option>
								      </select> &nbsp; 
								       Year<select name="Year">
								        <option value="2016">2016</option>
								        <option value="2017">2017</option>
								        <option value="2018">2018</option>
								        <option value="2019">2019</option>
								       </select> &nbsp;
								       Week<select name="Week">
								        <option value="1">Week1</option>
								        <option value="2">Week2</option>
								        <option value="3">Week3</option>
								        <option value="4">Week4</option>
								        <option value="0">All</option>
								        </select> &nbsp;
								        <button class="button" type="Submit">Submit</button>
								      </div>
								      </form>
								 </div><br>
	                                    <div class="table-responsive">
	                                        <!-- .table - Uses sparkline charts-->
	                                        <table class="table table-striped" >
	                                            <tr>
	                                                <th>EmpName</th>
	                                                <th>EmpId</th>
	                                                <th>Trip Fare</th>
	                                                <th>Trip Fee</th>
	                                                <th>History</th>
	                                            </tr>
				            					<c:if test="${fn:length(bookingList)> 0}">
						                    	<c:forEach items="${bookingList}" var="l" varStatus="index" step="1" begin="1" end="10">
													<tr>
														<td>${l.customerName} </td>
														<td><div id="sparkline-1">${l.empId}</div></td>
														<td>${l.bookingFee}</td>
														<td>${l.rideTotalAmt}</td>
														<td><a href="invoice?bookingId=${l.bookingId}"><i class="fa fa-book"></i></a>
														&nbsp;&nbsp;</td>
													</tr>
						                    	</c:forEach>
						                    	</c:if>
						                    	<c:if test="${fn:length(bookingList)== 0}">
						                    	<tr>
															<td colspan=5>No data</td>
												</tr>
							                    </c:if>
                                            
                                        </table><!-- /.table -->
                                    </div>
                                </div><!-- /.box-body-->
                                <div class="box-footer">
                                    <button class="btn btn-info"><i class="fa fa-download"></i> Generate PDF</button>
                                    <button class="btn btn-warning"><i class="fa fa-bug"></i> Report Bug</button>
                                    <button class="btn btn-info" target="_blank" onClick="location.href='history'">View Full Screen</button>
                                </div><br><br><br>
                                <div class="box box-primary">
                                <div class="addemp">
                                   <h3 class="box-title">Add Employee</h3><br><br>
                                   <button class="btn btn-info" target="_blank" onClick="location.href='http://localhost:8080/voyager/emp'">Add Employee</button>
                                </div>
                                </div>
                            </div>

         
                        </section><!-- right col -->
                    </div><!-- /.row (main row) -->

                </section><!-- /.content -->
            </aside><!-- /.right-side -->
        </div><!-- ./wrapper -->

        <!-- add new calendar event modal -->

       <jsp:include page="include/js_inc.jsp"></jsp:include>

<%-- <script src="${pageContext.request.contextPath}/resources/core/js/plugins/jqgrid/js/i18n/grid.locale-en.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/resources/core/js/plugins/jqgrid/js/jquery.jqGrid.min.js" type="text/javascript"></script>
 --%>
	<script type="text/javascript">
	
	
	
/* 	$(function() {
		jQuery("#empData").jqGrid({
		   	url:'getBookingHistoryCorp',
			datatype: "json",
			height: 255,
			width: 600,
		   	colNames:['Emp Name', 'Emp Id', 'Trip Fare', 'Trip Fee', 'History'],
		   	colModel:[
		   		{name:'customerName', index:'customerName', width:65},
		   		{name:'empId', index:'empId', width:150},
		   		{name:'rideTotalAmt', index:'rideTotalAmt', width:100},
		   		{name:'bookingFee', index:'bookingFee', width:100},
		   		{name:'bookingFee',  width:100}
		   		
		   	],
		   	rowNum:50,
			rowTotal: 2000,
			rowList : [20,30,50],
			loadonce:true,
		   	mtype: "GET",
			rownumbers: true,
			rownumWidth: 40,
			gridview: true,
		   	pager: '#pager27',
		   	sortname: 'item_id',
		    viewrecords: true,
		    sortorder: "asc",
			caption: "Loading data from server at once"	
		});

	 
	});
 */
	</script>
    </body>
    
    
    
</html>