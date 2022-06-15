module com.github.scipio.scipio_fx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires MaterialFX;
    requires static lombok;

    opens com.github.ScipioAM.scipio_fx.app;
    opens com.github.ScipioAM.scipio_fx.constant;
    opens com.github.ScipioAM.scipio_fx.controller;
    opens com.github.ScipioAM.scipio_fx.dialog;
    opens com.github.ScipioAM.scipio_fx.table;
    opens com.github.ScipioAM.scipio_fx.utils;
    opens com.github.ScipioAM.scipio_fx.view;
    opens com.github.ScipioAM.scipio_fx.test.util;

    opens com.github.ScipioAM.scipio_fx.test;
    opens com.github.ScipioAM.scipio_fx.test.bean;
    opens com.github.ScipioAM.scipio_fx.test.original;
    opens com.github.ScipioAM.scipio_fx.test.controllers;
    opens com.github.ScipioAM.scipio_fx.test.threads;

}