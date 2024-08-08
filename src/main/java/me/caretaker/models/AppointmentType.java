package me.caretaker.models;

public enum AppointmentType {
    CONSULTATION,
    FOLLOW_UP,
    EMERGENCY,
    OTHER;

    @Override
    public String toString() {
        // Customize the display name if needed
        switch (this) {
            case CONSULTATION:
                return "CONSULTATION";
            case FOLLOW_UP:
                return "FOLLOW_UP";
            case EMERGENCY:
                return "EMERGENCY";
            case OTHER:
                return "OTHER";
            default:
                throw new IllegalArgumentException();
        }
    }
}
