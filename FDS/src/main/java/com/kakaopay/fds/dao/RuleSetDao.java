package com.kakaopay.fds.dao;

import java.sql.Connection;

public interface RuleSetDao {

	boolean isViolatedByRuleA(Connection conn, long userId)  throws Exception;
	
	boolean isViolatedByRuleB(Connection conn, long userId)  throws Exception;
	
	boolean isViolatedByRuleC(Connection conn, long userId) throws Exception;
	
}
