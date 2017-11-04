<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>

<html>
    <head>
        <meta charset="UTF-8">
        <title> Payment Details</title>
        <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>		
		  <meta name="viewport" content="width=device-width, initial-scale=1">
		  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" />
		  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
		  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
		  
							
        <jsp:include page="include/files.jsp"></jsp:include>
<!-- 		<link href="resources/core/css/jquery.steps.css" rel="stylesheet" type="text/css" /> 
 -->		</head>
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
    
			<div class="container">
			<div class="page-header"> Modes of Payment
			</div>		<div class="col-md-3">
			<div class="list-group">
			<a href="#" class="list-group-item">PayWay </a>
			</div>
			</div>
			<div class="col-md-12">
			<form:form role="form"  name="payment" commandName="payment" id="payment" action="savePayment" method="post">
			<div class="box-body">
				<div class="form-group row">
					<label for="cardno" class="col-xs-3">Card Type</label>
					<div class="col-xs-3">
					
					<form:select  id="cardType" class="form-control required col-xs-3" path="cardType">
						<option value="VISA">VISA</option>
						<option value="VISA">MASTER</option>
						<option value="OTHERS">OTHERS</option>
					</form:select>
					</div>
				</div>
			
				<div class="form-group row">
					<label for="cardno" class="col-xs-3">Card Number</label>
					<div class="col-xs-4">
						<form:input  id="cardNo" placeholder="Enter your Card Number"  maxlength="16" class="form-control required col-xs-3 number" path="cardNo"/>
					</div>
				</div>
				<div class="form-group row">
				<label for="expiry" class="col-xs-3">Expiration Date</label>
					<div class="col-xs-6">

					<input  id="expiryMM" name="expiryMM" placeholder="MM" class="required number col-md-2" maxlength="2">
					<input id="expiryYY" name="expiryYY" placeholder="YY" class="required number col-md-2"  maxlength="2">
					</div>
					<br>
				</div>
				<div class="form-group row">
					<label for="cardname" class="col-xs-3"> Card Holder's Name</label>
					<div class="col-xs-4">
						<form:input id="cardname" placeholder="Enter Card Holder's Name" class="form-control required col-xs-3" path="cardName"/>
					</div>
				</div>
				<div class="form-group row">
					<label for="cvv" class="col-xs-3"> CVV</label>
					<div class="col-xs-3">
						<form:input path="cvv" id="cvv" placeholder="CVV" class="form-control required col-xs-3" onchange="validate_cvv(this.value);"/>
					</div>
				</div>
				<br>
				<form:hidden path="gatewayId" value="PayWay"/>

	            <div class="row form-group">
	                    <div class="col-xs-3">
	                    	<input  type="submit" class="btn btn-primary" value="Save"/>
	                    </div>	
	            </div>
  
			</div>
			</form:form>
			</div>
		</div>
		</div>
		
		</aside>
		</body>
		      <jsp:include page="include/js_inc.jsp"></jsp:include>
			
      		<script> 
		
		 $(function() {
  
  		  // Setup form validation on the #register-form element
    	 $("#payment").validate({
    		  rules: {
    			  	  cardno: {
	    			      required: true,
	    			      creditcard: true
    			    	}
    			     },
              submitHandler: function(form) {
            	if(!validate_cvv($('#cvv').val())){
            	  	return false;
            	}
            		
            	form.submit();
		        }
		    });
		
		  });
  
		 $.validator.addMethod('CCExp', function(value, element, params) {
		      var minMonth = new Date().getMonth() + 1;
		      var minYear = new Date().getFullYear();
		      var month = parseInt($(params.month).val(), 10);
		      var year = parseInt($(params.year).val(), 10);
		      return (year > minYear || (year === minYear && month >= minMonth));
		}, 'Your Credit Card Expiration date is invalid.');

		 function validate_cvv(cvv){

	         var myRe = /^[0-9]{3,4}$/;
	         var myArray = myRe.exec(cvv);
	         if(cvv!=myArray)
	          {
	            alert("Invalid cvv number"); //invalid cvv number
	            return false;
	         }else{
	             return true;  //valid cvv number
	            }

	         }

  		</script>
  
		
	</html>