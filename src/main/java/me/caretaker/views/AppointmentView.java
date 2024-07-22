package me.caretaker.views;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import me.caretaker.models.Appointment;
import me.caretaker.models.AppointmentType;
import me.caretaker.models.Patient;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;

public class AppointmentView {
    private final VBox root;
    private Node oldRoot;
    private Scene scene;

    private Appointment appointment;

    private final TextField fieldPatientId;
    private final TextField fieldPatientName;
    private final ComboBox<AppointmentType> comboReason;
    private final DatePicker datePicker;

    public AppointmentView() {
        root = new VBox();
        HBox boxActions = new HBox();
        GridPane gridDetails = new GridPane();

        Button buttonBook = new Button("Book Appointment");
        boxActions.setSpacing(5);
        boxActions.setPadding(new Insets( 15));
        Button buttonBack = new Button("Cancel");

        boxActions.getChildren().add(buttonBook);
        boxActions.getChildren().add(buttonBack);
        boxActions.setAlignment(Pos.CENTER_RIGHT);

        Label labelheader = new Label("Schedule Appointment ");
        labelheader.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        labelheader.setPadding(new Insets(15));

        root.getChildren().add(labelheader);
        root.getChildren().add(gridDetails);
        root.getChildren().add(boxActions);

        gridDetails.add(new Label("Patient ID"), 0, 0);
        gridDetails.setPadding(new Insets(15));
        gridDetails.setHgap(10);
        gridDetails.setVgap(10);

        fieldPatientId = new TextField();
        gridDetails.add(fieldPatientId, 1, 0);

        gridDetails.add(new Label("Patient Name"), 0, 1);
        fieldPatientName = new TextField();
        gridDetails.add(fieldPatientName, 1, 1);

        gridDetails.add(new Label("Appointment Reason"), 0, 2);
        comboReason = new ComboBox<>(FXCollections.observableArrayList(AppointmentType.values()));
        comboReason.setEditable(false);
        gridDetails.add(comboReason, 1, 2);

        gridDetails.add(new Label("Date"), 0, 3);
        datePicker = new DatePicker();
        gridDetails.add(datePicker, 1, 3);

        buttonBook.setOnAction(actionEvent -> {
            try {
                Patient patient = Patient.load(Long.parseLong(fieldPatientId.getText()));
                appointment = new Appointment();
                appointment.setPatientID(patient.getId());
                appointment.setReason(comboReason.getValue());
                Calendar calendar = Calendar.getInstance();
                calendar.set(
                        datePicker.getValue().getYear(),
                        datePicker.getValue().getMonthValue(),
                        datePicker.getValue().getDayOfMonth());

                appointment.setDate(calendar.getTime());

                appointment.save();
            } catch (IOException | NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to schedule an appointment");
                alert.setContentText("Please verify the patient's ID and the appointment date.");
                alert.showAndWait();
            }

//            this.stage.setScene(oldScene);
            ((ScrollPane) this.scene.getRoot()).setContent(oldRoot);

        });

        buttonBack.setOnAction(actionEvent -> ((ScrollPane) this.scene.getRoot()).setContent(oldRoot));
    }

    public void update(Appointment appointment) throws IOException {

        this.appointment = appointment;
        Calendar calender = Calendar.getInstance();
        calender.setTime(this.appointment.getDate());

        Patient patient = Patient.load(appointment.getPatientID());
        fieldPatientId.setText(Long.toString(patient.getId()));
        fieldPatientName.setText(patient.getName());
        comboReason.setValue(appointment.getReason());

        LocalDate localDate = LocalDate.of(
                calender.get(Calendar.YEAR),
                calender.get(Calendar.MONTH),
                calender.get(Calendar.DAY_OF_MONTH));
        datePicker.setValue(localDate);

    }

    public Parent getRoot() {
        return root;
    }

    public void show(Scene scene) {
//        oldScene = stage.getScene();
//        this.stage = stage;
//        this.stage.setScene(scene);

        oldRoot = ((ScrollPane) scene.getRoot()).getContent();
        this.scene = scene;
        ((ScrollPane) this.scene.getRoot()).setContent(root);
    }
}
