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
   	<body>
       <div class="box-body">
			<div class="box-body no-padding">
				<div class="table-responsive">
	              <!-- .table - Uses sparkline charts-->
	                  <table class="table table-striped">
	                   	  <tr> 
	                   	    <th>EmpId</th>
	                        <th>EmpName</th>
	                        <th>Ride Date</th>
	                        <th>Ride Km</th>
	                        <th>Fare</th>
	                        <th>History</th>
	                      </tr>
	                      <tr> 
	                   	    <th>EmpId</th>
	                        <th>EmpName</th>
	                        <th>Ride Date</th>
	                        <th>Ride Km</th>
	                        <th>Fare</th>
	                        <th>History</th>
	                      </tr>
	      			   </table>
	      		</div>
	      	</div>
	   </div>
	 </body>
</html>