<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="en">
<head>
<title>U-Conect</title>

<spring:url value="${pageContext.request.contextPath}/../resources/core/css/hello.css" var="coreCss" />
<spring:url value="${pageContext.request.contextPath}/../resources/core/css/bootstrap.min.css" var="bootstrapCss" />
<link href="${bootstrapCss}" rel="stylesheet" />
<link href="${coreCss}" rel="stylesheet" />

<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/core/css/font-awesome.css" type="text/css" />

<!-- Google Fonts -->
<link href='http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700,800' rel='stylesheet' type='text/css'>

<style type="text/css">
.form{
	margin-bottom: 10px;
}

</style>
</head>
<!-- 
<nav class="navbar navbar-inverse navbar-fixed-top">
	<div class="container">
		<div class="navbar-header">
			<a class="navbar-brand" href="#">
			</a>
		</div>
	</div>
</nav>
 -->
<div class="jumbotron">
	<div class="container" align="center" style="width: 100%;">
	
	<div align="center" style="width: 100%; background-color: black;" >
		<img src="${pageContext.request.contextPath}/resources/core/img/voyagerLogo.png" style="" width="422" height="222" align="middle" >
	</div>
		<h1>Login</h1>
		
		<div class="log-in">
	    <div class="top-sec-log">
	    
			<span class="user-log-in">
			
				
			</span><!-- User Image -->
			
				
		</div>
 
	<h3><?=$error?></h3>
			<form action="login" method="post">
			
           <c:if test="${param.error != null}">
                        <div class="alert alert-danger">
                            <p>Invalid username and password.</p>
                        </div>
                    </c:if>
                    <c:if test="${param.logout != null}">
                        <div class="alert alert-success">
                            <p>You have been logged out successfully.</p>
                        </div>
            </c:if>
			
			<div class="form">
				<span><i class="icon-user"></i></span>
				<input type="text" onblur="if(this.value=='')this.value=this.defaultValue;" onfocus="if(this.value==this.defaultValue)this.value='';"  name="username" value="User name" />
			</div><!-- Login User Name -->
			
			<div class="form">
				<span><i class="icon-key"></i></span><input type="password" onblur="if(this.value=='')this.value=this.defaultValue;" onfocus="" name="password" value="Password"  placeholder="Password"/>
			</div><!-- Login Password -->
			
			<a class="btns  red  sml-btn" id="submit"  href="forgotPassword" style="margin-right: 20px;">Forgot Password ?</a>
			<input type="submit" class="btns  red  sml-btn" style="margin-left:0px;margin-bottom:10px;" value="Log in">

			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/> 
			
			</form>
			
	
	
		</div><!-- Log in Page -->

		
	</div>
</div>


	<hr>
	<footer>
		<p align="center">&copy; voyager 2015, </p>
	</footer>

<spring:url value="${pageContext.request.contextPath}/../resources/core/js/hello.js" var="coreJs" />
<spring:url value="${pageContext.request.contextPath}/../resources/core/js/bootstrap.min.js" var="bootstrapJs" />

<script src="${coreJs}"></script>
<script src="${bootstrapJs}"></script>
<jsp:include page="include/js_inc.jsp"></jsp:include>

</body>
</html>