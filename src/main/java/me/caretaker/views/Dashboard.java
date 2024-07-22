package me.caretaker.views;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import me.caretaker.models.Patient;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class Dashboard {
    private final VBox root;
    private Node oldRoot;
    private Scene scene;

    private final TextField fieldPatientId;
    private final TextField fieldName;

    private final PatientView patientView;
    private final DatePicker pickerDob;

    public Dashboard() {
        patientView = new PatientView();

        root = new VBox();
        VBox boxContent = new VBox();
        HBox boxActions = new HBox();

        // Title header
        Label labelMainHeader = new Label("CareJ");
        labelMainHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 32px; -fx-background-color: black; -fx-text-fill: white;");
        labelMainHeader.setMaxWidth(Double.MAX_VALUE);
        labelMainHeader.setPadding(new Insets(15, 15, 10, 15));

        // New admission
        VBox boxAdmit = new VBox();
        boxAdmit.setPadding(new Insets(15));
        boxAdmit.setSpacing(10);

        Label labelNewAdmit = new Label("New Admission");
        labelNewAdmit.setStyle("-fx-font-weight: bold; -fx-font-size: 18px");

        Button buttonAdmit = new Button("Create a new patient record");
        buttonAdmit.setOnAction(actionEvent -> {
            patientView.create();
            patientView.show(scene);
        });

        boxAdmit.getChildren().add(labelNewAdmit);
        boxAdmit.getChildren().add(buttonAdmit);

        // Search
        Label labelHeader = new Label("Search for patient");
        labelHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        labelHeader.setPadding(new Insets(15, 15, 5, 15));

        GridPane gridSearch = new GridPane();
        gridSearch.setPadding(new Insets(15));
        gridSearch.setHgap(10);
        gridSearch.setVgap(10);
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(25);
        gridSearch.getColumnConstraints().add(col1);

        gridSearch.add(new Label("Patient ID"), 0, 0);
        fieldPatientId = new TextField();
        gridSearch.add(fieldPatientId, 1, 0);
        GridPane.setHgrow(fieldPatientId, Priority.ALWAYS);

        Label labelOr = new Label("OR");
        labelOr.setStyle("-fx-font-size: 14px;");
        gridSearch.add(labelOr, 0, 1);
        GridPane.setHalignment(labelOr, HPos.CENTER);
        GridPane.setColumnSpan(labelOr, GridPane.REMAINING);

        gridSearch.add(new Label("Patient Name"), 0, 2);
        fieldName = new TextField();
        gridSearch.add(fieldName, 1, 2);

        gridSearch.add(new Label("Patient DOB"), 0, 3);
        pickerDob = new DatePicker();
        gridSearch.add(pickerDob, 1, 3);

        Button buttonSearch = new Button("Search");
        buttonSearch.setOnAction(actionEvent -> {
            long patientId = (fieldPatientId.getText().isEmpty()) ? -1 : Long.parseLong(fieldPatientId.getText());
            String patientName = fieldName.getText();
            LocalDate dobLocal = pickerDob.getValue();
            Patient patient = null;

            if (patientId != -1) {
                try {
                    patient = Patient.load(patientId);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                List<Patient> patients = Patient.loadAll();
                for (Patient p : patients) {
                    if (p.getName().equalsIgnoreCase(patientName) && p.getDobAsLocalDate().equals(dobLocal)) {
                        patient = p;
                        break;
                    }
                }
            }

            if (patient != null) {
                fieldName.clear();
                fieldPatientId.clear();
                pickerDob.setValue(null);

                patientView.update(patient);
                patientView.show(scene);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("No patients found!");
                alert.setContentText("There were no patients found matching the information given. " + "Please check if the information entered is correct.");
                alert.showAndWait();
            }
        });

        boxActions.setSpacing(5);
        boxActions.setAlignment(Pos.CENTER);

        boxActions.getChildren().add(buttonSearch);
        boxActions.setPadding(new Insets(5, 15, 15, 15));
        boxActions.setAlignment(Pos.CENTER_RIGHT);

        // Appointment schedule
        Label labelHeaderAppt = new Label("Schedule an appointment");
        labelHeaderAppt.setStyle("-fx-font-weight: bold; -fx-font-size: 18px");
        labelHeaderAppt.setPadding(new Insets(15, 15, 5, 15));
        AppointmentView appointmentView = new AppointmentView();

        root.getChildren().add(labelMainHeader);
        root.getChildren().add(boxContent);

        boxContent.getChildren().add(boxAdmit);
        boxContent.getChildren().add(labelHeader);
        boxContent.getChildren().add(gridSearch);
        boxContent.getChildren().add(boxActions);
        //boxContent.getChildren().add(labelHeaderAppt);
       // boxContent.getChildren().add(appointmentView.getRoot());

        boxContent.setPadding(new Insets(25, 150, 25, 150));
        for (Node node : boxContent.getChildren())
            VBox.setVgrow(node, Priority.ALWAYS);
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
