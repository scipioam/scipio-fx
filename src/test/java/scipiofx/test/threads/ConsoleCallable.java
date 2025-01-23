package scipiofx.test.threads;

import java.util.concurrent.Callable;

/**
 * 子线程测试，异步获取线程执行结果
 *
 * @since 2022/9/16
 */
public class ConsoleCallable implements Callable<String> {

    private final int length;

    public ConsoleCallable(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("length must be positive");
        }
        this.length = length;
    }

    @Override
    public String call() {
        int i = 1;
        try {
            for(; i <= length; i++) {
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i + "";
    }

}
