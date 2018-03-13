package com.kakaopay.fds.foundation;

public class Constant {

	//RuleSet
	public class RuleSetForX {
		public class RuleA {
			final public static int ACCOUNT_OPENING_CHECK_PERIOD_HOUR = 1;
			final public static int CHARGE_MONEY_THRESHOLD = 200000;
			final public static int BALANCE_MONEY_THRESHOLD = 1000;
		}
		public class RuleB {
			final public static int ACCOUNT_OPENING_CHECK_PERIOD_DAY = 7;
			final public static int RECEIVE_MONEY_THRESHOLD = 100000;
			final public static int RECEIVE_COUNT_THRESHOLD = 5;
		}
		public class RuleC {
			final public static int RECEIVE_CHECK_PERIOD_HOUR = 2;
			final public static int RECEIVE_MONEY_THRESHOLD = 50000;
			final public static int RECEIVE_COUNT_THRESHOLD = 3;
		}
	}
	
	//custom message
	public class Message {
		public class Error {
			final public static String USER_ACCOUNT_NOT_EXISTS = "user account does not exist";
		}
	}
	
}
