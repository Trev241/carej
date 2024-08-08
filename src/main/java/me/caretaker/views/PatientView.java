package me.caretaker.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import me.caretaker.models.Address;
import me.caretaker.models.Appointment;
import me.caretaker.models.Patient;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class PatientView {
    private final VBox root;
    private Node oldRoot;
    private Scene scene;

    private Stage stage;
    private Patient patient;
    private final Label labelHeader;
    private final Button buttonBook;
    private final DatePicker pickerDob;

    private final TextField fieldId;
    private final TextField fieldName;
    private final TextField fieldPhone;
    private final TextField fieldStreet;
    private final TextField fieldCity;
    private final TextField fieldPostalCode;
    private final TextArea areaHistory;

    private VBox boxAppointments;

    private List<Appointment> appointments;

    public PatientView() {
        root = new VBox();
        HBox boxActions = new HBox();
        HBox boxActionsLeft = new HBox();
        HBox boxActionsRight = new HBox();
        GridPane gridDetails = new GridPane();

        Button buttonSave = new Button("Save");
        Button buttonBack = new Button("Cancel");
        buttonBook = new Button("Schedule Appointment");

        boxAppointments = new VBox();

        boxActions.getChildren().add(boxActionsLeft);
        boxActions.getChildren().add(boxActionsRight);
        boxActions.setPadding(new Insets(15));
        boxActions.setSpacing(20);
        HBox.setHgrow(boxActionsRight, Priority.ALWAYS);

        boxActionsRight.getChildren().add(buttonBack);
        boxActionsRight.getChildren().add(buttonSave);
        boxActionsRight.setSpacing(5);
        boxActionsRight.setAlignment(Pos.CENTER_RIGHT);

        boxActionsLeft.getChildren().add(buttonBook);
        boxActionsLeft.setAlignment(Pos.CENTER_LEFT);

        labelHeader = new Label();
        labelHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 24px");
        labelHeader.setPadding(new Insets(15));

        root.setPadding(new Insets(25, 150, 25, 150));
        root.getChildren().add(labelHeader);
        root.getChildren().add(gridDetails);
        root.getChildren().add(boxActions);
        root.getChildren().add(boxAppointments);

        root.setMaxHeight(Double.MAX_VALUE);
        for (Node node : root.getChildren())
            VBox.setVgrow(node, Priority.ALWAYS);

        gridDetails.setPadding(new Insets(15));
        gridDetails.setHgap(10);
        gridDetails.setVgap(10);

        Label labelPersonal = new Label("Personal");
        labelPersonal.setStyle("-fx-font-weight: bold; -fx-font-size: 18px");
        gridDetails.add(labelPersonal, 0, 0);
        GridPane.setColumnSpan(labelPersonal, GridPane.REMAINING);

        gridDetails.add(new Label("ID"), 0, 1);
        fieldId = new TextField();
        fieldId.setEditable(false);
        fieldId.setDisable(true);
        gridDetails.add(fieldId, 1, 1);
        GridPane.setHgrow(fieldId, Priority.ALWAYS);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(25);
        gridDetails.getColumnConstraints().add(col1);

        gridDetails.add(new Label("Name"), 0, 2);
        fieldName = new TextField();
        gridDetails.add(fieldName, 1, 2);

        gridDetails.add(new Label("Date of Birth"), 0, 3);
        pickerDob = new DatePicker();
        gridDetails.add(pickerDob, 1, 3);

        gridDetails.add(new Label("Phone"), 0, 4);
        fieldPhone = new TextField();
        gridDetails.add(fieldPhone, 1, 4);

        Label labelAddress = new Label("Address");
        labelAddress.setStyle("-fx-font-weight: bold; -fx-font-size: 18px");
        gridDetails.add(labelAddress, 0, 5);
        GridPane.setColumnSpan(labelAddress, GridPane.REMAINING);

        gridDetails.add(new Label("Street"), 0, 6);
        fieldStreet = new TextField();
        gridDetails.add(fieldStreet, 1, 6);

        gridDetails.add(new Label("City"), 0, 7);
        fieldCity = new TextField();
        gridDetails.add(fieldCity, 1, 7);

        gridDetails.add(new Label("Postal Code"), 0, 8);
        fieldPostalCode = new TextField();
        gridDetails.add(fieldPostalCode, 1, 8);

        Label labelHistory = new Label("Medical History");
        labelHistory.setStyle("-fx-font-weight: bold; -fx-font-size: 18px");
        gridDetails.add(labelHistory, 0, 9);
        GridPane.setColumnSpan(labelHistory, GridPane.REMAINING);

        areaHistory = new TextArea();
        gridDetails.add(areaHistory, 0, 10);
        GridPane.setColumnSpan(areaHistory, GridPane.REMAINING);

        buttonSave.setOnAction(actionEvent -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to create patient record");

            try {
                patient.setName(fieldName.getText());
                patient.setPhone(fieldPhone.getText());
                patient.setAddress(new Address(fieldStreet.getText(), fieldCity.getText(), fieldPostalCode.getText()));
                patient.setDob(java.sql.Date.valueOf(pickerDob.getValue()));
                patient.setMedicalHistory(areaHistory.getText());

                patient.save();
                ((ScrollPane) this.scene.getRoot()).setContent(oldRoot);
            } catch (IllegalArgumentException e) {
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        buttonBack.setOnAction(actionEvent -> ((ScrollPane) this.scene.getRoot()).setContent(oldRoot));

        buttonBook.setOnAction(actionEvent -> {
            AppointmentView appointmentView = new AppointmentView();
            Appointment appointment = new Appointment();
            appointment.setPatientID(this.patient.getId());
            // Use the setDateAndTime method with current date and default time (e.g., 00:00)
            appointment.setDateAndTime(new java.sql.Date(new Date().getTime()), 0, 0);

            try {
                appointmentView.update(appointment);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            appointmentView.show(scene);
        });
    }

    public void create() {
        updateFields(new Patient());

        labelHeader.setText("New Patient");
        buttonBook.setDisable(true);
        pickerDob.setDisable(false);

        // Explicitly set DatePicker to null or a default value if needed
        pickerDob.setValue(null);
    }

    public void update(Patient patient) {
        updateFields(patient);

        labelHeader.setText("Update Existing Patient");
        pickerDob.setDisable(true);
        buttonBook.setDisable(false);
    }

    private void updateFields(Patient patient) {
        this.patient = patient;

        fieldId.setText(Long.toString(patient.getId()));
        fieldName.setText(patient.getName());
        fieldPhone.setText(patient.getPhone());

        if (patient.getAddress() == null) {
            patient.setAddress(new Address());
        }

        fieldStreet.setText(patient.getAddress().street);
        fieldCity.setText(patient.getAddress().city);
        fieldPostalCode.setText(patient.getAddress().postalCode);

        pickerDob.setValue(patient.getDobAsLocalDate() != null ? patient.getDobAsLocalDate() : null);
        areaHistory.setText(patient.getMedicalHistory());

        // Display all appointments
        appointments = Appointment.loadAll();
        boxAppointments.getChildren().clear();
        for (Appointment appointment : appointments) {
            AppointmentView appointmentView = new AppointmentView(true);
            try {
                appointmentView.update(appointment);
            } catch (IOException | SQLException e) {
                throw new RuntimeException();
            }

            boxAppointments.getChildren().add(appointmentView.getRoot());
        }
    }

    public void show(Scene scene) {
//        oldRoot = scene.getRoot();
//        this.scene = scene;
//        this.scene.setRoot(root);

        oldRoot = ((ScrollPane) scene.getRoot()).getContent();
        this.scene = scene;
        ((ScrollPane) this.scene.getRoot()).setContent(root);
    }
}
