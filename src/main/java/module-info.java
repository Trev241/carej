module me.caretaker {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires javafx.base;
    requires java.sql;
    requires com.oracle.database.jdbc;


    opens me.caretaker to javafx.fxml, com.google.gson;
    exports me.caretaker;
    exports me.caretaker.models;
    opens me.caretaker.models to com.google.gson, javafx.fxml;
    exports me.caretaker.database;
    opens me.caretaker.database to com.google.gson, javafx.fxml;
}