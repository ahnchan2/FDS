package com.kakaopay.fds.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.kakaopay.fds.response.FraudResponse;
import com.kakaopay.fds.service.FraudDetectService;


@RestController
public class FraudDetectController {

	private static final Logger logger = LoggerFactory.getLogger(FraudDetectController.class);
	
	@Autowired
	FraudDetectService fraudDetectService;
	
	/**
	 * @param userId
	 * @return JSONString (format FraudResponse)
	 * @throws Exception
	 */
	@RequestMapping(value="/v1/fraud/{user_id}"
									,method=RequestMethod.GET)
	public String v1Fraud(@PathVariable(value = "user_id", required = true) long userId) throws Exception {
		
		logger.info("GET /v1/fraud/" + userId);
		
		FraudResponse response = new FraudResponse(userId);
		
		try {
			//get violation rule
			logger.info("get violation rule for user [" + userId + "]");
			String rule = fraudDetectService.getViolationRule(userId);
			
			//set response
			response.setRule(rule);
			if (rule != null && !rule.isEmpty()) {
				response.setIs_fraud(true);
				logger.info("user [" + userId + "], violation rule [" + rule + "]");
			} else {
				response.setIs_fraud(false);
				logger.info("user [" + userId + "], no violation rule");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

		logger.info("FraudResponse :: " + response);
		
		//make json
		Gson gson = new Gson();
		return gson.toJson(response);
	}
	
}
