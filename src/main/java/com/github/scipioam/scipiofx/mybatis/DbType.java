package com.github.scipioam.scipiofx.mybatis;

/**
 * @author Alan Scipio
 * created on 2024/1/17
 */
public enum DbType {

    SQLITE("SQLite", "org.sqlite.JDBC", "jdbc:sqlite"),

    MYSQL("MySQL", "com.mysql.cj.jdbc.Driver", "jdbc:mysql"),

    SQLSERVER("SQL Server", "com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver"),

    ORACLE("Oracle", "oracle.jdbc.driver.OracleDriver", "jdbc:oracle")

    ;

    public final String name;
    public final String driverClass;
    public final String jdbcUrlPrefix;

    DbType(String name, String driverClass, String jdbcUrlPrefix) {
        this.name = name;
        this.driverClass = driverClass;
        this.jdbcUrlPrefix = jdbcUrlPrefix;
    }

    public static DbType fromName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("DbType name cannot be null!");
        }
        for (DbType value : values()) {
            if (value.name.equals(name)) {
                return value;
            }
        }
        throw new EnumConstantNotPresentException(DbType.class, name);
    }

    public static DbType fromDriverClass(String driverClass) {
        if (driverClass == null) {
            throw new IllegalArgumentException("DbType driverClass cannot be null!");
        }
        if (driverClass.contains("sqlite")) {
            return SQLITE;
        } else if (driverClass.contains("mysql")) {
            return MYSQL;
        } else if (driverClass.contains("sqlserver")) {
            return SQLSERVER;
        }
        throw new EnumConstantNotPresentException(DbType.class, driverClass);
    }

}
