package com.github.ScipioAM.scipio_fx.test.threads;

import javafx.concurrent.Task;

/**
 * worker测试实例
 *
 * @author Alan Scipio
 * @since 2022/2/23
 */
public class TestWorker extends Task<Void> {

    @Override
    protected Void call() {
        try {
            for(int i = 0; i <= 10; i++) {
                Thread.sleep(1000);
                updateProgress(i, 10L);
                updateMessage("progress: " + i + "/10");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
