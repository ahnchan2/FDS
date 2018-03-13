package com.kakaopay.fds.service;

public interface FraudDetectService {

	String getViolationRule(long userId)  throws Exception;
}
