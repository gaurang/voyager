package com.cd.voyager.email;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class MailSenderPooled implements MailSender, Runnable {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	// -------------------------------------------------------------------------

	/**
	 * Java Mail sender
	 */
	
	@Autowired
	private JavaMailSenderImpl mailSender;

	private String adminemail;


	private static Map<String, String> templateMap = new HashMap<String, String>();

	
	public static String NEW_DRIVER_TPL = "newDriver.tpl";
	public static String FORGOT_PASS_TPL = "forgotpass.tpl";
	// -------------------------------------------------------------------------

	private final Pattern pattern = Pattern.compile("\\[(.+?)\\]");

	/**
	 * Mail sending thread
	 */
	private Thread thread;

	/**
	 * Mail queue
	 */
	private Queue queue = new Queue();

	/**
	 * Lock
	 * 
	 * @see #run()
	 * @see #add(Object)
	 */
	private Object mutex = new Object();

	// -------------------------------------------------------------------------

	/**
	 * Constructor
	 * 
	 */
	public MailSenderPooled() {
		thread = new Thread(this);
		thread.setDaemon(true);
		thread.start();
	}

	// -------------------------------------------------------------------------

	/**
	 * An iteration of mail sending
	 * 
	 * @see #add(Object)
	 * 
	 */
	public void run() {
		while (!isClose()) {
			if (!isEmpty()) {
				Object object = poll();
				try {
					// read send
					doSend(object);
					logger.info("Mail sent");
				} catch (Exception ex) {
					logger.error("Mails end Exception: " + ex.getMessage(), ex);
					ex.printStackTrace();
				}
			}
			// Wait till notified by 'ADD' method
			synchronized (mutex) {
				try {
					mutex.wait();
				} catch (InterruptedException e) {
					logger.error("InterruptedException : ", e);
					e.printStackTrace();
				}
			}
		}
	}

	// -------------------------------------------------------------------------

	public JavaMailSenderImpl getMailSender() {
		return mailSender;
	}

	/**
	 * Set the mail sender
	 * 
	 * @param mailSender
	 */
	public void setMailSender(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}

	// -------------------------------------------------------------------------

	/**
	 * Close the pool
	 * 
	 */
	public void close() {
		queue.clear();
		queue = null;
	}

	/**
	 * isClosed ?
	 */
	public boolean isClose() {
		return queue == null;
	}

	protected boolean isEmpty() {
		return queue != null && queue.isEmpty();
	}

	/**
	 * Add the mail into the queue
	 * 
	 * @param obj
	 * @see #run()
	 */
	protected void add(Object obj) {

		queue.add(obj);
		synchronized (mutex) {
			mutex.notify();
		}
	}

	protected Object poll() {

		return queue.poll();
	}

	/**
	 * 
	 */
	public void send(SimpleMailMessage simpleMessage) throws MailException {
		add(simpleMessage);
	}

	/**
	 * 
	 */
	public void send(SimpleMailMessage[] simpleMessages) throws MailException {

		SimpleMailMessage[] messages = simpleMessages;
		for (int i = 0; i < messages.length; i++) {
			SimpleMailMessage message = messages[i];
			add(message);
		}
	}

	/**
	 * 
	 * @param mimeMessage
	 * @throws MailException
	 */
	public void send(MimeMessage mimeMessage) throws MailException {
		add(mimeMessage);
	}

	/**
	 * 
	 * @param mimeMessages
	 * @throws MailException
	 */
	public void send(MimeMessage[] mimeMessages) throws MailException {

		MimeMessage[] messages = mimeMessages;
		for (int i = 0; i < messages.length; i++) {
			MimeMessage message = messages[i];
			add(message);
		}

	}

	/**
	 * 
	 * @param mimeMessagePreparator
	 * @throws MailException
	 */
	public void send(MimeMessagePreparator mimeMessagePreparator)
			throws MailException {
		add(mimeMessagePreparator);
	}

	/**
	 * 
	 * @param mimeMessagePreparators
	 * @throws MailException
	 */
	public void send(MimeMessagePreparator[] mimeMessagePreparators)
			throws MailException {

		MimeMessagePreparator[] preparators = mimeMessagePreparators;
		for (int i = 0; i < preparators.length; i++) {
			MimeMessagePreparator preparator = preparators[i];
			add(preparator);
		}
	}

	/**
	 * 
	 * @return
	 */
	public MimeMessage createMimeMessage() {
		return ((JavaMailSender) mailSender).createMimeMessage();
	}

	/**
	 * 
	 * @param contentStream
	 * @return
	 * @throws MailException
	 */
	public MimeMessage createMimeMessage(InputStream contentStream)
			throws MailException {
		return ((JavaMailSender) mailSender).createMimeMessage(contentStream);
	}

	/**
	 * Do the real send here
	 * 
	 * @param object
	 */
	public void doSend(Object object) throws MailException {
		if (object instanceof SimpleMailMessage) {
			doSend((SimpleMailMessage) object);
		} else if (object instanceof MimeMessage) {
			doSend((MimeMessage) object);
		} else if (object instanceof MimeMessagePreparator) {
			doSend((MimeMessagePreparator) object);
		}
	}

	/**
	 * 
	 * @param simpleMessage
	 * @throws MailException
	 */
	public void doSend(SimpleMailMessage simpleMessage) throws MailException {
		logger.info("Sending SimpleMailMessage mail to: " + simpleMessage.getTo() + ",cc: " + simpleMessage.getCc() + " subject: " + simpleMessage.getSubject());
		mailSender.send(simpleMessage);
	}

	/**
	 * 
	 * @param mimeMessage
	 * @throws MailException
	 */
	public void doSend(MimeMessage mimeMessage) throws MailException {
		try {
			logger.info("Sending MimeMessage mail to: " + mimeMessage.getRecipients(Message.RecipientType.TO) 
					+ ",cc: " + mimeMessage.getRecipients(Message.RecipientType.CC) 
					+ " subject: " + mimeMessage.getSubject());
		} catch (Exception e) {
			// TODO: handle exception
		}
//		((JavaMailSender) mailSender).send(mimeMessage);
		mailSender.send(mimeMessage);
	}

	/**
	 * 
	 * @param mimeMessagePreparator
	 * @throws MailException
	 */
	public void doSend(MimeMessagePreparator mimeMessagePreparator)
			throws MailException {
//		((JavaMailSender) mailSender).send(mimeMessagePreparator);
		mailSender.send(mimeMessagePreparator);
	}

	@Override
	public void sendEMail(String subject, String from, String to, String cc, String replyTo, String template, HashMap<String,String>  propertyMap) throws IOException, MessagingException, MailException {
		// TODO Auto-generated method stub
		try{
		Properties prop = new Properties();
		String input = null;
		
		
		Session session = Session.getDefaultInstance(mailSender.getJavaMailProperties());
		MimeMessage message = new MimeMessage(session);
		message.setSubject(subject);
		message.setFrom(new InternetAddress(from));
		message.setRecipient(RecipientType.TO, new InternetAddress(to));
		if(!StringUtils.isEmpty(cc))
			message.setRecipient(RecipientType.CC, new InternetAddress(cc));

		
		
		input = readFile(template, StandardCharsets.UTF_8);
		Iterator<Map.Entry<String, String>> iterator = propertyMap.entrySet().iterator() ;
/*        while(iterator.hasNext()){
            Map.Entry<String, String> variables = iterator.next();
            input.replaceAll(variables.getKey(), variables.getValue());
            System.out.println(variables.getKey() +" :: "+ variables.getValue());
            //You can remove elements while iterating.
            //iterator.remove();
        }
*/
		Matcher matcher = pattern.matcher(input);
		StringBuilder builder = new StringBuilder();
		int i = 0;
		while (matcher.find()) {
		    String replacement = propertyMap.get(matcher.group(1));
		    builder.append(input.substring(i, matcher.start()));
		    if (replacement == null)
		        builder.append(matcher.group(0));
		    else
		        builder.append(replacement);
		    i = matcher.end();
		}
		builder.append(input.substring(i, input.length()));
		input = builder.toString();
		
		//input.replaceAll("{{fname}}", replacement)
			
		message.setContent(input, "text/html");
		this.send(message);
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
		public  String readFile(String path, Charset encoding) 
			  throws IOException 
			{
			    byte[] encoded;
			    System.out.println("&&&&&&&&&&&"+System.getProperty("user.dir"));
				//encoded = Files.readAllBytes(Paths.get("template/"+path));
				encoded = IOUtils.toByteArray(getClass().getClassLoader().getResourceAsStream("template/"+path));
			  return new String(encoded, encoding);
			}

	/**/
	//@Override
	//public Integer sendMail(String subject, String from, String to, String cc, String replyTo, String template) {
		// TODO Auto-generated method stub
		//return null;
	//}

		

	
	
}
