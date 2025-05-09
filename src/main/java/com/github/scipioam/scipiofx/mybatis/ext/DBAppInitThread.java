package com.github.scipioam.scipiofx.mybatis.ext;

import com.github.scipioam.scipiofx.framework.AppContext;
import com.github.scipioam.scipiofx.framework.AppInitThread;
import com.github.scipioam.scipiofx.framework.JFXApplication;
import com.github.scipioam.scipiofx.framework.LogHelper;
import com.github.scipioam.scipiofx.framework.config.ConfigRootProperties;
import com.github.scipioam.scipiofx.framework.exception.FrameworkException;
import com.github.scipioam.scipiofx.mybatis.MybatisConfig;
import com.github.scipioam.scipiofx.mybatis.MybatisManager;

/**
 * 默认的MyBatis初始化线程
 *
 * @author Alan Scipio
 * @since 2024/4/30
 */
public class DBAppInitThread extends AppInitThread {

    protected final LogHelper log;

    public DBAppInitThread() {
        this(null);
    }

    public DBAppInitThread(LogHelper log) {
        if (log == null) {
            log = new LogHelper(getClass());
        }
        this.log = log;
    }

    @Override
    public void init(JFXApplication application, AppContext context) throws Exception {
        doBeforeMybatisInit(application, context);
        if (context instanceof DBAppContext dbAppContext) {
            ConfigRootProperties rootProperties = dbAppContext.getConfigRootProperties();
            if (rootProperties instanceof DBConfigProperties dbConfigProperties) {
                long startTime = System.currentTimeMillis();
                // 获取Mybatis配置
                MybatisConfig mybatisConfig = dbConfigProperties.getMybatis();
                dbAppContext.setMybatisConfig(mybatisConfig);
                setSplashProgress(0.2);
                // 初始化Mybatis管理器
                MybatisManager mybatisManager = new MybatisManager();
                mybatisManager.init(mybatisConfig);
                dbAppContext.setMybatisManager(mybatisManager);
                log.info("Mybatis init cost time: {}ms", System.currentTimeMillis() - startTime);
                setSplashProgress(0.9);
                // 自定义其他操作
                doAfterMybatisInit(application, context, mybatisConfig, mybatisManager);
            } else {
                throw new FrameworkException("DBAppInitThread must be used with DBConfigProperties");
            }
        } else {
            throw new FrameworkException("DBAppInitThread must be used with DBAppContext");
        }
    }

    @Override
    protected void afterInit(JFXApplication application, AppContext context) {
        setSplashProgress(1.0);
    }

    protected void doBeforeMybatisInit(JFXApplication application, AppContext context) {
        // Do nothing
    }

    protected void doAfterMybatisInit(JFXApplication application, AppContext context, MybatisConfig mybatisConfig, MybatisManager mybatisManager) {
        // Do nothing
    }

}
