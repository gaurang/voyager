<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>

<html>
    <head>
        <meta charset="UTF-8">
        <title>AdminLTE | Feeback</title>
        <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>

        <jsp:include page="../include/files.jsp"></jsp:include>
		<link href="resources/core/css/jquery.steps.css" rel="stylesheet" type="text/css" /> 
		</head>
    <body class="skin-black">
        <!-- header logo: style can be found in header.less -->
        <%-- <jsp:include page="include/header.jsp"></jsp:include>
         --%>
        
        <div class="wrapper row-offcanvas row-offcanvas-left" >
            <!-- Left side column. contains the logo and sidebar -->
			<div align="center" style="width: 100%; background-color: black;" >
				<img src="resources/core/img/Web-banner.jpg" width="100%"  align="middle" >
			</div>

            <!-- Right side column. Contains the navbar and content of the page -->
            <aside class="" style="margin: 0 auto; width:95%;">
                <!-- Content Header (Page header) -->
                <section class="content-header">
                    <h1>
                        Request
                        
                    </h1>
                    
                    <!-- <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                        <li class="active">CSE</li><li class="active">Corporate Client</li>
                    </ol> -->
					<p>&nbsp;</p>
       		<form:form action="saveFeedback" method="post" commandName="feedback" >             
                <div id="wizard" class="box box-primary">
    				<h1>Would you like to Conect with us?</h1>
    				<div >

 								<div class="box-body ">
	 										<div class="row form-group">
	 											<label for="connectAs" class="col-xs-3" style="padding-top:20px;">Connect with us as ?:</label>
	 										    <div class="col-xs-4">
											       <form:checkbox path="connectAs" class="form-control required" value="RIDER" label="Rider"/>
											       <form:checkbox path="connectAs" class="form-control required" value="DRIVER PARTNER" label="Driver Partner"/>
											       <form:checkbox path="connectAs" class="form-control required" value="TAXI PARTNER" label="Taxi Partner"/>
											     </div>   
											   
	 										</div>
	 												
	 									   <div class="row form-group">
										     <label for="drivers.fname" class="col-xs-3" style="padding-top:20px;">Name</label>
											   <div class="col-xs-4">
														<label for="fname"></label>
														<form:input class="form-control required" path="fname" placeholder="First Name"/>
											   </div>
											   <div class="col-xs-4">
														<label for="lname"></label>
														<form:input class="form-control" path="lname" placeholder="Last Name" style="width:61%;"/>
											   </div>
										   </div>
										   <div class="row form-group">
									            <label for="exampleInputEmail1" class="col-xs-3" style="padding-top:20px;">Email address</label>
									                <div class="col-xs-4">
									                    <label for="email"></label>
														<form:input type="email" class="form-control" path="email" placeholder="Email Address"/>                     	
									                </div>	
											</div>
											 <div class="row form-group">
									            <label for="Contact" class="col-xs-3" style="padding-top:20px;">Contact No</label>
									                <div class="col-xs-3">
									                    <label for="contact"></label>
														<form:input type="number" class="form-control" path="phone" placeholder="Contact No" data-inputmask='"mask": "(999) 999-9999"' data-mask=""  />                     	
									                </div>	
											</div>
											<%-- <div class="row form-group">
									            <label for="postal code" class="col-xs-3" style="padding-top:20px;">Postal Code</label>
									                <div class="col-xs-3">
									                    <label for="postal code"></label>
														<form:input type="text" class="form-control" path="postalCode" placeholder="Postal Code"/>                     	
									                </div>	
											</div> --%>
											<div class="row form-group">
	 											<label for="connectAs" class="col-xs-3" style="padding-top:20px;">Have you used ride sharing before ?:</label>
	 										    <div class="col-xs-4">
											       <form:checkbox path="connectAs" class="form-control required flat" value="AS RIDER" label="Yes as a rider"/>
											       <form:checkbox path="connectAs" class="form-control required flat" value="AS DRIVER" label="Yes as a driver"/>
											       <form:checkbox path="connectAs" class="form-control required flat"  value="NO" label="No"/>
											     </div>   
											   
	 										</div>
	 										
											<div class="row form-group" style="padding-top:80px;">
											  <div class="text-center">
												<button class="btn btn-primary">Submit</button>
											  </div>
											</div>
										 									
	 								
	 								
	 							</div>	
								
								
						</div>
                
                </div>
             </form:form>
                    
                </section>

             </aside><!-- /.right-side -->
        </div><!-- ./wrapper -->

        <!-- add new calendar event modal -->

       <jsp:include page="../include/js_inc.jsp"></jsp:include>

       <!-- <script src="resources/core/js/jquery.steps.min.js" type="text/javascript"></script>        
		 -->
		<script>
 //           $("#wizard").steps();
        </script>
    </body>
</html>