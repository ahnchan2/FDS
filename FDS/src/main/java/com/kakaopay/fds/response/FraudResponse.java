package com.kakaopay.fds.response;

public class FraudResponse {

	private long userId;
	
	private boolean is_fraud;
	
	private String rule;

	public FraudResponse(long userId) {
		this.userId = userId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public boolean isIs_fraud() {
		return is_fraud;
	}

	public void setIs_fraud(boolean is_fraud) {
		this.is_fraud = is_fraud;
	}

	public String getRule() {
		return rule;
	}

	public void setRule(String rule) {
		this.rule = rule;
	}

	@Override
	public String toString() {
		return "FraudResponse [userId=" + userId + ", is_fraud=" + is_fraud + ", rule=" + rule + "]";
	}
	
}
