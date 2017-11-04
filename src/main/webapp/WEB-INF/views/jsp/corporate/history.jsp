<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>



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
                                        Trip Status
                                      </div>
                                    </h3><br><br>
                                <div class="box-body no-padding">
                                    <!--<div id="world-map" style="height: 300px;"></div>-->
                                <form action="history">
                                  <div class="s3" style='display:inline-block'>
								       Month<select name="Month">
								        <option value="01">Jan</option>
								        <option value="02">Feb</option>
								        <option value="03">Mar</option>
								        <option value="04">Apr</option>
								        <option value="05">May</option>
								        <option value="06">Jun</option>
								        <option value="07">Jul</option>
								        <option value="08">Aug</option>
								        <option value="09">Sep</option>
								        <option value="10">Oct</option>
								        <option value="11">Nov</option>
								        <option value="12">Dec</option>
								      </select> &nbsp; 
								       Year<select name="Year">
								        <option value="2016">2016</option>
								        <option value="2017">2017</option>
								        <option value="2018">2018</option>
								        <option value="2019">2019</option>
								       </select> &nbsp;
								       Week<select name="Week">
								        <option value="1">Week1</option>
								        <option value="2">Week2</option>
								        <option value="3">Week3</option>
								        <option value="4">Week4</option>
								        <option value="0">All</option>
								        </select> &nbsp;
								        <button class="button" type="Submit">Submit</button>
								      </div>
								      </form>
								 </div><br>
								<div class="table-responsive">
	                                        <!-- .table - Uses sparkline charts-->
	                                        <table class="table table-striped" height="350">
	                                            <tr>
	                                                <th>EmpName</th>
	                                                <th>EmpId</th>
	                                                <th>Trip Fare</th>
	                                                <th>Trip Fee</th>
	                                                <th>History</th>
	                                            </tr>
				            					<c:if test="${fn:length(bookingList)> 0}">
						                    	<c:forEach items="${bookingList}" var="l" varStatus="index" step="1" begin="1" end="10">
													<tr>
														<td>${l.customerName} </td>
														<td><div id="sparkline-1">${l.empId}</div></td>
														<td>${l.bookingFee}</td>
														<td>${l.rideTotalAmt}</td>
														<td><a href="invoice?bookingId=${l.bookingId}"><i class="fa fa-book"></i></a>
														&nbsp;&nbsp;</td>
													</tr>
						                    	</c:forEach>
						                    	</c:if>
						                    	<c:if test="${fn:length(bookingList)== 0}">
						                    	<tr>
															<td colspan=5>No data</td>
												</tr>
							                    </c:if>
                                           </div><!-- /.box-body-->
                                <br><br>
                                <div class="box-footer">
                                    <button class="btn btn-info"><i class="fa fa-download"></i> Generate PDF</button>&nbsp; &nbsp; &nbsp; &nbsp;
                                    <button class="btn btn-warning"><i class="fa fa-bug"></i> Report Bug</button>
                                </div>		
	</body>
</html>