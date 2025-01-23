package com.github.scipioam.scipiofx.view;

import com.github.scipioam.scipiofx.framework.config.AppProperties;
import javafx.scene.Parent;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.MissingResourceException;

/**
 * @author Alan Scipio
 * @since 2022/2/22
 */
@SuppressWarnings({"LombokGetterMayBeUsed", "LombokGetterMayBeUsed"})
public class SplashScreen {

    /**
     * 启动画面的图片
     */
    private URL splashImgUrl;

    /**
     * 启动画面是否显示
     */
    private boolean visible = true;

    /**
     * 进度条是否显示
     */
    private boolean progressBarVisible = false;

    /**
     * 进度条对象
     */
    private ProgressBar progressBar;

    /**
     * 启动画面的stage
     */
    private Stage stage;

    public static SplashScreen create() {
        return new SplashScreen();
    }

    public static SplashScreen create(boolean visible) {
        return new SplashScreen().setVisible(visible);
    }

    public static SplashScreen create(URL splashImgUrl, boolean visible, boolean progressBarVisible) {
        return new SplashScreen()
                .setSplashImgUrl(splashImgUrl)
                .setVisible(visible)
                .setProgressBarVisible(progressBarVisible);
    }

    /**
     * 构建启动画面
     */
    public Parent buildViews() {
        if (splashImgUrl == null) {
            throw new MissingResourceException("SplashScreen build failed, image url is null", AppProperties.class.getName(), "app.splashImgPath");
        }
        final VBox vbox = new VBox();
        final ImageView splashImageView = new ImageView(new Image(splashImgUrl.toExternalForm()));
        if (progressBarVisible) {
            if (progressBar == null) {
                progressBar = buildProgressBar(splashImageView);
            }
            vbox.getChildren().addAll(splashImageView, progressBar);
        } else {
            vbox.getChildren().add(splashImageView);
        }
        return vbox;
    }

    /**
     * 构建进度条
     *
     * @param splashImageView 设定的
     * @return 构建好了的进度条
     */
    protected ProgressBar buildProgressBar(ImageView splashImageView) {
        ProgressBar progressBar = new ProgressBar();
//            progressBar.setPrefHeight(useMaterialUI ? 9.0 : 12.0);
        progressBar.setPrefHeight(12.0);
        progressBar.setPrefWidth(splashImageView.getImage().getWidth());
        return progressBar;
    }

    public void closeStage() {
        if (stage != null) {
            stage.close();
        }
    }

    public Stage getStage() {
        return stage;
    }

    public SplashScreen setStage(Stage stage) {
        this.stage = stage;
        return this;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public SplashScreen setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
        return this;
    }

    public boolean isProgressBarVisible() {
        return progressBarVisible;
    }

    public SplashScreen setProgressBarVisible(boolean progressBarVisible) {
        this.progressBarVisible = progressBarVisible;
        return this;
    }

    public boolean isVisible() {
        return visible;
    }

    public SplashScreen setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public URL getSplashImgUrl() {
        return splashImgUrl;
    }

    public SplashScreen setSplashImgUrl(URL splashImgUrl) {
        this.splashImgUrl = splashImgUrl;
        return this;
    }

    /**
     * 设置进度
     *
     * @param progress 百分比进度，0-1之间（0%-100%）
     */
    public void setProgress(double progress) {
        if (progressBar != null) {
            progressBar.setProgress(progress);
        }
    }
}
