package com.kakaopay.fds.foundation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kakaopay.fds.service.FraudDetectServiceForX;

@Configuration
public class ServiceBeanConfig {
	
	@Bean
	public FraudDetectServiceForX fraudDetectService() {
		return new FraudDetectServiceForX();
	}

//if you want, change service bean.
//	@Bean
//	public FraudDetectServiceForY fraudDetectable() {
//		return new FraudDetectServiceForY();
//	}
	
}
