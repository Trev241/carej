package me.caretaker.models;

import me.caretaker.tasks.AppointmentRepositoryTask;
import me.caretaker.tasks.OperationType;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.sql.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Appointment {
    private long patient_id;
    private final long id;
    private AppointmentType reason;
    private Timestamp timestamp;

    static ExecutorService executorService = Executors.newFixedThreadPool(3);

    public Appointment() {
        this.id = System.currentTimeMillis();
    }

    public Appointment(long patient_id, AppointmentType reason, Timestamp timestamp) {
        this.patient_id = patient_id;
        this.reason = reason;
        this.timestamp = timestamp;
        this.id = System.currentTimeMillis();
    }

    public long getPatientID() {
        return patient_id;
    }

    public void setPatientID(long patient_id) {
        this.patient_id = patient_id;
    }

    public long getID() {
        return id;
    }

    public AppointmentType getReason() {
        return reason;
    }

    public void setReason(AppointmentType reason) {
        this.reason = reason;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setDateAndTime(java.sql.Date date, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new java.util.Date(date.getTime())); // Convert java.sql.Date to java.util.Date
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        this.timestamp = new Timestamp(calendar.getTimeInMillis());
    }

    public int getHour() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.timestamp);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }
    public Date getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.timestamp);
        return new Date(calendar.getTimeInMillis());
    }

    public int getMinute() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.timestamp);
        return calendar.get(Calendar.MINUTE);
    }

    public void save() throws SQLException {
//        AppointmentRepository.save(this);
        AppointmentRepositoryTask writeTask = new AppointmentRepositoryTask();
        writeTask.setAppointment(this);
        writeTask.setOperation(OperationType.SAVE);

        executorService.submit(writeTask);
    }

    public static Appointment load(long id) throws SQLException {
//        return AppointmentRepository.one(id);
        AppointmentRepositoryTask readTask = new AppointmentRepositoryTask();
        readTask.setId(id);
        readTask.setOperation(OperationType.ONE);

        Future<Appointment> future = (Future<Appointment>) executorService.submit(readTask);
        Appointment appointment = null;
        try {
            future.get();
            appointment = readTask.getAppointment();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return appointment;
    }
}
