package me.caretaker;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Patient implements Serializable {
    private long id;
    private String name;
    private int age;
    private Gender gender;
    private Address address;
    private String medicalHistory;
    private String phone;
    private String notes;

    public Patient() {
    }

    public Patient(String name, int age, Gender gender, Address address, String medicalHistory, String phone) {
        this.name = name;
        this.age = age;
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

	public int getAge() {
		return age;
	}

	public String getMedicalHistory() {
		return medicalHistory;
	}

	public String getPhone() {
		return phone;
	}

    public Address getAddress() {
        return address;
    }

    public Gender getGender() {
        return gender;
    }
}