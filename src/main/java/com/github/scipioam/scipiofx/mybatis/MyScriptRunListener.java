package com.github.scipioam.scipiofx.mybatis;

import java.io.PrintWriter;

/**
 * @author Alan Scipio
 * created on 2025-03-07
 */
public interface MyScriptRunListener {

    /**
     * 脚本执行中的回调（一定是执行完一条命令后才被调用）
     *
     * @param command               当前执行的sql命令
     * @param logWriter             日志输出流
     * @param errorLogWriter        错误信息输出流
     * @param executedCommandsCount 已执行的命令数（不包括自身这条）
     */
    void onRunning(String command,
                   PrintWriter logWriter,
                   PrintWriter errorLogWriter,
                   int executedCommandsCount);

}
