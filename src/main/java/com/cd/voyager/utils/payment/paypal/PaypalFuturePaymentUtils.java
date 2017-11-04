package com.cd.voyager.utils.payment.paypal;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.paypal.api.openidconnect.CreateFromAuthorizationCodeParameters;
import com.paypal.api.openidconnect.Tokeninfo;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.FuturePayment;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.Transaction;
import com.paypal.base.ClientCredentials;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

public class PaypalFuturePaymentUtils {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	
	public Payment create(String correlationId, String authorizationCode, BigDecimal totAmount, String currency, String desc) throws PayPalRESTException, FileNotFoundException, IOException {
		
		Payer payer = new Payer();
		payer.setPaymentMethod("paypal");
		
		Amount amount = new Amount();
		
		amount.setTotal(totAmount.toPlainString());
		amount.setCurrency(currency);
		
		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction.setDescription(desc);
		
		List<Transaction> transactions = new ArrayList<Transaction>();
		transactions.add(transaction);
		
		FuturePayment futurePayment = new FuturePayment();
		futurePayment.setIntent("authorize");
		futurePayment.setPayer(payer);
		futurePayment.setTransactions(transactions);
		
		Tokeninfo tokeninfo = null;
		if (authorizationCode != null && authorizationCode.trim().length() > 0) {
			logger.info("creating future payment with auth code: " + authorizationCode);
			
			ClientCredentials credentials = futurePayment.getClientCredential();
			CreateFromAuthorizationCodeParameters params = new CreateFromAuthorizationCodeParameters();
			params.setClientID(credentials.getClientID());
			params.setClientSecret(credentials.getClientSecret());
			params.setCode(authorizationCode);

			Map<String, String> configurationMap = new HashMap<String, String>();
			configurationMap.put("mode", "sandbox");
			APIContext apiContext = new APIContext();
			apiContext.setConfigurationMap(configurationMap);
			tokeninfo = Tokeninfo.createFromAuthorizationCodeForFpp(apiContext, params);
			tokeninfo.setAccessToken(tokeninfo.getTokenType() + " " + tokeninfo.getAccessToken());

			System.out.println("Generated access token from auth code: " + tokeninfo.getAccessToken());
		}

		Payment createdPayment = futurePayment.create(tokeninfo.getAccessToken(), correlationId);
		if (createdPayment.getIntent().equals("authorize")) {
			System.out.println("payment authorized");
			System.out.println("Payment ID=" + createdPayment.getId());
			System.out.println("Authorization ID=" + createdPayment.getTransactions().get(0).getRelatedResources().get(0).getAuthorization().getId());
		}
		
		return createdPayment;
	}
	
	/*public static void main(String[] args) {
		try {
			
			String authorizationCode = "EBYhRW3ncivudQn8UopLp4A28xIlqPDpAoqd7biDLpeGCPvORHjP1Fh4CbFPgKMGCHejdDwe9w1uDWnjPCp1lkaFBjVmjvjpFtnr6z1YeBbmfZYqa9faQT_71dmgZhMIFVkbi4yO7hk0LBHXt_wtdsw";
			String correlationId = "111";
			for (int i = 0; i < args.length; ++i) {
				if (args[i].startsWith("authorization_code")) {
					authorizationCode = args[i].split("=")[1];
				}
				else if (args[i].startsWith("correlationId")) {
					correlationId = args[i].split("=")[1];
				}
			}
			PaypalFuturePaymentUtils fps = new PaypalFuturePaymentUtils();
			Payment payment = fps.create(correlationId, authorizationCode,new BigDecimal("9"),"USD", "TEST");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(Payment.getLastRequest());
			System.out.println(Payment.getLastResponse());
		}
	}
*/
	public Integer capture(String correlationId, String authorizationCode, BigDecimal totAmount, String currency, String desc) throws PayPalRESTException, FileNotFoundException, IOException {

			
			Payer payer = new Payer();
			payer.setPaymentMethod("paypal");
			
			Amount amount = new Amount();
			
			amount.setTotal(totAmount.toPlainString());
			amount.setCurrency(currency);
			
			Transaction transaction = new Transaction();
			transaction.setAmount(amount);
			transaction.setDescription(desc);
			
			List<Transaction> transactions = new ArrayList<Transaction>();
			transactions.add(transaction);
			
			return 1;
	}
	
	
	/*private void executePayment(String correlationId, String authorizationCode, BigDecimal totAmount, String currency, String desc )  {
		
		Invoice 
		String orderId = (String) request.getParameter("orderId");

		// Construct a payment for complete payment execution
		Payment payment = new Payment();
		payment.setId(userPaymentDetails.getPaymentId());
		PaymentExecution paymentExecute = new PaymentExecution();
		paymentExecute.setPayerId(payerId);
		try {

			// set access token
			String accessToken = AccessTokenGenerator.getAccessToken();
			String requestId = UUID.randomUUID().toString();
			APIContext apiContext = new APIContext(accessToken, requestId);
			payment = payment.execute(apiContext, paymentExecute);
		} catch (PayPalRESTException pex) {
			if (pex.getMessage().contains(AppConstants.VALIDATION_ERROR)) {
				WebHelper.formErrorMessage(request, pex);
				WebHelper.forward(request, response, AppConstants.SHOW_PROFILE);
				return;
			} else {
				LoggingManager.debug(PaymentServlet.class, pex.getMessage());
				throw new ServletException(pex);
			}
		}
	}
*/
	
	
	public static final String clientID = "AQBIq8BsPyuv9nmydXKr4CHt31ur3xc2b2HSetueaF3httXfoXNY1E_RcO5GTgNeJ8bl2uTgcdycOIt_";
	public static final String clientSecret = "EDy-B1j0PdXESx4oSgS6H-unhX_Pxdc6A2X5CblVSIGdJdtH1tlUn5hrBjOPvud6STAwLCtLGKTwd9iP";

	
	public String getRefreshTokenFromAuthCode(String authorizationCode, boolean isTest) throws Exception{
		
		APIContext context = new APIContext(clientID, clientSecret, "sandbox");
		if(isTest){
			context = new APIContext(clientID, clientSecret, "sandbox");
		}else{
			context = new APIContext(clientID, clientSecret, "live");
		}
		
		String refreshToken;
		try {
			refreshToken = FuturePayment.fetchRefreshToken(context, authorizationCode);
		} catch (PayPalRESTException e) {
			logger.error("Cannot create Paypal Token use other Payment option");
			throw new Exception("PAYPAL Payment failed");
			//			e.printStackTrace();
		}

		return refreshToken;
	}
	
