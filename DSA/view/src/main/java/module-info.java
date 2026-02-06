module org.example.view {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.example.model;
    requires org.apache.commons.lang3;
    requires java.desktop;
    requires org.apache.logging.log4j;
    requires java.sql;

    opens org.example.view to javafx.fxml;
    exports org.example.view;
}
