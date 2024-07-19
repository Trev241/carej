package me.caretaker.models;

public enum AppointmentType {
    CONSULTATION,
    FOLLOW_UP,
    EMERGENCY,
    OTHER;

    @Override
    public String toString() {
        // Customize the display name if needed
        switch(this) {
            case CONSULTATION: return "Consultation";
            case FOLLOW_UP: return "Follow-up";
            case EMERGENCY: return "Emergency";
            case OTHER: return "Other";
            default: throw new IllegalArgumentException();
        }
    }
}
