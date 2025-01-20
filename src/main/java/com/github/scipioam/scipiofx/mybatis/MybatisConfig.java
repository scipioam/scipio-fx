package com.github.scipioam.scipiofx.mybatis;

import lombok.Data;
import org.apache.ibatis.logging.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @since 2022/8/31
 */
@Data
public class MybatisConfig {


    /**
     * 基本连接参数
     */
    private Map<String, ConnectOptions> connects = new HashMap<>();

    /**
     * entity的包名
     */
    private List<String> typeAliases;

    /**
     * mapper.java的包全名
     */
    private List<String> mapperPackages;

    /**
     * mapper.java的直接类全名
     */
    private List<String> mapperInterfaceNames;
    private List<Class<?>> mapperInterfaces;

    /**
     * mapper.xml的相对路径根目录
     */
    private String mappersLocation;

    /**
     * 日志配置
     */
    private Class<? extends Log> logImpl;

    //================================================== ↓↓↓↓↓↓ setter/getter ↓↓↓↓↓↓ ==================================================

    public MybatisConfig addTypeAlias(String typeAlias) {
        if (this.typeAliases == null) {
            this.typeAliases = new ArrayList<>();
        }
        typeAliases.add(typeAlias);
        return this;
    }

    public MybatisConfig addMapperPackage(String mapperPackage) {
        if (this.mapperPackages == null) {
            this.mapperPackages = new ArrayList<>();
        }
        mapperPackages.add(mapperPackage);
        return this;
    }

    public MybatisConfig addMapperInterfaceName(String mapperInterfaceName) {
        if (this.mapperInterfaceNames == null) {
            this.mapperInterfaceNames = new ArrayList<>();
        }
        mapperInterfaceNames.add(mapperInterfaceName);
        return this;
    }

    public MybatisConfig addMapperInterface(Class<?> mapperInterface) {
        if (this.mapperInterfaces == null) {
            this.mapperInterfaces = new ArrayList<>();
        }
        mapperInterfaces.add(mapperInterface);
        return this;
    }

    public MybatisConfig setDriverClass(String dataSourceKey, String driverClass) {
        ConnectOptions opts = connects.computeIfAbsent(dataSourceKey, k -> new ConnectOptions());
        opts.setDriverClass(driverClass);
        return this;
    }

    public MybatisConfig setJdbcUrl(String dataSourceKey, String url) {
        ConnectOptions opts = connects.computeIfAbsent(dataSourceKey, k -> new ConnectOptions());
        opts.setJdbcUrl(url);
        return this;
    }

    public MybatisConfig setUsername(String dataSourceKey, String username) {
        ConnectOptions opts = connects.computeIfAbsent(dataSourceKey, k -> new ConnectOptions());
        opts.setUsername(username);
        return this;
    }

    public MybatisConfig setPassword(String dataSourceKey, String password) {
        ConnectOptions opts = connects.computeIfAbsent(dataSourceKey, k -> new ConnectOptions());
        opts.setPassword(password);
        return this;
    }

    public ConnectOptions getConnectOptions(String dataSourceKey) {
        return connects.get(dataSourceKey);
    }

    public String getDriverClass(String dataSourceKey) {
        ConnectOptions opts = connects.get(dataSourceKey);
        return opts == null ? null : opts.getDriverClass();
    }

    public String getJdbcUrl(String dataSourceKey) {
        ConnectOptions opts = connects.get(dataSourceKey);
        return opts == null ? null : opts.getJdbcUrl();
    }

    public String getUsername(String dataSourceKey) {
        ConnectOptions opts = connects.get(dataSourceKey);
        return opts == null ? null : opts.getUsername();
    }

    public String getPassword(String dataSourceKey) {
        ConnectOptions opts = connects.get(dataSourceKey);
        return opts == null ? null : opts.getPassword();
    }

}
