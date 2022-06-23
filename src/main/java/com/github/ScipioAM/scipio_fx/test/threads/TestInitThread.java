package com.github.ScipioAM.scipio_fx.test.threads;

import com.github.ScipioAM.scipio_fx.app.AppInitThread;
import com.github.ScipioAM.scipio_fx.app.JFXApplication;

/**
 * @since 2022/6/23
 */
public class TestInitThread extends AppInitThread {
    @Override
    public void init(JFXApplication application) {
        try {
            System.out.println("start init");
            Thread.sleep(2000);
            System.out.println("init finished");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
