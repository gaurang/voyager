<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
    	<meta charset="UTF-8">
        <title>Account Details</title>
        <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>

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
                        Account Details
                        
                    </h1>
                    
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                        <li class="active">CSE</li><li class="active">CSE</li>
                    </ol>
					<p>&nbsp;</p>
					<div class="box-body ">
      	      	  <div class="table-responsive">
	                    <!-- .table - Uses sparkline charts-->
	                     <table class="table table-striped" id="accountDetails">
							<tr>
								
								<td style="width:50%;">Bank Account Number</td>
								<td style="width:50%;">${bank.bankAccNo }</td>
							</tr>
							<tr>
								<td>Bank Address</td>
								<td>${bank.bankAddress }</td>
							</tr>
							<tr>
								<td>Bank branch</td>
								<td>${bank.bankBranch }</td>
							</tr>
							<tr>
								<td>Bank Name</td>
								<td>${bank.bankName }</td>
							</tr>
							<tr>
								<td>City</td>
								<td>${bank.city }</td>
							</tr>
							<tr>
								<td>IFSC Code</td>
								<td>${bank.ifscCode } </td>
								
							</tr>
							
						</table>
						</div>
				</div>
				<jsp:include page="include/js_inc.jsp"></jsp:include>
				</section>
				</aside>
			</div>
</body>
</html>