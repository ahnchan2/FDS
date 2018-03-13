package com.kakaopay.fds.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kakaopay.fds.foundation.Constant;
import com.kakaopay.fds.foundation.Query;

public class RuleSetDaoForX implements RuleSetDao {
	
	private static final Logger logger = LoggerFactory.getLogger(RuleSetDaoForX.class);
	
	@Override
	public boolean isViolatedByRuleA(Connection conn, long userId) throws Exception {
		
		boolean result = false;
		PreparedStatement pstmt01 = null;
		PreparedStatement pstmt02 = null;
		PreparedStatement pstmt03 = null;
		ResultSet rs01 = null; 
		ResultSet rs02 = null;
		ResultSet rs03 = null;
		String sql01;
		String sql02;
		String sql03;
		
		try {
			
			//	sql01 query sample
			/*
			 select service_time
				from
					open_account_log
				where
					user_id = 123 order by service_time desc limit 1;
		 	*/

			//select user account open time
			sql01 = Query.MySQL.SELECT_USER_ACCOUNT_OPEN_TIME;
			logger.debug("sql01 [" + sql01 + "]");
			
			pstmt01 = conn.prepareStatement(sql01);
			pstmt01.setLong(1, userId);
			rs01 = pstmt01.executeQuery();
			
			String openAccountServiceTime = "";
			while(rs01.next()){
				openAccountServiceTime = rs01.getString("service_time");
				logger.debug("openAccountServiceTime :: " + openAccountServiceTime);
			}
			
			// sql02 query sample
			/*
			select service_time
			from
				(select service_time, @running_total := @running_total + charge_amount as cumulative_sum
				from
					charge_money_log
					join (select @running_total := 0) tmp
				where
					user_id = 123
					and '2018-03-12 01:42:39' <= service_time
					and service_time < date_add('2018-03-12 01:42:39', interval 1 hour)
				) t
			where
				cumulative_sum >= 200000
			order by
				service_time asc
			limit 1
		 	*/
			
			//select charge threshold time
			sql02 = Query.MySQL.SELECT_CHARGE_THRESHOLD_TIME;
			logger.debug("sql02 [" + sql02 + "]");
			
			pstmt02 = conn.prepareStatement(sql02);
			pstmt02.setLong(1, userId);
			pstmt02.setString(2, openAccountServiceTime);
			pstmt02.setString(3, openAccountServiceTime);
			pstmt02.setInt(4, Constant.RuleSetForX.RuleA.ACCOUNT_OPENING_CHECK_PERIOD_HOUR);
			pstmt02.setInt(5, Constant.RuleSetForX.RuleA.CHARGE_MONEY_THRESHOLD);
			rs02 = pstmt02.executeQuery();
			
			String chargeThresholdServiceTime = "";
			while(rs02.next()){
				chargeThresholdServiceTime = rs02.getString("service_time");
				logger.debug("chargeThresholdServiceTime :: " + chargeThresholdServiceTime);
			}
			
			//insufficient charge, return false
			if (chargeThresholdServiceTime.equals("")) {
				logger.debug("user [" + userId + "] has insufficient charge");
				return false;
			}
			
			// sql03 query sample
			/*
			select if(
			(select count(service_time)
				from
					send_money_log
				where
					sender_id = 123
					and '2018-03-12 02:12:39' <= service_time
					and service_time < date_add('2018-03-12 01:42:39', interval 1 hour)
					and sender_balance_before_send - send_amount <= 1000) > 0
			, true, false) as result;
			 */
			
			//select under balance or not
			sql03 = Query.MySQL.SELECT_UNDER_BALANCE_OR_NOT;
			logger.debug("sql03 [" + sql03 + "]");
			
			pstmt03 = conn.prepareStatement(sql03);
			pstmt03.setLong(1, userId);
			pstmt03.setString(2, chargeThresholdServiceTime);
			pstmt03.setString(3, openAccountServiceTime);
			pstmt03.setInt(4, Constant.RuleSetForX.RuleA.ACCOUNT_OPENING_CHECK_PERIOD_HOUR);
			pstmt03.setInt(5, Constant.RuleSetForX.RuleA.BALANCE_MONEY_THRESHOLD);
			rs03 = pstmt03.executeQuery();
			
			while(rs03.next()){
				result = rs03.getBoolean("result");
				logger.debug("RuleA result :: " + result);
			}
			
		} catch (Exception e){
			throw e;
		} finally {
			if(rs01 != null) rs01.close();
			if(rs02 != null) rs02.close();
			if(rs03 != null) rs03.close();
			if(pstmt01 != null) pstmt01.close();
			if(pstmt02 != null) pstmt02.close();
			if(pstmt03 != null) pstmt03.close();
		}
		return result;
	}

