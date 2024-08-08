package me.caretaker.tasks;

import me.caretaker.models.Appointment;
import me.caretaker.repository.AppointmentRepository;

import java.sql.SQLException;
import java.util.List;

public class AppointmentRepositoryTask implements Runnable {
    private Appointment appointment;
    private long id;
    private OperationType operationType;
    private List<Appointment> appointments;

    @Override
    public void run() {
        switch (operationType) {
            case SAVE:
                try {
                    AppointmentRepository.save(appointment);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case ONE:
                try {
                    this.appointment = AppointmentRepository.one(id);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case ALL:
                try {
                    this.appointments = AppointmentRepository.all();
                } catch (SQLException e) {
                    throw new RuntimeException();
                }
        }
    }

    public void setOperation(OperationType operationType) {
        this.operationType = operationType;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
