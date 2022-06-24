module com.github.scipio.scipio_fx {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;

    requires transitive static lombok;
    requires org.controlsfx.controls;
    requires org.yaml.snakeyaml;
    requires transitive MaterialFX;


    exports com.github.ScipioAM.scipio_fx.app;
    exports com.github.ScipioAM.scipio_fx.app.config;
    exports com.github.ScipioAM.scipio_fx.constant;
    exports com.github.ScipioAM.scipio_fx.controller;
    exports com.github.ScipioAM.scipio_fx.dialog;
    exports com.github.ScipioAM.scipio_fx.dialog.mfx;
    exports com.github.ScipioAM.scipio_fx.table;
    exports com.github.ScipioAM.scipio_fx.table.annotations;
    exports com.github.ScipioAM.scipio_fx.table.cell;
    exports com.github.ScipioAM.scipio_fx.table.mfx;
    exports com.github.ScipioAM.scipio_fx.utils;
    exports com.github.ScipioAM.scipio_fx.view;
    exports com.github.ScipioAM.scipio_fx.exception;

    opens com.github.ScipioAM.scipio_fx.test.util;
    opens com.github.ScipioAM.scipio_fx.test;
    opens com.github.ScipioAM.scipio_fx.test.bean;
    opens com.github.ScipioAM.scipio_fx.test.original;
    opens com.github.ScipioAM.scipio_fx.test.controllers;
    opens com.github.ScipioAM.scipio_fx.test.threads;

}