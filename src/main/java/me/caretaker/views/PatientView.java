package me.caretaker.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import me.caretaker.models.Patient;

import java.io.IOException;

public class PatientView {
    private final Scene scene;
    private Scene oldScene;

    private Stage stage;
    private Patient patient;

    private final TextField fieldId;
    private final TextField fieldName;
    private final TextField fieldPhone;

    public PatientView() {
        VBox root = new VBox();
        HBox boxActions = new HBox();
        GridPane gridDetails = new GridPane();

        Button buttonSave = new Button("Save");
        Button buttonBack = new Button("Cancel");

        boxActions.getChildren().add(buttonSave);
        boxActions.getChildren().add(buttonBack);
        boxActions.setAlignment(Pos.CENTER_RIGHT);

        root.getChildren().add(gridDetails);
        root.getChildren().add(boxActions);

        gridDetails.add(new Label("ID"), 0, 0);
        gridDetails.setPadding(new Insets(15));
        gridDetails.setHgap(10);
        gridDetails.setVgap(10);

        fieldId = new TextField();
        fieldId.setEditable(false);
        gridDetails.add(fieldId, 1, 0);

        gridDetails.add(new Label("Name"), 0, 1);
        fieldName = new TextField();
        gridDetails.add(fieldName, 1, 1);

        gridDetails.add(new Label("Phone"), 0, 2);
        fieldPhone = new TextField();
        gridDetails.add(fieldPhone, 1, 2);

        scene = new Scene(root);

        buttonSave.setOnAction(actionEvent -> {
            patient.setName(fieldName.getText());
            patient.setPhone(fieldPhone.getText());

            try {
                patient.save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            this.stage.setScene(oldScene);
        });

        buttonBack.setOnAction(actionEvent -> this.stage.setScene(oldScene));
    }

    public void update(Patient patient) {
        this.patient = patient;

        fieldId.setText(Long.toString(patient.getId()));
        fieldName.setText(patient.getName());
        fieldPhone.setText(patient.getPhone());
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
