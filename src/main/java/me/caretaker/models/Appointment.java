package me.caretaker.models;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Appointment {
    private long patient_id;
    private long id;
    private AppointmentType reason;
    private Date date;

    // Constructors, getters, and setters
    public Appointment() {
        this.id=System.currentTimeMillis();
    }

    public Appointment(long patient_id, AppointmentType reason, Date date) {
        this.patient_id=patient_id;
        this.reason = reason;
        this.date = date;
        this.id= System.currentTimeMillis();
    }

    public long getPatientID() {
        return patient_id;
    }

    public void setPatientID(long patient_id) {
        this.patient_id=patient_id;
    }
    public long getID(){
        return  id;
    }

    public AppointmentType getReason() {
        return reason;
    }

    public void setReason(AppointmentType reason) {
        this.reason = reason;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    // Save method to save appointment details to a JSON file
    public void save() throws IOException {
        String filename = "data/appointments/" + id + ".json";
        try (FileWriter writer = new FileWriter(filename)) {
            new Gson().toJson(this, writer);
        }
    }

    // Load method to load appointment details from a JSON file
    public static Appointment load(long id) throws IOException {
        String filename = "data/appointments/" + id + ".json";
        try (FileReader reader = new FileReader(filename)) {
            return new Gson().fromJson(reader, Appointment.class);
        }
    }
}
