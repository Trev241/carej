package me.caretaker;

import java.io.Serializable;

public class Appointment implements Serializable {
    private String patientName;
    private String date;
    private String time;
    private String reason;

    public Appointment(String patientName, String date, String time, String reason) {
        this.patientName = patientName;
        this.date = date;
        this.time = time;
        this.reason = reason;
    }

    public String getPatientName() {
		return patientName;
	}

	public String getDate() {
		return date;
	}

	public String getTime() {
		return time;
	}

	public String getReason() {
		return reason;
	}

    @Override
    public String toString() {
        return "Appointment{" +
                "patientName='" + patientName + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}