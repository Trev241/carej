package me.caretaker;

import java.io.Serializable;

public class Patient implements Serializable {
    private String name;
    private int age;
    private String medicalHistory;
    private String contactInfo;

    public Patient(String name, int age, String medicalHistory, String contactInfo) {
        this.name = name;
        this.age = age;
        this.medicalHistory = medicalHistory;
        this.contactInfo = contactInfo;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", medicalHistory='" + medicalHistory + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                '}';
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

	public String getContactInfo() {
		return contactInfo;
	}
}