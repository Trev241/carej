package me.caretaker.models;

import com.google.gson.Gson;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Patient implements Serializable {
    private final long id;
    private int age;

    private Gender gender;
    private Address address;
    private Date dob;

    private String name;
    private String medicalHistory;
    private String phone;
    private String notes;

    public Patient() {
        this.id = System.currentTimeMillis();
    }

    public Patient(
            String name,
            int age,
            Date dob,
            Gender gender,
            Address address,
            String medicalHistory,
            String phone
    ) {
        this.name = name;
        this.age = age;
        this.dob = dob;
        this.gender = gender;
        this.address = address;
        this.medicalHistory = medicalHistory;
        this.phone = phone;
        this.id = System.currentTimeMillis();
    }

    public void save() throws IOException {
        FileWriter writer = new FileWriter("data/patients/" + id + ".json");
        new Gson().toJson(this, writer);
        writer.close();
    }

    public static Patient load(long id) throws IOException {
        FileReader reader = new FileReader("data/patients/" + id + ".json");
        Patient patient = new Gson().fromJson(reader, Patient.class);
        reader.close();

        return patient;
    }

    public static List<Patient> loadAll() {
        File[] patientFiles = new File("data/patients").listFiles();
        List<Patient> patients = new ArrayList<Patient>();

        for (int i = 0; i < patientFiles.length; i++) {
            Patient patient = null;
            try {
                String fileName = patientFiles[i].getName();
                patient = load(Long.parseLong(fileName.substring(0, fileName.length() - 5)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            patients.add(patient);
        }

        return patients;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", medicalHistory='" + medicalHistory + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public long getId() {
        return this.id;
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

	public String getMedicalHistory() {
		return medicalHistory;
	}

	public String getPhone() {
		return phone;
	}

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Gender getGender() {
        return gender;
    }

    public void setDob(LocalDate dob) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(dob.getYear(), dob.getMonthValue() - 1, dob.getDayOfMonth());

        this.dob = calendar.getTime();
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Date getDob() {
        return dob;
    }

    public LocalDate getDobAsLocalDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime((dob == null) ? new Date() : dob);

        return LocalDate.of(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
    }
}