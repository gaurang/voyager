<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>

<html>
    <head>
        <meta charset="UTF-8">
        <title>Corporate Registration</title>
        <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>		
		  <meta name="viewport" content="width=device-width, initial-scale=1">
		  <!-- <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css"> -->
<!--  		  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script> --> 
<style>
.modal {
    display: none; /* Hidden by default */
    position: fixed; /* Stay in place */
    z-index: 1; /* Sit on top */
    padding-top: 100px; /* Location of the box */
    left: 0;
    top: 0;
    width: 100%; /* Full width */
    height: 100%; /* Full height */
    overflow: auto; /* Enable scroll if needed */
    background-color: rgb(0,0,0); /* Fallback color */
    background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
}

/* Modal Content */
.modal-content {
    background-color: #fefefe;
    margin: auto;
    padding: 20px;
    border: 1px solid #888;
    width: 80%;
}
.close {
    color: #aaaaaa;
    float: right;
    font-size: 28px;
    font-weight: bold;
}
.close:hover,
.close:focus {
    color: #000;
    text-decoration: none;
    cursor: pointer;
}
</style>
							
        <jsp:include page="include/files.jsp"></jsp:include>
		<link href="${pageContext.request.contextPath}/resources/core/css/jquery.steps.css" rel="stylesheet" type="text/css" /> 
		</head>
<body class="bgImage">
	<img alt="" class="bg" src="${pageContext.request.contextPath}/resources/core/img/UCBGBlank1.png">
	<div align="center"   class="col-xd-6 transparent">
	
		<!--			<div id="error-message" class="errorList"></div>-->
    <div class="layer" style="background: transparent !important;" align="center">
		<!--  enctype="multipart/form-data">-->
	<br>
	<br>
	<h1>
    	Corporate Client         
    </h1>
    <br>
    <br>
    <div id="myModal" class="modal">
  <div class="modal-content">
    <span class="close">×</span>
    <p>Thank You for registering with voyager.Our voyager team will contact you soon</p>
  </div>
</div>
    
    <form action="saveCorpReg" method="post" class="transperent" enctype="multipart/form-data" id="corporate" style="width:70%;">             
               
    				
    				<div >
	   					

 								<div class="box-body ">
 									
 								
	 								<div class="row form-group">
	                                         <label for="customer.corporateName" class="col-xs-3" >Corporate Name<span style="color:red;">*</span></label>
	                                         <div class="col-xs-3" style="color:#E62B2B;">
	                                         	<input type="text" class="form-control required" name="corporateName" placeholder="Corporate Name"/>
											 </div>
                                     </div>
                                     <div class="row form-group">
                                        <label for="exampleInputEmail1" class="col-xs-3">Address <span style="color:red;">*</span></label>
                                     	<div class="col-xs-3" style="color:#E62B2B;">
                                         	<input type="text" class="form-control required" name="address" placeholder="Address"/>
                                        </div>
                                        <label for="exampleInputEmail1" class="col-xs-3">Street <span style="color:red;">*</span></label>
                                     	<div class="col-xs-3" style="color:#E62B2B;">
                                         	<input type="text" class="form-control required" name="street"  placeholder="Street"/>
                                         </div>
                                     </div>

                                     <div class="row form-group">
                                     
                                        <label for="exampleInputEmail1" class="col-xs-3">Area</label>
                                     	<div class="col-xs-3">
                                         	<input type="text" class="form-control" name="area" placeholder="Area"/>
                                         </div>
                                        <label for="exampleInputEmail1" class="col-xs-3">City<span style="color:red;">*</span></label>
                                     	<div class="col-xs-3" style="color:#E62B2B;">
                                         	<input type="text" class="form-control required" name="city" placeholder="City"/>
                                        </div>
  
                                     </div>

                                     <div class="row form-group">
                                        <label for="exampleInputEmail1" class="col-xs-3">State</label>
                                     	<div class="col-xs-3">
                                         	<input type="text" class="form-control" name="state" placeholder="State"/>
                                         </div>
                                        <label for="exampleInputEmail1" class="col-xs-3">Country<span style="color:red;">*</span></label>
                                     	<div class="col-xs-3" style="color:#E62B2B;">
                                         	<input type="text" class="form-control required" name="country" placeholder="Country"/>
                                        </div>
                                     </div>
 
                               		                               
  								     
                                    <div class="row form-group">
                                        <label for="exampleInputEmail1" class="col-xs-3">ABN / ACN No. <span style="color:red;">*</span></label>
                                     	<div class="col-xs-3" style="color:#E62B2B;">
                                         	<input type="text" class="form-control required" name="abnAcnNo" placeholder="ABN / ACN No."/>
                                         </div>
                                          <label for="exampleInputEmail1" class="col-xs-3">Zip Code</label>
                                     	<div class="col-xs-3">
                                         	<input type="text" class="form-control" name="pincode" placeholder="Zip Code"/>
                                         </div>
                                     </div>
 
 									<div class="row form-group">
                                         <label for="exampleInputEmail1" class="col-xs-3">Phone No.<span style="color:red;">*</span></label>
                                         <div class="col-xs-3" style="color:#E62B2B;">
                                         	<input type="text" class="form-control required number" name="phone" placeholder="Phone No."/>
                                         </div>	
                                         
                                     </div>

                                     <div class="row form-group">
                                         <label for="exampleInputEmail1" class="col-xs-3">Email address<span style="color:red;">*</span></label>
                                         <div class="col-xs-3" style="color:#E62B2B;">
                                         	<input type="email" class="form-control required email" name="email" placeholder="Email Id"/>
                                         </div>
                                         
                                     </div>
 
                                    <div class="row form-group">
                                         <label for="exampleInputEmail1" class="col-xs-3">Upload Logo</label>
                                         <div class="col-xs-3">
                                         	<input  type="file" class="form-control" name="logoPath"/>
                                         	<c:if test="${customerForm.corpCust.logoPath != null && customerForm.corpCust.logoPath != ''}">
                                         		${customerForm.corpCust.logoPath}
                                         		<img alt="" src="../downloads/${customerForm.corpCust.logoPath}" width="100" height="100"/>
                                         	</c:if>
                                         
                                         </div>	
                                     </div>
 									<br>
                                    <div class="row form-group" style="float:center;padding-left:50%;">
                                         <div class="col-xs-6">
                                         	<input  type="submit" class="btn btn-yellow" value="Submit" id="myBtn" />
                                         </div>	
                                     </div>
                                     
         							
								</div>	
								
								
						</div>
                
                
             </form>
    	</div>
    	</div>
    	<jsp:include page="include/js_inc.jsp"></jsp:include>
    	
    	
</body>
</html>

		  <script>
			var btn = document.getElementById("myBtn");
			var span = document.getElementsByClassName("close")[0];
			btn.onclick = function() {
			    modal.style.display = "block";
			}
			span.onclick = function() {
			    modal.style.display = "none";
			}
			window.onclick = function(event) {
			    if (event.target == modal) {
			        modal.style.display = "none";
			    }
			}
			</script>
