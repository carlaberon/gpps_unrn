module org.example.gpps_unrn {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive java.sql;
	requires java.desktop;
    exports model;
    exports database;
    opens model to javafx.base;

}