package com.github.scipioam.scipiofx.framework;

/**
 * @author Alan Scipio
 * created on 2025-01-17
 */
public class EmptyAppInitThread extends AppInitThread{

    private final long sleepTime;

    public EmptyAppInitThread() {
        this.sleepTime = defaultSplashTime;
    }

    public EmptyAppInitThread(long sleepTime) {
        super.defaultSplashTime = sleepTime;
        this.sleepTime = sleepTime;
    }

    @Override
    public void init(JFXApplication application, AppContext context) throws Exception {
        Thread.sleep(sleepTime);
    }
}
