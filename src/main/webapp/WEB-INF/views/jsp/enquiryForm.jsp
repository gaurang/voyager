<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>

<html>
    <head>
        <meta charset="UTF-8">
        <title>Enquiry Form</title>
        <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>		
		  <meta name="viewport" content="width=device-width, initial-scale=1">
		  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
		  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
		  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
		  
							
        <jsp:include page="include/files.jsp"></jsp:include>
		<link href="resources/core/css/jquery.steps.css" rel="stylesheet" type="text/css" /> 
		</head>
	<body class="bgImage">
	<img alt="" class="bg" src="${pageContext.request.contextPath}/resources/core/img/bgimg.png" width="75%">
		<!-- <div class="centerLogo col-xd-6"> 
		<div class="responsive-container">
		    <div class="dummy"></div>
		
		    <div class="img-container">
		        <img alt="" src="resources/core/img/UCBGSCREEN.png" />
		    </div>
		</div>
		
		</div> -->
<!-- style="background: transparent !important;width:50%;margin: 0 auto;border: 1px thin #ccc;" -->
		<div align="center"   class="col-xd-6 transparent">
		    <div >
		      <!--   <img alt="" src="resources/core/img/UCBGBlank.png" style="height: 75px !important;margin-top:10px; "/>-->
		    </div> 
		<!--			<div id="error-message" class="errorList"></div>-->
    	<div class="layer" style="background: transparent !important;" align="center">
		<!--  enctype="multipart/form-data">-->
			<form action="saveEnquiry" method="post" class="transperent" name="enquiryForm" id="enquiryForm">             
    				<h1 align="center" style="font-weight: bold;">Expression of Interest </h1><br><br>
 								<div class="box-body ">
 								
 								     <div class="row form-group">
                                         <div><label class="col-xs-4" style="font-size:21px;font-weight:bold;">City <span style="color:red;">*</span></label></div>
                                         <div class="col-xs-6">
                                         	<input type="text" class="form-control required" name="city" placeholder="City"/>
                                        </div>
                                     </div>
 									
                                     
                                      <div class="row form-group" >
                                         <label class="col-xs-4" style="padding-right:40px; font-size:21px;">Name<span style="color:red;">*</span></label>
                                         <div class="col-xs-6" style="display:inline;">
                                         	<input type="text" style="width:49.3%;display:inline-block;" class="form-control required" name="firstName" placeholder="First Name"/>                                         
                                         	<input type="text" style="width:49.3%;display:inline-block;" class="form-control required" name="lastName" placeholder="Last Name"/>
                                         </div>
                                     </div>
                                     
                               		<div class="row form-group">
                                         <div><label class="col-xs-4" style="font-size:21px;">Email <span style="color:red;">*</span></label></div>
                                         <div class="col-xs-6">
                                         	<input type="text" class="form-control required" name="email" placeholder="Email"/>
                                        </div>
                                  </div>
                                  <div class="row form-group">
                                         <div><label class="col-xs-4" style="font-size:21px;">Phone <span style="color:red;">*</span></label></div>
                                         <div class="col-xs-6" style="display:inline;">
                                         	<input type="text" readonly="readonly" style="width:20%;hetght:100%;padding:0px;margin:0px;display:inline;text-align:center;" class="form-control" name="countryCode" value="+61" placeholder="+61"/>
                                         	<input type="text" style="width:78%; hetght:100%;padding:0px;margin:0px;display:inline;padding-left:20px;" class="form-control required" name="mobile" placeholder="Phone"/>
                                        </div>
                                  </div>
                                  <br>
                                  	<div class="row form-group">
                                  		<div><label class="col-xs-7" style="font-size:21px;">Would you like to join us as:</label></div>
                                         <div class="col-xs-5" style="text-align:left;">
                                              <input type="checkbox" name="cseType1" value="Private CSE">&nbsp;&nbsp;Private Taxi Driver<br>
                                              <input type="checkbox" name="cseType2" value="Taxi CSE">&nbsp;&nbsp;Taxi Driver<br>
    										  <input type="checkbox" name="cseType3" value="Passenger">&nbsp;&nbsp;Passenger<br>
    										  <input type="checkbox" name="cseType4" value="Corporate Customer">&nbsp;&nbsp;Corporate Customer<br>
										</div>
  									</div>
                                  <br>
                                  <div class="row form-group">
                                         <div><label class="col-xs-7" style="font-size:21px;">Do you drive with us? </label></div>
                                         <div class="col-xs-5" style="display:inline;">
                                         	<input type="text" class="form-control" name="driveWith" placeholder="Uber | Taxi | Others"/>
                                        </div>
                                  </div>
                                  <!-- <div class="row form-group">
                                         <div><label class="col-xs-6">Invite Code</label></div>
                                         <div class="col-xs-6">
                                         	<input type="text" class="form-control required" placeholder="Code"/>
                                        </div>
                                  </div>
                                  <br>-->
                                  	<div class="row form-group">
                                  		<div><label class="col-xs-7" style="font-size:21px;">Where did you hear about us?</label></div>
                                         <div class="col-xs-5" style="color:#555;">
    										<select style="width:100%; height:35px" name="aboutUs" >
    										    <option value="">Select</option>
  												<option value="PodCast">Pod Cast</option>
  												<option value="Family and Friends">Family and Friends</option>
  												<option value="Mail">Mail</option>
  												<option value="Television">Television</option>
  												<option value="Others">Others</option>
											</select>
										</div>
  									</div>
                                  <br>
                                  <div class="row form-group" style="float:center;padding-left:270px;">
                                         <div class="col-xs-6" >
                                         	<input  type="submit" class="btn btn-yellow" value="Submit" style="width:100px; height:35px;"/>
                                         </div>	
                                  </div>
                                  </div>
                                  
                                  <div align="center" class="contactUs">
                                  		Contact Us @ <a href="mailto:info@voyager.me">info@voyager.me</a>
                                  </div>
	                 </form>
                 </div>           
                </div>
                        <jsp:include page="include/js_inc.jsp"></jsp:include>
                 
				        <script> 
						
						 $(function() {
				  
				  		  // Setup form validation on the #register-form element
				    	 $("#enquiryForm").validate({
				    
				              submitHandler: function(form) {
				            		form.submit();
						        },
						        invalidHandler: function(form, validator) {
						            var errors = validator.numberOfInvalids();

						            if (errors) {
						                $("#error-message").show().text("You missed " + errors + " field(s)");
						            } else {
						                $("#error-message").hide();
						            }
						        },
						        /*errorPlacement: function(error, element) {
						            element = element.closest('li');
						            element = element.children(':first-child');
						            error.insertBefore(element);
						            error.addClass('message');
						            $(function() { 
						            	$('.errorList').html();
						            	// my function
						                var errorIndex = $(error).index('div');
						                var errorId = 'errordiv' + errorIndex.toString();
						                $(error).attr('id', errorId);
						                $('.errorList').append('<li><a href="#' + errorId + '">' + $(error).text() + '</a></li>');
						            });    
						        },*/
						        messages: {
						            firstName: "Enter your firstname",
						            lastName: "Enter your lastname",
						            mobile: "Enter your Phone",
						            email: "Enter your Email",
						            city: "Enter your City"
						        }

						    });
						
						  });
				  
				  		</script>
						<style>
							.no-print{
							}
						</style>	  
                
           </body>
   </html>
			