package com.github.scipioam.scipiofx.mybatis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;

/**
 * @author Alan Scipio
 * created on 2025-02-25
 */
@SuppressWarnings({"LombokGetterMayBeUsed", "UnusedReturnValue"})
public class ScriptRunBuilder {

    /**
     * 数据源key
     */
    private String dataSourceKey;

    /**
     * 脚本文件路径
     */
    private String scriptFilePath;

    /**
     * 是否自动提交
     */
    private boolean autoCommit = true;

    /**
     * 是否在错误时停止
     */
    private boolean stopOnError = true;

    /**
     * 是否逐条执行而非发送整个脚本
     */
    private boolean sendFullScript = false;

    /**
     * 错误信息输出流，为空则按MyBatis的ScriptRunner里默认的，输出到控制台
     */
    private PrintWriter errorLogWriter;

    /**
     * 日志输出流，为空则按MyBatis的ScriptRunner里默认的，输出到控制台
     */
    private PrintWriter logWriter;

    /**
     * 分隔符
     */
    private String delimiter;

    /**
     * 脚本执行中的回调（一定是执行完一条命令后才被调用）
     */
    private MyScriptRunListener runListener;

    public MyScriptRunner build(Connection connection) {
        MyScriptRunner runner = new MyScriptRunner(connection);
        runner.setAutoCommit(autoCommit); // 设置自动提交
        runner.setStopOnError(stopOnError);  // 遇到错误时停止
        runner.setSendFullScript(sendFullScript); // 是发送整个脚本还是逐条执行
        // 分隔符
        if (delimiter != null && !delimiter.isBlank()) {
            runner.setDelimiter(delimiter);
        }
        // 错误信息输出，为空则默认输出到控制台
        if (errorLogWriter != null) {
            runner.setErrorLogWriter(errorLogWriter);
        }
        // 日志输出，为空则默认输出到控制台
        if (logWriter != null) {
            runner.setLogWriter(logWriter);
        }
        // 运行监听器
        if (runListener != null) {
            runner.setRunListener(runListener);
        }
        return runner;
    }

    public File getScriptFile() throws FileNotFoundException {
        File scriptFile = new File(scriptFilePath);
        if (!scriptFile.exists()) {
            throw new FileNotFoundException("script file not found: " + scriptFilePath);
        }
        return scriptFile;
    }

    public String getDataSourceKey() {
        return dataSourceKey;
    }

    public ScriptRunBuilder setDataSourceKey(String dataSourceKey) {
        this.dataSourceKey = dataSourceKey;
        return this;
    }

    public String getScriptFilePath() {
        return scriptFilePath;
    }

    public ScriptRunBuilder setScriptFilePath(String scriptFilePath) {
        this.scriptFilePath = scriptFilePath;
        return this;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public ScriptRunBuilder setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
        return this;
    }

    public boolean isStopOnError() {
        return stopOnError;
    }

    public ScriptRunBuilder setStopOnError(boolean stopOnError) {
        this.stopOnError = stopOnError;
        return this;
    }

    public boolean isSendFullScript() {
        return sendFullScript;
    }

    public ScriptRunBuilder setSendFullScript(boolean sendFullScript) {
        this.sendFullScript = sendFullScript;
        return this;
    }

    public PrintWriter getErrorLogWriter() {
        return errorLogWriter;
    }

    public ScriptRunBuilder setErrorLogWriter(PrintWriter errorLogWriter) {
        this.errorLogWriter = errorLogWriter;
        return this;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public ScriptRunBuilder setDelimiter(String delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    public PrintWriter getLogWriter() {
        return logWriter;
    }

    public ScriptRunBuilder setLogWriter(PrintWriter logWriter) {
        this.logWriter = logWriter;
        return this;
    }

    public MyScriptRunListener getRunListener() {
        return runListener;
    }

    public ScriptRunBuilder setRunListener(MyScriptRunListener runListener) {
        this.runListener = runListener;
        return this;
    }
}
