module com.github.scipioam.scipiofx {

    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive org.yaml.snakeyaml;

    requires static org.controlsfx.controls;
    requires static MaterialFX;

    requires org.slf4j;
    requires static lombok;

    requires static java.sql;
    requires static com.zaxxer.hikari;
    requires static org.mybatis;
    requires static org.mybatis.generator;

    exports com.github.scipioam.scipiofx.framework;
    exports com.github.scipioam.scipiofx.framework.concurrent;
    exports com.github.scipioam.scipiofx.framework.config;
    exports com.github.scipioam.scipiofx.framework.exception;
    exports com.github.scipioam.scipiofx.framework.fxml;
    exports com.github.scipioam.scipiofx.utils;
    exports com.github.scipioam.scipiofx.view;
    exports com.github.scipioam.scipiofx.view.dialog;
    exports com.github.scipioam.scipiofx.view.table;
    exports com.github.scipioam.scipiofx.view.table.annotations;
    exports com.github.scipioam.scipiofx.view.table.cell;
    exports com.github.scipioam.scipiofx.mybatis;
    exports com.github.scipioam.scipiofx.mybatis.ext;
    exports com.github.scipioam.scipiofx.controlsfx;
    exports com.github.scipioam.scipiofx.controlsfx.table;
    exports com.github.scipioam.scipiofx.materialfx;
    exports com.github.scipioam.scipiofx.materialfx.dialog;
    exports com.github.scipioam.scipiofx.materialfx.table;

}