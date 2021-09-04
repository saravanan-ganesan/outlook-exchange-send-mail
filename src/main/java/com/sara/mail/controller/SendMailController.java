package com.sara.mail.controller;

import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sara.mail.config.MailConfig;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.EmailAddress;
import microsoft.exchange.webservices.data.property.complex.MessageBody;

@RestController
public class SendMailController {
	
	@Autowired
	private MailConfig mailConfig;
	
	@GetMapping("/sendMail")
	public String sendMail() throws Exception {
		
		StringBuilder response = new StringBuilder();
		
		try {
			
			ExchangeService service = new ExchangeService();
			
			ExchangeCredentials credentials = new WebCredentials(mailConfig.getLoginEmailId(), 
					new String(Base64.getDecoder().decode(mailConfig.getLoginEmailPassword())));
			service.setCredentials(credentials);
			service.setUrl(new URI(mailConfig.getExchangeServiceUrl()));
			service.setTraceEnabled(true);
			
			EmailMessage mail = new EmailMessage(service);
			
			//from email id
			EmailAddress fromEmailAddress = new EmailAddress(mailConfig.getLoginEmailId());
			mail.setFrom(fromEmailAddress);
			
			//to email id
			if(!ObjectUtils.isEmpty(mailConfig.getEmailToAddr())) {
				
				List<EmailAddress> toEmailAddressList = new ArrayList<>();
				
				Arrays.asList(mailConfig.getEmailToAddr()).forEach(toAddr -> {
					toEmailAddressList.add(new EmailAddress(toAddr));
				});
				mail.getToRecipients().addEmailRange(toEmailAddressList.iterator());
			}
			
			//cc email id
			if(!ObjectUtils.isEmpty(mailConfig.getEmailCcAddr())) {
				
				List<EmailAddress> ccEmailAddressList = new ArrayList<>();
				
				Arrays.asList(mailConfig.getEmailCcAddr()).forEach(ccAddr -> {
					ccEmailAddressList.add(new EmailAddress(ccAddr));
				});
				mail.getCcRecipients().addEmailRange(ccEmailAddressList.iterator());
			}
	        
	        //email subject
	        mail.setSubject(mailConfig.getEmailSubject());
	        
	        //email body
	        mail.setBody(new MessageBody("This is test mail using exchange java mail api"));
	        
	        //email attachment
	        if(!ObjectUtils.isEmpty(mailConfig.getEmailAttachmentFilePath())) {
	        	
	        	Arrays.asList(mailConfig.getEmailAttachmentFilePath()).forEach(path -> {
	        		
	        		try {
						
						File file = new File(path);
		        		byte[] fileContent = Files.readAllBytes(file.toPath());
		        		mail.getAttachments().addFileAttachment(file.getName(), fileContent);
		        		
					} catch (Exception e) {
						response.append(e.getMessage());
						e.printStackTrace();
					}
	        		
	        	});
	        }
	        
	        //send mail
	        if(mailConfig.isSaveCopy()) {
	        	mail.sendAndSaveCopy();
	        	response.append("Mail sent successfully and saved copy in Sent Items folder");
	        } else {
	        	mail.send();
	        	response.append("Mail sent successfully");
	        }
		} catch (Exception e) {
			
			response.append(e.getMessage());
		}
		
        return response.toString();
	}
	
	

}
