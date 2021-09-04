package com.sara.mail.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:mail.properties")
public class MailConfig {
	
	@Value("${exchange.service.url}")
	private String exchangeServiceUrl;
	
	@Value("${login.email.id}")
	private String loginEmailId;
	
	@Value("${login.email.password}")
	private String loginEmailPassword;
	
	@Value("${email.to.addr}")
	private String[] emailToAddr;
	
	@Value("${email.cc.addr}")
	private String[] emailCcAddr;
	
	@Value("${email.subject}")
	private String emailSubject;
	
	@Value("${email.attachment.file.path}")
	private String[] emailAttachmentFilePath;
	
	@Value("${save.copy}")
	private boolean saveCopy;

	public String getExchangeServiceUrl() {
		return exchangeServiceUrl;
	}

	public String getLoginEmailId() {
		return loginEmailId;
	}

	public String getLoginEmailPassword() {
		return loginEmailPassword;
	}

	public String[] getEmailToAddr() {
		return emailToAddr;
	}

	public String[] getEmailCcAddr() {
		return emailCcAddr;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public String[] getEmailAttachmentFilePath() {
		return emailAttachmentFilePath;
	}

	public boolean isSaveCopy() {
		return saveCopy;
	}

	
}
