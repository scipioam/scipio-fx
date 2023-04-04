open module scipio_fx_test {

    requires org.controlsfx.controls;
    requires org.yaml.snakeyaml;
    requires org.slf4j;

    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive MaterialFX;
    requires transitive static lombok;
    requires transitive jakarta.persistence;
    requires transitive org.junit.jupiter.api;

    requires com.github.scipio.scipio_fx;

}