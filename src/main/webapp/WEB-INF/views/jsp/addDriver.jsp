<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>

<html>
    <head>
        <meta charset="UTF-8">
        <title>AdminLTE | Driver</title>
        <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
		<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/countryStateCity.js"></script> --%>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
        <jsp:include page="include/files.jsp"></jsp:include>
		<link href="${pageContext.request.contextPath}/resources/core/css/jquery.steps.css" rel="stylesheet" type="text/css" /> 
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
                        Customer Service Executive (CSE)
                        
                    </h1>
                    
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                        <li class="active">CSE</li><li class="active">CSE</li>
                    </ol>
					<p>&nbsp;</p>
					
       		<form:form action="saveAddDriver" method="post" commandName="driverForm" id="driverForm" class="box box-primary" name="driverForm" enctype="multipart/form-data">             
       			<spring:message code="documents.dir" var="docDir"/> 
       		
    				<h1>Personal Details</h1>
    				<fieldset>
    					<form:hidden path="driver.driverId" />
    					<form:hidden path="driver.password" />
    					<form:hidden path="driver.cseCode" />
    					<form:hidden path="driverDetails.driverDetailId" />
    					<form:hidden path="driverDetails.driverId" />
    					<form:hidden path="bank.bankId" />
    					<form:hidden path="bank.driverId" />
    					<form:hidden path="drVehicle.vehicleId" />
    					<form:hidden path="drVehicle.driverId" />
 								<div class="box-body ">
 								
 									<c:if test="${driverForm.driver.cseCode != null}">
 									 <div class="row form-group">
                                         <label for="exampleInputEmail1" class="col-xs-3">CSE Number</label>
                                         <div class="col-xs-3">
                                         	${driverForm.driver.cseCode} 
                                         </div>	
                                     </div>
 									</c:if>
                                     <div class="row form-group">
                                         <label for="driver.fname" class="col-xs-3">Name<span style="color:red;">*</span></label>
                                         <div class="col-xs-3">
                                         	<form:input type="text" class="form-control required" path="driver.fname"  placeholder="First Name"/>
										 </div>
										 <div class="col-xs-3">                                         
                                         	<form:input type="text" class="form-control" path="driver.mname" placeholder="Middle Name"/>
                                         </div>
                                          <div class="col-xs-3">                                         
                                         	<form:input type="text" class="form-control required" path="driver.lname" placeholder="Last Name"/>
                                         </div>
                                     </div>
                               		<div class="row form-group">
                                         <label for="dob" class="col-xs-3">Date of Birth <span style="color:red;">*</span></label>
                                         
                                      
                                    <div class="col-xs-3">
                                         	<form:input type="date" class="form-control date required" path="driver.dob"  placeholder="DOB"/>
                                    </div>	
 
                                         <label for="gender" class="col-xs-3">&nbsp;&nbsp;&nbsp;Gender<span style="color:red;">*</span></label>
                                         <div class="col-xs-3">
                                         <!--<c:if test="${driverForm.driver.gender == 'm'}" >checked='true' </c:if >
                                         <form:radiobutton path="driver.gender" value="m" class="required" />&nbsp;&nbsp;&nbsp;Male <form:radiobutton path="driver.gender" value="f"  cssStyle="v-align:top;" />&nbsp;&nbsp;&nbsp;Female
                                         <c:if test="${driverForm.driver.gender == 'f'}">checked='true'</c:if>
                                         --> 
                                         	<form:radiobutton path="driver.gender" value="m" class="required" checked="true"/>&nbsp;&nbsp;&nbsp;Male <form:radiobutton path="driver.gender" value="f"  cssStyle="v-align:top;" />&nbsp;&nbsp;&nbsp;Female
                                     
                                         </div>	
  
                                     </div>      
                                     <div class="row form-group">
                                         <label for="exampleInputEmail1" class="col-xs-3">Phone<span style="color:red;">*</span></label>
                                         <div class="col-xs-3" style="display:inline-block;">
											<form:input type="text" class="form-control required number" style="width:45px;height:35px;display:inline;"  path="driver.countryCode" placeholder="61" onblur="validate();" value="61"/>&nbsp;&nbsp;
                                         	<form:input type="text" class="form-control required number" style="width:172px;height:35px;display:inline;"  path="driver.phone" placeholder="Phone" onblur="validate();"/>
                                         </div>	
                                     </div>

                                     <div class="row form-group">
                                         <label for="exampleInputEmail1" class="col-xs-3">Email address<span style="color:red;">*</span></label>
                                         <div class="col-xs-3">
                                         	<form:input type="email" class="form-control required email" path="driver.email" placeholder="Email"/>
                                         </div>	
                                     </div>
                                    <div class="row form-group">
                                         <label for="exampleInputEmail1" class="col-xs-3">Upload Picture<span style="color:red;">*</span></label>
                                         <div class="col-xs-3">
                                        	<c:choose>
                                        	<c:when test="${driverForm.driver.photo != null && driverForm.driver.photo != ''}">
                                        		
                                        			<input  type="file" class="form-control" value="${driver.photo}" name="imageFile"/>
                                        			${driverForm.driver.photo}
                                         			<img alt="" src="../images/${driverForm.driver.photo}" width="100" height="100"/>
                                        	</c:when>
                                         	<c:otherwise>
                                         		<input  type="file" class="form-control required" value="${driver.photo}" name="imageFile"/>
                                         		
                                         	</c:otherwise>
                                         	
                                         	</c:choose>
                                         </div>	
                                         
                                     </div>
                      	
                                     <div class="row form-group">
                                     
                                        <label for="exampleInputEmail1" class="col-xs-3">Address<span style="color:red;">*</span></label>
                                     	
                                     	<div class="col-xs-3">
                                         	<form:input type="text" class="form-control required" path="driverDetails.address"  placeholder="Address"/>
                                         </div>
                                        <label for="exampleInputEmail1" class="col-xs-3">Street<span style="color:red;">*</span></label>
                                     	<div class="col-xs-3">
                                         	<form:input type="text" class="form-control required" path="driverDetails.street"  placeholder="Street"/>
                                        </div>
  
                                     </div>

                                     <div class="row form-group">
                                     
                                        <label for="exampleInputEmail1" class="col-xs-3">Area</label>
                                     	<div class="col-xs-3">
												
                                         	<form:input type="text" class="form-control" path="driverDetails.area"  placeholder="Area"/>
                                         </div>
                                        <label for="exampleInputEmail1" class="col-xs-3">City<span style="color:red;">*</span></label>
                                     	<div class="col-xs-3">
                                         	<form:input type="text" class="form-control required" path="driverDetails.city"  placeholder="City"/>
                                        </div>
  
                                     </div>
									
                                     <div class="row form-group">
                                        <label for="exampleInputEmail1" class="col-xs-3">State</label>
                                     	<div class="col-xs-3">
                                         	<form:input type="text" class="form-control" path="driverDetails.state" placeholder="State"/>
                                         </div>
                                        <label for="exampleInputEmail1" class="col-xs-3">Country<span style="color:red;">*</span></label>
                                     	<div class="col-xs-3">
                                         	<form:input type="text" class="form-control required" path="driverDetails.country"  placeholder="Country"/>
                                        </div>
                                     </div>
									<hr>
                                     <div class="row form-group">
                                        <label for="exampleInputEmail1" class="col-xs-3">License No  <span style="color:red;">*</span></label>
                                     	<div class="col-xs-3">
                                         	<form:input type="text" class="form-control required" path="driverDetails.licenseNo"  placeholder="Liscense No." onblur="validate();"/>
                                         </div>
                                        <label for="exampleInputEmail1" class="col-xs-3">Upload <span style="color:red;">*</span></label>
                                     	<div class="col-xs-3">
                                     	<c:choose>
                                         	
                                         	<c:when test="${driverForm.driverDetails.licensePath != null && driverForm.driverDetails.licensePath != ''}">
                                         		<input  type="file" class="form-control " value="driverForm.driverDetails.licensePath" name="licenseFile" />
                                         		${driverForm.driverDetails.licensePath}
                                         		<img alt="" src="../images/${driverForm.driverDetails.licensePath}" width="100" height="100"/>
                                         	</c:when>
                                         	<c:otherwise>
                                         		<input  type="file" class="form-control required" value="driverDetails.licensePath" name="licenseFile" />
                                         	</c:otherwise>
                                         </c:choose>
                                        </div>
                                     </div>
									<hr>
									
                                     <div class="row form-group">
                                        <label for="exampleInputEmail1" class="col-xs-3">Driver Authorization <span style="color:red;">*</span></label>
                                     	<div class="col-xs-3">
                                         	<form:input type="text" class="form-control required" path="driverDetails.driverAuth"  placeholder="Driver Authorization"/>
                                         </div>
                                        <label for="exampleInputEmail1" class="col-xs-3">Upload <span style="color:red;">*</span> </label>
                                     	<div class="col-xs-3">
                                     	<c:choose>
                                     		<c:when test="${driverForm.driverDetails.driverAuthPath != null && driverForm.driverDetails.driverAuthPath != ''}">
                                         		<input  type="file" class="form-control" value="driverDetails.driverAuthPath" name="driverAuthFile" />
                                         		${driverForm.driverDetails.driverAuthPath}
                                         		<img alt="" src="../images/${driverForm.driverDetails.driverAuthPath}" width="100" height="100"/>
                                         	</c:when>
                                         	<c:otherwise>	
                                         		<input  type="file" class="form-control required" value="driverDetails.driverAuthPath" name="driverAuthFile" />
                                         	</c:otherwise>
                                         </c:choose>
                                        </div>
                                     </div>
                                     <hr>
                                     <div class="row form-group">
                                        <label for="exampleInputEmail1" class="col-xs-3">Driver Insurance <span style="color:red;">*</span></label>
                                     	<div class="col-xs-3">
                                         	<form:input type="text" class="form-control required" path="driverDetails.driverInsuranceNo"  placeholder="Driver's Insurance"/>
                                         </div>
                                        <label for="exampleInputEmail1" class="col-xs-3">Upload <span style="color:red;">*</span></label>
                                     	<div class="col-xs-3">
                                     	<c:choose>
                                     		<c:when test="${driverForm.driverDetails.driverInsurancePath != null && driverForm.driverDetails.driverInsurancePath != ''}">
                                         		<input  type="file" class="form-control" value="driverDetails.driverInsurancePath" name="insuranceFile"/>
                                         		${driverForm.driverDetails.driverInsurancePath}
                                         		<img alt="" src="../images/${driverForm.driverDetails.driverInsurancePath}" width="100" height="100"/>
                                         	</c:when>
                                         	<c:otherwise>
                                         		<input  type="file" class="form-control required" value="driverDetails.driverInsurancePath" name="insuranceFile"/>	
                                         	</c:otherwise>
                                         </c:choose>
                                     </div>
									<hr>
                                     <div class="row form-group">
                                        <label for="exampleInputEmail1" class="col-xs-3">Accesibility</label>
                                     	<div class="col-xs-3">
                                         	<form:input type="text" class="form-control" path="driverDetails.accessibility" placeholder="Accesibility"/>
                                         </div>
                                        <label for="exampleInputEmail1" class="col-xs-3">Service Type<span style="color:red;">*</span></label>
                                        <div class="col-xs-3">
                                         	<form:select type="text" class="form-control required" value="Select" path="driver.serviceType" >
                                         		<option value="">select</option>
                                         		<c:forEach var="serviceType" items="${configMap['SERVICE_TYPE']}">
												    <option value="${serviceType.value.attrValue1}"  <c:if test="${driverForm.driver.serviceType == serviceType.value.attrValue1}" >selected</c:if>>${serviceType.key}</option>
												</c:forEach>
                                         	</form:select>
                                         </div>
                                     </div>
       
								</div>	
								</div>
						</fieldset>
    					<h1>Vehicles</h1>
    					<fieldset>
    					
    						        <div class="row form-group">
                                        <label for="exampleInputEmail1" class="col-xs-3">Vehicle Registration No <span style="color:red;">*</span></label>
                                     	<div class="col-xs-3">
                                         	<form:input type="text" class="form-control required" path="drVehicle.registrationId"  placeholder="Vehicle Registration No."/>
                                         </div>
                                        <label for="exampleInputEmail1" class="col-xs-3">Upload  <span style="color:red;">*</span> </label>
                                     	<div class="col-xs-3">
                                     	<c:choose>
                                     		<c:when test="${driverForm.drVehicle.registrationDocPath != null && driverForm.drVehicle.registrationDocPath != ''}">
                                         		<input  type="file" class="form-control" value="drVehicle.registrationDocPath" name="registrationFilePath"/>
                                         		${driverForm.drVehicle.registrationDocPath}
                                         		<img alt="" src="../images/${driverForm.drVehicle.registrationDocPath}" width="100" height="100"/>
                                         	</c:when>
                                         	<c:otherwise>
                                         		<input  type="file" class="form-control required" value="drVehicle.registrationDocPath" name="registrationFilePath"/>
                                         	</c:otherwise>
                                         </c:choose>
                                        </div>
                                        
                                     </div>
                                     <hr>
    						        <div class="row form-group">
                                        <label for="exampleInputEmail1" class="col-xs-3">Vehicle Insurance Policy No <span style="color:red;">*</span></label>
                                     	<div class="col-xs-3">
                                         	<form:input type="text" class="form-control required" path="drVehicle.vehInsuranceNo"  placeholder="Vehicle Insurance Policy No."/>
                                         </div>
                                        <label for="exampleInputEmail1" class="col-xs-3">Upload  <span style="color:red;">*</span> </label>
                                     	<div class="col-xs-3">
                                     	<c:choose>
                                     		<c:when test="${driverForm.drVehicle.vehinsurancePath != null && driverForm.drVehicle.vehinsurancePath != ''}">
                                         		<input  type="file" class="form-control" value="drVehicle.vehinsurancePath" name="insuranceFilePath"/>
                                         		${driverForm.drVehicle.vehinsurancePath}
                                         		<img alt="" src="../images/${driverForm.drVehicle.vehinsurancePath}" width="100" height="100"/>
                                         	</c:when>
                                  			<c:otherwise>
                                           		<input  type="file" class="form-control required" value="drVehicle.vehinsurancePath" name="insuranceFilePath"/>
                                  			</c:otherwise>
                                         </c:choose>
                                        </div>
                            		</div>
                                     <div class="row form-group">
                                        <label for="exampleInputEmail1" class="col-xs-3">Insurance Renewal Date<span style="color:red;">*</span></label>
	                                    
                                         <div class="col-xs-3">
                                         	<form:input type="date" class="form-control date required" path="drVehicle.vehinsuranceRenewDt"  placeholder="Insurance Renewal Date"/>
                                    	</div>	
                                     </div>
    								<hr>
                                     <div class="row form-group">
                                        <label for="v" class="col-xs-3">Make<span style="color:red;">*</span></label>
                                     	<div class="col-xs-3">
                                         	<form:input type="text" class="form-control required" path="drVehicle.make" placeholder="Make"/>
                                         </div>
                                        <label for="v" class="col-xs-3">Model<span style="color:red;">*</span></label>
                                     	<div class="col-xs-3">
                                         	<form:input type="text" class="form-control required" path="drVehicle.model" placeholder="Model"/>
                                         </div>
                                     </div>
                                     <div class="row form-group">
                                        <label for="exampleInputEmail1" class="col-xs-3">Type of Car <span style="color:red;">*</span></label>
                                     	<div class="col-xs-3">
                                         	<form:select type="text" class="form-control required" path="drVehicle.vType" >
                                         		<option value="HTB"  <c:if test="${driverForm.drVehicle.vType == 'HTB'}" >selected</c:if>>Hatch Back</option>
                                         		<option value="SED"  <c:if test="${driverForm.drVehicle.vType == 'SED'}" >selected</c:if>>Sedan</option>
                                         		<option value="SUV"  <c:if test="${driverForm.drVehicle.vType == 'SUV'}" >selected</c:if>>SUV</option>
                                         		<option value="MUV"  <c:if test="${driverForm.drVehicle.vType == 'MUV'}" >selected</c:if>>MUV</option>
                                         		<option value="TXI"  <c:if test="${driverForm.drVehicle.vType == 'TXI'}" >selected</c:if>>TXI</option>
                                         		<option value="MTX"  <c:if test="${driverForm.drVehicle.vType == 'MTX'}" >selected</c:if>>MTX</option>
                                         		<option value="O"  <c:if test="${driverForm.drVehicle.vType == 'O'}" >selected</c:if>>Others</option>
                                         	</form:select>
                                         </div>
                                         <label for="v" class="col-xs-3">Car Number<span style="color:red;">*</span></label>
                                     	<div class="col-xs-3">
                                         	<form:input type="text" class="form-control required" path="drVehicle.carNumber" placeholder="Car Number"/>
                                         </div>
                                     </div>
                                         
									
                                     <div class="row form-group">
                                        <label for="v" class="col-xs-3">Owner</label>
                                     	<div class="col-xs-3">
                                         	<form:input type="text" class="form-control" path="drVehicle.ownerId" placeholder="Owener's Id"/>
                                         </div>
                                     </div>			
							
                                     <div class="row form-group">
                                        <label for="v" class="col-xs-3">Attributes</label>
                                     	<div class="col-xs-3">
                                     	  <form:select  class="form-control" path="drVehicle.vehAttributes" value="1">
                                     	    <option value="Baby Seat">Select</option>
                                         	<option value="Baby Seat">Baby Seat</option>
                                         	<option value="Animal Seat">Animal Seat</option>
                                         	<option value="Over Size Luggage">Over Size Luggage</option>
                           				  </form:select>
                                         </div>
                                     </div>			
							    						
	    					         <div class="row form-group">
	                                        <label for="v" class="col-xs-3">Seat Capacity</label>
	                                     	<div class="col-xs-3">
	                                         	<form:input type="text" class="form-control" path="drVehicle.seatingCapacity" placeholder="No. of Passengers"/>
	                                         </div>
	                                 </div>			
								
    					</fieldset>
    					
    					
    					
    					
    					<h1>Bank</h1>
 	   					<fieldset>
 	   					
                          		<div class="row form-group">
                                   <label for="dob" class="col-xs-3">Bank Name <span style="color:red;">*</span></label>
                                   <div class="col-xs-3">
                                   	<form:input class="form-control required" path="bank.bankName"  placeholder="Bank Name"/>
                                   </div>	

                                   <label for="gender" class="col-xs-3">Account Number <span style="color:red;">*</span></label>
                                   <div class="col-xs-3">
                                   	<form:input class="form-control required" path="bank.bankAccNo"  placeholder="Bank Account Number"/>
                                   </div>	

                                </div>
                                           
                        		<div class="row form-group">
                                   <label for="dob" class="col-xs-3">Account Holder Name <span style="color:red;">*</span></label>
                                   <div class="col-xs-3">
                                   	<form:input class="form-control required" path="bank.accountName"  placeholder="Account Holder Name"/>
                                   </div>	
                                   
                                   
                                   
                                   <label for="gender" class="col-xs-3">Branch Code <span style="color:red;">*</span></label>
                                   <div class="col-xs-3">
                                   	<form:input class="form-control required" path="bank.bankBranch"  placeholder="Branch Code"/>
                                   </div>	
								</div>
                        		<div class="row form-group">
                                   <label for="dob" class="col-xs-3">Address</label>
                                   <div class="col-xs-3">
                                   	<form:input class="form-control" path="bank.bankAddress" placeholder="Address"/>
                                   </div>	

                                   <label for="gender" class="col-xs-3">City</label>
                                   <div class="col-xs-3">
                                   	<form:input class="form-control" path="bank.city" placeholder="City"/>
                                   </div>
                                </div>   	
                        		<div class="row form-group">
                                   <label for="dob" class="col-xs-3">Zip</label>
                                   <div class="col-xs-3">
                                   	<form:input class="form-control" path="bank.zip" placeholder="Zip"/>
                                   </div>	

                                 </div>   
                                 <div class="text-center">
      	  							<input type="submit" value="submit" style="width:100px;height:30px;"></input>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      							</div>   
    					</fieldset>
    					
                
                <!-- </div> -->
             </form:form>
                    
                </section>

             </aside><!-- /.right-side -->
        </div><!-- ./wrapper -->

        <!-- add new calendar event modal -->

       <jsp:include page="include/js_inc.jsp"></jsp:include>

       <script src="${pageContext.request.contextPath}/resources/core/js/jquery.steps.min.js" type="text/javascript"></script>        
		
		<script>
			var form = $('#driverForm');
            form.steps({
	                headerTag: "h1",
	                bodyTag: "fieldset",
	                enableAllSteps: true,
	                onStepChanging: function (event, currentIndex, newIndex)
	                {
	                    // Allways allow previous action even if the current form is not valid!
	                    if (currentIndex > newIndex)
	                    {
	                        return true;
	                    }
	                    // Forbid next action on "Warning" step if the user is to young
	                    if (newIndex === 3 && Number($("#age-2").val()) < 18)
	                    {
	                        return false;
	                    }
	                    // Needed in some cases if the user went back (clean up)
	                    if (currentIndex < newIndex)
	                    {
	                        // To remove error styles
	                        form.find(".body:eq(" + newIndex + ") label.error").remove();
	                        form.find(".body:eq(" + newIndex + ") .error").removeClass("error");
	                    }
	                    form.validate().settings.ignore = ":disabled,:hidden";
	                    return form.valid();
	                },
	                onStepChanged: function (event, currentIndex, priorIndex)
	                {
	                    // Used to skip the "Warning" step if the user is old enough.
	                    if (currentIndex === 2 && Number($("#age-2").val()) >= 18)
	                    {
	                        form.steps("next");
	                    }
	                    // Used to skip the "Warning" step if the user is old enough and wants to the previous step.
	                    if (currentIndex === 2 && priorIndex === 3)
	                    {
	                        form.steps("previous");
	                    }
                        resizeJquerySteps();
	                },
	                onFinishing: function (event, currentIndex)
	                {
	                    form.validate().settings.ignore = ":disabled";
	                    return form.valid();
	                },

/*             		onFinishing: function (event, currentIndex)
            	    {
            	        form.validate().settings.ignore = ":disabled";
            	        if(form.valid())            	        
            	        	form.submit();

            	    }
            		,
*/ 
            	    onFinished: function (event, currentIndex)
            	    {
            	        alert("Submitted!");
            	    }
	        }).validate({
	            errorPlacement: function errorPlacement(error, element) { element.before(error); }
	     });
        
        
            function resizeJquerySteps() {
                $('.wizard .content').animate({ height: $('.body.current').outerHeight() }, "slow");
			}
            

            $(function() {
                $("#driver.dob").datepick({dateFormat: 'dd-mm-yyyy'});
            });
            
            
            
            $(document).ready(function(){
                $("button").click(function(){
                    $("#change").toggle();
                });
            });
           
        </script>
    </body>
</html>