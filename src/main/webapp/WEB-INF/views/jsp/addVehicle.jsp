<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Add Vehicles</title>
        <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
		<script type="text/javascript" src="countryStateCity.js"></script>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
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
                        Add Vehicle
                        
                    </h1>
                    
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                        <li class="active">CSE</li><li class="active">CSE</li>
                    </ol>
					<p>&nbsp;</p>
	
			
      
         <c:choose>
    		<c:when test="${param.vehInsuranceNo != null}">
        		Value added to database successfully. 
        		<br />
    		</c:when>
		</c:choose>
         
	    <form action="saveVehicle" method="post" enctype="multipart/form-data" id="vehicle">             
             <div id="wizard" class="box box-primary" ><br>
             
    		<div >
    		
    		<div class="box-body ">
 			<div class="row form-group">
            	<label for="exampleInputEmail1" class="col-xs-3">Vehicle Insurance Policy No <span style="color:red;">*</span></label>
                	<div class="col-xs-3" style="color:#E62B2B;">
                    	<input type="text" class="form-control required" name="vehInsuranceNo"  placeholder="Vehicle Insurance Policy No."/>
                    </div>
                    <label for="exampleInputEmail1" class="col-xs-3">Upload  <span style="color:red;">*</span> </label>
                    <div class="col-xs-3" style="color:#E62B2B;">
       					<input  type="file" class="form-control required" name="insuranceFilePath"/>
                    </div>
            </div>
                                     <div class="row form-group">
                                        <label for="exampleInputEmail1" class="col-xs-3">Insurance Renewal Date<span style="color:red;">*</span></label>
	                                    
                                         <div class="col-xs-3" style="color:#E62B2B;">
                                         	<input type="date" class="form-control date required" name="vehinsuranceRenewDt"  placeholder="Insurance Renewal Date"/>
                                    	</div>	
                                     </div>
    								<hr>
                                     <div class="row form-group">
                                        <label for="v" class="col-xs-3">Make<span style="color:red;">*</span></label>
                                     	<div class="col-xs-3" style="color:#E62B2B;">
                                         	<input type="text" class="form-control required" name="make" placeholder="Make"/>
                                         </div>
                                        <label for="v" class="col-xs-3">Model<span style="color:red;">*</span></label>
                                     	<div class="col-xs-3" style="color:#E62B2B;">
                                         	<input type="text" class="form-control required" name="model" placeholder="Model"/>
                                         </div>
                                     </div>
                                     <div class="row form-group">
                                        <label for="exampleInputEmail1" class="col-xs-3">Type of Car <span style="color:red;">*</span></label>
                                     	<div class="col-xs-3" style="color:#E62B2B;">
                                         	<select class="form-control required" name="vType" >
                                         		<option value="HAT">Hatch Back</option>
                                         		<option value="SED">Sedan</option>
                                         		<option value="SUV">SUV</option>
                                         		<option value="MUV">MUV</option>
                                         		<option value="O" >Others</option>
                                         	</select>
                                         </div>
                                         <label for="v" class="col-xs-3">Car Number<span style="color:red;">*</span></label>
                                     	<div class="col-xs-3" style="color:#E62B2B;">
                                         	<input type="text" class="form-control required" name="carNumber" placeholder="Car Number"/>
                                         </div>
                                     </div>
									
                                     <div class="row form-group">
                                        <label for="v" class="col-xs-3">Owner</label>
                                     	<div class="col-xs-3">
                                         	<input type="text" class="form-control" name="ownerId" placeholder="Owener's Id"/>
                                         </div>
                                     </div>			
							
                                     <div class="row form-group">
                                        <label for="v" class="col-xs-3">Attributes</label>
                                     	<div class="col-xs-3">
                                     	  <select  class="form-control" name="vehAttributes">
                                     	    <option value="Baby Seat">Select</option>
                                         	<option value="Baby Seat">Baby Seat</option>
                                         	<option value="Animal Seat">Animal Seat</option>
                                         	<option value="Over Size Luggage">Over Size Luggage</option>
                           				  </select>
                                         </div>
                                     </div>			
							    						
	    					         <div class="row form-group">
	                                        <label for="v" class="col-xs-3">Seat Capacity</label>
	                                     	<div class="col-xs-3">
	                                         	<input type="text" class="form-control" name="seatingCapacity" placeholder="No. of Passengers"/>
	                                         </div>
	                                 </div>
      	<br>
      	<br>
      	<div class="text-center">
      	  <input type="submit" id="submit" value="submit" style="width:100px;height:30px;"></input>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      	  <button type="reset" value="Reset" style="width:100px;height:30px;">Reset</button>
      	</div>
      	<br><br>
      	</div>
      	</div>
      	</div>
      </form>
      
   
      <jsp:include page="include/js_inc.jsp"></jsp:include>
     
      </section>
      </aside>
      </div>
      
      <script>
 //           $("#wizard").steps();
        </script>
        
        		<script> 
		
		 $(function() {
  
  		  // Setup form validation on the #register-form element
    	 $("#vehicle").validate({
    
              submitHandler: function(form) {
            	form.submit();
		        }
		    });
		
		  });
  
  		</script>
      
      
    </body>
 </html>
 
