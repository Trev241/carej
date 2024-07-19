package me.caretaker.views;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import me.caretaker.models.Patient;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class Dashboard {
    private Scene scene;
    private Scene oldScene;
    private Stage stage;

    private TextField fieldPatientId;
    private TextField fieldName;

    private PatientView patientView;
    private DatePicker pickerDob;

    public Dashboard() {
        patientView = new PatientView();

        VBox root = new VBox();
        HBox boxActions = new HBox();

        // Title header
        Label labelMainHeader = new Label("CareJ");
        labelMainHeader.setStyle("-fx-font-weight: bold; -fx-font-size: 24px");
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
            patientView.show(stage);
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
                    if (p.getName().equalsIgnoreCase(patientName) &&
                        p.getDobAsLocalDate().equals(dobLocal)) {
                        patient = p;
                        break;
                    }
                }
            }

            if (patient != null ) {
                patientView.update(patient);
                patientView.show(stage);
            }
        });

        boxActions.setSpacing(5);
        boxActions.setAlignment(Pos.CENTER);

        boxActions.getChildren().add(buttonSearch);
        boxActions.setPadding(new Insets(5, 15, 15, 15));
        boxActions.setAlignment(Pos.CENTER_RIGHT);

        root.getChildren().add(labelMainHeader);
        root.getChildren().add(boxAdmit);
        root.getChildren().add(labelHeader);
        root.getChildren().add(gridSearch);
        root.getChildren().add(boxActions);

        scene = new Scene(root);
    }

    public void show(Stage stage) {
        oldScene = stage.getScene();
        this.stage = stage;
        this.stage.setScene(scene);
    }

    public Scene getScene() {
        return scene;
    }
}
