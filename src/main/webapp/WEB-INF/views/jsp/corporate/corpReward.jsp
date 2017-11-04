<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>

<html>
    <head>
        <meta charset="UTF-8">
        <title>AdminLTE | Customer</title>
        <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>

        <jsp:include page="include/files.jsp"></jsp:include>
		<link href="resources/core/css/jquery.steps.css" rel="stylesheet" type="text/css" /> 
	</head>
	<body>
		<div class="container">
			<div class="main" style="font-size:35px; font-weight:bold;">
            	<div class="text-center">
              		Loyalty points
           		 </div>
          </div><br><br>
          Enter Loyalty Points : <br><br>
          Loyalty Points &nbsp; <input type="text" name="loyalty" placeholder="&nbsp; Loyalty Points" style="width:180px;"><br><br><br><br>
          <div class="text-center">
          	<input type="submit" value="Reedem" style="width:100px;height:30px;"></input>
          </div>
		</div>
	</body>
</html>
