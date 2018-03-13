package com.kakaopay.fds.dao;

import java.sql.Connection;

//did not use now. if you want, implemente in here.
public class RuleSetDaoForY implements RuleSetDao {
	
	@Override
	public boolean isViolatedByRuleA(Connection conn, long userId) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isViolatedByRuleB(Connection conn, long userId) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isViolatedByRuleC(Connection conn, long userId) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
