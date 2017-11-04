<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Driver | Invoice</title>
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
		        <jsp:include page="include/menuDriver.jsp"></jsp:include>
                <!-- /.sidebar -->
            </aside>

            <!-- Right side column. Contains the navbar and content of the page -->
            <aside class="right-side">
                <!-- Content Header (Page header) -->
                <section class="content-header">
                    <h1>
                        Invoice
                        
                    </h1>
                    
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                        <li class="active">CSE</li><li class="active">CSE</li>
                    </ol>
					<p>&nbsp;</p>
        <div class="table-responsive">
	        <!-- .table - Uses sparkline charts-->
	        <table class="table table-striped">
			    <tr>
					<th>Week Ending</th>
					<th>Status</th>	
					<th>Amount</th>	
					<th>Detailed Report</th>
				</tr>
				
				<c:if test="${fn:length(listDriverEarning)> 0}">
							                    	<c:forEach items="${listDriverEarning}" var="l" varStatus="index">
														<tr>
															<td>${l.}</td>
															<td><div id="sparkline-1">${l.status}</div></td>
															<td><div id="sparkline-1">${l.totalEarning}</div></td>
															
															<td><a href="summary">Details</a></td>
														</tr>
							                    	</c:forEach>
						                    	</c:if>
						                    	<c:if test="${fn:length(listVehicles)== 0}">
						                    	
						                    	<tr>
															<td colspan=4>No data</td>
												</tr>
							                    </c:if>
			</table>
		</div>
		</section>
		</aside>
		</div>
    </body>
 </html>