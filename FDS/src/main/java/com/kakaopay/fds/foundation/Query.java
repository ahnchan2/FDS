package com.kakaopay.fds.foundation;

public class Query {

	//MySQL query set
	public class MySQL {
		public static final String SELECT_USER_ACCOUNT_EXIST = "select if(" + 
																												"	(select count(service_time)" + 
																												"	from" + 
																												"		open_account_log" + 
																												"	where" + 
																												"		user_id = ?) > 0" + 
																												"	, true, false) as result";
		
		public static final String SELECT_USER_ACCOUNT_OPEN_TIME = "select service_time" + 
																															" from" + 
																															"	open_account_log" + 
																															" where" + 
																															"	user_id = ? order by service_time desc limit 1";
		
		public static final String SELECT_CHARGE_THRESHOLD_TIME = "select service_time" + 
																															" from" + 
																															"	(select service_time, @running_total := @running_total + charge_amount as cumulative_sum" + 
																															"	from" + 
																															"		charge_money_log" + 
																															"		join (select @running_total := 0) tmp" + 
																															"	where" + 
																															"		user_id = ?" +
																															"		and ? <= service_time" + 
																															"		and service_time < date_add(?, interval ? hour)" + 
																															"	) t" + 
																															" where" + 
																															"	cumulative_sum >= ?" + 
																															" order by" + 
																															"	service_time asc" + 
																															" limit 1";
		
		public static final String SELECT_UNDER_BALANCE_OR_NOT = "select if(" + 
																														"	(select count(service_time)" + 
																														"		from" + 
																														"			send_money_log" + 
																														"		where" + 
																														"			sender_id = ?" + 
																														"			and ? <= service_time" + 
																														"			and service_time < date_add(?, interval ? hour)" + 
																														"			and sender_balance_before_send - send_amount <= ?) > 0" + 
																														"	, true, false) as result";
		
		public static final String SELECT_OVER_RECEIVE_OR_NOT = "select if(" + 
																													"				(select count(service_time)" + 
																													"				from" + 
																													"					receive_money_log" + 
																													"				where" + 
																													"					receiver_id = ?" +
																													"					and ? <= service_time" + 
																													"					and service_time < date_add(?, interval ? day)" + 
																													"					and receive_amount >= ?) >= ?" +
																													"				, true, false) as result";
		
		public static final String SELECT_OVER_RECEIVE_OR_NOT_BY_CURRENT_TIME = "select if(" + 
																																						"	(select count(service_time)" + 
																																						"	from" + 
																																						"		receive_money_log" + 
																																						"	where" + 
																																						"		receiver_id = ?" + 
																																						"		and	date_add(?, interval ? hour) <= service_time" + 
																																						"		and service_time <= ?" + 
																																						"		and receive_amount >= ?) >= ?" +
																																						"	, true, false) as result";
	}
	
}
