package me.caretaker;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import me.caretaker.models.Address;
import me.caretaker.models.Appointment;
import me.caretaker.models.Gender;
import me.caretaker.models.Patient;
import me.caretaker.views.AppointmentView;
import me.caretaker.views.PatientView;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main extends Application {

    private Stage primaryStage;
    private List<Patient> patients;
    private List<Appointment> appointments;

    // Views
    private PatientView patientView;
    private  AppointmentView appointmentView;

    @Override
    public void start(Stage primaryStage) {
        try {
            Files.createDirectories(Paths.get("data/patients"));
            Files.createDirectories(Paths.get("data/appointments"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.primaryStage = primaryStage;
        this.patients = new ArrayList<>();
        this.appointments = new ArrayList<>();
        primaryStage.setTitle("Healthcare Application");

        patientView = new PatientView();
        appointmentView = new AppointmentView();

        // Load patient data from file
        loadPatientsFromFile();

        // Load appointments data from file
        //loadAppointmentsFromFile();

        // Main Menu UI
        VBox mainMenuVBox = new VBox();
        mainMenuVBox.setAlignment(Pos.CENTER);
        mainMenuVBox.setSpacing(10);
        mainMenuVBox.setPadding(new Insets(20));

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> showLoginScreen());

        mainMenuVBox.getChildren().addAll(loginButton);

        Scene mainMenuScene = new Scene(mainMenuVBox, 400, 300);
        primaryStage.setScene(mainMenuScene);
        primaryStage.show();
    }

    private void showLoginScreen() {
        primaryStage.setScene(createLoginScene());
    }

    private Scene createLoginScene() {
        GridPane loginGrid = new GridPane();
        loginGrid.setAlignment(Pos.CENTER);
        loginGrid.setHgap(10);
        loginGrid.setVgap(10);
        loginGrid.setPadding(new Insets(25, 25, 25, 25));

        // Add UI controls for login screen
        TextField usernameTextField = new TextField();
        usernameTextField.setPromptText("Username");
        loginGrid.add(new Label("Username:"), 0, 0);
        loginGrid.add(usernameTextField, 1, 0);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        loginGrid.add(new Label("Password:"), 0, 1);
        loginGrid.add(passwordField, 1, 1);

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            String username = usernameTextField.getText();
            String password = passwordField.getText();
            // authentication
            if (isValidCredentials(username, password)) {
                showDashboard();
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
            }
        });
        loginGrid.add(loginButton, 1, 2);

        // Create scene for login screen
        Scene loginScene = new Scene(loginGrid, 400, 300);
        primaryStage.setScene(loginScene);

        // Show the primary stage
        primaryStage.show();
        return loginScene;
    }

    private void loadPatientsFromFile() {
//        File file = new File("patients.txt");
//        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                if (line.startsWith("Name: ")) {
//                    String name = line.substring("Name: ".length()).trim();
//                    int age = Integer.parseInt(reader.readLine().substring("Age: ".length()).trim());
//                    String medicalHistory = reader.readLine().substring("Medical History: ".length()).trim();
//                    String contactInfo = reader.readLine().substring("Contact Information: ".length()).trim();
//                    reader.readLine();
//                    Patient patient = new Patient(name, age, medicalHistory, contactInfo);
//                    patients.add(patient);
//                }
//            }
//        } catch (IOException e) {
//            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load patients: " + e.getMessage());
//        }

        patients = Patient.loadAll();
    }

//    private void loadAppointmentsFromFile() {
//        File file = new File("appointments.txt");
//        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                if (line.startsWith("Patient Name: ")) {
//                    String patientName = line.substring("Patient Name: ".length()).trim();
//                    String date = reader.readLine().substring("Date: ".length()).trim();
//                    String time = reader.readLine().substring("Time: ".length()).trim();
//                    String reason = reader.readLine().substring("Reason for Visit: ".length()).trim();
//                    reader.readLine();
//                    Appointment appointment = new Appointment(patientName, date, time, reason);
//                    appointments.add(appointment);
//                }
//            }
//        } catch (IOException e) {
//            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load appointments: " + e.getMessage());
//        }
//    }

    private void showDashboard() {
        VBox dashboardVBox = new VBox();
        dashboardVBox.setAlignment(Pos.CENTER);
        dashboardVBox.setSpacing(10);
        dashboardVBox.setPadding(new Insets(20));

        Label titleLabel = new Label("Dashboard");

        Label patientSummaryLabel = new Label("Total Patients: " + patients.size());

        Label appointmentSummaryLabel = new Label("Upcoming Appointments: " + appointments.size());

        Button viewPatientRecordsButton = new Button("Add Patient");
        viewPatientRecordsButton.setOnAction(event -> {
            patientView.update(new Patient());
            patientView.show(primaryStage);
        });
       viewPatientRecordsButton.setOnAction(e -> showPatientRecords());

        Button scheduleAppointmentButton = new Button("Schedule Appointment");
        scheduleAppointmentButton.setOnAction(e ->{

           appointmentView.show(primaryStage);
        } );

        Button viewPatientRecordsDetailsButton = new Button("View Patient Records");
        viewPatientRecordsDetailsButton.setOnAction(e -> {

//            readPatientsFromFile();
//            StringBuilder patientInfo = new StringBuilder();
//            for (Patient patient : patients) {
//                patientInfo.append("Name: ").append(patient.getName()).append("\n");
//                patientInfo.append("Age: ").append(patient.getAge()).append("\n");
//                patientInfo.append("Medical History: ").append(patient.getMedicalHistory()).append("\n");
//                patientInfo.append("Contact Information: ").append(patient.getPhone()).append("\n\n");
//            }
//            showScrollableAlert("Patient Records", patientInfo.toString());

            patientView.update(patients.getFirst());
            patientView.show(primaryStage);
        });

        Button viewScheduledAppointmentDetailsButton = new Button("View Scheduled Appointments");
        viewScheduledAppointmentDetailsButton.setOnAction(e -> {
            readAppointmentsFromFile();
            StringBuilder appointmentsInfo = new StringBuilder();
//            for (Appointment appointment : appointments) {
//                appointmentsInfo.append("Patient Name: ").append(appointment.getPatientName()).append("\n");
//                appointmentsInfo.append("Date: ").append(appointment.getDate()).append("\n");
//                appointmentsInfo.append("Time: ").append(appointment.getTime()).append("\n");
//                appointmentsInfo.append("Reason for Visit: ").append(appointment.getReason()).append("\n\n");
//            }
            showScrollableAlert("Scheduled Appointments", appointmentsInfo.toString());
        });


        dashboardVBox.getChildren().addAll(titleLabel, patientSummaryLabel, appointmentSummaryLabel,
                viewPatientRecordsButton, scheduleAppointmentButton,
                viewPatientRecordsDetailsButton, viewScheduledAppointmentDetailsButton);

        Scene dashboardScene = new Scene(dashboardVBox, 600, 400);
        primaryStage.setScene(dashboardScene);
        primaryStage.show();
    }



    private void showPatientRecords() {
        GridPane patientFormGrid = new GridPane();
        patientFormGrid.setAlignment(Pos.CENTER);
        patientFormGrid.setHgap(10);
        patientFormGrid.setVgap(10);
        patientFormGrid.setPadding(new Insets(25, 25, 25, 25));

        TextField nameTextField = new TextField();
        nameTextField.setPromptText("Patient Name");
        patientFormGrid.add(new Label("Name:"), 0, 0);
        patientFormGrid.add(nameTextField, 1, 0);

        TextField ageTextField = new TextField();
        ageTextField.setPromptText("Age");
        patientFormGrid.add(new Label("Age:"), 0, 1);
        patientFormGrid.add(ageTextField, 1, 1);

        TextField medicalHistoryTextField = new TextField();
        medicalHistoryTextField.setPromptText("Medical History");
        patientFormGrid.add(new Label("Medical History:"), 0, 2);
        patientFormGrid.add(medicalHistoryTextField, 1, 2);

        TextField contactTextField = new TextField();
        contactTextField.setPromptText("Contact Information");
        patientFormGrid.add(new Label("Contact Information:"), 0, 3);
        patientFormGrid.add(contactTextField, 1, 3);

        Button addPatientButton = new Button("Add Patient");
        addPatientButton.setOnAction(e -> {
            String name = nameTextField.getText();
            String age = ageTextField.getText();
            String medicalHistory = medicalHistoryTextField.getText();
            String contactInfo = contactTextField.getText();

            // TODO: Replace dummy attributes

            Patient patient = null;
            try {
                patient = new Patient(
                        name,
                        Integer.parseInt(age),
                        new Date(),
                        Gender.M,
                        new Address("5600 Yonge Street", "North York", "M2N5S2"),
                        medicalHistory,
                        contactInfo);
                patients.add(patient);
                patient.save();
            } catch (IllegalArgumentException | IOException ex) {
                showAlert(Alert.AlertType.ERROR, "Error", ex.getMessage());
            }

//            savePatientsToFile();
            showAlert(Alert.AlertType.INFORMATION, "Patient Added", "Patient record added successfully.");

            // After adding patient and saving data, show the dash board
            showDashboard();
        });
        patientFormGrid.add(addPatientButton, 1, 4);

        Scene patientFormScene = new Scene(patientFormGrid, 400, 300);
        primaryStage.setScene(patientFormScene);
        primaryStage.show();
    }


//    private void showAppointments() {
//        GridPane appointmentFormGrid = new GridPane();
//        appointmentFormGrid.setAlignment(Pos.CENTER);
//        appointmentFormGrid.setHgap(10);
//        appointmentFormGrid.setVgap(10);
//        appointmentFormGrid.setPadding(new Insets(25, 25, 25, 25));
//
//        TextField patientNameTextField = new TextField();
//        patientNameTextField.setPromptText("Patient Name");
//        appointmentFormGrid.add(new Label("Patient Name:"), 0, 0);
//        appointmentFormGrid.add(patientNameTextField, 1, 0);
//
//        TextField dateTextField = new TextField();
//        dateTextField.setPromptText("Date");
//        appointmentFormGrid.add(new Label("Date:"), 0, 1);
//        appointmentFormGrid.add(dateTextField, 1, 1);
//
//        TextField timeTextField = new TextField();
//        timeTextField.setPromptText("Time");
//        appointmentFormGrid.add(new Label("Time:"), 0, 2);
//        appointmentFormGrid.add(timeTextField, 1, 2);
//
//        TextField reasonTextField = new TextField();
//        reasonTextField.setPromptText("Reason for Visit");
//        appointmentFormGrid.add(new Label("Reason for Visit:"), 0, 3);
//        appointmentFormGrid.add(reasonTextField, 1, 3);
//
//        Button scheduleAppointmentButton = new Button("Schedule Appointment");
//        scheduleAppointmentButton.setOnAction(e -> {
//            String patientName = patientNameTextField.getText();
//            String date = dateTextField.getText();
//            String time = timeTextField.getText();
//            String reason = reasonTextField.getText();
//
//            Appointment appointment = new Appointment(patientName, date, time, reason);
//            appointments.add(appointment);
//
//            saveAppointmentToFile(appointment);
//            showAlert(Alert.AlertType.INFORMATION, "Appointment Scheduled", "Appointment scheduled successfully.");
//
//            // After scheduling appointment and saving data, show the dash board
//            showDashboard();
//        });
//        appointmentFormGrid.add(scheduleAppointmentButton, 1, 4);
//
//        Scene appointmentFormScene = new Scene(appointmentFormGrid, 400, 300);
//        primaryStage.setScene(appointmentFormScene);
//        primaryStage.show();
//    }


    private boolean isValidCredentials(String username, String password) {
        //  validation for login
        return username.equals("admin") && password.equals("admin");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showScrollableAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);

        // scrollable TextArea to display content
        TextArea textArea = new TextArea(content);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        // TextArea to ScrollPane
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(textArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        // Set content of alert to ScrollPane
        alert.getDialogPane().setContent(scrollPane);

        alert.showAndWait();
    }

    private void savePatientsToFile() {
//        File file = new File("patients.txt");
//        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
//            for (Patient patient : patients) {
//                writer.println("Name: " + patient.getName());
//                writer.println("Age: " + patient.getAge());
//                writer.println("Medical History: " + patient.getMedicalHistory());
//                writer.println("Contact Information: " + patient.getPhone());
//                writer.println();
//            }
//            showAlert(Alert.AlertType.INFORMATION, "Patients Saved", "Patient records saved successfully.");
//        } catch (IOException e) {
//            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save patients: " + e.getMessage());
//        }


    }


    private void saveAppointmentToFile(Appointment appointment) {
        File file = new File("appointments.txt");
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
            //writer.println("Patient Name: " + appointment.getPatientName());
            writer.println("Date: " + appointment.getDate());
            //writer.println("Time: " + appointment.getTime());
            writer.println("Reason for Visit: " + appointment.getReason());
            writer.println();
            showAlert(Alert.AlertType.INFORMATION, "Appointments Saved", "Appointment records saved successfully.");
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to save appointments: " + e.getMessage());
        }
    }

    private void readPatientsFromFile() {
        patients.clear(); // Clear the existing list of patients
//        File file = new File("patients.txt");
//        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                if (line.startsWith("Name: ")) {
//                    String name = line.substring(6);
//                    int age = Integer.parseInt(reader.readLine().substring(5));
//                    String medicalHistory = reader.readLine().substring(17);
//                    String contactInfo = reader.readLine().substring(21);
//                    patients.add(new Patient(name, age, medicalHistory, contactInfo));
//                    reader.readLine();
//                }
//            }
//        } catch (IOException e) {
//            showAlert(Alert.AlertType.ERROR, "Error", "Failed to read patients: " + e.getMessage());
//        }

        patients = Patient.loadAll();
    }


    private void readAppointmentsFromFile() {
        appointments.clear(); // Clear the existing list of appointments
        File file = new File("appointments.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Patient Name: ")) {
                    String patientName = line.substring(14);
                    String date = reader.readLine().substring(6);
                    String time = reader.readLine().substring(6);
                    String reason = reader.readLine().substring(18);
                    //appointments.add(new Appointment(patientName, date, time, reason));
                    reader.readLine();
                }
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to read appointments: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}




