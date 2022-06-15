package com.github.ScipioAM.scipio_fx.dialog;

/**
 * @author Alan Scipio
 * @since 2022/2/23
 */
public interface IDialog {

    void show();

//    default void showAndWait() {
//        System.err.println("未实现却调用了此方法:IDialog#showAndWait()");
//    }

    void close();

}
