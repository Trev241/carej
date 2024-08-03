package me.caretaker.models;

public class Address {
    public final String street;
    public final String city;
    public final String postalCode;

    public Address() {
        this(null, null, null);
    }

    public Address(String street, String city, String postalCode) throws IllegalArgumentException {
        // Initializing street
        this.street = street;

        if (postalCode != null) {
            if (postalCode.length() != 6)
                throw new IllegalArgumentException("Postal code does not meet the required length");

            postalCode = postalCode.toUpperCase();

            // Initializing postal code
            for (int i = 0; i < postalCode.length(); i++) {
                char c = postalCode.charAt(i);
                if ((i % 2 == 0 && Character.isDigit(c)) || (i % 2 == 1 && Character.isLetter(c)))
                    throw new IllegalArgumentException("Postal code does not comply with format A1A 1A1");
            }
        }
        this.postalCode = postalCode;

        // Initializing city
        if (city != null) {
            for (int i = 0; i < city.length(); i++)
                if (Character.isDigit(city.charAt(i)))
                    throw new IllegalArgumentException("City cannot contain digits");
        }

        this.city = city;
    }

    @Override
    public String toString() {
        return this.street + "|" + this.city + "|" + this.postalCode;
    }
}
