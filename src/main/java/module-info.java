module me.caretaker {
    requires javafx.controls;
    requires javafx.fxml;


    opens me.caretaker to javafx.fxml;
    exports me.caretaker;
}