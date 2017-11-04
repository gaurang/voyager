<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>List Payments</title>
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
                    Payments
                    </h1>
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                        <li class="active">Dashboard</li>
                    </ol>
                    
                </section>
                    
                    <div class="box">
                    	      <div class="box-body">
									
	                               <div class="box-body no-padding">
										<!--<div id="world-map" style="height: 300px;"></div>-->
										<div class="table-responsive">
											<!-- .table - Uses sparkline charts-->
											<table class="table table-striped">
												<tr>
													<th>Name</th>
													<th>CardType</th>
													<th>exp</th>
													<th>Delete</th>
												</tr>
                 	
                 								<c:if test="${fn:length(listPayment)> 0}">
						                    	<c:forEach items="${listPayment}" var="l" varStatus="index" step="1">
													<tr>
														<td>${l.cardName} </td>
														<td><div id="sparkline-1">
														${fn:substring(l.cardNo, 12, 15)}
														</div></td>
														<td>${l.expDate}</td>
														
														<td><a href="deletePayment?paymentId=${l.paymentId}"><i class="fa fa-trash-o fa-lg"></i></a></td>
													</tr>
						                    	</c:forEach>
						                    	</c:if>
						                    	<c:if test="${fn:length(listPayment)== 0}">
						                    	
						                    	<tr>
															<td colspan=4>No data</td>
												</tr>
							                    </c:if>
							                  </table>  	
                 						</div>
                 				  </div>
                 			</div>	  	                    	
                    </div>
                    

             </aside><!-- /.right-side -->
        </div><!-- ./wrapper -->

        <!-- add new calendar event modal -->

       <jsp:include page="include/js_inc.jsp"></jsp:include>


    </body>
</html>