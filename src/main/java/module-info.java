module com.github.scipio.scipio_fx {

    requires org.controlsfx.controls;
    requires org.yaml.snakeyaml;
    requires org.slf4j;

    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive MaterialFX;
    requires transitive static lombok;
    requires transitive jakarta.persistence;

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
    exports com.github.ScipioAM.scipio_fx.combobox;
    exports com.github.ScipioAM.scipio_fx.combobox.mfx;
    exports com.github.ScipioAM.scipio_fx.persistence;
    exports com.github.ScipioAM.scipio_fx.persistence.converter;
    exports com.github.ScipioAM.scipio_fx.concurrent;

//    opens com.github.ScipioAM.scipio_fx.test.util;
//    opens com.github.ScipioAM.scipio_fx.test;
//    opens com.github.ScipioAM.scipio_fx.test.bean;
//    opens com.github.ScipioAM.scipio_fx.test.original;
//    opens com.github.ScipioAM.scipio_fx.test.controllers;
//    opens com.github.ScipioAM.scipio_fx.test.threads;

}