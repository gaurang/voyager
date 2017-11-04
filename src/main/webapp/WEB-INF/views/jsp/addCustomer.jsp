<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>

<html>
    <head>
        <meta charset="UTF-8">
        <title>AdminLTE | Customer</title>
        <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>

        <jsp:include page="include/files.jsp"></jsp:include>
		<link href="${pageContext.request.contextPath}/resources/core/css/jquery.steps.css" rel="stylesheet" type="text/css" />
  		<!--  
  		<script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
		<script src="//cdnjs.cloudflare.com/ajax/libs/jquery-form-validator/2.2.8/jquery.form-validator.min.js"></script>
		<script> $.validate(); </script>
		-->
</head>
<body>
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
                        Corporate Client
                        
                    </h1>
                    
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                        <li class="active">CSE</li><li class="active">Corporate Client</li>
                    </ol>
					<p>&nbsp;</p>
       		<form:form action="saveAddCustomer" method="post" commandName="customerForm" enctype="multipart/form-data" id="corporate">             
                <input type="hidden"
				    name="${_csrf.parameterName}"
				    value="${_csrf.token}"/> 
				                
                <div id="wizard" class="box box-primary" >
    				<h1>Personal Details</h1>
    				<div >
	   					<form:hidden path="corpCust.corpCustId" />

 								<div class="box-body ">
 									<c:if test="${customerForm.corpCust.corpId != null}">
 									 <div class="row form-group">
                                         <label for="exampleInputEmail1" class="col-xs-3">Corp. Id</label>
                                         <div class="col-xs-3">
                                         	${customerForm.corpCust.corpId} 
                                         </div>	
                                     </div>
 									</c:if>
 								
	 								<div class="row form-group">
	                                         <label for="customer.corporateName" class="col-xs-3">Corporate Name<span style="color:red;">*</span></label>
	                                         <div class="col-xs-3" style="color:#E62B2B;">
	                                         	<form:input class="form-control required" path="corpCust.corporateName" placeholder="Corporate Name"/>
											 </div>
                                     </div>
                                     
                                     
                                     
                                     <div class="row form-group">
                                        <label for="exampleInputEmail1" class="col-xs-3">Address <span style="color:red;">*</span></label>
                                     	<div class="col-xs-3" style="color:#E62B2B;">
                                         	<form:input type="text" class="form-control required" path="corpCust.address" placeholder="Address"/>
                                        </div>
                                        <label for="exampleInputEmail1" class="col-xs-3">Street <span style="color:red;">*</span></label>
                                     	<div class="col-xs-3" style="color:#E62B2B;">
                                         	<form:input type="text" class="form-control required" path="corpCust.street" placeholder="Street"/>
                                         </div>
                                     </div>

                                     <div class="row form-group">
                                     
                                        <label for="exampleInputEmail1" class="col-xs-3">Area</label>
                                     	<div class="col-xs-3">
                                         	<form:input type="text" class="form-control" path="corpCust.area" placeholder="Area"/>
                                         </div>
                                        <label for="exampleInputEmail1" class="col-xs-3">City<span style="color:red;">*</span></label>
                                     	<div class="col-xs-3" style="color:#E62B2B;">
                                         	<form:input type="text" class="form-control required" path="corpCust.city" placeholder="City"/>
                                        </div>
  
                                     </div>

                                     <div class="row form-group">
                                        <label for="exampleInputEmail1" class="col-xs-3">State</label>
                                     	<div class="col-xs-3">
                                         	<form:input type="text" class="form-control" path="corpCust.state" placeholder="State"/>
                                         </div>
                                        <label for="exampleInputEmail1" class="col-xs-3">Country<span style="color:red;">*</span></label>
                                     	<div class="col-xs-3" style="color:#E62B2B;">
                                         	<form:input type="text" class="form-control required" path="corpCust.country" placeholder="Country"/>
                                        </div>
                                     </div>
 
                               		<%-- <div class="row form-group">
                                         <label for="dob" class="col-xs-3">Date of Birth</label>
                                         <div class="col-xs-3">
                                         	<form:input type="date" class="form-control" path="drivers.dob" placeholder="DOB"/>
                                         </div>	
 
                                         <label for="gender" class="col-xs-3">&nbsp;&nbsp;&nbsp;Gender</label>
                                         <div class="col-xs-3">
                                         	<form:radiobutton path="drivers.gender" value="m"/>&nbsp;&nbsp;&nbsp;Male <form:radiobutton path="drivers.gender" value="f" cssStyle="v-align:top;"/>&nbsp;&nbsp;&nbsp;Female
                                         </div>	
 
                                     </div>      
  									--%>                               
  								     <div class="row form-group">
                                         <label for="exampleInputEmail1" class="col-xs-3">Primary Contact No.<span style="color:red;">*</span></label>
                                         <div class="col-xs-3" style="color:#E62B2B;">
                                         	<form:input type="text" class="form-control required number" path="corpCust.phone" placeholder="Telephone"/>
                                         </div>	
 <%--                                         <label for="exampleInputEmail1" class="col-xs-3">Secondary Contact No.</label>
                                         <div class="col-xs-3">
                                         	<form:input type="text" class="form-control number" path="corpCust.secondaryPhone" placeholder="Telephone"/>
                                         </div>	
  --%>                                    </div>

                                     <div class="row form-group">
                                         <label for="exampleInputEmail1" class="col-xs-3">Primary Email address<span style="color:red;">*</span></label>
                                         <div class="col-xs-3" style="color:#E62B2B;">
                                         	<form:input type="email" class="form-control required email" path="corpCust.email" placeholder="Primary Email"/>
                                         </div>
