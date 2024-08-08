package me.caretaker.tasks;

import me.caretaker.models.Patient;
import me.caretaker.repository.PatientRepository;

import java.sql.SQLException;
import java.util.List;

public class PatientRepositoryTask implements Runnable {
    private Patient patient;
    private long id;
    private OperationType operationType;
    private List<Patient> patients;

    @Override
    public void run() {
        switch (operationType) {
            case SAVE:
                try {
                    PatientRepository.save(patient);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case ONE:
                try {
                    this.patient = PatientRepository.one(id);
                    System.out.println(this.patient.getAddress());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            case ALL:
                try {
                    this.patients = PatientRepository.all();
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

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Patient getPatient() {
        return patient;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
