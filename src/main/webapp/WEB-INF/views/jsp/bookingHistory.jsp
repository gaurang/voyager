<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Booking History</title>
        <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
		<jsp:include page="include/files.jsp"></jsp:include>
		<link href="resources/core/css/jquery.steps.css" rel="stylesheet" type="text/css" />
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
                       Booking History
                        
                    </h1>
                    
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                        <li class="active">CSE</li><li class="active">CSE</li>
                    </ol>
					<p>&nbsp;</p>
      <div class="container">
       <div align="center">
  			Enter Start date and End date.
	   </div>
	   <br>
	   <form id="bookingHistory" action="listHistory" method="post">
       <div class="row form-group">
        <label class="col-xs-3">Start Date</label>
      	<div class="col-xs-3">
           <input type="date" class="form-control date required" name="startDate" placeholder="Start Date"/>
        </div>	
      </div>
      <br>
      <div class="row form-group">
        <label class="col-xs-3">End Date</label>
      	<div class="col-xs-3">
           <input type="date" class="form-control date required" name="endDate" placeholder="End Date"/>
        </div>	
      </div>
      <br>
      <div class="text-center">
      	  <input type="submit" id="submit" value="Get History" style="width:100px;height:30px;"></input>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      </div>
      </form>
      </div>
     </section>
     </aside>
    </div>
  </body>
</html>