	@Override
	public boolean isViolatedByRuleB(Connection conn, long userId) throws Exception {
		
		boolean result = false;
		PreparedStatement pstmt01 = null;
		PreparedStatement pstmt02 = null;
		ResultSet rs01 = null; 
		ResultSet rs02 = null;
		String sql01;
		String sql02;
		
		try {
			
			//	sql01 query sample
			/*
			 select service_time
			 from open_account_log
			 where user_id = 123 order by service_time desc limit 1;
		 	*/

			//select user account open time
			sql01 = Query.MySQL.SELECT_USER_ACCOUNT_OPEN_TIME;
			logger.debug("sql01 [" + sql01 + "]");
			
			pstmt01 = conn.prepareStatement(sql01);
			pstmt01.setLong(1, userId);
			rs01 = pstmt01.executeQuery();
			
			String openAccountServiceTime = "";
			while(rs01.next()){
				openAccountServiceTime = rs01.getString("service_time");
				logger.debug("openAccountServiceTime :: " + openAccountServiceTime);
			}
			
			// sql02 query sample
			/*
			select if(
				(select count(service_time)
				from
					receive_money_log
				where
					receiver_id = 123
					and '2018-01-21 01:38:55' <= service_time
					and service_time < date_add('2018-01-21 01:38:55', interval 7 day)
					and receive_amount >= 100000) >= 5
				, true, false) as result;  
		 	*/
			
			//select over receive or not
			sql02 = Query.MySQL.SELECT_OVER_RECEIVE_OR_NOT;
			logger.debug("sql02 [" + sql02 + "]");
			
			pstmt02 = conn.prepareStatement(sql02);
			pstmt02.setLong(1, userId);
			pstmt02.setString(2, openAccountServiceTime);
			pstmt02.setString(3, openAccountServiceTime);
			pstmt02.setInt(4, Constant.RuleSetForX.RuleB.ACCOUNT_OPENING_CHECK_PERIOD_DAY );
			pstmt02.setInt(5, Constant.RuleSetForX.RuleB.RECEIVE_MONEY_THRESHOLD );
			pstmt02.setInt(6, Constant.RuleSetForX.RuleB.RECEIVE_COUNT_THRESHOLD );
			rs02 = pstmt02.executeQuery();
			
			while(rs02.next()){
				result = rs02.getBoolean("result");
				logger.debug("RuleB result :: " + result);
			}
			rs02.close();
			
		} catch (Exception e){
			throw e;
		} finally {
			if(rs01 != null) rs01.close();
			if(rs02 != null) rs02.close();
			if(pstmt01 != null) pstmt01.close();
			if(pstmt02 != null) pstmt02.close();
		}
		return result;
	}

	@Override
	public boolean isViolatedByRuleC(Connection conn, long userId) throws Exception {
		
		boolean result = false;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			
			//	sql query sample
			/*
			select if(
					(select count(service_time)
					from
						receive_money_log
					where
						receiver_id = 123
						and	date_add('2018-03-01 15:00:00', interval -2 hour) <= service_time
						and service_time <= '2018-03-01 15:00:00'
						and receive_amount >= 50000) >= 3
					, true, false) as result
		 	*/
			
			//select over receive or not by current time
			sql = Query.MySQL.SELECT_OVER_RECEIVE_OR_NOT_BY_CURRENT_TIME;
			logger.debug("sql [" + sql + "]");

			String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, userId);
			pstmt.setString(2, now);
			pstmt.setInt(3, -Constant.RuleSetForX.RuleC.RECEIVE_CHECK_PERIOD_HOUR);
			pstmt.setString(4, now);
			pstmt.setInt(5, Constant.RuleSetForX.RuleC.RECEIVE_MONEY_THRESHOLD);
			pstmt.setInt(6, Constant.RuleSetForX.RuleC.RECEIVE_COUNT_THRESHOLD);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				result = rs.getBoolean("result");
				logger.debug("RuleC result :: " + result);
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
