package com.kakaopay.fds.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kakaopay.fds.foundation.Query;

public class UserDaoForX implements UserDao {

	private static final Logger logger = LoggerFactory.getLogger(UserDaoForX.class);
	
	@Override
	public boolean isUserAccountExist(Connection conn, long userId) throws Exception {
		
		boolean result = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null; 
		try {
			
			String sql;
			
			//	query sample
			/*
			select if(
				(select count(service_time)
				from
					open_account_log
				where
					user_id = 123) > 0
				, true, false) as result
		 	*/

			sql = Query.MySQL.SELECT_USER_ACCOUNT_EXIST;
			logger.debug("sql [" + sql + "]");
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, userId);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				result = rs.getBoolean("result");
				logger.debug("user accunt exist result :: " + result);
			}
			
		} catch (Exception e){
			throw e;
		} finally {
			if(rs != null) rs.close();
			if(pstmt != null) pstmt.close();
		}
		
		return result;
	}

}