<%--                                          <label for="exampleInputEmail1" class="col-xs-3">Secondary Email address</label>
                                         <div class="col-xs-3">
                                         	<form:input type="email" class="form-control" path="corpCust.secondaryEmail" placeholder="Secondary Email"/>
                                         </div>	
 --%>                                     </div>
                                    <div class="row form-group">
                                        <label for="exampleInputEmail1" class="col-xs-3">ABN / ACN No. <span style="color:red;">*</span></label>
                                     	<div class="col-xs-3" style="color:#E62B2B;">
                                         	<form:input type="text" class="form-control required" path="corpCust.abnAcnNo" placeholder="ABN / ACN No."/>
                                         </div>
                                          <label for="exampleInputEmail1" class="col-xs-3">Zip Code</label>
                                     	<div class="col-xs-3">
                                         	<form:input type="text" class="form-control" path="corpCust.pincode" placeholder="Zip Code"/>
                                         </div>
                                     </div>
 
                                    <div class="row form-group">
                                         <label for="exampleInputEmail1" class="col-xs-3">Upload Logo</label>
                                         <div class="col-xs-3">
                                         	<input  type="file" class="form-control" value="corpCust.logoPath" name="logoPath"/>
                                         	<c:if test="${customerForm.corpCust.logoPath != null && customerForm.corpCust.logoPath != ''}">
                                         		${customerForm.corpCust.logoPath}
                                         		<img alt="" src="../images/${customerForm.corpCust.logoPath}" width="100" height="100"/>
                                         	</c:if>
                                         
                                         </div>	
                                     </div>
                                    <div class="row form-group">
                                        <label for="exampleInputEmail1" class="col-xs-3">Reimbursed</label>
                                     	<div class="col-xs-3" style="color:#E62B2B;">
										<c:if test="${customerForm.corpCust.rembursed == 0 }">
                                   			<form:checkbox class="form-control" path="corpCust.rembursed" value="1" />
                                    	</c:if>     	
                                        <c:if test="${customerForm.corpCust.rembursed == 1 }">
                                   			<form:checkbox class="form-control" path="corpCust.rembursed" value="1" checked="true"/>
                                    	</c:if>
                                         </div>
                                          <label for="exampleInputEmail1" class="col-xs-3">Payment Auto</label>
                                     	<div class="col-xs-3">
                                         	<form:checkbox class="form-control" path="corpCust.modeOfPayment" value="AUTO"/>
                                         </div>
                                     </div>
 
                                    <div class="row form-group">
                                         <div class="col-xs-3">
                                         	<input  type="submit" class="btn btn-primary" value="Save"/>
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

       <jsp:include page="include/js_inc.jsp"></jsp:include>

       <!-- <script src="${pageContext.request.contextPath}/resources/core/js/jquery.steps.min.js" type="text/javascript"></script>        
		 -->
		<script>
 //           $("#wizard").steps();
        </script>
        
        		<script> 
		
		 $(function() {
  
  		  // Setup form validation on the #register-form element
    	 $("#corporate").validate({
    
              submitHandler: function(form) {
            	form.submit();
		        }
		    });
		
		  });
  
  		</script>
        
    </body>
</html>