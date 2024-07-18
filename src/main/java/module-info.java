module me.caretaker {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens me.caretaker to javafx.fxml, com.google.gson;
    exports me.caretaker;
}