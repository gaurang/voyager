<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE html>

<html>
    <head>
        <meta charset="UTF-8">
        <title>AdminLTE | AddEmp</title>
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
		        <jsp:include page="include/menu.jsp"></jsp:include>
                <!-- /.sidebar -->
            </aside>

            <!-- Right side column. Contains the navbar and content of the page -->
            <aside class="right-side">
                <!-- Content Header (Page header) -->
                <section class="content-header">
                    <h1>
                        Emp
                        
                    </h1>
                    
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                        <li class="active">CSE</li><li class="active">CSE</li>
                    </ol>
					<p>&nbsp;</p>
	    <form id="emp" action="listEmp" method="post">
	    <input type="hidden" value='<sec:authentication property="principal.userDetails.corpCustId"/>' name="corpCustId">
	    
      	<div class="row">
      	   <div class="col-sm-12">
      	      	  <div class="table-responsive">
	                    <!-- .table - Uses sparkline charts-->
	                     <table class="table table-striped" id="empTable">
	                        <tr>
	                          <th>
	                            <div class="name" style="display:inline-block;">
	                              <div class="text-left" style="padding-left:30px;display:inline-block;"> First Name</div>
			                      <div class="text-left" style="padding-left:170px;display:inline-block;">Last Name</div>
			                    </div>
	                          </th>
	                          <th><div class="text-left" style="padding-left:50px;">Email</div></th>
	                        </tr> 
	                        <tr>
	                         <td>
	                          <div class="name" style="display:inline-block;">
								 <div class="text-left" style="padding-left:30px;display:inline-block;"><input type="text" name="firstname" placeholder="First Name" style="width:180px;"></div>					 
								 <div class="text-left" style="padding-left:50px;display:inline-block;"><input type="text" name="lastname" placeholder="Last Name" style="width:180px;"></div>					 
	                          </div>
	                         </td>
	                         <td>
	                           <div class="text-left" style="padding-left:50px;"><input type="text" name="email" placeholder="Email" style="width:250px;"></div>
	                          </td>
	                        </tr> 
	                        <tr>
	                         <td>
	                          <div class="name" style="display:inline-block;">
								 <div class="text-left" style="padding-left:30px;display:inline-block;"><input type="text" name="firstname" placeholder="First Name" style="width:180px;"></div>					 
								 <div class="text-left" style="padding-left:50px;display:inline-block;"><input type="text" name="lastname" placeholder="Last Name" style="width:180px;"></div>					 
	                          </div>
	                         </td>
	                         <td>
	                           <div class="text-left" style="padding-left:50px;"><input type="text" name="email" placeholder="Email" style="width:250px;"></div>
	                          </td>
	                        </tr>
	                        <tr>
	                         <td>
	                          <div class="name" style="display:inline-block;">
								 <div class="text-left" style="padding-left:30px;display:inline-block;"><input type="text" name="firstname" placeholder="First Name" style="width:180px;"></div>					 
								 <div class="text-left" style="padding-left:50px;display:inline-block;"><input type="text" name="lastname" placeholder="Last Name" style="width:180px;"></div>					 
	                          </div>
	                         </td>
	                         <td>
	                           <div class="text-left" style="padding-left:50px;"><input type="text" name="email" placeholder="Email" style="width:250px;"></div>
	                          </td>
	                        </tr> 
	                        <tr>
	                         <td>
	                          <div class="name" style="display:inline-block;">
								 <div class="text-left" style="padding-left:30px;display:inline-block;"><input type="text" name="firstname" placeholder="First Name" style="width:180px;"></div>					 
								 <div class="text-left" style="padding-left:50px;display:inline-block;"><input type="text" name="lastname" placeholder="Last Name" style="width:180px;"></div>					 
	                          </div>
	                         </td>
	                         <td>
	                           <div class="text-left" style="padding-left:50px;"><input type="text" name="email" placeholder="Email" style="width:250px;"></div>
	                          </td>
	                        </tr> 
	                        <tr>
	                         <td>
	                          <div class="name" style="display:inline-block;">
								 <div class="text-left" style="padding-left:30px;display:inline-block;"><input type="text" name="firstname" placeholder="First Name" style="width:180px;"></div>					 
								 <div class="text-left" style="padding-left:50px;display:inline-block;"><input type="text" name="lastname" placeholder="Last Name" style="width:180px;"></div>					 
	                          </div>
	                         </td>
	                         <td>
	                           <div class="text-left" style="padding-left:50px;"><input type="text" name="email" placeholder="Email" style="width:250px;"></div>
	                          </td>
	                        </tr>
	                        <tr>
	                         <td>
	                          <div class="name" style="display:inline-block;">
								 <div class="text-left" style="padding-left:30px;display:inline-block;"><input type="text" name="firstname" placeholder="First Name" style="width:180px;"></div>					 
								 <div class="text-left" style="padding-left:50px;display:inline-block;"><input type="text" name="lastname" placeholder="Last Name" style="width:180px;"></div>					 
	                          </div>
	                         </td>
	                         <td>
	                           <div class="text-left" style="padding-left:50px;"><input type="text" name="email" placeholder="Email" style="width:250px;"></div>
	                          </td>
	                        </tr>
	                        <tr>
	                         <td>
	                          <div class="name" style="display:inline-block;">
								 <div class="text-left" style="padding-left:30px;display:inline-block;"><input type="text" name="firstname" placeholder="First Name" style="width:180px;"></div>					 
								 <div class="text-left" style="padding-left:50px;display:inline-block;"><input type="text" name="lastname" placeholder="Last Name" style="width:180px;"></div>					 
	                          </div>
	                         </td>
	                         <td>
	                           <div class="text-left" style="padding-left:50px;"><input type="text" name="email" placeholder="Email" style="width:250px;"></div>
	                          </td>
	                        </tr>
	                        <tr>
	                         <td>
	                          <div class="name" style="display:inline-block;">
								 <div class="text-left" style="padding-left:30px;display:inline-block;"><input type="text" name="firstname" placeholder="First Name" style="width:180px;"></div>					 
								 <div class="text-left" style="padding-left:50px;display:inline-block;"><input type="text" name="lastname" placeholder="Last Name" style="width:180px;"></div>					 
	                          </div>
	                         </td>
	                         <td>
	                           <div class="text-left" style="padding-left:50px;"><input type="text" name="email" placeholder="Email" style="width:250px;"></div>
	                          </td>
	                        </tr>
	                        <tr>
	                         <td>
	                          <div class="name" style="display:inline-block;">
								 <div class="text-left" style="padding-left:30px;display:inline-block;"><input type="text" name="firstname" placeholder="First Name" style="width:180px;"></div>					 
								 <div class="text-left" style="padding-left:50px;display:inline-block;"><input type="text" name="lastname" placeholder="Last Name" style="width:180px;"></div>					 
	                          </div>
	                         </td>
	                         <td>
	                           <div class="text-left" style="padding-left:50px;"><input type="text" name="email" placeholder="Email" style="width:250px;"></div>
	                          </td>
	                        </tr>
	                        <tr>
	                         <td>
	                          <div class="name" style="display:inline-block;">
								 <div class="text-left" style="padding-left:30px;display:inline-block;"><input type="text" name="firstname" placeholder="First Name" style="width:180px;"></div>					 
								 <div class="text-left" style="padding-left:50px;display:inline-block;"><input type="text" name="lastname" placeholder="Last Name" style="width:180px;"></div>					 
	                          </div>
	                         </td>
	                         <td>
	                           <div class="text-left" style="padding-left:50px;"><input type="text" name="email" placeholder="Email" style="width:250px;"></div>
	                          </td>
	                        </tr> 
	                    </table>  
	               </div>
      	  	</div>  
      	</div>
      	<br>
      	<br>
      	<div class="text-center">
      	  <input type="submit" value="submit" style="width:100px;height:30px;"></input>
      	</div>
      </form>
      </section>
      </aside>
      </div>
      <jsp:include page="include/js_inc.jsp"></jsp:include>
    </body>
 </html>
 
