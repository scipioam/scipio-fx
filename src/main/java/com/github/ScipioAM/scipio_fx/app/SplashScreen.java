package com.github.ScipioAM.scipio_fx.app;

import javafx.scene.Parent;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Objects;

/**
 * @author Alan Scipio
 * @since 2022/2/22
 */
@Data
@Accessors(chain = true)
public class SplashScreen {

    /**
     * 启动画面的图片
     */
    private String imagePath = "/test/img/splash.gif";

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

    public static SplashScreen create(String imagePath, boolean visible, boolean progressBarVisible) {
        return new SplashScreen()
                .setImagePath(imagePath)
                .setVisible(visible)
                .setProgressBarVisible(progressBarVisible);
    }

    /**
     * 构建启动画面
     */
    public Parent buildViews() {
        final VBox vbox = new VBox();
        final ImageView splashImageView = new ImageView(Objects.requireNonNull(getClass().getResource(getImagePath())).toExternalForm());
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
     * @param splashImageView 设定的
     * @return 构建好了的进度条
     */
    public ProgressBar buildProgressBar(ImageView splashImageView) {
        ProgressBar progressBar = new ProgressBar();
//            progressBar.setPrefHeight(useMaterialUI ? 9.0 : 12.0);
        progressBar.setPrefHeight(12.0);
        progressBar.setPrefWidth(splashImageView.getImage().getWidth());
        return progressBar;
    }

}
