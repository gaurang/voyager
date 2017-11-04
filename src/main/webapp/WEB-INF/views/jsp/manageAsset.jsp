<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>

<html>
    <head>
        <meta charset="UTF-8">
        <title>AdminLTE | Asset</title>
        <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
		<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/countryStateCity.js"></script> --%>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
        <jsp:include page="include/files.jsp"></jsp:include>
<%-- 		<link href="${pageContext.request.contextPath}/resources/core/css/jquery.steps.css" rel="stylesheet" type="text/css" /> 
 --%>		</head>
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
                        Insert Asset
                        
                    </h1>
                    
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                        <li class="active">Asset</li><li class="active">Asset</li>
                    </ol>
					<p>&nbsp;</p>
					
       		<form action="saveAssetManage" method="post" id="assetForm" class="box box-primary" name="assetForm" enctype="multipart/form-data">             
    				
    				<DIV CLASS="error"></DIV>
    				<div class="row form-group">
                    	<label for="exampleInputEmail1" class="col-xs-3">Driver ID<span style="color:red;">*</span></label>
                        <div class="col-xs-3" style="display:inline-block;">
<%-- 							<input type="text" class="form-control required" name="driverId" placeholder="driver" onchange="checkDriverAsset();" value="${ed.driverId}"/>
 --%>					<select name="driverId" id="driverId">
 								<option value="">--Select--</option>
 								<c:forEach items="${driverList}" var="d">
 									<option value="${d.driverId}">${d.cseCode} -- ${d.fname} ${d.lname}</option>
 								</c:forEach>
 						</select>	
 			
                         </div>	
               		</div>
					               		
               		<div class="row form-group">
                    	<label for="exampleInputEmail1" class="col-xs-3">Asset Id</label>
                        <div class="col-xs-3" style="display:inline-block;">
			<%-- 				<input type="text" class="form-control" name="assetId" placeholder="Model Name" value="${ed.assetId}"/>
             --%>
             			<select name="assetId" id="assetId">
 								<option value="">--Select--</option>
 								<c:forEach items="${assetList}" var="a">
 									<option value="${a.assetId}">${a.serialNumber}</option>
 								</c:forEach>
 						</select>	
 			
                         </div>	
               		</div>
               		<div class="row form-group">
               			<div class="col-xs-2" style="display:inline-block;">
               			</div>
               			<div class="col-xs-3" style="display:inline-block;">
      	  					<input type="submit" id="submit" value="Submit" class="btn btn-primary"></input>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      					</div>
      				</div>
               		
   			</form>
    	</section>
    	</aside>
    </div>
    </body>
           <jsp:include page="include/js_inc.jsp"></jsp:include>
           <script>
           function checkDriverAsset(){
        	   $.getJSON( "getDriverAsset",{driverId:$('#driverId').val()}).done(function( data ) {
        		   if(data){
        	   			$('.error').html('Driver is already mapped to asset ID - '+data.assetId);
        		   }else{
        			//
        		   }
        	   });
           }
           
           </script>
    
</html>