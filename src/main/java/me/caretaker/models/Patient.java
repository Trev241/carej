package me.caretaker.models;

import me.caretaker.database.Database;

import java.io.Serializable;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Patient implements Serializable {
    private long id;
    private int age;
    private Gender gender;
    private Address address;
    private Date dob;  // java.sql.Date
    private String name;
    private String medicalHistory;
    private String phone;
    private String notes;

    public Patient() {
        this.id = System.currentTimeMillis();
    }

    public Patient(String name,
                   int age,
                   Date dob,  // java.sql.Date
                   Gender gender,
                   Address address,
                   String medicalHistory,
                   String phone) {
        this.name = name;
        this.age = age;
        this.dob = dob;
        this.gender = gender;
        this.address = address;
        this.medicalHistory = medicalHistory;
        this.phone = phone;
        this.id = System.currentTimeMillis();
    }

    public void save() throws SQLException {
        Database.savePatient(this);
    }

    public static Patient load(long id) throws SQLException {
        return Database.loadPatient(id);
    }

    public static List<Patient> loadAll() throws SQLException {
        return Database.loadAllPatients();
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", medicalHistory='" + medicalHistory + '\'' +
                ", phone='" + phone + '\'' +
                ", dob=" + dob +
                ", gender=" + gender +
                ", address=" + address +
                '}';
    }

    // Getters and setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public LocalDate getDobAsLocalDate() {
        return dob != null ? dob.toLocalDate() : null; // Safeguard against null
    }

    public void setDob(LocalDate dob) {
        this.dob = Date.valueOf(dob); // Converts LocalDate to java.sql.Date
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
