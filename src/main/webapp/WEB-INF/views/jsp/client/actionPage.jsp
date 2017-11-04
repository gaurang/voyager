<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>

<html>
    <head>
        <meta charset="UTF-8">
        <title>AdminLTE | Feeback</title>
        <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>

        <jsp:include page="../include/files.jsp"></jsp:include>
		<link href="resources/core/css/jquery.steps.css" rel="stylesheet" type="text/css" /> 
		</head>
    <body class="skin-black">
        <!-- header logo: style can be found in header.less -->
        <%-- <jsp:include page="include/header.jsp"></jsp:include>
         --%>
        
        <div class="wrapper row-offcanvas row-offcanvas-left" >
            <!-- Left side column. contains the logo and sidebar -->
			<div align="center" style="width: 100%; background-color: black;" >
				<img src="resources/core/img/Web-banner.jpg" width="100%"  align="middle" >
			</div>

            <!-- Right side column. Contains the navbar and content of the page -->
            <aside class="" style="margin: 0 auto; width:95%;">
                <!-- Content Header (Page header) -->
                <section class="content-header">
                    <h1>
							${actionHeader}						
                    </h1>
                    
                    <p>
                    	${actionMessage}
                    </p>
					
                       </section>

             </aside><!-- /.right-side -->
        </div><!-- ./wrapper -->

        <!-- add new calendar event modal -->

       <jsp:include page="../include/js_inc.jsp"></jsp:include>

       <!-- <script src="resources/core/js/jquery.steps.min.js" type="text/javascript"></script>        
		 -->
		<script>
 //           $("#wizard").steps();
        </script>
    </body>
</html>