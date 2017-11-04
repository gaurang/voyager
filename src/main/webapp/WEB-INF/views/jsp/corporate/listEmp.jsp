<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>

<html>
    <head>
        <meta charset="UTF-8">
        <title>AdminLTE | List Emp</title>
        <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
		<script type="text/javascript" src="countryStateCity.js"></script>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
        <jsp:include page="include/files.jsp"></jsp:include>
		<link href="resources/core/css/jquery.steps.css" rel="stylesheet" type="text/css" /> 
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
                        Emp
                        
                    </h1>
                    
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                        <li class="active">CSE</li><li class="active">CSE</li>
                    </ol>
					<p>&nbsp;</p>
            <div class="box">
                <div class="box-body">
					<div class="box-body no-padding">
						<!--<div id="world-map" style="height: 300px;"></div>-->
							<div class="table-responsive">
								<!-- .table - Uses sparkline charts-->
									<table class="table table-striped">
										<tr>
										   <th>
										     <div class="name" style="display:inline-block;">
												 <div class="text-left" style="padding-left:50px; display:inline-block;">First Name</div>
												 <div class="text-left" style="padding-left:200px; display:inline-block">Last Name</div>
											 </div>
											</th> 
											<th>
											  <div class="text-left" style="padding-left:17px;">Email</div>
											</th>
											<th>
											   <div class="text-left" style="padding-left:17px;">
													Activate/Deactivate
												</div>
											</th>
										</tr>
                 							<c:if test="${fn:length(listEmp)> 0}">
						                    <c:forEach items="${listEmp}" var="l" varStatus="index">
										<tr>
											<td>
												<div class="name" style="display:inline-block;">
													 <div class="text-left" style="padding-left:50px;display:inline-block;">${l.fname}</div>
													 <div class="text-left" style="padding-left:90px;display:inline-block;">${l.lname}</div>
												</div>
											</td>
											<td>${l.email}</td>
											<td>
<!-- 												<label class="radio-inline">
											      <input type="radio" name="optradio">Activate
											    </label>
 -->											    <!-- <label class="radio-inline">
											      <input type="radio" name="optradio">Deactivate
											    </label> -->
											    <%-- <a href="getDriver?driverId=${l.driverId}"><i class="fa fa-edit"></i></a>&nbsp;&nbsp; --%><a href="deleteEmp?corpEmpId=${l.corpEmpId}"><i class="fa fa-trash-o fa-lg"></i></a>
											</td>
										</tr>
						                    </c:forEach>
						                    </c:if>
						                    <c:if test="${fn:length(listEmp)== 0}">
						                 <tr>
											<td colspan=4>No data</td>
										</tr>
							                  </c:if>
							         </table>  	
                 		    </div>
                 	  </div>
                 </div>	  	                    	
            </div>
				<!-- ./wrapper -->
        		<!-- add new calendar event modal -->

       <jsp:include page="include/js_inc.jsp"></jsp:include>
       </section>
       </aside>
       </div>
    </body>
</html>