<html>
    <head>
        <meta charset="UTF-8">
        <title>Driver | Invoice</title>
        <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>
		<script type="text/javascript" src="countryStateCity.js"></script>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
        <jsp:include page="include/files.jsp"></jsp:include>
        <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
  		<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
		<link href="resources/core/css/jquery.steps.css" rel="stylesheet" type="text/css" /> 
	</head>
	<body>
	  <section id="headuc">
		<div class="row" style="background-color:black;color:white;text-align:center;font-weight:bold;padding-bottom:10px;">
		 <div class="container">
		  <h1>voyager</h1>
		</div>
	   </div>
	  </section>
	  <section id="main">
	    <div class="row">
	      <div class="col-xs-4" style="text-align:left;padding-top:60px;padding-left:80px;">
	      	<div class="container">
	      	  <img src="[link]" alt="map" width="450px" height="350px" >
	      	</div>
	      </div>
	      <div class="col-xs-4" style="text-align:center;">
	        <hr size="10">
	        <h3>FARE BREAKDOWN</h3>
	        <hr size="10">
	            <tr>
	                <td>
	                    <div class="row form-group">
							<div class="col-xs-4" style="text-align:left; padding-left:90px;">
								<p style="font-weight:bold;font-size:18px;display:inline;text-align:left;">Base Fare</p>
							</div>
							<div class="col-xs-4">
								<p style="font-weight:bold;font-size:16px;display:inline-block;">[baseFare]</p>
							</div>
						</div>
					</td>
				</tr>
				<br>
				 <tr>
	                <td>
	                    <div class="row form-group" >
							<div class="col-xs-4" style="text-align:left; padding-left:90px;">
								<p style="font-weight:bold;font-size:18px;display:inline;text-align:left;">Distance</p>
							</div>
							<div class="col-xs-4">
								<p style="font-weight:bold;font-size:16px;display:inline-block;">[distanceFare]</p>
							</div>
						</div>
					</td>
				</tr>
				<br>
				 <tr>
	                <td>
	                    <div class="row form-group">
							<div class="col-xs-4" style="text-align:left; padding-left:90px;">
								<p style="font-weight:bold;font-size:18px;display:inline;text-align:left;">Time</p>
							</div>
							<div class="col-xs-4">
								<p style="font-weight:bold;font-size:16px;display:inline-block;">[timeFare]</p>
							</div>
						</div>
					</td>
				</tr>
	        <hr size="10%">
	          <tr>
	                <td>
	                    <div class="row form-group">
							<div class="col-xs-4" style="text-align:left; padding-left:90px;">
								<p style="font-weight:bold;font-size:18px;display:inline;text-align:left;">Toll Charges</p>
							</div>
							<div class="col-xs-4">
								<p style="font-weight:bold;font-size:16px;display:inline-block;">[tollCharges]</p>
							</div>
						</div>
					</td>
				</tr>
	        <hr size="10">
	            <tr>
	                <td>
	                    <div class="row form-group">
							<div class="col-xs-4" style="text-align:left; padding-left:90px;">
								<p style="font-weight:bold;font-size:18px;display:inline;text-align:left;">Total</p>
							</div>
							<div class="col-xs-4">
								<p style="font-weight:bold;font-size:16px;display:inline-block;">[totalFare]</p>
							</div>
						</div>
					</td>
				</tr>
	        <hr size="10">
	      </div>
	    </div>
	  </section>
	</body>
</html>
