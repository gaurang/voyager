<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>AdminLTE | Dashboard</title>
        <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>

       <jsp:include page="include/files.jsp"></jsp:include>
 	</head>
    <body>
        	 <h3 class="box-title">
		        	    <div class="text-center">
		                    Invoice
		                </div>
             </h3><br><br>
              					<div class="box-body no-padding" align="center">
                                    <!--<div id="world-map" style="height: 300px;"></div>-->
                                      <div class="table-responsive  col-md-12" >
	                                        <!-- .table - Uses sparkline charts-->
											<div class="row">
												<label class="col-md-3">Booking Id</label>  
												<div class="col-md-3">${booking.bookingId}</div>
											</div>	

											<div class="row">
												<label class="col-md-3">customer Name</label>  
												<div class="col-md-3">${booking.customerName}</div>
												
												<label class="col-md-3">Driver Name</label>  
												<div class="col-md-3">${booking.driverName}</div>
												
											</div>	
											<div class="row">
												<label class="col-md-3">Ride Fare</label>  
												<div class="col-md-3">${booking.rideTotalAmt}  ${booking.currency == null? 'AUD' :booking.currency}</div>
												
												<label class="col-md-3">Booking Fee</label>  
												<div class="col-md-3">${booking.bookingFee}  ${booking.currency == null? 'AUD' :booking.currency}</div>
												
											</div>	
											<div class="row">
												<label class="col-md-3">Vehicle Details</label>  
												<div class="col-md-3">${booking.make} ${booking.model} ${booking.carNumber}</div>
											</div>	
											</br>
											</br>
											
											<table class="table col-md-12">
												<tr>
													<th>Source</th>
													<th>Destination</th>
													<th>Total KM</th>
													<th>Waiting Fair</th>
													<th>Base Fare</th>
													<th>Toll Tax</th>
												</tr>
												<tr>
													<td>${booking.srcPlace}</td>
													<td>${booking.destPlace}</td>
													<td>${booking.kmTraveled}</td>
													<td>${booking.waitCharges}</td>
													<td>${booking.baseFare}</td>
													<td>${booking.tollCharges}  ${booking.currency == null? 'AUD' :booking.currency}</td>
												</tr>
											</table>


                                        </div>
                                </div><!-- /.box-body-->
                                <br><br>
                                <!-- <div class="box-footer">
                                    <button class="btn btn-info"><i class="fa fa-download"></i> Generate PDF</button>
                                    <button class="btn btn-warning"><i class="fa fa-bug"></i> Report Bug</button>
                                </div> -->
       <jsp:include page="include/js_inc.jsp"></jsp:include>


    </body>
</html>