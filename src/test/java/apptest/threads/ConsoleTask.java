package apptest.threads;

import com.github.ScipioAM.scipio_fx.concurrent.AppAbstractTask;
import com.github.ScipioAM.scipio_fx.view.Console;

/**
 * 子线程测试实例。
 * FutureTask，或Callable的返回值Future如要通过{@link #get()}获取线程执行完的结果，是阻塞式的。所以要异步获取，就要判断{@link #isDone()}后再获取
 *
 * @author Alan Scipio
 * @since 2022/2/23
 */
public class ConsoleTask extends AppAbstractTask<String> {

//    private final Console console;

    public ConsoleTask(Console console) {
        super(console.getTextArea().textProperty());
//        this.console = console;
    }

    @Override
    public String doCall() {
        System.out.println("ConsoleTask start...");
        int i = 1;
        int length = 3;
        try {
            for(; i <= length; i++) {
                Thread.sleep(1000);
                String msg = "progress: " + i + "/" + length + "\n";
//                updateProgress(i, 10L);
                updateMessage(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i + "";
    }

}
