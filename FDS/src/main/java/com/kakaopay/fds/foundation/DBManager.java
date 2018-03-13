package com.kakaopay.fds.foundation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDriver;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.slf4j.LoggerFactory;

public class DBManager {

    protected final static org.slf4j.Logger logger = LoggerFactory.getLogger(DBManager.class);

    private static DBManager instance;

    synchronized public static DBManager getInstance() {
        try {
            if (instance == null) {
                instance = new DBManager();
                logger.info("DBManager initialize: {}", instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;
    }

    private DBManager() {
        initMySQLConnection();
    }

    private void initMySQLConnection(){
        try {
            setupMySQLDriver();
        } catch (Exception e) {
            logger.error("Exception : {}", e.getMessage());
        }
        logger.info("MySQL Connection Created");
    }

    public Connection getConnection() throws SQLException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:apache:commons:dbcp:mysql_connection");
        } catch (SQLException e) {
            logger.error("SQLException : {}", e.getMessage());
            throw e;
        }
        return conn;
    }

    @SuppressWarnings("unchecked")
	public void setupMySQLDriver() throws Exception {
        Class.forName(FDSConfig.getDRIVER_CLASS_NAME());

        @SuppressWarnings("rawtypes")
		GenericObjectPool connectionPool = new GenericObjectPool(null);
        connectionPool.setMaxActive(50);
        connectionPool.setMaxWait(10000);
        connectionPool.setMinIdle(4);
        connectionPool.setMaxIdle(45);

        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
        		FDSConfig.getURL(),
        		FDSConfig.getUSER_NAME(),
        		FDSConfig.getPASSWORD());

        @SuppressWarnings("unused")
		PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(
                connectionFactory,
                connectionPool,
                null,
                "SELECT 1",
                false,
                false);

        PoolingDriver driver = new PoolingDriver();
        driver.registerPool("mysql_connection", connectionPool);
    }

}
