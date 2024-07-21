package me.caretaker.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import me.caretaker.models.Address;
import me.caretaker.models.Appointment;
import me.caretaker.models.Patient;

import java.io.IOException;
import java.util.Date;

public class PatientView {
    private final VBox root;
    private Parent oldRoot;
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


    public PatientView() {
        root = new VBox();
        HBox boxActions = new HBox();
        HBox boxActionsLeft = new HBox();
        HBox boxActionsRight = new HBox();
        GridPane gridDetails = new GridPane();

        Button buttonSave = new Button("Save");
        Button buttonBack = new Button("Cancel");
        buttonBook = new Button("Schedule Appointment");

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

        root.getChildren().add(labelHeader);
        root.getChildren().add(gridDetails);
        root.getChildren().add(boxActions);

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

        buttonSave.setOnAction(actionEvent -> {
            patient.setName(fieldName.getText());
            patient.setPhone(fieldPhone.getText());
            patient.setAddress(new Address(
                    fieldStreet.getText(),
                    fieldCity.getText(),
                    fieldPostalCode.getText()
            ));
            patient.setDob(pickerDob.getValue());

            try {
                patient.save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

//            this.stage.setScene(oldScene);
            this.scene.setRoot(oldRoot);
        });

        buttonBack.setOnAction(actionEvent -> this.scene.setRoot(oldRoot));

        buttonBook.setOnAction(actionEvent -> {
            AppointmentView appointmentView = new AppointmentView();
            Appointment appointment = new Appointment();
            appointment.setPatientID(this.patient.getId());
            appointment.setDate(new Date());

            try {
                appointmentView.update(appointment);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            appointmentView.show(this.stage);
        });
    }

    public void create() {
        updateFields(new Patient());

        labelHeader.setText("New Patient");
        buttonBook.setDisable(true);
        pickerDob.setDisable(false);
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

        pickerDob.setValue(patient.getDobAsLocalDate());
    }

    public void show(Scene scene) {
//        oldScene = stage.getScene();
//        this.stage = stage;
//        this.stage.setScene(scene);

        oldRoot = scene.getRoot();
        this.scene = scene;
        this.scene.setRoot(root);
    }
}
