package com.kakaopay.fds.service;

import java.sql.Connection;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kakaopay.fds.dao.RuleSetDao;
import com.kakaopay.fds.dao.UserDao;
import com.kakaopay.fds.foundation.Constant;
import com.kakaopay.fds.foundation.DBManager;
import com.kakaopay.fds.foundation.DaoBeanConfig;

public class FraudDetectServiceForX implements FraudDetectService {

	private static final Logger logger = LoggerFactory.getLogger(FraudDetectServiceForX.class);
	
	@Override
	public String getViolationRule(long userId) throws Exception {

		String resultString = "";
		
		//dynamic Dao allocation
		UserDao userDao = (UserDao)Class.forName(DaoBeanConfig.UserDao).newInstance();
		RuleSetDao ruleSetDao = (RuleSetDao)Class.forName(DaoBeanConfig.RuleSetDao).newInstance();
		
		Connection conn = null;
		
		try {
			
			//get db connection
			conn = DBManager.getInstance().getConnection();
			
			//check user account exist
			if (!userDao.isUserAccountExist(conn, userId)) {
				String msg = Constant.Message.Error.USER_ACCOUNT_NOT_EXISTS + ", user_id [" + userId + "]"; 
				Exception e = new Exception(msg);
				throw e;
			}
			logger.debug("user [" + userId + "] account exist, go to next step");
			
			//check rules
			ArrayList<String> resultlist=new ArrayList<String>();
			
			logger.info("checking RuleA");
			if (ruleSetDao.isViolatedByRuleA(conn, userId))
				resultlist.add("RuleA");
			logger.info("checking RuleB");
			if (ruleSetDao.isViolatedByRuleB(conn, userId))
				resultlist.add("RuleB");
			logger.info("checking RuleC");
			if (ruleSetDao.isViolatedByRuleC(conn, userId))
				resultlist.add("RuleC");
			
			logger.info("rule check finished");
			
			//make return value
			resultString = String.join(",", resultlist);
			logger.debug(resultString);
			
		} catch  (Exception e) {
			throw e;
		} finally {
				if (conn != null) conn.close();
		}
		
		return resultString;
	}
	

}
