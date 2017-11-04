<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>

<html>
    <head>
        <meta charset="UTF-8">
        <title>AdminLTE | Driver</title>
        <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
		<script type="text/javascript" src="countryStateCity.js"></script>
							
        <jsp:include page="include/files.jsp"></jsp:include>
		<link href="resources/core/css/jquery.steps.css" rel="stylesheet" type="text/css" /> 
		</head>
    <body>
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
    				<h1>Personal Details</h1>
    				<fieldset>
    					<form:hidden path="driver.driverId" />
 								<div class="box-body ">
 								
 									<c:if test="${driverForm.driver.driverLogin.driverCode != null}">
 									 <div class="row form-group">
                                         <label for="exampleInputEmail1" class="col-xs-3">CSE Number</label>
                                         <div class="col-xs-3">
                                         	${driverForm.driver.driverLogin.driverCode} 
                                         </div>	
                                     </div>
 									</c:if>
                                     <div class="row form-group">
                                         <label for="driver.fname" class="col-xs-3">Name<span style="color:red;">*</span></label>
                                         <div class="col-xs-3">
                                         	<form:input type="text" class="form-control required" path="driver.fname" placeholder="First Name"/>
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
                                         
                                         
                                         <script> 
											function myAgeValidation() {
											
											    var lre = /^\s*/;
											    var datemsg = "";
											   
											    var inputDate = document.as400samplecode.myDate.value;
											    inputDate = inputDate.replace(lre, "");
											    document.as400samplecode.myDate.value = inputDate;
											    datemsg = isValidDate(inputDate);
											        if (datemsg != "") {
											            alert(datemsg);
											            return;
											        }
											        else {
											            //Now find the Age based on the Birth Date
											            getAge(new Date(inputDate));
											        }
											
											}
											
											function getAge(birth) {
											
											    var today = new Date();
											    var nowyear = today.getFullYear();
											    var nowmonth = today.getMonth();
											    var nowday = today.getDate();
											
											    var birthyear = birth.getFullYear();
											    var birthmonth = birth.getMonth();
											    var birthday = birth.getDate();
											
											    var age = nowyear - birthyear;
											    var age_month = nowmonth - birthmonth;
											    var age_day = nowday - birthday;
											   
											    if(age_month < 0 || (age_month == 0 && age_day <0)) {
											            age = parseInt(age) -1;
											        }
											    alert(age);
											   
											    if ((age == 18 && age_month <= 0 && age_day <=0) || age < 18) {
											    }
											    else {
											        alert("You have crossed your 18th birthday !");
											    }
											
											}
											
											function isValidDate(dateStr) {
											
											   
											    var msg = "";
											    // Checks for the following valid date formats:
											    // MM/DD/YY   MM/DD/YYYY   MM-DD-YY   MM-DD-YYYY
											    // Also separates date into month, day, and year variables
											
											    // To require a 2 & 4 digit year entry, use this line instead:
											    //var datePat = /^(\d{1,2})(\/|-)(\d{1,2})\2(\d{2}|\d{4})$/;
											    // To require a 4 digit year entry, use this line instead:
											    var datePat = /^(\d{1,2})(\/|-)(\d{1,2})\2(\d{4})$/;
											
											    var matchArray = dateStr.match(datePat); // is the format ok?
											    if (matchArray == null) {
											        msg = "Date is not in a valid format.";
											        return msg;
											    }
											
											    month = matchArray[1]; // parse date into variables
											    day = matchArray[3];
											    year = matchArray[4];
											
											   
											    if (month < 1 || month > 12) { // check month range
											        msg = "Month must be between 1 and 12.";
											        return msg;
											    }
											
											    if (day < 1 || day > 31) {
											        msg = "Day must be between 1 and 31.";
											        return msg;
											    }
											
											    if ((month==4 || month==6 || month==9 || month==11) && day==31) {
											        msg = "Month "+month+" doesn't have 31 days!";
											        return msg;
											    }
											
											    if (month == 2) { // check for february 29th
											    var isleap = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
											    if (day>29 || (day==29 && !isleap)) {
											        msg = "February " + year + " doesn't have " + day + " days!";
											        return msg;
											    }
											    }
											
											    if (day.charAt(0) == '0') day= day.charAt(1);
											   
											    //Incase you need the value in CCYYMMDD format in your server program
											    //msg = (parseInt(year,10) * 10000) + (parseInt(month,10) * 100) + parseInt(day,10);
											   
											    return msg;  // date is valid
											}
											
											 </script>
                                         <div class="col-xs-3">
                                         	<form:input type="date" class="form-control date required" path="driver.dob" onclick="Javascript:myAgeValidation()" placeholder="DOB"/>
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
                                         	<form:input type="text" style="width:50px;height:35px;display:inline;" class="form-control" path="" placeholder="+61"/>
                                         	<form:input type="text" class="form-control required number" style="width:172px;height:35px;display:inline;" path="driver.phone" placeholder="Phone" onblur="validate();"/>
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
                                         	<input  type="file" class="form-control required" value="imagePath" name="imageFile"/>
                                         	<c:if test="${driverForm.driver.photo != null && driverForm.driver.photo != ''}">
                                         		${driverForm.driver.photo}
                                         		<img alt="" src="../downloads/${driverForm.driver.photo}" width="100" height="100"/>
                                         	</c:if>
                                         </div>	
                                     </div>
                                     <div class="row form-group">
                                     
                                        <label for="exampleInputEmail1" class="col-xs-3">Address<span style="color:red;">*</span></label>
                                     	<div class="col-xs-3">
                                         	<form:input type="text" class="form-control required" path="driver.address" placeholder="Address"/>
                                         </div>
                                        <label for="exampleInputEmail1" class="col-xs-3">Street<span style="color:red;">*</span></label>
                                     	<div class="col-xs-3">
                                         	<form:input type="text" class="form-control required" path="driver.street" placeholder="Street"/>
                                        </div>
  
                                     </div>

                                     <div class="row form-group">
                                     
                                        <label for="exampleInputEmail1" class="col-xs-3">Area<span style="color:red;">*</span></label>
                                     	<div class="col-xs-3">
                                     	
											                                     	
											<!-- <fieldset style="width: 230px;">
											<legend><strong>Make your selection</strong></legend>
											<p>
											<form name="test" method="POST" action="processingpage.php">
											<table>
											<tr>
											<td style="text-align: left;">Country:</td>
											<td style="text-align: left;">
											<select name="country" id="country" onchange="setStates();">
											  <option value="Canada">Canada</option>
											  <option value="Mexico">Mexico</option>
											  <option value="United States">United States</option>
											</select>
											</td>
											</tr><tr>
											<td style="text-align: left;">State:</td>
											<td style="text-align: left;">
											<select name="state" id="state" onchange="setCities();">
											  <option value="">Please select a Country</option>
											</select>
											</td>
											</tr><tr>
											<td style="text-align: left;">City:</td>
											<td style="text-align: left;">
											<select name="city"  id="city">
											  <option value="">Please select a Country</option>
											</select>
											</td>
											</tr>
											</table>
											</form>
											</fieldset> --> 
												
                                         	<form:input type="text" class="form-control required" path="driver.area" placeholder="Area"/>
                                         </div>
                                        <label for="exampleInputEmail1" class="col-xs-3">City<span style="color:red;">*</span></label>
                                     	<div class="col-xs-3">
                                     	   <!-- <select name="city"  id="city">
											  <option value="">Sydney</option>
											  <option value="">Albury</option>
											  <option value="">Armydale</option>
											  <option value="">Goulburn</option>
											  <option value="">Liverpool</option>
											  <option value="">Newcastle</option>
											</select> -->
                                         	<form:input type="text" class="form-control required" path="driver.city" placeholder="City"/>
                                        </div>
  
                                     </div>
									
                                     <div class="row form-group">
                                        <label for="exampleInputEmail1" class="col-xs-3">State</label>
                                     	<div class="col-xs-3">
                                         	<form:input type="text" class="form-control" path="driver.state" placeholder="State"/>
                                         </div>
                                        <label for="exampleInputEmail1" class="col-xs-3">Country<span style="color:red;">*</span></label>
                                     	<div class="col-xs-3">
                                         	<form:input type="text" class="form-control required" path="driver.country" placeholder="Country"/>
                                        </div>
                                     </div>
									<hr>
                                     <div class="row form-group">
                                        <label for="exampleInputEmail1" class="col-xs-3">License No  <span style="color:red;">*</span></label>
                                     	<div class="col-xs-3">
                                         	<form:input type="text" class="form-control required" path="driver.licenseNo" placeholder="Liscense No." onblur="validate();"/>
                                         </div>
                                        <label for="exampleInputEmail1" class="col-xs-3">Upload</label>
                                     	<div class="col-xs-3">
                                         	<input  type="file" class="form-control" value="licensePath" name="licenseFile" />
                                         		<c:if test="${driverForm.driver.licensePath != null && driverForm.driver.licensePath != ''}">
                                         		${driverForm.driver.licensePath}
                                         		<img alt="" src="../downloads/${driverForm.driver.licensePath}" width="100" height="100"/>
                                         	</c:if>
                                        </div>
                                     </div>
									<hr>
									
                                     <div class="row form-group">
                                        <label for="exampleInputEmail1" class="col-xs-3">Driver Authorization <span style="color:red;">*</span></label>
                                     	<div class="col-xs-3">
                                         	<form:input type="text" class="form-control required" path="driver.driverAuth" placeholder="Driver Authorization"/>
                                         </div>
                                        <label for="exampleInputEmail1" class="col-xs-3">Upload <!-- <span style="color:red;">*</span> --></label>
                                     	<div class="col-xs-3">
                                         	<input  type="file" class="form-control" value="driverAuthPath" name="driverAuthFile" />
                                         		<c:if test="${driverForm.driver.driverAuthPath != null && driverForm.driver.driverAuthPath != ''}">
                                         		${driverForm.driver.driverAuthPath}
                                         		<img alt="" src="../downloads/${driverForm.driver.driverAuthPath}" width="100" height="100"/>
                                         	</c:if>
                                        </div>
                                     </div>
                                     <hr>
                                     <div class="row form-group">
                                        <label for="exampleInputEmail1" class="col-xs-3">Driver Insuarance <span style="color:red;">*</span></label>
                                     	<div class="col-xs-3">
                                         	<form:input type="text" class="form-control required" path="driver.insurance" placeholder="Driver's Insurance"/>
                                         </div>
                                        <label for="exampleInputEmail1" class="col-xs-3">Upload <!-- <span style="color:red;">*</span> --></label>
                                     	<div class="col-xs-3">
                                         	<input  type="file" class="form-control" value="insurancePath" name="insuranceFile"/>
                                         	<c:if test="${driverForm.driver.insurancePath != null && driverForm.driver.insurancePath != ''}">
                                         		${driverForm.driver.insurancePath}
                                         		<img alt="" src="../downloads/${driverForm.driver.insurancePath}" width="100" height="100"/>
                                         	</c:if>
                                        </div>
                                     </div>
									<hr>
                                     <div class="row form-group">
                                        <label for="exampleInputEmail1" class="col-xs-3">Accesibility</label>
                                     	<div class="col-xs-3">
                                         	<form:input type="text" class="form-control" path="driver.accesebility" placeholder="Accesibility"/>
                                         </div>
                                        <label for="exampleInputEmail1" class="col-xs-3">Service Type</label>
                                        <div class="col-xs-3">
                                         	<form:select type="text" class="form-control" path="driver.serviceType" >
                                         		<c:forEach var="serviceType" items="${configMap['SERVICE_TYPE']}">
												    <option value="${serviceType.value.attrValue1}"  <c:if test="${driverForm.driver.serviceType == serviceType.value.attrValue1}" >selected</c:if>>${serviceType.key}</option>
												</c:forEach>
                                         	</form:select>
                                         </div>
                                     </div>
       
								</div>	
						</fieldset>
    					<h1>Vehicles</h1>
    					<fieldset>
    					
    						        <div class="row form-group">
                                        <label for="exampleInputEmail1" class="col-xs-3">Vehicle Registration No<span style="color:red;">*</span></label>
                                     	<div class="col-xs-3">
                                         	<form:input type="text" class="form-control required" path="vehicle.registrationId" placeholder="Vehicle Registration No."/>
                                         </div>
                                        <label for="exampleInputEmail1" class="col-xs-3">Upload <!-- <span style="color:red;">*</span> --></label>
                                     	<div class="col-xs-3">
                                         	<input  type="file" class="form-control" value="vehicle.registrationId" name="registrationFilePath"/>
                                         	<c:if test="${driverForm.vehicle.registrationDocPath != null && driverForm.vehicle.registrationDocPath != ''}">
                                         		${driverForm.vehicle.registrationDocPath}
                                         		<img alt="" src="../downloads/${driverForm.vehicle.registrationDocPath}" width="100" height="100"/>
                                         	</c:if>
                                        </div>
                                        
                                     </div>
                                     <hr>
    						        <div class="row form-group">
                                        <label for="exampleInputEmail1" class="col-xs-3">Vehicle Insurance Policy No <span style="color:red;">*</span></label>
                                     	<div class="col-xs-3">
                                         	<form:input type="text" class="form-control required" path="vehicle.insuarancePolicyNo" placeholder="Vehicle Insurance Policy No."/>
                                         </div>
                                        <label for="exampleInputEmail1" class="col-xs-3">Upload <!-- <span style="color:red;">*</span> --></label>
                                     	<div class="col-xs-3">
                                         	<input  type="file" class="form-control" value="" name="insuranceFilePath"/>
                                        	<c:if test="${driverForm.vehicle.insuranceDocPath != null && driverForm.vehicle.insuranceDocPath != ''}">
                                         		${driverForm.vehicle.insuranceDocPath}
                                         		<img alt="" src="../downloads/${driverForm.vehicle.insuranceDocPath}" width="100" height="100"/>
                                         	</c:if>
                                        </div>
                            		</div>
                                     <div class="row form-group">
                                        <label for="exampleInputEmail1" class="col-xs-3">Insurance Renewal Date</label>
	                                    
                                        <div class="col-xs-3">                                         
                                         	<form:input type="text" class="form-control" path="vehicle.insuranceRenewalDate" placeholder="Insurance Renewal Date"/>
                                         </div>
                                     </div>
    								<hr>
                                     <div class="row form-group">
                                        <label for="v" class="col-xs-3">Make</label>
                                     	<div class="col-xs-3">
                                         	<form:input type="text" class="form-control" path="vehicle.make" placeholder="Make"/>
                                         </div>
                                        <label for="v" class="col-xs-3">Model</label>
                                     	<div class="col-xs-3">
                                         	<form:input type="text" class="form-control" path="vehicle.model" placeholder="Model"/>
                                         </div>
                                     </div>
                                              <div class="row form-group">
                                        <label for="v" class="col-xs-3">Owner</label>
                                     	<div class="col-xs-3">
                                         	<form:input type="text" class="form-control" path="vehicle.ownerId" placeholder="Owener's Id"/>
                                         </div>
                                     </div>			
							
               					</fieldset>
    					
    					<h1>Bank</h1>
 	   					<fieldset>
 	   					
                          		<div class="row form-group">
                                   <label for="dob" class="col-xs-3">Bank Name <span style="color:red;">*</span></label>
                                   <div class="col-xs-3">
                                   	<form:input class="form-control required" path="bank.bankName" placeholder="Bank Name"/>
                                   </div>	

                                   <label for="gender" class="col-xs-3">Account Number <span style="color:red;">*</span></label>
                                   <div class="col-xs-3">
                                   	<form:input class="form-control required" path="bank.bankAccNo" placeholder="Bank Account Number"/>
                                   </div>	

                                </div>
                                           
                        		<div class="row form-group">
                                   <label for="dob" class="col-xs-3">Account Holder Name <span style="color:red;">*</span></label>
                                   <div class="col-xs-3">
                                   	<form:input class="form-control required" path="bank.nameOfAcc" placeholder="Account Holder Name"/>
                                   </div>	

                                   <label for="gender" class="col-xs-3">Branch Code <span style="color:red;">*</span></label>
                                   <div class="col-xs-3">
                                   	<form:input class="form-control required" path="bank.bankBranch" placeholder="Branch Code"/>
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
    					</fieldset>
    					
                
                <!-- </div> -->
             </form:form>
                    
                </section>

             </aside><!-- /.right-side -->
        </div><!-- ./wrapper -->

        <!-- add new calendar event modal -->

       <jsp:include page="include/js_inc.jsp"></jsp:include>

       <script src="resources/core/js/jquery.steps.min.js" type="text/javascript"></script>        
		
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
        </script>
    </body>
</html>