	public String getCreatePayment(String refreshToken, boolean isTest) throws Exception{
		String correlationId = "123456123";
		
		APIContext context = new APIContext(clientID, clientSecret, "sandbox");
		if(isTest){
			context = new APIContext(clientID, clientSecret, "sandbox");
		}else{
			context = new APIContext(clientID, clientSecret, "live");
		}
		
		
		// Create Payment Object
		Payer payer = new Payer();
		payer.setPaymentMethod("paypal");
		Amount amount = new Amount();
		amount.setTotal("0.17");
		amount.setCurrency("USD");
		Transaction transaction = new Transaction();
		transaction.setAmount(amount);
		transaction.setDescription("This is the payment tranasction description.");
		List<Transaction> transactions = new ArrayList<Transaction>();
		transactions.add(transaction);

		FuturePayment futurePayment = new FuturePayment();
		futurePayment.setIntent("authorize");
		futurePayment.setPayer(payer);
		futurePayment.setTransactions(transactions);

		Payment createdPayment = futurePayment.create(context, correlationId);
		PaymentExecution paymentExecute = new PaymentExecution();
		//paymentExecute.setPayerId(payer.getPayerInfo().getPayerId());
		createdPayment.execute(context, paymentExecute);
		System.out.println(createdPayment.toString());
		
		return "";
	}	
	public static void main(String[] args) {
		try {
			
			
			String email ="gaurangjsheth-buyer-1@gmail.com";
			String pass  ="12345678";
			
			
			// Authorization Code and Co-relationID retrieved from Mobile SDK.
			String authorizationCode = "C101.Rya9US0s60jg-hOTMNFRTjDfbePYv3W_YjDJ49BVI6YJY80HvjL1C6apK8h3IIas.ZWOGll_Ju62T9SXRSRFHZVwZESK";
			String correlationId = "123456123";
			
			APIContext context = new APIContext(clientID, clientSecret, "sandbox");

			// Fetch the long lived refresh token from authorization code.
			String refreshToken = FuturePayment.fetchRefreshToken(context, authorizationCode);
			
			// Store the refresh token in long term storage for future use.

			// Set the refresh token to context to make future payments of
			// pre-consented customer.
			context.setRefreshToken(refreshToken);
			
			// Create Payment Object
			Payer payer = new Payer();
			payer.setPaymentMethod("paypal");
			Amount amount = new Amount();
			amount.setTotal("0.17");
			amount.setCurrency("USD");
			Transaction transaction = new Transaction();
			transaction.setAmount(amount);
			transaction.setDescription("This is the payment tranasction description.");
			List<Transaction> transactions = new ArrayList<Transaction>();
			transactions.add(transaction);

			FuturePayment futurePayment = new FuturePayment();
			futurePayment.setIntent("authorize");
			futurePayment.setPayer(payer);
			futurePayment.setTransactions(transactions);

			Payment createdPayment = futurePayment.create(context, correlationId);
			PaymentExecution paymentExecute = new PaymentExecution();
			//paymentExecute.setPayerId(payer.getPayerInfo().getPayerId());
			createdPayment.execute(context, paymentExecute);
			System.out.println(createdPayment.toString());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(Payment.getLastRequest());
			System.out.println(Payment.getLastResponse());
		}
	}
}