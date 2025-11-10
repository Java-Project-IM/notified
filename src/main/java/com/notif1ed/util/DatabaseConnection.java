package com.notif1ed.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Database connection manager using HikariCP connection pooling.
 * Provides efficient and thread-safe database connections.
 * 
 * @author Notif1ed Development Team
 * @version 2.0.0
 */
public class DatabaseConnection {
    
    private static final Logger log = LoggerFactory.getLogger(DatabaseConnection.class);
    private static HikariDataSource dataSource;
    
    static {
        try {
            initializeDataSource();
        } catch (Exception e) {
            log.error("Failed to initialize database connection pool", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }
    
    /**
     * Private constructor to prevent instantiation.
     */
    private DatabaseConnection() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
    
    /**
     * Initializes the HikariCP data source with properties from application.properties.
     */
    private static void initializeDataSource() {
        Properties props = loadProperties();
        
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(props.getProperty("db.url"));
        config.setUsername(props.getProperty("db.username"));
        config.setPassword(props.getProperty("db.password"));
        
        // Connection pool settings
        config.setMaximumPoolSize(Integer.parseInt(
            props.getProperty("db.pool.maximum.size", "10")));
        config.setMinimumIdle(Integer.parseInt(
            props.getProperty("db.pool.minimum.idle", "2")));
        config.setConnectionTimeout(Long.parseLong(
            props.getProperty("db.pool.connection.timeout", "30000")));
        config.setIdleTimeout(Long.parseLong(
            props.getProperty("db.pool.idle.timeout", "600000")));
        config.setMaxLifetime(Long.parseLong(
            props.getProperty("db.pool.max.lifetime", "1800000")));
        
        // Performance settings
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        
        dataSource = new HikariDataSource(config);
        log.info("✅ Database connection pool initialized successfully");
    }
    
    /**
     * Loads database properties from application.properties file.
     * 
     * @return Properties object containing database configuration
     */
    private static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = DatabaseConnection.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {
            
            if (input == null) {
                log.error("Unable to find application.properties");
                throw new RuntimeException("application.properties not found");
            }
            
            props.load(input);
            log.debug("Database properties loaded successfully");
            
        } catch (IOException e) {
            log.error("Error loading application.properties", e);
            throw new RuntimeException("Failed to load database properties", e);
        }
        
        return props;
    }
    
    /**
     * Gets a database connection from the connection pool.
     * 
     * @return a database Connection
     * @throws SQLException if a database access error occurs
     */
    public static Connection connect() throws SQLException {
        if (dataSource == null || dataSource.isClosed()) {
            log.error("❌ Data source is not initialized or has been closed");
            throw new SQLException("Database connection pool is not available");
        }
        
        Connection conn = dataSource.getConnection();
        log.debug("✅ Database connection obtained from pool");
        return conn;
    }
    
    /**
     * Closes the connection pool and releases all resources.
     * Should be called on application shutdown.
     */
    public static void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            log.info("✅ Database connection pool closed");
        }
    }
    
    /**
     * Gets statistics about the connection pool.
     * 
     * @return formatted string with pool statistics
     */
    public static String getPoolStatistics() {
        if (dataSource == null) {
            return "Pool not initialized";
        }
        
        return String.format(
            "Pool Stats - Active: %d, Idle: %d, Total: %d, Waiting: %d",
            dataSource.getHikariPoolMXBean().getActiveConnections(),
            dataSource.getHikariPoolMXBean().getIdleConnections(),
            dataSource.getHikariPoolMXBean().getTotalConnections(),
            dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection()
        );
    }
}
