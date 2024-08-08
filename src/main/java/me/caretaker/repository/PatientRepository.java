package me.caretaker.repository;

import me.caretaker.models.Address;
import me.caretaker.models.Gender;
import me.caretaker.models.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientRepository {
    private static final String JDBC_URL = "jdbc:oracle:thin:@calvin.humber.ca:1521:grok";
    private static final String JDBC_USER = "n01661510";
    private static final String JDBC_PASSWORD = "oracle";

    static {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
            System.out.println("Driver Loaded");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void save(Patient patient) throws SQLException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String selectSQL = "SELECT COUNT(*) FROM Patient WHERE id = ?";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectSQL)) {
                selectStatement.setLong(1, patient.getId());
                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    resultSet.next();
                    int count = resultSet.getInt(1);
                    if (count == 0) {
                        String insertSQL = "INSERT INTO Patient (id, name, age, dob, gender, address, medicalHistory, phone, notes) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement insertStatement = connection.prepareStatement(insertSQL)) {
                            insertStatement.setLong(1, patient.getId());
                            insertStatement.setString(2, patient.getName());
                            insertStatement.setInt(3, patient.getAge());
                            insertStatement.setDate(4, patient.getDob());
                            insertStatement.setString(5, patient.getGender() != null ? patient.getGender().toString() : null);
                            insertStatement.setString(6, patient.getAddress() != null ? patient.getAddress().toString() : null);
                            insertStatement.setString(7, patient.getMedicalHistory());
                            insertStatement.setString(8, patient.getPhone());
                            insertStatement.setString(9, patient.getNotes());
                            insertStatement.executeUpdate();
                        }
                    } else {
                        String updateSQL = "UPDATE Patient SET name = ?, age = ?, dob = ?, gender = ?, address = ?, " +
                                "medicalHistory = ?, phone = ?, notes = ? WHERE id = ?";
                        try (PreparedStatement updateStatement = connection.prepareStatement(updateSQL)) {
                            updateStatement.setString(1, patient.getName());
                            updateStatement.setInt(2, patient.getAge());
                            updateStatement.setDate(3, patient.getDob());
                            updateStatement.setString(4, patient.getGender() != null ? patient.getGender().toString() : null);
                            updateStatement.setString(5, patient.getAddress() != null ? patient.getAddress().toString() : null);
                            updateStatement.setString(6, patient.getMedicalHistory());
                            updateStatement.setString(7, patient.getPhone());
                            updateStatement.setString(8, patient.getNotes());
                            updateStatement.setLong(9, patient.getId());
                            updateStatement.executeUpdate();
                        }
                    }
                }
            }
        }
    }

    public static Patient one(long id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "SELECT * FROM Patient WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        Patient patient = new Patient();
                        patient.setId(resultSet.getLong("id"));
                        patient.setName(resultSet.getString("name"));
                        patient.setAge(resultSet.getInt("age"));
                        patient.setDob(resultSet.getDate("dob"));

                        // Use Gender.fromString to handle null or invalid values
                        String genderStr = resultSet.getString("gender");
                        patient.setGender(Gender.fromString(genderStr));

                        String[] address = resultSet.getString("address").split("\\|");
                        patient.setAddress(new Address(address[0], address[1], address[2]));
                        patient.setMedicalHistory(resultSet.getString("medicalHistory"));
                        patient.setPhone(resultSet.getString("phone"));
                        patient.setNotes(resultSet.getString("notes"));
                        return patient;
                    } else {
                        throw new SQLException("Patient not found with id: " + id);
                    }
                }
            }
        }
    }

    public static List<Patient> all() throws SQLException {
        List<Patient> patients = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "SELECT * FROM Patient";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    Patient patient = new Patient();
                    patient.setId(resultSet.getLong("id"));
                    patient.setName(resultSet.getString("name"));
                    patient.setAge(resultSet.getInt("age"));
                    patient.setDob(resultSet.getDate("dob"));
                    // Use Gender.fromString to handle null or invalid values
                    String genderStr = resultSet.getString("gender");
                    patient.setGender(Gender.fromString(genderStr));
                    String[] address = resultSet.getString("address").split("\\|");
                    patient.setAddress(new Address(address[0], address[1], address[2]));
                    patient.setMedicalHistory(resultSet.getString("medicalHistory"));
                    patient.setPhone(resultSet.getString("phone"));
                    patient.setNotes(resultSet.getString("notes"));
                    patients.add(patient);
                }
            }
        }
        return patients;
    }
}
