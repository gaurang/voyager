<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
    	<meta charset="UTF-8">
        <title>User Form</title>
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
		        <jsp:include page="include/menu.jsp"></jsp:include>
                <!-- /.sidebar -->
            </aside>

            <!-- Right side column. Contains the navbar and content of the page -->
            <aside class="right-side">
                <!-- Content Header (Page header) -->
                <section class="content-header">
                    <h1>
                        User Login
                        
                    </h1>
                    
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                        <li class="active">CSE</li><li class="active">CSE</li>
                    </ol>
					<p>&nbsp;</p>
	   <form action="" method="post"  id="userForm" class="box box-primary" name="userForm" enctype="multipart/form-data">             
    		<h1>User Form</h1>
    			<div class="box-body ">
    		<div class="row form-group">
            	<label class="col-xs-3">Name<span style="color:red;">*</span></label>
                <div class="col-xs-3">
              		<input type="text" class="form-control required" placeholder="First Name"/>
				</div>
				<div class="col-xs-3">                                         
                	<input type="text" class="form-control" placeholder="Middle Name"/>
                </div>
                <div class="col-xs-3">                                         
                	<input type="text" class="form-control required"  placeholder="Last Name"/>
                </div>
          </div>
          
   		 <div class="row form-group">
         	<label for="dob" class="col-xs-3">Date of Birth <span style="color:red;">*</span></label>
            <div class="col-xs-3">
            	<input type="date" class="form-control date required"  placeholder="DOB"/>
            </div>	
 
            <label for="gender" class="col-xs-3">&nbsp;&nbsp;&nbsp;Gender<span style="color:red;">*</span></label>
            <div class="col-xs-3">
            	<input type="radio"  value="m" class="required" />&nbsp;&nbsp;&nbsp;Male <input type="radio"  value="f" />&nbsp;&nbsp;&nbsp;Female
            </div>	
 		</div>  
 		
 		<div class="row form-group">
        	<label for="exampleInputEmail1" class="col-xs-3">Phone No.<span style="color:red;">*</span></label>
        	<div class="col-xs-3">
           		<input type="text" class="form-control required number"  placeholder="Phone No." onblur="validate();"/>
        	</div>	
        </div>
        <div class="row form-group">
        	<label for="exampleInputEmail1" class="col-xs-3">Email address<span style="color:red;">*</span></label>
        	<div class="col-xs-3">
           		<input type="email" class="form-control required email"  placeholder="Email"/>
        	</div>	
        </div>
        <div class="row form-group">
        <label for="exampleInputEmail1" class="col-xs-3">Upload Picture<span style="color:red;">*</span></label>
        	<div class="col-xs-3">
            <input  type="file" class="form-control required" value="imagePath" name="imageFile"/>
            <!-- <img alt="" src="../downloads/${driverForm.driver.photo}" width="100" height="100"/>-->
            </div>	
       </div> 
          
       <div class="row form-group">
          <label for="exampleInputEmail1" class="col-xs-3">Address<span style="color:red;">*</span></label>
          <div class="col-xs-3">
          	<input type="text" class="form-control required"  placeholder="Address"/>
          </div>
          <label for="exampleInputEmail1" class="col-xs-3">Street<span style="color:red;">*</span></label>
          <div class="col-xs-3">
           	<input type="text" class="form-control required"  placeholder="Street"/>
          </div>
      </div>
          
          
      <div class="row form-group">
         <label for="exampleInputEmail1" class="col-xs-3">Area<span style="color:red;">*</span></label>
         <div class="col-xs-3">
          	<input type="text" class="form-control required"  placeholder="Area"/>
         </div>
         <label for="exampleInputEmail1" class="col-xs-3">City<span style="color:red;">*</span></label>
         <div class="col-xs-3">
           	<input type="text" class="form-control required"  placeholder="City"/>
         </div>
     </div>
									
     <div class="row form-group">
     	<label for="exampleInputEmail1" class="col-xs-3">State</label>
     	<div class="col-xs-3">
       		<input type="text" class="form-control"  placeholder="State"/>
     	</div>
     	<label for="exampleInputEmail1" class="col-xs-3">Country<span style="color:red;">*</span></label>
     	<div class="col-xs-3">
      		<input type="text" class="form-control required"  placeholder="Country"/>
     	</div>
     </div>
     
     <div class="text-center">
     	<input type="submit" value="submit" style="width:100px;height:30px;"></input>
     </div>
     </div>
     </form>
     </section>
     </aside><!-- /.right-side -->
        </div><!-- ./wrapper -->
          
      <jsp:include page="include/js_inc.jsp"></jsp:include>
    </body>
 </html>
 
