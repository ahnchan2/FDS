package com.kakaopay.fds.dao;

import java.sql.Connection;

public interface UserDao {

	boolean isUserAccountExist(Connection conn, long userId)  throws Exception;
	
}
