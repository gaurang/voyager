<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Attribute</title>
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
                        Update Attribute
                        
                    </h1>
                    
                    <ol class="breadcrumb">
                        <li><a href="#"><i class="fa fa-dashboard"></i> Home</a></li>
                        <li class="active">CSE</li><li class="active">CSE</li>
                    </ol>
					<p>&nbsp;</p>
	<div style="width:100%;overflow:auto">
					
      <div class="container"> 
         
          <c:choose>
    		<c:when test="${param.attrName != null}">
        		Value Updated successfully. 
        		<br />
    		</c:when>
		</c:choose>
         
	    <form id="attrForm" action="attributeForm1" method="POST">
      	<div class="row">
      	   <div class="col-sm-12">
      	      	  <div class="table-responsive">
	                    <!-- .table - Uses sparkline charts-->
	                     <table class="table table-striped" id="attributeTable" style = "padding-right:30px;">
	                        <tr>
	                          <th>
	                              <div class="text-left" style="padding-left:20px;">Attribute</div>
	                          </th>
	                          <th>
			                      <div class="text-left" style="padding-left:20px;">Attribute Name</div>
	                          </th>
	                          <th><div class="text-left" style="padding-left:20px;">Attribute Value 1</div>
	                          </th>
	                          <th>
			                      <div class="text-left" style="padding-left:20px;">Attribute Value 2</div>
	                          </th>
	                          <th>
			                      <div class="text-left" style="padding-left:20px;">Attribute Type</div>
	                          </th>
	                          <th>
			                      <div class="text-left" style="padding-left:20px;">Level</div>
	                          </th>
	                          <th>
			                      <div class="text-left" style="padding-left:20px;">Flag</div>
	                          </th>
	                          <th>
			                      <div class="text-left" style="padding-left:20px;">Unit</div>
	                          </th>
	                          <th>
			                      <div class="text-left" style="padding-left:20px;">Zone Id</div>
	                          </th>
	                          <th>
			                      <div class="text-left" style="padding-left:20px;">Sort</div>
	                          </th>
	                        </tr> 
	                        <c:forEach items="${attrList}" var="l" varStatus="index">
	                        <tr>
	                         <td>
								 <div class="text-left" style="padding-left:20px;">${l.masterAttr}</div>					 
							</td>
							<td>
								 <div class="text-left" style="padding-left:20px;;"><input type="text" name="attrName" value="${l.attrName}" style="width:100px;" readOnly></div>					 
	                         </td>
	                         <td>
	                           <div class="text-left" style="padding-left:20px;"><input type="text" name="attrValue1" value="${l.attrValue1}" style="width:100px;" ></div>
	                          </td>
	                          <td>
								 <div class="text-left" style="padding-left:20px;"><input type="text" name="attrValue2" value="${l.attrValue2}" style="width:100px;" ></div>					 
							</td>
							<td>
								 <div class="text-left" style="padding-left:20px;">${l.type}</div>					 
							</td>
							<td>
								 <div class="text-left" style="padding-left:20px;">${l.level}</div>					 
							</td>
							<td>
								 <div class="text-left" style="padding-left:20px;">${l.flag}</div>					 
							</td>
							<td>
								 <div class="text-left" style="padding-left:20px;"><input type="text" name="unit" value="${l.unit}" style="width:100px;"></div>					 
							</td>
							<td>
								 <div class="text-left" style="padding-left:20px;">${l.zoneId}</div>					 
							</td>
							<td>
								 <div class="text-left" style="padding-left:20px;">${l.sort}</div>					 
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
      	  <input type="submit" value="submit" style="width:100px;height:30px;"></input>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      	 <!--  <button type="reset" value="Reset" style="width:100px;height:30px;">Reset</button>-->
      	</div>
      </div>
      </form>
      </div>
      </div>
      <jsp:include page="include/js_inc.jsp"></jsp:include>
      </section>
      </aside>
      </div>
    </body>
 </html>