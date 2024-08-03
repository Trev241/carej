package me.caretaker.database;

import me.caretaker.models.Appointment;
import me.caretaker.models.AppointmentType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAppointments {
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

    public static void saveAppointment(Appointment appointment) throws SQLException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String selectSQL = "SELECT COUNT(*) FROM Appointments WHERE id = ?";
            try (PreparedStatement selectStatement = connection.prepareStatement(selectSQL)) {
                selectStatement.setLong(1, appointment.getID());
                try (ResultSet resultSet = selectStatement.executeQuery()) {
                    resultSet.next();
                    int count = resultSet.getInt(1);
                    if (count == 0) {
                        String insertSQL = "INSERT INTO Appointments (id, patient_id, reason, appointment_timestamp) " +
                                "VALUES (?, ?, ?, ?)";
                        try (PreparedStatement insertStatement = connection.prepareStatement(insertSQL)) {
                            insertStatement.setLong(1, appointment.getID());
                            insertStatement.setLong(2, appointment.getPatientID());
                            insertStatement.setString(3, appointment.getReason() != null ? appointment.getReason().toString() : null);
                            insertStatement.setTimestamp(4, appointment.getTimestamp()); // Updated method name
                            insertStatement.executeUpdate();
                        }
                    } else {
                        String updateSQL = "UPDATE Appointments SET patient_id = ?, reason = ?, appointment_timestamp = ? WHERE id = ?";
                        try (PreparedStatement updateStatement = connection.prepareStatement(updateSQL)) {
                            updateStatement.setLong(1, appointment.getPatientID());
                            updateStatement.setString(2, appointment.getReason() != null ? appointment.getReason().toString() : null);
                            updateStatement.setTimestamp(3, appointment.getTimestamp()); // Updated method name
                            updateStatement.setLong(4, appointment.getID());
                            updateStatement.executeUpdate();
                        }
                    }
                }
            }
        }
    }

    public static Appointment loadAppointment(long id) throws SQLException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "SELECT * FROM Appointments WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, id);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        Appointment appointment = new Appointment();
                        appointment.setPatientID(resultSet.getLong("patient_id"));
                        appointment.setReason(AppointmentType.valueOf(resultSet.getString("reason")));
                        appointment.setTimestamp(resultSet.getTimestamp("appointment_timestamp")); // Updated method name
                        return appointment;
                    } else {
                        throw new SQLException("Appointment not found with id: " + id);
                    }
                }
            }
        }
    }

    public static List<Appointment> loadAllAppointments() throws SQLException {
        List<Appointment> appointments = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            String sql = "SELECT * FROM Appointments";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    Appointment appointment = new Appointment();
                    appointment.setPatientID(resultSet.getLong("patient_id"));
                    appointment.setReason(AppointmentType.valueOf(resultSet.getString("reason")));
                    appointment.setTimestamp(resultSet.getTimestamp("appointment_timestamp")); // Updated method name
                    appointments.add(appointment);
                }
            }
        }
        return appointments;
    }
}
