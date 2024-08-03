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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.stream.IntStream;

public class AppointmentView {
    private final VBox root;
    private Node oldRoot;
    private Scene scene;

    private Appointment appointment;

    private final TextField fieldPatientId;
    private final TextField fieldPatientName;
    private final ComboBox<AppointmentType> comboReason;
    private final DatePicker datePicker;
    private final ComboBox<Integer> comboHour;
    private final ComboBox<Integer> comboMinute;

    public AppointmentView() {
        root = new VBox();
        HBox boxActions = new HBox();
        GridPane gridDetails = new GridPane();

        Button buttonBook = new Button("Book Appointment");
        boxActions.setSpacing(5);
        boxActions.setPadding(new Insets(15));
        Button buttonBack = new Button("Cancel");

        boxActions.getChildren().add(buttonBook);
        boxActions.getChildren().add(buttonBack);
        boxActions.setAlignment(Pos.CENTER_RIGHT);

        Label labelHeader = new Label("Schedule Appointment ");
        labelHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        labelHeader.setPadding(new Insets(15));

        root.getChildren().add(labelHeader);
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

        gridDetails.add(new Label("Time"), 0, 4);
        HBox timeBox = new HBox(5);
        comboHour = new ComboBox<>(FXCollections.observableArrayList(IntStream.range(0, 24).boxed().toArray(Integer[]::new)));
        comboMinute = new ComboBox<>(FXCollections.observableArrayList(IntStream.range(0, 60).boxed().toArray(Integer[]::new)));
        comboHour.setEditable(false);
        comboMinute.setEditable(false);
        timeBox.getChildren().addAll(comboHour, new Label(":"), comboMinute);
        gridDetails.add(timeBox, 1, 4);

        buttonBook.setOnAction(actionEvent -> {
            try {
                Patient patient = Patient.load(Long.parseLong(fieldPatientId.getText()));
                appointment = new Appointment();
                appointment.setPatientID(patient.getId());
                appointment.setReason(comboReason.getValue());

                LocalDate localDate = datePicker.getValue();
                Calendar calendar = Calendar.getInstance();
                calendar.set(
                        localDate.getYear(),
                        localDate.getMonthValue() - 1,
                        localDate.getDayOfMonth(),
                        comboHour.getValue(),
                        comboMinute.getValue()
                );

                appointment.setTimestamp(new Timestamp(calendar.getTimeInMillis()));

                appointment.save();
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to schedule an appointment");
                alert.setContentText("Please verify the patient's ID and the appointment date.");
                alert.showAndWait();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to schedule an appointment");
                alert.setContentText("A database error occurred.");
                alert.showAndWait();
            }

            ((ScrollPane) this.scene.getRoot()).setContent(oldRoot);
        });

        buttonBack.setOnAction(actionEvent -> ((ScrollPane) this.scene.getRoot()).setContent(oldRoot));
    }

    public void update(Appointment appointment) throws IOException, SQLException {
        this.appointment = appointment;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.appointment.getTimestamp());

        Patient patient = Patient.load(appointment.getPatientID());
        fieldPatientId.setText(Long.toString(patient.getId()));
        fieldPatientName.setText(patient.getName());
        comboReason.setValue(appointment.getReason());

        LocalDate localDate = LocalDate.of(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.setValue(localDate);

        comboHour.setValue(calendar.get(Calendar.HOUR_OF_DAY));
        comboMinute.setValue(calendar.get(Calendar.MINUTE));
    }

    public Parent getRoot() {
        return root;
    }

    public void show(Scene scene) {
        oldRoot = ((ScrollPane) scene.getRoot()).getContent();
        this.scene = scene;
        ((ScrollPane) this.scene.getRoot()).setContent(root);
    }
}
