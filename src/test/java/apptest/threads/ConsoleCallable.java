package apptest.threads;

import java.util.concurrent.Callable;

/**
 * 子线程测试，异步获取线程执行结果
 *
 * @since 2022/9/16
 */
public class ConsoleCallable implements Callable<String> {

    private final ConsoleTask consoleTask;

    public ConsoleCallable(ConsoleTask consoleTask) {
        this.consoleTask = consoleTask;
    }

    @Override
    public String call() throws Exception {
        return consoleTask.call();
    }

}
