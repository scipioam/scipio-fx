module com.github.scipio.scipio_fx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires MaterialFX;
    requires static lombok;

    exports com.github.ScipioAM.scipio_fx.app;
    exports com.github.ScipioAM.scipio_fx.constant;
    exports com.github.ScipioAM.scipio_fx.controller;
    exports com.github.ScipioAM.scipio_fx.dialog;
    exports com.github.ScipioAM.scipio_fx.table;
    exports com.github.ScipioAM.scipio_fx.utils;
    exports com.github.ScipioAM.scipio_fx.view;
//    exports com.github.ScipioAM.scipio_fx.test.util;

//    opens com.github.ScipioAM.scipio_fx.test;
//    opens com.github.ScipioAM.scipio_fx.test.bean;
//    opens com.github.ScipioAM.scipio_fx.test.original;
//    opens com.github.ScipioAM.scipio_fx.test.controllers;
//    opens com.github.ScipioAM.scipio_fx.test.threads;

}