<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Support</title>
        <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
		<link rel="stylesheet" href="resources/core/css/emp.css"  type="text/css">
       <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">
       <jsp:include page="include/files.jsp"></jsp:include>
 
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
                        Update Support Table
                        
                    </h1>
                    
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                        <li class="active">CSE</li><li class="active">CSE</li>
                    </ol>
					<p>&nbsp;</p>
	
					
      <div class="container"> 
         
	    <form id="support" action="nextSupport" method="POST">
      	<div class="row">
      	   <div class="col-sm-12">
      	      	  <div class="table-responsive">
	                    <!-- .table - Uses sparkline charts-->
	                     <table class="table table-striped" id="suppoprtTable" style = "padding-right:30px;">
	                        <tr>
	                         <th>
	                              <div class="text-left" style="padding-left:30px;">Support ID</div>
	                          </th>
	                          <th>
	                              <div class="text-left" style="padding-left:30px;">Support Type</div>
	                          </th>
	                          <th>
			                      <div class="text-left" style="padding-left:50px;">Support Question</div>
	                          </th>
	                          <th><div class="text-left" style="padding-left:30px;">Description</div>
	                          </th>
	                          
	                        </tr> 
	                        <c:forEach items="${suppList}" var="l" varStatus="index">
	                        <tr>
	                        <td>
								 <div class="text-left" style="padding-left:30px;"><input type="text" name="supportId" value="${l.supportId}" style="width:120px;" readonly></div>				 
							</td>
	                         <td>
								 <div class="text-left" style="padding-left:30px;"><input type="text" name="supportType" value="${l.supportType}" style="width:120px;"></div>				 
							</td>
							<td>
								 <div class="text-left" style="padding-left:50px;;"><input type="text" name="supportQuestion" value="${l.supportQuestion}" style="width:170px;"></div>					 
	                         </td>
	                         <td>
	                           <div class="text-left" style="padding-left:30px;"><input type="text" name="description" value="${l.description}" style="width:100px;"></div>
	                          </td>
	                          
	                        </tr> 
	                        </c:forEach>
	                    </table>  
	               </div>
      	  	</div>  
      	</div>
      	<br>
      	<br>
      <div>
      	<div class="text-center">
      	  <input type="submit" value="submit"  style="width:100px;height:30px;"></input>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      	 <!--  <button type="reset" value="Reset" style="width:100px;height:30px;">Reset</button>-->
      	</div>
      </div>
      </form>
      </div>
      
      <jsp:include page="include/js_inc.jsp"></jsp:include>
      </section>
      </aside>
      </div>
    </body>
 </html>