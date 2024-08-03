package me.caretaker.models;

public enum Gender {
    MALE, FEMALE, OTHER, UNKNOWN;

    public static Gender fromString(String genderStr) {
        if (genderStr == null) {
            return UNKNOWN;
        }
        try {
            return Gender.valueOf(genderStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}

