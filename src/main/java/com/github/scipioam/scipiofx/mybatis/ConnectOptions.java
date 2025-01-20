package com.github.scipioam.scipiofx.mybatis;

import lombok.Data;


/**
 * @since 2022/8/31
 */
@Data
public class ConnectOptions {

    private String driverClass;

    private String jdbcUrl;

    private String username;

    private String password;

    //============================================================

    /**
     * 连接空闲时的最大存活时间（以毫秒为单位）
     */
    private long idleTimeout = 60000L;

    /**
     * 默认的连接 auto-commit 状态。如果设置为 true，每个 SQL 语句都作为一个事务执行
     */
    private boolean autoCommit = false;

    /**
     * 连接池中允许的最大连接数。到达此大小时，对数据源的额外请求将会阻塞，直到池中有连接可用或超时
     */
    private int maxPoolSize = 5;

    /**
     * 池中保持空闲的最小连接数量
     */
    private int minIdle = 1;

    /**
     * 一个连接的最大生存期（以毫秒为单位）
     */
    private long maxLifetime = 600000L;

    /**
     * 用于测试池中连接是否仍然活跃的查询
     */
    private String connectionTestQuery = "SELECT 1";

    /**
     * 一个连接的最大生存期（以毫秒为单位）
     */
    private long connectionTimeout = 30000L;

    /**
     * 连接验证的超时时间（以毫秒为单位）
     */
    private long validationTimeout = 5000L;

    /**
     * 连接池名称
     */
    private String poolName;

    /**
     * 默认事务隔离级别
     */
    private String transactionIsolation;

}
