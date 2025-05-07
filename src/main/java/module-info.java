module org.example.gpps_unrn {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive java.sql;
    requires mysql.connector.j;

    opens org.example.gpps_unrn to javafx.fxml;
    exports org.example.gpps_unrn;
    exports model;
    exports database;
    opens model to javafx.base;
}