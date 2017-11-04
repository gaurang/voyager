<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>History</title>
        <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
		<link rel="stylesheet" href="resources/core/css/emp.css"  type="text/css">
       <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">
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
                        History
                        
                    </h1>
                    
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                        <li class="active">CSE</li><li class="active">CSE</li>
                    </ol>
					<p>&nbsp;</p>
	<div style="width:100%;overflow:auto">
	<div class="box-body no-padding">			
      <div class="container"> 
         
      	<div class="row">
      	   <div class="col-sm-12">
      	      	  <div class="table-responsive">
	                    <!-- .table - Uses sparkline charts-->
	                     <table class="table table-striped" id="suppoprtTable" style = "padding-right:30px;">
	                        <tr>
	                        <th>
	                              <div class="text-left" style="padding-left:30px;">Booking Id</div>
	                          </th>
	                           <th>
	                              <div class="text-left" style="padding-left:30px;">Employee Name</div>
	                          </th>
	                         <th>
	                              <div class="text-left" style="padding-left:30px;">Email</div>
	                          </th>
	                          
	                          <th>
			                      <div class="text-left" style="padding-left:30px;">Source</div>
	                          </th>
	                          <th>
	                          		<div class="text-left" style="padding-left:30px;">Destination</div>
	                          </th>
	                           <th>
	                              <div class="text-left" style="padding-left:30px;">Travel Date</div>
	                          </th>
	                           <th>
	                              <div class="text-left" style="padding-left:30px;">Booking Fee</div>
	                          </th>
	                          <th>
			                      <div class="text-left" style="padding-left:30px;">Total Fare</div>
	                          </th>
	                          
	                        </tr> 
	                        <c:forEach items="${bookingList}" var="l" varStatus="index">
	                        <tr>
	                        <td>
								 <div class="text-left" style="padding-left:30px;">${l.bookingId}</div>				 
							</td>
	                         <td>
								 <div class="text-left" style="padding-left:30px;">${l.customerName}</div>				 
							</td>
							<td>
								 <div class="text-left" style="padding-left:30px;">${l.email}</div>					 
	                         </td>
							<td>
								 <div class="text-left" style="padding-left:30px;">${l.srcPlace}</div>					 
	                         </td>
	                         <td>
	                           <div class="text-left" style="padding-left:30px;">${l.destPlace}</div>
	                          </td>
	                          <td>
	                           <div class="text-left" style="padding-left:30px;">${l.rideDate}</div>
	                          </td>
	                          <td>
								 <div class="text-left" style="padding-left:30px;">${l.bookingFee}</div>					 
	                         </td>
	                          <td>
	                           <div class="text-left" style="padding-left:30px;">${l.rideTotalAmt}</div>
	                          </td>
	                        </tr> 
	                        </c:forEach>
	                    </table>  
	               </div>
      	  	</div>  
      	</div>
      	<br>
      	<br>
      <div>
      	<div class="text-center">
      	  <!--<button class="btn btn-info"><i class="fa fa-download"></i> Generate PDF</button>&nbsp; &nbsp; &nbsp; &nbsp;
          <button class="btn btn-warning"><i class="fa fa-bug"></i> Report Bug</button>-->
      	</div>
      </div>
      </div>
      
      <jsp:include page="include/js_inc.jsp"></jsp:include>
      </div>
      </div>
      </section>
      </aside>
      </div>
    </body>
 </html>