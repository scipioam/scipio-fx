package com.github.scipioam.scipiofx.mybatis;

import com.github.scipioam.scipiofx.framework.LogHelper;
import com.github.scipioam.scipiofx.utils.StringUtils;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.type.TypeAliasRegistry;

import javax.sql.DataSource;
import java.io.*;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @since 2022/8/31
 */
@Getter
public class MybatisManager {

    private String defaultDataSourceKey;

    private MybatisConfig config;

    private final Map<String, MyDataSource> dataSourceMap = new HashMap<>();

    private final LogHelper log = new LogHelper(MybatisManager.class);

    public MybatisManager() {
    }

    public MybatisManager(MybatisConfig config) throws Exception {
        init(config);
    }

    //================================================== ↓↓↓↓↓↓ 初始化相关 ↓↓↓↓↓↓ ==================================================

    /**
     * 初始化
     *
     * @param config 初始化参数
     */
    public void init(MybatisConfig config) throws Exception {
        if (config == null) {
            throw new IllegalArgumentException("MybatisConfig can not be null !");
        }
        this.config = config;
        Map<String, ConnectOptions> connects = config.getConnects();
        //将key排序
        Set<String> keys = connects.keySet().stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new));
        for (String dataSourceKey : keys) {
            ConnectOptions connectOptions = connects.get(dataSourceKey);
            if (defaultDataSourceKey == null || defaultDataSourceKey.isEmpty()) {
                //第一个数据源为默认数据源
                defaultDataSourceKey = dataSourceKey;
            }
            addDataSource(config, dataSourceKey, connectOptions);
        }
    }

    /**
     * 添加数据源
     *
     * @param config         Mybatis配置
     * @param connectOptions 连接参数
     * @param dataSourceKey  数据源key
     */
    private void addDataSource(MybatisConfig config, String dataSourceKey, ConnectOptions connectOptions) throws Exception {
        //自定义配置加载
        Configuration configuration = new Configuration();
        //初始化数据源
        DataSource dataSource = initDataSource(connectOptions);
        Environment environment = new Environment(dataSourceKey, new JdbcTransactionFactory(), dataSource);
        configuration.setEnvironment(environment);
        //注册entity的包名
        TypeAliasRegistry typeAliasRegistry = configuration.getTypeAliasRegistry();
        if (config.getTypeAliases() != null) {
            for (String typeAlias : config.getTypeAliases()) {
                typeAliasRegistry.registerAliases(typeAlias);
            }
        }
        //注册mapper.java
        if (config.getMapperPackages() != null) {
            for (String mapperPackage : config.getMapperPackages()) {
                configuration.addMappers(mapperPackage);
            }
        }
        if (config.getMapperInterfaces() != null) {
            for (Class<?> mapperInterface : config.getMapperInterfaces()) {
                configuration.addMapper(mapperInterface);
            }
        }
        if (config.getMapperInterfaceNames() != null) {
            for (String mapperInterfaceName : config.getMapperInterfaceNames()) {
                Class<?> mapperInterface = Class.forName(mapperInterfaceName);
                configuration.addMapper(mapperInterface);
            }
        }
        //注册mapper.xml
        registryMapperXml(configuration, config.getMappersLocation());
        //指定日志实现类
        if (config.getLogImpl() != null) {
            configuration.setLogImpl(config.getLogImpl());
        }
        //构建SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        dataSourceMap.put(dataSourceKey, new MyDataSource(dataSourceKey, sqlSessionFactory, connectOptions));
        log.info("SqlSessionFactory init success, dataSource loaded: {}", dataSourceKey);
    }

    public void addDataSource(String dataSourceKey, ConnectOptions connectOptions) throws Exception {
        if (config == null) {
            throw new IllegalStateException("MybatisManager must be inited first");
        }
        addDataSource(config, dataSourceKey, connectOptions);
    }

    /**
     * 初始化数据源
     */
    private DataSource initDataSource(ConnectOptions options) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(options.getJdbcUrl());
        dataSource.setDriverClassName(options.getDriverClass());
        dataSource.setUsername(options.getUsername());
        dataSource.setPassword(options.getPassword());
        dataSource.setIdleTimeout(options.getIdleTimeout());
        dataSource.setAutoCommit(options.isAutoCommit());
        dataSource.setMaximumPoolSize(options.getMaxPoolSize());
        dataSource.setMinimumIdle(options.getMinIdle());
        dataSource.setMaxLifetime(options.getMaxLifetime());
        dataSource.setConnectionTestQuery(options.getConnectionTestQuery());
        dataSource.setConnectionTimeout(options.getConnectionTimeout());
        dataSource.setValidationTimeout(options.getValidationTimeout());
        dataSource.setPoolName(options.getPoolName());
        dataSource.setTransactionIsolation(options.getTransactionIsolation());
        return dataSource;
    }

    /**
     * 解析mapper.xml文件
     */
    private void registryMapperXml(Configuration configuration, String rootPath) throws IOException, URISyntaxException {
        if (StringUtils.isBlank(rootPath)) {
            rootPath = System.getProperty("java.class.path");
        }

        Enumeration<URL> mappersUrl;
        if (rootPath.toLowerCase().startsWith("file:/")) {
            rootPath = rootPath.replace("file:/", "");
            File rootDir = new File(rootPath);
            mappersUrl = findFilesToURL(rootDir);
        } else {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            mappersUrl = contextClassLoader.getResources(rootPath);
        }

        if (!mappersUrl.hasMoreElements()) {
            log.warn("Can not found any mapper.xml file in rootPath [{}]", rootPath);
        }
        while (mappersUrl.hasMoreElements()) {
            URL url = mappersUrl.nextElement();
            if (url.getProtocol().equals("file")) {
                URI uri = url.toURI();
                File file = new File(uri);
                List<File> files = getFileList(file);
                if (files.isEmpty()) {
                    throw new FileNotFoundException("Can not found any mapper.xml file in [" + file.getPath() + "]");
                }
                for (File f : files) {
                    if (!f.exists()) {
                        throw new FileNotFoundException("mapper file does not exists: " + f.getPath());
                    }
                    if (!f.getName().toLowerCase().endsWith(".xml")) {
                        //跳过后缀名不是.xml的文件
                        continue;
                    }
                    log.info("mapper xml found: {}", f.getPath());
                    try (FileInputStream in = new FileInputStream(f)) {
                        XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(in, configuration, f.getPath(), configuration.getSqlFragments());
                        xmlMapperBuilder.parse();
                    }
                }
            } else {
                JarURLConnection urlConnection = (JarURLConnection) url.openConnection();
                JarFile jarFile = urlConnection.getJarFile();
                Enumeration<JarEntry> entries = jarFile.entries();
                Pattern mapperPattern = Pattern.compile("(/?)(" + rootPath + ")(\\S+)(\\.xml)");
                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = entries.nextElement();
                    String entryName = jarEntry.getName();
                    Matcher matcher = mapperPattern.matcher(entryName);
                    if (matcher.find()) {
                        log.info("mapper xml found: {}", entryName);
                        InputStream in = jarFile.getInputStream(jarEntry);
                        XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(in, configuration, jarEntry.getName(), configuration.getSqlFragments());
                        xmlMapperBuilder.parse();
                        in.close();
                    }
                }
            }
        }
    }

    public void updateDataSource(String dataSourceKey, ConnectOptions opts) {
        MyDataSource bean = dataSourceMap.get(dataSourceKey);
        if (bean == null) {
            throw new IllegalArgumentException("DataSource [" + dataSourceKey + "] not found !");
        }
        //关闭旧的数据源
        closeDataSource(dataSourceKey, false);
        //初始化新的数据源
        SqlSessionFactory sqlSessionFactory = bean.getSqlSessionFactory();
        DataSource newDataSource = initDataSource(opts);
        Environment newEnvironment = new Environment(dataSourceKey, new JdbcTransactionFactory(), newDataSource);
        sqlSessionFactory.getConfiguration().setEnvironment(newEnvironment);
    }

    public void closeDataSource(String dataSourceKey, boolean removeFromPool) {
        MyDataSource dataSourceInfo = dataSourceMap.get(dataSourceKey);
        if (dataSourceInfo == null) {
            throw new IllegalArgumentException("DataSource [" + dataSourceKey + "] not found !");
        }
        if (dataSourceMap.size() == 1) {
            throw new IllegalStateException("Can not remove the last data source !");
        }
        SqlSessionFactory sqlSessionFactory = dataSourceInfo.getSqlSessionFactory();
        DataSource dataSource = sqlSessionFactory.getConfiguration().getEnvironment().getDataSource();
        if (dataSource instanceof HikariDataSource) {
            ((HikariDataSource) dataSource).close();
            if (removeFromPool) {
                dataSourceMap.remove(dataSourceKey);
                //如果是默认数据源被移除，则将第一个数据源设为默认
                if (defaultDataSourceKey.equals(dataSourceKey)) {
                    defaultDataSourceKey = dataSourceMap.keySet().iterator().next();
                }
            }
        } else {
            throw new IllegalStateException("DataSource [" + dataSourceKey + "] is not a HikariDataSource !");
        }
    }

    public void closeDataSource(String dataSourceKey) {
        closeDataSource(dataSourceKey, true);
    }

    //================================================== ↓↓↓↓↓↓ 其他 ↓↓↓↓↓↓ ==================================================

    /**
     * 打开一个DB连接会话
     */
    public SqlSession openSession(String dataSourceKey) {
        MyDataSource dataSource = dataSourceMap.get(dataSourceKey);
        if (dataSource != null) {
            return dataSource.openSession();
        } else {
            throw new IllegalStateException("DataSource [" + dataSourceKey + "] not found !");
        }
    }

    public SqlSession openSession() {
        return openSession(defaultDataSourceKey);
    }

    /**
     * 执行DB操作
     *
     * @param dataSourceKey 数据源key
     * @param mapperClass   mapper类型
     * @param handler       操作
     */
    public <T> void doAction(String dataSourceKey, Class<T> mapperClass, ActionHandler<T> handler) {
        SqlSession sqlSession = openSession(dataSourceKey);
        try {
            T mapper = sqlSession.getMapper(mapperClass);
            if (handler != null) {
                handler.handle(mapper);
                sqlSession.commit();
            }
        } catch (Exception e) {
            sqlSession.rollback();
//            log.error("Got exception while executing sql !", e);
            throw e;
        } finally {
            sqlSession.close();
        }
    }

    public <T> void doAction(Class<T> mapperClass, ActionHandler<T> handler) {
        doAction(defaultDataSourceKey, mapperClass, handler);
    }

    public ConnectOptions getConnectOptions(String dataSourceKey) {
        MyDataSource dataSource = dataSourceMap.get(dataSourceKey);
        if (dataSource != null) {
            return dataSource.getConnectOptions();
        } else {
            return null;
        }
    }

    public ConnectOptions getConnectOptions() {
        return getConnectOptions(defaultDataSourceKey);
    }

    public List<MyDataSource> getDataSourceList() {
        return new ArrayList<>(dataSourceMap.values());
    }

    /**
     * 测试连接
     *
     * @param dataSourceKey 数据源key
     * @return 异常信息，null表示连接成功
     */
    public Throwable testConnect(String dataSourceKey) {
        ConnectOptions connectOptions = getConnectOptions(dataSourceKey);
        if (connectOptions == null) {
            throw new IllegalStateException("DataSource [" + dataSourceKey + "] not found !");
        }
        try (SqlSession sqlSession = openSession(dataSourceKey); Connection connection = sqlSession.getConnection()) {
            //非sqlite的再执行一个
            String testSql = connectOptions.getConnectionTestQuery();
            if (StringUtils.isBlank(testSql)) {
                testSql = "select 1 from dual";
            }
            connection.prepareStatement(testSql).execute();
            return null;
        } catch (Exception e) {
            return e;
        }
    }

    public Throwable testConnect() {
        return testConnect(defaultDataSourceKey);
    }

    public void runScript(ScriptRunBuilder builder) throws Exception {
        // 获取DB连接
        SqlSession sqlSession;
        if (StringUtils.isNotBlank(builder.getDataSourceKey())) {
            sqlSession = openSession(builder.getDataSourceKey());
        } else {
            sqlSession = openSession();
        }
        Connection connection = sqlSession.getConnection();
        try {
            // 初始化脚本执行器
            MyScriptRunner runner = builder.build(connection);
            // 执行脚本
            File scriptFile = builder.getScriptFile();
            try (Reader reader = new FileReader(scriptFile)) {
                runner.runScript(reader);
            }
        } catch (Exception e) {
            sqlSession.rollback();
//            log.error("Got exception while executing sql !", e);
            throw e;
        } finally {
            sqlSession.close();
        }
    }

    public void runScript(File scriptFile) throws Exception {
        ScriptRunBuilder builder = new ScriptRunBuilder()
                .setScriptFilePath(scriptFile.getPath());
        runScript(builder);
    }

    public void runScript(String scriptFilePath) throws Exception {
        ScriptRunBuilder builder = new ScriptRunBuilder()
                .setScriptFilePath(scriptFilePath);
        runScript(builder);
    }

    //================================================== ↓↓↓↓↓↓ 工具方法 ↓↓↓↓↓↓ ==================================================

    /**
     * 递归查找指定的文件
     *
     * @param directory        查找的目录
     * @param targetFileSuffix 目标后缀
     */
    public static List<File> findFiles(File directory, String targetFileSuffix) {
        List<File> javaFiles = new ArrayList<>();
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        javaFiles.addAll(findFiles(file, targetFileSuffix));
                    } else {
                        if (StringUtils.isNotBlank(targetFileSuffix)) {
                            if (file.getName().endsWith(targetFileSuffix)) {
                                javaFiles.add(file);
                            }
                        } else {
                            javaFiles.add(file);
                        }
                    }
                }
            }
        }
        return javaFiles;
    }

    public static List<File> findFiles(File directory) {
        return findFiles(directory, null);
    }

    public static Enumeration<URL> findFilesToURL(File directory, String targetFileSuffix) {
        List<File> fileList = findFiles(directory);
        Vector<URL> urlVector = new Vector<>();
        for (File file : fileList) {
            try {
                URL url = file.toURI().toURL();
                urlVector.add(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Collections.enumeration(urlVector);
    }

    public static Enumeration<URL> findFilesToURL(File directory) {
        return findFilesToURL(directory, null);
    }

    public static List<File> getFileList(File file) {
        List<File> list = new ArrayList<>();
        if (file.isDirectory()) {
            File[] subFiles = file.listFiles();
            if (subFiles != null) {
                list.addAll(Arrays.asList(subFiles));
            }
        } else {
            list.add(file);
        }
        return list;
    }

}
